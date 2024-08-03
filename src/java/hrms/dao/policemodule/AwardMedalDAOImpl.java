/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.policemodule;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.common.FileAttribute;
import hrms.model.policemodule.AwardMedalDifferentRanks;
import hrms.model.policemodule.AwardMedalListForm;
import hrms.model.policemodule.EmployeeDetailsForRank;
import hrms.model.policemodule.InvestigatedCaseList;
import hrms.model.policemodule.NonSrCaseList;
import hrms.model.policemodule.SRCaseList;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
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
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Surendra
 */
public class AwardMedalDAOImpl implements AwardMedalDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    @Resource(name = "repodataSource")
    protected DataSource repodataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setRepodataSource(DataSource repodataSource) {
        this.repodataSource = repodataSource;
    }

    @Override
    public List getAwardTypeList() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();

        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select reward_type_id, reward_type from g_reward_type where is_active='Y' order by reward_type");
            rs = ps.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setLabel(rs.getString("reward_type"));
                so.setValue(rs.getString("reward_type_id"));
                li.add(so);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    @Override
    public List getEmployeeListGpfNowise(String gpfno, String awardYear, String awardId, String awardOccasion) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();

        try {
            con = dataSource.getConnection();

            ps = con.prepareStatement(" select emp_mast.emp_id, emp_mast.gpf_no, "
                    + " ARRAY_TO_STRING(ARRAY[emp_mast.INITIALS, emp_mast.F_NAME, emp_mast.M_NAME, emp_mast.L_NAME], ' ') full_name, "
                    + " emp_mast.dob, emp_mast.dos, emp_mast.gender, emp_mast.category, "
                    + " (SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') FATHERS_NAME "
                    + " FROM EMP_RELATION WHERE RELATION='FATHER' AND EMP_ID=EMP_MAST.EMP_ID) FATHERS_NAME "
                    + " from emp_mast "
                    + " left outer join "
                    + " (select gpf_no from police_award_form where gpf_no=? and award_medal_year=? and award_medal_type_id=? and award_occasion=?) pnei "
                    + " on emp_mast.gpf_no=pnei.gpf_no "
                    + " where emp_mast.gpf_no=? "
                    + " and pnei.gpf_no is null");
            ps.setString(1, gpfno);
            ps.setInt(2, Integer.parseInt(awardYear));
            ps.setString(3, awardId);
            ps.setString(4, awardOccasion);
            ps.setString(5, gpfno);
            rs = ps.executeQuery();
            while (rs.next()) {
                EmployeeDetailsForRank emp = new EmployeeDetailsForRank();
                emp.setEmpId(rs.getString("emp_id"));
                emp.setGpfno(rs.getString("gpf_no"));
                emp.setEmpName(rs.getString("full_name"));
                emp.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                emp.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                emp.setFathersName(rs.getString("FATHERS_NAME"));
                li.add(emp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    @Override
    public void addEmployeeForAward(String gpfno, String awardName, String awardYear, String loginoffcode, String awardOccasion) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

            ps = con.prepareStatement("insert into police_award_form (emp_id, full_name, dob, designation,doe_gov,award_medal_type_id, award_medal_name, "
                    + "award_medal_year, gpf_no, off_code, off_name, gender, father_name, age_in_year, age_in_month,award_occasion,total_police_service_years,total_police_service_months,total_police_service_days,category,religion,PROPERTY_SUBMITTED_ON_HRMS,mobile) "
                    + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");

            ps2 = con.prepareStatement("select emp_id, gpf_no, dob, doe_gov, gender,mobile, "
                    + "	ARRAY_TO_STRING(ARRAY[emp_mast.INITIALS, emp_mast.F_NAME, emp_mast.M_NAME, emp_mast.L_NAME], ' ') full_name, "
                    + " (SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') FATHERS_NAME \n"
                    + " FROM EMP_RELATION WHERE RELATION='FATHER' AND EMP_ID=EMP_MAST.EMP_ID) fathername, \n"
                    + " (SELECT submitted_on FROM property_statement_list WHERE EMP_ID=EMP_MAST.EMP_ID and financial_year='2023' and status_id=1) PROPERTY_SUBMITTED_ON_HRMS,"
                    + "	cur_off_code, off_name_abbr,post, category, g_religion.religion from emp_mast  "
                    + "	inner join police_office_list_data on emp_mast.cur_off_code=police_office_list_data.police_off_code "
                    + " inner join g_spc on emp_mast.cur_spc=g_spc.spc "
                    + "	inner join g_post on g_spc.gpc=g_post.post_code "
                    + " LEFT OUTER JOIN g_religion ON EMP_MAST.religion=g_religion.int_religion_id "
                    + "	where emp_mast.gpf_no=? ");
            ps2.setString(1, gpfno);
            rs = ps2.executeQuery();
            while (rs.next()) {

                String dob = CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("dob"));
                Date date = formatter.parse(dob);
                System.out.println("dob is **" +dob);
                //Converting obtained Date object to LocalDate object
                Instant instant = date.toInstant();
                ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
                LocalDate givenDate1 = zone.toLocalDate();

                Date date2 = formatter.parse("15-AUG-2024");
                if (awardName.equals("09")) {
                    date2 = formatter.parse("15-AUG-2024");
                }
                //Converting obtained Date object to LocalDate object
                Instant instant2 = date2.toInstant();
                ZonedDateTime zone2 = instant2.atZone(ZoneId.systemDefault());
                LocalDate nowDate1 = zone2.toLocalDate();
                
                
                Period period1 = Period.between(givenDate1, nowDate1);
               
                String doa = CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("doe_gov"));
                Date date3 = formatter.parse(doa);
                
                //Converting obtained Date object to LocalDate object
                instant = date3.toInstant();
                zone = instant.atZone(ZoneId.systemDefault());
                LocalDate givenDate2 = zone.toLocalDate();

                Date date4 = formatter.parse("15-AUG-2024");

                //Converting obtained Date object to LocalDate object
                Instant instant3 = date3.toInstant();
                ZonedDateTime zone3 = instant3.atZone(ZoneId.systemDefault());
                LocalDate nowDate2 = zone2.toLocalDate();

                //Calculating the difference between given date to current date.
                Period period2 = Period.between(givenDate2, nowDate2);

                ps.setString(1, rs.getString("emp_id"));
                ps.setString(2, rs.getString("full_name"));
                ps.setTimestamp(3, rs.getTimestamp("dob"));
                ps.setString(4, rs.getString("post"));
                ps.setTimestamp(5, rs.getTimestamp("doe_gov"));
                ps.setString(6, awardName);
                ps.setString(7, "");
                ps.setInt(8, Integer.parseInt(awardYear));
                ps.setString(9, rs.getString("gpf_no"));
                ps.setString(10, loginoffcode);
                ps.setString(11, rs.getString("off_name_abbr"));
                ps.setString(12, rs.getString("gender"));
                ps.setString(13, rs.getString("fathername"));
                ps.setInt(14, period1.getYears());
                ps.setInt(15, period1.getMonths());
                ps.setString(16, awardOccasion);
                ps.setInt(17, period2.getYears());
                ps.setInt(18, period2.getMonths());
                ps.setInt(19, period2.getDays());
                ps.setString(20, rs.getString("category"));
                ps.setString(21, rs.getString("religion"));
                ps.setTimestamp(22, rs.getTimestamp("PROPERTY_SUBMITTED_ON_HRMS"));
                ps.setString(23, rs.getString("mobile"));
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(ps2);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getAwardeeList(String offcode, String awardMedalId, String awardYear, String awardOccasion) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List li = new ArrayList();
        AwardMedalListForm award = new AwardMedalListForm();
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT award_medal_id, emp_id, full_name, dob, designation, doa, "
                    + " award_medal_type_id, award_medal_name, award_medal_year, gpf_no, off_code, off_name, range_office_name, completed_status, submitted_on,"
                    + " submitted_range_off, range_submitted_on, dg_off_code"
                    + " FROM police_award_form "
                    + " left outer join police_range_office_list prol on police_award_form.submitted_range_off=prol.range_office_id::TEXT "
                    + " where off_code=? and award_medal_type_id=? and award_medal_year=? and award_occasion=?"
                    + " order by full_name");
            ps.setString(1, offcode);
            ps.setString(2, awardMedalId);
            ps.setInt(3, Integer.parseInt(awardYear));
            ps.setString(4, awardOccasion);
            rs = ps.executeQuery();
            while (rs.next()) {
                award = new AwardMedalListForm();
                award.setRewardMedalId(rs.getInt("award_medal_id") + "");
                award.setEmpId(rs.getString("emp_id"));
                award.setFullname(rs.getString("full_name"));
                award.setDob(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("dob")));
                award.setDesignation(rs.getString("designation"));
                award.setDoa(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("doa")));
                award.setGpfNo(rs.getString("gpf_no"));
                award.setSubmittedOn(CommonFunctions.getFormattedOutputDatAndTime(rs.getTimestamp("submitted_on")));
                award.setSubmittedRangeOff(rs.getString("range_office_name"));
                award.setCompletedStatus(rs.getString("completed_status"));
                li.add(award);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public AwardMedalListForm getAwardeeData(String rewardId, String profilePhotoPath) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List li = new ArrayList();
        AwardMedalListForm award = new AwardMedalListForm();
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT award_medal_id, emp_id, full_name, dob, designation, doa,doe_gov, "
                    + " award_medal_type_id, award_medal_name,reward_type, award_medal_year, gpf_no, "
                    + " police_award_form.off_code, off_name_abbr, "
                    + " doc_present_rank, doj_dist_est,  money_reward,  commendation, "
                    + " gs_mark, appreciation, aar, other_award,punishment_major1,punishment_major2,punishment_major3,punishment_major4,punishment_major5, "
                    + " punishment_minor1,punishment_minor2,punishment_minor3,punishment_minor4,punishment_minor5, award_medal_previous_year, "
                    + " award_medal_rank, award_medal_posting_place, previously_awarded ,  brief_note, dpcifany, disc_details, "
                    + " recommend_by_dist, recommend_by_range, not_recommend_reason_by_range, completed_status, submitted_on,"
                    + " submitted_range_off, range_submitted_on, dg_off_code, gender, father_name, age_in_year, age_in_month, "
                    + " initial_appoint_year, initial_appointment_rank, initial_appointment_cadre , "
                    + " date_since_present_rank , present_posting, posting_address, punishment_details, punishment_years , pending_enquiry_details, "
                    + " court_case_year , court_case_charge, court_case_status, "
                    + " acr_year1 , acr_year1_grading , acr_year2 , acr_year2_grading , acr_year3 , acr_year3_grading ,"
                    + " acr_year4 , acr_year4_grading , acr_year5 , acr_year5_grading, further_information_by_range,"
                    + "first_name,middle_name,last_name,father_first_name,father_second_name,father_last_name,category,"
                    + " initial_appoint_date,initial_appoint_service,initial_appoint_category,total_police_service_years,total_police_service_months,total_police_service_days,"
                    + " present_posting_designation,present_posting_address,present_posting_place,present_posting_pin,present_posting_date,"
                    + " if_deputation,deputation_doj,rewards_no,rewards_total_amt,rewards_cash_awards,rewards_commendation,rewards_appreciation,rewards_good_service,rewards_any_other,"
                    + " medal_meritorious_service_year,medal_meritorious_occassion,punishment_penalty_details,punishment_penalty_year,punishment_penalty_order_no,punishment_penalty_order_date,"
                    + " medical_category,pending_enquiry,if_disciplinary_proceeding,dpc_year,dpc_nature,dpc_status,court_case_pending_year,court_case_pending_details,court_case_pending_status,"
                    + " acr_grading_1,acr_grading_2,acr_grading_3,acr_grading_4,acr_grading_5,acr_grading_6,acr_grading_7,acr_grading_8,acr_grading_9,acr_grading_10,acr_grading_11,"
                    + " acr_grading_12,acr_grading_13,acr_grading_14,acr_grading_os,acr_grading_vg,acr_grading_good,acr_grading_avg,acr_grading_nic,acr_grading_adv,acr_grading_ms,"
                    + " acr_grading_na,email,mobile,brief_description,pending_enquiry_note,rewards_cash_awards_amt,rewards_commendation_amt,rewards_appreciation_amt,rewards_good_service_amt,rewards_any_other_desc,ccr_remarks_one,ccr_remarks_two,ccr_remarks_three,award_occasion,"
                    + " criminal_details,chargesheet_details,proceeding_details,criminalcase_ifany,chargesheet_ifany,rank_designation_withpresent_posting,other_range_information_governor,is_governor_medal_earlier,"
                    + " disciplinary_proceeding_details,proceeding_meeting_ifany,dgps_other_information_district,punishment_major_detail,punishment_minor_detail,"
                    + " punishment_minor_penalty_details,punishment_minor_penalty_year,punishment_minor_penalty_order_no,punishment_minor_penalty_order_date,if_meritorious_service_awarded,deputation_detail,"
                    + " if_major_punishment,if_minor_punishment,penaltydetails_minor,penaltydetails_major,category,religion,ccr_reference,property_submitted_if_any,property_submitted_on_hrms,property_submitted_on_hrms_byofficer, "
                    + " punishment_minor_penaltypreeciding_details,punishment_minor_penaltypreeciding_number,punishment_major_penaltypreeciding_details,punishment_major_penaltypreeciding_number,punishment_major, "
                    + " punishment_major_dgpdisc,punishment_minor_dgpdisc,punishment_minor,submitted_on,country,nationality,state,acrgrading_detail,acrcopy_ifany FROM police_award_form "
                    + " inner join g_reward_type on police_award_form.award_medal_type_id=g_reward_type.reward_type_id "
                    + " inner join police_office_list_data on police_award_form.off_code=police_office_list_data.police_off_code "
                    + " where award_medal_id=? ");
            ps.setInt(1, Integer.parseInt(rewardId));

            rs = ps.executeQuery();
            if (rs.next()) {
                award = new AwardMedalListForm();
                award.setRewardMedalId(rs.getInt("award_medal_id") + "");
                award.setEmpId(rs.getString("emp_id"));
                award.setFullname(rs.getString("full_name"));
                award.setDob(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("dob")));
                award.setDesignation(rs.getString("designation"));
                award.setDoa(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("doa")));
                award.setInitialAppointDate(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("doe_gov")));
                award.setGpfNo(rs.getString("gpf_no"));
                award.setAwardMedalName(rs.getString("reward_type"));
                award.setOffName(rs.getString("off_name_abbr"));
                award.setOffCode(rs.getString("off_code"));
                award.setAwardMedalTypeId(rs.getString("award_medal_type_id") + "");
                award.setAwardYear(rs.getInt("award_medal_year") + "");
                award.setDocPresentRank(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("doc_present_rank")));
                award.setDojDistEst(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("doj_dist_est")));
                award.setMoneyReward(rs.getInt("money_reward") + "");
                award.setCommendation(rs.getInt("commendation") + "");
                award.setGsMark(rs.getInt("gs_mark") + "");
                award.setAppreciation(rs.getInt("appreciation") + "");
                award.setAar(rs.getInt("aar") + "");
                award.setOtherReward(rs.getInt("other_award") + "");
                award.setPunishmentmajor1(rs.getString("punishment_major1"));
                award.setPunishmentmajor2(rs.getString("punishment_major2"));
                award.setPunishmentmajor3(rs.getString("punishment_major3"));
                award.setPunishmentmajor4(rs.getString("punishment_major4"));
                award.setPunishmentmajor5(rs.getString("punishment_major5"));
                award.setPunishmentminor1(rs.getString("punishment_minor1"));
                award.setPunishmentminor2(rs.getString("punishment_minor2"));
                award.setPunishmentminor3(rs.getString("punishment_minor3"));
                award.setPunishmentminor4(rs.getString("punishment_minor4"));
                award.setPunishmentminor5(rs.getString("punishment_minor5"));
                award.setAwardMedalPreviousYear(rs.getString("award_medal_previous_year"));
                award.setAwardMedalRank(rs.getString("award_medal_rank"));
                award.setAwardMedalPostingPlace(rs.getString("award_medal_posting_place"));
                award.setPrevAwardCmb(rs.getString("previously_awarded"));
                award.setBriefNote(rs.getString("brief_note"));
                award.setDpcifany(rs.getString("dpcifany"));
                award.setDiscDetails(rs.getString("disc_details"));
                award.setRecommendStatusofDist(rs.getString("recommend_by_dist"));
                award.setRecommendStatusofRange(rs.getString("recommend_by_range"));
                award.setReasonFornotRecommend(rs.getString("not_recommend_reason_by_range"));
                award.setSubmittedOn(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("submitted_on")));
                award.setRangeSubmittedOn(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("range_submitted_on")));
                award.setCompletedStatus(rs.getString("completed_status"));
                award.setGender(rs.getString("gender"));
                award.setFathername(rs.getString("father_name"));
                award.setAgeInYear(rs.getInt("age_in_year"));
                award.setAgeInMonth(rs.getInt("age_in_month"));

                award.setInitialAppointYear(rs.getString("initial_appoint_year"));
                award.setInitialAppointRank(rs.getString("initial_appointment_rank"));
                award.setInitialAppointCadre(rs.getString("initial_appointment_cadre"));
                award.setDatePresentRank(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("date_since_present_rank")));
                award.setPresentPosting(rs.getString("present_posting"));
                award.setPostingAddress(rs.getString("posting_address"));
                award.setPunishmentDetails(rs.getString("punishment_details"));
                award.setPunishmentYears(rs.getString("punishment_years"));
                award.setPendingEnquiryDetails(rs.getString("pending_enquiry_details"));
                award.setCourtCaseYear(rs.getString("court_case_year"));
                award.setCourtCaseDetails(rs.getString("court_case_charge"));
                award.setCourtCaseStatus(rs.getString("court_case_status"));

                award.setAcrYear1(rs.getInt("award_medal_year") + "");
                award.setAcrYear1Grading(rs.getString("acr_year1_grading"));
                award.setAcrYear2((rs.getInt("award_medal_year") - 1) + "");
                award.setAcrYear2Grading(rs.getString("acr_year2_grading"));
                award.setAcrYear3((rs.getInt("award_medal_year") - 2) + "");
                award.setAcrYear3Grading(rs.getString("acr_year3_grading"));
                award.setAcrYear4((rs.getInt("award_medal_year") - 3) + "");
                award.setAcrYear4Grading(rs.getString("acr_year4_grading"));
                award.setAcrYear5((rs.getInt("award_medal_year") - 4) + "");
                award.setAcrYear5Grading(rs.getString("acr_year5_grading"));
                award.setFurtherInfoByRange(rs.getString("further_information_by_range"));

                award.setFirstName(rs.getString("first_name"));
                award.setMiddleName(rs.getString("middle_name"));
                award.setLastName(rs.getString("last_name"));

                award.setFatherfirstname(rs.getString("father_first_name"));
                award.setFathermiddlename(rs.getString("father_second_name"));
                award.setFatherlastname(rs.getString("father_last_name"));

                award.setSltCategory(rs.getString("category"));

                //award.setInitialAppointDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("initial_appoint_date")));
                award.setInitialAppointService(rs.getString("initial_appoint_service"));
                award.setInitialAppointCategory(rs.getString("initial_appoint_category"));

                award.setTotalpoliceyears(rs.getString("total_police_service_years"));
                award.setTotalpolicemonths(rs.getString("total_police_service_months"));
                award.setTotalpolicedays(rs.getString("total_police_service_days"));
                /* award.setTotalpoliceyears(rs.getInt("total_police_service_years"));
                 award.setTotalpolicemonths(rs.getInt("total_police_service_months"));
                 award.setTotalpolicedays(rs.getInt("total_police_service_days"));*/

                award.setPresentPosting(rs.getString("present_posting_designation"));
                award.setPresentPostingPlace(rs.getString("present_posting_place"));
                award.setPresentPostingPIN(rs.getString("present_posting_pin"));
                award.setPresentPostingDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("present_posting_date")));
                award.setPostingAddress(rs.getString("present_posting_address"));

                award.setSltDeputation(rs.getString("if_deputation"));
                award.setDeputationDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("deputation_doj")));

                award.setRewardsTotalNo(rs.getString("rewards_no"));
                award.setRewardsAmt(rs.getString("rewards_total_amt"));
                award.setCashAwardsNo(rs.getString("rewards_cash_awards"));
                award.setPresidentcommendation(rs.getString("rewards_commendation"));
                award.setPresidentappreciation(rs.getString("rewards_appreciation"));
                award.setGoodServiceEntries(rs.getString("rewards_good_service"));
                award.setAnyOtherRewards(rs.getString("rewards_any_other"));

                award.setMeritoriousYear(rs.getString("medal_meritorious_service_year"));
                award.setSltOccasion(rs.getString("medal_meritorious_occassion"));
                award.setPenaltydetails(rs.getString("punishment_penalty_details"));
                award.setPenaltyyear(rs.getString("punishment_penalty_year"));
                award.setPenaltyOrderNo(rs.getString("punishment_penalty_order_no"));
                award.setPenaltyOrderDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("punishment_penalty_order_date")));

                award.setMedicalCategory(rs.getString("medical_category"));
                award.setIfenquirypending(rs.getString("pending_enquiry"));
                award.setIfdisciplinaryProceedingpending(rs.getString("if_disciplinary_proceeding"));

                award.setDpcyear(rs.getString("dpc_year"));
                award.setDpcnatureallegation(rs.getString("dpc_nature"));
                award.setDpcpresentstatus(rs.getString("dpc_status"));

                award.setCourtCasePendingYear(rs.getString("court_case_pending_year"));
                award.setCourtCasePendingDetails(rs.getString("court_case_pending_details"));
                award.setCourtCasePendingStatus(rs.getString("court_case_pending_status"));

                award.setAcrGrading1(rs.getString("acr_grading_1"));
                award.setAcrGrading2(rs.getString("acr_grading_2"));
                award.setAcrGrading3(rs.getString("acr_grading_3"));
                award.setAcrGrading4(rs.getString("acr_grading_4"));
                award.setAcrGrading5(rs.getString("acr_grading_5"));
                award.setAcrGrading6(rs.getString("acr_grading_6"));
                award.setAcrGrading7(rs.getString("acr_grading_7"));
                award.setAcrGrading8(rs.getString("acr_grading_8"));
                award.setAcrGrading9(rs.getString("acr_grading_9"));
                award.setAcrGrading10(rs.getString("acr_grading_10"));
                award.setAcrGrading11(rs.getString("acr_grading_11"));
                award.setAcrGrading12(rs.getString("acr_grading_12"));
                award.setAcrGrading13(rs.getString("acr_grading_13"));
                award.setAcrGrading14(rs.getString("acr_grading_14"));
                award.setAcrGradingOS(rs.getString("acr_grading_os"));
                award.setAcrGradingVS(rs.getString("acr_grading_vg"));
                award.setAcrGradingGood(rs.getString("acr_grading_good"));
                award.setAcrGradingAvg(rs.getString("acr_grading_avg"));
                award.setAcrGradingNic(rs.getString("acr_grading_nic"));
                award.setAcrGradingAdv(rs.getString("acr_grading_adv"));
                award.setAcrGradingMs(rs.getString("acr_grading_ms"));
                award.setAcrGradingNa(rs.getString("acr_grading_na"));

                award.setEmail(rs.getString("email"));
                award.setMobile(rs.getString("mobile"));
                award.setBriefdescription(rs.getString("brief_description"));
                award.setEnquirypending(rs.getString("pending_enquiry_note"));

                award.setCashAwardsAmt(rs.getString("rewards_cash_awards_amt"));
                award.setPresidentcommendationAmt(rs.getString("rewards_commendation_amt"));
                award.setPresidentappreciationAmt(rs.getString("rewards_appreciation_amt"));
                award.setGoodServiceEntriesAmt(rs.getString("rewards_good_service_amt"));
                award.setAnyOtherRewardsDesc(rs.getString("rewards_any_other_desc"));

                award.setCcrolloneremarks(rs.getString("ccr_remarks_one"));
                award.setCcrolltworemarks(rs.getString("ccr_remarks_two"));
                award.setCcrollthreeremarks(rs.getString("ccr_remarks_three"));

                award.setSltAwardOccasion(rs.getString("award_occasion"));
                award.setAnnexure2Details(rs.getString("criminal_details"));
                award.setAnnexure3Details(rs.getString("chargesheet_details"));
                award.setProceedingDetails(rs.getString("proceeding_details"));
                award.setCriminalcaseifany(rs.getString("criminalcase_ifany"));
                award.setChargesheetedifany(rs.getString("chargesheet_ifany"));
                award.setMeetingProceeding(rs.getString("proceeding_meeting_ifany"));
                award.setPresentPostingRankandDesignation(rs.getString("rank_designation_withpresent_posting"));
                award.setApprovGovernorMedalAwarded(rs.getString("is_governor_medal_earlier"));
                award.setOtherInformationofRange(rs.getString("other_range_information_governor"));
                award.setDisciplinaryProceedingpending(rs.getString("disciplinary_proceeding_details"));
                award.setOtherInformationFromDistrictDgps(rs.getString("dgps_other_information_district"));
                award.setPunishmentMajorDetails(rs.getString("punishment_major_detail"));
                award.setPunishmentMinorDetails(rs.getString("punishment_minor_detail"));
                award.setMinorpenaltydetails(rs.getString("punishment_minor_penalty_details"));
                award.setMinorpenaltyyear(rs.getString("punishment_minor_penalty_year"));
                award.setMinorpenaltyOrderNo(rs.getString("punishment_minor_penalty_order_no"));
                award.setMinorpenaltyOrderDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("punishment_minor_penalty_order_date")));
                award.setApprovMeritoriousServiceAwarded(rs.getString("if_meritorious_service_awarded"));
                award.setDeputationDetail(rs.getString("deputation_detail"));
                award.setIfMajorPunishment(rs.getString("if_major_punishment"));
                award.setIfMinorPunishment(rs.getString("if_minor_punishment"));
                award.setPenaltydetailsMinor(rs.getString("penaltydetails_minor"));
                award.setPenaltydetailsMojor(rs.getString("penaltydetails_major"));
                award.setCategory(rs.getString("category"));
                award.setReligious(rs.getString("religion"));
                award.setCcrRefenrence(rs.getString("ccr_reference"));
                award.setPropertyStatementSubmittedifAny(rs.getString("property_submitted_if_any"));
                award.setDateofPropertySubmittedByHRMS(CommonFunctions.getFormattedOutputDate1(rs.getDate("property_submitted_on_hrms")));
                award.setDateofPropertySubmittedByOfficer(CommonFunctions.getFormattedOutputDate1(rs.getDate("property_submitted_on_hrms_byofficer")));

                award.setPunishmentMinorpreecidingDetails(rs.getString("punishment_minor_penaltypreeciding_details"));
                award.setPunishmentMinorpreeciding(rs.getString("punishment_minor_penaltypreeciding_number"));
                award.setPunishmentMajorpreecidingDetails(rs.getString("punishment_major_penaltypreeciding_details"));
                award.setPunishmentMajorpreeciding(rs.getString("punishment_major_penaltypreeciding_number"));
                award.setPunishmentMinordgp(rs.getString("punishment_minor_dgpdisc"));
                award.setPunishmentMajordgp(rs.getString("punishment_major_dgpdisc"));
                award.setSubmittedOn(rs.getString("submitted_on"));
                award.setCountry(rs.getString("country"));
                award.setNationality(rs.getString("nationality"));
                award.setState(rs.getString("state"));
                award.setAcrGradingDetail(rs.getString("acrgrading_detail"));
                award.setAcrCopyifAny(rs.getString("acrcopy_ifany"));
                

            }

            DataBaseFunctions.closeSqlObjects(rs, ps);

            List differentRankList = new ArrayList();

            String sql = "SELECT * FROM police_award_form_different_ranks where award_medal_id=? order by rank_id";
            ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(award.getRewardMedalId()));
            rs = ps.executeQuery();
            while (rs.next()) {
                AwardMedalDifferentRanks amdr = new AwardMedalDifferentRanks();
                amdr.setRankid(rs.getString("rank_id"));
                amdr.setRankname(rs.getString("rank_name"));
                amdr.setRankdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("rank_date")));
                amdr.setRankyear(rs.getString("rank_year"));
                amdr.setRankmonth(rs.getString("rank_month"));
                amdr.setRankdays(rs.getString("rank_days"));
                differentRankList.add(amdr);
            }

            award.setDifferentRankList(differentRankList);
            ps = con.prepareStatement("select file_name, original_name, doc_name from police_award_doc where data_table_id=?");
            ps.setInt(1, Integer.parseInt(award.getRewardMedalId()));
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("doc_name").equalsIgnoreCase("DPC")) {
                    award.setOriginalFileName(rs.getString("original_name"));
                    award.setDiskFilename(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("CCROLL")) {
                    award.setOriginalFileNameCCROll(rs.getString("original_name"));
                    award.setDiskFilenameCCROll(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("SB")) {
                    award.setOriginalFileNameSB(rs.getString("original_name"));
                    award.setDiskFilenameSB(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("SRD")) {
                    award.setOriginalFileNamesrDocument(rs.getString("original_name"));
                    award.setDiskFilenamesrDocument(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("NSRD")) {
                    award.setOriginalFileNamenonSrDocument(rs.getString("original_name"));
                    award.setDiskFilenamenonSrDocument(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("PROC")) {
                    award.setOriginalFileNameProccdingDocument(rs.getString("original_name"));
                    award.setDiskFilenameProccdingDocument(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("CRTR")) {
                    award.setOriginalFileNamecertificateDoc(rs.getString("original_name"));
                    award.setDiskFilenamecertificateDoc(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("ICD")) {
                    award.setOriginalFileNameintegrityCertificateDoc(rs.getString("original_name"));
                    award.setDiskFilenameintegrityCertificateDoc(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("MED")) {
                    award.setOriginalFileNamemedicalcertificateDoc(rs.getString("original_name"));
                    award.setDiskFilenamemedicalcertificateDoc(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("PUN")) {
                    award.setOriginalFileNamepunishmentDoc(rs.getString("original_name"));
                    award.setDiskFilenamepunishmentDoc(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("ENQ")) {
                    award.setOriginalFileNameenquiryDoc(rs.getString("original_name"));
                    award.setDiskFilenameenquiryDoc(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("DISCPC")) {
                    award.setOriginalFileNamedpcDoc(rs.getString("original_name"));
                    award.setDiskFilenamedpcDoc(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("COURT")) {
                    award.setOriginalFileNamecourtCaseDoc(rs.getString("original_name"));
                    award.setDiskFilenamecourtCaseDoc(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("ACR1")) {
                    award.setOriginalFileNameAcrGrading1Doc(rs.getString("original_name"));
                    award.setDiskFilenameAcrGrading1Doc(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("ACR2")) {
                    award.setOriginalFileNameAcrGrading2Doc(rs.getString("original_name"));
                    award.setDiskFilenameAcrGrading2Doc(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("ACR3")) {
                    award.setOriginalFileNameAcrGrading3Doc(rs.getString("original_name"));
                    award.setDiskFilenameAcrGrading3Doc(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("ACR4")) {
                    award.setOriginalFileNameAcrGrading4Doc(rs.getString("original_name"));
                    award.setDiskFilenameAcrGrading4Doc(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("ACR5")) {
                    award.setOriginalFileNameAcrGrading5Doc(rs.getString("original_name"));
                    award.setDiskFilenameAcrGrading5Doc(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("ACR6")) {
                    award.setOriginalFileNameAcrGrading6Doc(rs.getString("original_name"));
                    award.setDiskFilenameAcrGrading6Doc(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("ACR7")) {
                    award.setOriginalFileNameAcrGrading7Doc(rs.getString("original_name"));
                    award.setDiskFilenameAcrGrading7Doc(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("ACR8")) {
                    award.setOriginalFileNameAcrGrading8Doc(rs.getString("original_name"));
                    award.setDiskFilenameAcrGrading8Doc(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("ACR9")) {
                    award.setOriginalFileNameAcrGrading9Doc(rs.getString("original_name"));
                    award.setDiskFilenameAcrGrading9Doc(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("ACR10")) {
                    award.setOriginalFileNameAcrGrading10Doc(rs.getString("original_name"));
                    award.setDiskFilenameAcrGrading10Doc(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("ACR11")) {
                    award.setOriginalFileNameAcrGrading11Doc(rs.getString("original_name"));
                    award.setDiskFilenameAcrGrading11Doc(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("ACR12")) {
                    award.setOriginalFileNameAcrGrading12Doc(rs.getString("original_name"));
                    award.setDiskFilenameAcrGrading12Doc(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("ACR13")) {
                    award.setOriginalFileNameAcrGrading13Doc(rs.getString("original_name"));
                    award.setDiskFilenameAcrGrading13Doc(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("CCROLLOne")) {
                    award.setOriginalFileNameCCRollone(rs.getString("original_name"));
                    award.setDiskFilenameCCRollone(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("CCROLLTwo")) {
                    award.setOriginalFileNameCCRolltwo(rs.getString("original_name"));
                    award.setDiskFilenameCCRolltwo(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("CCROLLThree")) {
                    award.setOriginalFileNameCCRollthree(rs.getString("original_name"));
                    award.setDiskFilenameCCRollthree(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("MEDCAT")) {
                    award.setOriginalFileNameMedicalCategoryDoc(rs.getString("original_name"));
                    award.setDiskFilenameMedicalCategoryDoc(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("PerformaB")) {
                    award.setOriginalFileNameperformaBDoc(rs.getString("original_name"));
                    award.setDiskFilenameperformaBDoc(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("PerformaC")) {
                    award.setOriginalFileNameperformaCDoc(rs.getString("original_name"));
                    award.setDiskFilenameperformaCDoc(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("MAJORPUNISH")) {
                    award.setOriginalFileNameMajorPunishment(rs.getString("original_name"));
                    award.setDiskFilenameMajorPunishment(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("MINORPUNISH")) {
                    award.setOriginalFileNameMinorPunishment(rs.getString("original_name"));
                    award.setDiskFilenameMinorPunishment(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("MINORPUNISHPREEDIDING")) {
                    award.setOriginalFileNameMinorPunishmentpreeciding(rs.getString("original_name"));
                    award.setDiskFilenameMinorPunishmentpreeciding(rs.getString("file_name"));
                } else if (rs.getString("doc_name").equalsIgnoreCase("MAJORPUNISHPREECIDING")) {
                    award.setOriginalFileNameMajorPunishmentpreeciding(rs.getString("original_name"));
                    award.setDiskFilenameMajorPunishmentpreeciding(rs.getString("file_name"));
                }

            }
            String filename = award.getEmpId() + ".jpg";
            File file = new File(profilePhotoPath + filename);
            if (file.exists()) {
                award.setIsPhotoAvailable("Y");
            } else {
                award.setIsPhotoAvailable("N");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return award;
    }

    @Override
    public void updateAwardData(AwardMedalListForm award, String filePath) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            con = dataSource.getConnection();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            ps = con.prepareStatement("update police_award_form set doc_present_rank=?, doj_dist_est=?,  money_reward=?,  commendation=?, "
                    + "  gs_mark=?, appreciation=?, aar=?, punishment_major1=?, "
                    + " punishment_minor1=?, award_medal_previous_year=?, "
                    + "  award_medal_rank=?, award_medal_posting_place =?, previously_awarded=?, brief_note=?, dpcifany=?, disc_details=? , "
                    + " completed_status=?, recommend_by_dist=?,  "
                    + " initial_appoint_year=?, initial_appointment_rank=?, initial_appointment_cadre =?, "
                    + " date_since_present_rank=? , present_posting=?, posting_address=?, punishment_details=?, punishment_years=? , pending_enquiry_details=?, "
                    + " court_case_year=? , court_case_charge=?, court_case_status=?, "
                    + " acr_year1=? , acr_year1_grading=? , acr_year2=? , acr_year2_grading=? , acr_year3=? , acr_year3_grading =?,"
                    + " acr_year4=? , acr_year4_grading =?, acr_year5=? , acr_year5_grading=? , other_award=?, further_information_by_range=?,"
                    + " first_name=?,middle_name=?,last_name=?,father_first_name=?,father_second_name=?,father_last_name=?,category=?,"
                    + " doe_gov=?,initial_appoint_service=?,initial_appoint_category=?,total_police_service_years=?,total_police_service_months=?,total_police_service_days=?,"
                    + " present_posting_designation=?,present_posting_address=?,present_posting_place=?,present_posting_pin=?,present_posting_date=?,"
                    + " if_deputation=?,deputation_doj=?,rewards_no=?,rewards_total_amt=?,rewards_cash_awards=?,rewards_commendation=?,rewards_appreciation=?,rewards_good_service=?,rewards_any_other=?,"
                    + " medal_meritorious_service_year=?,medal_meritorious_occassion=?,punishment_penalty_details=?,punishment_penalty_year=?,punishment_penalty_order_no=?,punishment_penalty_order_date=?,"
                    + " medical_category=?,pending_enquiry=?,dpc_year=?,dpc_nature=?,dpc_status=?,court_case_pending_year=?,court_case_pending_details=?,court_case_pending_status=?,"
                    + " acr_grading_1=?,acr_grading_2=?,acr_grading_3=?,acr_grading_4=?,acr_grading_5=?,acr_grading_6=?,acr_grading_7=?,acr_grading_8=?,acr_grading_9=?,acr_grading_10=?,acr_grading_11=?,"
                    + " acr_grading_12=?,acr_grading_13=?,acr_grading_14=?,acr_grading_os=?,acr_grading_vg=?,acr_grading_good=?,acr_grading_avg=?,acr_grading_nic=?,acr_grading_adv=?,acr_grading_ms=?,"
                    + " acr_grading_na=?,email=?,mobile=?,brief_description=?,pending_enquiry_note=?,rewards_cash_awards_amt=?,rewards_commendation_amt=?,rewards_appreciation_amt=?,rewards_good_service_amt=?,rewards_any_other_desc=?,"
                    + " ccr_remarks_one=?,ccr_remarks_two=?,ccr_remarks_three=?,dgps_other_information_district=?,punishment_major_detail=?,punishment_minor_detail=?,"
                    + " punishment_minor_penalty_details=?,punishment_minor_penalty_year=?,punishment_minor_penalty_order_no=?,punishment_minor_penalty_order_date=?,if_meritorious_service_awarded=?,"
                    + " punishment_major2=?,punishment_major3=?,punishment_major4=?,punishment_major5=?,punishment_minor2=?,punishment_minor3=?,punishment_minor4=?,punishment_minor5=?,"
                    + " criminal_details=?,chargesheet_details=?,criminalcase_ifany=?,chargesheet_ifany=?,designation=?,proceeding_meeting_ifany=?,proceeding_details=?,ccr_reference=?,property_submitted_if_any=?, "
                    + " property_submitted_on_hrms=?,property_submitted_on_hrms_byofficer=?,punishment_minor_penaltypreeciding_details=?,punishment_minor_penaltypreeciding_number=?,punishment_major_penaltypreeciding_details=?,punishment_major_penaltypreeciding_number=?, "
                    + " punishment_major_dgpdisc=?,punishment_minor_dgpdisc=?,doa=?,country=?,nationality=?,state=?,acrgrading_detail=?,acrcopy_ifany=? where award_medal_id=?");
            if (award.getDocPresentRank() != null && !award.getDocPresentRank().equals("")) {
                ps.setTimestamp(1, new Timestamp(sdf.parse(award.getDocPresentRank()).getTime()));
            } else {
                ps.setTimestamp(1, null);
            }
            if (award.getDojDistEst() != null && !award.getDojDistEst().equals("")) {
                ps.setTimestamp(2, new Timestamp(sdf.parse(award.getDojDistEst()).getTime()));
            } else {
                ps.setTimestamp(2, null);
            }
            if (award.getMoneyReward() != null && !award.getMoneyReward().equals("")) {
                ps.setInt(3, Integer.parseInt(award.getMoneyReward()));
            } else {
                ps.setInt(3, 0);
            }
            if (award.getCommendation() != null && !award.getCommendation().equals("")) {
                ps.setInt(4, Integer.parseInt(award.getCommendation()));
            } else {
                ps.setInt(4, 0);
            }
            if (award.getGsMark() != null && !award.getGsMark().equals("")) {
                ps.setInt(5, Integer.parseInt(award.getGsMark()));
            } else {
                ps.setInt(5, 0);
            }
            if (award.getAppreciation() != null && !award.getAppreciation().equals("")) {
                ps.setInt(6, Integer.parseInt(award.getAppreciation()));
            } else {
                ps.setInt(6, 0);
            }
            if (award.getAar() != null && !award.getAar().equals("")) {
                ps.setInt(7, Integer.parseInt(award.getAar()));
            } else {
                ps.setInt(7, 0);
            }
            if (award.getPunishmentmajor1() != null && !award.getPunishmentmajor1().equals("")) {
                ps.setString(8, award.getPunishmentmajor1());
            } else {
                ps.setString(8, "");
            }

            if (award.getPunishmentminor1() != null && !award.getPunishmentminor1().equals("")) {
                ps.setString(9, award.getPunishmentminor1());
            } else {
                ps.setString(9, "");
            }
            ps.setString(10, award.getAwardMedalPreviousYear());
            ps.setString(11, award.getAwardMedalRank());
            ps.setString(12, award.getAwardMedalPostingPlace());
            ps.setString(13, award.getPrevAwardCmb());
            ps.setString(14, award.getBriefNote());
            ps.setString(15, award.getDpcifany());
            ps.setString(16, award.getDiscDetails());
            ps.setString(17, "Y");
            ps.setString(18, award.getRecommendStatusofDist());
            if (award.getInitialAppointYear() != null && !award.getInitialAppointYear().equals("")) {
                ps.setInt(19, Integer.parseInt(award.getInitialAppointYear()));
            } else {
                ps.setInt(19, 0);
            }

            ps.setString(20, award.getInitialAppointRank());
            ps.setString(21, award.getInitialAppointCadre());
            if (award.getDatePresentRank() != null && !award.getDatePresentRank().equals("")) {
                ps.setTimestamp(22, new Timestamp(sdf.parse(award.getDatePresentRank()).getTime()));
            } else {
                ps.setTimestamp(22, null);
            }
            ps.setString(23, award.getPresentPosting());
            ps.setString(24, award.getPostingAddress());
            ps.setString(25, award.getPunishmentDetails());
            ps.setString(26, award.getPunishmentYears());
            ps.setString(27, award.getPendingEnquiryDetails());

            if (award.getCourtCaseYear() != null && !award.getCourtCaseYear().equals("")) {
                ps.setInt(28, Integer.parseInt(award.getCourtCaseYear()));
            } else {
                ps.setInt(28, 0);
            }
            ps.setString(29, award.getCourtCaseDetails());
            ps.setString(30, award.getCourtCaseStatus());

            if (award.getAcrYear1() != null && !award.getAcrYear1().equals("")) {
                ps.setInt(31, Integer.parseInt(award.getAcrYear1()));
            } else {
                ps.setInt(31, 0);
            }
            ps.setString(32, award.getAcrYear1Grading());

            if (award.getAcrYear2() != null && !award.getAcrYear2().equals("")) {
                ps.setInt(33, Integer.parseInt(award.getAcrYear2()));
            } else {
                ps.setInt(33, 0);
            }
            ps.setString(34, award.getAcrYear2Grading());

            if (award.getAcrYear3() != null && !award.getAcrYear3().equals("")) {
                ps.setInt(35, Integer.parseInt(award.getAcrYear3()));
            } else {
                ps.setInt(35, 0);
            }
            ps.setString(36, award.getAcrYear3Grading());

            if (award.getAcrYear4() != null && !award.getAcrYear4().equals("")) {
                ps.setInt(37, Integer.parseInt(award.getAcrYear4()));
            } else {
                ps.setInt(37, 0);
            }
            ps.setString(38, award.getAcrYear4Grading());

            if (award.getAcrYear5() != null && !award.getAcrYear5().equals("")) {
                ps.setInt(39, Integer.parseInt(award.getAcrYear5()));
            } else {
                ps.setInt(39, 0);
            }
            ps.setString(40, award.getAcrYear5Grading());

            if (award.getOtherReward() != null && !award.getOtherReward().equals("")) {
                ps.setInt(41, Integer.parseInt(award.getOtherReward()));
            } else {
                ps.setInt(41, 0);
            }
            ps.setString(42, award.getFurtherInfoByRange());

            /*President Police Medal*/
            ps.setString(43, award.getFirstName());
            ps.setString(44, award.getMiddleName());
            ps.setString(45, award.getLastName());
            ps.setString(46, award.getFatherfirstname());
            ps.setString(47, award.getFathermiddlename());
            ps.setString(48, award.getFatherlastname());
            ps.setString(49, award.getSltCategory());
            if (award.getInitialAppointDate() != null && !award.getInitialAppointDate().equals("")) {
                ps.setTimestamp(50, new Timestamp(sdf.parse(award.getInitialAppointDate()).getTime()));
            } else {
                ps.setTimestamp(50, null);
            }
            ps.setString(51, award.getInitialAppointService());
            ps.setString(52, award.getInitialAppointCategory());

            if (award.getTotalpoliceyears() != null && !award.getTotalpoliceyears().equals("")) {
                ps.setInt(53, Integer.parseInt(award.getTotalpoliceyears()));
            } else {
                ps.setInt(53, 0);
            }
            if (award.getTotalpolicemonths() != null && !award.getTotalpolicemonths().equals("")) {
                ps.setInt(54, Integer.parseInt(award.getTotalpolicemonths()));
            } else {
                ps.setInt(54, 0);
            }
            if (award.getTotalpolicedays() != null && !award.getTotalpolicedays().equals("")) {
                ps.setInt(55, Integer.parseInt(award.getTotalpolicedays()));
            } else {
                ps.setInt(55, 0);
            }
            /* ps.setInt(53, award.getTotalpoliceyears());
             ps.setInt(54, award.getTotalpolicemonths());
             ps.setInt(55, award.getTotalpolicedays());*/

            ps.setString(56, award.getPresentPosting());
            ps.setString(57, award.getPostingAddress());
            ps.setString(58, award.getPresentPostingPlace());
            if (award.getPresentPostingPIN() != null && !award.getPresentPostingPIN().equals("")) {
                ps.setInt(59, Integer.parseInt(award.getPresentPostingPIN()));
            } else {
                ps.setInt(59, 0);
            }
            if (award.getPresentPostingDate() != null && !award.getPresentPostingDate().equals("")) {
                ps.setTimestamp(60, new Timestamp(sdf.parse(award.getPresentPostingDate()).getTime()));
            } else {
                ps.setTimestamp(60, null);
            }
            ps.setString(61, award.getSltDeputation());
            if (award.getDeputationDate() != null && !award.getDeputationDate().equals("")) {
                ps.setTimestamp(62, new Timestamp(sdf.parse(award.getDeputationDate()).getTime()));
            } else {
                ps.setTimestamp(62, null);
            }

            if (award.getRewardsTotalNo() != null && !award.getRewardsTotalNo().equals("")) {
                ps.setInt(63, Integer.parseInt(award.getRewardsTotalNo()));
            } else {
                ps.setInt(63, 0);
            }
            if (award.getRewardsAmt() != null && !award.getRewardsAmt().equals("")) {
                ps.setInt(64, Integer.parseInt(award.getRewardsAmt()));
            } else {
                ps.setInt(64, 0);
            }
            if (award.getCashAwardsNo() != null && !award.getCashAwardsNo().equals("")) {
                ps.setInt(65, Integer.parseInt(award.getCashAwardsNo()));
            } else {
                ps.setInt(65, 0);
            }
            if (award.getPresidentcommendation() != null && !award.getPresidentcommendation().equals("")) {
                ps.setInt(66, Integer.parseInt(award.getPresidentcommendation()));
            } else {
                ps.setInt(66, 0);
            }
            if (award.getPresidentappreciation() != null && !award.getPresidentappreciation().equals("")) {
                ps.setInt(67, Integer.parseInt(award.getPresidentappreciation()));
            } else {
                ps.setInt(67, 0);
            }
            if (award.getGoodServiceEntries() != null && !award.getGoodServiceEntries().equals("")) {
                ps.setInt(68, Integer.parseInt(award.getGoodServiceEntries()));
            } else {
                ps.setInt(68, 0);
            }
            if (award.getAnyOtherRewards() != null && !award.getAnyOtherRewards().equals("")) {
                ps.setInt(69, Integer.parseInt(award.getAnyOtherRewards()));
            } else {
                ps.setInt(69, 0);
            }

            if (award.getMeritoriousYear() != null && !award.getMeritoriousYear().equals("")) {
                ps.setInt(70, Integer.parseInt(award.getMeritoriousYear()));
            } else {
                ps.setInt(70, 0);
            }
            ps.setString(71, award.getSltOccasion());

            ps.setString(72, award.getPenaltydetails());
            if (award.getPenaltyyear() != null && !award.getPenaltyyear().equals("")) {
                ps.setInt(73, Integer.parseInt(award.getPenaltyyear()));
            } else {
                ps.setInt(73, 0);
            }
            ps.setString(74, award.getPenaltyOrderNo());
            if (award.getPenaltyOrderDate() != null && !award.getPenaltyOrderDate().equals("")) {
                ps.setTimestamp(75, new Timestamp(sdf.parse(award.getPenaltyOrderDate()).getTime()));
            } else {
                ps.setTimestamp(75, null);
            }
            ps.setString(76, award.getMedicalCategory());
            ps.setString(77, award.getIfenquirypending());
            if (award.getDpcyear() != null && !award.getDpcyear().equals("")) {
                ps.setInt(78, Integer.parseInt(award.getDpcyear()));
            } else {
                ps.setInt(78, 0);
            }
            ps.setString(79, award.getDpcnatureallegation());
            ps.setString(80, award.getDpcpresentstatus());
            if (award.getCourtCasePendingYear() != null && !award.getCourtCasePendingYear().equals("")) {
                ps.setInt(81, Integer.parseInt(award.getCourtCasePendingYear()));
            } else {
                ps.setInt(81, 0);
            }
            ps.setString(82, award.getCourtCasePendingDetails());
            ps.setString(83, award.getCourtCasePendingStatus());

            ps.setString(84, award.getAcrGrading1());
            ps.setString(85, award.getAcrGrading2());
            ps.setString(86, award.getAcrGrading3());
            ps.setString(87, award.getAcrGrading4());
            ps.setString(88, award.getAcrGrading5());
            ps.setString(89, award.getAcrGrading6());
            ps.setString(90, award.getAcrGrading7());
            ps.setString(91, award.getAcrGrading8());
            ps.setString(92, award.getAcrGrading9());
            ps.setString(93, award.getAcrGrading10());
            ps.setString(94, award.getAcrGrading11());
            ps.setString(95, award.getAcrGrading12());
            ps.setString(96, award.getAcrGrading13());
            ps.setString(97, award.getAcrGrading14());

            if (award.getAcrGradingOS() != null && !award.getAcrGradingOS().equals("")) {
                ps.setInt(98, Integer.parseInt(award.getAcrGradingOS()));
            } else {
                ps.setInt(98, 0);
            }
            if (award.getAcrGradingVS() != null && !award.getAcrGradingVS().equals("")) {
                ps.setInt(99, Integer.parseInt(award.getAcrGradingVS()));
            } else {
                ps.setInt(99, 0);
            }
            if (award.getAcrGradingGood() != null && !award.getAcrGradingGood().equals("")) {
                ps.setInt(100, Integer.parseInt(award.getAcrGradingGood()));
            } else {
                ps.setInt(100, 0);
            }
            if (award.getAcrGradingAvg() != null && !award.getAcrGradingAvg().equals("")) {
                ps.setInt(101, Integer.parseInt(award.getAcrGradingAvg()));
            } else {
                ps.setInt(101, 0);
            }
            if (award.getAcrGradingNic() != null && !award.getAcrGradingNic().equals("")) {
                ps.setInt(102, Integer.parseInt(award.getAcrGradingNic()));
            } else {
                ps.setInt(102, 0);
            }
            if (award.getAcrGradingAdv() != null && !award.getAcrGradingAdv().equals("")) {
                ps.setInt(103, Integer.parseInt(award.getAcrGradingAdv()));
            } else {
                ps.setInt(103, 0);
            }
            if (award.getAcrGradingMs() != null && !award.getAcrGradingMs().equals("")) {
                ps.setInt(104, Integer.parseInt(award.getAcrGradingMs()));
            } else {
                ps.setInt(104, 0);
            }
            if (award.getAcrGradingNa() != null && !award.getAcrGradingNa().equals("")) {
                ps.setInt(105, Integer.parseInt(award.getAcrGradingNa()));
            } else {
                ps.setInt(105, 0);
            }

            ps.setString(106, award.getEmail());
            ps.setString(107, award.getMobile());
            ps.setString(108, award.getBriefdescription());
            ps.setString(109, award.getEnquirypending());

            if (award.getCashAwardsAmt() != null && !award.getCashAwardsAmt().equals("")) {
                ps.setInt(110, Integer.parseInt(award.getCashAwardsAmt()));
            } else {
                ps.setInt(110, 0);
            }

            if (award.getPresidentcommendationAmt() != null && !award.getPresidentcommendationAmt().equals("")) {
                ps.setInt(111, Integer.parseInt(award.getPresidentcommendationAmt()));
            } else {
                ps.setInt(111, 0);
            }
            if (award.getPresidentappreciationAmt() != null && !award.getPresidentappreciationAmt().equals("")) {
                ps.setInt(112, Integer.parseInt(award.getPresidentappreciationAmt()));
            } else {
                ps.setInt(112, 0);
            }
            if (award.getGoodServiceEntriesAmt() != null && !award.getGoodServiceEntriesAmt().equals("")) {
                ps.setInt(113, Integer.parseInt(award.getGoodServiceEntriesAmt()));
            } else {
                ps.setInt(113, 0);
            }
            ps.setString(114, award.getAnyOtherRewardsDesc());

            ps.setString(115, award.getCcrolloneremarks());
            ps.setString(116, award.getCcrolltworemarks());
            ps.setString(117, award.getCcrollthreeremarks());
            ps.setString(118, award.getOtherInformationFromDistrictDgps());
            ps.setString(119, award.getPunishmentMajorDetails());
            ps.setString(120, award.getPunishmentMinorDetails());
            ps.setString(121, award.getMinorpenaltydetails());
            if (award.getMinorpenaltyyear() != null && !award.getMinorpenaltyyear().equals("")) {
                ps.setInt(122, Integer.parseInt(award.getMinorpenaltyyear()));
            } else {
                ps.setInt(122, 0);
            }
            ps.setString(123, award.getMinorpenaltyOrderNo());
            if (award.getMinorpenaltyOrderDate() != null && !award.getMinorpenaltyOrderDate().equals("")) {
                ps.setTimestamp(124, new Timestamp(sdf.parse(award.getMinorpenaltyOrderDate()).getTime()));
            } else {
                ps.setTimestamp(124, null);
            }
            ps.setString(125, award.getApprovMeritoriousServiceAwarded());

            if (award.getPunishmentmajor2() != null && !award.getPunishmentmajor2().equals("")) {
                ps.setString(126, award.getPunishmentmajor2());
            } else {
                ps.setString(126, "");
            }
            if (award.getPunishmentmajor3() != null && !award.getPunishmentmajor3().equals("")) {
                ps.setString(127, award.getPunishmentmajor3());
            } else {
                ps.setString(127, "");
            }
            if (award.getPunishmentmajor4() != null && !award.getPunishmentmajor4().equals("")) {
                ps.setString(128, award.getPunishmentmajor4());
            } else {
                ps.setString(128, "");
            }
            if (award.getPunishmentmajor5() != null && !award.getPunishmentmajor5().equals("")) {
                ps.setString(129, award.getPunishmentmajor5());
            } else {
                ps.setString(129, "");
            }
            if (award.getPunishmentminor2() != null && !award.getPunishmentminor2().equals("")) {
                ps.setString(130, award.getPunishmentminor2());
            } else {
                ps.setString(130, "");
            }
            if (award.getPunishmentminor3() != null && !award.getPunishmentminor3().equals("")) {
                ps.setString(131, award.getPunishmentminor3());
            } else {
                ps.setString(131, "");
            }
            if (award.getPunishmentminor4() != null && !award.getPunishmentminor4().equals("")) {
                ps.setString(132, award.getPunishmentminor4());
            } else {
                ps.setString(132, "");
            }
            if (award.getPunishmentminor5() != null && !award.getPunishmentminor5().equals("")) {
                ps.setString(133, award.getPunishmentminor5());
            } else {
                ps.setString(133, "");
            }
            ps.setString(134, award.getAnnexure2Details());
            ps.setString(135, award.getAnnexure3Details());
            ps.setString(136, award.getCriminalcaseifany());
            ps.setString(137, award.getChargesheetedifany());
            ps.setString(138, award.getDesignation());
            ps.setString(139, award.getMeetingProceeding());
            ps.setString(140, award.getProceedingDetails());
            ps.setString(141, award.getCcrRefenrence());
            ps.setString(142, award.getPropertyStatementSubmittedifAny());
            if (award.getDateofPropertySubmittedByHRMS() != null && !award.getDateofPropertySubmittedByHRMS().equals("")) {
                ps.setTimestamp(143, new Timestamp(sdf.parse(award.getDateofPropertySubmittedByHRMS()).getTime()));
            } else {
                ps.setTimestamp(143, null);
            }
            if (award.getDateofPropertySubmittedByOfficer() != null && !award.getDateofPropertySubmittedByOfficer().equals("")) {
                ps.setTimestamp(144, new Timestamp(sdf.parse(award.getDateofPropertySubmittedByOfficer()).getTime()));
            } else {
                ps.setTimestamp(144, null);
            }
            ps.setString(145, award.getPunishmentMinorpreecidingDetails());
            ps.setString(146, award.getPunishmentMinorpreeciding());
            ps.setString(147, award.getPunishmentMajorpreecidingDetails());
            ps.setString(148, award.getPunishmentMajorpreeciding());
            ps.setString(149, award.getPunishmentMajordgp());
            ps.setString(150, award.getPunishmentMinordgp());
            if (award.getDoa() != null && !award.getDoa().equals("")) {
                ps.setTimestamp(151, new Timestamp(sdf.parse(award.getDoa()).getTime()));
            } else {
                ps.setTimestamp(151, null);
            }
            ps.setString(152, award.getCountry());
            ps.setString(153, award.getNationality());
            ps.setString(154, award.getState());
            ps.setString(155, award.getAcrGradingDetail());
            ps.setString(156, award.getAcrCopyifAny());
            ps.setInt(157, Integer.parseInt(award.getRewardMedalId()));
            ps.execute();

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getDifferentRank() != null && !award.getDifferentRank().equals("")) {
                String[] differentRankArr = award.getDifferentRank().split(",");
                String[] differentDateArr = award.getDifferentDate().split(",");
                String[] totaldifferentrankyears = award.getTotaldifferentrankyears().split(",");
                String[] totaldifferentrankmonths = award.getTotaldifferentrankmonths().split(",");
                String[] totaldifferentrankdays = award.getTotaldifferentrankdays().split(",");

                ps = con.prepareStatement("DELETE FROM police_award_form_different_ranks WHERE award_medal_id=?");
                ps.setInt(1, Integer.parseInt(award.getRewardMedalId()));
                ps.execute();

                String sql = "insert into police_award_form_different_ranks(award_medal_id,rank_name,rank_date,rank_year,rank_month,rank_days) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(sql);

                for (int i = 0; i < differentRankArr.length; i++) {
                    ps.setInt(1, Integer.parseInt(award.getRewardMedalId()));
                    ps.setString(2, differentRankArr[i]);
                    if (differentDateArr[i] != null && !differentDateArr[i].equals("")) {
                        ps.setTimestamp(3, new Timestamp(sdf.parse(differentDateArr[i]).getTime()));
                    } else {
                        ps.setTimestamp(3, null);
                    }
                    if (totaldifferentrankyears[i] != null && !totaldifferentrankyears[i].equals("")) {
                        ps.setInt(4, Integer.parseInt(totaldifferentrankyears[i]));
                    } else {
                        ps.setInt(4, 0);
                    }
                    if (totaldifferentrankmonths[i] != null && !totaldifferentrankmonths[i].equals("")) {
                        ps.setInt(5, Integer.parseInt(totaldifferentrankmonths[i]));
                    } else {
                        ps.setInt(5, 0);
                    }
                    if (totaldifferentrankdays[i] != null && !totaldifferentrankdays[i].equals("")) {
                        ps.setInt(6, Integer.parseInt(totaldifferentrankdays[i]));
                    } else {
                        ps.setInt(6, 0);
                    }
                    ps.execute();
                }
            }

            if (award.getServiceBookCopy() != null && !award.getServiceBookCopy().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getServiceBookCopy().getInputStream();
                String diskfilename = "SB" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getServiceBookCopy().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getServiceBookCopy().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "SB");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getDiscDocument() != null && !award.getDiscDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getDiscDocument().getInputStream();
                String diskfilename = "DPC" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getDiscDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getDiscDocument().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "DPC");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getDiscCCROll() != null && !award.getDiscCCROll().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getDiscCCROll().getInputStream();
                String diskfilename = "CCROLL" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getDiscCCROll().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getDiscCCROll().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "CCROLL");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getSrDocument() != null && !award.getSrDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getSrDocument().getInputStream();
                String diskfilename = "SR" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getSrDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getSrDocument().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "SRD");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getNonSrDocument() != null && !award.getNonSrDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getNonSrDocument().getInputStream();
                String diskfilename = "NSR" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getNonSrDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getNonSrDocument().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "NSRD");
                ps.executeUpdate();

            }

            if (award.getProceedingDocument() != null && !award.getProceedingDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getProceedingDocument().getInputStream();
                String diskfilename = "PRO" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getProceedingDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getProceedingDocument().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "PROC");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getCertificateDoc() != null && !award.getCertificateDoc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getCertificateDoc().getInputStream();
                String diskfilename = "CRT" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getCertificateDoc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getCertificateDoc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "CRTR");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getIntegrityCertificateDoc() != null && !award.getIntegrityCertificateDoc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getIntegrityCertificateDoc().getInputStream();
                String diskfilename = "ICD" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getIntegrityCertificateDoc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getIntegrityCertificateDoc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ICD");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getMedicalCertificateDoc() != null && !award.getMedicalCertificateDoc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getMedicalCertificateDoc().getInputStream();
                String diskfilename = "MED" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getMedicalCertificateDoc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getMedicalCertificateDoc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "MED");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getPunishmentDoc() != null && !award.getPunishmentDoc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getPunishmentDoc().getInputStream();
                String diskfilename = "PUN" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getPunishmentDoc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getPunishmentDoc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "PUN");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getEnquiryDoc() != null && !award.getEnquiryDoc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getEnquiryDoc().getInputStream();
                String diskfilename = "ENQ" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getEnquiryDoc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getEnquiryDoc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ENQ");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getDpcDoc() != null && !award.getDpcDoc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getDpcDoc().getInputStream();
                String diskfilename = "DISCPC" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getDpcDoc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getDpcDoc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "DISCPC");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getCourtCaseDoc() != null && !award.getCourtCaseDoc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getCourtCaseDoc().getInputStream();
                String diskfilename = "COURT" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getCourtCaseDoc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getCourtCaseDoc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "COURT");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading1Doc() != null && !award.getAcrGrading1Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading1Doc().getInputStream();
                String diskfilename = "ACR1" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading1Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading1Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR1");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading2Doc() != null && !award.getAcrGrading2Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading2Doc().getInputStream();
                String diskfilename = "ACR2" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading2Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading2Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR2");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading3Doc() != null && !award.getAcrGrading3Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading3Doc().getInputStream();
                String diskfilename = "ACR3" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading3Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading3Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR3");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading4Doc() != null && !award.getAcrGrading4Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading4Doc().getInputStream();
                String diskfilename = "ACR4" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading4Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading4Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR4");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading5Doc() != null && !award.getAcrGrading5Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading5Doc().getInputStream();
                String diskfilename = "ACR5" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading5Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading5Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR5");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading6Doc() != null && !award.getAcrGrading6Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading6Doc().getInputStream();
                String diskfilename = "ACR6" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading6Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading6Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR6");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading7Doc() != null && !award.getAcrGrading7Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading7Doc().getInputStream();
                String diskfilename = "ACR7" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading7Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading7Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR7");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading8Doc() != null && !award.getAcrGrading8Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading8Doc().getInputStream();
                String diskfilename = "ACR8" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading8Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading8Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR8");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading9Doc() != null && !award.getAcrGrading9Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading9Doc().getInputStream();
                String diskfilename = "ACR9" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading9Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading9Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR9");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading10Doc() != null && !award.getAcrGrading10Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading10Doc().getInputStream();
                String diskfilename = "ACR10" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading10Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading10Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR10");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading11Doc() != null && !award.getAcrGrading11Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading11Doc().getInputStream();
                String diskfilename = "ACR11" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading11Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading11Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR11");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading12Doc() != null && !award.getAcrGrading12Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading12Doc().getInputStream();
                String diskfilename = "ACR12" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading12Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading12Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR12");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading13Doc() != null && !award.getAcrGrading13Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading13Doc().getInputStream();
                String diskfilename = "ACR13" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading13Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading13Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR13");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getDiscCCRollone() != null && !award.getDiscCCRollone().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getDiscCCRollone().getInputStream();
                String diskfilename = "CCROLLOne" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getDiscCCRollone().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getDiscCCRollone().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "CCROLLOne");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getDiscCCRolltwo() != null && !award.getDiscCCRolltwo().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getDiscCCRolltwo().getInputStream();
                String diskfilename = "CCROLLTwo" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getDiscCCRolltwo().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getDiscCCRolltwo().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "CCROLLTwo");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getDiscCCRollthree() != null && !award.getDiscCCRollthree().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getDiscCCRollthree().getInputStream();
                String diskfilename = "CCROLLThree" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getDiscCCRollthree().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getDiscCCRollthree().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "CCROLLThree");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getMedicalCategoryDoc() != null && !award.getMedicalCategoryDoc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getMedicalCategoryDoc().getInputStream();
                String diskfilename = "MEDCAT" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getMedicalCategoryDoc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getMedicalCategoryDoc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "MEDCAT");
                ps.executeUpdate();

            }
            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getMajorPunishmentDocument() != null && !award.getMajorPunishmentDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getMajorPunishmentDocument().getInputStream();
                String diskfilename = "MAJORPUNISH" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getMajorPunishmentDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getMajorPunishmentDocument().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "MAJORPUNISH");
                ps.executeUpdate();

            }
            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getMinorPunishmentDocument() != null && !award.getMinorPunishmentDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getMinorPunishmentDocument().getInputStream();
                String diskfilename = "MINORPUNISH" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getMinorPunishmentDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getMinorPunishmentDocument().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "MINORPUNISH");
                ps.executeUpdate();

            }
            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getMajorPunishmentpreecidingDocument() != null && !award.getMajorPunishmentpreecidingDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getMajorPunishmentpreecidingDocument().getInputStream();
                String diskfilename = "MAJORPUNISHPREECIDING" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getMajorPunishmentpreecidingDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getMajorPunishmentpreecidingDocument().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "MAJORPUNISHPREECIDING");
                ps.executeUpdate();

            }
            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getMinorPunishmentpreecidingDocument() != null && !award.getMinorPunishmentpreecidingDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getMinorPunishmentpreecidingDocument().getInputStream();
                String diskfilename = "MINORPUNISHPREEDIDING" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getMinorPunishmentpreecidingDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getMinorPunishmentpreecidingDocument().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "MINORPUNISHPREEDIDING");
                ps.executeUpdate();

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void updateAwardDataForGovernor(AwardMedalListForm award, String filePath) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            con = dataSource.getConnection();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            ps = con.prepareStatement("update police_award_form set doc_present_rank=?, doj_dist_est=?,  money_reward=?,  commendation=?, "
                    + " gs_mark=?, appreciation=?, aar=?, punishment_major=?, punishment_minor=?, award_medal_previous_year=?, "
                    + " award_medal_rank=?, award_medal_posting_place =?, previously_awarded=?, brief_note=?, dpcifany=?, disc_details=?, "
                    + " completed_status=?, recommend_by_dist=?,  "
                    + " initial_appoint_year=?, initial_appointment_rank=?, initial_appointment_cadre =?, "
                    + " date_since_present_rank=?, present_posting=?, posting_address=?, punishment_details=?, punishment_years=?, pending_enquiry_details=?, "
                    + " court_case_year=?, court_case_charge=?, court_case_status=?, "
                    + " acr_year1=?, acr_year1_grading=? , acr_year2=?, acr_year2_grading=?, acr_year3=?, acr_year3_grading =?, "
                    + " acr_year4=?, acr_year4_grading =?, acr_year5=?, acr_year5_grading=?, other_award=?, further_information_by_range=?,"
                    + " first_name=?,middle_name=?,last_name=?,father_first_name=?,father_second_name=?,father_last_name=?,"
                    + " initial_appoint_date=?,initial_appoint_service=?,initial_appoint_category=?,total_police_service_years=?,total_police_service_months=?,total_police_service_days=?, "
                    + " present_posting_designation=?,present_posting_address=?,present_posting_place=?,present_posting_pin=?,present_posting_date=?, "
                    + " rewards_no=?,rewards_total_amt=?,rewards_cash_awards=?,rewards_commendation=?,rewards_appreciation=?,rewards_good_service=?,rewards_any_other=?, "
                    + " medal_meritorious_service_year=?,is_governor_medal_earlier=?,punishment_penalty_details=?,punishment_penalty_year=?,punishment_penalty_order_no=?,punishment_penalty_order_date=?, "
                    + " pending_enquiry=?,dpc_year=?,dpc_nature=?,dpc_status=?,court_case_pending_year=?,court_case_pending_details=?,court_case_pending_status=?, "
                    + " acr_grading_9=?,acr_grading_10=?,acr_grading_11=?, "
                    + " acr_grading_12=?,acr_grading_13=?,acr_grading_os=?,acr_grading_vg=?,acr_grading_good=?,acr_grading_avg=?,acr_grading_nic=?,acr_grading_adv=?,acr_grading_ms=?, "
                    + " acr_grading_na=?,email=?,mobile=?,brief_description=?,pending_enquiry_note=?,rewards_cash_awards_amt=?,rewards_commendation_amt=?,rewards_appreciation_amt=?,rewards_good_service_amt=?,rewards_any_other_desc=?, "
                    + " ccr_remarks_one=?,ccr_remarks_two=?,ccr_remarks_three=?,rank_designation_withpresent_posting=?,other_range_information_governor=?,disciplinary_proceeding_details=?,if_major_punishment=?,if_minor_punishment=?, "
                    + " penaltydetails_minor=?,penaltydetails_major=?,if_disciplinary_proceeding=?,acr_grading_14=?,property_submitted_if_any=?, "
                    + " property_submitted_on_hrms=?,property_submitted_on_hrms_byofficer=? "
                    + " where award_medal_id=?");
            if (award.getDocPresentRank() != null && !award.getDocPresentRank().equals("")) {
                ps.setTimestamp(1, new Timestamp(sdf.parse(award.getDocPresentRank()).getTime()));
            } else {
                ps.setTimestamp(1, null);
            }
            if (award.getDojDistEst() != null && !award.getDojDistEst().equals("")) {
                ps.setTimestamp(2, new Timestamp(sdf.parse(award.getDojDistEst()).getTime()));
            } else {
                ps.setTimestamp(2, null);
            }
            if (award.getMoneyReward() != null && !award.getMoneyReward().equals("")) {
                ps.setInt(3, Integer.parseInt(award.getMoneyReward()));
            } else {
                ps.setInt(3, 0);
            }
            if (award.getCommendation() != null && !award.getCommendation().equals("")) {
                ps.setInt(4, Integer.parseInt(award.getCommendation()));
            } else {
                ps.setInt(4, 0);
            }
            if (award.getGsMark() != null && !award.getGsMark().equals("")) {
                ps.setInt(5, Integer.parseInt(award.getGsMark()));
            } else {
                ps.setInt(5, 0);
            }
            if (award.getAppreciation() != null && !award.getAppreciation().equals("")) {
                ps.setInt(6, Integer.parseInt(award.getAppreciation()));
            } else {
                ps.setInt(6, 0);
            }
            if (award.getAar() != null && !award.getAar().equals("")) {
                ps.setInt(7, Integer.parseInt(award.getAar()));
            } else {
                ps.setInt(7, 0);
            }
            if (award.getPunishmentMajor() != null && !award.getPunishmentMajor().equals("")) {
                ps.setInt(8, Integer.parseInt(award.getPunishmentMajor()));
            } else {
                ps.setInt(8, 0);
            }
            if (award.getPunishmentMinor() != null && !award.getPunishmentMinor().equals("")) {
                ps.setInt(9, Integer.parseInt(award.getPunishmentMinor()));
            } else {
                ps.setInt(9, 0);
            }
            ps.setString(10, award.getAwardMedalPreviousYear());
            ps.setString(11, award.getAwardMedalRank());
            ps.setString(12, award.getAwardMedalPostingPlace());
            ps.setString(13, award.getPrevAwardCmb());
            ps.setString(14, award.getBriefNote());
            ps.setString(15, award.getDpcifany());
            ps.setString(16, award.getDiscDetails());
            ps.setString(17, "Y");
            ps.setString(18, award.getRecommendStatusofDist());
            if (award.getInitialAppointYear() != null && !award.getInitialAppointYear().equals("")) {
                ps.setInt(19, Integer.parseInt(award.getInitialAppointYear()));
            } else {
                ps.setInt(19, 0);
            }

            ps.setString(20, award.getInitialAppointRank());
            ps.setString(21, award.getInitialAppointCadre());
            if (award.getDatePresentRank() != null && !award.getDatePresentRank().equals("")) {
                ps.setTimestamp(22, new Timestamp(sdf.parse(award.getDatePresentRank()).getTime()));
            } else {
                ps.setTimestamp(22, null);
            }
            ps.setString(23, award.getPresentPosting());
            ps.setString(24, award.getPostingAddress());
            ps.setString(25, award.getPunishmentDetails());
            ps.setString(26, award.getPunishmentYears());
            ps.setString(27, award.getPendingEnquiryDetails());

            if (award.getCourtCaseYear() != null && !award.getCourtCaseYear().equals("")) {
                ps.setInt(28, Integer.parseInt(award.getCourtCaseYear()));
            } else {
                ps.setInt(28, 0);
            }
            ps.setString(29, award.getCourtCaseDetails());
            ps.setString(30, award.getCourtCaseStatus());

            if (award.getAcrYear1() != null && !award.getAcrYear1().equals("")) {
                ps.setInt(31, Integer.parseInt(award.getAcrYear1()));
            } else {
                ps.setInt(31, 0);
            }
            ps.setString(32, award.getAcrYear1Grading());

            if (award.getAcrYear2() != null && !award.getAcrYear2().equals("")) {
                ps.setInt(33, Integer.parseInt(award.getAcrYear2()));
            } else {
                ps.setInt(33, 0);
            }
            ps.setString(34, award.getAcrYear2Grading());

            if (award.getAcrYear3() != null && !award.getAcrYear3().equals("")) {
                ps.setInt(35, Integer.parseInt(award.getAcrYear3()));
            } else {
                ps.setInt(35, 0);
            }
            ps.setString(36, award.getAcrYear3Grading());

            if (award.getAcrYear4() != null && !award.getAcrYear4().equals("")) {
                ps.setInt(37, Integer.parseInt(award.getAcrYear4()));
            } else {
                ps.setInt(37, 0);
            }
            ps.setString(38, award.getAcrYear4Grading());

            if (award.getAcrYear5() != null && !award.getAcrYear5().equals("")) {
                ps.setInt(39, Integer.parseInt(award.getAcrYear5()));
            } else {
                ps.setInt(39, 0);
            }
            ps.setString(40, award.getAcrYear5Grading());

            if (award.getOtherReward() != null && !award.getOtherReward().equals("")) {
                ps.setInt(41, Integer.parseInt(award.getOtherReward()));
            } else {
                ps.setInt(41, 0);
            }
            ps.setString(42, award.getFurtherInfoByRange());

            /*President Police Medal*/
            ps.setString(43, award.getFirstName());
            ps.setString(44, award.getMiddleName());
            ps.setString(45, award.getLastName());
            ps.setString(46, award.getFatherfirstname());
            ps.setString(47, award.getFathermiddlename());
            ps.setString(48, award.getFatherlastname());
            //ps.setString(49, award.getSltCategory());
            if (award.getInitialAppointDate() != null && !award.getInitialAppointDate().equals("")) {
                ps.setTimestamp(49, new Timestamp(sdf.parse(award.getInitialAppointDate()).getTime()));
            } else {
                ps.setTimestamp(49, null);
            }
            ps.setString(50, award.getInitialAppointService());
            ps.setString(51, award.getInitialAppointCategory());

            if (award.getTotalpoliceyears() != null && !award.getTotalpoliceyears().equals("")) {
                ps.setInt(52, Integer.parseInt(award.getTotalpoliceyears()));
            } else {
                ps.setInt(52, 0);
            }
            if (award.getTotalpolicemonths() != null && !award.getTotalpolicemonths().equals("")) {
                ps.setInt(53, Integer.parseInt(award.getTotalpolicemonths()));
            } else {
                ps.setInt(53, 0);
            }
            if (award.getTotalpolicedays() != null && !award.getTotalpolicedays().equals("")) {
                ps.setInt(54, Integer.parseInt(award.getTotalpolicedays()));
            } else {
                ps.setInt(54, 0);
            }
            /* ps.setInt(52, award.getTotalpoliceyears());
             ps.setInt(53, award.getTotalpolicemonths());
             ps.setInt(54, award.getTotalpolicedays());*/

            ps.setString(55, award.getPresentPosting());
            ps.setString(56, award.getPostingAddress());
            ps.setString(57, award.getPresentPostingPlace());
            if (award.getPresentPostingPIN() != null && !award.getPresentPostingPIN().equals("")) {
                ps.setInt(58, Integer.parseInt(award.getPresentPostingPIN()));
            } else {
                ps.setInt(58, 0);
            }
            if (award.getPresentPostingDate() != null && !award.getPresentPostingDate().equals("")) {
                ps.setTimestamp(59, new Timestamp(sdf.parse(award.getPresentPostingDate()).getTime()));
            } else {
                ps.setTimestamp(59, null);
            }
            //ps.setString(60, award.getSltDeputation());
            //if (award.getDeputationDate() != null && !award.getDeputationDate().equals("")) {
            // ps.setTimestamp(61, new Timestamp(sdf.parse(award.getDeputationDate()).getTime()));
            //} else {
            ps.setTimestamp(61, null);
            //}

            if (award.getRewardsTotalNo() != null && !award.getRewardsTotalNo().equals("")) {
                ps.setInt(60, Integer.parseInt(award.getRewardsTotalNo()));
            } else {
                ps.setInt(60, 0);
            }
            if (award.getRewardsAmt() != null && !award.getRewardsAmt().equals("")) {
                ps.setInt(61, Integer.parseInt(award.getRewardsAmt()));
            } else {
                ps.setInt(61, 0);
            }
            if (award.getCashAwardsNo() != null && !award.getCashAwardsNo().equals("")) {
                ps.setInt(62, Integer.parseInt(award.getCashAwardsNo()));
            } else {
                ps.setInt(62, 0);
            }
            if (award.getPresidentcommendation() != null && !award.getPresidentcommendation().equals("")) {
                ps.setInt(63, Integer.parseInt(award.getPresidentcommendation()));
            } else {
                ps.setInt(63, 0);
            }
            if (award.getPresidentappreciation() != null && !award.getPresidentappreciation().equals("")) {
                ps.setInt(64, Integer.parseInt(award.getPresidentappreciation()));
            } else {
                ps.setInt(64, 0);
            }
            if (award.getGoodServiceEntries() != null && !award.getGoodServiceEntries().equals("")) {
                ps.setInt(65, Integer.parseInt(award.getGoodServiceEntries()));
            } else {
                ps.setInt(65, 0);
            }
            if (award.getAnyOtherRewards() != null && !award.getAnyOtherRewards().equals("")) {
                ps.setInt(66, Integer.parseInt(award.getAnyOtherRewards()));
            } else {
                ps.setInt(66, 0);
            }

            if (award.getMeritoriousYear() != null && !award.getMeritoriousYear().equals("")) {
                ps.setInt(67, Integer.parseInt(award.getMeritoriousYear()));
            } else {
                ps.setInt(67, 0);
            }
            ps.setString(68, award.getApprovGovernorMedalAwarded());

            ps.setString(69, award.getPenaltydetails());
            if (award.getPenaltyyear() != null && !award.getPenaltyyear().equals("")) {
                ps.setInt(70, Integer.parseInt(award.getPenaltyyear()));
            } else {
                ps.setInt(70, 0);
            }
            ps.setString(71, award.getPenaltyOrderNo());
            if (award.getPenaltyOrderDate() != null && !award.getPenaltyOrderDate().equals("")) {
                ps.setTimestamp(72, new Timestamp(sdf.parse(award.getPenaltyOrderDate()).getTime()));
            } else {
                ps.setTimestamp(72, null);
            }
            //ps.setString(75, award.getMedicalCategory());
            ps.setString(73, award.getIfenquirypending());
            if (award.getDpcyear() != null && !award.getDpcyear().equals("")) {
                ps.setInt(74, Integer.parseInt(award.getDpcyear()));
            } else {
                ps.setInt(74, 0);
            }
            ps.setString(75, award.getDpcnatureallegation());
            ps.setString(76, award.getDpcpresentstatus());
            if (award.getCourtCasePendingYear() != null && !award.getCourtCasePendingYear().equals("")) {
                ps.setInt(77, Integer.parseInt(award.getCourtCasePendingYear()));
            } else {
                ps.setInt(77, 0);
            }
            ps.setString(78, award.getCourtCasePendingDetails());
            ps.setString(79, award.getCourtCasePendingStatus());

            ps.setString(80, award.getAcrGrading9());
            ps.setString(81, award.getAcrGrading10());
            ps.setString(82, award.getAcrGrading11());
            ps.setString(83, award.getAcrGrading12());
            ps.setString(84, award.getAcrGrading13());
            //ps.setString(97, award.getAcrGrading14());

            if (award.getAcrGradingOS() != null && !award.getAcrGradingOS().equals("")) {
                ps.setInt(85, Integer.parseInt(award.getAcrGradingOS()));
            } else {
                ps.setInt(85, 0);
            }
            if (award.getAcrGradingVS() != null && !award.getAcrGradingVS().equals("")) {
                ps.setInt(86, Integer.parseInt(award.getAcrGradingVS()));
            } else {
                ps.setInt(86, 0);
            }
            if (award.getAcrGradingGood() != null && !award.getAcrGradingGood().equals("")) {
                ps.setInt(87, Integer.parseInt(award.getAcrGradingGood()));
            } else {
                ps.setInt(87, 0);
            }
            if (award.getAcrGradingAvg() != null && !award.getAcrGradingAvg().equals("")) {
                ps.setInt(88, Integer.parseInt(award.getAcrGradingAvg()));
            } else {
                ps.setInt(88, 0);
            }
            if (award.getAcrGradingNic() != null && !award.getAcrGradingNic().equals("")) {
                ps.setInt(89, Integer.parseInt(award.getAcrGradingNic()));
            } else {
                ps.setInt(89, 0);
            }
            if (award.getAcrGradingAdv() != null && !award.getAcrGradingAdv().equals("")) {
                ps.setInt(90, Integer.parseInt(award.getAcrGradingAdv()));
            } else {
                ps.setInt(90, 0);
            }
            if (award.getAcrGradingMs() != null && !award.getAcrGradingMs().equals("")) {
                ps.setInt(91, Integer.parseInt(award.getAcrGradingMs()));
            } else {
                ps.setInt(91, 0);
            }
            if (award.getAcrGradingNa() != null && !award.getAcrGradingNa().equals("")) {
                ps.setInt(92, Integer.parseInt(award.getAcrGradingNa()));
            } else {
                ps.setInt(92, 0);
            }

            ps.setString(93, award.getEmail());
            ps.setString(94, award.getMobile());
            ps.setString(95, award.getBriefdescription());
            ps.setString(96, award.getEnquirypending());

            if (award.getCashAwardsAmt() != null && !award.getCashAwardsAmt().equals("")) {
                ps.setInt(97, Integer.parseInt(award.getCashAwardsAmt()));
            } else {
                ps.setInt(97, 0);
            }

            if (award.getPresidentcommendationAmt() != null && !award.getPresidentcommendationAmt().equals("")) {
                ps.setInt(98, Integer.parseInt(award.getPresidentcommendationAmt()));
            } else {
                ps.setInt(98, 0);
            }
            if (award.getPresidentappreciationAmt() != null && !award.getPresidentappreciationAmt().equals("")) {
                ps.setInt(99, Integer.parseInt(award.getPresidentappreciationAmt()));
            } else {
                ps.setInt(99, 0);
            }
            if (award.getGoodServiceEntriesAmt() != null && !award.getGoodServiceEntriesAmt().equals("")) {
                ps.setInt(100, Integer.parseInt(award.getGoodServiceEntriesAmt()));
            } else {
                ps.setInt(100, 0);
            }
            ps.setString(101, award.getAnyOtherRewardsDesc());

            ps.setString(102, award.getCcrolloneremarks());
            ps.setString(103, award.getCcrolltworemarks());
            ps.setString(104, award.getCcrollthreeremarks());
            ps.setString(105, award.getPresentPostingRankandDesignation());
            ps.setString(106, award.getOtherInformationofRange());
            ps.setString(107, award.getDisciplinaryProceedingpending());
            ps.setString(108, award.getIfMajorPunishment());
            ps.setString(109, award.getIfMinorPunishment());
            ps.setString(110, award.getPenaltydetailsMinor());
            ps.setString(111, award.getPenaltydetailsMojor());
            ps.setString(112, award.getIfdisciplinaryProceedingpending());
            ps.setString(113, award.getAcrGrading14());
            ps.setString(114, award.getPropertyStatementSubmittedifAny());
            if (award.getDateofPropertySubmittedByHRMS() != null && !award.getDateofPropertySubmittedByHRMS().equals("")) {
                ps.setTimestamp(115, new Timestamp(sdf.parse(award.getDateofPropertySubmittedByHRMS()).getTime()));
            } else {
                ps.setTimestamp(115, null);
            }
            if (award.getDateofPropertySubmittedByOfficer() != null && !award.getDateofPropertySubmittedByOfficer().equals("")) {
                ps.setTimestamp(116, new Timestamp(sdf.parse(award.getDateofPropertySubmittedByOfficer()).getTime()));
            } else {
                ps.setTimestamp(116, null);
            }
            ps.setInt(117, Integer.parseInt(award.getRewardMedalId()));
            ps.execute();

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getDifferentRank() != null && !award.getDifferentRank().equals("")) {
                String[] differentRankArr = award.getDifferentRank().split(",");
                String[] differentDateArr = award.getDifferentDate().split(",");
                String[] totaldifferentrankyears = award.getTotaldifferentrankyears().split(",");
                String[] totaldifferentrankmonths = award.getTotaldifferentrankmonths().split(",");
                String[] totaldifferentrankdays = award.getTotaldifferentrankdays().split(",");

                ps = con.prepareStatement("DELETE FROM police_award_form_different_ranks WHERE award_medal_id=?");
                ps.setInt(1, Integer.parseInt(award.getRewardMedalId()));
                ps.execute();

                String sql = "insert into police_award_form_different_ranks(award_medal_id,rank_name,rank_date,rank_year,rank_month,rank_days) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(sql);

                for (int i = 0; i < differentRankArr.length; i++) {
                    ps.setInt(1, Integer.parseInt(award.getRewardMedalId()));
                    ps.setString(2, differentRankArr[i]);
                    if (differentDateArr[i] != null && !differentDateArr[i].equals("")) {
                        ps.setTimestamp(3, new Timestamp(sdf.parse(differentDateArr[i]).getTime()));
                    } else {
                        ps.setTimestamp(3, null);
                    }
                    if (totaldifferentrankyears[i] != null && !totaldifferentrankyears[i].equals("")) {
                        ps.setInt(4, Integer.parseInt(totaldifferentrankyears[i]));
                    } else {
                        ps.setInt(4, 0);
                    }
                    if (totaldifferentrankmonths[i] != null && !totaldifferentrankmonths[i].equals("")) {
                        ps.setInt(5, Integer.parseInt(totaldifferentrankmonths[i]));
                    } else {
                        ps.setInt(5, 0);
                    }
                    if (totaldifferentrankdays[i] != null && !totaldifferentrankdays[i].equals("")) {
                        ps.setInt(6, Integer.parseInt(totaldifferentrankdays[i]));
                    } else {
                        ps.setInt(6, 0);
                    }
                    ps.execute();
                }
            }

            if (award.getServiceBookCopy() != null && !award.getServiceBookCopy().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getServiceBookCopy().getInputStream();
                String diskfilename = "SB" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getServiceBookCopy().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getServiceBookCopy().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "SB");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getDiscDocument() != null && !award.getDiscDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getDiscDocument().getInputStream();
                String diskfilename = "DPC" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getDiscDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getDiscDocument().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "DPC");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getDiscCCROll() != null && !award.getDiscCCROll().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getDiscCCROll().getInputStream();
                String diskfilename = "CCROLL" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getDiscCCROll().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getDiscCCROll().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "CCROLL");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getSrDocument() != null && !award.getSrDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getSrDocument().getInputStream();
                String diskfilename = "SR" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getSrDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getSrDocument().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "SRD");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getNonSrDocument() != null && !award.getNonSrDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getNonSrDocument().getInputStream();
                String diskfilename = "NSR" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getNonSrDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getNonSrDocument().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "NSRD");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);
            if (award.getProceedingDocument() != null && !award.getProceedingDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getProceedingDocument().getInputStream();
                String diskfilename = "PRO" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getProceedingDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getProceedingDocument().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "PROC");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getCertificateDoc() != null && !award.getCertificateDoc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getCertificateDoc().getInputStream();
                String diskfilename = "CRT" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getCertificateDoc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getCertificateDoc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "CRTR");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getIntegrityCertificateDoc() != null && !award.getIntegrityCertificateDoc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getIntegrityCertificateDoc().getInputStream();
                String diskfilename = "ICD" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getIntegrityCertificateDoc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getIntegrityCertificateDoc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ICD");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getMedicalCertificateDoc() != null && !award.getMedicalCertificateDoc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getMedicalCertificateDoc().getInputStream();
                String diskfilename = "MED" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getMedicalCertificateDoc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getMedicalCertificateDoc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "MED");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getPunishmentDoc() != null && !award.getPunishmentDoc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getPunishmentDoc().getInputStream();
                String diskfilename = "PUN" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getPunishmentDoc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getPunishmentDoc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "PUN");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getEnquiryDoc() != null && !award.getEnquiryDoc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getEnquiryDoc().getInputStream();
                String diskfilename = "ENQ" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getEnquiryDoc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getEnquiryDoc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ENQ");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getDpcDoc() != null && !award.getDpcDoc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getDpcDoc().getInputStream();
                String diskfilename = "DISCPC" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getDpcDoc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getDpcDoc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "DISCPC");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getCourtCaseDoc() != null && !award.getCourtCaseDoc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getCourtCaseDoc().getInputStream();
                String diskfilename = "COURT" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getCourtCaseDoc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getCourtCaseDoc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "COURT");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading1Doc() != null && !award.getAcrGrading1Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading1Doc().getInputStream();
                String diskfilename = "ACR1" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading1Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading1Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR1");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading2Doc() != null && !award.getAcrGrading2Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading2Doc().getInputStream();
                String diskfilename = "ACR2" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading2Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading2Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR2");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading3Doc() != null && !award.getAcrGrading3Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading3Doc().getInputStream();
                String diskfilename = "ACR3" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading3Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading3Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR3");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading4Doc() != null && !award.getAcrGrading4Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading4Doc().getInputStream();
                String diskfilename = "ACR4" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading4Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading4Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR4");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading5Doc() != null && !award.getAcrGrading5Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading5Doc().getInputStream();
                String diskfilename = "ACR5" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading5Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading5Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR5");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading6Doc() != null && !award.getAcrGrading6Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading6Doc().getInputStream();
                String diskfilename = "ACR6" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading6Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading6Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR6");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading7Doc() != null && !award.getAcrGrading7Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading7Doc().getInputStream();
                String diskfilename = "ACR7" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading7Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading7Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR7");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading8Doc() != null && !award.getAcrGrading8Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading8Doc().getInputStream();
                String diskfilename = "ACR8" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading8Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading8Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR8");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading9Doc() != null && !award.getAcrGrading9Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading9Doc().getInputStream();
                String diskfilename = "ACR9" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading9Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading9Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR9");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading10Doc() != null && !award.getAcrGrading10Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading10Doc().getInputStream();
                String diskfilename = "ACR10" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading10Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading10Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR10");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading11Doc() != null && !award.getAcrGrading11Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading11Doc().getInputStream();
                String diskfilename = "ACR11" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading11Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading11Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR11");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading12Doc() != null && !award.getAcrGrading12Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading12Doc().getInputStream();
                String diskfilename = "ACR12" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading12Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading12Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR12");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading13Doc() != null && !award.getAcrGrading13Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading13Doc().getInputStream();
                String diskfilename = "ACR13" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading13Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading13Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR13");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getDiscCCRollone() != null && !award.getDiscCCRollone().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getDiscCCRollone().getInputStream();
                String diskfilename = "CCROLLOne" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getDiscCCRollone().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getDiscCCRollone().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "CCROLLOne");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getDiscCCRolltwo() != null && !award.getDiscCCRolltwo().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getDiscCCRolltwo().getInputStream();
                String diskfilename = "CCROLLTwo" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getDiscCCRolltwo().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getDiscCCRolltwo().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "CCROLLTwo");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getDiscCCRollthree() != null && !award.getDiscCCRollthree().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getDiscCCRollthree().getInputStream();
                String diskfilename = "CCROLLThree" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getDiscCCRollthree().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getDiscCCRollthree().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "CCROLLThree");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getPerformaBDoc() != null && !award.getPerformaBDoc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getPerformaBDoc().getInputStream();
                String diskfilename = "PerformaB" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getPerformaBDoc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getPerformaBDoc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "PerformaB");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getPerformaCDoc() != null && !award.getPerformaCDoc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getPerformaCDoc().getInputStream();
                String diskfilename = "PerformaC" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getPerformaCDoc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getPerformaCDoc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "PerformaC");
                ps.executeUpdate();

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateAwardDataForPresident(AwardMedalListForm award, String filePath) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            con = dataSource.getConnection();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            ps = con.prepareStatement("update police_award_form set doc_present_rank=?, doj_dist_est=?,  money_reward=?,  commendation=?, "
                    + "  gs_mark=?, appreciation=?, aar=?, punishment_major1=?, "
                    + " punishment_minor1=?, award_medal_previous_year=?, "
                    + "  award_medal_rank=?, award_medal_posting_place =?, previously_awarded=?, brief_note=?, dpcifany=?, disc_details=? , "
                    + " completed_status=?, recommend_by_dist=?,  "
                    + " initial_appoint_year=?, initial_appointment_rank=?, initial_appointment_cadre =?, "
                    + " date_since_present_rank=? , present_posting=?, posting_address=?, punishment_details=?, punishment_years=? , pending_enquiry_details=?, "
                    + " court_case_year=? , court_case_charge=?, court_case_status=?, "
                    + " acr_year1=? , acr_year1_grading=? , acr_year2=? , acr_year2_grading=? , acr_year3=? , acr_year3_grading =?,"
                    + " acr_year4=? , acr_year4_grading =?, acr_year5=? , acr_year5_grading=? , other_award=?, further_information_by_range=?,"
                    + " first_name=?,middle_name=?,last_name=?,father_first_name=?,father_second_name=?,father_last_name=?,category=?,"
                    + " initial_appoint_date=?,initial_appoint_service=?,initial_appoint_category=?,total_police_service_years=?,total_police_service_months=?,total_police_service_days=?,"
                    + " present_posting_designation=?,present_posting_address=?,present_posting_place=?,present_posting_pin=?,present_posting_date=?,"
                    + " if_deputation=?,deputation_doj=?,rewards_no=?,rewards_total_amt=?,rewards_cash_awards=?,rewards_commendation=?,rewards_appreciation=?,rewards_good_service=?,rewards_any_other=?,"
                    + " medal_meritorious_service_year=?,medal_meritorious_occassion=?,punishment_penalty_details=?,punishment_penalty_year=?,punishment_penalty_order_no=?,punishment_penalty_order_date=?,"
                    + " medical_category=?,pending_enquiry=?,dpc_year=?,dpc_nature=?,dpc_status=?,court_case_pending_year=?,court_case_pending_details=?,court_case_pending_status=?,"
                    + " acr_grading_1=?,acr_grading_2=?,acr_grading_3=?,acr_grading_4=?,acr_grading_5=?,acr_grading_6=?,acr_grading_7=?,acr_grading_8=?,acr_grading_9=?,acr_grading_10=?,acr_grading_11=?,"
                    + " acr_grading_12=?,acr_grading_13=?,acr_grading_14=?,acr_grading_os=?,acr_grading_vg=?,acr_grading_good=?,acr_grading_avg=?,acr_grading_nic=?,acr_grading_adv=?,acr_grading_ms=?,"
                    + " acr_grading_na=?,email=?,mobile=?,brief_description=?,pending_enquiry_note=?,rewards_cash_awards_amt=?,rewards_commendation_amt=?,rewards_appreciation_amt=?,rewards_good_service_amt=?,rewards_any_other_desc=?,"
                    + " ccr_remarks_one=?,ccr_remarks_two=?,ccr_remarks_three=?,dgps_other_information_district=?,punishment_major_detail=?,punishment_minor_detail=?,"
                    + " punishment_minor_penalty_details=?,punishment_minor_penalty_year=?,punishment_minor_penalty_order_no=?,punishment_minor_penalty_order_date=?,if_meritorious_service_awarded=?,"
                    + " punishment_major2=?,punishment_major3=?,punishment_major4=?,punishment_major5=?,punishment_minor2=?,punishment_minor3=?,punishment_minor4=?,punishment_minor5=?,"
                    + "criminal_details=?,chargesheet_details=?,criminalcase_ifany=?,chargesheet_ifany=?,proceeding_meeting_ifany=?,proceeding_details=?,deputation_detail=?,country=?,nationality=?,state=?,acrgrading_detail=?,acrcopy_ifany=?,property_submitted_if_any=?,property_submitted_on_hrms=?,property_submitted_on_hrms_byofficer=?  where award_medal_id=?");
            if (award.getDocPresentRank() != null && !award.getDocPresentRank().equals("")) {
                ps.setTimestamp(1, new Timestamp(sdf.parse(award.getDocPresentRank()).getTime()));
            } else {
                ps.setTimestamp(1, null);
            }
            if (award.getDojDistEst() != null && !award.getDojDistEst().equals("")) {
                ps.setTimestamp(2, new Timestamp(sdf.parse(award.getDojDistEst()).getTime()));
            } else {
                ps.setTimestamp(2, null);
            }
            if (award.getMoneyReward() != null && !award.getMoneyReward().equals("")) {
                ps.setInt(3, Integer.parseInt(award.getMoneyReward()));
            } else {
                ps.setInt(3, 0);
            }
            if (award.getCommendation() != null && !award.getCommendation().equals("")) {
                ps.setInt(4, Integer.parseInt(award.getCommendation()));
            } else {
                ps.setInt(4, 0);
            }
            if (award.getGsMark() != null && !award.getGsMark().equals("")) {
                ps.setInt(5, Integer.parseInt(award.getGsMark()));
            } else {
                ps.setInt(5, 0);
            }
            if (award.getAppreciation() != null && !award.getAppreciation().equals("")) {
                ps.setInt(6, Integer.parseInt(award.getAppreciation()));
            } else {
                ps.setInt(6, 0);
            }
            if (award.getAar() != null && !award.getAar().equals("")) {
                ps.setInt(7, Integer.parseInt(award.getAar()));
            } else {
                ps.setInt(7, 0);
            }
            if (award.getPunishmentmajor1() != null && !award.getPunishmentmajor1().equals("")) {
                ps.setString(8, award.getPunishmentmajor1());
            } else {
                ps.setString(8, "");
            }

            if (award.getPunishmentminor1() != null && !award.getPunishmentminor1().equals("")) {
                ps.setString(9, award.getPunishmentminor1());
            } else {
                ps.setString(9, "");
            }
            ps.setString(10, award.getAwardMedalPreviousYear());
            ps.setString(11, award.getAwardMedalRank());
            ps.setString(12, award.getAwardMedalPostingPlace());
            ps.setString(13, award.getPrevAwardCmb());
            ps.setString(14, award.getBriefNote());
            ps.setString(15, award.getDpcifany());
            ps.setString(16, award.getDiscDetails());
            ps.setString(17, "Y");
            ps.setString(18, award.getRecommendStatusofDist());
            if (award.getInitialAppointYear() != null && !award.getInitialAppointYear().equals("")) {
                ps.setInt(19, Integer.parseInt(award.getInitialAppointYear()));
            } else {
                ps.setInt(19, 0);
            }

            ps.setString(20, award.getInitialAppointRank());
            ps.setString(21, award.getInitialAppointCadre());
            if (award.getDatePresentRank() != null && !award.getDatePresentRank().equals("")) {
                ps.setTimestamp(22, new Timestamp(sdf.parse(award.getDatePresentRank()).getTime()));
            } else {
                ps.setTimestamp(22, null);
            }
            ps.setString(23, award.getPresentPosting());
            ps.setString(24, award.getPostingAddress());
            ps.setString(25, award.getPunishmentDetails());
            ps.setString(26, award.getPunishmentYears());
            ps.setString(27, award.getPendingEnquiryDetails());

            if (award.getCourtCaseYear() != null && !award.getCourtCaseYear().equals("")) {
                ps.setInt(28, Integer.parseInt(award.getCourtCaseYear()));
            } else {
                ps.setInt(28, 0);
            }
            ps.setString(29, award.getCourtCaseDetails());
            ps.setString(30, award.getCourtCaseStatus());

            if (award.getAcrYear1() != null && !award.getAcrYear1().equals("")) {
                ps.setInt(31, Integer.parseInt(award.getAcrYear1()));
            } else {
                ps.setInt(31, 0);
            }
            ps.setString(32, award.getAcrYear1Grading());

            if (award.getAcrYear2() != null && !award.getAcrYear2().equals("")) {
                ps.setInt(33, Integer.parseInt(award.getAcrYear2()));
            } else {
                ps.setInt(33, 0);
            }
            ps.setString(34, award.getAcrYear2Grading());

            if (award.getAcrYear3() != null && !award.getAcrYear3().equals("")) {
                ps.setInt(35, Integer.parseInt(award.getAcrYear3()));
            } else {
                ps.setInt(35, 0);
            }
            ps.setString(36, award.getAcrYear3Grading());

            if (award.getAcrYear4() != null && !award.getAcrYear4().equals("")) {
                ps.setInt(37, Integer.parseInt(award.getAcrYear4()));
            } else {
                ps.setInt(37, 0);
            }
            ps.setString(38, award.getAcrYear4Grading());

            if (award.getAcrYear5() != null && !award.getAcrYear5().equals("")) {
                ps.setInt(39, Integer.parseInt(award.getAcrYear5()));
            } else {
                ps.setInt(39, 0);
            }
            ps.setString(40, award.getAcrYear5Grading());

            if (award.getOtherReward() != null && !award.getOtherReward().equals("")) {
                ps.setInt(41, Integer.parseInt(award.getOtherReward()));
            } else {
                ps.setInt(41, 0);
            }
            ps.setString(42, award.getFurtherInfoByRange());

            /*President Police Medal*/
            ps.setString(43, award.getFirstName());
            ps.setString(44, award.getMiddleName());
            ps.setString(45, award.getLastName());
            ps.setString(46, award.getFatherfirstname());
            ps.setString(47, award.getFathermiddlename());
            ps.setString(48, award.getFatherlastname());
            ps.setString(49, award.getSltCategory());
            if (award.getInitialAppointDate() != null && !award.getInitialAppointDate().equals("")) {
                ps.setTimestamp(50, new Timestamp(sdf.parse(award.getInitialAppointDate()).getTime()));
            } else {
                ps.setTimestamp(50, null);
            }
            ps.setString(51, award.getInitialAppointService());
            ps.setString(52, award.getInitialAppointCategory());

            if (award.getTotalpoliceyears() != null && !award.getTotalpoliceyears().equals("")) {
                ps.setInt(53, Integer.parseInt(award.getTotalpoliceyears()));
            } else {
                ps.setInt(53, 0);
            }
            if (award.getTotalpolicemonths() != null && !award.getTotalpolicemonths().equals("")) {
                ps.setInt(54, Integer.parseInt(award.getTotalpolicemonths()));
            } else {
                ps.setInt(54, 0);
            }
            if (award.getTotalpolicedays() != null && !award.getTotalpolicedays().equals("")) {
                ps.setInt(55, Integer.parseInt(award.getTotalpolicedays()));
            } else {
                ps.setInt(55, 0);
            }

            ps.setString(56, award.getPresentPosting());
            ps.setString(57, award.getPostingAddress());
            ps.setString(58, award.getPresentPostingPlace());
            if (award.getPresentPostingPIN() != null && !award.getPresentPostingPIN().equals("")) {
                ps.setInt(59, Integer.parseInt(award.getPresentPostingPIN()));
            } else {
                ps.setInt(59, 0);
            }
            if (award.getPresentPostingDate() != null && !award.getPresentPostingDate().equals("")) {
                ps.setTimestamp(60, new Timestamp(sdf.parse(award.getPresentPostingDate()).getTime()));
            } else {
                ps.setTimestamp(60, null);
            }
            ps.setString(61, award.getSltDeputation());
            if (award.getDeputationDate() != null && !award.getDeputationDate().equals("")) {
                ps.setTimestamp(62, new Timestamp(sdf.parse(award.getDeputationDate()).getTime()));
            } else {
                ps.setTimestamp(62, null);
            }

            if (award.getRewardsTotalNo() != null && !award.getRewardsTotalNo().equals("")) {
                ps.setInt(63, Integer.parseInt(award.getRewardsTotalNo()));
            } else {
                ps.setInt(63, 0);
            }
            if (award.getRewardsAmt() != null && !award.getRewardsAmt().equals("")) {
                ps.setInt(64, Integer.parseInt(award.getRewardsAmt()));
            } else {
                ps.setInt(64, 0);
            }
            if (award.getCashAwardsNo() != null && !award.getCashAwardsNo().equals("")) {
                ps.setInt(65, Integer.parseInt(award.getCashAwardsNo()));
            } else {
                ps.setInt(65, 0);
            }
            if (award.getPresidentcommendation() != null && !award.getPresidentcommendation().equals("")) {
                ps.setInt(66, Integer.parseInt(award.getPresidentcommendation()));
            } else {
                ps.setInt(66, 0);
            }
            if (award.getPresidentappreciation() != null && !award.getPresidentappreciation().equals("")) {
                ps.setInt(67, Integer.parseInt(award.getPresidentappreciation()));
            } else {
                ps.setInt(67, 0);
            }
            if (award.getGoodServiceEntries() != null && !award.getGoodServiceEntries().equals("")) {
                ps.setInt(68, Integer.parseInt(award.getGoodServiceEntries()));
            } else {
                ps.setInt(68, 0);
            }
            if (award.getAnyOtherRewards() != null && !award.getAnyOtherRewards().equals("")) {
                ps.setInt(69, Integer.parseInt(award.getAnyOtherRewards()));
            } else {
                ps.setInt(69, 0);
            }

            if (award.getMeritoriousYear() != null && !award.getMeritoriousYear().equals("")) {
                ps.setInt(70, Integer.parseInt(award.getMeritoriousYear()));
            } else {
                ps.setInt(70, 0);
            }
            ps.setString(71, award.getSltOccasion());

            ps.setString(72, award.getPenaltydetails());
            if (award.getPenaltyyear() != null && !award.getPenaltyyear().equals("")) {
                ps.setInt(73, Integer.parseInt(award.getPenaltyyear()));
            } else {
                ps.setInt(73, 0);
            }
            ps.setString(74, award.getPenaltyOrderNo());
            if (award.getPenaltyOrderDate() != null && !award.getPenaltyOrderDate().equals("")) {
                ps.setTimestamp(75, new Timestamp(sdf.parse(award.getPenaltyOrderDate()).getTime()));
            } else {
                ps.setTimestamp(75, null);
            }
            ps.setString(76, award.getMedicalCategory());
            ps.setString(77, award.getIfenquirypending());
            if (award.getDpcyear() != null && !award.getDpcyear().equals("")) {
                ps.setInt(78, Integer.parseInt(award.getDpcyear()));
            } else {
                ps.setInt(78, 0);
            }
            ps.setString(79, award.getDpcnatureallegation());
            ps.setString(80, award.getDpcpresentstatus());
            if (award.getCourtCasePendingYear() != null && !award.getCourtCasePendingYear().equals("")) {
                ps.setInt(81, Integer.parseInt(award.getCourtCasePendingYear()));
            } else {
                ps.setInt(81, 0);
            }
            ps.setString(82, award.getCourtCasePendingDetails());
            ps.setString(83, award.getCourtCasePendingStatus());

            ps.setString(84, award.getAcrGrading1());
            ps.setString(85, award.getAcrGrading2());
            ps.setString(86, award.getAcrGrading3());
            ps.setString(87, award.getAcrGrading4());
            ps.setString(88, award.getAcrGrading5());
            ps.setString(89, award.getAcrGrading6());
            ps.setString(90, award.getAcrGrading7());
            ps.setString(91, award.getAcrGrading8());
            ps.setString(92, award.getAcrGrading9());
            ps.setString(93, award.getAcrGrading10());
            ps.setString(94, award.getAcrGrading11());
            ps.setString(95, award.getAcrGrading12());
            ps.setString(96, award.getAcrGrading13());
            ps.setString(97, award.getAcrGrading14());

            if (award.getAcrGradingOS() != null && !award.getAcrGradingOS().equals("")) {
                ps.setInt(98, Integer.parseInt(award.getAcrGradingOS()));
            } else {
                ps.setInt(98, 0);
            }
            if (award.getAcrGradingVS() != null && !award.getAcrGradingVS().equals("")) {
                ps.setInt(99, Integer.parseInt(award.getAcrGradingVS()));
            } else {
                ps.setInt(99, 0);
            }
            if (award.getAcrGradingGood() != null && !award.getAcrGradingGood().equals("")) {
                ps.setInt(100, Integer.parseInt(award.getAcrGradingGood()));
            } else {
                ps.setInt(100, 0);
            }
            if (award.getAcrGradingAvg() != null && !award.getAcrGradingAvg().equals("")) {
                ps.setInt(101, Integer.parseInt(award.getAcrGradingAvg()));
            } else {
                ps.setInt(101, 0);
            }
            if (award.getAcrGradingNic() != null && !award.getAcrGradingNic().equals("")) {
                ps.setInt(102, Integer.parseInt(award.getAcrGradingNic()));
            } else {
                ps.setInt(102, 0);
            }
            if (award.getAcrGradingAdv() != null && !award.getAcrGradingAdv().equals("")) {
                ps.setInt(103, Integer.parseInt(award.getAcrGradingAdv()));
            } else {
                ps.setInt(103, 0);
            }
            if (award.getAcrGradingMs() != null && !award.getAcrGradingMs().equals("")) {
                ps.setInt(104, Integer.parseInt(award.getAcrGradingMs()));
            } else {
                ps.setInt(104, 0);
            }
            if (award.getAcrGradingNa() != null && !award.getAcrGradingNa().equals("")) {
                ps.setInt(105, Integer.parseInt(award.getAcrGradingNa()));
            } else {
                ps.setInt(105, 0);
            }

            ps.setString(106, award.getEmail());
            ps.setString(107, award.getMobile());
            ps.setString(108, award.getBriefdescription());
            ps.setString(109, award.getEnquirypending());

            if (award.getCashAwardsAmt() != null && !award.getCashAwardsAmt().equals("")) {
                ps.setInt(110, Integer.parseInt(award.getCashAwardsAmt()));
            } else {
                ps.setInt(110, 0);
            }

            if (award.getPresidentcommendationAmt() != null && !award.getPresidentcommendationAmt().equals("")) {
                ps.setInt(111, Integer.parseInt(award.getPresidentcommendationAmt()));
            } else {
                ps.setInt(111, 0);
            }
            if (award.getPresidentappreciationAmt() != null && !award.getPresidentappreciationAmt().equals("")) {
                ps.setInt(112, Integer.parseInt(award.getPresidentappreciationAmt()));
            } else {
                ps.setInt(112, 0);
            }
            if (award.getGoodServiceEntriesAmt() != null && !award.getGoodServiceEntriesAmt().equals("")) {
                ps.setInt(113, Integer.parseInt(award.getGoodServiceEntriesAmt()));
            } else {
                ps.setInt(113, 0);
            }
            ps.setString(114, award.getAnyOtherRewardsDesc());

            ps.setString(115, award.getCcrolloneremarks());
            ps.setString(116, award.getCcrolltworemarks());
            ps.setString(117, award.getCcrollthreeremarks());
            ps.setString(118, award.getOtherInformationFromDistrictDgps());
            ps.setString(119, award.getPunishmentMajorDetails());
            ps.setString(120, award.getPunishmentMinorDetails());
            ps.setString(121, award.getMinorpenaltydetails());
            if (award.getMinorpenaltyyear() != null && !award.getMinorpenaltyyear().equals("")) {
                ps.setInt(122, Integer.parseInt(award.getMinorpenaltyyear()));
            } else {
                ps.setInt(122, 0);
            }
            ps.setString(123, award.getMinorpenaltyOrderNo());
            if (award.getMinorpenaltyOrderDate() != null && !award.getMinorpenaltyOrderDate().equals("")) {
                ps.setTimestamp(124, new Timestamp(sdf.parse(award.getMinorpenaltyOrderDate()).getTime()));
            } else {
                ps.setTimestamp(124, null);
            }
            ps.setString(125, award.getApprovMeritoriousServiceAwarded());

            if (award.getPunishmentmajor2() != null && !award.getPunishmentmajor2().equals("")) {
                ps.setString(126, award.getPunishmentmajor2());
            } else {
                ps.setString(126, "");
            }
            if (award.getPunishmentmajor3() != null && !award.getPunishmentmajor3().equals("")) {
                ps.setString(127, award.getPunishmentmajor3());
            } else {
                ps.setString(127, "");
            }
            if (award.getPunishmentmajor4() != null && !award.getPunishmentmajor4().equals("")) {
                ps.setString(128, award.getPunishmentmajor4());
            } else {
                ps.setString(128, "");
            }
            if (award.getPunishmentmajor5() != null && !award.getPunishmentmajor5().equals("")) {
                ps.setString(129, award.getPunishmentmajor5());
            } else {
                ps.setString(129, "");
            }
            if (award.getPunishmentminor2() != null && !award.getPunishmentminor2().equals("")) {
                ps.setString(130, award.getPunishmentminor2());
            } else {
                ps.setString(130, "");
            }
            if (award.getPunishmentminor3() != null && !award.getPunishmentminor3().equals("")) {
                ps.setString(131, award.getPunishmentminor3());
            } else {
                ps.setString(131, "");
            }
            if (award.getPunishmentminor4() != null && !award.getPunishmentminor4().equals("")) {
                ps.setString(132, award.getPunishmentminor4());
            } else {
                ps.setString(132, "");
            }
            if (award.getPunishmentminor5() != null && !award.getPunishmentminor5().equals("")) {
                ps.setString(133, award.getPunishmentminor5());
            } else {
                ps.setString(133, "");
            }
            ps.setString(134, award.getAnnexure2Details());
            ps.setString(135, award.getAnnexure3Details());
            ps.setString(136, award.getCriminalcaseifany());
            ps.setString(137, award.getChargesheetedifany());
            ps.setString(138, award.getMeetingProceeding());
            ps.setString(139, award.getProceedingDetails());
            ps.setString(140, award.getDeputationDetail());
            
            ps.setString(141, award.getCountry());
            ps.setString(142, award.getNationality());
            ps.setString(143, award.getState());
            ps.setString(144, award.getAcrGradingDetail());
            ps.setString(145, award.getAcrCopyifAny());
            ps.setString(146, award.getPropertyStatementSubmittedifAny());
            if (award.getDateofPropertySubmittedByHRMS() != null && !award.getDateofPropertySubmittedByHRMS().equals("")) {
                ps.setTimestamp(147, new Timestamp(sdf.parse(award.getDateofPropertySubmittedByHRMS()).getTime()));
            } else {
                ps.setTimestamp(147, null);
            }
            if (award.getDateofPropertySubmittedByOfficer() != null && !award.getDateofPropertySubmittedByOfficer().equals("")) {
                ps.setTimestamp(148, new Timestamp(sdf.parse(award.getDateofPropertySubmittedByOfficer()).getTime()));
            } else {
                ps.setTimestamp(148, null);
            }
            ps.setInt(149, Integer.parseInt(award.getRewardMedalId()));
            ps.execute();

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getDifferentRank() != null && !award.getDifferentRank().equals("")) {
                String[] differentRankArr = award.getDifferentRank().split(",");
                String[] differentDateArr = award.getDifferentDate().split(",");
                String[] totaldifferentrankyears = award.getTotaldifferentrankyears().split(",");
                String[] totaldifferentrankmonths = award.getTotaldifferentrankmonths().split(",");
                String[] totaldifferentrankdays = award.getTotaldifferentrankdays().split(",");

                ps = con.prepareStatement("DELETE FROM police_award_form_different_ranks WHERE award_medal_id=?");
                ps.setInt(1, Integer.parseInt(award.getRewardMedalId()));
                ps.execute();

                String sql = "insert into police_award_form_different_ranks(award_medal_id,rank_name,rank_date,rank_year,rank_month,rank_days) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(sql);

                for (int i = 0; i < differentRankArr.length; i++) {
                    ps.setInt(1, Integer.parseInt(award.getRewardMedalId()));
                    ps.setString(2, differentRankArr[i]);
                    if (differentDateArr[i] != null && !differentDateArr[i].equals("")) {
                        ps.setTimestamp(3, new Timestamp(sdf.parse(differentDateArr[i]).getTime()));
                    } else {
                        ps.setTimestamp(3, null);
                    }
                    if (totaldifferentrankyears[i] != null && !totaldifferentrankyears[i].equals("")) {
                        ps.setInt(4, Integer.parseInt(totaldifferentrankyears[i]));
                    } else {
                        ps.setInt(4, 0);
                    }
                    if (totaldifferentrankmonths[i] != null && !totaldifferentrankmonths[i].equals("")) {
                        ps.setInt(5, Integer.parseInt(totaldifferentrankmonths[i]));
                    } else {
                        ps.setInt(5, 0);
                    }
                    if (totaldifferentrankdays[i] != null && !totaldifferentrankdays[i].equals("")) {
                        ps.setInt(6, Integer.parseInt(totaldifferentrankdays[i]));
                    } else {
                        ps.setInt(6, 0);
                    }
                    ps.execute();
                }
            }

            if (award.getServiceBookCopy() != null && !award.getServiceBookCopy().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getServiceBookCopy().getInputStream();
                String diskfilename = "SB" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getServiceBookCopy().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getServiceBookCopy().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "SB");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getDiscDocument() != null && !award.getDiscDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getDiscDocument().getInputStream();
                String diskfilename = "DPC" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getDiscDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getDiscDocument().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "DPC");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getDiscCCROll() != null && !award.getDiscCCROll().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getDiscCCROll().getInputStream();
                String diskfilename = "CCROLL" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getDiscCCROll().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getDiscCCROll().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "CCROLL");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getSrDocument() != null && !award.getSrDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getSrDocument().getInputStream();
                String diskfilename = "SR" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getSrDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getSrDocument().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "SRD");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getNonSrDocument() != null && !award.getNonSrDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getNonSrDocument().getInputStream();
                String diskfilename = "NSR" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getNonSrDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getNonSrDocument().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "NSRD");
                ps.executeUpdate();

            }

            if (award.getProceedingDocument() != null && !award.getProceedingDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getProceedingDocument().getInputStream();
                String diskfilename = "PRO" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getProceedingDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getProceedingDocument().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "PROC");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getCertificateDoc() != null && !award.getCertificateDoc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getCertificateDoc().getInputStream();
                String diskfilename = "CRT" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getCertificateDoc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getCertificateDoc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "CRTR");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getIntegrityCertificateDoc() != null && !award.getIntegrityCertificateDoc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getIntegrityCertificateDoc().getInputStream();
                String diskfilename = "ICD" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getIntegrityCertificateDoc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getIntegrityCertificateDoc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ICD");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getMedicalCertificateDoc() != null && !award.getMedicalCertificateDoc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getMedicalCertificateDoc().getInputStream();
                String diskfilename = "MED" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getMedicalCertificateDoc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getMedicalCertificateDoc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "MED");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getPunishmentDoc() != null && !award.getPunishmentDoc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getPunishmentDoc().getInputStream();
                String diskfilename = "PUN" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getPunishmentDoc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getPunishmentDoc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "PUN");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getEnquiryDoc() != null && !award.getEnquiryDoc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getEnquiryDoc().getInputStream();
                String diskfilename = "ENQ" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getEnquiryDoc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getEnquiryDoc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ENQ");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getDpcDoc() != null && !award.getDpcDoc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getDpcDoc().getInputStream();
                String diskfilename = "DISCPC" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getDpcDoc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getDpcDoc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "DISCPC");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getCourtCaseDoc() != null && !award.getCourtCaseDoc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getCourtCaseDoc().getInputStream();
                String diskfilename = "COURT" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getCourtCaseDoc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getCourtCaseDoc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "COURT");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading1Doc() != null && !award.getAcrGrading1Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading1Doc().getInputStream();
                String diskfilename = "ACR1" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading1Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading1Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR1");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading2Doc() != null && !award.getAcrGrading2Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading2Doc().getInputStream();
                String diskfilename = "ACR2" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading2Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading2Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR2");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading3Doc() != null && !award.getAcrGrading3Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading3Doc().getInputStream();
                String diskfilename = "ACR3" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading3Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading3Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR3");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading4Doc() != null && !award.getAcrGrading4Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading4Doc().getInputStream();
                String diskfilename = "ACR4" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading4Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading4Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR4");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading5Doc() != null && !award.getAcrGrading5Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading5Doc().getInputStream();
                String diskfilename = "ACR5" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading5Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading5Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR5");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading6Doc() != null && !award.getAcrGrading6Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading6Doc().getInputStream();
                String diskfilename = "ACR6" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading6Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading6Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR6");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading7Doc() != null && !award.getAcrGrading7Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading7Doc().getInputStream();
                String diskfilename = "ACR7" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading7Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading7Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR7");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading8Doc() != null && !award.getAcrGrading8Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading8Doc().getInputStream();
                String diskfilename = "ACR8" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading8Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading8Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR8");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading9Doc() != null && !award.getAcrGrading9Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading9Doc().getInputStream();
                String diskfilename = "ACR9" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading9Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading9Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR9");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading10Doc() != null && !award.getAcrGrading10Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading10Doc().getInputStream();
                String diskfilename = "ACR10" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading10Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading10Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR10");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading11Doc() != null && !award.getAcrGrading11Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading11Doc().getInputStream();
                String diskfilename = "ACR11" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading11Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading11Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR11");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading12Doc() != null && !award.getAcrGrading12Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading12Doc().getInputStream();
                String diskfilename = "ACR12" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading12Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading12Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR12");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getAcrGrading13Doc() != null && !award.getAcrGrading13Doc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getAcrGrading13Doc().getInputStream();
                String diskfilename = "ACR13" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getAcrGrading13Doc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getAcrGrading13Doc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "ACR13");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getDiscCCRollone() != null && !award.getDiscCCRollone().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getDiscCCRollone().getInputStream();
                String diskfilename = "CCROLLOne" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getDiscCCRollone().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getDiscCCRollone().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "CCROLLOne");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getDiscCCRolltwo() != null && !award.getDiscCCRolltwo().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getDiscCCRolltwo().getInputStream();
                String diskfilename = "CCROLLTwo" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getDiscCCRolltwo().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getDiscCCRolltwo().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "CCROLLTwo");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getDiscCCRollthree() != null && !award.getDiscCCRollthree().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getDiscCCRollthree().getInputStream();
                String diskfilename = "CCROLLThree" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getDiscCCRollthree().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getDiscCCRollthree().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "CCROLLThree");
                ps.executeUpdate();

            }

            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getMedicalCategoryDoc() != null && !award.getMedicalCategoryDoc().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getMedicalCategoryDoc().getInputStream();
                String diskfilename = "MEDCAT" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getMedicalCategoryDoc().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getMedicalCategoryDoc().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "MEDCAT");
                ps.executeUpdate();

            }
            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getMajorPunishmentDocument() != null && !award.getMajorPunishmentDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getMajorPunishmentDocument().getInputStream();
                String diskfilename = "MAJORPUNISH" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getMajorPunishmentDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getMajorPunishmentDocument().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "MAJORPUNISH");
                ps.executeUpdate();

            }
            DataBaseFunctions.closeSqlObjects(ps);

            if (award.getMinorPunishmentDocument() != null && !award.getMinorPunishmentDocument().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getMinorPunishmentDocument().getInputStream();
                String diskfilename = "MINORPUNISH" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_award_doc ( file_type, file_path, file_name, original_name, data_table_id, doc_name) values(?,?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getMinorPunishmentDocument().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getMinorPunishmentDocument().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getRewardMedalId()));
                ps.setString(6, "MINORPUNISH");
                ps.executeUpdate();

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public FileAttribute getDocument(String filePath, String docName, int attachId) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        File f = null;
        FileAttribute fa = new FileAttribute();
        try {
            con = this.dataSource.getConnection();
            String sql = "";
            if (docName != null && !docName.equals("")) {
                sql = "SELECT * FROM police_award_doc WHERE data_table_id=? and doc_name=? ";
                pst = con.prepareStatement(sql);
                pst.setInt(1, attachId);
                pst.setString(2, docName);
            } else {
                sql = "SELECT * FROM police_award_doc WHERE data_table_id=? ";
                pst = con.prepareStatement(sql);
                pst.setInt(1, attachId);

            }

            rs = pst.executeQuery();
            if (rs.next()) {
                String dirpath = filePath + rs.getString("file_name");

                f = new File(dirpath);
                if (f.exists()) {
                    fa.setDiskFileName(rs.getString("file_name"));
                    fa.setOriginalFileName(rs.getString("original_name"));
                    fa.setFileType(rs.getString("file_type"));
                    fa.setUploadAttachment(f);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fa;
    }

    @Override
    public void deleteAwardeeDetails(int awardMedalId) {
        Connection con = null;

        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("delete from police_award_form where award_medal_id=? ");
            pst.setInt(1, awardMedalId);
            pst.execute();
            pst = con.prepareStatement("delete from police_sr_cases where award_medal_id=? ");
            pst.setInt(1, awardMedalId);
            pst.execute();
            pst = con.prepareStatement("delete from police_investigation where award_medal_id=? ");
            pst.setInt(1, awardMedalId);
            pst.execute();
            pst = con.prepareStatement("delete from police_nonsrcase_detail where award_medal_id=? ");
            pst.setInt(1, awardMedalId);
            pst.execute();
            pst = con.prepareStatement("delete from police_award_doc where data_table_id=?");
            pst.setInt(1, awardMedalId);
            pst.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void submit2Range(int awardMedalId, String offcode) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String rangeId = "";
        try {
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh.mm aa");
            String createdDateTime = dateFormat.format(cal.getTime());
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select range_office_id from police_office_list_data distoff \n"
                    + "	inner join police_office_map2_range_office rangeoff on distoff.est_code=rangeoff.est_code\n"
                    + "	where police_off_code=? ");
            pst.setString(1, offcode);
            rs = pst.executeQuery();
            if (rs.next()) {
                rangeId = rs.getInt("range_office_id") + "";
            }
            pst = con.prepareStatement("update police_award_form set submitted_on=?, submitted_range_off=? where award_medal_id=?");
            pst.setTimestamp(1, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            pst.setString(2, rangeId);
            pst.setInt(3, awardMedalId);
            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getSubmittedAwardListForRange(String offcode, String awardMedalId, String awardYear, String awardoccasion) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List li = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("select award_medal_id, full_name, designation, off_name, submitted_on, range_submitted_on, award_medal_type_id   from police_award_form "
                    + " inner join police_range_office_list prol on police_award_form.submitted_range_off=prol.range_office_id::TEXT "
                    + " where submitted_on is not null and range_office_code=? and award_medal_type_id=? and award_medal_year=? and award_occasion=?"
                    + " order by submitted_on, full_name ");

            ps.setString(1, offcode);
            ps.setString(2, awardMedalId);
            ps.setInt(3, Integer.parseInt(awardYear));
            ps.setString(4, awardoccasion);
            rs = ps.executeQuery();
            while (rs.next()) {
                AwardMedalListForm form = new AwardMedalListForm();
                form.setRewardMedalId(rs.getInt("award_medal_id") + "");
                form.setFullname(rs.getString("full_name"));
                form.setDesignation(rs.getString("designation"));
                form.setOffName(rs.getString("off_name"));
                form.setAwardMedalTypeId(rs.getString("award_medal_type_id"));
                form.setSubmittedOn(CommonFunctions.getFormattedOutputDatAndTime(rs.getTimestamp("submitted_on")));
                form.setRangeSubmittedOn(CommonFunctions.getFormattedOutputDatAndTime(rs.getTimestamp("range_submitted_on")));
                li.add(form);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public void revertNominationDataByRangeOffice(AwardMedalListForm award) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("update police_award_form set submitted_on=null, submitted_range_off=null where award_medal_id=?");
            pst.setInt(1, Integer.parseInt(award.getAwardMedalTypeId()));
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateAwardDataByRange(AwardMedalListForm award) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String rangeId = "";
        try {
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh.mm aa");
            String createdDateTime = dateFormat.format(cal.getTime());
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("update police_award_form set range_submitted_on=?, dg_off_code=?, recommend_by_range=?, not_recommend_reason_by_range=?,further_information_by_range=? where award_medal_id=?");
            pst.setTimestamp(1, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            pst.setString(2, "CTCHOM0050000");
            pst.setString(3, award.getRecommendStatusofRange());
            pst.setString(4, award.getReasonFornotRecommend());
            pst.setString(5, award.getFurtherInfoByRange());
            pst.setInt(6, Integer.parseInt(award.getRewardMedalId()));
            pst.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getAwardeeCompletedList(String awardMedalId, String awardYear, String awardoccasion) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List li = new ArrayList();
        AwardMedalListForm award = new AwardMedalListForm();
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT award_medal_id, emp_id, full_name, dob, designation, doa, "
                    + " award_medal_type_id, award_medal_name, award_medal_year, gpf_no, off_code, off_name, range_office_name, completed_status, submitted_on,"
                    + " submitted_range_off, range_submitted_on, dg_off_code, recommend_by_dist "
                    + " FROM police_award_form "
                    + " left outer join police_range_office_list prol on police_award_form.submitted_range_off=prol.range_office_id::TEXT "
                    + " where award_medal_type_id=? and award_medal_year=? and award_occasion=? and submitted_on is not null "
                    + " order by off_name, full_name");
            ps.setString(1, awardMedalId);
            ps.setInt(2, Integer.parseInt(awardYear));
            ps.setString(3, awardoccasion);
            rs = ps.executeQuery();
            while (rs.next()) {
                award = new AwardMedalListForm();
                award.setRewardMedalId(rs.getInt("award_medal_id") + "");
                award.setOffName(rs.getString("off_name"));
                award.setFullname(rs.getString("full_name"));
                award.setDesignation(rs.getString("designation"));
                award.setDoa(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("doa")));
                award.setGpfNo(rs.getString("gpf_no"));
                award.setSubmittedOn(CommonFunctions.getFormattedOutputDatAndTime(rs.getTimestamp("submitted_on")));
                award.setSubmittedRangeOff(rs.getString("range_office_name"));
                award.setRecommendStatusofDist(rs.getString("recommend_by_dist"));
                li.add(award);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getAwardeeCompletedList(String awardMedalId, String awardYear, String offCode, String awardoccasion) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List li = new ArrayList();
        AwardMedalListForm award = new AwardMedalListForm();
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT award_medal_id, emp_id, full_name, dob, designation, doa, "
                    + " award_medal_type_id, award_medal_name, award_medal_year, gpf_no, off_code, off_name, range_office_name, completed_status, submitted_on,"
                    + " submitted_range_off, range_submitted_on, dg_off_code, recommend_by_dist "
                    + " FROM police_award_form "
                    + " left outer join police_range_office_list prol on police_award_form.submitted_range_off=prol.range_office_id::TEXT "
                    + " where award_medal_type_id=? and award_medal_year=? and submitted_on is not null and off_code=? and award_occasion=?"
                    + " order by off_name, full_name");
            ps.setString(1, awardMedalId);
            ps.setInt(2, Integer.parseInt(awardYear));
            ps.setString(3, offCode);
            ps.setString(4, awardoccasion);
            rs = ps.executeQuery();
            while (rs.next()) {
                award = new AwardMedalListForm();
                award.setRewardMedalId(rs.getInt("award_medal_id") + "");
                award.setOffName(rs.getString("off_name"));
                award.setFullname(rs.getString("full_name"));
                award.setDesignation(rs.getString("designation"));
                award.setDoa(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("doa")));
                award.setGpfNo(rs.getString("gpf_no"));
                award.setSubmittedOn(CommonFunctions.getFormattedOutputDatAndTime(rs.getTimestamp("submitted_on")));
                award.setSubmittedRangeOff(rs.getString("range_office_name"));
                award.setRecommendStatusofDist(rs.getString("recommend_by_dist"));
                li.add(award);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getDistEstList(String awardMedalId, String awardYear) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List li = new ArrayList();
        SelectOption so = new SelectOption();
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT off_code, off_name   "
                    + " FROM police_award_form  "
                    + " where award_medal_type_id=? and award_medal_year=? and submitted_on is not null "
                    + " group by off_code, off_name   "
                    + " order by off_name");
            ps.setString(1, awardMedalId);
            ps.setInt(2, Integer.parseInt(awardYear));

            rs = ps.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setLabel(rs.getString("off_name"));
                so.setValue(rs.getString("off_code"));
                li.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getRangeList(String awardMedalId, String awardYear) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List li = new ArrayList();
        SelectOption so = new SelectOption();
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT submitted_range_off, range_office_name "
                    + " FROM police_award_form "
                    + " inner join police_range_office_list prol on police_award_form.submitted_range_off=prol.range_office_id::TEXT "
                    + " where award_medal_type_id=? and award_medal_year=? and range_submitted_on is not null "
                    + " group by submitted_range_off, range_office_name "
                    + " order by range_office_name");
            ps.setString(1, awardMedalId);
            ps.setInt(2, Integer.parseInt(awardYear));

            rs = ps.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setLabel(rs.getString("range_office_name"));
                so.setValue(rs.getString("submitted_range_off"));
                li.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getAwardeeCompletedByRangeList(String awardMedalId, String awardYear, String rangeCode, String awardoccasion) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List li = new ArrayList();
        AwardMedalListForm award = new AwardMedalListForm();
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT award_medal_id, emp_id, full_name, dob, designation, doa, "
                    + " award_medal_type_id, award_medal_name, award_medal_year, gpf_no, off_code, off_name, range_office_name, completed_status, submitted_on,"
                    + " submitted_range_off, range_submitted_on, dg_off_code, recommend_by_dist "
                    + " FROM police_award_form "
                    + " left outer join police_range_office_list prol on police_award_form.submitted_range_off=prol.range_office_id::TEXT "
                    + " where award_medal_type_id=? and award_medal_year=? and range_submitted_on is not null and submitted_range_off=? and award_occasion=? and recommend_by_range='RECOMMENDED'"
                    + " order by range_office_name, off_name, full_name");

            ps.setString(1, awardMedalId);
            ps.setInt(2, Integer.parseInt(awardYear));
            ps.setString(3, rangeCode);
            ps.setString(4, awardoccasion);

            rs = ps.executeQuery();
            while (rs.next()) {
                award = new AwardMedalListForm();
                award.setRewardMedalId(rs.getInt("award_medal_id") + "");
                award.setOffName(rs.getString("off_name"));
                award.setFullname(rs.getString("full_name"));
                award.setDesignation(rs.getString("designation"));
                award.setDoa(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("doa")));
                award.setGpfNo(rs.getString("gpf_no"));
                award.setSubmittedOn(CommonFunctions.getFormattedOutputDatAndTime(rs.getTimestamp("submitted_on")));
                award.setRangeSubmittedOn(CommonFunctions.getFormattedOutputDatAndTime(rs.getTimestamp("range_submitted_on")));
                award.setSubmittedRangeOff(rs.getString("range_office_name"));
                award.setRecommendStatusofDist(rs.getString("recommend_by_dist"));

                li.add(award);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getAwardeeCompletedByRangeList(String awardMedalId, String awardYear, String awardoccasion) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List li = new ArrayList();
        AwardMedalListForm award = new AwardMedalListForm();
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT award_medal_id, emp_id, full_name, dob, designation, doa, "
                    + " award_medal_type_id, award_medal_name, award_medal_year, gpf_no, off_code, off_name, range_office_name, completed_status, submitted_on,"
                    + " submitted_range_off, range_submitted_on, dg_off_code, recommend_by_dist "
                    + " FROM police_award_form "
                    + " left outer join police_range_office_list prol on police_award_form.submitted_range_off=prol.range_office_id::TEXT "
                    + " where award_medal_type_id=? and award_medal_year=? and award_occasion=? and range_submitted_on is not null and recommend_by_range='RECOMMENDED'"
                    + " order by range_office_name, off_name, full_name");
            ps.setString(1, awardMedalId);
            ps.setInt(2, Integer.parseInt(awardYear));
            ps.setString(3, awardoccasion);
            rs = ps.executeQuery();
            while (rs.next()) {
                award = new AwardMedalListForm();
                award.setRewardMedalId(rs.getInt("award_medal_id") + "");
                award.setOffName(rs.getString("off_name"));
                award.setFullname(rs.getString("full_name"));
                award.setDesignation(rs.getString("designation"));
                award.setDoa(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("doa")));
                award.setGpfNo(rs.getString("gpf_no"));
                award.setSubmittedOn(CommonFunctions.getFormattedOutputDatAndTime(rs.getTimestamp("submitted_on")));
                award.setRangeSubmittedOn(CommonFunctions.getFormattedOutputDatAndTime(rs.getTimestamp("range_submitted_on")));
                award.setSubmittedRangeOff(rs.getString("range_office_name"));
                award.setRecommendStatusofDist(rs.getString("recommend_by_dist"));

                li.add(award);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getAwardeeListNotCompletedByRange(String awardMedalId, String awardYear, String awardoccasion) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List li = new ArrayList();
        AwardMedalListForm award = new AwardMedalListForm();
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT award_medal_id, emp_id, full_name, dob, designation, doa, "
                    + " award_medal_type_id, award_medal_name, award_medal_year, gpf_no, off_code, off_name, range_office_name, completed_status, submitted_on,"
                    + " submitted_range_off, range_submitted_on, dg_off_code, recommend_by_dist, recommend_by_range "
                    + " FROM police_award_form "
                    + " left outer join police_range_office_list prol on police_award_form.submitted_range_off=prol.range_office_id::TEXT "
                    + " where award_medal_type_id=? and award_medal_year=? and award_occasion=? and submitted_on is not null and (range_submitted_on is null) "
                    + " order by off_name, full_name");
            ps.setString(1, awardMedalId);
            ps.setInt(2, Integer.parseInt(awardYear));
            ps.setString(3, awardoccasion);
            rs = ps.executeQuery();
            while (rs.next()) {
                award = new AwardMedalListForm();

                award.setRewardMedalId(rs.getInt("award_medal_id") + "");
                award.setOffName(rs.getString("off_name"));
                award.setFullname(rs.getString("full_name"));
                award.setDesignation(rs.getString("designation"));
                award.setDoa(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("doa")));
                award.setGpfNo(rs.getString("gpf_no"));
                award.setSubmittedOn(CommonFunctions.getFormattedOutputDatAndTime(rs.getTimestamp("submitted_on")));
                award.setSubmittedRangeOff(rs.getString("range_office_name"));
                award.setRecommendStatusofDist(rs.getString("recommend_by_dist"));
                award.setRecommendStatusofRange(rs.getString("recommend_by_range"));
                li.add(award);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public void downloadBroadSheetForAwardMedal(WritableWorkbook workbook, String awardId, String awardYear, String awardoccasion) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int row = 0;

        try {
            con = this.dataSource.getConnection();

            WritableSheet sheet = workbook.createSheet("Broad Sheet", 0);

            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 15, WritableFont.BOLD);
            WritableCellFormat headcell = new WritableCellFormat(headformat);
            headcell.setAlignment(Alignment.CENTRE);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setWrap(true);
            headcell.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableFont headformat2 = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
            WritableCellFormat innerheadcell = new WritableCellFormat(headformat2);
            innerheadcell.setAlignment(Alignment.CENTRE);
            innerheadcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            innerheadcell.setWrap(true);
            innerheadcell.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableFont innerformat = new WritableFont(WritableFont.ARIAL, 12, WritableFont.NO_BOLD);

            WritableCellFormat innercell = new WritableCellFormat(innerformat);
            innercell.setWrap(true);
            innercell.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableCellFormat headcellvertical = new WritableCellFormat(headformat);
            headcellvertical.setWrap(true);
            headcellvertical.setBorder(Border.ALL, BorderLineStyle.THIN);
            headcellvertical.setOrientation(jxl.format.Orientation.PLUS_90);

            WritableCellFormat innercellvertical = new WritableCellFormat(innerformat);
            innercellvertical.setWrap(true);
            innercellvertical.setBorder(Border.ALL, BorderLineStyle.THIN);
            innercellvertical.setOrientation(jxl.format.Orientation.PLUS_90);

            if (awardId.equals("07")) {

                Label label = new Label(0, row, " COMPARATIVE CHART FOR SELECTION OF POLICE PERSONNEL FOR AWARDING OF DGP'S DISC. ON 01.04.2024 WHO HAVE RENDERED SPECTCULAR MERITORIOUS SERVICE DURING THE BUDGETARY YEAR 2022-2023  ", headcell);//column,row
                sheet.setRowView(row, 30 * 30);
                sheet.addCell(label);
                sheet.mergeCells(0, row, 20, row);

                row = row + 1;

                sheet.mergeCells(0, row, 0, row + 1);
                label = new Label(0, row, "SL No", innerheadcell);
                sheet.setRowView(row, 60 * 30);
                sheet.addCell(label);

                sheet.mergeCells(1, row, 1, row + 1);
                label = new Label(1, row, "Name of the Police Personel ", innerheadcell);
                sheet.setColumnView(1, 32);
                sheet.addCell(label);

                sheet.mergeCells(2, row, 2, row + 1);
                label = new Label(2, row, "Designation", innerheadcell);
                sheet.setColumnView(2, 32);
                sheet.addCell(label);

                sheet.mergeCells(3, row, 3, row + 1);
                label = new Label(3, row, "Employee Id", innerheadcell);
                sheet.setColumnView(3, 32);
                sheet.addCell(label);

                sheet.mergeCells(4, row, 4, row + 1);
                label = new Label(4, row, "GPF NO", innerheadcell);
                sheet.setColumnView(4, 32);
                sheet.addCell(label);

                sheet.mergeCells(5, row, 5, row + 1);
                label = new Label(5, row, "Name of the Districts/ Establishment", innerheadcell);
                sheet.setColumnView(5, 32);
                sheet.addCell(label);

                sheet.mergeCells(6, row, 6, row + 1);
                label = new Label(6, row, "Date of Birth", innerheadcell);
                sheet.setColumnView(6, 32);
                sheet.addCell(label);

                sheet.mergeCells(7, row, 7, row + 1);
                label = new Label(7, row, "Date of appointment/ date of continuing in the present rank", innerheadcell);
                sheet.setColumnView(7, 32);
                sheet.addCell(label);

                sheet.mergeCells(8, row, 8, row + 1);
                label = new Label(8, row, "Total Period of service (approx)", innerheadcell);
                sheet.setColumnView(8, 32);
                sheet.addCell(label);

                sheet.mergeCells(9, row, 14, row);
                label = new Label(9, row, "Rewards during year 2023-2024.", innerheadcell);
                sheet.setColumnView(9, 32);
                sheet.addCell(label);

                sheet.mergeCells(15, row, 16, row);
                label = new Label(15, row, "Punishment during year 2023-2024.", innerheadcell);
                sheet.setColumnView(15, 32);
                sheet.addCell(label);

                sheet.mergeCells(17, row, 18, row);
                label = new Label(17, row, "Punishment Preeceding year 2023-2024.", innerheadcell);
                sheet.setColumnView(17, 32);
                sheet.addCell(label);

                sheet.mergeCells(19, row, 19, row + 1);
                label = new Label(19, row, "Whether he/she has received DGP's Disc during the previous year", innerheadcell);
                sheet.setColumnView(19, 32);
                sheet.addCell(label);

                sheet.mergeCells(20, row, 20, row + 1);
                label = new Label(20, row, "Whether any criminal/Vig. case/Departmental Prog. pending against him", innerheadcell);
                sheet.setColumnView(20, 32);
                sheet.addCell(label);

                sheet.mergeCells(21, row, 23, row);
                label = new Label(20, row, "PAR/CCR Synopsis of preceeding 3 year", innerheadcell);
                sheet.setColumnView(20, 32);
                sheet.addCell(label);

                sheet.mergeCells(24, row, 24, row);
                label = new Label(24, row, "Property Statement Submitted /Not", innerheadcell);
                sheet.setColumnView(24, 32);
                sheet.addCell(label);

                sheet.mergeCells(25, row, 25, row);
                label = new Label(25, row, "Property Statement Submitted on HRMS", innerheadcell);
                sheet.setColumnView(25, 32);
                sheet.addCell(label);

                sheet.mergeCells(26, row, 26, row);
                label = new Label(26, row, "Property Statement Submitted By Officer", innerheadcell);
                sheet.setColumnView(26, 32);
                sheet.addCell(label);

                sheet.mergeCells(27, row, 27, row + 1);
                label = new Label(27, row, "Recommendation Status District", innerheadcell);
                sheet.setColumnView(27, 32);
                sheet.addCell(label);

                sheet.mergeCells(28, row, 28, row + 1);
                label = new Label(28, row, "Recommendation Status Range", innerheadcell);
                sheet.setColumnView(28, 32);
                sheet.addCell(label);

                sheet.mergeCells(29, row, 29, row + 1);
                label = new Label(29, row, "Counter signing authority", innerheadcell);
                sheet.setColumnView(29, 32);
                sheet.addCell(label);

                sheet.mergeCells(30, row, 30, row + 1);
                label = new Label(30, row, "Brief description of spectacular meritorious service during the year, 2022-23", innerheadcell);
                sheet.setColumnView(30, 32);
                sheet.addCell(label);

                sheet.mergeCells(31, row, 31, row + 1);
                label = new Label(31, row, "Decision of the selection Committee", innerheadcell);
                sheet.setColumnView(31, 32);
                sheet.addCell(label);

                row = row + 1;
                label = new Label(9, row, "M.R.", innerheadcell);
                sheet.setColumnView(9, 32);
                sheet.addCell(label);

                label = new Label(10, row, "C/H.C.", innerheadcell);
                sheet.setColumnView(10, 32);
                sheet.addCell(label);

                label = new Label(11, row, "GSM", innerheadcell);
                sheet.setColumnView(11, 32);
                sheet.addCell(label);

                label = new Label(12, row, "APP", innerheadcell);
                sheet.setColumnView(12, 32);
                sheet.addCell(label);

                label = new Label(13, row, "ARR", innerheadcell);
                sheet.setColumnView(13, 32);
                sheet.addCell(label);

                label = new Label(14, row, "TOTAL", innerheadcell);
                sheet.setColumnView(14, 32);
                sheet.addCell(label);

                label = new Label(15, row, "MAJOR", innerheadcell);
                sheet.setColumnView(15, 32);
                sheet.addCell(label);

                label = new Label(16, row, "MINOR", innerheadcell);
                sheet.setColumnView(16, 32);
                sheet.addCell(label);

                label = new Label(17, row, "MAJOR", innerheadcell);
                sheet.setColumnView(17, 32);
                sheet.addCell(label);

                label = new Label(18, row, "MINOR", innerheadcell);
                sheet.setColumnView(18, 32);
                sheet.addCell(label);

                label = new Label(21, row, "2020-21", innerheadcell);
                sheet.setColumnView(21, 32);
                sheet.addCell(label);

                label = new Label(22, row, "2021-22", innerheadcell);
                sheet.setColumnView(22, 32);
                sheet.addCell(label);
                
                label = new Label(23, row, "2022-23", innerheadcell);
                sheet.setColumnView(23, 32);
                sheet.addCell(label);

                pst = con.prepareStatement("select off_name, full_name, designation, to_char(dob, 'DD-Mon-YYYY') dob , to_char(doa, 'DD-Mon-YYYY') doa , to_char(doc_present_rank, 'DD-Mon-YYYY') doc_present_rank , doj_dist_est, money_reward, commendation,\n"
                        + " gs_mark, appreciation, aar, punishment_major, punishment_minor, award_medal_previous_year, award_medal_rank,\n"
                        + " award_medal_posting_place, previously_awarded, brief_note, recommend_by_dist, recommend_by_range, \n"
                        + " not_recommend_reason_by_range, dpcifany, disc_details, further_information_by_range,property_submitted_if_any,property_submitted_on_hrms,"
                        + " property_submitted_on_hrms_byofficer,emp_id,gpf_no,ccr_remarks_one,ccr_remarks_two,ccr_remarks_three,punishment_major_dgpdisc,punishment_minor_dgpdisc,punishment_major_penaltypreeciding_number,punishment_minor_penaltypreeciding_number  from police_award_form \n"
                        + " where award_medal_type_id='07' and award_medal_year=? and award_occasion=? and recommend_by_range='RECOMMENDED' "
                        + " order by designation, full_name ");
                pst.setInt(1, Integer.parseInt(awardYear));
                pst.setString(2, awardoccasion);
                rs = pst.executeQuery();
                int i = 0;

                //Converting String to Date
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                Date date = null;

                Date date2 = null;

                while (rs.next()) {
                    i++;
                    row = row + 1;
                    label = new Label(0, row, i + "", innerheadcell);
                    sheet.addCell(label);

                    label = new Label(1, row, rs.getString("full_name"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(2, row, rs.getString("designation"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(3, row, rs.getString("emp_id"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(4, row, rs.getString("gpf_no"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(5, row, rs.getString("off_name"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(6, row, rs.getString("dob"), innerheadcell);
                    sheet.addCell(label);

                    if (rs.getString("doa") != null && !rs.getString("doa").equals("")) {
                        label = new Label(7, row, rs.getString("doa") + " / " + rs.getString("doc_present_rank"), innerheadcell);
                        sheet.addCell(label);
                    } else {
                        label = new Label(7, row, null, innerheadcell);
                        sheet.addCell(label);
                    }

                    if (rs.getString("doa") != null && !rs.getString("doa").equals("")) {
                        date = formatter.parse(rs.getString("doa"));
                        //Converting obtained Date object to LocalDate object
                        Instant instant = date.toInstant();
                        ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
                        LocalDate givenDate = zone.toLocalDate();

                        date2 = formatter.parse("01-JAN-2024");
                        //Converting obtained Date object to LocalDate object
                        Instant instant2 = date2.toInstant();
                        ZonedDateTime zone2 = instant2.atZone(ZoneId.systemDefault());
                        LocalDate nowDate = zone2.toLocalDate();

                        //Calculating the difference between given date to current date.
                        Period period = Period.between(givenDate, nowDate);

                        int serviceYear = period.getYears();
                        int serviceMonth = period.getMonths();
                        int serviceDays = period.getDays();

                        label = new Label(8, row, serviceYear + " years " + serviceMonth + " month(s) " + serviceDays + " day(s)", innerheadcell);
                        sheet.addCell(label);
                    } else {
                        label = new Label(8, row, null, innerheadcell);
                        sheet.addCell(label);
                    }

                    label = new Label(9, row, rs.getInt("money_reward") + "", innerheadcell);
                    sheet.addCell(label);

                    label = new Label(10, row, rs.getInt("commendation") + "", innerheadcell);
                    sheet.addCell(label);

                    label = new Label(11, row, rs.getInt("gs_mark") + "", innerheadcell);
                    sheet.addCell(label);

                    label = new Label(12, row, rs.getInt("appreciation") + "", innerheadcell);
                    sheet.addCell(label);

                    label = new Label(13, row, rs.getInt("aar") + "", innerheadcell);
                    sheet.addCell(label);

                    int totalreward = rs.getInt("money_reward") + rs.getInt("commendation") + rs.getInt("gs_mark") + rs.getInt("appreciation") + rs.getInt("aar");
                    label = new Label(14, row, totalreward + "", innerheadcell);
                    sheet.addCell(label);

                    label = new Label(15, row, rs.getString("punishment_major_dgpdisc"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(16, row, rs.getString("punishment_minor_dgpdisc"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(17, row, rs.getString("punishment_major_penaltypreeciding_number"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(18, row, rs.getString("punishment_minor_penaltypreeciding_number"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(19, row, rs.getString("award_medal_previous_year") + "-" + rs.getString("award_medal_rank") + "-" + rs.getString("award_medal_posting_place"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(20, row, rs.getString("disc_details"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(21, row, rs.getString("ccr_remarks_two"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(22, row, rs.getString("ccr_remarks_three"), innerheadcell);
                    sheet.addCell(label);
                    
                    label = new Label(23, row, rs.getString("ccr_remarks_one"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(24, row, rs.getString("property_submitted_if_any"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(25, row, rs.getString("property_submitted_on_hrms"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(26, row, rs.getString("property_submitted_on_hrms_byofficer"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(27, row, rs.getString("recommend_by_dist"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(28, row, rs.getString("recommend_by_range"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(29, row, "", innerheadcell);
                    sheet.addCell(label);

                    label = new Label(30, row, rs.getString("brief_note"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(31, row, "", innerheadcell);
                    sheet.addCell(label);

                }

            } else if (awardId.equals("09")) {
                Label label = new Label(0, row, "PRESIDENT'S POLICE MEDAL FOR DISTINGUISHED SERVICE/POLICE MEDALS FOR MERITORIOS SERVICE ON THE OCCASION OF REPUBLIC DAY,2022", headcell);//column,row
                sheet.setRowView(row, 30 * 39);
                sheet.addCell(label);
                sheet.mergeCells(0, row, 20, row);

                row = row + 1;

                sheet.mergeCells(0, row, 0, row + 1);
                label = new Label(0, row, "SL No", innerheadcell);
                sheet.setRowView(row, 60 * 39);
                sheet.addCell(label);
                
                sheet.mergeCells(1, row, 1, row + 1);
                label = new Label(1, row, "Name", innerheadcell);
                sheet.setColumnView(1, 39);
                sheet.addCell(label);
                
                 sheet.mergeCells(2, row, 2, row + 1);
                label = new Label(2, row, "Emp Id", innerheadcell);
                sheet.setColumnView(2, 39);
                sheet.addCell(label);
                
                 sheet.mergeCells(3, row, 3, row + 1);
                label = new Label(3, row, "GPF No", innerheadcell);
                sheet.setColumnView(3, 39);
                sheet.addCell(label);
                
                sheet.mergeCells(4, row, 4, row + 1);
                label = new Label(4, row, "Designation", innerheadcell);
                sheet.setColumnView(4, 39);
                sheet.addCell(label);
                
                sheet.mergeCells(5, row, 5, row + 1);
                label = new Label(5, row, "D.O.B", innerheadcell);
                sheet.setColumnView(5, 39);
                sheet.addCell(label);
                
                sheet.mergeCells(6, row, 6, row + 1);
                label = new Label(6, row, "Age as on\n15.08.2023\n(Y-M-D)", innerheadcell);
                sheet.setColumnView(6, 39);
                sheet.addCell(label);
                
                sheet.mergeCells(7, row, 7, row + 1);
                label = new Label(7, row, "Year of Joining", innerheadcell);
                sheet.setColumnView(7, 39);
                sheet.addCell(label);
                
                sheet.mergeCells(8, row, 8, row + 1);
                label = new Label(8, row, "Service as on\n26.01.2022\n(Y-M-D)", innerheadcell);
                sheet.setColumnView(8, 39);
                sheet.addCell(label);
                
                sheet.mergeCells(9, row, 9, row + 1);
                label = new Label(9, row, "If Police Medal for Meritorius Service awarded (Year/Occasion)", headcellvertical);
                sheet.setColumnView(9, 39);
                sheet.addCell(label);
                
                sheet.mergeCells(10, row, 16, row);
                label = new Label(10, row, "Rewards", innerheadcell);
                sheet.setColumnView(10, 39);
                sheet.addCell(label);
                
                sheet.mergeCells(17, row, 19, row);
                label = new Label(17, row, "Punishment", innerheadcell);
                sheet.setColumnView(17, 39);
                sheet.addCell(label);
                
                sheet.mergeCells(20, row, 31, row);
                label = new Label(20, row, "ACR Grading of preceeding 10 Years", innerheadcell);
                sheet.setColumnView(20, 39);
                sheet.addCell(label);
                
                sheet.mergeCells(32, row, 32, row + 1);
                label = new Label(32, row, "Remarks", headcellvertical);
                sheet.setColumnView(32, 39);
                sheet.addCell(label);
                
                sheet.mergeCells(33, row, 33, row + 1);
                label = new Label(33, row, "Date of Recommendation by Range office /Heads of the organisation ", headcellvertical);
                sheet.setColumnView(33, 39);
                sheet.addCell(label);
                
                sheet.mergeCells(34, row, 34, row + 1);
                label = new Label(34, row, " Whether Property Statement Submitted /Not", headcellvertical);
                sheet.setColumnView(34, 39);
                sheet.addCell(label);
                
                sheet.mergeCells(35, row, 35, row + 1);
                label = new Label(35, row, " Date of submission by Officer.", headcellvertical);
                sheet.setColumnView(35, 39);
                sheet.addCell(label);
                
                sheet.mergeCells(36, row, 36, row + 1);
                label = new Label(36, row, " Date of submission by HRMS", headcellvertical);
                sheet.setColumnView(36, 39);
                sheet.addCell(label);
                
                sheet.mergeCells(37, row, 37, row + 1);
                label = new Label(37, row, " Hard copy Page No.", headcellvertical);
                sheet.setColumnView(37, 39);
                sheet.addCell(label);
                
                sheet.mergeCells(38, row, 38, row + 1);
                label = new Label(38, row, "District/Establishment", innerheadcell);
                sheet.setColumnView(38, 39);
                sheet.addCell(label);
                
                sheet.mergeCells(39, row, 39, row + 1);
                label = new Label(39, row, "Application Id", innerheadcell);
                sheet.setColumnView(39, 39);
                sheet.addCell(label);

                row = row + 1;

                label = new Label(10, row, "Cash rewards in Rs", headcellvertical);
                sheet.setColumnView(10, 39);
                sheet.addCell(label);
                label = new Label(11, row, "Cash Rewards", headcellvertical);
                sheet.setColumnView(11, 39);
                sheet.addCell(label);
                label = new Label(12, row, "Commendation", headcellvertical);
                sheet.setColumnView(12, 39);
                sheet.addCell(label);
                label = new Label(13, row, "Appreciation", headcellvertical);
                sheet.setColumnView(13, 39);
                sheet.addCell(label);
                label = new Label(14, row, "Good Services", headcellvertical);
                sheet.setColumnView(14, 39);
                sheet.addCell(label);
                label = new Label(15, row, "Others", headcellvertical);
                sheet.setColumnView(15, 39);
                sheet.addCell(label);
                label = new Label(16, row, "Total", headcellvertical);
                sheet.setColumnView(16, 39);
                sheet.addCell(label);
                label = new Label(17, row, "Major", headcellvertical);
                sheet.setColumnView(17, 39);
                sheet.addCell(label);
                label = new Label(18, row, "Minor", headcellvertical);
                sheet.setColumnView(18, 39);
                sheet.addCell(label);
                label = new Label(19, row, "Total", headcellvertical);
                sheet.setColumnView(19, 39);
                sheet.addCell(label);
                label = new Label(20, row, "2010-11", headcellvertical);
                sheet.setColumnView(20, 39);
                sheet.addCell(label);
                label = new Label(21, row, "2011-12", headcellvertical);
                sheet.setColumnView(21, 39);
                sheet.addCell(label);
                label = new Label(22, row, "2012-13", headcellvertical);
                sheet.setColumnView(22, 39);
                sheet.addCell(label);
                label = new Label(23, row, "2013-14", headcellvertical);
                sheet.setColumnView(23, 39);
                sheet.addCell(label);
                label = new Label(24, row, "2014-15", headcellvertical);
                sheet.setColumnView(24, 39);
                sheet.addCell(label);
                label = new Label(25, row, "2015-16", headcellvertical);
                sheet.setColumnView(25, 39);
                sheet.addCell(label);
                label = new Label(26, row, "2016-17", headcellvertical);
                sheet.setColumnView(26, 39);
                sheet.addCell(label);
                label = new Label(27, row, "2017-18", headcellvertical);
                sheet.setColumnView(27, 39);
                sheet.addCell(label);
                label = new Label(28, row, "2018-19", headcellvertical);
                sheet.setColumnView(28, 39);
                sheet.addCell(label);
                label = new Label(29, row, "2019-20", headcellvertical);
                sheet.setColumnView(29, 39);
                sheet.addCell(label);
                label = new Label(30, row, "2020-21", headcellvertical);
                sheet.setColumnView(30, 39);
                sheet.addCell(label);
                label = new Label(31, row, "2021-22", headcellvertical);
                sheet.setColumnView(31, 39);
                sheet.addCell(label);

                int i = 0;

                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

                pst = con.prepareStatement("select emp_id,gpf_no,off_name, full_name, designation, dob , to_char(dob, 'DD-Mon-YYYY') dateofbirth, to_char(doa, 'DD-Mon-YYYY') doa , to_char(doc_present_rank, 'DD-Mon-YYYY') doc_present_rank , doj_dist_est, money_reward, commendation,"
                        + "gs_mark, appreciation, aar, punishment_major, punishment_minor, award_medal_previous_year, award_medal_rank,"
                        + "award_medal_posting_place, previously_awarded, brief_note, recommend_by_dist, recommend_by_range,range_submitted_on,"
                        + "not_recommend_reason_by_range, dpcifany, disc_details, further_information_by_range,age_in_year, age_in_month,first_name,middle_name,last_name,father_first_name,father_second_name,father_last_name,category,"
                        + "initial_appoint_date,initial_appoint_service,initial_appoint_category,total_police_service_years,total_police_service_months,total_police_service_days,"
                        + "present_posting_designation,present_posting_address,present_posting_place,present_posting_pin,present_posting_date,"
                        + "if_deputation,deputation_doj,rewards_no,rewards_total_amt,rewards_cash_awards,rewards_commendation,rewards_appreciation,rewards_good_service,rewards_any_other,"
                        + "medal_meritorious_service_year,medal_meritorious_occassion,punishment_penalty_details,punishment_penalty_year,punishment_penalty_order_no,punishment_penalty_order_date,"
                        + "medical_category,pending_enquiry,dpc_year,dpc_nature,dpc_status,court_case_pending_year,court_case_pending_details,court_case_pending_status,"
                        + "acr_grading_1,acr_grading_2,acr_grading_3,acr_grading_4,acr_grading_5,acr_grading_6,acr_grading_7,acr_grading_8,acr_grading_9,acr_grading_10,acr_grading_11,"
                        + "acr_grading_12,acr_grading_13,acr_grading_14,acr_grading_os,acr_grading_vg,acr_grading_good,acr_grading_avg,acr_grading_nic,acr_grading_adv,acr_grading_ms,"
                        + "acr_grading_na,email,mobile,brief_description,pending_enquiry_note,rewards_cash_awards_amt,rewards_commendation_amt,rewards_appreciation_amt,rewards_good_service_amt,rewards_any_other_desc,award_medal_id,property_submitted_if_any,property_submitted_on_hrms,property_submitted_on_hrms_byofficer from police_award_form"
                        + " where award_medal_type_id='09' and award_medal_year=? and range_submitted_on is not null and recommend_by_range='RECOMMENDED' and award_occasion=?"
                        + " order by designation, full_name ");
                pst.setInt(1, Integer.parseInt(awardYear));
                pst.setString(2, awardoccasion);
                rs = pst.executeQuery();
                while (rs.next()) {
                    row = row + 1;

                    i = i + 1;

                    String totalService = StringUtils.defaultString(rs.getString("total_police_service_years")) + "-" + StringUtils.defaultString(rs.getString("total_police_service_months")) + "-" + StringUtils.defaultString(rs.getString("total_police_service_days"));

                    String dob = CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("dob"));
                    Date date = formatter.parse(dob);
                    //Converting obtained Date object to LocalDate object
                    Instant instant = date.toInstant();
                    ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
                    LocalDate givenDate = zone.toLocalDate();

                    Date date2 = formatter.parse("26-JAN-2024");

                    //Converting obtained Date object to LocalDate object
                    Instant instant2 = date2.toInstant();
                    ZonedDateTime zone2 = instant2.atZone(ZoneId.systemDefault());
                    LocalDate nowDate = zone2.toLocalDate();

                    //Calculating the difference between given date to current date.
                    Period period = Period.between(givenDate, nowDate);

                    String employeeage = period.getYears() + "-" + period.getMonths() + "-" + period.getDays();

                    sheet.mergeCells(0, row, 0, row);
                    label = new Label(0, row, i + "", innercell);
                    sheet.setRowView(row, 60 * 39);
                    sheet.addCell(label);
                    sheet.mergeCells(1, row, 1, row);
                    label = new Label(1, row, StringUtils.defaultString(rs.getString("full_name")), innercell);
                    sheet.setColumnView(1, 39);
                    sheet.addCell(label);
                    
                    sheet.mergeCells(2, row, 2, row);
                    label = new Label(2, row, StringUtils.defaultString(rs.getString("designation")), innercell);
                    sheet.setColumnView(2, 39);
                    sheet.addCell(label);
                    
                    sheet.mergeCells(3, row, 3, row);
                    label = new Label(3, row, StringUtils.defaultString(rs.getString("emp_id")), innercell);
                    sheet.setColumnView(3, 39);
                    sheet.addCell(label);
                    
                    sheet.mergeCells(4, row, 4, row);
                    label = new Label(4, row, StringUtils.defaultString(rs.getString("gpf_no")), innercell);
                    sheet.setColumnView(4, 39);
                    sheet.addCell(label);
                    
                    sheet.mergeCells(5, row, 5, row);
                    label = new Label(5, row, StringUtils.defaultString(rs.getString("dateofbirth")), innercell);
                    sheet.setColumnView(5, 39);
                    sheet.addCell(label);
                    
                    sheet.mergeCells(6, row, 6, row);
                    label = new Label(6, row, employeeage, innercell);
                    sheet.setColumnView(6, 39);
                    sheet.addCell(label);
                    
                    sheet.mergeCells(7, row, 7, row);
                    label = new Label(7, row, CommonFunctions.getFormattedOutputDate1(rs.getDate("initial_appoint_date")), innercell); //Year of Joining
                    sheet.setColumnView(7, 39);
                    sheet.addCell(label);
                    
                    sheet.mergeCells(8, row, 8, row);
                    label = new Label(8, row, totalService, innercell);
                    sheet.setColumnView(8, 39);
                    sheet.addCell(label);
                    
                    sheet.mergeCells(9, row, 9, row);
                    label = new Label(9, row, StringUtils.defaultString(rs.getString("medal_meritorious_service_year")) + "/" + StringUtils.defaultString(rs.getString("medal_meritorious_occassion")), innercellvertical);
                    sheet.setColumnView(9, 39);
                    sheet.addCell(label);
                    
                    label = new Label(10, row, StringUtils.defaultString(rs.getString("rewards_total_amt")), innercellvertical);
                    sheet.setColumnView(10, 39);
                    sheet.addCell(label);
                    label = new Label(11, row, StringUtils.defaultString(rs.getString("rewards_cash_awards")), innercellvertical);
                    sheet.setColumnView(11, 39);
                    sheet.addCell(label);
                    label = new Label(12, row, StringUtils.defaultString(rs.getString("rewards_commendation")), innercellvertical);
                    sheet.setColumnView(12, 39);
                    sheet.addCell(label);
                    label = new Label(13, row, StringUtils.defaultString(rs.getString("rewards_appreciation")), innercellvertical);
                    sheet.setColumnView(13, 39);
                    sheet.addCell(label);
                    
                    label = new Label(14, row, StringUtils.defaultString(rs.getString("rewards_good_service")), innercellvertical);
                    sheet.setColumnView(14, 39);
                    sheet.addCell(label);
                    
                    label = new Label(15, row, StringUtils.defaultString(rs.getString("rewards_any_other")), innercellvertical);
                    sheet.setColumnView(15, 39);
                    sheet.addCell(label);
                    
                    label = new Label(16, row, "", innercellvertical); //Total
                    sheet.setColumnView(16, 39);
                    sheet.addCell(label);
                    
                    label = new Label(17, row, "", innercellvertical); //Major
                    sheet.setColumnView(17, 39);
                    sheet.addCell(label);
                    
                    label = new Label(18, row, "", innercellvertical); //Minor
                    sheet.setColumnView(18, 39);
                    sheet.addCell(label);
                    
                    label = new Label(19, row, "", innercellvertical); //Total
                    sheet.setColumnView(19, 39);
                    sheet.addCell(label);
                    
                    label = new Label(20, row, StringUtils.defaultString(rs.getString("acr_grading_4")), innercellvertical);
                    sheet.setColumnView(20, 39);
                    sheet.addCell(label);
                    
                    label = new Label(21, row, StringUtils.defaultString(rs.getString("acr_grading_5")), innercellvertical);
                    sheet.setColumnView(21, 39);
                    sheet.addCell(label);
                    
                    label = new Label(22, row, StringUtils.defaultString(rs.getString("acr_grading_6")), innercellvertical);
                    sheet.setColumnView(22, 39);
                    sheet.addCell(label);
                    
                    label = new Label(23, row, StringUtils.defaultString(rs.getString("acr_grading_7")), innercellvertical);
                    sheet.setColumnView(23, 39);
                    sheet.addCell(label);
                    
                    label = new Label(24, row, StringUtils.defaultString(rs.getString("acr_grading_8")), innercellvertical);
                    sheet.setColumnView(24, 39);
                    sheet.addCell(label);
                    
                    label = new Label(25, row, StringUtils.defaultString(rs.getString("acr_grading_9")), innercellvertical);
                    sheet.setColumnView(25, 39);
                    sheet.addCell(label);
                    
                    label = new Label(26, row, StringUtils.defaultString(rs.getString("acr_grading_10")), innercellvertical);
                    sheet.setColumnView(26, 39);
                    sheet.addCell(label);
                    
                    label = new Label(27, row, StringUtils.defaultString(rs.getString("acr_grading_11")), innercellvertical);
                    sheet.setColumnView(27, 39);
                    sheet.addCell(label);
                    
                    label = new Label(28, row, StringUtils.defaultString(rs.getString("acr_grading_12")), innercellvertical);
                    sheet.setColumnView(28, 39);
                    sheet.addCell(label);
                    
                    label = new Label(29, row, StringUtils.defaultString(rs.getString("acr_grading_13")), innercellvertical);
                    sheet.setColumnView(29, 39);
                    sheet.addCell(label);
                    
                    label = new Label(30, row, StringUtils.defaultString(rs.getString("acr_grading_1")), innercellvertical);
                    sheet.setColumnView(30, 39);
                    sheet.addCell(label);
                    
                    label = new Label(31, row, StringUtils.defaultString(rs.getString("acr_grading_2")), innercellvertical);
                    sheet.setColumnView(31, 39);
                    sheet.addCell(label);
                    
                    label = new Label(32, row, "", innercellvertical);//StringUtils.defaultString(rs.getString("brief_description"))
                    sheet.setColumnView(32, 39);
                    sheet.addCell(label);
                    
                    label = new Label(33, row, "", innercellvertical);//StringUtils.defaultString(rs.getString("brief_description"))
                    sheet.setColumnView(33, 39);
                    sheet.addCell(label);
                    
                    sheet.mergeCells(34, row, 34, row);
                    label = new Label(34, row, rs.getString("property_submitted_if_any"), innercellvertical);
                    sheet.setColumnView(34, 39);
                    sheet.addCell(label);
                    
                    sheet.mergeCells(35, row, 35, row);
                    label = new Label(35, row, CommonFunctions.getFormattedOutputDate1(rs.getDate("property_submitted_on_hrms")), innercellvertical);
                    sheet.setColumnView(35, 39);
                    sheet.addCell(label);
                    
                    sheet.mergeCells(36, row, 36, row);
                    label = new Label(36, row, CommonFunctions.getFormattedOutputDate1(rs.getDate("property_submitted_on_hrms_byofficer")), innercellvertical);
                    sheet.setColumnView(36, 39);
                    sheet.addCell(label);
                    
                    sheet.mergeCells(37, row, 37, row);
                    label = new Label(37, row, CommonFunctions.getFormattedOutputDate1(rs.getDate("range_submitted_on")), innercellvertical);
                    sheet.setColumnView(37, 39);
                    sheet.addCell(label);
                    
                    sheet.mergeCells(38, row, 38, row);
                    label = new Label(38, row, "", innercellvertical); //Hard copy Page No.
                    sheet.setColumnView(38, 39);
                    sheet.addCell(label);
                    
                    sheet.mergeCells(38, row, 38, row);
                    label = new Label(38, row, rs.getString("off_name"), innercell); //District/Establishment.
                    sheet.setColumnView(38, 39);
                    sheet.addCell(label);
                    
                    
                    sheet.mergeCells(39, row, 39, row);
                    label = new Label(39, row, rs.getString("award_medal_id"), innercell); //District/Establishment.
                    sheet.setColumnView(39, 39);
                    sheet.addCell(label);
                }

            } else if (awardId.equals("06")) {

                Label label = new Label(0, row, "GOVERNOR’S MEDAL TO POLICE PERSONNEL ON THE OCCASION OF REPUBLIC DAY,2024", headcell);//column,row
                sheet.setRowView(row, 30 * 30);
                sheet.addCell(label);
                sheet.mergeCells(0, row, 20, row);

                row = row + 1;

                sheet.mergeCells(0, row, 0, row + 1);
                label = new Label(0, row, "SL No", innerheadcell);
                sheet.setRowView(row, 60 * 30);
                sheet.addCell(label);
                sheet.mergeCells(1, row, 1, row + 1);
                label = new Label(1, row, "Name", innerheadcell);
                sheet.setColumnView(1, 34);
                sheet.addCell(label);
                sheet.mergeCells(2, row, 2, row + 1);
                label = new Label(2, row, "Designation", innerheadcell);
                sheet.setColumnView(2, 34);
                sheet.addCell(label);
                sheet.mergeCells(3, row, 3, row + 1);
                label = new Label(3, row, "D.O.B", innerheadcell);
                sheet.setColumnView(3, 34);
                sheet.addCell(label);
                sheet.mergeCells(4, row, 4, row + 1);
                label = new Label(4, row, "GPF No", innerheadcell);
                sheet.setColumnView(4, 34);
                sheet.addCell(label);
                sheet.mergeCells(5, row, 5, row + 1);
                label = new Label(5, row, "Age as on\n26.01.2023\n(Y-M-D)", innerheadcell);
                sheet.setColumnView(5, 34);
                sheet.addCell(label);
                sheet.mergeCells(6, row, 6, row + 1);
                label = new Label(6, row, "Category", innerheadcell);
                sheet.setColumnView(6, 34);
                sheet.addCell(label);
                sheet.mergeCells(7, row, 7, row + 1);
                label = new Label(7, row, "Year of Joining", innerheadcell);
                sheet.setColumnView(7, 34);
                sheet.addCell(label);
                sheet.mergeCells(8, row, 8, row + 1);
                label = new Label(8, row, "Service as on\n31.12.2023\n(Y-M-D)", innerheadcell);
                sheet.setColumnView(8, 34);
                sheet.addCell(label);
                sheet.mergeCells(9, row, 9, row + 1);
                label = new Label(9, row, "If Governor Medal Awarded previously(Year)", headcellvertical);
                sheet.setColumnView(9, 34);
                sheet.addCell(label);
                sheet.mergeCells(10, row, 16, row);
                label = new Label(10, row, "Rewards", innerheadcell);
                sheet.setColumnView(10, 34);
                sheet.addCell(label);
                sheet.mergeCells(17, row, 18, row);
                label = new Label(17, row, "Punishment", innerheadcell);
                sheet.setColumnView(17, 34);
                sheet.addCell(label);

                sheet.mergeCells(20, row, 25, row);
                label = new Label(20, row, "ACR Grading of preceeding 06 Years", innerheadcell);
                sheet.setColumnView(20, 34);
                sheet.addCell(label);

                label = new Label(26, row, "Remarks", headcellvertical);
                sheet.setColumnView(26, 34);
                sheet.addCell(label);

                label = new Label(27, row, "Date of Recommendation by Range office /Heads of the organisation ", headcellvertical);
                sheet.setColumnView(27, 34);
                sheet.addCell(label);
                
                label = new Label(28, row, "Property Statement Submitted /Not ", headcellvertical);
                sheet.setColumnView(28, 34);
                sheet.addCell(label);
                
                label = new Label(29, row, "Property Statement Submitted on HRMS ", headcellvertical);
                sheet.setColumnView(29, 34);
                sheet.addCell(label);
                
                label = new Label(30, row, "Property Statement Submitted By Officer ", headcellvertical);
                sheet.setColumnView(30, 34);
                sheet.addCell(label);

                label = new Label(31, row, " Hard copy Page No.", headcellvertical);
                sheet.setColumnView(31, 34);
                sheet.addCell(label);

                label = new Label(32, row, "District/Establishment", innerheadcell);
                sheet.setColumnView(32, 34);
                sheet.addCell(label);

                label = new Label(33, row, "Application Id", innerheadcell);
                sheet.setColumnView(33, 34);
                sheet.addCell(label);

                row = row + 1;

                label = new Label(10, row, "Cash rewards in Rs", headcellvertical);
                sheet.setColumnView(10, 12);
                sheet.addCell(label);
                label = new Label(11, row, "Cash Rewards", headcellvertical);
                sheet.setColumnView(11, 12);
                sheet.addCell(label);
                label = new Label(12, row, "Commendation", headcellvertical);
                sheet.setColumnView(12, 12);
                sheet.addCell(label);
                label = new Label(13, row, "Appreciation", headcellvertical);
                sheet.setColumnView(13, 12);
                sheet.addCell(label);
                label = new Label(14, row, "Good Services", headcellvertical);
                sheet.setColumnView(14, 12);
                sheet.addCell(label);
                label = new Label(15, row, "Others", headcellvertical);
                sheet.setColumnView(15, 12);
                sheet.addCell(label);
                label = new Label(16, row, "Total", headcellvertical);
                sheet.setColumnView(16, 12);
                sheet.addCell(label);
                label = new Label(17, row, "Major", headcellvertical);
                sheet.setColumnView(17, 12);
                sheet.addCell(label);
                label = new Label(18, row, "Minor", headcellvertical);
                sheet.setColumnView(18, 12);
                sheet.addCell(label);
                label = new Label(19, row, "Year", headcellvertical);
                sheet.setColumnView(19, 12);
                sheet.addCell(label);
                
                label = new Label(20, row, "2016-17", headcellvertical);
                sheet.setColumnView(20, 12);
                sheet.addCell(label);
                label = new Label(21, row, "2017-18", headcellvertical);
                sheet.setColumnView(21, 12);
                sheet.addCell(label);
                label = new Label(22, row, "2018-19", headcellvertical);
                sheet.setColumnView(22, 12);
                sheet.addCell(label);
                label = new Label(23, row, "2019-20", headcellvertical);
                sheet.setColumnView(23, 12);
                sheet.addCell(label);
                label = new Label(24, row, "2020-21", headcellvertical);
                sheet.setColumnView(24, 12);
                sheet.addCell(label);
                label = new Label(25, row, "2021-22", headcellvertical);
                sheet.setColumnView(25, 12);
                sheet.addCell(label);

                int i = 0;

                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

                pst = con.prepareStatement("select police_award_form.off_name,off_en, full_name, designation, dob , to_char(dob, 'DD-Mon-YYYY') dateofbirth, gpf_no,to_char(doa, 'DD-Mon-YYYY') doa , to_char(doc_present_rank, 'DD-Mon-YYYY') doc_present_rank , doj_dist_est, money_reward, commendation,"
                        + "gs_mark, appreciation, aar, punishment_major, punishment_minor, award_medal_previous_year, award_medal_rank,"
                        + "award_medal_posting_place, previously_awarded, brief_note, recommend_by_dist, recommend_by_range,range_submitted_on,"
                        + "not_recommend_reason_by_range, dpcifany, disc_details, further_information_by_range,age_in_year, age_in_month,first_name,middle_name,last_name,father_first_name,father_second_name,father_last_name,police_award_form.category,"
                        + "initial_appoint_date,initial_appoint_service,initial_appoint_category,total_police_service_years,total_police_service_months,total_police_service_days,"
                        + "present_posting_designation,present_posting_address,present_posting_place,present_posting_pin,present_posting_date,"
                        + "if_deputation,deputation_doj,rewards_no,rewards_total_amt,rewards_cash_awards,rewards_commendation,rewards_appreciation,rewards_good_service,rewards_any_other,"
                        + "medal_meritorious_service_year,medal_meritorious_occassion,punishment_penalty_details,punishment_penalty_year,punishment_penalty_order_no,punishment_penalty_order_date,"
                        + "medical_category,pending_enquiry,dpc_year,dpc_nature,dpc_status,court_case_pending_year,court_case_pending_details,court_case_pending_status,"
                        + "acr_grading_1,acr_grading_2,acr_grading_3,acr_grading_4,acr_grading_5,acr_grading_6,acr_grading_7,acr_grading_8,acr_grading_9,acr_grading_10,acr_grading_11,"
                        + "acr_grading_12,acr_grading_13,acr_grading_14,acr_grading_os,acr_grading_vg,acr_grading_good,acr_grading_avg,acr_grading_nic,acr_grading_adv,acr_grading_ms,"
                        + "acr_grading_na,email,mobile,brief_description,pending_enquiry_note,rewards_cash_awards_amt,rewards_commendation_amt,rewards_appreciation_amt,rewards_good_service_amt,"
                        + "property_submitted_if_any,property_submitted_on_hrms,property_submitted_on_hrms_byofficer,rewards_any_other_desc,is_governor_medal_earlier,award_medal_id,"
                        + "if_major_punishment,if_minor_punishment from police_award_form "
                        + "inner join g_office on police_award_form.off_code = g_office.off_code "
                        + "where award_medal_type_id='06' and award_medal_year=? and recommend_by_range='RECOMMENDED'"
                        + "order by designation, full_name ");
                pst.setInt(1, Integer.parseInt(awardYear));
                rs = pst.executeQuery();
                while (rs.next()) {
                    row = row + 1;

                    i = i + 1;

                    String totalService = StringUtils.defaultString(rs.getString("total_police_service_years")) + "-" + StringUtils.defaultString(rs.getString("total_police_service_months")) + "-" + StringUtils.defaultString(rs.getString("total_police_service_days"));

                    String dob = CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("dob"));
                    Date date = formatter.parse(dob);
                    //Converting obtained Date object to LocalDate object
                    Instant instant = date.toInstant();
                    ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
                    LocalDate givenDate = zone.toLocalDate();

                    Date date2 = formatter.parse("26-JAN-2023");

                    //Converting obtained Date object to LocalDate object
                    Instant instant2 = date2.toInstant();
                    ZonedDateTime zone2 = instant2.atZone(ZoneId.systemDefault());
                    LocalDate nowDate = zone2.toLocalDate();

                    //Calculating the difference between given date to current date.
                    Period period = Period.between(givenDate, nowDate);

                    String employeeage = period.getYears() + "-" + period.getMonths() + "-" + period.getDays();

                    sheet.mergeCells(0, row, 0, row);
                    label = new Label(0, row, i + "", innercell);
                    sheet.setRowView(row, 60 * 30);
                    sheet.addCell(label);
                    sheet.mergeCells(1, row, 1, row);
                    label = new Label(1, row, StringUtils.defaultString(rs.getString("full_name")), innercell);
                    sheet.setColumnView(1, 34);
                    sheet.addCell(label);
                    sheet.mergeCells(2, row, 2, row);
                    label = new Label(2, row, StringUtils.defaultString(rs.getString("designation")), innercell);
                    sheet.setColumnView(2, 34);
                    sheet.addCell(label);
                    sheet.mergeCells(3, row, 3, row);
                    label = new Label(3, row, StringUtils.defaultString(rs.getString("dateofbirth")), innercell);
                    sheet.setColumnView(3, 34);
                    sheet.addCell(label);
                    sheet.mergeCells(4, row, 4, row);
                    label = new Label(4, row, StringUtils.defaultString(rs.getString("gpf_no")), innercell);
                    sheet.setColumnView(4, 34);
                    sheet.addCell(label);
                    sheet.mergeCells(5, row, 5, row);
                    label = new Label(5, row, employeeage, innercell);
                    sheet.setColumnView(5, 20);
                    sheet.addCell(label);
                    sheet.mergeCells(6, row, 6, row);
                    label = new Label(6, row, StringUtils.defaultString(rs.getString("category")), innercell);
                    sheet.setColumnView(6, 34);
                    sheet.addCell(label);
                    sheet.mergeCells(7, row, 7, row);
                    label = new Label(7, row, CommonFunctions.getFormattedOutputDate1(rs.getDate("initial_appoint_date")), innercell); //Year of Joining
                    sheet.setColumnView(7, 34);
                    sheet.addCell(label);
                    sheet.mergeCells(8, row, 8, row);
                    label = new Label(8, row, StringUtils.defaultString(rs.getString("total_police_service_years")) + "/" + StringUtils.defaultString(rs.getString("total_police_service_months")) + "/" + StringUtils.defaultString(rs.getString("total_police_service_days")), innercell);
                    sheet.setColumnView(8, 20);
                    sheet.addCell(label);
                    sheet.mergeCells(9, row, 9, row);
                    label = new Label(9, row, StringUtils.defaultString(rs.getString("is_governor_medal_earlier")), innercellvertical);
                    sheet.setColumnView(9, 16);
                    sheet.addCell(label);
                    label = new Label(10, row, StringUtils.defaultString(rs.getString("rewards_cash_awards_amt")), innercellvertical);
                    sheet.setColumnView(10, 12);
                    sheet.addCell(label);
                    label = new Label(11, row, StringUtils.defaultString(rs.getString("rewards_cash_awards")), innercellvertical);
                    sheet.setColumnView(11, 12);
                    sheet.addCell(label);
                    label = new Label(12, row, StringUtils.defaultString(rs.getString("rewards_commendation")), innercellvertical);
                    sheet.setColumnView(12, 12);
                    sheet.addCell(label);
                    label = new Label(13, row, StringUtils.defaultString(rs.getString("rewards_appreciation")), innercellvertical);
                    sheet.setColumnView(13, 12);
                    sheet.addCell(label);
                    label = new Label(14, row, StringUtils.defaultString(rs.getString("rewards_good_service")), innercellvertical);
                    sheet.setColumnView(14, 12);
                    sheet.addCell(label);
                    label = new Label(15, row, StringUtils.defaultString(rs.getString("rewards_any_other")), innercellvertical);
                    sheet.setColumnView(15, 12);
                    sheet.addCell(label);
                    label = new Label(16, row, "", innercellvertical); //Total
                    sheet.setColumnView(16, 12);
                    sheet.addCell(label);
                    label = new Label(17, row, StringUtils.defaultString(rs.getString("if_major_punishment")), innercellvertical); //Major
                    sheet.setColumnView(17, 12);
                    sheet.addCell(label);
                    label = new Label(18, row, StringUtils.defaultString(rs.getString("if_minor_punishment")), innercellvertical); //Minor
                    sheet.setColumnView(18, 12);
                    sheet.addCell(label);
                    label = new Label(19, row, StringUtils.defaultString(rs.getString("punishment_penalty_year")), innercellvertical); //Total
                    sheet.setColumnView(19, 12);
                    sheet.addCell(label);

                    
                    label = new Label(20, row, StringUtils.defaultString(rs.getString("acr_grading_10")), innercellvertical);
                    sheet.setColumnView(20, 12);
                    sheet.addCell(label);
                    label = new Label(21, row, StringUtils.defaultString(rs.getString("acr_grading_11")), innercellvertical);
                    sheet.setColumnView(21, 12);
                    sheet.addCell(label);
                    label = new Label(22, row, StringUtils.defaultString(rs.getString("acr_grading_12")), innercellvertical);
                    sheet.setColumnView(2223, 12);
                    sheet.addCell(label);
                    label = new Label(23, row, StringUtils.defaultString(rs.getString("acr_grading_13")), innercellvertical);
                    sheet.setColumnView(23, 12);
                    label = new Label(24, row, StringUtils.defaultString(rs.getString("acr_grading_14")), innercellvertical);
                    sheet.setColumnView(24, 12);
                    sheet.addCell(label);
                    label = new Label(25, row, StringUtils.defaultString(rs.getString("acr_grading_9")), innercellvertical);
                    sheet.setColumnView(25, 12);
                    sheet.addCell(label);
                    label = new Label(26, row, "", innercellvertical);//StringUtils.defaultString(rs.getString("brief_description"))
                    sheet.setColumnView(26, 21);
                    sheet.addCell(label);
                    // sheet.mergeCells(24, row, 29, row);
                    label = new Label(27, row, CommonFunctions.getFormattedOutputDate1(rs.getDate("range_submitted_on")), innercellvertical);
                    sheet.setColumnView(25, 21);
                    sheet.addCell(label);
                    
                    label = new Label(28, row, StringUtils.defaultString(rs.getString("property_submitted_if_any")), innercellvertical);
                    sheet.setColumnView(28, 34);
                    sheet.addCell(label);
                    
                    label = new Label(29, row, CommonFunctions.getFormattedOutputDate1(rs.getDate("property_submitted_on_hrms")), innercellvertical);
                    sheet.setColumnView(29, 34);
                    sheet.addCell(label);
                    
                    label = new Label(30, row, CommonFunctions.getFormattedOutputDate1(rs.getDate("property_submitted_on_hrms_byofficer")), innercellvertical);
                    sheet.setColumnView(30, 34);
                    sheet.addCell(label);
                    
                    //sheet.mergeCells(25, row, 25, row);
                    label = new Label(31, row, "", innercellvertical); //Hard copy Page No.
                    sheet.setColumnView(31, 34);
                    sheet.addCell(label);
                    //sheet.mergeCells(26, row, 26, row);
                    label = new Label(32, row, rs.getString("off_en"), innercell); //Hard copy Page No.   
                    sheet.setColumnView(32, 34);
                    sheet.addCell(label);

                    label = new Label(33, row, rs.getString("award_medal_id"), innercell); //Hard copy Page No.   
                    sheet.setColumnView(33, 34);
                    sheet.addCell(label);
                }
            }

            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public static void main(String sur[]) {
        Connection con = null;
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://192.168.1.19:5432/hrmis", "hrmis2", "cmgi");
            AwardMedalDAOImpl obj = new AwardMedalDAOImpl();
            WritableWorkbook workbook = Workbook.createWorkbook(new File("D://template_test.xls"));

            downloadBroadSheetForAwardMedal2(workbook, "07", "2021");
            /*int year = Calendar.getInstance().get(Calendar.YEAR);
             SelectOption so = null;
             ArrayList yearlist = new ArrayList();
             for(int i = 1990; i <= year; i++){
             so = new SelectOption();
             so.setValue(i+"");
             so.setLabel(i+"");
             yearlist.add(so);
             }*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public static void downloadBroadSheetForAwardMedal2(WritableWorkbook workbook, String awardId, String awardYear) {

        PreparedStatement pst = null;
        ResultSet rs = null;

        int row = 0;
        try {

            WritableSheet sheet = workbook.createSheet("Broad Sheet", 0);

            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 15, WritableFont.BOLD);
            WritableCellFormat headcell = new WritableCellFormat(headformat);
            headcell.setAlignment(Alignment.CENTRE);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setWrap(true);
            headcell.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableFont headformat2 = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
            WritableCellFormat innerheadcell = new WritableCellFormat(headformat2);
            innerheadcell.setAlignment(Alignment.CENTRE);
            innerheadcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            innerheadcell.setWrap(true);
            innerheadcell.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableFont innerformat = new WritableFont(WritableFont.ARIAL, 12, WritableFont.NO_BOLD);

            WritableCellFormat innercell = new WritableCellFormat(innerformat);
            innercell.setWrap(true);
            innercell.setBorder(Border.ALL, BorderLineStyle.THIN);

            if (awardId.equals("07")) {

                Label label = new Label(0, row, " COMPARATIVE CHART FOR SELECTION OF POLICE PERSONNEL FOR AWARDING OF DGP'S DISC. ON 01.04.2021 WHO HAVE RENDERED SPECTCULAR MERITORIOUS SERVICE DURING THE BUDGETARY YEAR 2020-2021  ", headcell);//column,row
                sheet.setRowView(row, 30 * 30);
                sheet.addCell(label);
                sheet.mergeCells(0, row, 19, row);

                row = row + 1;

                sheet.mergeCells(0, row, 0, row + 1);
                label = new Label(0, row, "SL No", innerheadcell);
                sheet.setRowView(row, 60 * 30);
                sheet.addCell(label);

                sheet.mergeCells(1, row, 1, row + 1);
                label = new Label(1, row, "Name of the Districts/ Establishment", innerheadcell);
                sheet.setColumnView(1, 37);
                sheet.addCell(label);

                sheet.mergeCells(2, row, 2, row + 1);
                label = new Label(2, row, "Name of the Police Personel with their present posting ", innerheadcell);
                sheet.setColumnView(2, 30);
                sheet.addCell(label);

                sheet.mergeCells(3, row, 3, row + 1);
                label = new Label(3, row, "Date of appointment/ date of continuing in the present rank", innerheadcell);
                sheet.setColumnView(3, 20);
                sheet.addCell(label);

                sheet.mergeCells(4, row, 4, row + 1);
                label = new Label(4, row, "Total Period of service (approx)", innerheadcell);
                sheet.setColumnView(4, 20);
                sheet.addCell(label);

                sheet.mergeCells(5, row, 9, row);
                label = new Label(5, row, "Rewards during year 2020-2021.", innerheadcell);
                sheet.setColumnView(5, 20);
                sheet.addCell(label);

                sheet.mergeCells(10, row, 12, row);
                label = new Label(10, row, "Punishment during year 2020-2021.", innerheadcell);
                sheet.setColumnView(10, 20);
                sheet.addCell(label);

                sheet.mergeCells(13, row, 13, row + 1);
                label = new Label(13, row, "Whether he/she has received DGP's Disc during the previous year", innerheadcell);
                sheet.setColumnView(13, 20);
                sheet.addCell(label);

                sheet.mergeCells(14, row, 14, row + 1);
                label = new Label(14, row, "Whether any criminal/Vig. case/Departmental Prog. pending against him", innerheadcell);
                sheet.setColumnView(14, 20);
                sheet.addCell(label);

                sheet.mergeCells(15, row, 15, row + 1);
                label = new Label(15, row, "Recommending authority", innerheadcell);
                sheet.setColumnView(15, 20);
                sheet.addCell(label);

                sheet.mergeCells(16, row, 16, row + 1);
                label = new Label(16, row, "Counter signing authority", innerheadcell);
                sheet.setColumnView(16, 20);
                sheet.addCell(label);

                sheet.mergeCells(17, row, 17, row + 1);
                label = new Label(17, row, "Brief description of spectacular meritorious service during the year, 2020-21", innerheadcell);
                sheet.setColumnView(17, 20);
                sheet.addCell(label);

                sheet.mergeCells(18, row, 18, row + 1);
                label = new Label(18, row, "Remarks", innerheadcell);
                sheet.setColumnView(18, 20);
                sheet.addCell(label);

                sheet.mergeCells(19, row, 19, row + 1);
                label = new Label(19, row, "Decision of the selection Committee", innerheadcell);
                sheet.setColumnView(19, 20);
                sheet.addCell(label);

                row = row + 1;
                label = new Label(5, row, "M.R.", innerheadcell);
                sheet.setColumnView(5, 20);
                sheet.addCell(label);

                label = new Label(6, row, "C/H.C.", innerheadcell);
                sheet.setColumnView(6, 20);
                sheet.addCell(label);

                label = new Label(7, row, "GSM", innerheadcell);
                sheet.setColumnView(7, 20);
                sheet.addCell(label);

                label = new Label(8, row, "APP", innerheadcell);
                sheet.setColumnView(8, 20);
                sheet.addCell(label);

                label = new Label(9, row, "TOTAL", innerheadcell);
                sheet.setColumnView(9, 20);
                sheet.addCell(label);

                label = new Label(10, row, "MAJOR", innerheadcell);
                sheet.setColumnView(10, 20);
                sheet.addCell(label);

                label = new Label(11, row, "MINOR", innerheadcell);
                sheet.setColumnView(11, 20);
                sheet.addCell(label);

                label = new Label(12, row, "TOTAL", innerheadcell);
                sheet.setColumnView(12, 20);
                sheet.addCell(label);
            }

            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    public void revertAwardDataByDGOffice(AwardMedalListForm award) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("update police_award_form set submitted_on=null, submitted_range_off=null, range_submitted_on=null,dg_off_code=null where award_medal_id=?");
            pst.setInt(1, Integer.parseInt(award.getRewardMedalId()));
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveSRCaseData(AwardMedalListForm award) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("insert into police_sr_cases (award_medal_id,case_no,reg_date,fr_date,dfsl_team_dtls,exhibits_no_sent,conviction_dtls,crime_attachment)"
                    + " values (?,?,?,?,?,?,?,?)");
            ps.setInt(1, Integer.parseInt(award.getRewardMedalId()));
            ps.setString(2, award.getSrCaseNo());
            ps.setString(3, award.getRegDate());
            ps.setString(4, award.getFrDate());
            ps.setString(5, award.getDtlsTeamUse());
            ps.setString(6, award.getNoOfExhibits());
            ps.setString(7, award.getConvictionDtls());
            ps.setString(8, award.getCrimeProceedAttach());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    public List getSRCaseList(String awardMedalId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List li = new ArrayList();
        SRCaseList srCase = new SRCaseList();
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select sr_case_id,case_no,reg_date,fr_date,dfsl_team_dtls,exhibits_no_sent,conviction_dtls,crime_attachment from police_sr_cases where award_medal_id=?");
            ps.setInt(1, Integer.parseInt(awardMedalId));
            rs = ps.executeQuery();
            while (rs.next()) {
                srCase = new SRCaseList();
                srCase.setSrCaseId(rs.getString("sr_case_id"));
                srCase.setSrCaseNo(rs.getString("case_no"));
                srCase.setSrRegDate(rs.getString("reg_date"));
                srCase.setFrDate(rs.getString("fr_date"));
                srCase.setDfslTeamDtls(rs.getString("dfsl_team_dtls"));
                srCase.setExhibitsNoSent(rs.getString("exhibits_no_sent"));
                srCase.setConvictionDtls(rs.getString("conviction_dtls"));
                srCase.setSrAttachment(rs.getString("crime_attachment"));
                li.add(srCase);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    public AwardMedalListForm getSRCaseData(String srCaseId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        AwardMedalListForm amf = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select case_no,reg_date,fr_date,dfsl_team_dtls,exhibits_no_sent,conviction_dtls,crime_attachment from police_sr_cases where sr_case_id=?");
            ps.setInt(1, Integer.parseInt(srCaseId));
            rs = ps.executeQuery();
            if (rs.next()) {
                amf = new AwardMedalListForm();
                amf.setSrCaseId(srCaseId);
                amf.setSrCaseNo(rs.getString("case_no"));
                amf.setRegDate(rs.getString("reg_date"));
                amf.setFrDate(rs.getString("fr_date"));
                amf.setDtlsTeamUse(rs.getString("dfsl_team_dtls"));
                amf.setNoOfExhibits(rs.getString("exhibits_no_sent"));
                amf.setConvictionDtls(rs.getString("conviction_dtls"));
                amf.setCrimeProceedAttach(rs.getString("crime_attachment"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return amf;
    }

    public AwardMedalListForm getInvestigatedCaseData(String invstCaseId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        AwardMedalListForm amf = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select award_medal_id,ps_name,case_no,date_of_reg,cs_fr,final_submit_date,sr_nonsr,brief_case,innovative_method,tools_used, "
                    + "evidence,chargesheet_filing,attach_crime,investigation_challenges,conv_dtls,original_name,if_scientifictool,if_invstinnovmethod,if_invstscientificevd,if_invstattachconfis "
                    + "from police_investigation  "
                    + "left outer JOIN police_investigation_doc on police_investigation_doc.data_table_id=police_investigation.police_invest_id "
                    + "where police_invest_id=?");

            ps.setInt(1, Integer.parseInt(invstCaseId));
            rs = ps.executeQuery();
            if (rs.next()) {
                amf = new AwardMedalListForm();
                amf.setInvstCaseId(invstCaseId);
                amf.setPsName(rs.getString("ps_name"));
                amf.setInvstCaseNo(rs.getString("case_no"));
                amf.setInvstDateReg(CommonFunctions.getFormattedOutputDate1(rs.getDate("date_of_reg")));
                amf.setInvstCsFr(rs.getString("cs_fr"));
                amf.setInvstFinalSubDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("final_submit_date")));
                amf.setInvstSrNonsr(rs.getString("sr_nonsr"));
                amf.setInvstBriefCase(rs.getString("brief_case"));
                amf.setInvstInnovMethods(rs.getString("innovative_method"));
                amf.setInvstScientificAids(rs.getString("tools_used"));
                amf.setInvstScientificEvd(rs.getString("evidence"));
                amf.setInvstPromptness(rs.getString("chargesheet_filing"));
                amf.setInvstAttachConfis(rs.getString("attach_crime"));
                amf.setInvstChallenges(rs.getString("investigation_challenges"));
                amf.setInvstConvcDtls(rs.getString("conv_dtls"));
                amf.setOriginalJudgementCopy(rs.getString("original_name"));
                amf.setSelScientificTool(rs.getString("if_scientifictool"));
                amf.setSelInvstInnovMethods(rs.getString("if_invstinnovmethod"));
                amf.setSelInvstScientificEvd(rs.getString("if_invstscientificevd"));
                amf.setSelInvstAttachConfis(rs.getString("if_invstattachconfis"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return amf;
    }

    @Override
    public void updateSRCaseData(AwardMedalListForm award) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("update police_sr_cases set case_no=?,reg_date=?,fr_date=?,dfsl_team_dtls=?,exhibits_no_sent=?,conviction_dtls=?,crime_attachment=? where sr_case_id=?");
            ps.setString(1, award.getSrCaseNo());
            ps.setString(2, award.getRegDate());
            ps.setString(3, award.getFrDate());
            ps.setString(4, award.getDtlsTeamUse());
            ps.setString(5, award.getNoOfExhibits());
            ps.setString(6, award.getConvictionDtls());
            ps.setString(7, award.getCrimeProceedAttach());
            ps.setInt(8, Integer.parseInt(award.getSrCaseId()));
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    public void deleteSRCaseData(String srCaseId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("delete from police_sr_cases where sr_case_id=?");
            ps.setInt(1, Integer.parseInt(srCaseId));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    public void saveInvestigatedCaseData(AwardMedalListForm award, String filePath) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        int rowCnt;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT count(*) AS maxcnt FROM police_investigation where award_medal_id=?");
            ps.setInt(1, Integer.parseInt(award.getRewardMedalId()));
            rs = ps.executeQuery();
            if (rs.next()) {
                rowCnt = rs.getInt("maxcnt");
                if (rowCnt < 2) {
                    ps = con.prepareStatement("insert into police_investigation (award_medal_id,ps_name,case_no,date_of_reg,cs_fr,final_submit_date,sr_nonsr,brief_case,innovative_method,tools_used,"
                            + "evidence,chargesheet_filing,attach_crime,investigation_challenges,conv_dtls,if_scientifictool,if_invstinnovmethod,if_invstscientificevd,if_invstattachconfis)values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                    ps.setInt(1, Integer.parseInt(award.getRewardMedalId()));
                    ps.setString(2, award.getPsName());
                    ps.setString(3, award.getInvstCaseNo());
                    if (award.getInvstDateReg() != null && !award.getInvstDateReg().equals("")) {
                        ps.setTimestamp(4, new Timestamp(sdf.parse(award.getInvstDateReg()).getTime()));
                    } else {
                        ps.setTimestamp(4, null);
                    }

                    ps.setString(5, award.getInvstCsFr());
                    if (award.getInvstFinalSubDate() != null && !award.getInvstFinalSubDate().equals("")) {
                        ps.setTimestamp(6, new Timestamp(sdf.parse(award.getInvstFinalSubDate()).getTime()));
                    } else {
                        ps.setTimestamp(6, null);
                    }

                    ps.setString(7, award.getInvstSrNonsr());
                    ps.setString(8, award.getInvstBriefCase());
                    ps.setString(9, award.getInvstInnovMethods());
                    ps.setString(10, award.getInvstScientificAids());
                    ps.setString(11, award.getInvstScientificEvd());
                    ps.setString(12, award.getInvstPromptness());
                    ps.setString(13, award.getInvstAttachConfis());
                    ps.setString(14, award.getInvstChallenges());
                    ps.setString(15, award.getInvstConvcDtls());
                    ps.setString(16, award.getSelScientificTool());
                    ps.setString(17, award.getSelInvstInnovMethods());
                    ps.setString(18, award.getSelInvstScientificEvd());
                    ps.setString(19, award.getSelInvstAttachConfis());
                    ps.executeUpdate();
                    rs = ps.getGeneratedKeys();
                    rs.next();
                    int polInvstId = rs.getInt("police_invest_id");

                    if (award.getJudgementCopy() != null && !award.getJudgementCopy().isEmpty()) {
                        int read = 0;
                        byte[] bytes = new byte[1024];

                        inputStream = award.getJudgementCopy().getInputStream();
                        String diskfilename = "JC" + System.currentTimeMillis() + "";
                        File newFile1 = new File(filePath + diskfilename);
                        if (!newFile1.exists()) {
                            newFile1.createNewFile();
                        }
                        outputStream = new FileOutputStream(newFile1);

                        while ((read = inputStream.read(bytes)) != -1) {
                            outputStream.write(bytes, 0, read);
                        }

                        String insFileQry = "insert into police_investigation_doc ( file_type, file_path, file_name, original_name, data_table_id) values(?,?,?,?,?)";
                        ps = con.prepareStatement(insFileQry);
                        ps.setString(1, award.getJudgementCopy().getContentType());
                        ps.setString(2, filePath);
                        ps.setString(3, diskfilename);
                        ps.setString(4, award.getJudgementCopy().getOriginalFilename());
                        ps.setInt(5, polInvstId);
                        ps.executeUpdate();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public List investigatedCaseList(String awardMedalId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List li = new ArrayList();
        InvestigatedCaseList invstcaseList = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select police_invest_id,ps_name,case_no,date_of_reg,cs_fr,sr_nonsr from police_investigation where award_medal_id=?");
            ps.setInt(1, Integer.parseInt(awardMedalId));
            rs = ps.executeQuery();
            while (rs.next()) {
                invstcaseList = new InvestigatedCaseList();
                invstcaseList.setInvstcaseId(rs.getString("police_invest_id"));
                invstcaseList.setPsName(rs.getString("ps_name"));
                invstcaseList.setInvstCaseNo(rs.getString("case_no"));
                invstcaseList.setInvstRegdate(rs.getString("date_of_reg"));
                invstcaseList.setInvstCsFr(rs.getString("cs_fr"));
                invstcaseList.setInvstSrNonSr(rs.getString("sr_nonsr"));
                li.add(invstcaseList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    public void updateInvestigatedCaseData(AwardMedalListForm award, String filePath) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("update police_investigation set ps_name=?,case_no=?,date_of_reg=?,cs_fr=?,final_submit_date=?,sr_nonsr=?,brief_case=?,innovative_method=?,tools_used=?,"
                    + "evidence=?,chargesheet_filing=?,attach_crime=?,investigation_challenges=?,conv_dtls=?,if_scientifictool=?,if_invstinnovmethod=?,if_invstscientificevd=?,if_invstattachconfis=? where police_invest_id=?");

            ps.setString(1, award.getPsName());
            ps.setString(2, award.getInvstCaseNo());
            if (award.getInvstDateReg() != null && !award.getInvstDateReg().equals("")) {
                ps.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(award.getInvstDateReg()).getTime()));

            } else {
                ps.setTimestamp(3, null);
            }
            ps.setString(4, award.getInvstCsFr());
            if (award.getInvstFinalSubDate() != null && !award.getInvstFinalSubDate().equals("")) {
                ps.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(award.getInvstFinalSubDate()).getTime()));
            } else {
                ps.setTimestamp(5, null);
            }
            ps.setString(6, award.getInvstSrNonsr());
            ps.setString(7, award.getInvstBriefCase());
            ps.setString(8, award.getInvstInnovMethods());
            ps.setString(9, award.getInvstScientificAids());
            ps.setString(10, award.getInvstScientificEvd());
            ps.setString(11, award.getInvstPromptness());
            ps.setString(12, award.getInvstAttachConfis());
            ps.setString(13, award.getInvstChallenges());
            ps.setString(14, award.getInvstConvcDtls());
            ps.setString(15, award.getSelScientificTool());
            ps.setString(16, award.getSelInvstInnovMethods());
            ps.setString(17, award.getSelInvstScientificEvd());
            ps.setString(18, award.getSelInvstAttachConfis());
            ps.setInt(19, Integer.parseInt(award.getInvstCaseId()));
            ps.executeUpdate();
            if (award.getJudgementCopy() != null && !award.getJudgementCopy().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = award.getJudgementCopy().getInputStream();
                String diskfilename = "JC" + System.currentTimeMillis() + "";
                File newFile1 = new File(filePath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_investigation_doc ( file_type, file_path, file_name, original_name,data_table_id) values(?,?,?,?,?)";
                ps = con.prepareStatement(insFileQry);
                ps.setString(1, award.getJudgementCopy().getContentType());
                ps.setString(2, filePath);
                ps.setString(3, diskfilename);
                ps.setString(4, award.getJudgementCopy().getOriginalFilename());
                ps.setInt(5, Integer.parseInt(award.getInvstCaseId()));
                ps.executeUpdate();

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public FileAttribute getInvstigateCaseDoc(String filePath, int attachId) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        File f = null;
        FileAttribute fa = new FileAttribute();
        try {
            con = this.dataSource.getConnection();
            String sql = "";
            sql = "SELECT * FROM police_investigation_doc WHERE data_table_id=? ";
            pst = con.prepareStatement(sql);
            pst.setInt(1, attachId);

            rs = pst.executeQuery();
            if (rs.next()) {
                String dirpath = filePath + rs.getString("file_name");
                f = new File(dirpath);
                if (f.exists()) {
                    fa.setDiskFileName(rs.getString("file_name"));
                    fa.setOriginalFileName(rs.getString("original_name"));
                    fa.setFileType(rs.getString("file_type"));
                    fa.setUploadAttachment(f);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fa;
    }

    public void deleteInvestigatedCaseData(String invstcaseId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("delete from police_investigation where police_invest_id=?");
            ps.setInt(1, Integer.parseInt(invstcaseId));
            ps.executeUpdate();
            ps = con.prepareStatement("delete from police_investigation_doc where data_table_id=?");
            ps.setInt(1, Integer.parseInt(invstcaseId));
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    public void saveNonSrCaseDtlsData(AwardMedalListForm award) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("insert into police_nonsrcase_detail (award_medal_id,no_of_cases2018,no_of_cases2019,no_of_cases2020,no_of_cases_in2018,no_of_cases_in2019,"
                    + "no_of_cases_in2020,no_of_cases_after2018,no_of_cases_after2019,no_of_cases_after2020,pending_cases_2018,pending_cases_2019,pending_cases_2020,no_of_srcases2018,no_of_srcases2019,no_of_srcases2020,no_of_srcases_in2018,"
                    + "no_of_srcases_in2019,no_of_srcases_in2020,no_of_srcases_after2018,no_of_srcases_after2019,no_of_srcases_after2020,pending_srcases_2018,pending_srcases_2019,pending_srcases_2020)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, Integer.parseInt(award.getRewardMedalId()));
            ps.setString(2, award.getNoOfNonSrcaseInvst2018());
            // ps.setString(2,);
            ps.setString(3, award.getNoOfNonSrcaseInvst2019());
            ps.setString(4, award.getNoOfNonSrcaseInvst2020());
            ps.setString(5, award.getNoFinalizedInThirtydays2018());
            ps.setString(6, award.getNoFinalizedInThirtydays2019());
            ps.setString(7, award.getNoFinalizedInThirtydays2020());
            ps.setString(8, award.getNoFinalizedAfterThirtydays2018());
            ps.setString(9, award.getNoFinalizedAfterThirtydays2019());
            ps.setString(10, award.getNoFinalizedAfterThirtydays2020());
            ps.setString(11, award.getNoStillPending2018());
            ps.setString(12, award.getNoStillPending2019());
            ps.setString(13, award.getNoStillPending2020());
            ps.setString(14, award.getNoOfSrcaseInvst2018());
            ps.setString(15, award.getNoOfSrcaseInvst2019());
            ps.setString(16, award.getNoOfSrcaseInvst2020());
            ps.setString(17, award.getNoSrFinalizedInThirtydays2018());
            ps.setString(18, award.getNoSrFinalizedInThirtydays2019());
            ps.setString(19, award.getNoSrFinalizedInThirtydays2020());
            ps.setString(20, award.getNoSrFinalizedAfterThirtydays2018());
            ps.setString(21, award.getNoSrFinalizedAfterThirtydays2019());
            ps.setString(22, award.getNoSrFinalizedAfterThirtydays2020());
            ps.setString(23, award.getNoSrStillPending2018());
            ps.setString(24, award.getNoSrStillPending2019());
            ps.setString(25, award.getNoSrStillPending2020());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public List getNonSrCaseList(String awardMedalId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List nonSrcaseList = new ArrayList();
        NonSrCaseList nonsrcaselist = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select non_sr_case_id,no_of_cases2018,no_of_cases2019,no_of_cases2020,no_of_cases_in2018,no_of_cases_in2019,"
                    + "no_of_cases_in2020,no_of_cases_after2018,no_of_cases_after2019,no_of_cases_after2020,pending_cases_2018,pending_cases_2019,pending_cases_2020 from police_nonsrcase_detail where award_medal_id=? order by non_sr_case_id");
            ps.setInt(1, Integer.parseInt(awardMedalId));
            rs = ps.executeQuery();
            while (rs.next()) {
                nonsrcaselist = new NonSrCaseList();
                nonsrcaselist.setNonSrcaseId(rs.getString("non_sr_case_id"));
                nonsrcaselist.setNoofcases2018(rs.getString("no_of_cases2018"));
                nonsrcaselist.setNoofcases2019(rs.getString("no_of_cases2019"));
                nonsrcaselist.setNoofcases2020(rs.getString("no_of_cases2020"));
                nonsrcaselist.setNoofcasesin2018(rs.getString("no_of_cases_in2018"));
                nonsrcaselist.setNoofcasesin2019(rs.getString("no_of_cases_in2019"));
                nonsrcaselist.setNoofcasesin2020(rs.getString("no_of_cases_in2020"));
                nonsrcaselist.setNoofcasesafter2018(rs.getString("no_of_cases_after2018"));
                nonsrcaselist.setNoofcasesafter2019(rs.getString("no_of_cases_after2019"));
                nonsrcaselist.setNoofcasesafter2020(rs.getString("no_of_cases_after2020"));
                nonsrcaselist.setPendingcases2018(rs.getString("pending_cases_2018"));
                nonsrcaselist.setPendingcases2019(rs.getString("pending_cases_2019"));
                nonsrcaselist.setPendingcases2020(rs.getString("pending_cases_2020"));
                nonSrcaseList.add(nonsrcaselist);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return nonSrcaseList;
    }

    public AwardMedalListForm getNonSRCaseData(String nonSrcaseId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        AwardMedalListForm amf = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select non_sr_case_id,no_of_cases2018,no_of_cases2019,no_of_cases2020,no_of_cases_in2018,no_of_cases_in2019,"
                    + "no_of_cases_in2020,no_of_cases_after2018,no_of_cases_after2019,no_of_cases_after2020,pending_cases_2018,pending_cases_2019,pending_cases_2020,no_of_srcases2018,no_of_srcases2019,no_of_srcases2020,no_of_srcases_in2018,"
                    + "no_of_srcases_in2019,no_of_srcases_in2020,no_of_srcases_after2018,no_of_srcases_after2019,no_of_srcases_after2020,pending_srcases_2018,pending_srcases_2019,pending_srcases_2020 from police_nonsrcase_detail where non_sr_case_id=?");

            ps.setInt(1, Integer.parseInt(nonSrcaseId));
            rs = ps.executeQuery();
            if (rs.next()) {
                amf = new AwardMedalListForm();
                amf.setNonSrcaseId(rs.getString("non_sr_case_id"));
                amf.setNoOfNonSrcaseInvst2018(rs.getString("no_of_cases2018"));
                amf.setNoOfNonSrcaseInvst2019(rs.getString("no_of_cases2019"));
                amf.setNoOfNonSrcaseInvst2020(rs.getString("no_of_cases2020"));
                amf.setNoFinalizedInThirtydays2018(rs.getString("no_of_cases_in2018"));
                amf.setNoFinalizedInThirtydays2019(rs.getString("no_of_cases_in2019"));
                amf.setNoFinalizedInThirtydays2020(rs.getString("no_of_cases_in2020"));
                amf.setNoFinalizedAfterThirtydays2018(rs.getString("no_of_cases_after2018"));
                amf.setNoFinalizedAfterThirtydays2019(rs.getString("no_of_cases_after2019"));
                amf.setNoFinalizedAfterThirtydays2020(rs.getString("no_of_cases_after2020"));
                amf.setNoStillPending2018(rs.getString("pending_cases_2018"));
                amf.setNoStillPending2019(rs.getString("pending_cases_2019"));
                amf.setNoStillPending2020(rs.getString("pending_cases_2020"));
                amf.setNoOfSrcaseInvst2018(rs.getString("no_of_srcases2018"));
                amf.setNoOfSrcaseInvst2019(rs.getString("no_of_srcases2019"));
                amf.setNoOfSrcaseInvst2020(rs.getString("no_of_srcases2020"));
                amf.setNoSrFinalizedInThirtydays2018(rs.getString("no_of_srcases_in2018"));
                amf.setNoSrFinalizedInThirtydays2019(rs.getString("no_of_srcases_in2019"));
                amf.setNoSrFinalizedInThirtydays2020(rs.getString("no_of_srcases_in2020"));
                amf.setNoSrFinalizedAfterThirtydays2018(rs.getString("no_of_srcases_after2018"));
                amf.setNoSrFinalizedAfterThirtydays2019(rs.getString("no_of_srcases_after2019"));
                amf.setNoSrFinalizedAfterThirtydays2020(rs.getString("no_of_srcases_after2020"));
                amf.setNoSrStillPending2018(rs.getString("pending_srcases_2018"));
                amf.setNoSrStillPending2019(rs.getString("pending_srcases_2019"));
                amf.setNoSrStillPending2020(rs.getString("pending_srcases_2020"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return amf;
    }

    public void updateNonSrCaseDtlsData(AwardMedalListForm award) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("update police_nonsrcase_detail set no_of_cases2018=?,no_of_cases2019=?,no_of_cases2020=?,no_of_cases_in2018=?,no_of_cases_in2019=?,"
                    + "no_of_cases_in2020=?,no_of_cases_after2018=?,no_of_cases_after2019=?,no_of_cases_after2020=?,pending_cases_2018=?,pending_cases_2019=?,pending_cases_2020=?,no_of_srcases2018=?,no_of_srcases2019=?,no_of_srcases2020=?,no_of_srcases_in2018=?,"
                    + "no_of_srcases_in2019=?,no_of_srcases_in2020=?,no_of_srcases_after2018=?,no_of_srcases_after2019=?,no_of_srcases_after2020=?,pending_srcases_2018=?,pending_srcases_2019=?,pending_srcases_2020=? where non_sr_case_id=?");

            ps.setString(1, award.getNoOfNonSrcaseInvst2018());
            // ps.setString(2,);
            ps.setString(2, award.getNoOfNonSrcaseInvst2019());
            ps.setString(3, award.getNoOfNonSrcaseInvst2020());
            ps.setString(4, award.getNoFinalizedInThirtydays2018());
            ps.setString(5, award.getNoFinalizedInThirtydays2019());
            ps.setString(6, award.getNoFinalizedInThirtydays2020());
            ps.setString(7, award.getNoFinalizedAfterThirtydays2018());
            ps.setString(8, award.getNoFinalizedAfterThirtydays2019());
            ps.setString(9, award.getNoFinalizedAfterThirtydays2020());
            ps.setString(10, award.getNoStillPending2018());
            ps.setString(11, award.getNoStillPending2019());
            ps.setString(12, award.getNoStillPending2020());
            ps.setString(13, award.getNoOfSrcaseInvst2018());
            ps.setString(14, award.getNoOfSrcaseInvst2019());
            ps.setString(15, award.getNoOfSrcaseInvst2020());
            ps.setString(16, award.getNoSrFinalizedInThirtydays2018());
            ps.setString(17, award.getNoSrFinalizedInThirtydays2019());
            ps.setString(18, award.getNoSrFinalizedInThirtydays2020());
            ps.setString(19, award.getNoSrFinalizedAfterThirtydays2018());
            ps.setString(20, award.getNoSrFinalizedAfterThirtydays2019());
            ps.setString(21, award.getNoSrFinalizedAfterThirtydays2020());
            ps.setString(22, award.getNoSrStillPending2018());
            ps.setString(23, award.getNoSrStillPending2019());
            ps.setString(24, award.getNoSrStillPending2020());

            ps.setInt(25, Integer.parseInt(award.getNonSrcaseId()));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void deleteNonSrCaseData(String nonSrcaseId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("delete from police_nonsrcase_detail  where non_sr_case_id=?");
            ps.setInt(1, Integer.parseInt(nonSrcaseId));
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public int closePoliceMedal(String awardmedaltypeid) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int retVal = 0;
        String newstatus = "N";
        try {
            con = this.dataSource.getConnection();

            if (awardmedaltypeid != null && !awardmedaltypeid.equals("")) {

                String sql = "select is_active from g_reward_type where reward_type_id=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, awardmedaltypeid);
                rs = pst.executeQuery();
                if (rs.next()) {
                    String currentstatus = rs.getString("is_active");
                    if (currentstatus != null && currentstatus.equals("Y")) {
                        newstatus = "N";
                    } else {
                        newstatus = "Y";
                    }
                }

                DataBaseFunctions.closeSqlObjects(rs, pst);

                sql = "UPDATE g_reward_type set is_active=? where reward_type_id=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, newstatus);
                pst.setString(2, awardmedaltypeid);
                retVal = pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return retVal;
    }

    @Override
    public String CheckStatusOfPoliceMedal(String awardmedaltypeid) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String newstatus = "";
        try {
            con = this.dataSource.getConnection();

            if (awardmedaltypeid != null && !awardmedaltypeid.equals("")) {

                String sql = "select is_active from g_reward_type where reward_type_id=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, awardmedaltypeid);
                rs = pst.executeQuery();
                if (rs.next()) {
                    String currentstatus = rs.getString("is_active");
                    if (currentstatus != null && currentstatus.equals("Y")) {
                        newstatus = "Active";
                    } else {
                        newstatus = "Not Active";
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return newstatus;
    }

    @Override
    public int activePoliceMedal(String awardmedaltypeid) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int retVal = 0;
        String newstatus = "N";
        try {
            con = this.dataSource.getConnection();

            if (awardmedaltypeid != null && !awardmedaltypeid.equals("")) {

                String sql = "select is_active from g_reward_type where reward_type_id=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, awardmedaltypeid);
                rs = pst.executeQuery();
                if (rs.next()) {
                    String currentstatus = rs.getString("is_active");
                    if (currentstatus != null && currentstatus.equals("N")) {
                        newstatus = "Y";

                    }
                }

                DataBaseFunctions.closeSqlObjects(rs, pst);

                sql = "UPDATE g_reward_type set is_active=? where reward_type_id=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, newstatus);
                pst.setString(2, awardmedaltypeid);
                retVal = pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return retVal;
    }

    @Override
    public int deActivePoliceMedal(String awardmedaltypeid) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int retVal = 0;
        String newstatus = "";
        try {
            con = this.dataSource.getConnection();

            if (awardmedaltypeid != null && !awardmedaltypeid.equals("")) {

                String sql = "select is_active from g_reward_type where reward_type_id=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, awardmedaltypeid);
                rs = pst.executeQuery();
                if (rs.next()) {
                    String currentstatus = rs.getString("is_active");
                    if (currentstatus != null && currentstatus.equals("Y")) {
                        newstatus = "N";
                    }
                }

                DataBaseFunctions.closeSqlObjects(rs, pst);

                sql = "UPDATE g_reward_type set is_active=? where reward_type_id=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, newstatus);
                pst.setString(2, awardmedaltypeid);
                retVal = pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return retVal;
    }

    @Override
    public int deleteRewardAttachment(int attchid, String docName, String filepath, FileAttribute fa) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        File f = null;

        int deletestatus = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "DELETE FROM police_award_doc WHERE data_table_id=? and doc_name=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, attchid);
            pst.setString(2, docName);
            deletestatus = pst.executeUpdate();

            if (deletestatus > 0) {
                String dirpath = filepath + fa.getDiskFileName();

                f = new File(dirpath);
                if (f.exists()) {
                    boolean isDeleted = f.delete();
                    if (isDeleted == true) {

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deletestatus;
    }

    @Override
    public void deleteAwardeeDetailsForGovernor(int awardMedalId) {
        Connection con = null;

        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("delete from police_award_form where award_medal_id=? ");
            pst.setInt(1, awardMedalId);
            pst.execute();
            pst = con.prepareStatement("delete from police_award_doc where data_table_id=?");
            pst.setInt(1, awardMedalId);
            pst.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

}

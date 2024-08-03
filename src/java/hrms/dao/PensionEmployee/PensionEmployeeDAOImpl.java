/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.PensionEmployee;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.EmpQuarterAllotment.QMSDAO;
import hrms.model.employee.PensionFamilyList;
import hrms.model.employee.PensionNomineeList;
import hrms.model.employee.PensionProfile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Manas
 */
public class PensionEmployeeDAOImpl implements PensionEmployeeDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void insertEmployeePensionDetails(PensionProfile profile, String empid) {
//        System.out.println("empid===" + empid);
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement fmpstmt = null;
        PreparedStatement selectStmt = null;
//        PreparedStatement updateStmt = null;
//        PreparedStatement fmupdate = null;
        ResultSet resultSet = null;
        PensionFamilyList pensionFamilyList = null;

        try {
            con = this.dataSource.getConnection();
            selectStmt = con.prepareStatement("SELECT COUNT(*)cntEmp FROM hrmis2.employee_pension_details WHERE (hrms_emp_id = ? )");
            selectStmt.setString(1, empid);
            String gpfNo = profile.getTpfSeries() + "" + profile.getTpfAcNo();
            resultSet = selectStmt.executeQuery();
            if (resultSet.next()) {
//                System.out.println("profile.getDob():" + profile.getDob() + "::" + profile.getRetirementDate());

                if (resultSet.getInt("cntEmp") == 0) {
                    pstmt = con.prepareStatement("INSERT INTO hrmis2.employee_pension_details ( emp_gpf_series, emp_acc_number, hrms_emp_id, emp_salutation, emp_first_name, "
                            + "    emp_middle_name, emp_last_name, date_of_birth, identification_mark, height, maritial_status, religion, "
                            + "    identification_mark_2, sex, nationality, designation, pension_category, cvp_applied, retirement_date, "
                            + "    cvp_percentage, pan_no, ifsc_code, bank_acc_no, payable_treasury, email, bank_branch, ddo_code, mobile_no, city, "
                            + "    police_station, town, state, pincode, comm_city, comm_police_station, comm_district, comm_town, comm_state, comm_pincode, "
                            + "    prev_pension_type, prev_pension_amount, prev_payable_treasury, prev_ifsc_code, pension_issuing_auth, source, ppo_or_fppo_no, pension_effective_from_date, prev_bank_branch,gpf_no,district,acc_type,last_serve)"
                            + "    VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                    pstmt.setString(1, profile.getTpfSeries());
                    pstmt.setString(2, profile.getTpfAcNo());
                    pstmt.setString(3, profile.getHrmsEmpId());
                    pstmt.setString(4, profile.getSalutationEmp());
                    pstmt.setString(5, profile.getEmployeeFirstName());
                    pstmt.setString(6, profile.getEmployeeMiddleName());
                    pstmt.setString(7, profile.getEmployeeLastName());
                    if (profile.getDob() != null && !profile.getDob().equals("")) {
                        pstmt.setTimestamp(8, new Timestamp(new SimpleDateFormat("MM/dd/yyyy").parse(profile.getDob()).getTime()));
                    } else {
                        pstmt.setTimestamp(8, null);
                    }

                    pstmt.setString(9, profile.getPenIdnMark());
                    pstmt.setString(10, profile.getHeight());
                    pstmt.setString(11, profile.getIntMaritalStatusTypeId());
                    pstmt.setString(12, profile.getReligionId());
                    pstmt.setString(13, profile.getPenIdnMark2());
                    pstmt.setString(14, profile.getSex());
                    pstmt.setString(15, profile.getNationalityId());
                    pstmt.setString(16, profile.getDesignationId());
                    pstmt.setString(17, profile.getPenCategoryId());
                    pstmt.setString(18, profile.getCvp());
                    if (profile.getRetirementDate() != null && !profile.getRetirementDate().equals("")) {
                        pstmt.setTimestamp(19, new Timestamp(new SimpleDateFormat("MM/dd/yyyy").parse(profile.getRetirementDate()).getTime()));
                    } else {
                        pstmt.setTimestamp(19, null);
                    }
                    pstmt.setString(20, profile.getCvpPercentage());
                    pstmt.setString(21, profile.getPanNo());
                    pstmt.setString(22, profile.getIfscCode());
                    pstmt.setString(23, profile.getBankAcctNo());
                    pstmt.setString(24, profile.getTreasuryCode());
                    pstmt.setString(25, profile.getMailId());
                    pstmt.setString(26, profile.getBankBranch());
                    pstmt.setString(27, profile.getDdoName());
                    pstmt.setString(28, profile.getMobileNo());
                    pstmt.setString(29, profile.getPerAddcity());
                    pstmt.setString(30, profile.getPerAddpoliceStation());
                    pstmt.setString(31, profile.getPerAddtown());
                    // System.out.prinln("perAddstateId value: " + profile.getPerAddstateId());
                    pstmt.setString(32, profile.getPerAddstateId());
                    pstmt.setString(33, profile.getPerAddpin());
                    pstmt.setString(34, profile.getCommAddcity());
                    pstmt.setString(35, profile.getCommAddpoliceStation());
                    pstmt.setString(36, profile.getCommDistrictCode());
                    pstmt.setString(37, profile.getCommAddtown());
                    pstmt.setString(38, profile.getCommAddstateId());
                    pstmt.setString(39, profile.getCommAddpin());
                    pstmt.setString(40, profile.getPrevPenType());
                    pstmt.setString(41, profile.getPrevPenAmt());
                    pstmt.setString(42, profile.getPrevPenPayTresId());
                    pstmt.setString(43, profile.getPrevPenBankIfscCd());
                    pstmt.setString(44, profile.getPrevPenPia());
                    pstmt.setString(45, profile.getPrevPenSource());
                    pstmt.setString(46, profile.getPrevPPOOrFPPONo());

                    if (profile.getPrevPensionEfffromDate() != null && !profile.getPrevPensionEfffromDate().equals("")) {
                        pstmt.setTimestamp(47, new Timestamp(new SimpleDateFormat("MM/dd/yyyy").parse(profile.getPrevPensionEfffromDate()).getTime()));
                    } else {
                        pstmt.setTimestamp(47, null);
                    }
                    pstmt.setString(48, profile.getPrevPenBankBranch());
                    pstmt.setString(49, gpfNo);
                    pstmt.setString(50, profile.getDistrictCode());
                    pstmt.setString(51, profile.getGpftype());
                    pstmt.setString(52, profile.getLastDistrictServe());
                    pstmt.executeUpdate();
                    DataBaseFunctions.closeSqlObjects(pstmt);
                    List familyList = profile.getPensionfamilylist();

                    for (int i = 0; i < familyList.size(); i++) {
                        pensionFamilyList = (PensionFamilyList) familyList.get(i);
                        fmpstmt = con.prepareStatement("INSERT INTO emp_pensioner_family_det (salutation, f_name, m_name, l_name, relation, sex, date_of_birth, mobile_no, marital_status, share_percentage, ifsc_code, bank_acc_no, bank_branch, handicapped, handicapped_type, minor,guardian_details, ti_applicable, remarks, is_nominee, is_guardian,priority_label,cvp_applied,cvp_percentage,salutaion_of_guardian,address,hrms_emp_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                        // System.out.println("fmStatus11112222====="+pensionFamilyList.getIntMaritalStatusTypeId());
                        fmpstmt.setString(1, pensionFamilyList.getSalutationFamily());
                        fmpstmt.setString(2, pensionFamilyList.getGuardianfname());
                        fmpstmt.setString(3, pensionFamilyList.getGuardianmname());
                        fmpstmt.setString(4, pensionFamilyList.getGuardianlname());
                        fmpstmt.setString(5, pensionFamilyList.getRelationshipId());
                        fmpstmt.setString(6, pensionFamilyList.getSex());
                        if (pensionFamilyList.getFamilyDob() != null && !pensionFamilyList.getFamilyDob().equals("")) {
                            fmpstmt.setTimestamp(7, new Timestamp(new SimpleDateFormat("MM/dd/yyyy").parse(pensionFamilyList.getFamilyDob()).getTime()));
                        } else {
                            fmpstmt.setTimestamp(7, null);
                        }
                        fmpstmt.setString(8, pensionFamilyList.getMobileNo());
                        fmpstmt.setString(9, pensionFamilyList.getIntMaritalStatusTypeId());
                        fmpstmt.setString(10, pensionFamilyList.getFamilySharePercentage());
                        fmpstmt.setString(11, pensionFamilyList.getIfscCode());
                        fmpstmt.setString(12, pensionFamilyList.getBankAccountNo());
                        fmpstmt.setString(13, pensionFamilyList.getBankBranchName());
                        fmpstmt.setString(14, pensionFamilyList.getFamilyHandicappedFlag());
                        fmpstmt.setString(15, pensionFamilyList.getFamilyHandicappedTypeId());
                        fmpstmt.setString(16, pensionFamilyList.getMinorFlag());
                        fmpstmt.setString(17, pensionFamilyList.getGurdianName());
                        fmpstmt.setString(18, pensionFamilyList.getTiApplicableFlag());
                        fmpstmt.setString(19, pensionFamilyList.getFamilyRemarks());
                        fmpstmt.setString(20, pensionFamilyList.getIsNominee());
                        fmpstmt.setString(21, pensionFamilyList.getIsGuardian());
                        fmpstmt.setString(22, pensionFamilyList.getPriority());
//                        System.out.println("CVP : " + pensionFamilyList.getCvp());
                        fmpstmt.setString(23, pensionFamilyList.getCvp());
//                        System.out.println("CVP : " + pensionFamilyList.getCvpPercentage());
                        fmpstmt.setString(24, pensionFamilyList.getCvpPercentage());
                        fmpstmt.setString(25, pensionFamilyList.getSalutationgurdian());
                        fmpstmt.setString(26, pensionFamilyList.getFamilyAddress());
                        fmpstmt.setString(27, profile.getHrmsEmpId());
                        fmpstmt.executeUpdate();
                    }
//                } else {
//
//                    updateStmt = con.prepareStatement("UPDATE hrmis2.employee_pension_details SET  emp_gpf_series = ?, emp_acc_number = ?, hrms_emp_id = ?, emp_salutation = ?, emp_first_name = ?, "
//                            + "    emp_middle_name = ?, emp_last_name = ?, date_of_birth = ?, identification_mark = ?, height = ?, maritial_status = ?, religion = ?, "
//                            + "    identification_mark_2 = ?, sex = ?, nationality = ?, designation = ?, cvp_applied = ?, retirement_date = ?, "
//                            + "    cvp_percentage = ?, pan_no = ?, ifsc_code = ?, bank_acc_no = ?, payable_treasury = ?, email = ?, bank_branch = ?, ddo_code = ?, mobile_no = ?, city = ?, "
//                            + "    police_station = ?, town = ?, state = ?, pincode = ?, comm_city = ?, comm_police_station = ?, comm_district = ?, comm_town = ?, comm_state = ?, comm_pincode = ?, "
//                            + "    prev_pension_type = ?, prev_pension_amount = ?, prev_payable_treasury = ?, prev_ifsc_code = ?, pension_issuing_auth = ?, source = ?, ppo_or_fppo_no = ?,"
//                            + "    pension_effective_from_date = ?, prev_bank_branch = ?,gpf_no=?,district=?, acc_type=?, last_serve=? WHERE hrms_emp_id = ?");
//                    updateStmt.setString(1, profile.getTpfSeries());
//                    updateStmt.setString(2, profile.getTpfAcNo());
//                    updateStmt.setString(3, profile.getHrmsEmpId());
//                    updateStmt.setString(4, profile.getSalutationEmp());
//                    updateStmt.setString(5, profile.getEmployeeFirstName());
//                    updateStmt.setString(6, profile.getEmployeeMiddleName());
//                    updateStmt.setString(7, profile.getEmployeeLastName());
//                    if (profile.getDob() != null && !profile.getDob().equals("")) {
//                        updateStmt.setTimestamp(8, new Timestamp(new SimpleDateFormat("MM/dd/yyyy").parse(profile.getDob()).getTime()));
//                    } else {
//                        updateStmt.setTimestamp(8, null);
//                    }
//
//                    updateStmt.setString(9, profile.getPenIdnMark());
//                    updateStmt.setString(10, profile.getHeight());
//                    updateStmt.setString(11, profile.getIntMaritalStatusTypeId());
//                    updateStmt.setString(12, profile.getReligionId());
//                    updateStmt.setString(13, profile.getPenIdnMark2());
//                    updateStmt.setString(14, profile.getSex());
//                    updateStmt.setString(15, profile.getNationalityId());
//                    updateStmt.setString(9, profile.getPenIdnMark());
//                    updateStmt.setString(10, profile.getHeight());
//                    updateStmt.setString(11, profile.getIntMaritalStatusTypeId());
//                    updateStmt.setString(12, profile.getReligionId());
//                    updateStmt.setString(13, profile.getPenIdnMark2());
//                    updateStmt.setString(14, profile.getSex());
//                    updateStmt.setString(15, profile.getNationalityId());
//                    updateStmt.setString(16, profile.getDesignationId());
////                    updateStmt.setString(17, profile.getPenCategoryId());   pension_category = ?,
//                    updateStmt.setString(17, profile.getCvp());
//                    if (profile.getRetirementDate() != null && !profile.getRetirementDate().equals("")) {
//                        updateStmt.setTimestamp(18, new Timestamp(new SimpleDateFormat("MM/dd/yyyy").parse(profile.getRetirementDate()).getTime()));
//                    } else {
//                        updateStmt.setTimestamp(18, null);
//                    }
//                    updateStmt.setString(19, profile.getCvpPercentage());
//                    updateStmt.setString(20, profile.getPanNo());
//                    updateStmt.setString(21, profile.getIfscCode());
//                    updateStmt.setString(22, profile.getBankAcctNo());
//                    updateStmt.setString(23, profile.getTreasuryCode());
//                    updateStmt.setString(24, profile.getMailId());
//                    updateStmt.setString(25, profile.getBankBranch());
//                    updateStmt.setString(26, profile.getDdoName());
//                    updateStmt.setString(27, profile.getMobileNo());
//                    updateStmt.setString(28, profile.getPerAddcity());
//                    updateStmt.setString(29, profile.getPerAddpoliceStation());
//                    updateStmt.setString(30, profile.getPerAddtown());
//                    updateStmt.setString(31, profile.getPerAddstateId());
//                    updateStmt.setString(32, profile.getPerAddpin());
//                    updateStmt.setString(33, profile.getCommAddcity());
//                    updateStmt.setString(34, profile.getCommAddpoliceStation());
//                    updateStmt.setString(35, profile.getCommDistrictCode());
//                    updateStmt.setString(36, profile.getCommAddtown());
//                    updateStmt.setString(37, profile.getCommAddstateId());
//                    updateStmt.setString(38, profile.getCommAddpin());
//                    updateStmt.setString(39, profile.getPrevPenType());
//                    updateStmt.setString(40, profile.getPrevPenAmt());
//                    updateStmt.setString(41, profile.getPrevPenPayTresId());
//                    updateStmt.setString(42, profile.getPrevPenBankIfscCd());
//                    updateStmt.setString(43, profile.getPrevPenPia());
//                    updateStmt.setString(44, profile.getPrevPenSource());
//                    updateStmt.setString(45, profile.getPrevPPOOrFPPONo());
//                    if (profile.getPrevPensionEfffromDate() != null && !profile.getPrevPensionEfffromDate().equals("")) {
//                        updateStmt.setTimestamp(46, new Timestamp(new SimpleDateFormat("dd/MM/yyyy").parse(profile.getPrevPensionEfffromDate()).getTime()));
//                    } else {
//                        updateStmt.setTimestamp(46, null);
//                    }
//                    updateStmt.setString(47, profile.getPrevPenBankBranch());
//                    updateStmt.setString(48, gpfNo);
//                    updateStmt.setString(49, profile.getDistrictCode());
//                    updateStmt.setString(50, profile.getGpftype());
//                    updateStmt.setString(51, profile.getLastDistrictServe());
//                    updateStmt.setString(52, empid);
//                    updateStmt.executeUpdate();
//
//                    String profileStatus = null;
//
//                    fmupdate = con.prepareStatement("select pension_profile_status from employee_pension_details where hrms_emp_id=?");
//                    fmupdate.setString(1, empid);
//                    ResultSet profilers = fmupdate.executeQuery();
//                    if (profilers.next()) {
//                        profileStatus = profilers.getString("pension_profile_status");
//                    }
//
//                    DataBaseFunctions.closeSqlObjects(updateStmt);
//                    pstmtDelete = con.prepareStatement("DELETE FROM emp_pensioner_family_det WHERE hrms_emp_id = ?");
//                    pstmtDelete.setString(1, empid);
//                    if (profileStatus == null || profileStatus.equals("")) {
//                        pstmtDelete.executeUpdate();
//                    }
//
//                    DataBaseFunctions.closeSqlObjects(pstmtDelete);
//                    List familyList = profile.getPensionfamilylist();
//                    for (int i = 0; i < familyList.size(); i++) {
//                        pensionFamilyList = (PensionFamilyList) familyList.get(i);
//
//                        fmpstmt = con.prepareStatement("INSERT INTO emp_pensioner_family_det (salutation, f_name, m_name, l_name, relation, sex, date_of_birth, mobile_no, marital_status, share_percentage, ifsc_code, bank_acc_no, bank_branch, handicapped, handicapped_type, minor,guardian_details, ti_applicable, remarks, is_nominee, is_guardian,priority_label,cvp_applied,cvp_percentage,salutaion_of_guardian, hrms_emp_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
//                        // System.out.println("fmStatus11112222====="+pensionFamilyList.getIntMaritalStatusTypeId());
//                        fmpstmt.setString(1, pensionFamilyList.getSalutationFamily());
//                        fmpstmt.setString(2, pensionFamilyList.getGuardianfname());
//                        fmpstmt.setString(3, pensionFamilyList.getGuardianmname());
//                        fmpstmt.setString(4, pensionFamilyList.getGuardianlname());
//                        fmpstmt.setString(5, pensionFamilyList.getRelationshipId());
//                        fmpstmt.setString(6, pensionFamilyList.getSex());
//                        if (pensionFamilyList.getFamilyDob() != null && !pensionFamilyList.getFamilyDob().equals("")) {
//                            fmpstmt.setTimestamp(7, new Timestamp(new SimpleDateFormat("dd/MM/yyyy").parse(pensionFamilyList.getFamilyDob()).getTime()));
//                        } else {
//                            fmpstmt.setTimestamp(7, null);
//                        }
//                        fmpstmt.setString(8, pensionFamilyList.getMobileNo());
//                        fmpstmt.setString(9, pensionFamilyList.getIntMaritalStatusTypeId());
//                        fmpstmt.setString(10, pensionFamilyList.getFamilySharePercentage());
//                        fmpstmt.setString(11, pensionFamilyList.getIfscCode());
//                        fmpstmt.setString(12, pensionFamilyList.getBankAccountNo());
//                        fmpstmt.setString(13, pensionFamilyList.getBankBranchName());
//                        fmpstmt.setString(14, pensionFamilyList.getFamilyHandicappedFlag());
//                        fmpstmt.setString(15, pensionFamilyList.getFamilyHandicappedTypeId());
//                        fmpstmt.setString(16, pensionFamilyList.getMinorFlag());
//                        fmpstmt.setString(17, pensionFamilyList.getGurdianName());
//                        fmpstmt.setString(18, pensionFamilyList.getTiApplicableFlag());
//                        fmpstmt.setString(19, pensionFamilyList.getFamilyRemarks());
//                        fmpstmt.setString(20, pensionFamilyList.getIsNominee());
//                        fmpstmt.setString(21, pensionFamilyList.getIsGuardian());
//                        fmpstmt.setString(22, pensionFamilyList.getPriority());
////                        System.out.println("CVP : " + pensionFamilyList.getCvp());
//                        fmpstmt.setString(23, pensionFamilyList.getCvp());
////                        System.out.println("CVP : " + pensionFamilyList.getCvpPercentage());
//                        fmpstmt.setString(24, pensionFamilyList.getCvpPercentage());
//                        fmpstmt.setString(25, pensionFamilyList.getSalutationgurdian());
//                        fmpstmt.setString(26, profile.getHrmsEmpId());
//                        if (profileStatus == null || profileStatus.equals("")) {
//                            fmpstmt.executeUpdate();
//                        }
//                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(resultSet, selectStmt);
            DataBaseFunctions.closeSqlObjects(fmpstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void updateEmployeePensionDetails(PensionProfile profile, String empid) {
        Connection con = null;
        PreparedStatement fmpstmt = null;
        PreparedStatement selectStmt = null;
        PreparedStatement updateStmt = null;
        PreparedStatement fmupdate = null;
        PreparedStatement pstmtDelete = null;
        ResultSet resultSet = null;
        PensionFamilyList pensionFamilyList = null;

        try {
            con = this.dataSource.getConnection();
            selectStmt = con.prepareStatement("SELECT COUNT(*)cntEmp FROM hrmis2.employee_pension_details WHERE (hrms_emp_id = ? )");
            selectStmt.setString(1, empid);
            String gpfNo = profile.getTpfSeries() + "" + profile.getTpfAcNo();
            resultSet = selectStmt.executeQuery();
            if (resultSet.next()) {
//                System.out.println("profile.getDob():" + profile.getDob() + "::" + profile.getRetirementDate());

                if (resultSet.getInt("cntEmp") > 0) {
                    updateStmt = con.prepareStatement("UPDATE hrmis2.employee_pension_details SET  emp_gpf_series = ?, emp_acc_number = ?, hrms_emp_id = ?, emp_salutation = ?, emp_first_name = ?, "
                            + "    emp_middle_name = ?, emp_last_name = ?, date_of_birth = ?, identification_mark = ?, height = ?, maritial_status = ?, religion = ?, "
                            + "    identification_mark_2 = ?, sex = ?, nationality = ?, designation = ?, cvp_applied = ?, retirement_date = ?, "
                            + "    cvp_percentage = ?, pan_no = ?, ifsc_code = ?, bank_acc_no = ?, payable_treasury = ?, email = ?, bank_branch = ?, ddo_code = ?, mobile_no = ?, city = ?, "
                            + "    police_station = ?, town = ?, state = ?, pincode = ?, comm_city = ?, comm_police_station = ?, comm_district = ?, comm_town = ?, comm_state = ?, comm_pincode = ?, "
                            + "    prev_pension_type = ?, prev_pension_amount = ?, prev_payable_treasury = ?, prev_ifsc_code = ?, pension_issuing_auth = ?, source = ?, ppo_or_fppo_no = ?,"
                            + "    pension_effective_from_date = ?, prev_bank_branch = ?,gpf_no=?,district=?, acc_type=?, last_serve=? WHERE hrms_emp_id = ?");
                    updateStmt.setString(1, profile.getTpfSeries());
                    updateStmt.setString(2, profile.getTpfAcNo());
                    updateStmt.setString(3, profile.getHrmsEmpId());
                    updateStmt.setString(4, profile.getSalutationEmp());
                    updateStmt.setString(5, profile.getEmployeeFirstName());
                    updateStmt.setString(6, profile.getEmployeeMiddleName());
                    updateStmt.setString(7, profile.getEmployeeLastName());
                    if (profile.getDob() != null && !profile.getDob().equals("")) {
                        updateStmt.setTimestamp(8, new Timestamp(new SimpleDateFormat("MM/dd/yyyy").parse(profile.getDob()).getTime()));
                    } else {
                        updateStmt.setTimestamp(8, null);
                    }
                    updateStmt.setString(9, profile.getPenIdnMark());
                    updateStmt.setString(10, profile.getHeight());
                    updateStmt.setString(11, profile.getIntMaritalStatusTypeId());
                    updateStmt.setString(12, profile.getReligionId());
                    updateStmt.setString(13, profile.getPenIdnMark2());
                    updateStmt.setString(14, profile.getSex());
                    updateStmt.setString(15, profile.getNationalityId());
                    updateStmt.setString(9, profile.getPenIdnMark());
                    updateStmt.setString(10, profile.getHeight());
                    updateStmt.setString(11, profile.getIntMaritalStatusTypeId());
                    updateStmt.setString(12, profile.getReligionId());
                    updateStmt.setString(13, profile.getPenIdnMark2());
                    updateStmt.setString(14, profile.getSex());
                    updateStmt.setString(15, profile.getNationalityId());
                    updateStmt.setString(16, profile.getDesignationId());
//                    updateStmt.setString(17, profile.getPenCategoryId());   pension_category = ?,
                    updateStmt.setString(17, profile.getCvp());
                    if (profile.getRetirementDate() != null && !profile.getRetirementDate().equals("")) {
                        updateStmt.setTimestamp(18, new Timestamp(new SimpleDateFormat("MM/dd/yyyy").parse(profile.getRetirementDate()).getTime()));
                    } else {
                        updateStmt.setTimestamp(18, null);
                    }
                    updateStmt.setString(19, profile.getCvpPercentage());
                    updateStmt.setString(20, profile.getPanNo());
                    updateStmt.setString(21, profile.getIfscCode());
                    updateStmt.setString(22, profile.getBankAcctNo());
                    updateStmt.setString(23, profile.getTreasuryCode());
                    updateStmt.setString(24, profile.getMailId());
                    updateStmt.setString(25, profile.getBankBranch());
                    updateStmt.setString(26, profile.getDdoName());
                    updateStmt.setString(27, profile.getMobileNo());
                    updateStmt.setString(28, profile.getPerAddcity());
                    updateStmt.setString(29, profile.getPerAddpoliceStation());
                    updateStmt.setString(30, profile.getPerAddtown());
                    updateStmt.setString(31, profile.getPerAddstateId());
                    updateStmt.setString(32, profile.getPerAddpin());
                    updateStmt.setString(33, profile.getCommAddcity());
                    updateStmt.setString(34, profile.getCommAddpoliceStation());
                    updateStmt.setString(35, profile.getCommDistrictCode());
                    updateStmt.setString(36, profile.getCommAddtown());
                    updateStmt.setString(37, profile.getCommAddstateId());
                    updateStmt.setString(38, profile.getCommAddpin());
                    updateStmt.setString(39, profile.getPrevPenType());
                    updateStmt.setString(40, profile.getPrevPenAmt());
                    updateStmt.setString(41, profile.getPrevPenPayTresId());
                    updateStmt.setString(42, profile.getPrevPenBankIfscCd());
                    updateStmt.setString(43, profile.getPrevPenPia());
                    updateStmt.setString(44, profile.getPrevPenSource());
                    updateStmt.setString(45, profile.getPrevPPOOrFPPONo());
                    if (profile.getPrevPensionEfffromDate() != null && !profile.getPrevPensionEfffromDate().equals("")) {
                        updateStmt.setTimestamp(46, new Timestamp(new SimpleDateFormat("dd/MM/yyyy").parse(profile.getPrevPensionEfffromDate()).getTime()));
                    } else {
                        updateStmt.setTimestamp(46, null);
                    }
                    updateStmt.setString(47, profile.getPrevPenBankBranch());
                    updateStmt.setString(48, gpfNo);
                    updateStmt.setString(49, profile.getDistrictCode());
                    updateStmt.setString(50, profile.getGpftype());
                    updateStmt.setString(51, profile.getLastDistrictServe());
                    updateStmt.setString(52, empid);
                    updateStmt.executeUpdate();

                    String profileStatus = null;

                    fmupdate = con.prepareStatement("select pension_profile_status from employee_pension_details where hrms_emp_id=?");
                    fmupdate.setString(1, empid);
                    ResultSet profilers = fmupdate.executeQuery();
                    if (profilers.next()) {
                        profileStatus = profilers.getString("pension_profile_status");
                    }

                    DataBaseFunctions.closeSqlObjects(updateStmt);
                    pstmtDelete = con.prepareStatement("DELETE FROM emp_pensioner_family_det WHERE hrms_emp_id = ?");
                    pstmtDelete.setString(1, empid);
                    if (profileStatus == null || profileStatus.equals("")) {
                        pstmtDelete.executeUpdate();
                    }

                    DataBaseFunctions.closeSqlObjects(pstmtDelete);
                    List familyList = profile.getPensionfamilylist();
                    for (int i = 0; i < familyList.size(); i++) {
                        pensionFamilyList = (PensionFamilyList) familyList.get(i);
                        fmpstmt = con.prepareStatement("INSERT INTO emp_pensioner_family_det (salutation, f_name, m_name, l_name, relation, sex, date_of_birth, mobile_no, marital_status, share_percentage, ifsc_code, bank_acc_no, bank_branch, handicapped, handicapped_type, minor,guardian_details, ti_applicable, remarks, is_nominee, is_guardian,priority_label,cvp_applied,cvp_percentage,salutaion_of_guardian, address,hrms_emp_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)");
                        // System.out.println("fmStatus11112222====="+pensionFamilyList.getIntMaritalStatusTypeId());
                        fmpstmt.setString(1, pensionFamilyList.getSalutationFamily());
                        fmpstmt.setString(2, pensionFamilyList.getGuardianfname());
                        fmpstmt.setString(3, pensionFamilyList.getGuardianmname());
                        fmpstmt.setString(4, pensionFamilyList.getGuardianlname());
                        fmpstmt.setString(5, pensionFamilyList.getRelationshipId());
                        fmpstmt.setString(6, pensionFamilyList.getSex());
                        if (pensionFamilyList.getFamilyDob() != null && !pensionFamilyList.getFamilyDob().equals("")) {
                            fmpstmt.setTimestamp(7, new Timestamp(new SimpleDateFormat("dd/MM/yyyy").parse(pensionFamilyList.getFamilyDob()).getTime()));
                        } else {
                            fmpstmt.setTimestamp(7, null);
                        }
                        fmpstmt.setString(8, pensionFamilyList.getMobileNo());
                        fmpstmt.setString(9, pensionFamilyList.getIntMaritalStatusTypeId());
                        fmpstmt.setString(10, pensionFamilyList.getFamilySharePercentage());
                        fmpstmt.setString(11, pensionFamilyList.getIfscCode());
                        fmpstmt.setString(12, pensionFamilyList.getBankAccountNo());
                        fmpstmt.setString(13, pensionFamilyList.getBankBranchName());
                        fmpstmt.setString(14, pensionFamilyList.getFamilyHandicappedFlag());
                        fmpstmt.setString(15, pensionFamilyList.getFamilyHandicappedTypeId());
                        fmpstmt.setString(16, pensionFamilyList.getMinorFlag());
                        fmpstmt.setString(17, pensionFamilyList.getGurdianName());
                        fmpstmt.setString(18, pensionFamilyList.getTiApplicableFlag());
                        fmpstmt.setString(19, pensionFamilyList.getFamilyRemarks());
                        fmpstmt.setString(20, pensionFamilyList.getIsNominee());
                        fmpstmt.setString(21, pensionFamilyList.getIsGuardian());
                        fmpstmt.setString(22, pensionFamilyList.getPriority());
//                        System.out.println("CVP : " + pensionFamilyList.getCvp());
                        fmpstmt.setString(23, pensionFamilyList.getCvp());
//                        System.out.println("CVP : " + pensionFamilyList.getCvpPercentage());
                        fmpstmt.setString(24, pensionFamilyList.getCvpPercentage());
                        fmpstmt.setString(25, pensionFamilyList.getSalutationgurdian());
                        fmpstmt.setString(26, pensionFamilyList.getFamilyAddress());
                        fmpstmt.setString(27, profile.getHrmsEmpId());
                        if (profileStatus == null || profileStatus.equals("")) {
                            fmpstmt.executeUpdate();
                        }
                    }
                }
            }

        } catch (Exception e) {
        } finally {
            DataBaseFunctions.closeSqlObjects(resultSet, selectStmt);
            DataBaseFunctions.closeSqlObjects(fmpstmt);
            DataBaseFunctions.closeSqlObjects(updateStmt, fmupdate);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public List getPensionEmpNomineeDetails(String empid) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        List nomineeList = new ArrayList();
        PensionNomineeList pensionfamily = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("select *,g_relation.relation relationType,m_status from emp_pensioner_family_det "
                    + " left outer join g_relation on emp_pensioner_family_det.relation=g_relation.print_sl LEFT OUTER JOIN  g_marital ON  emp_pensioner_family_det.marital_status=g_marital.int_marital_status_id where hrms_emp_id=?  ");
            pstmt.setString(1, empid);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                //  System.out.println("Here");
                pensionfamily = new PensionNomineeList();
                pensionfamily.setSalutationNominee(rs.getString("salutation").trim());
                String firstName = StringUtils.defaultString(rs.getString("f_name"));
                String middleName = StringUtils.defaultString(rs.getString("m_name"));
                String lastName = StringUtils.defaultString(rs.getString("l_name"));
                String dependentName = (firstName + " " + (middleName.isEmpty() ? "" : middleName + " ") + lastName).trim();
                pensionfamily.setNomineeName(dependentName);
                if (rs.getString("sex") != null && rs.getString("sex").equals("F")) {
                    pensionfamily.setSex("FEMALE");
                } else if (rs.getString("sex") != null && rs.getString("sex").equals("M")) {
                    pensionfamily.setSex("MALE");
                } else {
                    pensionfamily.setSex("Other");
                }
                pensionfamily.setNomineeMaritalStatusId(rs.getString("m_status"));
                pensionfamily.setRelation(rs.getString("relationType"));
                pensionfamily.setBankBranchName(rs.getString("bank_branch"));
//                Timestamp dobTimestamp = rs.getTimestamp("date_of_birth");
//                if (dobTimestamp != null) {
//                    pensionfamily.setDob(dateFormat.format(dobTimestamp));
//                }
                if (rs.getDate("date_of_birth") != null && !rs.getDate("date_of_birth").equals("")) {
                    pensionfamily.setDob(CommonFunctions.getFormattedOutputDateDDMMYYYY(rs.getDate("date_of_birth")));
                } else {
                    pensionfamily.setDob("");
                }
                pensionfamily.setMobileNo(rs.getString("mobile_no"));
                pensionfamily.setNomineeType(rs.getString("is_nominee"));
                pensionfamily.setIfscCode(rs.getString("ifsc_code"));
                pensionfamily.setBankAccountNo(rs.getString("bank_acc_no"));
                pensionfamily.setSharePercentage(rs.getString("share_percentage"));
//              pensionfamily.setPriorityLevel(rs.getInt("priority_level"));
                //  pensionfamily.setNomineeAddress(rs.getString("nominee_address"));
                pensionfamily.setMinorFlag(rs.getString("minor"));
                pensionfamily.setSerialNo(rs.getInt("serial_no"));
                pensionfamily.setSalutationGaurdian(rs.getString("salutaion_of_guardian"));
                pensionfamily.setGurdianName(rs.getString("guardian_details"));
                pensionfamily.setCvp(rs.getString("cvp_applied"));
                pensionfamily.setCvpPercentage(rs.getString("cvp_percentage"));
                pensionfamily.setPriorityLevel(rs.getString("priority_label"));
                pensionfamily.setRetirementBenefitType(rs.getString("retirement_benefit_type"));
                pensionfamily.setNomineeTyp(rs.getString("nominee_type"));
                pensionfamily.setFamilyHandicappedFlag(rs.getString("handicapped"));
                pensionfamily.setNomineeAddress(rs.getString("address"));
                pensionfamily.setRemarks(rs.getString("remarks"));
                if (rs.getString("handicapped_type") != null && rs.getString("handicapped_type").equals("P")) {
                    pensionfamily.setFamilyHandicappedTypeId("Physically");
                } else if (rs.getString("handicapped_type") != null && rs.getString("handicapped_type").equals("M")) {
                    pensionfamily.setFamilyHandicappedTypeId("Mentally");
                }
//                pensionfamily.setFamilyHandicappedTypeId(rs.getString("handicapped_type"));
                if (rs.getString("is_guardian") != null && rs.getString("is_guardian").equals("Y")) {
                    pensionfamily.setGaurdianDetails("Yes");
                }
                nomineeList.add(pensionfamily);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return nomineeList;
    }

    @Override
    public PensionProfile getPensionEmpProfileDetailsById(String empid, String acctType) {
        PensionProfile employee = new PensionProfile();
        List availablePost = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement postStmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("select acc_type,ifms_post,submit_date,emp_gpf_series, emp_acc_number, hrms_emp_id, emp_salutation, emp_first_name, emp_middle_name, emp_last_name, date_of_birth,\n"
                    + "identification_mark, height, identification_mark_2, sex, pension_category, cvp_applied,\n"
                    + "final_gpf_applied_flag, final_gpf_applied_date, date_of_death, retirement_date, retirement_type, cvp_percentage, final_gpf_not_applied_reason, pan_no,\n"
                    + "ifsc_code, bank_acc_no, payable_treasury, email, bank_branch, bsr_code, mobile_no, city, police_station, town,\n"
                    + "empension.pincode, prev_pension_type, prev_pension_amount, prev_ifsc_code, pension_issuing_auth, source, ppo_or_fppo_no,\n"
                    + "pension_effective_from_date, prev_bank_branch,comm_police_station,  comm_town, comm_pincode, gpf_no, pension_profile_status, serial_no,\n"
                    + "g_treasury.tr_name payable_treasury_name,g_treasury1.tr_name prev_payable_treasury,g_district.dist_name district,g_district1.dist_name comm_district,\n"
                    + "g_state.state_name state, g_state1.state_name comm_state,m_status, off_en ddo_code, spn designation,g_marital.m_status maritial_status,\n"
                    + "g_religion.religion,g_nationality.nationality_name,comm_city,gd.dist_name lastdistname  from hrmis2.employee_pension_details empension \n"
                    + "left outer join g_treasury on empension.payable_treasury=g_treasury.tr_code\n"
                    + "left outer join g_treasury g_treasury1 on empension.prev_payable_treasury=g_treasury1.tr_code \n"
                    + "left outer join g_district on empension.district=g_district.int_district_id\n"
                    + "left outer join g_district g_district1 on empension.comm_district=g_district1.int_district_id \n"
                    + "left outer join g_state on empension.state=g_state.int_state_id\n"
                    + "left outer join g_district gd on empension.last_serve=gd.int_district_id\n"
                    + "left outer join g_state g_state1 on empension.comm_state=g_state1.int_state_id\n"
                    + "left outer join g_marital on empension.maritial_status=g_marital.int_marital_status_id\n"
                    + "left outer join g_religion on empension.religion=g_religion.int_religion_id\n"
                    + "left outer join g_nationality on empension.nationality=g_nationality.int_nationality_id \n"
                    + "left outer join (select * from g_office where is_ddo='Y' and online_bill_submission='Y')goff on empension.ddo_code=goff.ddo_code\n"
                    + "left outer join g_spc on empension.designation=g_spc.spc\n"
                    + "WHERE hrms_emp_id =? ");
            pstmt.setString(1, empid);
            rs = pstmt.executeQuery();
            if (rs.next()) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                //Employee Details
                employee.setTpfSeries(rs.getString("emp_gpf_series"));
                employee.setTpfAcNo(rs.getString("emp_acc_number"));
                employee.setHrmsEmpId(rs.getString("hrms_emp_id"));
                employee.setSalutationEmp(rs.getString("emp_salutation"));
                employee.setEmployeeFirstName(rs.getString("emp_first_name"));
                employee.setEmployeeMiddleName(rs.getString("emp_middle_name"));
                employee.setEmployeeLastName(rs.getString("emp_last_name"));

                //persional Details
                employee.setPenIdnMark(rs.getString("identification_mark"));
                /* Timestamp dobTimestamp = rs.getTimestamp("date_of_birth");
                 if (dobTimestamp != null) {
                 employee.setDob(dateFormat.format(dobTimestamp));
                 }*/
                employee.setDob(CommonFunctions.getFormattedOutputDateDDMMYYYY(rs.getDate("date_of_birth")));
                employee.setPenIdnMark2(rs.getString("identification_mark_2"));
                employee.setHeight(rs.getString("height"));
                employee.setReligionId(rs.getString("religion"));
                employee.setIntMaritalStatusTypeId(rs.getString("maritial_status"));
                employee.setNationalityId(rs.getString("nationality_name"));
                if (rs.getString("sex") != null && rs.getString("sex").equals("F")) {
                    employee.setSex("FEMALE");
                } else if (rs.getString("sex") != null && rs.getString("sex").equals("M")) {
                    employee.setSex("MALE");
                } else {
                    employee.setSex("Other");
                }

                //official Details
                employee.setDesignationId(rs.getString("designation"));
                employee.setPenCategoryId(rs.getString("pension_category"));
                employee.setCvp(rs.getString("cvp_applied"));
                employee.setFinalGpfAppliedFlag(rs.getString("final_gpf_applied_flag"));
                if (rs.getString("submit_date") != null && !rs.getString("submit_date").equals("")) {
                    employee.setStatus("Y");
                } else {
                    employee.setStatus("N");
                }
                employee.setRetirementDate(CommonFunctions.getFormattedOutputDateDDMMYYYY(rs.getDate("retirement_date")));
                employee.setCvpPercentage(rs.getString("cvp_percentage"));
                employee.setPanNo(rs.getString("pan_no"));
                employee.setRetirementType(rs.getString("retirement_type"));
                employee.setReason(rs.getString("final_gpf_not_applied_reason"));

                //Payment And Contact Details
                employee.setMailId(rs.getString("email"));
                employee.setMobileNo(rs.getString("mobile_no"));
                employee.setIfscCode(rs.getString("ifsc_code"));
                employee.setBankAcctNo(rs.getString("bank_acc_no"));
                employee.setTreasuryCode(rs.getString("payable_treasury_name"));
                employee.setBankBranch(rs.getString("bank_branch"));
                employee.setBsrCode(rs.getString("bsr_code"));
                employee.setDdoName(rs.getString("ddo_code"));
                employee.setLastDistrictServe(rs.getString("lastdistname"));

                //Permanent Address       
                employee.setPerAddcity(rs.getString("city"));
                employee.setPerAddpoliceStation(rs.getString("police_station"));
                employee.setPerAddtown(rs.getString("town"));
                employee.setPerAddstateId(rs.getString("state"));
                employee.setPerAddpin(rs.getString("pincode"));
                employee.setDistrictCode(rs.getString("district"));

                //Commm Address
                employee.setCommAddcity(rs.getString("comm_city"));
                employee.setCommAddpoliceStation(rs.getString("comm_police_station"));
                employee.setCommDistrictCode(rs.getString("comm_district"));
                employee.setCommAddtown(rs.getString("comm_town"));
                employee.setCommAddstateId(rs.getString("comm_state"));
                employee.setCommAddpin(rs.getString("comm_pincode"));

                //previous Pension Details
                employee.setPrevPenType(rs.getString("prev_pension_type"));
                employee.setPrevPenAmt(rs.getString("prev_pension_amount"));
                employee.setPrevPenPayTresCode(rs.getString("prev_payable_treasury"));
                employee.setPrevPenBankIfscCd(rs.getString("prev_ifsc_code"));
                employee.setPrevPenPia(rs.getString("pension_issuing_auth"));
                employee.setPrevPenSource(rs.getString("source"));
                employee.setPrevPPOOrFPPONo(rs.getString("ppo_or_fppo_no"));
                employee.setPrevPenBankBranch(rs.getString("prev_bank_branch"));
                Timestamp EffDate = rs.getTimestamp("pension_effective_from_date");
                if (EffDate != null) {
                    employee.setPrevPensionEfffromDate(dateFormat.format(EffDate));
                }
                String selectedPostCode = rs.getString("ifms_post");

                acctType = rs.getString("acc_type");

                if (acctType.equals("GPF")) {
                    postStmt = con.prepareStatement("select post_code, post_name, user_type from ifms_post where user_type='G' order by post_name");
                } else if (acctType.equals("TPF")) {
                    postStmt = con.prepareStatement("select post_code, post_name, user_type from ifms_post where user_type='T' order by post_name");
                }
                rs1 = postStmt.executeQuery();
                while (rs1.next()) {
                    PensionProfile postEmployee = new PensionProfile();
                    postEmployee.setPostCode(rs1.getString("post_code"));
                    postEmployee.setPostName(rs1.getString("post_name"));
                    postEmployee.setUserType(rs1.getString("user_type"));
                    availablePost.add(postEmployee);
                    if (selectedPostCode != null && selectedPostCode.equals(postEmployee.getPostCode())) {
                        employee.setPostCode(postEmployee.getPostCode());
                    }
                }
                employee.setAvailablePosts(availablePost);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, rs1);
            DataBaseFunctions.closeSqlObjects(pstmt, postStmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return employee;
    }

    public List getPensionEmpGuardianDetails(String empid) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        List guardianlist = new ArrayList();
        PensionFamilyList pensionGuardian = null;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("select salutation,f_name,m_name,l_name,g_relation.relation relationType "
                    + "from emp_pensioner_family_det "
                    + "left outer join g_relation on emp_pensioner_family_det.relation=g_relation.print_sl "
                    + "where hrms_emp_id=? and is_guardian='Y'  LIMIT 1");
            pstmt.setString(1, empid);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                pensionGuardian = new PensionFamilyList();

                pensionGuardian.setSalutationgurdian(rs.getString("salutation"));
                pensionGuardian.setGuardianfname(rs.getString("f_name"));
                pensionGuardian.setGuardianmname(rs.getString("m_name"));
                pensionGuardian.setGuardianlname(rs.getString("l_name"));
                pensionGuardian.setRelation_type(rs.getString("relationType"));
                guardianlist.add(pensionGuardian);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return guardianlist;
    }

    @Override
    public void updatePensionCompletedStatusData(PensionProfile profile, String empid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("UPDATE employee_pension_details SET pension_profile_status=?,pension_category=?,cvp_applied=?,retirement_type=?,cvp_percentage=?,submit_date=?,ifms_post=? WHERE hrms_emp_id=?");
            pstmt.setString(1, "Y");
            pstmt.setString(2, profile.getPenCategoryId());
            pstmt.setString(3, profile.getCvp());
            pstmt.setString(4, profile.getRetirementType());
            pstmt.setString(5, profile.getCvpPercentage());
            pstmt.setTimestamp(6, new Timestamp(new Date().getTime()));
            pstmt.setString(7, profile.getPostCode());
            pstmt.setString(8, empid);
            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void updatePensionType(PensionNomineeList pensionNomineeList, String empid, int serialNo) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("UPDATE emp_pensioner_family_det SET nominee_type=?,retirement_benefit_type=?,priority_label=? WHERE hrms_emp_id=? and serial_no=?");
            pstmt.setString(1, pensionNomineeList.getNomineeTyp());
            pstmt.setString(2, pensionNomineeList.getRetirementBenefitType());
            pstmt.setString(3, pensionNomineeList.getPriorityLevel());
            pstmt.setString(4, empid);
            pstmt.setInt(5, serialNo);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    public String checkProfileStatus(String empid) {
        PensionProfile employee = new PensionProfile();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String empStatus = null;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("select pension_profile_status from employee_pension_details where hrms_emp_id =? OR CONCAT(emp_gpf_series, emp_acc_number) = ?");
            pstmt.setString(1, empid);
            pstmt.setString(2, empid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                empStatus = rs.getString("pension_profile_status");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empStatus;
    }

    @Override
    public PensionProfile getEmpPensionDet(String empid) {
        PensionProfile employee = new PensionProfile();
        PensionNomineeList nomineelist = new PensionNomineeList();
        PensionFamilyList familylist = new PensionFamilyList();
        ArrayList outList = new ArrayList();
        ArrayList outList1 = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM employee_pension_details WHERE hrms_emp_id = ? OR CONCAT(emp_gpf_series, emp_acc_number) = ?");
//            pstmt = con.prepareStatement("select * from employee_pension_details where hrms_emp_id = ?");
            pstmt.setString(1, empid);
            pstmt.setString(2, empid);
            rs = pstmt.executeQuery();
            if (rs.next()) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                //Employee Details
                employee.setTpfSeries(StringUtils.defaultString(rs.getString("emp_gpf_series")));
                employee.setTpfAcNo(StringUtils.defaultString(rs.getString("emp_acc_number")));
                employee.setHrmsEmpId(rs.getString("hrms_emp_id"));
                employee.setSalutationEmp(StringUtils.defaultString(rs.getString("emp_salutation")));
                employee.setEmployeeFirstName(StringUtils.defaultString(rs.getString("emp_first_name")));
                employee.setEmployeeMiddleName(StringUtils.defaultString(rs.getString("emp_middle_name")));
                employee.setEmployeeLastName(StringUtils.defaultString(rs.getString("emp_last_name")));
                //persional Details
                employee.setPenIdnMark(StringUtils.defaultString(rs.getString("identification_mark")));
                Timestamp dobTimestamp = rs.getTimestamp("date_of_birth");
                if (dobTimestamp != null) {
                    employee.setDob(dateFormat.format(dobTimestamp));
                } else {
                    employee.setDob("");
                }
                employee.setPenIdnMark2(StringUtils.defaultString(rs.getString("identification_mark_2")));
                employee.setHeight(StringUtils.defaultString(rs.getString("height")));
                employee.setReligionId(StringUtils.defaultString(rs.getString("religion")));
                employee.setIntMaritalStatusTypeId(StringUtils.defaultString(rs.getString("maritial_status")));
                employee.setNationalityId(StringUtils.defaultString(rs.getString("nationality")));
                employee.setSex(StringUtils.defaultString(rs.getString("sex")));

                //official Details
                employee.setDesignationId(StringUtils.defaultString(rs.getString("ifms_post")));
                employee.setPenCategoryId(StringUtils.defaultString(rs.getString("pension_category")));
                employee.setCvp(StringUtils.defaultString(rs.getString("cvp_applied")));
                employee.setFinalGpfAppliedFlag(StringUtils.defaultString(rs.getString("final_gpf_applied_flag")));
                if (rs.getString("submit_date") != null && !rs.getString("submit_date").equals("")) {
                    employee.setStatus("Y");
                } else {
                    employee.setStatus("N");
                }

                if (rs.getDate("retirement_date") != null && !rs.getDate("retirement_date").equals("")) {
                    employee.setRetirementDate(CommonFunctions.getFormattedOutputDateDDMMYYYY(rs.getDate("retirement_date")));
                } else {
                    employee.setRetirementDate("");
                }
                employee.setCvpPercentage(StringUtils.defaultString(rs.getString("cvp_percentage")));
                employee.setPanNo(StringUtils.defaultString(rs.getString("pan_no")));
                employee.setPenTypeId(StringUtils.defaultString(rs.getString("retirement_type")));
                employee.setReason(StringUtils.defaultString(rs.getString("final_gpf_not_applied_reason")));

                if (rs.getDate("final_gpf_applied_date") != null && !rs.getDate("final_gpf_applied_date").equals("")) {
                    employee.setFinalGpfAppliedDate(CommonFunctions.getFormattedOutputDateDDMMYYYY(rs.getDate("final_gpf_applied_date")));
                } else {
                    employee.setFinalGpfAppliedDate("");
                }
                if (rs.getDate("date_of_death") != null && !rs.getDate("date_of_death").equals("")) {
                    employee.setDod(CommonFunctions.getFormattedOutputDateDDMMYYYY(rs.getDate("date_of_death")));
                } else {
                    employee.setDod("");
                }

                //Payment And Contact Details
                employee.setMailId(StringUtils.defaultString(rs.getString("email")));
                employee.setMobileNo(StringUtils.defaultString(rs.getString("mobile_no")));
                employee.setIfscCode(StringUtils.defaultString(rs.getString("ifsc_code")));
                employee.setBankAcctNo(StringUtils.defaultString(rs.getString("bank_acc_no")));
                employee.setIntTreasuryId(StringUtils.defaultString(rs.getString("payable_treasury")));
                employee.setBankBranch(StringUtils.defaultString(rs.getString("bank_branch")));
                employee.setBsrCode(StringUtils.defaultString(rs.getString("bsr_code")));
                employee.setDdoId(StringUtils.defaultString(rs.getString("ddo_code")));

                //Permanent Address       
                employee.setPerAddcity(StringUtils.defaultString(rs.getString("city")));
                employee.setPerAddpoliceStation(StringUtils.defaultString(rs.getString("police_station")));
                employee.setPerAddtown(StringUtils.defaultString(rs.getString("town")));
                employee.setPerAddstateId(StringUtils.defaultString(rs.getString("state")));
                employee.setPerAddpin(StringUtils.defaultString(rs.getString("pincode")));
                employee.setDistrictCode(StringUtils.defaultString(rs.getString("district")));
                //Commm Address
                employee.setCommAddcity(StringUtils.defaultString(rs.getString("comm_city")));
                employee.setCommAddpoliceStation(StringUtils.defaultString(rs.getString("comm_police_station")));
                //employee.setcDistrictCode(rs.getString("comm_district"));

                employee.setCommdist(rs.getString("comm_district"));
                if (rs.getString("comm_town") != null) {
                    employee.setCommAddtown(rs.getString("comm_town"));
                } else {
                    employee.setCommAddtown("NA");
                }
                employee.setCommAddstateId(rs.getString("comm_state"));
                employee.setCommAddpin(rs.getString("comm_pincode"));
                employee.setCommdist(StringUtils.defaultString(rs.getString("comm_district")));
                employee.setCommAddtown(StringUtils.defaultString(rs.getString("comm_town")));
                employee.setCommAddstateId(StringUtils.defaultString(rs.getString("comm_state")));
                employee.setCommAddpin(StringUtils.defaultString(rs.getString("comm_pincode")));
                //previous Pension Details
                employee.setPrevPenType(StringUtils.defaultString(rs.getString("prev_pension_type")));
                employee.setPrevPenAmt(StringUtils.defaultString(rs.getString("prev_pension_amount")));
                employee.setPrevPenPayTresId(StringUtils.defaultString(rs.getString("prev_payable_treasury")));
                employee.setPrevPenBankIfscCd(StringUtils.defaultString(rs.getString("prev_ifsc_code")));
                employee.setPrevPenPia(StringUtils.defaultString(rs.getString("pension_issuing_auth")));
                employee.setPrevPenSource(StringUtils.defaultString(rs.getString("source")));
                employee.setPrevPPOOrFPPONo(StringUtils.defaultString(rs.getString("ppo_or_fppo_no")));
                employee.setPrevPenBankBranch(StringUtils.defaultString(rs.getString("prev_bank_branch")));
                employee.setLastDistrictServe(StringUtils.defaultString(rs.getString("last_serve")));
                if (rs.getDate("pension_effective_from_date") != null) {
                    employee.setPrevPensionEfffromDate(CommonFunctions.getFormattedOutputDateDDMMYYYY(rs.getDate("pension_effective_from_date")));

                } else {
                    employee.setPrevPensionEfffromDate("");
                }
                //Family
                String SQLFamily = "select epfd.* from "
                        + "employee_pension_details  epd "
                        + "inner join emp_pensioner_family_det epfd on epd.hrms_emp_id=epfd.hrms_emp_id "
                        + "where CONCAT(emp_gpf_series, emp_acc_number)=? or epfd.hrms_emp_id=?";
                pstmt = con.prepareStatement(SQLFamily);
                pstmt.setString(1, empid);
                pstmt.setString(2, empid);
                rs1 = pstmt.executeQuery();
                while (rs1.next()) {
                    familylist = new PensionFamilyList();
//                    familylist.setSalutationFamily(StringUtils.defaultString(rs1.getString("salutation")));
                    String salutationdata = rs1.getString("salutation");
                    if (salutationdata != null && !salutationdata.isEmpty()) {
                        if (salutationdata.equals("Mr.")) {
                            familylist.setSalutationFamily("Mr.");
                        } else if (salutationdata.equals("Mrs.")) {
                            familylist.setSalutationFamily("Mrs.");
                        } else if (salutationdata.equals("Miss")) {
                            familylist.setSalutationFamily("Miss");
                        } else if (salutationdata.equals("Dr(Allopathic)")) {
                            familylist.setSalutationFamily("Dr(Allopathic)");
                        } else if (salutationdata.equals("Dr(Med. College Prof)")) {
                            familylist.setSalutationFamily("Dr(Med. College Prof)");
                        } else if (salutationdata.equals("Dr(Academic)")) {
                            familylist.setSalutationFamily("Dr(Academic)");
                        } else if (salutationdata.equals("Dr(Others)")) {
                            familylist.setSalutationFamily("Dr(Others)");
                        } else if (salutationdata.equals("Prof.")) {
                            familylist.setSalutationFamily("Prof.");
                        } else {
                            familylist.setSalutationFamily("Other");
                        }
                    }

                    String firstName = StringUtils.defaultString(rs1.getString("f_name"));
                    String middleName = StringUtils.defaultString(rs1.getString("m_name"));
                    String lastName = StringUtils.defaultString(rs1.getString("l_name"));
                    String dependentName = (firstName + " " + (middleName.isEmpty() ? "" : middleName + " ") + lastName).trim();
                    familylist.setDependentName(dependentName);
                    familylist.setSex(StringUtils.defaultString(rs1.getString("sex")));
                    familylist.setIntMaritalStatusTypeId(StringUtils.defaultString(rs1.getString("marital_status")));
                    familylist.setRelationshipId(StringUtils.defaultString(rs1.getString("relation")));

                    String relation = rs1.getString("relation");
                    if (relation != null && !relation.isEmpty()) {
                        if (relation.equals("8")) {
                            familylist.setRelationshipId("9");
                        } else if (relation.equals("9")) {
                            familylist.setRelationshipId("14");
                        } else if (relation.equals("10")) {
                            familylist.setRelationshipId("13");
                        } else if (relation.equals("7")) {
                            familylist.setRelationshipId("5");
                        } else if (relation.equals("2")) {
                            familylist.setRelationshipId("2");
                        } else if (relation.equals("1")) {
                            familylist.setRelationshipId("1");
                        } else if (relation.equals("5")) {
                            familylist.setRelationshipId("4");
                        } else if (relation.equals("6")) {
                            familylist.setRelationshipId("6");
                        } else if (relation.equals("4")) {
                            familylist.setRelationshipId("3");
                        } else if (relation.equals("14")) {
                            familylist.setRelationshipId("10");
                        } else {
                            familylist.setRelationshipId("");
                        }
                    }

                    familylist.setBankBranchName(StringUtils.defaultString(rs1.getString("bank_branch")));
                    if (rs1.getDate("date_of_birth") != null && !rs1.getDate("date_of_birth").equals("")) {
                        familylist.setFamilyDob(CommonFunctions.getFormattedOutputDateDDMMYYYY(rs1.getDate("date_of_birth")));
                    } else {
                        familylist.setFamilyDob("");
                    }
                    familylist.setMobileNo(StringUtils.defaultString(rs1.getString("mobile_no")));
                    familylist.setIfscCode(StringUtils.defaultString(rs1.getString("ifsc_code")));
                    familylist.setBankAccountNo(StringUtils.defaultString(rs1.getString("bank_acc_no")));
                    familylist.setFamilySharePercentage(StringUtils.defaultString(rs1.getString("share_percentage")));
                    String fMinorValue = rs1.getString("minor");
                    if (fMinorValue != null && fMinorValue.equalsIgnoreCase("yes")) {
                        familylist.setMinorFlag("Y");
                    } else {
                        familylist.setMinorFlag("N");
                    }
                    String familyHandicappedValue = rs1.getString("handicapped");
                    if (familyHandicappedValue != null && familyHandicappedValue.equalsIgnoreCase("yes")) {
                        familylist.setFamilyHandicappedFlag("Y");
                    } else {
                        familylist.setFamilyHandicappedFlag("N");
                    }
                    familylist.setFamilyHandicappedTypeId(StringUtils.defaultString(rs1.getString("handicapped_type")));
                    String tiApplicableValue = rs1.getString("ti_applicable");
                    if (tiApplicableValue != null && tiApplicableValue.equalsIgnoreCase("yes")) {
                        familylist.setTiApplicableFlag("Y");
                    } else {
                        familylist.setTiApplicableFlag("N");
                    }
                    familylist.setFamilyRemarks(StringUtils.defaultString(rs1.getString("remarks")));

//                    && rs1.getString("is_guardian") != null && rs1.getString("is_guardian").equals("Y")
                    if (rs1.getString("minor") != null && rs1.getString("minor").equals("Yes")) {
                        familylist.setSalutationFamilyGuardian(StringUtils.defaultString(rs1.getString("salutaion_of_guardian")));
                        familylist.setGurdianName(StringUtils.defaultString(rs1.getString("guardian_details")));
                    }
                    outList.add(familylist);
                }

                String SQLGuardian = "select epfd.* from "
                        + "employee_pension_details  epd "
                        + "inner join emp_pensioner_family_det epfd on epd.hrms_emp_id=epfd.hrms_emp_id "
                        + "where  CONCAT(emp_gpf_series, emp_acc_number)=? or epfd.hrms_emp_id=? and is_guardian=? Limit 1";
                pstmt = con.prepareStatement(SQLGuardian);
                pstmt.setString(1, empid);
                pstmt.setString(2, empid);
                pstmt.setString(3, "Y");
                rs2 = pstmt.executeQuery();
                if (rs2.next()) {
                    String guardianSaluta = rs2.getString("salutation");
                    String firstName = rs2.getString("f_name");
                    String middleName = rs2.getString("m_name");
                    String lastName = rs2.getString("l_name");
                    String guardianrelationId = rs2.getString("relation");

                    String salutation = "Other";
                    if (guardianSaluta != null && !guardianSaluta.isEmpty()
                            && (guardianSaluta.equals("Mr.") || guardianSaluta.equals("Mrs.") || guardianSaluta.equals("Miss")
                            || guardianSaluta.equals("Dr(Allopathic)") || guardianSaluta.equals("Dr(Med. College Prof)")
                            || guardianSaluta.equals("Dr(Academic)") || guardianSaluta.equals("Dr(Others)")
                            || guardianSaluta.equals("Prof."))) {
                        salutation = guardianSaluta;
                    }

                    employee.setSalutation(salutation);
                    employee.setGuardianFName(StringUtils.defaultString(firstName));
                    employee.setGuardianMName(StringUtils.defaultString(middleName));
                    employee.setGuardianLName(StringUtils.defaultString(lastName));
                    employee.setGuardianRelId(StringUtils.defaultString(guardianrelationId));
                }

                String SQLNominee = "select epfd.* from "
                        + "employee_pension_details  epd "
                        + "inner join emp_pensioner_family_det epfd on epd.hrms_emp_id=epfd.hrms_emp_id "
                        + "where  CONCAT(emp_gpf_series, emp_acc_number)=? or epfd.hrms_emp_id=? and is_nominee=?";
                pstmt = con.prepareStatement(SQLNominee);
                pstmt.setString(1, empid);
                pstmt.setString(2, empid);
                pstmt.setString(3, "Y");
                rs3 = pstmt.executeQuery();
                while (rs3.next()) {
                    if (!rs3.getString("share_percentage").equals("0")) {
                        nomineelist = new PensionNomineeList();
//                        nomineelist.setPenBenefitTypeId("1");
//                        nomineelist.setNomineeOriginalOrAlterId("O");
//                        nomineelist.setSalutationNominee(StringUtils.defaultString(rs3.getString("salutation")));
                        String nomineesaluta = rs3.getString("salutation");
                        if (nomineesaluta != null && !nomineesaluta.isEmpty()) {
                            if (nomineesaluta.equals("Mr.")) {
                                nomineelist.setSalutationNominee("Mr.");
                            } else if (nomineesaluta.equals("Mrs.")) {
                                nomineelist.setSalutationNominee("Mrs.");
                            } else if (nomineesaluta.equals("Miss")) {
                                nomineelist.setSalutationNominee("Miss");
                            } else if (nomineesaluta.equals("Dr(Allopathic)")) {
                                nomineelist.setSalutationNominee("Dr(Allopathic)");
                            } else if (nomineesaluta.equals("Dr(Med. College Prof)")) {
                                nomineelist.setSalutationNominee("Dr(Med. College Prof)");
                            } else if (nomineesaluta.equals("Dr(Academic)")) {
                                nomineelist.setSalutationNominee("Dr(Academic)");
                            } else if (nomineesaluta.equals("Dr(Others)")) {
                                nomineelist.setSalutationNominee("Dr(Others)");
                            } else if (nomineesaluta.equals("Prof.")) {
                                nomineelist.setSalutationNominee("Prof.");
                            } else {
                                nomineelist.setSalutationNominee("Other");
                            }
                        }
                        String firstName = StringUtils.defaultString(rs3.getString("f_name"));
                        String middleName = StringUtils.defaultString(rs3.getString("m_name"));
                        String lastName = StringUtils.defaultString(rs3.getString("l_name"));
                        String dependentName = (firstName + " " + (middleName.isEmpty() ? "" : middleName + " ") + lastName).trim();
                        nomineelist.setNomineeName(dependentName);
                        nomineelist.setFamilySex(StringUtils.defaultString(rs3.getString("sex")));
                        nomineelist.setRelationId(StringUtils.defaultString(rs3.getString("relation")));

                        if (rs3.getDate("date_of_birth") != null && !rs3.getDate("date_of_birth").equals("")) {
                            nomineelist.setDob(CommonFunctions.getFormattedOutputDateDDMMYYYY(rs3.getDate("date_of_birth")));
                        } else {
                            nomineelist.setDob("");
                        }
                        nomineelist.setPriorityLevel(StringUtils.defaultString(rs3.getString("priority_label")));
                        nomineelist.setSharePercentage(StringUtils.defaultString(rs3.getString("share_percentage")));
                        nomineelist.setIfscCode(StringUtils.defaultString(rs3.getString("ifsc_code")));
                        nomineelist.setBankAccountNo(StringUtils.defaultString(rs3.getString("bank_acc_no")));
                        nomineelist.setBankBranchName(StringUtils.defaultString(rs3.getString("bank_branch")));
                        nomineelist.setPenBenefitTypeId(StringUtils.defaultString(rs3.getString("retirement_benefit_type")));
                        nomineelist.setNomineeOriginalOrAlterId(StringUtils.defaultString(rs3.getString("nominee_type")));
                        String minorValue = rs3.getString("minor");
                        if (minorValue != null && minorValue.equalsIgnoreCase("yes")) {
                            nomineelist.setMinorFlag("Y");
                        } else {
                            nomineelist.setMinorFlag("N");
                        }
                        nomineelist.setMobileNo(StringUtils.defaultString(rs3.getString("mobile_no")));
                        nomineelist.setNomineeMaritalStatusId(StringUtils.defaultString(rs3.getString("marital_status")));
                        if (rs3.getString("minor") != null && rs3.getString("minor").equals("Yes") && rs3.getString("is_guardian") != null && rs3.getString("is_guardian").equals("Y")) {
                            nomineelist.setSalutationNomineeGaurdian(StringUtils.defaultString(rs3.getString("salutaion_of_guardian")));
                            nomineelist.setGurdianName(StringUtils.defaultString(rs3.getString("guardian_details")));
                        }

                        outList1.add(nomineelist);
                    }
                }
                employee.setPensionfamilylist(outList);
                employee.setPensionnomineelist(outList1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return employee;
    }

}

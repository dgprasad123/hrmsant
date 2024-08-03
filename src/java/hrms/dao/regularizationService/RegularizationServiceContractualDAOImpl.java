package hrms.dao.regularizationService;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.regularizeService.RegularizeServiceContractualForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class RegularizationServiceContractualDAOImpl implements RegularizationServiceContractualDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    protected ServiceBookLanguageDAO sbDAO;
    protected NotificationDAO notificationDao;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    public void setNotificationDao(NotificationDAO notificationDao) {
        this.notificationDao = notificationDao;
    }

    @Override
    public RegularizeServiceContractualForm getRegularizationServiceContractualData(RegularizeServiceContractualForm regularizeServiceForm) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "select emp_regularization_contractual.*,ARRAY_TO_STRING(ARRAY[emp_mast.INITIALS, emp_mast.F_NAME, emp_mast.M_NAME,emp_mast.L_NAME], ' ') EMP_NAME from emp_regularization_contractual"
                    + " inner join emp_mast on emp_regularization_contractual.emp_id=emp_mast.emp_id where emp_regularization_contractual.emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, regularizeServiceForm.getEmpid());
            rs = pst.executeQuery();
            if (rs.next()) {
                regularizeServiceForm.setHregid(rs.getString("reg_id"));

                regularizeServiceForm.setEmpname(rs.getString("EMP_NAME"));

                regularizeServiceForm.setTxtNotOrdNo(rs.getString("ord_no"));
                regularizeServiceForm.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("ord_date")));

                regularizeServiceForm.setTxtBasic(rs.getString("basic"));
                regularizeServiceForm.setRdPayCommission(rs.getString("pay_commission"));
                regularizeServiceForm.setTxtGP(rs.getString("gp"));

                regularizeServiceForm.setSltGenericPost(rs.getString("gpc"));
                regularizeServiceForm.setSltSubstantivePost(rs.getString("spc"));

                regularizeServiceForm.setSltGender(rs.getString("gender"));
                regularizeServiceForm.setTxtMobile(rs.getString("mobile"));
                regularizeServiceForm.setSltAcctType(rs.getString("acct_type"));
                regularizeServiceForm.setChkIfAssumed(rs.getString("is_assumed"));
                regularizeServiceForm.setTxtAccNo(rs.getString("acc_no"));
                regularizeServiceForm.setTxtDOB(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                regularizeServiceForm.setTxtDOJ(CommonFunctions.getFormattedOutputDate1(rs.getDate("doj")));
                regularizeServiceForm.setTxtAadharNo(rs.getString("aadhar_no"));
                regularizeServiceForm.setSltPostGroup(rs.getString("post_group"));
            } else {
                sql = "select emp_mast.*,ARRAY_TO_STRING(ARRAY[emp_mast.INITIALS, emp_mast.F_NAME, emp_mast.M_NAME,emp_mast.L_NAME], ' ') EMP_NAME,(select id_no from emp_id_doc where emp_id=emp_mast.emp_id and id_description='AADHAAR')aadharno from emp_mast where emp_id=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, regularizeServiceForm.getEmpid());
                rs = pst.executeQuery();
                if (rs.next()) {
                    regularizeServiceForm.setEmpname(rs.getString("EMP_NAME"));
                    regularizeServiceForm.setTxtBasic(rs.getString("cur_basic_salary"));
                    regularizeServiceForm.setRdPayCommission(rs.getString("pay_commission"));
                    regularizeServiceForm.setTxtGP(rs.getString("gp"));
                    regularizeServiceForm.setSltGender(rs.getString("gender"));
                    regularizeServiceForm.setTxtMobile(rs.getString("mobile"));
                    regularizeServiceForm.setSltAcctType("PRAN");
                    regularizeServiceForm.setTxtAccNo(rs.getString("gpf_no"));
                    regularizeServiceForm.setTxtDOB(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                    regularizeServiceForm.setTxtDOJ(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe_gov")));
                    regularizeServiceForm.setTxtAadharNo(rs.getString("aadharno"));
                    regularizeServiceForm.setSltPostGroup(rs.getString("post_grp_type"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return regularizeServiceForm;
    }

    @Override
    public void insertRegularizationContractualData(RegularizeServiceContractualForm regularizeService, String loginempid) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "INSERT INTO emp_regularization_contractual(emp_id,ord_no,ord_date,basic,pay_commission,gpc,spc,gender,mobile,acct_type,is_assumed,dob,doj,aadhar_no,"
                    + "post_group,acc_no,gp,entry_taken_by,entry_taken_on,if_visible) "
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, regularizeService.getEmpid());
            pst.setString(2, regularizeService.getTxtNotOrdNo());
            if (regularizeService.getTxtNotOrdDt() != null && !regularizeService.getTxtNotOrdDt().equals("")) {
                pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(regularizeService.getTxtNotOrdDt()).getTime()));
            } else {
                pst.setTimestamp(3, null);
            }
            if (regularizeService.getTxtBasic() != null && !regularizeService.getTxtBasic().equals("")) {
                double basicdata = Double.parseDouble(regularizeService.getTxtBasic());
                pst.setInt(4, (int) basicdata);
            } else {
                pst.setInt(4, 0);
            }
            if (regularizeService.getRdPayCommission() != null && !regularizeService.getRdPayCommission().equals("")) {
                pst.setInt(5, Integer.parseInt(regularizeService.getRdPayCommission()));
            } else {
                pst.setInt(5, 0);
            }
            pst.setString(6, regularizeService.getSltGenericPost());
            pst.setString(7, regularizeService.getSltSubstantivePost());
            pst.setString(8, regularizeService.getSltGender());
            pst.setString(9, regularizeService.getTxtMobile());
            pst.setString(10, regularizeService.getSltAcctType());
            pst.setString(11, regularizeService.getChkIfAssumed());
            if (regularizeService.getTxtDOB() != null && !regularizeService.getTxtDOB().equals("")) {
                pst.setTimestamp(12, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(regularizeService.getTxtDOB()).getTime()));
            } else {
                pst.setTimestamp(12, null);
            }
            if (regularizeService.getTxtDOJ() != null && !regularizeService.getTxtDOJ().equals("")) {
                pst.setTimestamp(13, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(regularizeService.getTxtDOJ()).getTime()));
            } else {
                pst.setTimestamp(13, null);
            }
            pst.setString(14, regularizeService.getTxtAadharNo());
            pst.setString(15, regularizeService.getSltPostGroup());
            pst.setString(16, regularizeService.getTxtAccNo());
            if (regularizeService.getTxtGP() != null && !regularizeService.getTxtGP().equals("")) {
                double gpdata = Double.parseDouble(regularizeService.getTxtGP());
                pst.setInt(17, (int) gpdata);
            } else {
                pst.setInt(17, 0);
            }
            pst.setString(18, loginempid);
            pst.setTimestamp(19, new Timestamp(new Date().getTime()));
            if (regularizeService.getChkNotSBPrint() != null && regularizeService.getChkNotSBPrint().equals("Y")) {
                pst.setString(20, "N");
            } else {
                pst.setString(20, "Y");
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
    public void updateRegularizationContractualData(RegularizeServiceContractualForm regularizeService, String loginempid) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "UPDATE emp_regularization_contractual SET ord_no=?,ord_date=?,basic=?,pay_commission=?,gpc=?,spc=?,gender=?,mobile=?,acct_type=?,is_assumed=?,dob=?,doj=?,"
                    + "aadhar_no=?,post_group=?,acc_no=?,gp=?,entry_taken_by=?,entry_taken_on=?,if_visible=? WHERE EMP_ID=? AND reg_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, regularizeService.getTxtNotOrdNo());
            if (regularizeService.getTxtNotOrdDt() != null && !regularizeService.getTxtNotOrdDt().equals("")) {
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(regularizeService.getTxtNotOrdDt()).getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            if (regularizeService.getTxtBasic() != null && !regularizeService.getTxtBasic().equals("")) {
                double basicdata = Double.parseDouble(regularizeService.getTxtBasic());
                pst.setInt(3, (int) basicdata);
            } else {
                pst.setInt(3, 0);
            }
            if (regularizeService.getRdPayCommission() != null && !regularizeService.getRdPayCommission().equals("")) {
                pst.setInt(4, Integer.parseInt(regularizeService.getRdPayCommission()));
            } else {
                pst.setInt(4, 0);
            }
            pst.setString(5, regularizeService.getSltGenericPost());
            pst.setString(6, regularizeService.getSltSubstantivePost());
            pst.setString(7, regularizeService.getSltGender());
            pst.setString(8, regularizeService.getTxtMobile());
            pst.setString(9, regularizeService.getSltAcctType());
            pst.setString(10, regularizeService.getChkIfAssumed());
            if (regularizeService.getTxtDOB() != null && !regularizeService.getTxtDOB().equals("")) {
                pst.setTimestamp(11, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(regularizeService.getTxtDOB()).getTime()));
            } else {
                pst.setTimestamp(11, null);
            }
            if (regularizeService.getTxtDOJ() != null && !regularizeService.getTxtDOJ().equals("")) {
                pst.setTimestamp(12, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(regularizeService.getTxtDOJ()).getTime()));
            } else {
                pst.setTimestamp(12, null);
            }
            pst.setString(13, regularizeService.getTxtAadharNo());
            pst.setString(14, regularizeService.getSltPostGroup());
            pst.setString(15, regularizeService.getTxtAccNo());
            if (regularizeService.getTxtGP() != null && !regularizeService.getTxtGP().equals("")) {
                double gpdata = Double.parseDouble(regularizeService.getTxtGP());
                pst.setInt(16, (int) gpdata);
            } else {
                pst.setInt(16, 0);
            }
            pst.setString(17, loginempid);
            pst.setTimestamp(18, new Timestamp(new Date().getTime()));
            if (regularizeService.getChkNotSBPrint() != null && regularizeService.getChkNotSBPrint().equals("Y")) {
                pst.setString(19, "N");
            } else {
                pst.setString(19, "Y");
            }
            pst.setString(20, regularizeService.getEmpid());
            pst.setInt(21, Integer.parseInt(regularizeService.getHregid()));
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateEmployeeData(RegularizeServiceContractualForm regularizeService) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String empprofilestatus = getEmployeeProfileCompletedStatus(regularizeService.getEmpid());

            if (empprofilestatus.equals("N")) {
                String sql = "update emp_mast set cur_basic_salary=?,pay_commission=?,cur_spc=?,gender=?,mobile=?,acct_type=?,gpf_no=?,if_gpf_assumed=?,post_grp_type=?,IS_REGULAR='Y',gp=? where emp_id=?";
                pst = con.prepareStatement(sql);
                if (regularizeService.getTxtBasic() != null && !regularizeService.getTxtBasic().equals("")) {
                    double basicdata = Double.parseDouble(regularizeService.getTxtBasic());
                    pst.setInt(1, (int) basicdata);
                } else {
                    pst.setInt(1, 0);
                }
                if (regularizeService.getRdPayCommission() != null && !regularizeService.getRdPayCommission().equals("")) {
                    pst.setInt(2, Integer.parseInt(regularizeService.getRdPayCommission()));
                } else {
                    pst.setInt(2, 6);
                }
                pst.setString(3, regularizeService.getSltSubstantivePost());
                pst.setString(4, regularizeService.getSltGender());
                pst.setString(5, regularizeService.getTxtMobile());
                pst.setString(6, regularizeService.getSltAcctType());
                pst.setString(7, regularizeService.getTxtAccNo());
                if (regularizeService.getChkIfAssumed() != null && regularizeService.getChkIfAssumed().equals("Y")) {
                    pst.setString(8, regularizeService.getChkIfAssumed());
                } else {
                    pst.setString(8, "N");
                }
                pst.setString(9, regularizeService.getSltPostGroup());
                if (regularizeService.getTxtGP() != null && !regularizeService.getTxtGP().equals("")) {
                    double gpdata = Double.parseDouble(regularizeService.getTxtGP());
                    pst.setInt(10, (int) gpdata);
                } else {
                    pst.setInt(10, 0);
                }
                pst.setString(11, regularizeService.getEmpid());
                pst.executeUpdate();
            } else if (empprofilestatus.equals("Y")) {
                String sql = "update emp_mast set cur_basic_salary=?,pay_commission=?,cur_spc=?,IS_REGULAR='Y',gp=? where emp_id=?";
                pst = con.prepareStatement(sql);
                if (regularizeService.getTxtBasic() != null && !regularizeService.getTxtBasic().equals("")) {
                    double basicdata = Double.parseDouble(regularizeService.getTxtBasic());
                    pst.setInt(1, (int) basicdata);
                } else {
                    pst.setInt(1, 0);
                }
                if (regularizeService.getRdPayCommission() != null && !regularizeService.getRdPayCommission().equals("")) {
                    pst.setInt(2, Integer.parseInt(regularizeService.getRdPayCommission()));
                } else {
                    pst.setInt(2, 6);
                }
                pst.setString(3, regularizeService.getSltSubstantivePost());
                if (regularizeService.getTxtGP() != null && !regularizeService.getTxtGP().equals("")) {
                    double gpdata = Double.parseDouble(regularizeService.getTxtGP());
                    pst.setInt(4, (int) gpdata);
                } else {
                    pst.setInt(4, 0);
                }
                pst.setString(5, regularizeService.getEmpid());
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
    public RegularizeServiceContractualForm getRegularizationServiceContractual6YearsData(RegularizeServiceContractualForm regularizeServiceForm) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "select matrix_level,matrix_cell,substr(cur_spc,14,6)gpc,cur_spc,emp_mast.dob DateOfBirth,doe_gov,emp_regularization_contractual.*,ARRAY_TO_STRING(ARRAY[emp_mast.INITIALS, emp_mast.F_NAME, emp_mast.M_NAME,emp_mast.L_NAME], ' ') EMP_NAME,\n"
                    + "(select id_no from emp_id_doc where emp_id=emp_mast.emp_id and id_description='AADHAAR')aadharno,contractual_type,date_of_regularization\n"
                    + "from emp_regularization_contractual\n"
                    + "inner join emp_mast on emp_regularization_contractual.emp_id=emp_mast.emp_id where emp_regularization_contractual.emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, regularizeServiceForm.getEmpid());
            rs = pst.executeQuery();
            if (rs.next()) {
                regularizeServiceForm.setHregid(rs.getString("reg_id"));

                regularizeServiceForm.setEmpname(rs.getString("EMP_NAME"));

                regularizeServiceForm.setTxtNotOrdNo(rs.getString("ord_no"));
                regularizeServiceForm.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("ord_date")));

                regularizeServiceForm.setTxtBasic(rs.getString("basic"));
                regularizeServiceForm.setRdPayCommission(rs.getString("pay_commission"));
                regularizeServiceForm.setTxtGP(rs.getString("gp"));

                regularizeServiceForm.setSltGender(rs.getString("gender"));
                regularizeServiceForm.setTxtMobile(rs.getString("mobile"));
                regularizeServiceForm.setSltAcctType(rs.getString("acct_type"));
                regularizeServiceForm.setChkIfAssumed(rs.getString("is_assumed"));
                regularizeServiceForm.setTxtAccNo(rs.getString("acc_no"));
                regularizeServiceForm.setTxtAadharNo(rs.getString("aadharno"));
                regularizeServiceForm.setSltPostGroup(rs.getString("post_group"));
                regularizeServiceForm.setGenPost(rs.getString("gpc"));
                regularizeServiceForm.setSubPost(rs.getString("cur_spc"));
                regularizeServiceForm.setHidAadhar(rs.getString("aadharno"));
                regularizeServiceForm.setHidSpc(rs.getString("cur_spc"));
                regularizeServiceForm.setHidGpc(rs.getString("gpc"));
                regularizeServiceForm.setTxtDOB(rs.getString("DateOfBirth"));
                regularizeServiceForm.setTxtDOJ(rs.getString("doe_gov"));
                regularizeServiceForm.setSltContractualType(rs.getString("contractual_type"));
                regularizeServiceForm.setTxtRegularizationDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("date_of_regularization")));
                regularizeServiceForm.setPayLevel(rs.getString("matrix_level"));
                regularizeServiceForm.setPayCell(rs.getString("matrix_cell"));
                regularizeServiceForm.setFrmDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("fdate")));
                regularizeServiceForm.setToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("tdate")));
            } else {
                sql = "select matrix_level,matrix_cell,substr(cur_spc,14,6)gpc,emp_mast.*,ARRAY_TO_STRING(ARRAY[emp_mast.INITIALS, emp_mast.F_NAME, emp_mast.M_NAME,emp_mast.L_NAME], ' ') EMP_NAME,"
                        + "(select id_no from emp_id_doc where emp_id=emp_mast.emp_id and id_description='AADHAAR')aadharno from emp_mast where emp_id=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, regularizeServiceForm.getEmpid());
                rs = pst.executeQuery();
                if (rs.next()) {
                    regularizeServiceForm.setEmpname(rs.getString("EMP_NAME"));
                    regularizeServiceForm.setTxtBasic(rs.getString("cur_basic_salary"));
                    regularizeServiceForm.setRdPayCommission(rs.getString("pay_commission"));
                    regularizeServiceForm.setTxtGP(rs.getString("gp"));
                    regularizeServiceForm.setSltGender(rs.getString("gender"));
                    regularizeServiceForm.setTxtMobile(rs.getString("mobile"));
                    regularizeServiceForm.setSltAcctType("PRAN");
                    regularizeServiceForm.setTxtAccNo(rs.getString("gpf_no"));
                    regularizeServiceForm.setTxtAadharNo(rs.getString("aadharno"));
                    regularizeServiceForm.setSltPostGroup(rs.getString("post_grp_type"));
                    regularizeServiceForm.setSubPost(rs.getString("cur_spc"));
                    regularizeServiceForm.setGenPost(rs.getString("gpc"));
                    regularizeServiceForm.setHidAadhar(rs.getString("aadharno"));
                    regularizeServiceForm.setHidSpc(rs.getString("cur_spc"));
                    regularizeServiceForm.setHidGpc(rs.getString("gpc"));
                    regularizeServiceForm.setTxtDOB(rs.getString("dob"));
                    regularizeServiceForm.setTxtDOJ(rs.getString("doe_gov"));
                    regularizeServiceForm.setPayLevel(rs.getString("matrix_level"));
                    regularizeServiceForm.setPayCell(rs.getString("matrix_cell"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return regularizeServiceForm;
    }

    @Override
    public void insertRegularizationContractual6YearsData(RegularizeServiceContractualForm regularizeService, String loginempid) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "INSERT INTO emp_regularization_contractual(emp_id,ord_no,ord_date,basic,pay_commission,gender,mobile,acct_type,is_assumed,aadhar_no,post_group,acc_no,gp,"
                    + "entry_taken_by,entry_taken_on,gpc,spc,fdate,tdate,dob,doj,if_visible,contractual_type,date_of_regularization) "
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, regularizeService.getEmpid());
            pst.setString(2, regularizeService.getTxtNotOrdNo());
            if (regularizeService.getTxtNotOrdDt() != null && !regularizeService.getTxtNotOrdDt().equals("")) {
                pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(regularizeService.getTxtNotOrdDt()).getTime()));
            } else {
                pst.setTimestamp(3, null);
            }
            if (regularizeService.getTxtBasic() != null && !regularizeService.getTxtBasic().equals("")) {
                double basicdata = Double.parseDouble(regularizeService.getTxtBasic());
                pst.setInt(4, (int) basicdata);
            } else {
                pst.setInt(4, 0);
            }
            if (regularizeService.getRdPayCommission() != null && regularizeService.getRdPayCommission().equals("6")) {
                pst.setInt(5, Integer.parseInt(regularizeService.getRdPayCommission()));
                double gpdata = Double.parseDouble(regularizeService.getTxtGP());
                pst.setInt(13, (int) gpdata);
            } else {
                pst.setInt(5, 7);
                pst.setInt(13, 0);
            }
            pst.setString(6, regularizeService.getSltGender());
            pst.setString(7, regularizeService.getTxtMobile());
            pst.setString(8, regularizeService.getSltAcctType());
            pst.setString(9, regularizeService.getChkIfAssumed());
            pst.setString(10, regularizeService.getHidAadhar());
            pst.setString(11, regularizeService.getSltPostGroup());
            pst.setString(12, regularizeService.getTxtAccNo());

            pst.setString(14, loginempid);
            pst.setTimestamp(15, new Timestamp(new Date().getTime()));
            pst.setString(16, regularizeService.getHidGpc());
            pst.setString(17, regularizeService.getHidSpc());
            if (regularizeService.getFrmDate() != null && !regularizeService.getFrmDate().equals("")) {
                pst.setTimestamp(18, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(regularizeService.getFrmDate()).getTime()));
            } else {
                pst.setTimestamp(18, null);
            }
            if (regularizeService.getToDate() != null && !regularizeService.getToDate().equals("")) {
                pst.setTimestamp(19, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(regularizeService.getToDate()).getTime()));
            } else {
                pst.setTimestamp(19, null);
            }
            if (regularizeService.getTxtDOB() != null && !regularizeService.getTxtDOB().equals("")) {
                pst.setTimestamp(20, new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(regularizeService.getTxtDOB()).getTime()));
            } else {
                pst.setTimestamp(20, null);
            }
            if (regularizeService.getTxtDOJ() != null && !regularizeService.getTxtDOJ().equals("")) {
                pst.setTimestamp(21, new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(regularizeService.getTxtDOJ()).getTime()));
            } else {
                pst.setTimestamp(21, null);
            }
            if (regularizeService.getChkNotSBPrint() != null && regularizeService.getChkNotSBPrint().equals("Y")) {
                pst.setString(22, "N");
            } else {
                pst.setString(22, "Y");
            }
            pst.setString(23, regularizeService.getSltContractualType());
            if (regularizeService.getTxtRegularizationDate() != null && !regularizeService.getTxtRegularizationDate().equals("")) {
                pst.setTimestamp(24, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(regularizeService.getTxtRegularizationDate()).getTime()));
            } else {
                pst.setTimestamp(24, null);
            }
            pst.executeUpdate();

            String sbLang = sbDAO.getRegularisationServiceContractual6Yrs(regularizeService.getEmpid());
            //notificationDao.saveServiceBookLanguage(sbLang, notid, sbLang)
            pst = con.prepareStatement("UPDATE emp_regularization_contractual SET SB_DESCRIPTION=? WHERE EMP_ID=?");
            pst.setString(1, sbLang);
            pst.setString(2, regularizeService.getEmpid());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateRegularizationContractual6YearsData(RegularizeServiceContractualForm regularizeService, String loginempid) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "UPDATE emp_regularization_contractual SET ord_no=?,ord_date=?,basic=?,pay_commission=?,gender=?,mobile=?,acct_type=?,is_assumed=?,aadhar_no=?,post_group=?,acc_no=?,gp=?,"
                    + "entry_taken_by=?,entry_taken_on=?,fdate=?,tdate=?,gpc=?,spc=?,dob=?,doj=?,if_visible=?,contractual_type=?,date_of_regularization=? WHERE EMP_ID=? AND reg_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, regularizeService.getTxtNotOrdNo());
            if (regularizeService.getTxtNotOrdDt() != null && !regularizeService.getTxtNotOrdDt().equals("")) {
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(regularizeService.getTxtNotOrdDt()).getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            if (regularizeService.getTxtBasic() != null && !regularizeService.getTxtBasic().equals("")) {
                double basicdata = Double.parseDouble(regularizeService.getTxtBasic());
                pst.setInt(3, (int) basicdata);
            } else {
                pst.setInt(3, 0);
            }
            if (regularizeService.getRdPayCommission() != null && regularizeService.getRdPayCommission().equals("6")) {
                pst.setInt(4, Integer.parseInt(regularizeService.getRdPayCommission()));
                double gpdata = Double.parseDouble(regularizeService.getTxtGP());
                pst.setInt(12, (int) gpdata);
            } else {
                pst.setInt(4, 7);
                pst.setInt(12, 0);
            }
            pst.setString(5, regularizeService.getSltGender());
            pst.setString(6, regularizeService.getTxtMobile());
            pst.setString(7, regularizeService.getSltAcctType());
            pst.setString(8, regularizeService.getChkIfAssumed());
            pst.setString(9, regularizeService.getHidAadhar());
            pst.setString(10, regularizeService.getSltPostGroup());
            pst.setString(11, regularizeService.getTxtAccNo());

            pst.setString(13, loginempid);
            pst.setTimestamp(14, new Timestamp(new Date().getTime()));
            if (regularizeService.getFrmDate() != null && !regularizeService.getFrmDate().equals("")) {
                pst.setTimestamp(15, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(regularizeService.getFrmDate()).getTime()));
            } else {
                pst.setTimestamp(15, null);
            }
            if (regularizeService.getToDate() != null && !regularizeService.getToDate().equals("")) {
                pst.setTimestamp(16, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(regularizeService.getToDate()).getTime()));
            } else {
                pst.setTimestamp(16, null);
            }

            pst.setString(17, regularizeService.getHidGpc());
            pst.setString(18, regularizeService.getHidSpc());

            if (regularizeService.getTxtDOB() != null && !regularizeService.getTxtDOB().equals("")) {
                pst.setTimestamp(19, new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(regularizeService.getTxtDOB()).getTime()));
            } else {
                pst.setTimestamp(19, null);
            }
            if (regularizeService.getTxtDOJ() != null && !regularizeService.getTxtDOJ().equals("")) {
                pst.setTimestamp(20, new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(regularizeService.getTxtDOJ()).getTime()));
            } else {
                pst.setTimestamp(20, null);
            }
            if (regularizeService.getChkNotSBPrint() != null && regularizeService.getChkNotSBPrint().equals("Y")) {
                pst.setString(21, "N");
            } else {
                pst.setString(21, "Y");
            }
            pst.setString(22, regularizeService.getSltContractualType());
            if (regularizeService.getTxtRegularizationDate() != null && !regularizeService.getTxtRegularizationDate().equals("")) {
                pst.setTimestamp(23, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(regularizeService.getTxtRegularizationDate()).getTime()));
            } else {
                pst.setTimestamp(23, null);
            }
            pst.setString(24, regularizeService.getEmpid());
            pst.setInt(25, Integer.parseInt(regularizeService.getHregid()));
            pst.executeUpdate();

            String sbLang = sbDAO.getRegularisationServiceContractual6Yrs(regularizeService.getEmpid());
            pst = con.prepareStatement("UPDATE emp_regularization_contractual SET SB_DESCRIPTION=? WHERE EMP_ID=?");
            pst.setString(1, sbLang);
            pst.setString(2, regularizeService.getEmpid());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    /*@Override
     public void updateEmployeeDataFor6Years(RegularizeServiceContractualForm regularizeService) {

     Connection con = null;

     PreparedStatement pst = null;

     try {
     con = this.dataSource.getConnection();

     String empprofilestatus = getEmployeeProfileCompletedStatus(regularizeService.getEmpid());

     if (empprofilestatus.equals("N")) {
     String sql = "update emp_mast set cur_basic_salary=?,pay_commission=?,gender=?,mobile=?,acct_type=?,gpf_no=?,if_gpf_assumed=?,post_grp_type=?,IS_REGULAR='Y',gp=?,matrix_level=?,matrix_cell=?,cur_salary=? where emp_id=?";
     pst = con.prepareStatement(sql);
     if (regularizeService.getTxtBasic() != null && !regularizeService.getTxtBasic().equals("")) {
     double basicdata = Double.parseDouble(regularizeService.getTxtBasic());
     pst.setInt(1, (int) basicdata);
     } else {
     pst.setInt(1, 0);
     }
     pst.setString(3, regularizeService.getSltGender());
     pst.setString(4, regularizeService.getTxtMobile());
     pst.setString(5, regularizeService.getSltAcctType());
     pst.setString(6, regularizeService.getTxtAccNo());
     if (regularizeService.getChkIfAssumed() != null && regularizeService.getChkIfAssumed().equals("Y")) {
     pst.setString(7, regularizeService.getChkIfAssumed());
     } else {
     pst.setString(7, "N");
     }
     pst.setString(8, regularizeService.getSltPostGroup());

     if (regularizeService.getRdPayCommission() != null && regularizeService.getRdPayCommission().equals("6")) {
     pst.setString(12, regularizeService.getSltPayScale());
     pst.setInt(10, 0);
     pst.setInt(11, 0);
     double gpdata = Double.parseDouble(regularizeService.getTxtGP());
     pst.setInt(9, (int) gpdata);
     pst.setInt(2, Integer.parseInt(regularizeService.getRdPayCommission()));

     } else if (regularizeService.getRdPayCommission() != null && regularizeService.getRdPayCommission().equals("7")) {
     pst.setInt(10, Integer.parseInt(regularizeService.getPayLevel()));
     pst.setInt(11, Integer.parseInt(regularizeService.getPayCell()));
     pst.setString(12, null);
     pst.setInt(9, 0);
     pst.setInt(2, Integer.parseInt(regularizeService.getRdPayCommission()));

     }
     pst.setString(13, regularizeService.getEmpid());
     pst.executeUpdate();
     } else if (empprofilestatus.equals("Y")) {
     String sql = "update emp_mast set cur_basic_salary=?,pay_commission=?,IS_REGULAR='Y',gp=?,matrix_level=?,matrix_cell=?,cur_salary=? where emp_id=?";
     pst = con.prepareStatement(sql);
     if (regularizeService.getTxtBasic() != null && !regularizeService.getTxtBasic().equals("")) {
     double basicdata = Double.parseDouble(regularizeService.getTxtBasic());
     pst.setInt(1, (int) basicdata);
     } else {
     pst.setInt(1, 0);
     }

     if (regularizeService.getRdPayCommission() != null && regularizeService.getRdPayCommission().equals("6")) {
     pst.setString(6, regularizeService.getSltPayScale());
     pst.setInt(4, 0);
     pst.setInt(5, 0);
     double gpdata = Double.parseDouble(regularizeService.getTxtGP());
     pst.setInt(3, (int) gpdata);
     pst.setInt(2, Integer.parseInt(regularizeService.getRdPayCommission()));

     } else if (regularizeService.getRdPayCommission() != null && regularizeService.getRdPayCommission().equals("7")) {
     pst.setInt(4, Integer.parseInt(regularizeService.getPayLevel()));
     pst.setInt(5, Integer.parseInt(regularizeService.getPayCell()));
     pst.setString(6, null);
     pst.setInt(3, 0);
     pst.setInt(2, Integer.parseInt(regularizeService.getRdPayCommission()));

     }

     pst.setString(7, regularizeService.getEmpid());

     pst.executeUpdate();
     }
     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     DataBaseFunctions.closeSqlObjects(pst);
     DataBaseFunctions.closeSqlObjects(con);
     }
     }*/
    @Override
    public void updateEmployeeDataFor6Years(RegularizeServiceContractualForm regularizeService) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String empprofilestatus = getEmployeeProfileCompletedStatus(regularizeService.getEmpid());

            if (empprofilestatus.equals("N")) {
                String sql = "update emp_mast set IS_REGULAR='Y' where emp_id=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, regularizeService.getEmpid());
                pst.executeUpdate();
            } else if (empprofilestatus.equals("Y")) {
                String sql = "update emp_mast set IS_REGULAR='Y' where emp_id=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, regularizeService.getEmpid());
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    private String getEmployeeProfileCompletedStatus(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String profilestatus = "N";
        try {
            con = this.dataSource.getConnection();

            String sql = "select if_profile_completed from emp_mast where emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("if_profile_completed") != null && rs.getString("if_profile_completed").equals("Y")) {
                    profilestatus = "Y";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return profilestatus;
    }

    @Override
    public boolean checkEntryForContractual6YearsToRegular(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean enteredstatus = false;
        String isregular = "N";
        try {
            con = this.dataSource.getConnection();

            String sql = "select is_regular from emp_mast where emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                isregular = rs.getString("is_regular");
                if (rs.getString("is_regular") != null && rs.getString("is_regular").equals("Y")) {
                    enteredstatus = true;
                }
            }

            sql = "select count(*) cnt from emp_regularization_contractual where emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    //if(isregular != null && isregular.equals("Y")){
                    enteredstatus = true;
                    //}else{
                    //enteredstatus = false;
                    //}
                } else if (isregular != null && isregular.equals("C")) {
                    enteredstatus = true;
                } else {
                    enteredstatus = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return enteredstatus;
    }
}

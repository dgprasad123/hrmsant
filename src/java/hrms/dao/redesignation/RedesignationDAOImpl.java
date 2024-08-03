package hrms.dao.redesignation;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.emppayrecord.EmpPayRecordDAO;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.transfer.MaxTransferIdDAOImpl;
import hrms.model.emppayrecord.EmpPayRecordForm;
import hrms.model.redesignation.Redesignation;
import hrms.model.redesignation.RedesignationList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.builder.ToStringBuilder;

public class RedesignationDAOImpl implements RedesignationDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    protected MaxTransferIdDAOImpl maxTransferIdDao;

    protected EmpPayRecordDAO emppayrecordDAO;

    protected ServiceBookLanguageDAO sbDAO;

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setEmppayrecordDAO(EmpPayRecordDAO emppayrecordDAO) {
        this.emppayrecordDAO = emppayrecordDAO;
    }

    public void setMaxTransferIdDao(MaxTransferIdDAOImpl maxTransferIdDao) {
        this.maxTransferIdDao = maxTransferIdDao;
    }

    public void insertRedesignationData(Redesignation redesig, int notid) {

        Connection con = null;

        PreparedStatement pst = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        try {
            con = this.dataSource.getConnection();

            String sql = "INSERT INTO EMP_TRANSFER (NOT_ID, NOT_TYPE, EMP_ID,DEPT_CODE,OFF_CODE, NEXT_SPC,IF_DEPLOYED,FIELD_OFF_CODE) VALUES(?,?,?,?,?,?,?,?)";
            //pst = con.prepareStatement(sql);
            pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //  pst.setString(1, maxTransferIdDao.getMaxTransferId()); //CommonFunctions.getMaxCode("EMP_TRANSFER", "TR_ID", con));
            pst.setInt(1, notid);
            pst.setString(2, "REDESIGNATION");
            pst.setString(3, redesig.getEmpid());
            pst.setString(4, redesig.getHidPostedDeptCode());
            pst.setString(5, redesig.getHidPostedOffCode());
            pst.setString(6, redesig.getPostedspc());
            pst.setString(7, "");
            pst.setString(8, redesig.getSltPostedFieldOff());
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            int trid = rs.getInt("TR_ID");

            String sqlString = ToStringBuilder.reflectionToString(pst);
            pst = con.prepareStatement("UPDATE EMP_TRANSFER SET query_string=? WHERE TR_ID=? AND EMP_ID=?");
            pst.setString(1, sqlString);
            pst.setInt(2, trid);
            pst.setString(3, redesig.getEmpid());
            pst.executeUpdate();

            if (redesig.getRdTransaction() != null && redesig.getRdTransaction().equals("C")) {
                sql = "UPDATE EMP_MAST SET NEXT_OFFICE_CODE=? WHERE EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, redesig.getHidPostedOffCode());
                pst.setString(2, redesig.getEmpid());
                pst.executeUpdate();
            }
            epayrecordform.setEmpid(redesig.getEmpid());
            epayrecordform.setNot_id(notid);
            epayrecordform.setNot_type("REDESIGNATION");
            epayrecordform.setPayscale(redesig.getSltPayScale());
            epayrecordform.setBasic(redesig.getTxtBasic());
            epayrecordform.setGp(redesig.getTxtGP());
            epayrecordform.setS_pay(redesig.getTxtSP());
            epayrecordform.setP_pay(redesig.getTxtPP());
            epayrecordform.setOth_pay(redesig.getTxtOP());
            epayrecordform.setOth_desc(redesig.getTxtDescOP());
            epayrecordform.setWefDt(redesig.getTxtWEFDt());
            epayrecordform.setWefTime(redesig.getSltWEFTime());
            emppayrecordDAO.saveEmpPayRecordData(epayrecordform);

            /*
             * Updating the Service Book Language
             */
            if (redesig.getChkNotSBPrint() != null && redesig.getChkNotSBPrint().equals("Y")) {
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET IF_VISIBLE='N' WHERE NOT_ID=?");
                pst.setInt(1, notid);
                pst.execute();
            } else if (redesig.getChkNotSBPrint() == null || redesig.getChkNotSBPrint().equals("")) {
                String sbLang = sbDAO.getRedesignationDetails(redesig, notid, redesig.getNotType());
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION =?,IF_VISIBLE='Y' WHERE NOT_ID=? ");
                pst.setString(1, sbLang);
                pst.setInt(2, notid);
                pst.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void updateRedesignationData(Redesignation redesig) {

        Connection con = null;

        PreparedStatement pst = null;

        // int notid = 0;
        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();

        try {
            con = dataSource.getConnection();

            String sql = "UPDATE EMP_TRANSFER SET DEPT_CODE=?,OFF_CODE=?, NEXT_SPC=?,IF_DEPLOYED=?,FIELD_OFF_CODE=? WHERE EMP_ID=? AND TR_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, redesig.getHidPostedDeptCode());
            pst.setString(2, redesig.getHidPostedOffCode());
            pst.setString(3, redesig.getPostedspc());
            pst.setString(4, "");
            pst.setString(5, redesig.getSltPostedFieldOff());
            pst.setString(6, redesig.getEmpid());
            pst.setInt(7, Integer.parseInt(redesig.getHtrid()));
            pst.executeUpdate();

            if (redesig.getRdTransaction() != null && redesig.getRdTransaction().equals("C")) {
                sql = "UPDATE EMP_MAST SET NEXT_OFFICE_CODE=? WHERE EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, redesig.getHidPostedOffCode());
                pst.setString(2, redesig.getEmpid());
                pst.executeUpdate();
            }

            epayrecordform.setPayid(redesig.getHpayid());
            epayrecordform.setEmpid(redesig.getEmpid());
            epayrecordform.setPayscale(redesig.getSltPayScale());
            epayrecordform.setBasic(redesig.getTxtBasic());
            epayrecordform.setGp(redesig.getTxtGP());
            epayrecordform.setS_pay(redesig.getTxtSP());
            epayrecordform.setP_pay(redesig.getTxtPP());
            epayrecordform.setOth_pay(redesig.getTxtOP());
            epayrecordform.setOth_desc(redesig.getTxtDescOP());
            epayrecordform.setWefDt(redesig.getTxtWEFDt());
            epayrecordform.setWefTime(redesig.getSltWEFTime());
            emppayrecordDAO.updateEmpPayRecordData(epayrecordform);

            /*
             * Updating the Service Book Language
             */
            if (redesig.getChkNotSBPrint() != null && redesig.getChkNotSBPrint().equals("Y")) {
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET IF_VISIBLE='N' WHERE NOT_ID=?");
                pst.setInt(1, redesig.getHnotid());
                pst.execute();
            } else if (redesig.getChkNotSBPrint() == null || redesig.getChkNotSBPrint().equals("")) {
                String sbLang = sbDAO.getRedesignationDetails(redesig, redesig.getHnotid(), redesig.getNotType());
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION =?,IF_VISIBLE='Y' WHERE NOT_ID=? ");
                pst.setString(1, sbLang);
                pst.setInt(2, redesig.getHnotid());
                pst.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void deleteRedesignation(String notId) {

        Connection con = null;

        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();

            if (notId != null && !notId.equals("")) {
                pst = con.prepareStatement("DELETE FROM EMP_NOTIFICATION WHERE NOT_ID=?");
                pst.setInt(1, Integer.parseInt(notId));
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("DELETE FROM EMP_TRANSFER WHERE NOT_ID=?");
                pst.setInt(1, Integer.parseInt(notId));
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("DELETE FROM EMP_PAY_RECORD WHERE NOT_ID=?");
                pst.setInt(1, Integer.parseInt(notId));
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("DELETE FROM EMP_RELIEVE WHERE NOT_ID=?");
                pst.setInt(1, Integer.parseInt(notId));
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("DELETE FROM EMP_JOIN WHERE NOT_ID=?");
                pst.setInt(1, Integer.parseInt(notId));
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public Redesignation editRedesignation(String empId, int notId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        Redesignation redesig = new Redesignation();
        try {
            con = this.dataSource.getConnection();

            String sql = "select"
                    + " emp_notification.not_id,ordno,orddt,emp_notification.dept_code notDeptCode,emp_notification.off_code notOffCode,"
                    + " auth,note,tr_id,emp_transfer.dept_code transDeptCode,emp_transfer.off_code transOffCode,next_spc,field_off_code,pay_id,wef,weft,emp_pay_record.pay_scale,"
                    + " pay,s_pay,p_pay,oth_pay,emp_pay_record.gp,oth_desc,spn from"
                    + " (SELECT not_id,tr_id,dept_code,off_code,next_spc,field_off_code FROM EMP_TRANSFER WHERE EMP_ID=? AND NOT_ID=?)emp_transfer"
                    + " inner join (select not_id,ordno,orddt,dept_code,off_code,auth,note from emp_notification where EMP_ID=? AND NOT_ID=?)emp_notification"
                    + " on emp_transfer.not_id=emp_notification.not_id"
                    + " left outer join (select pay_id,not_id,wef,weft,pay_scale,pay,s_pay,p_pay,oth_pay,gp,oth_desc from emp_pay_record where EMP_ID=? AND NOT_ID=?)emp_pay_record"
                    + " on emp_notification.not_id=emp_pay_record.not_id"
                    + " left outer join g_spc on emp_transfer.next_spc=g_spc.spc";
            pst = con.prepareStatement(sql);
            pst.setString(1, empId);
            pst.setInt(2, notId);
            pst.setString(3, empId);
            pst.setInt(4, notId);
            pst.setString(5, empId);
            pst.setInt(6, notId);
            rs = pst.executeQuery();
            if (rs.next()) {
                System.out.println("Inside Redesignation Edit");
                redesig.setEmpid(empId);
                redesig.setHnotid(rs.getInt("not_id"));
                redesig.setHpayid(rs.getInt("pay_id"));

                redesig.setTxtNotOrdNo(rs.getString("ordno"));
                redesig.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));

                redesig.setAuthPostName(CommonFunctions.getSPN(con, rs.getString("auth")));
                redesig.setHidAuthDeptCode(rs.getString("notDeptCode"));
                redesig.setHidAuthOffCode(rs.getString("notOffCode"));
                redesig.setAuthSpc(rs.getString("auth"));

                redesig.setHtrid(rs.getString("tr_id"));
                redesig.setPostedPostName(CommonFunctions.getSPN(con, rs.getString("next_spc")));
                redesig.setHidPostedDeptCode(rs.getString("transDeptCode"));
                redesig.setHidPostedOffCode(rs.getString("transOffCode"));
                redesig.setPostedspc(rs.getString("next_spc"));
                redesig.setSltPostedFieldOff(rs.getString("field_off_code"));

                redesig.setHpayid(rs.getInt("pay_id"));
                redesig.setSltPayScale(rs.getString("pay_scale"));
                redesig.setTxtGP(rs.getString("gp"));
                redesig.setTxtBasic(rs.getString("pay"));
                redesig.setTxtSP(rs.getString("s_pay"));
                redesig.setTxtPP(rs.getString("p_pay"));
                redesig.setTxtOP(rs.getString("oth_pay"));
                redesig.setTxtDescOP(rs.getString("oth_desc"));
                redesig.setTxtWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("wef")));
                redesig.setSltWEFTime(rs.getString("weft"));
                redesig.setNote(rs.getString("note"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return redesig;
    }

    public List findAllRedesignation(String empId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List list = new ArrayList();

        RedesignationList redepList = null;
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("SELECT NOT_ID,NOT_TYPE,DOE,ORDNO,ORDDT FROM EMP_NOTIFICATION WHERE EMP_ID=? AND NOT_TYPE='REDESIGNATION'");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            while (rs.next()) {
                redepList = new RedesignationList();
                redepList.setNotId(rs.getString("NOT_ID"));
                redepList.setNotType(rs.getString("NOT_TYPE"));
                redepList.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOE")));
                redepList.setOrdno(rs.getString("ORDNO"));
                redepList.setOrdDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                list.add(redepList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public Redesignation findRetirementStatus(String gpfno) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String empid = "";
        Redesignation redsg = new Redesignation();
        try {
            con = this.dataSource.getConnection();

            String sql = "select emp_mast.emp_id,gpf_no,ret_type, cur_off_code from emp_mast\n"
                    + "inner join emp_ret_res on emp_mast.emp_id=emp_ret_res.emp_id where emp_mast.gpf_no=? \n"
                    + "and ((dep_code='09' and if_retired='Y') or dep_code='08' or dep_code='15')";
            pst = con.prepareStatement(sql);
            pst.setString(1, gpfno);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("emp_id") != null && !rs.getString("emp_id").equals("")) {
                    redsg.setEmpid(rs.getString("emp_id"));
                    redsg.setRetType(rs.getString("ret_type"));
                    redsg.setHidPostedOffCode(rs.getString("cur_off_code"));
                    redsg.setGpfno(rs.getString("gpf_no"));
                }
            } else {
                redsg.setRetType("NA");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return redsg;
    }

    @Override
    public int saveRedesignation(Redesignation redesig, String loginoffcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        String depcode = null;
        String isregular = null;

        int retVal = 0;
        try {
            con = this.dataSource.getConnection();

            if (redesig.getSltRecruitmentType() != null && !redesig.getSltRecruitmentType().equals("")) {
                if (redesig.getSltRecruitmentType().equals("Y") && !redesig.getHtrid().equals("RG") && !redesig.getHtrid().equals("RP")) {
                    String sql = "update emp_mast set cur_off_code=?,cur_spc=?,dep_code='02',is_regular='Y',if_reengaged='Y' where emp_id=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, loginoffcode);
                    pst.setString(2, redesig.getPostedspc());
                    pst.setString(3, redesig.getEmpid());
                    retVal = pst.executeUpdate();
                    depcode = "02";
                    isregular = "Y";
                } else if (redesig.getSltRecruitmentType().equals("Y") && redesig.getHtrid().equals("RG")) {
                    String sql = "update emp_mast set cur_off_code=?,cur_spc=null,dep_code='08',is_regular='Y',if_reengaged='Y' where emp_id=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, loginoffcode);
                    //pst.setString(2, redesig.getPostedspc());
                    pst.setString(2, redesig.getEmpid());
                    retVal = pst.executeUpdate();
                    depcode = "08";
                    isregular = "Y";
                } else if (redesig.getSltRecruitmentType().equals("N") && !redesig.getHtrid().equals("RG") && !redesig.getHtrid().equals("RP")) {
                    String sql = "update emp_mast set cur_off_code=?,cur_spc=null,post_nomenclature=?,dep_code='02',is_regular='N',if_reengaged='Y',if_gpf_assumed='Y' where emp_id=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, loginoffcode);
                    pst.setString(2, redesig.getPostnomenclature().toUpperCase());
                    pst.setString(3, redesig.getEmpid());
                    retVal = pst.executeUpdate();
                    depcode = "02";
                    isregular = "N";
                } else if (redesig.getSltRecruitmentType().equals("N") && redesig.getHtrid().equals("RG")) {
                    String sql = "update emp_mast set cur_off_code=?,cur_spc=null,post_nomenclature=?,dep_code='08',is_regular='N',if_reengaged='Y',if_gpf_assumed='Y' where emp_id=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, loginoffcode);
                    pst.setString(2, redesig.getPostnomenclature().toUpperCase());
                    pst.setString(3, redesig.getEmpid());
                    retVal = pst.executeUpdate();
                    depcode = "08";
                    isregular = "N";
                } else if (redesig.getSltRecruitmentType().equals("N") && redesig.getHtrid().equals("RP")) {
                    String sql = "update emp_mast set cur_off_code=?,cur_spc=null,post_nomenclature=?,dep_code='02',is_regular='N',if_gpf_assumed='Y' where emp_id=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, loginoffcode);
                    pst.setString(2, redesig.getPostnomenclature().toUpperCase());
                    pst.setString(3, redesig.getEmpid());
                    retVal = pst.executeUpdate();
                    depcode = "02";
                    isregular = "N";

                } else if (redesig.getSltRecruitmentType().equals("Y") && redesig.getHtrid().equals("RP")) {
                    String sql = "update emp_mast set cur_off_code=?,cur_spc=null,dep_code='03',is_regular='Y',if_gpf_assumed='Y' where emp_id=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, loginoffcode);
                    pst.setString(2, redesig.getEmpid());
                    retVal = pst.executeUpdate();
                    depcode = "03";
                    isregular = "Y";
                }
                if (retVal > 0) {
                    String sql1 = "insert into emp_reengaged(empid,"
                            + "gpf_no,prev_office_code,curr_off_code,cur_spc,post_nomenclature,dep_code,doe,is_regular,reengaged_type)values(?,?,?,?,?,?,?,CURRENT_TIMESTAMP,?,?)";
                    pst = con.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
                    pst.setString(1, redesig.getEmpid());
                    pst.setString(2, redesig.getGpfno());
                    pst.setString(3, redesig.getHidPostedOffCode());
                    pst.setString(4, loginoffcode);
                    pst.setString(5, redesig.getPostedspc());
                    if (redesig.getPostedspc() != null && !redesig.getPostedspc().equals("")) {
                        pst.setString(5, redesig.getPostedspc());
                    } else {
                        pst.setString(5, null);
                    }

                    if (redesig.getPostnomenclature() != null && !redesig.getPostnomenclature().equals("")) {
                        pst.setString(6, redesig.getPostnomenclature().toUpperCase());
                    } else {
                        pst.setString(6, null);
                    }

                    pst.setString(7, depcode);
                    pst.setString(8, isregular);
                    pst.setString(9, redesig.getHtrid());
                    pst.execute();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return retVal;
    }
}

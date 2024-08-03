package hrms.dao.promotion;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.emppayrecord.EmpPayRecordDAO;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.master.CadreDAO;
import hrms.model.cadre.Cadre;
import hrms.model.emppayrecord.EmpPayRecordForm;
import hrms.model.notification.NotificationBean;
import hrms.model.promotion.PromotionBean;
import hrms.model.promotion.PromotionForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

public class PromotionDAOImpl implements PromotionDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    protected MaxPromotionIdDAOImpl maxPromotionIdDAO;

    protected EmpPayRecordDAO emppayrecordDAO;

    protected CadreDAO cadreDAO;

    protected ServiceBookLanguageDAO sbDAO;

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setMaxPromotionIdDAO(MaxPromotionIdDAOImpl maxPromotionIdDAO) {
        this.maxPromotionIdDAO = maxPromotionIdDAO;
    }

    public void setEmppayrecordDAO(EmpPayRecordDAO emppayrecordDAO) {
        this.emppayrecordDAO = emppayrecordDAO;
    }

    public void setCadreDAO(CadreDAO cadreDAO) {
        this.cadreDAO = cadreDAO;
    }

    @Override
    public List getPromotionList(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List promotionList = new ArrayList();
        PromotionBean pBean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select emp_notification.is_validated,emp_promotion.modified_by,emp_promotion.modified_on,emp_notification.if_visible,pro_id,emp_notification.doe,emp_notification.not_id,emp_promotion.not_type,ordno,orddt,off_en,relieve_id from"
                    + " (select is_validated,if_visible,emp_id,doe,not_id,not_type,ordno,orddt from emp_notification where emp_id=? and not_type='PROMOTION')emp_notification"
                    + " left outer join emp_promotion on emp_notification.not_id=emp_promotion.not_id"
                    + " left outer join g_office on emp_promotion.off_code=g_office.off_code"
                    + " left outer join emp_relieve on emp_notification.not_id=emp_relieve.not_id"
                    + " order by emp_notification.orddt desc, emp_notification.doe desc ";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                pBean = new PromotionBean();
                pBean.setPromotionId(rs.getString("pro_id"));
                pBean.setHnotid(rs.getString("not_id"));
                pBean.setNotType(rs.getString("not_type"));
                pBean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOE")));
                pBean.setOrdno(rs.getString("ordno"));
                pBean.setOrdt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                pBean.setHrlvid(rs.getString("relieve_id"));
                pBean.setPrintInServiceBook(rs.getString("if_visible"));
                pBean.setModifiedby(StringUtils.defaultString(rs.getString("modified_by")) + "<br />" + StringUtils.defaultString(CommonFunctions.getFormattedOutputDate1(rs.getDate("modified_on"))));
                pBean.setIsValidated(rs.getString("is_validated"));
                promotionList.add(pBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return promotionList;
    }

    @Override
    public List getAllotDescList(String notType) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List allotDescList = new ArrayList();

        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from g_allot_desc where not_type=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, notType);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("allot_desc"));
                so.setLabel(rs.getString("allot_desc"));
                allotDescList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return allotDescList;
    }

    @Override
    public void savePromotion(PromotionForm promotionForm, int notId, String loginempid, String loginuserid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        String gradename = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        Cadre cadreForm = new Cadre();
        try {
            con = this.dataSource.getConnection();
            if (promotionForm.getSltGrade() != null && !promotionForm.getSltGrade().equals("")) {
                pst = con.prepareStatement("select GRADE from G_CADRE_GRADE WHERE CADRE_GRADE_CODE=?");
                pst.setInt(1, Integer.parseInt(promotionForm.getSltGrade()));
                rs = pst.executeQuery();
                if (rs.next()) {
                    promotionForm.setGradeName(rs.getString("GRADE"));
                    System.out.println("gradnm:" + promotionForm.getGradeName());
                }
            }

            pst = con.prepareStatement("INSERT INTO EMP_PROMOTION (NOT_ID, NOT_TYPE, EMP_ID, ALLOT_DESC, IF_RETROSPECTIVE,DEPT_CODE,OFF_CODE, NEXT_SPC,FIELD_OFF_CODE,"
                    + "entry_taken_by,entry_taken_on,post_grp_promotion,posting_organization_type) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            //String promoId = maxPromotionIdDAO.getMaxPromotionId();
            pst.setInt(1, notId);
            pst.setString(2, "PROMOTION");
            pst.setString(3, promotionForm.getEmpid());
            pst.setString(4, promotionForm.getSltAllotmentDesc());
            pst.setString(5, promotionForm.getChkRetroPromotion());

            pst.setString(9, promotionForm.getSltPostedFieldOff());
            pst.setString(10, loginempid);
            pst.setTimestamp(11, new Timestamp(new Date().getTime()));
            pst.setString(12, promotionForm.getSltPostGroup());
            if (promotionForm.getRadpostingauthtype() != null && promotionForm.getRadpostingauthtype().equals("GOI")) {
                System.out.println("goi");
                pst.setString(6, null);
                pst.setString(7, null);
                pst.setString(8, promotionForm.getHidPostingOthSpc());
                pst.setString(13, "GOI");
            } else if (promotionForm.getRadpostingauthtype() != null && promotionForm.getRadpostingauthtype().equals("GOO")) {
                System.out.println("gooo" + promotionForm.getHidPostedDeptCode() + promotionForm.getHidPostedOffCode() + promotionForm.getPostedspc());
                pst.setString(6, promotionForm.getHidPostedDeptCode());
                pst.setString(7, promotionForm.getHidPostedOffCode());
                pst.setString(8, promotionForm.getPostedspc());
                pst.setString(13, "GOO");
            }
            pst.executeUpdate();

            rs = pst.getGeneratedKeys();
            rs.next();
            int promotionid = rs.getInt("pro_id");

            String sqlString = ToStringBuilder.reflectionToString(pst);
            pst = con.prepareStatement("UPDATE EMP_PROMOTION SET modified_by=?,modified_on=?,query_string=? WHERE pro_id=? AND EMP_ID=?");
            pst.setString(1, loginuserid);
            pst.setTimestamp(2, new Timestamp(new Date().getTime()));
            pst.setString(3, sqlString);
            pst.setInt(4, promotionid);
            pst.setString(5, promotionForm.getEmpid());
            pst.executeUpdate();

            if (promotionForm.getRdTransaction() != null && promotionForm.getRdTransaction().equals("C")) {
                System.out.println("current");
                pst = con.prepareStatement("UPDATE EMP_MAST SET NEXT_OFFICE_CODE=?,cur_basic_salary=?,cur_salary=?,gp=?, post_grp_type=?,pay_commission=?,matrix_level=?,matrix_cell=?,"
                        + "CUR_CADRE_GRADE=?,CUR_CADRE_GRADE_CODE=?,CUR_CADRE_CODE=? WHERE EMP_ID=?");
                pst.setString(1, promotionForm.getHidPostedOffCode());

                if (promotionForm.getTxtBasic() != null && !promotionForm.getTxtBasic().equals("")) {
                    pst.setDouble(2, Double.parseDouble(promotionForm.getTxtBasic()));
                } else {
                    pst.setDouble(2, 0);
                }

                pst.setString(5, promotionForm.getSltPostGroup());
                if (promotionForm.getRdoPaycomm() != null && !promotionForm.getRdoPaycomm().equals("")) {
                    pst.setInt(6, Integer.parseInt(promotionForm.getRdoPaycomm()));
                    if (Integer.parseInt(promotionForm.getRdoPaycomm()) == 6) {
                        if (promotionForm.getSltPayScale() != null && !promotionForm.getSltPayScale().equals("")) {
                            pst.setString(3, promotionForm.getSltPayScale());
                        } else {
                            pst.setString(3, null);
                        }
                        if (promotionForm.getTxtGP() != null && !promotionForm.getTxtGP().equals("")) {
                            pst.setDouble(4, Double.parseDouble(promotionForm.getTxtGP()));
                        } else {
                            pst.setDouble(4, 0);
                        }
                        pst.setString(7, null);
                        pst.setString(8, null);

                    } else if (Integer.parseInt(promotionForm.getRdoPaycomm()) == 7) {
                        pst.setString(3, null);
                        pst.setDouble(4, 0);
                        if (promotionForm.getPayLevel() != null && !promotionForm.getPayLevel().equals("")) {
                            pst.setString(7, promotionForm.getPayLevel());
                        }
                        if (promotionForm.getPayCell() != null && !promotionForm.getPayCell().equals("")) {
                            pst.setString(8, promotionForm.getPayCell());
                        }

                    }
                } else {
                    pst.setInt(6, 0);
                }
                if (promotionForm.getSltGrade() != null && !promotionForm.getSltGrade().equals("")) {
                    System.out.println("selectgrade-->" + gradename + ";;" + promotionForm.getSltGrade());
                    pst.setString(9, promotionForm.getGradeName());
                    pst.setInt(10, Integer.parseInt(promotionForm.getSltGrade()));
                } else {
                    pst.setString(9, null);
                    pst.setInt(10, 0);
                }
                pst.setString(11, promotionForm.getSltCadre());

                pst.setString(12, promotionForm.getEmpid());
                pst.executeUpdate();

                sqlString = ToStringBuilder.reflectionToString(pst);
                sbDAO.createTransactionLog(loginuserid, promotionForm.getEmpid(), "PROMOTION", sqlString);
            }

            epayrecordform.setEmpid(promotionForm.getEmpid());
            epayrecordform.setNot_id(notId);
            epayrecordform.setNot_type("PROMOTION");
            epayrecordform.setPayscale(promotionForm.getSltPayScale());
            epayrecordform.setBasic(promotionForm.getTxtBasic());
            epayrecordform.setGp(promotionForm.getTxtGP());
            epayrecordform.setS_pay(promotionForm.getTxtSP());
            epayrecordform.setP_pay(promotionForm.getTxtPP());
            epayrecordform.setOth_pay(promotionForm.getTxtOP());
            epayrecordform.setOth_desc(promotionForm.getTxtDescOP());
            epayrecordform.setWefDt(promotionForm.getTxtWEFDt());
            epayrecordform.setWefTime(promotionForm.getSltWEFTime());
            epayrecordform.setPayLevel(promotionForm.getPayLevel());
            epayrecordform.setPayCell(promotionForm.getPayCell());
            emppayrecordDAO.saveEmpPayRecordData(epayrecordform);

            cadreForm.setEmpId(promotionForm.getEmpid());
            cadreForm.setNotId(notId);
            cadreForm.setNotType("PROMOTION");
            cadreForm.setCadreDept(promotionForm.getSltCadreDept());
            cadreForm.setCadreName(promotionForm.getSltCadre());
            //Set Cadre GradeCode and GradeName
            cadreForm.setGrade(promotionForm.getSltGrade());
            cadreForm.setGradeCodeName(promotionForm.getGradeName());
            cadreForm.setCadreLvl(promotionForm.getSltCadreLevel());
            cadreForm.setDescription(promotionForm.getSltDescription());
            cadreForm.setAllotmentYear(promotionForm.getTxtAllotmentYear());
            cadreForm.setCadreId(promotionForm.getTxtCadreId());
            cadreForm.setPostingDept(promotionForm.getSltPostingDept());
            cadreForm.setPostCode(promotionForm.getSltGenericPost());
            cadreForm.setPostClassification(promotionForm.getRdPostClassification());
            cadreForm.setPostStatus(promotionForm.getRdPostStatus());
            cadreForm.setCadreJoiningWEFDt(promotionForm.getTxtCadreJoiningWEFDt());
            cadreForm.setCadreJoiningWEFTime(promotionForm.getSltCadreJoiningWEFTime());
            cadreForm.setJoinedAsSuch(promotionForm.getChkJoinedAsSuch());
            cadreForm.setIfProforma(promotionForm.getChkProformaPromotion());
            cadreDAO.saveCadreData(cadreForm);

            if (promotionForm.getChkNotSBPrint() != null && promotionForm.getChkNotSBPrint().equals("Y")) {
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET IF_VISIBLE='N' WHERE NOT_ID=?");
                pst.setInt(1, notId);
                pst.execute();
            } else if (promotionForm.getChkNotSBPrint() == null || promotionForm.getChkNotSBPrint().equals("")) {
                String sbLanguage = sbDAO.getPromotionDetails(promotionForm, notId, "PROMOTION");
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION =?,IF_VISIBLE='Y' WHERE NOT_ID=?");
                pst.setString(1, sbLanguage);
                pst.setInt(2, notId);
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
    public void updatePromotion(PromotionForm promotionForm, String loginempid, String loginuserid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        String gradeNm = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        Cadre cadreForm = new Cadre();
        try {
            con = this.dataSource.getConnection();
            if (promotionForm.getSltGrade() != null && !promotionForm.getSltGrade().equals("")) {
                pst = con.prepareStatement("select GRADE from G_CADRE_GRADE WHERE CADRE_GRADE_CODE=?");
                pst.setInt(1, Integer.parseInt(promotionForm.getSltGrade()));
                rs = pst.executeQuery();
                if (rs.next()) {
                    promotionForm.setGradeName(rs.getString("GRADE"));
                }
            }

            pst = con.prepareStatement("UPDATE EMP_PROMOTION SET ALLOT_DESC=?, IF_RETROSPECTIVE=? ,DEPT_CODE=? ,OFF_CODE=?, NEXT_SPC=? ,FIELD_OFF_CODE=?,entry_taken_by=?,entry_taken_on=?,post_grp_promotion=?,posting_organization_type=? WHERE PRO_ID=?");
            pst.setString(1, promotionForm.getSltAllotmentDesc());
            pst.setString(2, promotionForm.getChkRetroPromotion());

            pst.setString(6, promotionForm.getSltPostedFieldOff());
            pst.setString(7, loginempid);
            pst.setTimestamp(8, new Timestamp(new Date().getTime()));
            pst.setString(9, promotionForm.getSltPostGroup());
            if (promotionForm.getRadpostingauthtype() != null && promotionForm.getRadpostingauthtype().equals("GOI")) {
                pst.setString(3, null);
                pst.setString(4, null);
                pst.setString(5, promotionForm.getHidPostingOthSpc());
                pst.setString(10, "GOI");
            } else if (promotionForm.getRadpostingauthtype() != null && promotionForm.getRadpostingauthtype().equals("GOO")) {
                pst.setString(3, promotionForm.getHidPostedDeptCode());
                pst.setString(4, promotionForm.getHidPostedOffCode());
                pst.setString(5, promotionForm.getPostedspc());
                pst.setString(10, "GOO");
            }
            pst.setInt(11, Integer.parseInt(promotionForm.getPromotionId()));
            pst.executeUpdate();

            String sqlString = ToStringBuilder.reflectionToString(pst);
            pst = con.prepareStatement("UPDATE EMP_PROMOTION SET modified_by=?,modified_on=?,query_string=? WHERE pro_id=? AND EMP_ID=?");
            pst.setString(1, loginuserid);
            pst.setTimestamp(2, new Timestamp(new Date().getTime()));
            pst.setString(3, sqlString);
            pst.setInt(4, Integer.parseInt(promotionForm.getPromotionId()));
            pst.setString(5, promotionForm.getEmpid());
            pst.executeUpdate();

            if (promotionForm.getRdTransaction() != null && promotionForm.getRdTransaction().equals("C")) {
                pst = con.prepareStatement("UPDATE EMP_MAST SET NEXT_OFFICE_CODE=?,cur_basic_salary=?,cur_salary=?,gp=?, post_grp_type=?,pay_commission=?,matrix_level=?,matrix_cell=?, "
                        + "CUR_CADRE_GRADE=?,CUR_CADRE_GRADE_CODE=?,CUR_CADRE_CODE=? WHERE EMP_ID=?");
                pst.setString(1, promotionForm.getHidPostedOffCode());
                /*if (promotionForm.getSltPayScale() != null && !promotionForm.getSltPayScale().equals("")) {
                 pst.setString(2, promotionForm.getSltPayScale());
                 } else {
                 pst.setString(2, null);
                 }
                 if (promotionForm.getTxtGP() != null && !promotionForm.getTxtGP().equals("")) {
                 pst.setDouble(4, Double.parseDouble(promotionForm.getTxtGP()));
                 } else {
                 pst.setDouble(4, 0);
                 }*/
                if (promotionForm.getTxtBasic() != null && !promotionForm.getTxtBasic().equals("")) {
                    pst.setDouble(2, Double.parseDouble(promotionForm.getTxtBasic()));
                } else {
                    pst.setDouble(2, 0);
                }

                pst.setString(5, promotionForm.getSltPostGroup());
                if (promotionForm.getRdoPaycomm() != null && !promotionForm.getRdoPaycomm().equals("")) {
                    pst.setInt(6, Integer.parseInt(promotionForm.getRdoPaycomm()));
                    if (Integer.parseInt(promotionForm.getRdoPaycomm()) == 6) {
                        if (promotionForm.getSltPayScale() != null && !promotionForm.getSltPayScale().equals("")) {
                            pst.setString(3, promotionForm.getSltPayScale());
                        } else {
                            pst.setString(3, null);
                        }
                        if (promotionForm.getTxtGP() != null && !promotionForm.getTxtGP().equals("")) {
                            pst.setDouble(4, Double.parseDouble(promotionForm.getTxtGP()));
                        } else {
                            pst.setDouble(4, 0);
                        }
                        pst.setString(7, null);
                        pst.setString(8, null);

                    } else if (Integer.parseInt(promotionForm.getRdoPaycomm()) == 7) {
                        pst.setString(3, null);
                        pst.setDouble(4, 0);
                        if (promotionForm.getPayLevel() != null && !promotionForm.getPayLevel().equals("")) {
                            pst.setString(7, promotionForm.getPayLevel());
                        }
                        if (promotionForm.getPayCell() != null && !promotionForm.getPayCell().equals("")) {
                            pst.setString(8, promotionForm.getPayCell());
                        }

                    }
                } else {
                    pst.setInt(6, 0);
                }
                System.out.println("editGrade-->" + promotionForm.getSltGrade());
                if (promotionForm.getSltGrade() != null && !promotionForm.getSltGrade().equals("")) {
                    pst.setString(9, promotionForm.getGradeName());
                    pst.setInt(10, Integer.parseInt(promotionForm.getSltGrade()));
                } else {
                    pst.setString(9, null);
                    pst.setInt(10, 0);
                }
                pst.setString(11, promotionForm.getSltCadre());

                pst.setString(12, promotionForm.getEmpid());
                pst.executeUpdate();

                sqlString = ToStringBuilder.reflectionToString(pst);
                sbDAO.createTransactionLog(loginuserid, promotionForm.getEmpid(), "PROMOTION", sqlString);
            }

            epayrecordform.setPayid(promotionForm.getHpayid());
            epayrecordform.setEmpid(promotionForm.getEmpid());
            //epayrecordform.setPayscale(promotionForm.getSltPayScale());
            epayrecordform.setBasic(promotionForm.getTxtBasic());
            epayrecordform.setGp(promotionForm.getTxtGP());
            epayrecordform.setS_pay(promotionForm.getTxtSP());
            epayrecordform.setP_pay(promotionForm.getTxtPP());
            epayrecordform.setOth_pay(promotionForm.getTxtOP());
            epayrecordform.setOth_desc(promotionForm.getTxtDescOP());
            epayrecordform.setWefDt(promotionForm.getTxtWEFDt());
            epayrecordform.setWefTime(promotionForm.getSltWEFTime());
            //epayrecordform.setPayLevel(promotionForm.getPayLevel());
            //epayrecordform.setPayCell(promotionForm.getPayCell());
            if (promotionForm.getRdoPaycomm() != null && promotionForm.getRdoPaycomm().equals("6")) {
                epayrecordform.setPayscale(promotionForm.getSltPayScale());
                epayrecordform.setGp(promotionForm.getTxtGP());
                epayrecordform.setPayLevel(null);
                epayrecordform.setPayCell(null);

            } else if (promotionForm.getRdoPaycomm() != null && promotionForm.getRdoPaycomm().equals("7")) {
                epayrecordform.setPayscale(null);
                epayrecordform.setGp("0");
                epayrecordform.setPayLevel(promotionForm.getPayLevel());
                epayrecordform.setPayCell(promotionForm.getPayCell());
            }
            if(epayrecordform.getPayid() == 0){
                epayrecordform.setNot_id(promotionForm.getHnotid());
                epayrecordform.setNot_type("PROMOTION");
                emppayrecordDAO.saveEmpPayRecordData(epayrecordform);
            }else{
                emppayrecordDAO.updateEmpPayRecordData(epayrecordform);
            }

            cadreForm.setEmpId(promotionForm.getEmpid());
            cadreForm.setNotId(promotionForm.getHnotid());
            cadreForm.setNotType("PROMOTION");
            cadreForm.setCadreCode(promotionForm.gethCadId());
            cadreForm.setEmpId(promotionForm.getEmpid());
            cadreForm.setCadreDept(promotionForm.getSltCadreDept());
            cadreForm.setCadreName(promotionForm.getSltCadre());
            //Set Cadre GradeCode and GradeName
            cadreForm.setGrade(promotionForm.getSltGrade());
            cadreForm.setGradeCodeName(promotionForm.getGradeName());
            cadreForm.setCadreLvl(promotionForm.getSltCadreLevel());
            cadreForm.setDescription(promotionForm.getSltDescription());
            cadreForm.setAllotmentYear(promotionForm.getTxtAllotmentYear());
            cadreForm.setCadreId(promotionForm.getTxtCadreId());
            cadreForm.setPostingDept(promotionForm.getSltPostingDept());
            cadreForm.setPostCode(promotionForm.getSltGenericPost());
            cadreForm.setPostClassification(promotionForm.getRdPostClassification());
            cadreForm.setPostStatus(promotionForm.getRdPostStatus());
            cadreForm.setCadreJoiningWEFDt(promotionForm.getTxtCadreJoiningWEFDt());
            cadreForm.setCadreJoiningWEFTime(promotionForm.getSltCadreJoiningWEFTime());
            cadreForm.setIfJoined(promotionForm.getChkJoinedAsSuch());
            cadreForm.setIfProforma(promotionForm.getChkProformaPromotion());
            if (promotionForm.gethCadId() != null && !promotionForm.gethCadId().equals("")) {
                cadreDAO.updateCadreData(cadreForm);
            } else {
                cadreDAO.saveCadreData(cadreForm);
            }

            if (promotionForm.getChkNotSBPrint() != null && promotionForm.getChkNotSBPrint().equals("Y")) {
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET IF_VISIBLE='N' WHERE NOT_ID=?");
                pst.setInt(1, promotionForm.getHnotid());
                pst.execute();
            } else if (promotionForm.getChkNotSBPrint() == null || promotionForm.getChkNotSBPrint().equals("")) {
                String sbLanguage = sbDAO.getPromotionDetails(promotionForm, promotionForm.getHnotid(), "PROMOTION");
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION =?,IF_VISIBLE='Y' WHERE NOT_ID=?");
                pst.setString(1, sbLanguage);
                pst.setInt(2, promotionForm.getHnotid());
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
    public PromotionForm getEmpPromotionData(PromotionForm prform, int notId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        ResultSet rs1 = null;

        try {
            con = dataSource.getConnection();

            String sql = "select post_grp_promotion,organization_type,entry_type,emp_notification.if_visible,cad_id,joined_assuch,if_proforma,post_class,post_stat,emp_cadre.dept_code cadreDept,\n"
                    + "emp_cadre.cadre_code,grade,cur_cadre_grade_code,emp_cadre.lvl,description,emp_cadre.posting_dept_code,emp_cadre.post_code,allot_desc,if_retrospective,\n"
                    + "emp_notification.not_id,ordno,orddt,emp_notification.dept_code notDeptCode, \n"
                    + "emp_notification.off_code notOffCode, auth,note,pro_id,emp_promotion.dept_code proDeptCode,emp_promotion.off_code proOffCode,next_spc,field_off_code, \n"
                    + "emp_cadre.wefd cadreWEFDate,emp_cadre.weft cadreWEFTime,pay_id, \n"
                    + "emp_pay_record.wef payrecordWEFDate,emp_pay_record.weft payrecordWEFTime,emp_pay_record.pay_scale,pay,s_pay,p_pay,oth_pay,emp_pay_record.gp, \n"
                    + "oth_desc,office1.dist_code notdistcode,office2.dist_code postingdistcode,spc1.gpc notgpc,spc2.gpc postinggpc,spc1.spn authSpn,spc2.spn postingSpn,\n"
                    + "gothspc.off_en goiPostingSpn,gothspc.oth_spc,posting_organization_type,pay_level,pay_cell from \n"
                    + "(SELECT not_id,pro_id,dept_code,off_code,next_spc,field_off_code,allot_desc,if_retrospective,post_grp_promotion,posting_organization_type FROM EMP_PROMOTION \n"
                    + " WHERE EMP_ID=? AND NOT_ID=?)emp_promotion \n"
                    + "inner join (select organization_type,not_id,ordno,orddt,dept_code,off_code,auth,note,if_visible,entry_type from emp_notification where \n"
                    + "EMP_ID=? AND NOT_ID=?)emp_notification \n"
                    + "on emp_promotion.not_id=emp_notification.not_id \n"
                    + "left outer join (select pay_id,not_id,wef,weft,pay_scale,pay,s_pay,p_pay,oth_pay,gp,oth_desc,pay_level,pay_cell from emp_pay_record \n"
                    + "where EMP_ID=? AND NOT_ID=?)emp_pay_record \n"
                    + "on emp_notification.not_id=emp_pay_record.not_id \n"
                    + "left outer join emp_cadre on emp_notification.not_id=emp_cadre.not_id \n"
                    + "left outer join g_office office1 on emp_notification.off_code=office1.off_code \n"
                    + "left outer join g_office office2 on emp_promotion.off_code=office2.off_code \n"
                    + "left outer join g_spc spc1 on emp_notification.auth=spc1.spc \n"
                    + "left outer join g_spc spc2 on emp_promotion.next_spc=spc2.spc\n"
                    + "left outer join (select off_en,cast(other_spc_id as text)oth_spc from g_oth_spc where is_active='Y')gothspc  \n"
                    + "on emp_promotion.next_spc=gothspc.oth_spc";
            pst = con.prepareStatement(sql);
            pst.setString(1, prform.getEmpid());
            pst.setInt(2, notId);
            pst.setString(3, prform.getEmpid());
            pst.setInt(4, notId);
            pst.setString(5, prform.getEmpid());
            pst.setInt(6, notId);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    prform.setChkNotSBPrint("Y");
                } else {
                    prform.setChkNotSBPrint("N");
                }
                String postgrp = rs.getString("post_grp_promotion");
                if (postgrp == null || postgrp.equals("")) {
                    postgrp = getEmployeePostGroup(prform.getEmpid());
                }
                prform.setSltPostGroup(postgrp);
                prform.setPromotionId(rs.getString("pro_id"));
                prform.setTxtNotOrdNo(rs.getString("ordno"));
                prform.setHnotid(rs.getInt("not_id"));
                prform.setHnotidSBCorrection(rs.getString("not_id"));
                //System.out.println("hidnotid:"+rs.getInt("not_id"));
                prform.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                prform.setRadnotifyingauthtype(rs.getString("organization_type"));
                prform.setHidNotifyingDeptCode(rs.getString("notDeptCode"));
                prform.setHidNotifyingDistCode(rs.getString("notdistcode"));
                prform.setHidNotifyingOffCode(rs.getString("notOffCode"));
                prform.setHidNotifyingGPC(rs.getString("notgpc"));
                prform.setHidTempNotifyingOffCode(rs.getString("notOffCode"));
                if (prform.getRadnotifyingauthtype() != null && prform.getRadnotifyingauthtype().equals("GOI")) {
                    prform.setNotifyingPostName(getOtherSpn(rs.getString("auth")));
                    prform.setHidNotifyingOthSpc(rs.getString("auth"));
                } else {
                    prform.setHidTempNotifyingPost(rs.getString("auth"));
                    prform.setNotifyingPostName(rs.getString("authSpn"));
                    prform.setNotifyingSpc(rs.getString("auth"));
                }

                //prform.setNotifyingPostName(CommonFunctions.getSPN(con, rs.getString("auth")));
                prform.setSltAllotmentDesc(rs.getString("allot_desc"));
                prform.setChkRetroPromotion(rs.getString("if_retrospective"));

                prform.sethCadId(rs.getString("cad_id"));
                prform.setSltCadreDept(rs.getString("cadreDept"));
                prform.setSltCadre(rs.getString("cadre_code"));
                prform.setHidTempCadre(rs.getString("cadre_code"));
                prform.setSltGrade(rs.getString("grade"));
                prform.setHidTempGrade(rs.getString("grade"));
                prform.setSltCadreLevel(rs.getString("lvl"));
                prform.setSltDescription(rs.getString("description"));
                prform.setTxtCadreJoiningWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("cadreWEFDate")));
                prform.setSltCadreJoiningWEFTime(rs.getString("cadreWEFTime"));

                prform.setSltPostingDept(rs.getString("posting_dept_code"));
                prform.setSltGenericPost(rs.getString("post_code"));
                prform.setHidTempGenericPost(rs.getString("post_code"));

                prform.setRdPostClassification(rs.getString("post_class"));
                prform.setRdPostStatus(rs.getString("post_stat"));

                prform.setChkJoinedAsSuch(rs.getString("joined_assuch"));
                prform.setChkProformaPromotion(rs.getString("if_proforma"));

                prform.setHidPostedDeptCode(rs.getString("proDeptCode"));
                if ((rs.getString("proDeptCode") != null && !rs.getString("proDeptCode").equals(""))
                        || (rs.getString("posting_organization_type") != null && rs.getString("posting_organization_type").equals("GOO"))) {
                    prform.setRadpostingauthtype("GOO");
                    prform.setPostedPostName(rs.getString("postingSpn"));
                } else if (rs.getString("posting_organization_type") != null && rs.getString("posting_organization_type").equals("GOI")) {
                    prform.setRadpostingauthtype("GOI");
                    prform.setPostedPostName(rs.getString("goiPostingSpn"));
                    prform.setHidPostingOthSpc(rs.getString("oth_spc"));
                }
                prform.setHidPostedDistrict(rs.getString("postingdistcode"));
                prform.setHidPostedOffCode(rs.getString("proOffCode"));
                prform.setHidTempPostedOffCode(rs.getString("proOffCode"));
                prform.setHidPostedGPC(rs.getString("postinggpc"));
                prform.setPostedspc(rs.getString("next_spc"));
                prform.setHidTempPostedPost(rs.getString("next_spc"));
                //prform.setPostedPostName(rs.getString("postingSpn"));
                prform.setSltPostedFieldOff(rs.getString("field_off_code"));
                prform.setHidTempPostedFieldOffCode(rs.getString("field_off_code"));

                prform.setHpayid(rs.getInt("pay_id"));
                prform.setHpayidSBCorrection(rs.getString("pay_id"));
                //prform.setSltPayScale(rs.getString("pay_scale"));
                //prform.setTxtGP(rs.getString("gp"));
                prform.setTxtBasic(rs.getString("pay"));
                prform.setTxtSP(rs.getString("s_pay"));
                prform.setTxtPP(rs.getString("p_pay"));
                prform.setTxtOP(rs.getString("oth_pay"));
                prform.setTxtDescOP(rs.getString("oth_desc"));
                prform.setTxtWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("payrecordWEFDate")));
                prform.setSltWEFTime(rs.getString("payrecordWEFTime"));
                prform.setNote(rs.getString("note"));
                if (rs.getString("pay_level") != null && !rs.getString("pay_level").equals("")) {
                    prform.setRdoPaycomm("7");
                    prform.setPayLevel(rs.getString("pay_level"));
                    prform.setPayCell(rs.getString("pay_cell"));
                } else if (rs.getString("pay_scale") != null && !rs.getString("pay_scale").equals("")) {
                    prform.setRdoPaycomm("6");
                    prform.setSltPayScale(rs.getString("pay_scale"));
                    prform.setTxtGP(rs.getString("gp"));
                }
                if (rs.getString("cur_cadre_grade_code") != null && !rs.getString("cur_cadre_grade_code").equals("")) {
                    prform.setSltGrade(rs.getString("cur_cadre_grade_code"));
                    prform.setGradeCode(rs.getString("cur_cadre_grade_code"));
                }
                if (rs.getString("grade") != null && !rs.getString("grade").equals("")) {
                    prform.setGradeName(rs.getString("grade"));
                }
                prform.setEntryType(rs.getString("entry_type"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return prform;
    }

    @Override
    public String getEmployeePostGroup(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String postgrp = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select emp_id,post_grp_type from emp_mast where emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                postgrp = rs.getString("post_grp_type");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return postgrp;
    }

    @Override
    public void deletePromotion(PromotionForm promotionForm) {
        Connection con = null;

        PreparedStatement pst = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();

        try {
            con = dataSource.getConnection();

            String sql = "DELETE FROM EMP_PROMOTION WHERE EMP_ID=? AND PRO_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, promotionForm.getEmpid());
            pst.setInt(2, Integer.parseInt(promotionForm.getPromotionId()));
            pst.executeUpdate();

            epayrecordform.setEmpid(promotionForm.getEmpid());
            epayrecordform.setPayid(promotionForm.getHpayid());
            emppayrecordDAO.deleteEmpPayRecordData(epayrecordform);

            sql = "update emp_mast set NEXT_OFFICE_CODE=null where emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, promotionForm.getEmpid());
            pst.executeUpdate();

            //Delete from Emp Join
            sql = "DELETE FROM emp_join WHERE not_id = '" + promotionForm.getHnotid() + "' AND not_type = '" + promotionForm.getHnotType() + "'";
            pst = con.prepareStatement(sql);
            pst.executeUpdate();

            //Delete from Emp Relieve
            sql = "DELETE FROM emp_relieve WHERE not_id = '" + promotionForm.getHnotid() + "' AND not_type = '" + promotionForm.getHnotType() + "'";
            pst = con.prepareStatement(sql);
            pst.executeUpdate();

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
    public PromotionForm getEmpCurrentData(String empid) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        PromotionForm prform = null;

        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("SELECT cur_salary,cur_basic_salary,gp, post_grp_type,pay_commission,matrix_level,matrix_cell FROM EMP_MAST WHERE EMP_ID=?");
            pst.setString(1, empid);
            rs1 = pst.executeQuery();
            if (rs1.next()) {
                prform = new PromotionForm();
                if (rs1.getInt("pay_commission") == 6) {
                    if (rs1.getString("cur_salary") != null && !rs1.getString("cur_salary").equals("")) {
                        prform.setSltPayScale(rs1.getString("cur_salary"));
                    }
                    prform.setTxtGP(rs1.getString("gp"));
                } else if (rs1.getInt("pay_commission") == 7) {
                    if (rs1.getString("matrix_level") != null && !rs1.getString("matrix_level").equals("0")) {
                        prform.setPayLevel(rs1.getString("matrix_level"));
                    }
                    if (rs1.getInt("matrix_cell") != 0) {
                        prform.setPayCell(Integer.toString(rs1.getInt("matrix_cell")));
                    }
                }
                prform.setRdoPaycomm(rs1.getString("pay_commission"));
                prform.setTxtBasic(rs1.getString("cur_basic_salary"));
                prform.setSltPostGroup(rs1.getString(("post_grp_type")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return prform;

    }

    @Override
    public String savePromotionDataSBCorrection(PromotionForm promotionForm, int notId, int refcorrectionid, String loginempid, String loginuserid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        Cadre cadreForm = new Cadre();

        String sbLanguage = "";

        try {
            con = this.dataSource.getConnection();
            if (promotionForm.getSltGrade() != null && !promotionForm.getSltGrade().equals("")) {
                pst = con.prepareStatement("select GRADE from G_CADRE_GRADE WHERE CADRE_GRADE_CODE=?");
                pst.setInt(1, Integer.parseInt(promotionForm.getSltGrade()));
                rs = pst.executeQuery();
                if (rs.next()) {
                    promotionForm.setGradeName(rs.getString("GRADE"));
                }
            }

            pst = con.prepareStatement("DELETE FROM EMP_PROMOTION_LOG WHERE EMP_ID=? AND NOT_ID=? AND NOT_TYPE=?");
            pst.setString(1, promotionForm.getEmpid());
            pst.setInt(2, notId);
            pst.setString(3, "PROMOTION");
            pst.executeUpdate();

            pst = con.prepareStatement("INSERT INTO EMP_PROMOTION_LOG (NOT_ID, NOT_TYPE, EMP_ID, ALLOT_DESC, IF_RETROSPECTIVE,DEPT_CODE,OFF_CODE, NEXT_SPC,FIELD_OFF_CODE,"
                    + "entry_taken_by,entry_taken_on,post_grp_promotion,posting_organization_type,ref_correction_id,pro_id) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, notId);
            pst.setString(2, "PROMOTION");
            pst.setString(3, promotionForm.getEmpid());
            pst.setString(4, promotionForm.getSltAllotmentDesc());
            pst.setString(5, promotionForm.getChkRetroPromotion());
            pst.setString(9, promotionForm.getSltPostedFieldOff());
            pst.setString(10, loginempid);
            pst.setTimestamp(11, new Timestamp(new Date().getTime()));
            pst.setString(12, promotionForm.getSltPostGroup());
            if (promotionForm.getRadpostingauthtype() != null && promotionForm.getRadpostingauthtype().equals("GOI")) {
                pst.setString(6, null);
                pst.setString(7, null);
                pst.setString(8, promotionForm.getHidPostingOthSpc());
                pst.setString(13, "GOI");
            } else if (promotionForm.getRadpostingauthtype() != null && promotionForm.getRadpostingauthtype().equals("GOO")) {
                pst.setString(6, promotionForm.getHidPostedDeptCode());
                pst.setString(7, promotionForm.getHidPostedOffCode());
                pst.setString(8, promotionForm.getPostedspc());
                pst.setString(13, "GOO");
            }
            pst.setInt(14, refcorrectionid);
            pst.setInt(15, Integer.parseInt(promotionForm.getPromotionId()));
            pst.executeUpdate();

            rs = pst.getGeneratedKeys();
            rs.next();
            int logid = rs.getInt("log_id");

            String sqlString = ToStringBuilder.reflectionToString(pst);
            pst = con.prepareStatement("UPDATE EMP_PROMOTION_LOG SET modified_by=?,modified_on=?,query_string=? WHERE log_id=? AND EMP_ID=?");
            pst.setString(1, loginuserid);
            pst.setTimestamp(2, new Timestamp(new Date().getTime()));
            pst.setString(3, sqlString);
            pst.setInt(4, logid);
            pst.setString(5, promotionForm.getEmpid());
            pst.executeUpdate();

            epayrecordform.setEmpid(promotionForm.getEmpid());
            epayrecordform.setNot_id(notId);
            epayrecordform.setNot_type("PROMOTION");
            epayrecordform.setPayscale(promotionForm.getSltPayScale());
            epayrecordform.setBasic(promotionForm.getTxtBasic());
            epayrecordform.setGp(promotionForm.getTxtGP());
            epayrecordform.setS_pay(promotionForm.getTxtSP());
            epayrecordform.setP_pay(promotionForm.getTxtPP());
            epayrecordform.setOth_pay(promotionForm.getTxtOP());
            epayrecordform.setOth_desc(promotionForm.getTxtDescOP());
            epayrecordform.setWefDt(promotionForm.getTxtWEFDt());
            epayrecordform.setWefTime(promotionForm.getSltWEFTime());
            epayrecordform.setPayLevel(promotionForm.getPayLevel());
            epayrecordform.setPayCell(promotionForm.getPayCell());
            epayrecordform.setRefcorrectionid(refcorrectionid);
            emppayrecordDAO.deleteEmpPayRecordDataSBCorrection(epayrecordform);
            emppayrecordDAO.saveEmpPayRecordDataSBCorrection(epayrecordform);

            cadreForm.setEmpId(promotionForm.getEmpid());
            cadreForm.setNotId(notId);
            cadreForm.setNotType("PROMOTION");
            cadreForm.setCadreDept(promotionForm.getSltCadreDept());
            cadreForm.setCadreName(promotionForm.getSltCadre());
            //Set Cadre GradeCode and GradeName
            cadreForm.setGrade(promotionForm.getSltGrade());
            cadreForm.setGradeCodeName(promotionForm.getGradeName());
            cadreForm.setCadreLvl(promotionForm.getSltCadreLevel());
            cadreForm.setDescription(promotionForm.getSltDescription());
            cadreForm.setAllotmentYear(promotionForm.getTxtAllotmentYear());
            cadreForm.setCadreId(promotionForm.getTxtCadreId());
            cadreForm.setPostingDept(promotionForm.getSltPostingDept());
            cadreForm.setPostCode(promotionForm.getSltGenericPost());
            cadreForm.setPostClassification(promotionForm.getRdPostClassification());
            cadreForm.setPostStatus(promotionForm.getRdPostStatus());
            cadreForm.setCadreJoiningWEFDt(promotionForm.getTxtCadreJoiningWEFDt());
            cadreForm.setCadreJoiningWEFTime(promotionForm.getSltCadreJoiningWEFTime());
            cadreForm.setJoinedAsSuch(promotionForm.getChkJoinedAsSuch());
            cadreForm.setIfProforma(promotionForm.getChkProformaPromotion());
            cadreForm.setRefcorrectionid(refcorrectionid + "");
            cadreDAO.deleteCadreDataSBCorrection(cadreForm);
            cadreDAO.saveCadreDataSBCorrection(cadreForm);

            if (promotionForm.getChkNotSBPrint() != null && promotionForm.getChkNotSBPrint().equals("Y")) {
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION_LOG SET IF_VISIBLE='N' WHERE NOT_ID=?");
                pst.setInt(1, notId);
                pst.execute();
            } else if (promotionForm.getChkNotSBPrint() == null || promotionForm.getChkNotSBPrint().equals("")) {
                sbLanguage = sbDAO.getPromotionDetails(promotionForm, notId, "PROMOTION");
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION_LOG SET SB_DESCRIPTION =?,IF_VISIBLE='Y' WHERE NOT_ID=?");
                pst.setString(1, sbLanguage);
                pst.setInt(2, notId);
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return sbLanguage;
    }

    @Override
    public PromotionForm editPromotionSBCorrectionDDO(PromotionForm prform, int notId, int correctionid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();

            String sql = "select post_grp_promotion,organization_type,entry_type,emp_notification.if_visible,cad_id,joined_assuch,if_proforma,post_class,post_stat,emp_cadre.dept_code cadreDept,\n"
                    + "emp_cadre.cadre_code,grade,cur_cadre_grade_code,emp_cadre.lvl,description,emp_cadre.posting_dept_code,emp_cadre.post_code,allot_desc,if_retrospective,\n"
                    + "emp_notification.not_id,ordno,orddt,emp_notification.dept_code notDeptCode, \n"
                    + "emp_notification.off_code notOffCode, auth,note,pro_id,emp_promotion.dept_code proDeptCode,emp_promotion.off_code proOffCode,next_spc,field_off_code, \n"
                    + "emp_cadre.wefd cadreWEFDate,emp_cadre.weft cadreWEFTime,pay_id, \n"
                    + "emp_pay_record.wef payrecordWEFDate,emp_pay_record.weft payrecordWEFTime,emp_pay_record.pay_scale,pay,s_pay,p_pay,oth_pay,emp_pay_record.gp, \n"
                    + "oth_desc,office1.dist_code notdistcode,office2.dist_code postingdistcode,spc1.gpc notgpc,spc2.gpc postinggpc,spc1.spn authSpn,spc2.spn postingSpn,\n"
                    + "gothspc.off_en goiPostingSpn,gothspc.oth_spc,posting_organization_type,pay_level,pay_cell from \n"
                    + "(SELECT not_id,pro_id,dept_code,off_code,next_spc,field_off_code,allot_desc,if_retrospective,post_grp_promotion,posting_organization_type FROM EMP_PROMOTION_LOG \n"
                    + " WHERE EMP_ID=? AND NOT_ID=? AND REF_CORRECTION_ID=?)emp_promotion \n"
                    + "inner join (select organization_type,not_id,ordno,orddt,dept_code,off_code,auth,note,if_visible,entry_type from emp_notification_log where \n"
                    + "EMP_ID=? AND NOT_ID=?)emp_notification \n"
                    + "on emp_promotion.not_id=emp_notification.not_id \n"
                    + "left outer join (select pay_id,not_id,wef,weft,pay_scale,pay,s_pay,p_pay,oth_pay,gp,oth_desc,pay_level,pay_cell from emp_pay_record_log \n"
                    + "where EMP_ID=? AND NOT_ID=?)emp_pay_record \n"
                    + "on emp_notification.not_id=emp_pay_record.not_id \n"
                    + "left outer join emp_cadre on emp_notification.not_id=emp_cadre.not_id \n"
                    + "left outer join g_office office1 on emp_notification.off_code=office1.off_code \n"
                    + "left outer join g_office office2 on emp_promotion.off_code=office2.off_code \n"
                    + "left outer join g_spc spc1 on emp_notification.auth=spc1.spc \n"
                    + "left outer join g_spc spc2 on emp_promotion.next_spc=spc2.spc\n"
                    + "left outer join (select off_en,cast(other_spc_id as text)oth_spc from g_oth_spc where is_active='Y')gothspc  \n"
                    + "on emp_promotion.next_spc=gothspc.oth_spc";
            pst = con.prepareStatement(sql);
            pst.setString(1, prform.getEmpid());
            pst.setInt(2, notId);
            pst.setInt(3, correctionid);
            pst.setString(4, prform.getEmpid());
            pst.setInt(5, notId);
            pst.setString(6, prform.getEmpid());
            pst.setInt(7, notId);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    prform.setChkNotSBPrint("Y");
                } else {
                    prform.setChkNotSBPrint("N");
                }
                String postgrp = rs.getString("post_grp_promotion");
                if (postgrp == null || postgrp.equals("")) {
                    postgrp = getEmployeePostGroup(prform.getEmpid());
                }
                prform.setSltPostGroup(postgrp);
                prform.setPromotionId(rs.getString("pro_id"));
                prform.setTxtNotOrdNo(rs.getString("ordno"));
                prform.setHnotid(rs.getInt("not_id"));
                prform.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                prform.setRadnotifyingauthtype(rs.getString("organization_type"));
                prform.setHidNotifyingDeptCode(rs.getString("notDeptCode"));
                prform.setHidNotifyingDistCode(rs.getString("notdistcode"));
                prform.setHidNotifyingOffCode(rs.getString("notOffCode"));
                prform.setHidNotifyingGPC(rs.getString("notgpc"));
                prform.setHidTempNotifyingOffCode(rs.getString("notOffCode"));
                if (prform.getRadnotifyingauthtype() != null && prform.getRadnotifyingauthtype().equals("GOI")) {
                    prform.setNotifyingPostName(getOtherSpn(rs.getString("auth")));
                    prform.setHidNotifyingOthSpc(rs.getString("auth"));
                } else {
                    prform.setHidTempNotifyingPost(rs.getString("auth"));
                    prform.setNotifyingPostName(rs.getString("authSpn"));
                    prform.setNotifyingSpc(rs.getString("auth"));
                }

                //prform.setNotifyingPostName(CommonFunctions.getSPN(con, rs.getString("auth")));
                prform.setSltAllotmentDesc(rs.getString("allot_desc"));
                prform.setChkRetroPromotion(rs.getString("if_retrospective"));

                prform.sethCadId(rs.getString("cad_id"));
                prform.setSltCadreDept(rs.getString("cadreDept"));
                prform.setSltCadre(rs.getString("cadre_code"));
                prform.setHidTempCadre(rs.getString("cadre_code"));
                prform.setSltGrade(rs.getString("grade"));
                prform.setHidTempGrade(rs.getString("grade"));
                prform.setSltCadreLevel(rs.getString("lvl"));
                prform.setSltDescription(rs.getString("description"));
                prform.setTxtCadreJoiningWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("cadreWEFDate")));
                prform.setSltCadreJoiningWEFTime(rs.getString("cadreWEFTime"));

                prform.setSltPostingDept(rs.getString("posting_dept_code"));
                prform.setSltGenericPost(rs.getString("post_code"));
                prform.setHidTempGenericPost(rs.getString("post_code"));

                prform.setRdPostClassification(rs.getString("post_class"));
                prform.setRdPostStatus(rs.getString("post_stat"));

                prform.setChkJoinedAsSuch(rs.getString("joined_assuch"));
                prform.setChkProformaPromotion(rs.getString("if_proforma"));

                prform.setHidPostedDeptCode(rs.getString("proDeptCode"));
                if ((rs.getString("proDeptCode") != null && !rs.getString("proDeptCode").equals(""))
                        || (rs.getString("posting_organization_type") != null && rs.getString("posting_organization_type").equals("GOO"))) {
                    prform.setRadpostingauthtype("GOO");
                    prform.setPostedPostName(rs.getString("postingSpn"));
                } else if (rs.getString("posting_organization_type") != null && rs.getString("posting_organization_type").equals("GOI")) {
                    prform.setRadpostingauthtype("GOI");
                    prform.setPostedPostName(rs.getString("goiPostingSpn"));
                    prform.setHidPostingOthSpc(rs.getString("oth_spc"));
                }
                prform.setHidPostedDistrict(rs.getString("postingdistcode"));
                prform.setHidPostedOffCode(rs.getString("proOffCode"));
                prform.setHidTempPostedOffCode(rs.getString("proOffCode"));
                prform.setHidPostedGPC(rs.getString("postinggpc"));
                prform.setPostedspc(rs.getString("next_spc"));
                prform.setHidTempPostedPost(rs.getString("next_spc"));
                //prform.setPostedPostName(rs.getString("postingSpn"));
                prform.setSltPostedFieldOff(rs.getString("field_off_code"));
                prform.setHidTempPostedFieldOffCode(rs.getString("field_off_code"));

                prform.setHpayid(rs.getInt("pay_id"));
                //prform.setSltPayScale(rs.getString("pay_scale"));
                //prform.setTxtGP(rs.getString("gp"));
                prform.setTxtBasic(rs.getString("pay"));
                prform.setTxtSP(rs.getString("s_pay"));
                prform.setTxtPP(rs.getString("p_pay"));
                prform.setTxtOP(rs.getString("oth_pay"));
                prform.setTxtDescOP(rs.getString("oth_desc"));
                prform.setTxtWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("payrecordWEFDate")));
                prform.setSltWEFTime(rs.getString("payrecordWEFTime"));
                prform.setNote(rs.getString("note"));
                if (rs.getString("pay_level") != null && !rs.getString("pay_level").equals("")) {
                    prform.setRdoPaycomm("7");
                    prform.setPayLevel(rs.getString("pay_level"));
                    prform.setPayCell(rs.getString("pay_cell"));
                } else if (rs.getString("pay_scale") != null && !rs.getString("pay_scale").equals("")) {
                    prform.setRdoPaycomm("6");
                    prform.setSltPayScale(rs.getString("pay_scale"));
                    prform.setTxtGP(rs.getString("gp"));
                }
                if (rs.getString("cur_cadre_grade_code") != null && !rs.getString("cur_cadre_grade_code").equals("")) {
                    prform.setSltGrade(rs.getString("cur_cadre_grade_code"));
                    prform.setGradeCode(rs.getString("cur_cadre_grade_code"));
                }
                if (rs.getString("grade") != null && !rs.getString("grade").equals("")) {
                    prform.setGradeName(rs.getString("grade"));
                }
                prform.setEntryType(rs.getString("entry_type"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return prform;
    }

    @Override
    public void approvePromotionDataSBCorrection(PromotionForm promotionform, String entrydeptcode, String entryoffcode, String entryspc, String loginuserid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        try {
            con = this.dataSource.getConnection();

            if (promotionform.getEntrytypeSBCorrection() != null && promotionform.getEntrytypeSBCorrection().equals("NEW")) {

                pst = con.prepareStatement("INSERT INTO EMP_NOTIFICATION(NOT_TYPE,EMP_ID,DOE,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH) "
                        + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                pst.setString(1, "PROMOTION");
                pst.setString(2, promotionform.getEmpid());
                pst.setTimestamp(3, new Timestamp(new Date().getTime()));
                pst.setString(4, promotionform.getTxtNotOrdNo());
                if (promotionform.getTxtNotOrdDt() != null) {
                    pst.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(promotionform.getTxtNotOrdDt()).getTime()));
                } else {
                    pst.setTimestamp(5, null);
                }
                pst.setString(6, promotionform.getHidNotifyingDeptCode());
                pst.setString(7, promotionform.getHidNotifyingOffCode());
                pst.setString(8, promotionform.getNotifyingSpc());
                pst.setString(9, "Y");
                pst.setString(10, entrydeptcode);
                pst.setString(11, entryoffcode);
                pst.setString(12, entryspc);
                pst.executeUpdate();

                rs = pst.getGeneratedKeys();
                rs.next();
                int notId = rs.getInt("NOT_ID");

                DataBaseFunctions.closeSqlObjects(rs, pst);

                String sqlString = ToStringBuilder.reflectionToString(pst);
                pst = con.prepareStatement("UPDATE emp_notification SET MODIFIED_BY=?,MODIFIED_ON=?,query_string=? where NOT_ID=?");
                pst.setString(1, loginuserid);
                pst.setTimestamp(2, new Timestamp(new Date().getTime()));
                pst.setString(3, sqlString);
                pst.setInt(4, notId);
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(rs, pst);

                pst = con.prepareStatement("INSERT INTO EMP_PROMOTION (NOT_ID, NOT_TYPE, EMP_ID, ALLOT_DESC, IF_RETROSPECTIVE,DEPT_CODE,OFF_CODE, NEXT_SPC,FIELD_OFF_CODE,"
                        + "entry_taken_by,entry_taken_on,post_grp_promotion,posting_organization_type) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                pst.setInt(1, notId);
                pst.setString(2, "PROMOTION");
                pst.setString(3, promotionform.getEmpid());
                pst.setString(4, promotionform.getSltAllotmentDesc());
                pst.setString(5, promotionform.getChkRetroPromotion());
                pst.setString(9, promotionform.getSltPostedFieldOff());
                pst.setString(10, loginuserid);
                pst.setTimestamp(11, new Timestamp(new Date().getTime()));
                pst.setString(12, promotionform.getSltPostGroup());
                if (promotionform.getRadpostingauthtype() != null && promotionform.getRadpostingauthtype().equals("GOI")) {
                    System.out.println("goi");
                    pst.setString(6, null);
                    pst.setString(7, null);
                    pst.setString(8, promotionform.getHidPostingOthSpc());
                    pst.setString(13, "GOI");
                } else if (promotionform.getRadpostingauthtype() != null && promotionform.getRadpostingauthtype().equals("GOO")) {
                    pst.setString(6, promotionform.getHidPostedDeptCode());
                    pst.setString(7, promotionform.getHidPostedOffCode());
                    pst.setString(8, promotionform.getPostedspc());
                    pst.setString(13, "GOO");
                }
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(rs, pst);

                EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
                epayrecordform.setEmpid(promotionform.getEmpid());
                epayrecordform.setNot_id(notId);
                epayrecordform.setNot_type("PROMOTION");
                epayrecordform.setPayscale(promotionform.getSltPayScale());
                epayrecordform.setBasic(promotionform.getTxtBasic());
                epayrecordform.setGp(promotionform.getTxtGP());
                epayrecordform.setS_pay(promotionform.getTxtSP());
                epayrecordform.setP_pay(promotionform.getTxtPP());
                epayrecordform.setOth_pay(promotionform.getTxtOP());
                epayrecordform.setOth_desc(promotionform.getTxtDescOP());
                epayrecordform.setWefDt(promotionform.getTxtWEFDt());
                epayrecordform.setWefTime(promotionform.getSltWEFTime());
                epayrecordform.setPayLevel(promotionform.getPayLevel());
                epayrecordform.setPayCell(promotionform.getPayCell());
                emppayrecordDAO.saveEmpPayRecordData(epayrecordform);

                Cadre cadreForm = new Cadre();

                cadreForm.setEmpId(promotionform.getEmpid());
                cadreForm.setNotId(notId);
                cadreForm.setNotType("PROMOTION");
                cadreForm.setCadreDept(promotionform.getSltCadreDept());
                cadreForm.setCadreName(promotionform.getSltCadre());
                //Set Cadre GradeCode and GradeName
                cadreForm.setGrade(promotionform.getSltGrade());
                cadreForm.setGradeCodeName(promotionform.getGradeName());
                cadreForm.setCadreLvl(promotionform.getSltCadreLevel());
                cadreForm.setDescription(promotionform.getSltDescription());
                cadreForm.setAllotmentYear(promotionform.getTxtAllotmentYear());
                cadreForm.setCadreId(promotionform.getTxtCadreId());
                cadreForm.setPostingDept(promotionform.getSltPostingDept());
                cadreForm.setPostCode(promotionform.getSltGenericPost());
                cadreForm.setPostClassification(promotionform.getRdPostClassification());
                cadreForm.setPostStatus(promotionform.getRdPostStatus());
                cadreForm.setCadreJoiningWEFDt(promotionform.getTxtCadreJoiningWEFDt());
                cadreForm.setCadreJoiningWEFTime(promotionform.getSltCadreJoiningWEFTime());
                cadreForm.setJoinedAsSuch(promotionform.getChkJoinedAsSuch());
                cadreForm.setIfProforma(promotionform.getChkProformaPromotion());
                cadreDAO.saveCadreData(cadreForm);

                String sbLanguage = sbDAO.getPromotionDetails(promotionform, notId, "PROMOTION");
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE NOT_ID=?");
                pst.setString(1, sbLanguage);
                pst.setInt(2, notId);
                pst.executeUpdate();
            } else {
                pst = con.prepareStatement("UPDATE emp_notification_log set ORDNO=? ,ORDDT=?, DEPT_CODE=?, OFF_CODE=?, AUTH=?, NOTE=?, IF_ASSUMED=?, IF_VISIBLE=?, ENT_DEPT=?, ENT_OFF=?, ENT_AUTH=?, ACS=?, ASCS=?, organization_type=?,organization_type_posting=? WHERE NOT_ID=?");
                pst.setString(1, promotionform.getTxtNotOrdNo());
                if (promotionform.getTxtNotOrdDt() != null) {
                    pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(promotionform.getTxtNotOrdDt()).getTime()));
                } else {
                    pst.setTimestamp(2, null);
                }
                pst.setString(3, promotionform.getHidNotifyingDeptCode());
                pst.setString(4, promotionform.getHidNotifyingOffCode());
                pst.setString(5, promotionform.getNotifyingSpc());
                pst.setString(6, promotionform.getNote());
                pst.setString(7, "N");
                pst.setString(8, "Y");
                pst.setString(9, entrydeptcode);
                pst.setString(10, entryoffcode);
                pst.setString(11, entryspc);
                pst.setString(12, null);
                pst.setString(13, null);
                if (promotionform.getRadpostingauthtype() != null && !promotionform.getRadpostingauthtype().equals("")) {
                    pst.setString(14, promotionform.getRadpostingauthtype());
                } else {
                    pst.setString(14, "GOO");
                }
                if (promotionform.getRadnotifyingauthtype() != null && !promotionform.getRadnotifyingauthtype().equals("")) {
                    pst.setString(15, promotionform.getRadnotifyingauthtype());
                } else {
                    pst.setString(15, "GOO");
                }
                pst.setInt(16, promotionform.getHnotid());
                pst.executeUpdate();

                pst = con.prepareStatement("UPDATE EMP_PROMOTION_LOG SET ALLOT_DESC=?, IF_RETROSPECTIVE=? ,DEPT_CODE=? ,OFF_CODE=?, NEXT_SPC=? ,FIELD_OFF_CODE=?,entry_taken_by=?,entry_taken_on=?,post_grp_promotion=?,posting_organization_type=? WHERE PRO_ID=?");
                pst.setString(1, promotionform.getSltAllotmentDesc());
                pst.setString(2, promotionform.getChkRetroPromotion());
                pst.setString(6, promotionform.getSltPostedFieldOff());
                pst.setString(7, loginuserid);
                pst.setTimestamp(8, new Timestamp(new Date().getTime()));
                pst.setString(9, promotionform.getSltPostGroup());
                if (promotionform.getRadpostingauthtype() != null && promotionform.getRadpostingauthtype().equals("GOI")) {
                    pst.setString(3, null);
                    pst.setString(4, null);
                    pst.setString(5, promotionform.getHidPostingOthSpc());
                    pst.setString(10, "GOI");
                } else if (promotionform.getRadpostingauthtype() != null && promotionform.getRadpostingauthtype().equals("GOO")) {
                    pst.setString(3, promotionform.getHidPostedDeptCode());
                    pst.setString(4, promotionform.getHidPostedOffCode());
                    pst.setString(5, promotionform.getPostedspc());
                    pst.setString(10, "GOO");
                }
                pst.setInt(11, Integer.parseInt(promotionform.getPromotionId()));
                pst.executeUpdate();

                EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
                epayrecordform.setPayid(promotionform.getHpayid());
                epayrecordform.setEmpid(promotionform.getEmpid());
                //epayrecordform.setPayscale(promotionForm.getSltPayScale());
                epayrecordform.setBasic(promotionform.getTxtBasic());
                epayrecordform.setGp(promotionform.getTxtGP());
                epayrecordform.setS_pay(promotionform.getTxtSP());
                epayrecordform.setP_pay(promotionform.getTxtPP());
                epayrecordform.setOth_pay(promotionform.getTxtOP());
                epayrecordform.setOth_desc(promotionform.getTxtDescOP());
                epayrecordform.setWefDt(promotionform.getTxtWEFDt());
                epayrecordform.setWefTime(promotionform.getSltWEFTime());
                if (promotionform.getRdoPaycomm() != null && promotionform.getRdoPaycomm().equals("6")) {
                    epayrecordform.setPayscale(promotionform.getSltPayScale());
                    epayrecordform.setGp(promotionform.getTxtGP());
                    epayrecordform.setPayLevel(null);
                    epayrecordform.setPayCell(null);

                } else if (promotionform.getRdoPaycomm() != null && promotionform.getRdoPaycomm().equals("7")) {
                    epayrecordform.setPayscale(null);
                    epayrecordform.setGp("0");
                    epayrecordform.setPayLevel(promotionform.getPayLevel());
                    epayrecordform.setPayCell(promotionform.getPayCell());
                }
                emppayrecordDAO.updateEmpPayRecordDataSBCorrection(epayrecordform);

                Cadre cadreForm = new Cadre();
                cadreForm.setEmpId(promotionform.getEmpid());
                cadreForm.setNotId(promotionform.getHnotid());
                cadreForm.setNotType("PROMOTION");
                cadreForm.setCadreCode(promotionform.gethCadId());
                cadreForm.setEmpId(promotionform.getEmpid());
                cadreForm.setCadreDept(promotionform.getSltCadreDept());
                cadreForm.setCadreName(promotionform.getSltCadre());
                //Set Cadre GradeCode and GradeName
                cadreForm.setGrade(promotionform.getSltGrade());
                cadreForm.setGradeCodeName(promotionform.getGradeName());
                cadreForm.setCadreLvl(promotionform.getSltCadreLevel());
                cadreForm.setDescription(promotionform.getSltDescription());
                cadreForm.setAllotmentYear(promotionform.getTxtAllotmentYear());
                cadreForm.setCadreId(promotionform.getTxtCadreId());
                cadreForm.setPostingDept(promotionform.getSltPostingDept());
                cadreForm.setPostCode(promotionform.getSltGenericPost());
                cadreForm.setPostClassification(promotionform.getRdPostClassification());
                cadreForm.setPostStatus(promotionform.getRdPostStatus());
                cadreForm.setCadreJoiningWEFDt(promotionform.getTxtCadreJoiningWEFDt());
                cadreForm.setCadreJoiningWEFTime(promotionform.getSltCadreJoiningWEFTime());
                cadreForm.setIfJoined(promotionform.getChkJoinedAsSuch());
                cadreForm.setIfProforma(promotionform.getChkProformaPromotion());
                if (promotionform.gethCadId() != null && !promotionform.gethCadId().equals("")) {
                    cadreDAO.updateCadreDataSBCorrection(cadreForm);
                }

                String sql = "UPDATE emp_promotion"
                        + " SET ALLOT_DESC = emp_promotion_log.ALLOT_DESC,IF_RETROSPECTIVE=emp_promotion_log.IF_RETROSPECTIVE,DEPT_CODE=emp_promotion_log.DEPT_CODE,OFF_CODE=emp_promotion_log.OFF_CODE,NEXT_SPC=emp_promotion_log.NEXT_SPC,FIELD_OFF_CODE=emp_promotion_log.FIELD_OFF_CODE,post_grp_promotion=emp_promotion_log.post_grp_promotion,posting_organization_type=emp_promotion_log.posting_organization_type"
                        + " FROM emp_promotion_log"
                        + " WHERE emp_promotion.not_id = emp_promotion_log.not_id and emp_promotion_log.not_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, promotionform.getHnotid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                sql = "UPDATE emp_notification"
                        + " SET ordno = emp_notification_log.ordno,orddt=emp_notification_log.orddt,dept_code=emp_notification_log.dept_code,off_code=emp_notification_log.off_code,auth=emp_notification_log.auth,organization_type=emp_notification_log.organization_type,organization_type_posting=emp_notification_log.organization_type_posting"
                        + " FROM emp_notification_log"
                        + " WHERE emp_notification.not_id = emp_notification_log.not_id and emp_notification_log.not_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, promotionform.getHnotid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                sql = "UPDATE emp_pay_record"
                        + " SET wef = emp_pay_record_log.wef,weft=emp_pay_record_log.weft,pay_scale=emp_pay_record_log.pay_scale,pay=emp_pay_record_log.pay,s_pay=emp_pay_record_log.s_pay,p_pay=emp_pay_record_log.p_pay,oth_pay=emp_pay_record_log.oth_pay,oth_desc=emp_pay_record_log.oth_desc,gp=emp_pay_record_log.gp"
                        + " FROM emp_pay_record_log"
                        + " WHERE emp_pay_record.not_id = emp_pay_record_log.not_id and emp_pay_record_log.not_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, promotionform.getHnotid());
                pst.executeUpdate();

                sql = "UPDATE EMP_CADRE"
                        + " SET wefd=EMP_CADRE_LOG.wefd, weft=EMP_CADRE_LOG.weft, dept_code=EMP_CADRE_LOG.dept_code, cadre_code=EMP_CADRE_LOG.cadre_code, grade=EMP_CADRE_LOG.grade, post_code=EMP_CADRE_LOG.post_code, post_class=EMP_CADRE_LOG.post_class, post_stat=EMP_CADRE_LOG.post_stat, joined_assuch=EMP_CADRE_LOG.joined_assuch, lvl=EMP_CADRE_LOG.lvl, DESCRIPTION=EMP_CADRE_LOG.DESCRIPTION, IF_PROFORMA=EMP_CADRE_LOG.IF_PROFORMA, IF_JOINED=EMP_CADRE_LOG.IF_JOINED,allot_year=EMP_CADRE_LOG.allot_year,"
                        + "cadreid=EMP_CADRE_LOG.cadreid,POSTING_DEPT_CODE=EMP_CADRE_LOG.POSTING_DEPT_CODE,cur_cadre_grade_code=EMP_CADRE_LOG.cur_cadre_grade_code FROM emp_cadre_log WHERE emp_cadre.not_id = emp_cadre_log.not_id and emp_cadre_log.cad_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(promotionform.gethCadId()));
                pst.executeUpdate();

                String sbLang = sbDAO.getPromotionDetails(promotionform, promotionform.getHnotid(), promotionform.getRdoPaycomm());
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE NOT_ID=?");
                pst.setString(1, sbLang);
                pst.setInt(2, promotionform.getHnotid());
                pst.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
}

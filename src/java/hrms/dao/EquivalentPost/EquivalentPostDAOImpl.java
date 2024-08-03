/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.EquivalentPost;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.EquivalentPost.EquivalentPost;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.EnrollmentToInsurance.Enrollmenttoinsurance;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Surendra
 */
public class EquivalentPostDAOImpl implements EquivalentPostDAO {
    
    @Resource(name = "dataSource")
    protected DataSource dataSource;
    
    protected ServiceBookLanguageDAO sbDAO;
    
    protected NotificationDAO notificationDao;
    
    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }
    
    public void setNotificationDao(NotificationDAO notificationDao) {
        this.notificationDao = notificationDao;
    }
    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public void saveEquivalentPost(EquivalentPost equivalpostForm, int notid) {
        Connection con = null;
        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "INSERT INTO EQUIVALENT_POST (NOT_ID,NOT_TYPE,EMP_ID,CUR_DEPT,CUR_OFF,CUR_POST,CUR_CADRE,CUR_GRADE,EQV_DEPT,EQV_POST,EQV_CADRE,EQV_GRADE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setInt(1, notid);
            pst.setString(2, "EQ_STAT");
            pst.setString(3, equivalpostForm.getEmpid());

            if(equivalpostForm.getRadpostingauthtype()!=null && equivalpostForm.getRadpostingauthtype().equals("GOI") ){
                pst.setString(4, null);
                pst.setString(5, null); 
                pst.setString(6, equivalpostForm.getHidPostedOthSpc()); 
                pst.setString(7, equivalpostForm.getSltCadre());
                pst.setString(8, equivalpostForm.getSltGrade());
            }else if(equivalpostForm.getRdTransaction()!=null && equivalpostForm.getRdTransaction().equals("C") ){
                pst.setString(4, equivalpostForm.getHidcurdeptcode());
                pst.setString(5, equivalpostForm.getHidcuroffcode()); 
                pst.setString(6, equivalpostForm.getHidcurpostcode()); 
                pst.setString(7, equivalpostForm.getHidcurcadre());
                pst.setString(8, equivalpostForm.getHidcurgrade());
            }else{   
                pst.setString(4, equivalpostForm.getHidPostedDeptCode());
                pst.setString(5, equivalpostForm.getHidPostedOffCode()); 
                pst.setString(6, equivalpostForm.getHidPostedSpc()); 
                pst.setString(7, equivalpostForm.getSltCadre());
                pst.setString(8, equivalpostForm.getSltGrade());
            }
            
            
            pst.setString(9, equivalpostForm.getHidequvalPostedDeptCode());
            pst.setString(10, equivalpostForm.getHidequvalPostedSpc());           
            pst.setString(11, equivalpostForm.getEquvalCadre()); 
            pst.setString(12, equivalpostForm.getEquvalGrade());     
            pst.executeUpdate();

            String sbLang = sbDAO.getEquivalentSbDetails(equivalpostForm, "EQ_STAT", notid);
            pst = con.prepareStatement("UPDATE emp_notification SET SB_DESCRIPTION =? WHERE NOT_ID=? ");
            pst.setString(1, sbLang);
            pst.setInt(2, notid);
            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
   
    
   @Override
    public List getEquivalentPostList(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List equivalentpostlist = new ArrayList();
        EquivalentPost dptBean = null;

        try {
            con = dataSource.getConnection();
            String sql = "select emp_notification.not_id,emp_notification.is_validated,emp_notification.doe,"
                    + "ordno,orddt from (select is_validated,emp_id,not_id,not_type,ordno,orddt,doe from \n"
                    + "emp_notification where emp_id=? and not_type='EQ_STAT')\n"
                    + "emp_notification\n"
                    + "inner join EQUIVALENT_POST on emp_notification.not_id=EQUIVALENT_POST.not_id;";
                    
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                dptBean = new EquivalentPost();
                dptBean.setNotId(rs.getString("not_id"));        
                dptBean.setIsValidated(rs.getString("is_validated"));              
                dptBean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                dptBean.setNotOrdNo(rs.getString("ordno"));
                dptBean.setNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                /*if (rs.getString("organization_type") != null && rs.getString("organization_type").equals("GOI")) {
                    dptBean.setPostingOffice(getOtherSpn(rs.getString("next_spc")));
                } else {
                    dptBean.setPostingOffice(rs.getString("off_en"));
                    dptBean.setPostingPost(rs.getString("spn"));
                }*/
              
                            
                equivalentpostlist.add(dptBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return equivalentpostlist;
    }
    
    @Override
    public void getCurrentEmpData(String empid,EquivalentPost equivalpostForm) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            String sql = "select emp_mast.cur_spc,g_cadre.department_code cadre_department,emp_mast.cur_off_code, emp_mast.cur_cadre_code, emp_mast.cur_cadre_grade, gspc1.spn spn1\n"
                    + "from ( select * from emp_mast where emp_id=? and dep_code='02') emp_mast\n"
                    + "left outer join g_spc gspc1 on emp_mast.cur_spc=gspc1.spc\n"
                    + "left outer join g_cadre on emp_mast.cur_cadre_code=g_cadre.cadre_code";
                    
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {       
                equivalpostForm.setHidcurpostname(rs.getString("spn1"));              
                equivalpostForm.setHidcurpostcode(rs.getString("cur_spc"));  
                String str1 = rs.getString("cur_spc");
                equivalpostForm.setHidcurdeptcode(str1.substring(13, 15));         
                equivalpostForm.setHidcuroffcode(rs.getString("cur_off_code")); 
                equivalpostForm.setHidcurcadredeptcode(rs.getString("cadre_department"));
                equivalpostForm.setHidcurcadre(rs.getString("cur_cadre_code"));                             
                equivalpostForm.setHidcurgrade(rs.getString("cur_cadre_grade"));     
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        
    }
     
    @Override
    public EquivalentPost getempeditEquivalentData(String empid, int notId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        EquivalentPost deptnBean = new EquivalentPost();
        try {
            con = this.dataSource.getConnection();

            String sql = "select emp_notification.not_id,emp_notification.not_type,EQUIVALENT_POST.CUR_POST,gspc1.spn spn1,EQUIVALENT_POST.CUR_OFF,EQUIVALENT_POST.CUR_DEPT,EQUIVALENT_POST.CUR_CADRE,EQUIVALENT_POST.CUR_GRADE,\n"
                    + "EQUIVALENT_POST.eqv_dept,EQUIVALENT_POST.eqv_post,g_post.post eqv_postname,g_cadre.department_code cadre_department,\n"
                    + "EQUIVALENT_POST.eqv_cadre,EQUIVALENT_POST.eqv_grade,ordno, orddt,organization_type,organization_type_posting,if_visible,\n"
                    + "emp_notification.dept_code,emp_notification.off_code,auth,g_spc.spn,note,office1.dist_code eqvDistCode,spc1.gpc eqvGPC\n"
                    + "from (select * from emp_notification\n"
                    + "where emp_id= ? and not_type='EQ_STAT' and not_id= ?) emp_notification\n"
                    + "inner join EQUIVALENT_POST on emp_notification.not_id=EQUIVALENT_POST.not_id \n"
                    + "left outer join g_spc on auth=g_spc.spc\n"
                    + "left outer join g_spc gspc1 on EQUIVALENT_POST.CUR_POST=gspc1.spc\n"
                    + "left outer join g_post on EQUIVALENT_POST.eqv_post=g_post.post_code\n"
                    + "left outer join g_cadre on EQUIVALENT_POST.eqv_cadre=g_cadre.cadre_code\n"
                    + "left outer join g_office office1 on EQUIVALENT_POST.cur_off=office1.off_code\n"
                    + "left outer join g_spc spc1 on EQUIVALENT_POST.CUR_POST=spc1.spc";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, notId);
            
            rs = pst.executeQuery();
            if (rs.next()) {
                deptnBean.setHidNotId(rs.getInt("not_id"));
                deptnBean.setHidNotifyingDeptCode(rs.getString("dept_code"));
                deptnBean.setHidNotifyingOffCode(rs.getString("off_code"));
                deptnBean.setHidNotiSpc(rs.getString("auth"));
                deptnBean.setNotifyingSpc(rs.getString("spn"));
                deptnBean.setTxtNotOrdNo(rs.getString("ordno"));
                deptnBean.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                deptnBean.setNote(rs.getString("note"));        
                
                deptnBean.setRadpostingauthtype(rs.getString("organization_type"));               
                deptnBean.setRadnotifyingauthtype(rs.getString("organization_type_posting"));
                
                deptnBean.setHidPostedDeptCode(rs.getString("CUR_DEPT"));              
                deptnBean.setHidPostedDistCode(rs.getString("eqvDistCode"));
                deptnBean.setHidPostedOffCode(rs.getString("CUR_OFF"));
                deptnBean.setGenericpostPosted(rs.getString("eqvGPC"));  
               // deptnBean.setHidPostedSpc(rs.getString("spn1"));                  
                deptnBean.setSltCadreDept(rs.getString("CUR_DEPT"));
                deptnBean.setSltCadre(rs.getString("CUR_CADRE"));
                deptnBean.setSltGrade(rs.getString("CUR_GRADE"));
                
                deptnBean.setHidequvalPostedDeptCode(rs.getString("EQV_DEPT"));
                //deptnBean.setHidequvalPostedOffCode(rs.getString("CUR_OFF"));
                deptnBean.setHidequvalPostedSpc(rs.getString("eqv_post"));
                deptnBean.setEquvalCadreDept(rs.getString("cadre_department"));
                deptnBean.setEquvalCadre(rs.getString("EQV_CADRE"));
                deptnBean.setEquvalGrade(rs.getString("EQV_GRADE"));               

                if(rs.getString("organization_type") != null && rs.getString("organization_type").equals("GOI")){
                    deptnBean.setRadpostingauthtype("GOI");
                    deptnBean.setHidPostedSpc(rs.getString("CUR_POST"));
                    deptnBean.setPostedSpc(getOtherSpn(rs.getString("CUR_POST")));
                    deptnBean.setHidNotifyingOthSpc(rs.getString("auth"));
                    deptnBean.setNotifyingSpc(getOtherSpn(rs.getString("auth")));                   
                }else{
                    deptnBean.setRadpostingauthtype("GOO");
                     deptnBean.setHidPostedSpc(rs.getString("CUR_POST"));
                     deptnBean.setPostedSpc(rs.getString("spn1"));
                     
                }
                
                if (rs.getString("organization_type_posting") != null && rs.getString("organization_type_posting").equals("GOI")) {
                    deptnBean.setHidNotifyingOthSpc(rs.getString("auth"));
                    deptnBean.setNotifyingSpc(getOtherSpn(rs.getString("auth")));
                }               
              
                if(rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")){
                    deptnBean.setChkNotSBPrint("Y");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deptnBean;
    }
    
    
    @Override
    public void updateEquivalentPost(EquivalentPost equivalpostForm) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

                String sql = "UPDATE EQUIVALENT_POST SET CUR_DEPT=?,CUR_OFF=?,CUR_POST=?,CUR_CADRE=?,CUR_GRADE=?,EQV_DEPT=?,EQV_POST=?,EQV_CADRE=?,EQV_GRADE=? WHERE NOT_ID=? AND EMP_ID=?";
                
                pst = con.prepareStatement(sql);
                
                if(equivalpostForm.getRadpostingauthtype()!=null && equivalpostForm.getRadpostingauthtype().equals("GOO") ){
                    pst.setString(1, equivalpostForm.getHidPostedDeptCode());
                    pst.setString(2, (equivalpostForm.getHidPostedOffCode()));              
                    pst.setString(3, (equivalpostForm.getHidPostedSpc()));               
                }else{
                    pst.setString(1, null);
                    pst.setString(2, null);                            
                    pst.setString(3, equivalpostForm.getHidPostedOthSpc()); 
                }
                pst.setString(4, (equivalpostForm.getSltCadre()));
                pst.setString(5, (equivalpostForm.getSltGrade()));
                pst.setString(6, (equivalpostForm.getHidequvalPostedDeptCode()));
                pst.setString(7, (equivalpostForm.getHidequvalPostedSpc()));
                pst.setString(8, (equivalpostForm.getEquvalCadre()));
                pst.setString(9,(equivalpostForm.getEquvalGrade()));
                pst.setInt(10, equivalpostForm.getHidNotId());
                pst.setString(11,equivalpostForm.getEmpid());
                pst.executeUpdate();

                String sbLang = sbDAO.getEquivalentSbDetails(equivalpostForm, "EQ_STAT", equivalpostForm.getHidNotId());

               pst = con.prepareStatement("UPDATE emp_notification SET SB_DESCRIPTION =? WHERE NOT_ID=? ");
               pst.setString(1, sbLang);
               pst.setInt(2, equivalpostForm.getHidNotId());
               pst.execute();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
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
    
}

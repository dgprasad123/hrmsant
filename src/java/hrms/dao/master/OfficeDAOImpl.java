/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.SelectOption;
import hrms.common.DataBaseFunctions;
import hrms.model.master.Office;
import hrms.model.report.superannuationProjectionReport.SuperannuationProjectionReportForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Durga
 */
public class OfficeDAOImpl implements OfficeDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getOtherOrganisationList() {
        Connection con = null;

        PreparedStatement ps = null;
        ResultSet rs = null;
        List officelist = new ArrayList();
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement(" SELECT other_spc_id, off_en FROM g_oth_spc WHERE  is_active='Y' ");
            rs = ps.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setLabel(rs.getString("off_en"));
                so.setValue(rs.getString("other_spc_id"));
                officelist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officelist;
    }

    @Override
    public List getGranteeOfficeList(String deptcode, String offSearch, int page, int rows) {
        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List officelist = new ArrayList();
        SelectOption so = null;

        int minlimit = rows * (page - 1);
        int maxlimit = rows;
        try {
            con = dataSource.getConnection();

            if (offSearch != null && !offSearch.equals("")) {
                pstmt = con.prepareStatement("SELECT off_code,off_en from g_office where off_bill_status='GA' AND department_code=? and off_en like ? order by off_en LIMIT ? OFFSET ?");
                pstmt.setString(1, deptcode);
                pstmt.setString(2, "%" + offSearch.toUpperCase() + "%");
                pstmt.setInt(3, maxlimit);
                pstmt.setInt(4, minlimit);
            } else {
                pstmt = con.prepareStatement("SELECT off_code,off_en from g_office where off_bill_status='GA' order by off_en LIMIT ? OFFSET ?");
                pstmt.setString(1, deptcode);
                pstmt.setInt(2, maxlimit);
                pstmt.setInt(3, minlimit);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("off_code"));
                so.setLabel(rs.getString("off_en"));
                officelist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officelist;
    }

    @Override
    public List getOfficeList(String deptcode, String offSearch, int page, int rows) {
        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List officelist = new ArrayList();
        SelectOption so = null;

        int minlimit = rows * (page - 1);
        int maxlimit = rows;
        try {
            con = dataSource.getConnection();

            if (offSearch != null && !offSearch.equals("")) {
                pstmt = con.prepareStatement("SELECT off_code,off_en from g_office where department_code=? and off_en like ? order by off_en LIMIT ? OFFSET ?");
                pstmt.setString(1, deptcode);
                pstmt.setString(2, "%" + offSearch.toUpperCase() + "%");
                pstmt.setInt(3, maxlimit);
                pstmt.setInt(4, minlimit);
            } else {
                pstmt = con.prepareStatement("SELECT off_code,off_en from g_office where department_code=? order by off_en LIMIT ? OFFSET ?");
                pstmt.setString(1, deptcode);
                pstmt.setInt(2, maxlimit);
                pstmt.setInt(3, minlimit);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("off_code"));
                so.setLabel(rs.getString("off_en"));
                officelist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officelist;
    }

    @Override
    public List getOfficeListCOWise(String coOffcode) {
        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList officelistcowise = new ArrayList();
        SelectOption so = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select ddo_off_code,off_en from ddo_to_co_mapping "
                    + " inner join g_office on ddo_to_co_mapping.ddo_off_code = g_office.off_code where co_off_code = ? ");
            pstmt.setString(1, coOffcode);
            rs = pstmt.executeQuery();
            while (rs.next()) {

                Office office = new Office();
                office.setOffCode(rs.getString("ddo_off_code"));
                office.setOffName(rs.getString("off_en"));
                officelistcowise.add(office);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officelistcowise;
    }

    @Override
    public List getFieldOffList(String offCode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        SelectOption so = null;

        ArrayList alist = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select off_code,off_name from g_office where p_off_code=? order by off_en");
            pstmt.setString(1, offCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setLabel(rs.getString("off_name"));
                so.setValue(rs.getString("off_code"));
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
    public int getOfficeListCount(String deptcode, String offSearch) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int cnt = 0;

        try {
            con = this.dataSource.getConnection();

            String sql = "";
            if (offSearch != null && !offSearch.equals("")) {
                sql = "SELECT COUNT(*) CNT FROM G_OFFICE WHERE DEPARTMENT_CODE=? AND OFF_EN LIKE ?";
                pst = con.prepareStatement(sql);
                pst.setString(1, deptcode);
                pst.setString(2, "%" + offSearch.toUpperCase() + "%");
            } else {
                sql = "SELECT COUNT(*) CNT FROM G_OFFICE WHERE DEPARTMENT_CODE=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, deptcode);
            }
            rs = pst.executeQuery();
            if (rs.next()) {
                cnt = rs.getInt("CNT");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return cnt;
    }

    @Override
    public List getReportingOfficeList(String offCode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List officelist = new ArrayList();
        try {
            String offlvl = "";
            String deptCode = "";
            String distCode = "";
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT DEPARTMENT_CODE,LVL,DIST_CODE FROM G_OFFICE WHERE OFF_CODE=?");
            pstmt.setString(1, offCode);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                offlvl = rs.getString("LVL");
                deptCode = rs.getString("DEPARTMENT_CODE");
                distCode = rs.getString("DIST_CODE");
            }
            if (!offlvl.equals("01") && !offlvl.equals("02")) {
                /*Get Collectorate of that district*/
                pstmt = con.prepareStatement("select off_code,off_en from g_office where dist_code=? and category = 'DISTRICT COLLECTORATE'");
                pstmt.setString(1, distCode);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    Office office = new Office();
                    office.setOffCode(rs.getString("off_code"));
                    office.setOffName(rs.getString("off_en"));
                    officelist.add(office);
                }
                pstmt = con.prepareStatement("select off_code,off_en from ddo_to_co_mapping "
                        + "inner join g_office on ddo_to_co_mapping.co_off_code = g_office.off_code "
                        + "where ddo_off_code=?;");
                pstmt.setString(1, offCode);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    Office office = new Office();
                    office.setOffCode(rs.getString("off_code"));
                    office.setOffName(rs.getString("off_en"));
                    officelist.add(office);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officelist;
    }

    @Override
    public List getTotalDDOOfficeList(String deptcode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List officelist = new ArrayList();
        Office office = null;
        try {
            if (deptcode != null && !deptcode.equals("")) {
                con = dataSource.getConnection();
                pstmt = con.prepareStatement("SELECT off_code,off_en,ddo_code from g_office where department_code=? and is_ddo='Y' order by off_en asc");
                pstmt.setString(1, deptcode);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    office = new Office();
                    office.setOffCode(rs.getString("off_code"));
                    office.setOffName(rs.getString("off_en") + "(" + rs.getString("ddo_code") + ")");
                    office.setDdoCode(rs.getString("ddo_code"));
                    officelist.add(office);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officelist;

    }

    @Override
    public List getTotalOfficeList(String deptcode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List officelist = new ArrayList();
        Office office = null;
        try {
            if (deptcode != null && !deptcode.equals("")) {
                con = dataSource.getConnection();
                if (deptcode.equals("LM")) {
                    String sql = "SELECT off_id,off_name FROM g_officiating ORDER BY off_id ASC";
                    pstmt = con.prepareStatement(sql);
                    rs = pstmt.executeQuery();
                    while (rs.next()) {
                        office = new Office();
                        office.setOffCode(rs.getString("off_id"));
                        office.setOffName(rs.getString("off_name"));
                        officelist.add(office);
                    }
                } else {
                    pstmt = con.prepareStatement("SELECT off_code,off_en,ddo_code,dist_code from g_office where department_code=? and (is_ddo='Y' or off_status='F') order by off_en asc");
                    pstmt.setString(1, deptcode);
                    rs = pstmt.executeQuery();
                    while (rs.next()) {
                        office = new Office();
                        office.setOffCode(rs.getString("off_code"));
                        office.setOffName(rs.getString("off_en") + "(" + rs.getString("ddo_code") + ")");
                        office.setDdoCode(rs.getString("ddo_code"));
                        office.setDistCode(rs.getString("dist_code"));
                        officelist.add(office);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officelist;
    }

    @Override
    public List getTotalDeputedOfficeList(String deptcode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List officelist = new ArrayList();
        Office office = null;
        try {
            if (deptcode != null && !deptcode.equals("")) {
                con = dataSource.getConnection();

                pstmt = con.prepareStatement("SELECT off_code,off_en,ddo_code from g_office where department_code=? and (is_ddo='Y' or off_status='F') and IS_DEPUTED_OFF='Y' order by off_en asc");
                pstmt.setString(1, deptcode);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    office = new Office();
                    office.setOffCode(rs.getString("off_code"));
                    office.setOffName(rs.getString("off_en") + "(" + rs.getString("ddo_code") + ")");
                    office.setDdoCode(rs.getString("ddo_code"));
                    officelist.add(office);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officelist;
    }

    @Override
    public List getTotalOfficeList(String deptcode, String distCode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List officelist = new ArrayList();
        Office office = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT off_code,off_en,ddo_code from g_office where department_code=? and dist_code=? and (is_ddo='Y' or off_status='F') order by off_en asc");
            pstmt.setString(1, deptcode);
            pstmt.setString(2, distCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                office = new Office();
                office.setOffCode(rs.getString("off_code"));
                office.setOffName(rs.getString("off_en") + "(" + rs.getString("ddo_code") + ")");
                office.setDdoCode(rs.getString("ddo_code"));
                officelist.add(office);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officelist;
    }

    @Override
    public List getControllingOfficelist(String deptcode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List officelist = new ArrayList();
        Office office = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT off_code,off_en,ddo_code from g_office where department_code=? and (is_ddo='Y' or off_status='F') and (lvl='01' or lvl='02') order by off_en asc");
            pstmt.setString(1, deptcode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                office = new Office();
                office.setOffCode(rs.getString("off_code"));
                office.setOffName(rs.getString("off_en") + "(" + rs.getString("ddo_code") + ")");
                office.setDdoCode(rs.getString("ddo_code"));
                officelist.add(office);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officelist;
    }

    @Override
    public List getOfficeListTreasuryWise(String trcode, String deptcode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List officelist = new ArrayList();
        Office office = null;
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT off_code, off_en, ddo_code from g_office where department_code=? and off_code like '" + trcode + "%' and (is_ddo='Y' or off_status='F') order by off_en asc");
            pstmt.setString(1, deptcode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                office = new Office();
                office.setOffCode(rs.getString("off_code"));
                office.setOffName(rs.getString("off_en") + "(" + rs.getString("ddo_code") + ")");
                office.setDdoCode(rs.getString("ddo_code"));
                officelist.add(office);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officelist;
    }

    @Override
    public List getParentOffice(String poffCode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List officelist = new ArrayList();
        Office office = null;
        //String pOffCode = "";
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT off_code,off_en,ddo_code from g_office where p_off_code=? order by off_en asc");
            pstmt.setString(1, poffCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                office = new Office();
                office.setPoffCode(rs.getString("off_code"));
                office.setOffName(rs.getString("off_en") + "(" + rs.getString("ddo_code") + ")");
                office.setDdoCode(rs.getString("ddo_code"));
                officelist.add(office);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officelist;
    }

    @Override
    public List getDistrictWiseOfficeList(String distCode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List officelist = new ArrayList();
        Office office = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT off_code,off_en,ddo_code,getNoofEmployee(off_code) NO_OF_EMPLOYEE from g_office where dist_code=? and is_ddo='Y' order by off_en");
            pstmt.setString(1, distCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                office = new Office();
                office.setOffCode(rs.getString("off_code"));
                office.setOffName(rs.getString("off_en") + "(" + rs.getString("ddo_code") + ")");
                office.setDdoCode(rs.getString("ddo_code"));
                office.setNoofemployee(rs.getInt("NO_OF_EMPLOYEE"));
                officelist.add(office);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officelist;
    }

    @Override
    public Office getOfficeDetails(String offCode) {
        Office off = new Office();
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement(" SELECT off_code,off_en,off_name,g_office.department_code,g_office.category,suffix,lvl,if_group,g_office.off_address,state_code,dist_code,block_code,ps_code,po_code,vill_code,pincode,\n"
                    + " g_office.tr_code,g_office.ddo_code,ddo_post,tel_std,tel_no,fax_std,fax_no,g_office.off_email,off_incharge,p_off_code,g_office.bank_code,g_office.branch_code,rec_by,\n"
                    + " desg,sal_head,sal_head_desc,off_status,ddo_reg_no,tan_no,dto_reg_no,online_bill_submission,pa_code,ddo_cur_acc_no,is_ddo,off_bill_status,ddo_spc,ddo_hrmsid,\n"
                    + " office_category_id,hod_office_code,sub_division_code,paybill_priority,p_type_id,ext_field_code,have_sub_office,off_abbrev,int_ddo_id, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME, POST FROM g_office \n"
                    + " left outer join g_post \n"
                    + " on g_office.ddo_post=g_post.post_code \n"
                    + " LEFT OUTER JOIN EMP_MAST ON g_office.ddo_hrmsid=EMP_MAST.EMP_ID\n"
                    + " WHERE off_code = ?");
            pst.setString(1, offCode);
            rs = pst.executeQuery();

            while (rs.next()) {
                off = new Office();
                off.setOffCode(rs.getString("off_code"));
                off.setOffEn(rs.getString("off_en"));
                off.setOffName(rs.getString("off_name"));
                off.setDeptCode(rs.getString("department_code"));
                off.setCategory(rs.getString("category"));
                off.setSuffix(rs.getString("suffix"));
                off.setLvl(rs.getString("lvl"));
                off.setIfGroup(rs.getString("if_group"));
                off.setOffAddress(rs.getString("off_address"));
                off.setStateCode(rs.getString("state_code"));
                off.setDistCode(rs.getString("dist_code"));
                off.setSltBlock(rs.getString("block_code"));
                off.setPsCode(rs.getString("ps_code"));
                off.setPoCode(rs.getString("po_code"));
                off.setVillCode(rs.getString("vill_code"));
                off.setPincode(rs.getString("pincode"));
                off.setTrCode(rs.getString("tr_code"));
                off.setDdoCode(rs.getString("ddo_code"));
                off.setDdoName(rs.getString("EMPNAME"));
                off.setDdoPost(rs.getString("ddo_post"));
                off.setDdoPostName(rs.getString("post"));
                off.setTelStd(rs.getString("tel_std"));
                off.setTelNo(rs.getString("tel_no"));
                off.setFaxStd(rs.getString("fax_std"));
                off.setFaxNo(rs.getString("fax_no"));
                off.setOffEmail(rs.getString("off_email"));
                off.setOffIncharge(rs.getString("off_incharge"));
                off.setPoffCode(rs.getString("p_off_code"));
                off.setSltBank(rs.getString("bank_code"));
                off.setSltBranch(rs.getString("branch_code"));
                off.setRecBy(rs.getString("rec_by"));
                off.setDesg(rs.getString("desg"));
                off.setSalHead(rs.getString("sal_head"));
                off.setSalHeadDesc(rs.getString("sal_head_desc"));
                off.setOffStatus(rs.getString("off_status"));
                off.setDdoRegNo(rs.getString("ddo_reg_no"));
                off.setTanNo(rs.getString("tan_no"));
                off.setDtoRegNo(rs.getString("dto_reg_no"));
                off.setOnlineBillSubmission(rs.getString("online_bill_submission"));
                off.setPaCode(rs.getString("pa_code"));
                off.setDdoCurAccNo(rs.getString("ddo_cur_acc_no"));
                off.setIsDdo(rs.getString("is_ddo"));
                off.setOffBillStatus(rs.getString("off_bill_status"));
                off.setDdoSpc(rs.getString("ddo_spc"));

                off.setDdoHrmsid(rs.getString("ddo_hrmsid"));
                off.setOfficeCategoryId(rs.getString("office_category_id"));
                off.setHodOfficeCode(rs.getString("hod_office_code"));
                off.setSubDivisionCode(rs.getString("sub_division_code"));
                off.setPaybillPriority(rs.getInt("paybill_priority"));
                off.setPTypeId(rs.getString("p_type_id"));
                off.setExtFieldCode(rs.getString("ext_field_code"));
                off.setHaveSubOffice(rs.getString("have_sub_office"));
                off.setOffAbbrev(rs.getString("off_abbrev"));
                off.setIntDdoId(rs.getString("int_ddo_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return off;
    }

    @Override
    public boolean saveGranteeOfficeDetails(Office off) {
        int n = 0;
        boolean flag = false;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs = null;
        Connection con = null;
        String offCode = "";
        String newOffCode = "";
        String maxCode = "";
        int mCode = 0;
        String deptabbr = "";
        String offCodesubStr = "";
        try {
            con = dataSource.getConnection();

            pst1 = con.prepareStatement("select dept_abbr from g_department where department_code=?");
            pst1.setString(1, off.getDeptCode());
            rs = pst1.executeQuery();
            if (rs.next()) {
                deptabbr = rs.getString("dept_abbr");
            } else {
                deptabbr = "ABB";
            }
            offCodesubStr = off.getPoffCode().substring(0, 9);

            pst1 = con.prepareStatement("select max(cast(substring(off_code,10,4) as integer))+1 off_code  from g_office where  off_code like '" + offCodesubStr + "%'");
            // pst1.setString(1, off.getPoffCode());
            rs = pst1.executeQuery();
            if (rs.next()) {
                offCode = off.getPoffCode().substring(0, 9) + StringUtils.leftPad(rs.getString("off_code"), 4, "0");

            } else {
                offCode = off.getPoffCode().substring(0, 9) + "0001";
            }

            pst = con.prepareStatement("INSERT INTO g_office(off_code,off_en,off_name,department_code,category,suffix,lvl,if_group,off_address,state_code,dist_code,block_code,ps_code,po_code,vill_code,pincode,tr_code,ddo_code,ddo_post,tel_std,tel_no,fax_std,fax_no,off_email,off_incharge,p_off_code,bank_code,branch_code,rec_by,desg,sal_head,sal_head_desc,off_status,ddo_reg_no,tan_no,dto_reg_no,online_bill_submission,pa_code,ddo_cur_acc_no,is_ddo,off_bill_status,ddo_spc,ddo_hrmsid,office_category_id,hod_office_code,sub_division_code,paybill_priority,p_type_id,ext_field_code,have_sub_office,off_abbrev,int_ddo_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            pst.setString(1, offCode);
            pst.setString(2, off.getOffEn().toUpperCase());
            pst.setString(3, off.getOffEn().toUpperCase());
            pst.setString(4, off.getDeptCode());
            pst.setString(5, off.getCategory());
            pst.setString(6, off.getSuffix());
            pst.setString(7, off.getLvl());
            pst.setString(8, off.getIfGroup());
            pst.setString(9, off.getOffAddress());
            pst.setString(10, off.getStateCode());
            pst.setString(11, off.getDistCode());
            pst.setString(12, off.getSltBlock());
            pst.setString(13, off.getPsCode());
            pst.setString(14, off.getPoCode());
            pst.setString(15, off.getVillCode());
            pst.setString(16, off.getPincode());
            pst.setString(17, off.getTrCode());
            pst.setString(18, off.getDdoCode());
            pst.setString(19, off.getDdoPost());
            pst.setString(20, off.getTelStd());
            pst.setString(21, off.getTelNo());
            pst.setString(22, off.getFaxStd());
            pst.setString(23, off.getFaxNo());
            pst.setString(24, off.getOffEmail());
            pst.setString(25, off.getOffIncharge());
            pst.setString(26, off.getPoffCode());
            pst.setString(27, off.getSltBank());
            pst.setString(28, off.getSltBranch());
            pst.setString(29, off.getRecBy());
            pst.setString(30, off.getDesg());
            pst.setString(31, off.getSalHead());
            pst.setString(32, off.getSalHeadDesc());
            pst.setString(33, off.getOffStatus());
            pst.setString(34, off.getDdoRegNo());
            pst.setString(35, off.getTanNo());
            pst.setString(36, off.getDtoRegNo());
            // pst.setString(37, off.getOnlineBillSubmission());
            pst.setString(37, "N");
            pst.setString(38, off.getPaCode());
            pst.setString(39, off.getDdoCurAccNo());
            pst.setString(40, off.getIsDdo());
            pst.setString(41, off.getOffStatus());
            pst.setString(42, off.getDdoSpc());
            pst.setString(43, off.getDdoHrmsid());
            pst.setString(44, off.getOfficeCategoryId());
            pst.setString(45, off.getHodOfficeCode());
            pst.setString(46, off.getSubDivisionCode());
            pst.setInt(47, off.getPaybillPriority());
            pst.setString(48, off.getPTypeId());
            pst.setString(49, off.getExtFieldCode());
            // pst.setString(50, off.getHaveSubOffice());
            if (off.getPoffCode() != null && !off.getPoffCode().equals("")) {
                pst.setString(50, "Y");
            } else {
                pst.setString(50, "N");
            }
            pst.setString(51, off.getOffAbbrev());
            pst.setString(52, off.getIntDdoId());

            n = pst.executeUpdate();
            if (n == 1) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return flag;
    }

    @Override
    public boolean updateGranteeOfficeDetails(Office off) {
        int n = 0;
        boolean flag = false;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE g_office SET off_en=?, off_name=?, off_address=?, dist_code=?, block_code=?, vill_code=?, pincode=?, tr_code=?  WHERE off_code=?");

            pst.setString(1, off.getOffEn().toUpperCase());
            pst.setString(2, off.getOffEn().toUpperCase());
            pst.setString(3, off.getOffAddress());
            pst.setString(4, off.getDistCode());
            pst.setString(5, off.getBlockCode());
            pst.setString(6, off.getVillCode());
            pst.setString(7, off.getPincode());
            pst.setString(8, off.getTrCode());

            pst.setString(9, off.getOffCode());
            n = pst.executeUpdate();
            if (n == 1) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return flag;
    }

    @Override
    public boolean saveOfficeDetails(Office off) {
        int n = 0;
        boolean flag = false;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs = null;
        Connection con = null;
        String offCode = "";
        String newOffCode = "";
        String maxCode = "";
        int mCode = 0;
        try {
            con = dataSource.getConnection();
            pst1 = con.prepareStatement("select max(cast(substring(off_code,10) as int)+1) offcode from g_office where ddo_code=?");
            pst1.setString(1, off.getDdoCode());
            rs = pst1.executeQuery();
            if (rs.next()) {
                if (rs.getInt("offcode") == 0) {
                    offCode = off.getDdoCode() + "0000";
                } else {
                    // mCode = rs.getInt("offcode") + 1;
                    maxCode = "0000" + "" + rs.getInt("offcode");
                    offCode = off.getDdoCode() + maxCode.substring(maxCode.length() - 4);
                }
            }

            pst = con.prepareStatement("INSERT INTO g_office(off_code,off_en,off_name,department_code,category,suffix,lvl,if_group,off_address,state_code,dist_code,block_code,ps_code,po_code,vill_code,pincode,tr_code,ddo_code,ddo_post,tel_std,tel_no,fax_std,fax_no,off_email,off_incharge,p_off_code,bank_code,branch_code,rec_by,desg,sal_head,sal_head_desc,off_status,ddo_reg_no,tan_no,dto_reg_no,online_bill_submission,pa_code,ddo_cur_acc_no,is_ddo,off_bill_status,ddo_spc,ddo_hrmsid,office_category_id,hod_office_code,sub_division_code,paybill_priority,p_type_id,ext_field_code,have_sub_office,off_abbrev,int_ddo_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            pst.setString(1, offCode);
            pst.setString(2, off.getOffEn());
            pst.setString(3, off.getOffName());
            pst.setString(4, off.getDeptCode());
            pst.setString(5, off.getCategory());
            pst.setString(6, off.getSuffix());
            pst.setString(7, off.getLvl());
            pst.setString(8, off.getIfGroup());
            pst.setString(9, off.getOffAddress());
            pst.setString(10, off.getStateCode());
            pst.setString(11, off.getDistCode());
            pst.setString(12, off.getBlockCode());
            pst.setString(13, off.getPsCode());
            pst.setString(14, off.getPoCode());
            pst.setString(15, off.getVillCode());
            pst.setString(16, off.getPincode());
            pst.setString(17, off.getTrCode());
            pst.setString(18, off.getDdoCode());
            pst.setString(19, off.getDdoPost());
            pst.setString(20, off.getTelStd());
            pst.setString(21, off.getTelNo());
            pst.setString(22, off.getFaxStd());
            pst.setString(23, off.getFaxNo());
            pst.setString(24, off.getOffEmail());
            pst.setString(25, off.getOffIncharge());
            pst.setString(26, off.getPoffCode());
            pst.setString(27, off.getSltBank());
            pst.setString(28, off.getSltBranch());
            pst.setString(29, off.getRecBy());
            pst.setString(30, off.getDesg());
            pst.setString(31, off.getSalHead());
            pst.setString(32, off.getSalHeadDesc());
            pst.setString(33, off.getOffStatus());
            pst.setString(34, off.getDdoRegNo());
            pst.setString(35, off.getTanNo());
            pst.setString(36, off.getDtoRegNo());
            // pst.setString(37, off.getOnlineBillSubmission());
            pst.setString(37, "N");
            pst.setString(38, off.getPaCode());
            pst.setString(39, off.getDdoCurAccNo());
            pst.setString(40, off.getIsDdo());
            pst.setString(41, off.getOffBillStatus());
            pst.setString(42, off.getDdoSpc());
            pst.setString(43, off.getDdoHrmsid());
            pst.setString(44, off.getOfficeCategoryId());
            pst.setString(45, off.getHodOfficeCode());
            pst.setString(46, off.getSubDivisionCode());
            pst.setInt(47, off.getPaybillPriority());
            pst.setString(48, off.getPTypeId());
            pst.setString(49, off.getExtFieldCode());
            // pst.setString(50, off.getHaveSubOffice());
            if (off.getPoffCode() != null && !off.getPoffCode().equals("")) {
                pst.setString(50, "Y");
            } else {
                pst.setString(50, "N");
            }
            pst.setString(51, off.getOffAbbrev());
            pst.setString(52, off.getIntDdoId());

            n = pst.executeUpdate();
            if (n == 1) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return flag;
    }

    @Override
    public boolean updateOfficeDetails(Office off) {
        int n = 0;
        boolean flag = false;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE g_office SET ddo_reg_no = ?, tan_no = ?, dto_reg_no = ?,ddo_post=?,ddo_hrmsid=?,pa_code=?,rec_by=?,bank_code=?,branch_code=?,DDO_CUR_ACC_NO=?, tr_code=?  WHERE off_code=?");

            pst.setString(1, off.getDdoRegNo());
            pst.setString(2, off.getTanNo());
            pst.setString(3, off.getDtoRegNo());
            pst.setString(4, off.getDdoPost());
            pst.setString(5, off.getDdoName());
            pst.setString(6, off.getPaCode());
            pst.setString(7, off.getRecBy());
            pst.setString(8, off.getSltBank());
            pst.setString(9, off.getSltBranch());
            pst.setString(10, off.getDdoCurAccNo());
            pst.setString(11, off.getTrCode());
            pst.setString(12, off.getOffCode());
            n = pst.executeUpdate();
            if (n == 1) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return flag;
    }

    @Override
    public List getOfficeListFilter(String offcode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List officelist = new ArrayList();
        SelectOption so = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT off_code,off_en,ddo_code from g_office where off_code=?");
            pstmt.setString(1, offcode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("off_code"));
                so.setLabel(rs.getString("off_en") + " (" + rs.getString("ddo_code") + ")");
                officelist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officelist;
    }

    @Override
    public List getEmployeePostedOfficeList(String deptcode, String empid, String offcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List officelist = new ArrayList();
        Office office = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select off_code,off_en from emp_join"
                    + " inner join g_office on emp_join.auth_off=g_office.off_code where emp_id=? and department_code=? group by off_code,off_en"
                    + " union"
                    + " select off_code,off_en from g_workflow_routing"
                    + " inner join g_office on g_workflow_routing.office_code=g_office.off_code where reporting_office_code=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setString(2, deptcode);
            pst.setString(3, offcode);
            rs = pst.executeQuery();
            while (rs.next()) {
                office = new Office();
                office.setOffCode(rs.getString("off_code"));
                office.setOffName(rs.getString("off_en"));
                officelist.add(office);
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
    public Office getOfficeName(String ddoCode) {
        Office off = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT * FROM g_office WHERE ddo_code = ?");
            pst.setString(1, ddoCode);
            rs = pst.executeQuery();

            while (rs.next()) {
                off = new Office();
                off.setOffCode(rs.getString("off_code"));
                off.setOffEn(rs.getString("off_en"));

            }
        } catch (Exception e) {
            e.printStackTrace();;
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return off;
    }

    @Override
    public List getDistrictWiseDDOList(String distcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SelectOption so = null;

        List ddolist = new ArrayList();
        try {
            con = dataSource.getConnection();

            String sql = "select ddo_code,off_code,off_en from g_office where dist_code=? and is_ddo='Y' order by off_en";
            pst = con.prepareStatement(sql);
            pst.setString(1, distcode);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("off_code"));
                so.setLabel(rs.getString("off_en"));
                ddolist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ddolist;
    }

    @Override
    public List getControllingOfficelistToAdd(String deptcode) {

        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List colist = new ArrayList();
        Office office = null;

        try {
            con = this.dataSource.getConnection();

            pstmt = con.prepareStatement("select off_code,co_code,ddo_code,off_en from g_office where department_code=? and (co_code is not null and co_code <> '')");
            pstmt.setString(1, deptcode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                office = new Office();
                office.setOffCode(rs.getString("off_code"));
                office.setOffName(rs.getString("off_en") + "(" + rs.getString("co_code") + ")");
                colist.add(office);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return colist;
    }

    @Override
    public ArrayList getCOList(String logindistcode, String offcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean distCodeStatus = false;

        String errMsg = "N";

        SelectOption so = null;

        ArrayList colist = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String sql = "select dist_code from g_office where off_code=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("dist_code") != null && !rs.getString("dist_code").equals("")) {
                    if (logindistcode.equals(rs.getString("dist_code"))) {
                        distCodeStatus = true;
                        errMsg = "N";
                    } else {
                        errMsg = "This Office does not belong to your District.";
                    }
                }
            }

            if (distCodeStatus == true) {
                sql = "select co_code,off_en from ddo_to_co_mapping"
                        + " inner join g_office on ddo_to_co_mapping.co_off_code=g_office.off_code where ddo_off_code=? and is_active='Y'";
                pst = con.prepareStatement(sql);
                pst.setString(1, offcode);
                rs = pst.executeQuery();
                while (rs.next()) {
                    so = new SelectOption();
                    so.setValue(rs.getString("co_code"));
                    so.setLabel(rs.getString("off_en"));
                    colist.add(so);
                }
            } else {
                so = new SelectOption();
                so.setValue("ERROR");
                so.setLabel(errMsg);
                colist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return colist;
    }

    @Override
    public int insertCOOffice(Office office) {

        Connection con = null;

        PreparedStatement pst = null;
        int retVal = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "insert into ddo_to_co_mapping(ddo_off_code,co_off_code) values(?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, office.getOffCode());
            pst.setString(2, office.getPoffCode());
            retVal = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return retVal;
    }

    @Override
    public List getTotalOfficeListForBacklogEntry(String deptcode, String distCode) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List officelist = new ArrayList();
        Office office = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT off_code,off_en,ddo_code from g_office where department_code=? and dist_code=? order by off_en asc");
            pstmt.setString(1, deptcode);
            pstmt.setString(2, distCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                office = new Office();
                office.setOffCode(rs.getString("off_code"));
                office.setOffName(rs.getString("off_en") + "(" + rs.getString("ddo_code") + ")");
                office.setDdoCode(rs.getString("ddo_code"));
                officelist.add(office);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officelist;

    }

    @Override
    public List getTotalOfficeListForBacklogEntry(String deptcode) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List officelist = new ArrayList();
        Office office = null;
        try {
            if (deptcode != null && !deptcode.equals("")) {
                con = dataSource.getConnection();
                if (deptcode.equals("LM")) {
                    String sql = "SELECT off_id,off_name FROM g_officiating ORDER BY off_id ASC";
                    pstmt = con.prepareStatement(sql);
                    rs = pstmt.executeQuery();
                    while (rs.next()) {
                        office = new Office();
                        office.setOffCode(rs.getString("off_id"));
                        office.setOffName(rs.getString("off_name"));
                        officelist.add(office);
                    }
                } else {
                    pstmt = con.prepareStatement("SELECT off_code,off_en,ddo_code from g_office where department_code=? order by off_en asc");
                    pstmt.setString(1, deptcode);
                    rs = pstmt.executeQuery();
                    while (rs.next()) {
                        office = new Office();
                        office.setOffCode(rs.getString("off_code"));
                        if (rs.getString("ddo_code") != null) {
                            office.setOffName(rs.getString("off_en") + " (" + rs.getString("ddo_code") + ")");
                        } else {
                            office.setOffName(rs.getString("off_en"));
                        }
                        office.setDdoCode(rs.getString("ddo_code"));
                        officelist.add(office);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officelist;

    }

    @Override
    public Office getOfficeCodeDetails(String offCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Office offForm = null;
        try {
            con = this.dataSource.getConnection();
            offForm = new Office();
            ps = con.prepareStatement("Select * from g_office where off_code=?");
            ps.setString(1, offCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                offForm.setHidOfficeCode(rs.getString("off_code"));
                offForm.setOffEn(rs.getString("off_en"));
                offForm.setCategory(rs.getString("category"));
                offForm.setSuffix(rs.getString("suffix"));
                offForm.setIsDdo(rs.getString("is_ddo"));
                offForm.setOnlineBillSubmission(rs.getString("online_bill_submission"));
                offForm.setOffStatus(rs.getString("off_status"));
                offForm.setHidOfficeCode(offCode);
                offForm.setOffEn(rs.getString("off_en"));
                offForm.setHidDeptCode(rs.getString("department_code"));
                offForm.setHidDistCode(rs.getString("dist_code"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return offForm;
    }

    @Override
    public boolean updateOfficeName(Office office) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        int n = 0;
        boolean b = false;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("Update g_office set off_en=?,off_name=?,category=?, suffix=?, off_status=?, is_ddo=?, online_bill_submission=? where off_code=?");
            if (office.getOffEn() != null && !office.getOffEn().equals("")) {
                pst.setString(1, office.getOffEn().toUpperCase().trim());
                pst.setString(2, office.getOffEn().toUpperCase().trim());
            }
            pst.setString(3, office.getCategory());
            pst.setString(4, office.getSuffix());
            pst.setString(5, office.getOffStatus());
            pst.setString(6, office.getIsDdo());
            pst.setString(7, office.getOnlineBillSubmission());
            pst.setString(8, office.getHidOfficeCode());
            n = pst.executeUpdate();
            if (n == 1) {
                b = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return b;
    }

    @Override
    public int saveBacklogOffice(Office office) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String newbacklogOfcCode = null;
        int retVal = 0;
        try {
            con = this.dataSource.getConnection();
            String sql = "Select substr(off_code,1,9)backlogddocode from g_office where off_code=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, office.getHidOfficeCode());
            rs = ps.executeQuery();
            while (rs.next()) {
                String backlog_ddocode = rs.getString("backlogddocode");
                if (backlog_ddocode != null && !backlog_ddocode.equals("")) {
                    String sql1 = "select length(cast(cast(max(substr(off_code,10,13)) as integer)+1 as text))length_postfixoffcode,cast(max(substr(off_code,10,13)) as integer)+1 postfixOffCode "
                            + "from g_office where substr(off_code,1,9)=?";
                    ps = con.prepareStatement(sql1);
                    ps.setString(1, backlog_ddocode);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        int len_newNumeric = rs.getInt("length_postfixoffcode");
                        String newOfcNumeric = rs.getString("postfixOffCode");
                        if (len_newNumeric == 1) {
                            newbacklogOfcCode = backlog_ddocode + "000" + newOfcNumeric;
                        } else if (len_newNumeric == 2) {
                            newbacklogOfcCode = backlog_ddocode + "00" + newOfcNumeric;
                        } else if (len_newNumeric == 3) {
                            newbacklogOfcCode = backlog_ddocode + "0" + newOfcNumeric;
                        }

                    }
                    //System.out.println("office details:"+office.getOffEn()+";"+office.getCategory()+";"+office.getSuffix()+";"+office.getHidDeptCode()+";"+office.getHidDistCode());
                    ps = con.prepareStatement("INSERT INTO G_OFFICE(OFF_CODE,OFF_EN,OFF_NAME,CATEGORY,SUFFIX,DEPARTMENT_CODE,LVL,STATE_CODE,DIST_CODE,OFF_STATUS,IS_DDO,REF_OFF_CODE)"
                            + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
                    ps.setString(1, newbacklogOfcCode);
                    if (office.getOffEn() != null && !office.getOffEn().equals("")) {
                        ps.setString(2, office.getOffEn().toUpperCase().trim());
                        ps.setString(3, office.getOffEn().toUpperCase().trim());
                    }
                    ps.setString(4, office.getCategory());
                    ps.setString(5, office.getSuffix());
                    ps.setString(6, office.getHidDeptCode());
                    ps.setString(7, "18");
                    ps.setString(8, "21");
                    ps.setString(9, office.getHidDistCode());
                    ps.setString(10, "F");
                    ps.setString(11, "B");
                    ps.setString(12, office.getHidOfficeCode());
                    retVal = ps.executeUpdate();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();;
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return retVal;
    }

    @Override
    public int cntRefOffCode(String offCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        int noOfRefOffCode = 0;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("Select count(*) cntref from g_office where ref_off_code=?");
            pst.setString(1, offCode);
            rs = pst.executeQuery();
            if (rs.next()) {
                noOfRefOffCode = rs.getInt("cntref");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return noOfRefOffCode;
    }

    @Override
    public List getOfficeLevelList() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List officeLvllList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("Select * from g_off_level order by lvl_desc");
            rs = ps.executeQuery();
            while (rs.next()) {
                Office ofclvl = new Office();
                ofclvl.setLvl(rs.getString("lvl"));
                ofclvl.setLvlDesc(rs.getString("lvl_desc") + " (" + rs.getString("lvl") + ")");
                officeLvllList.add(ofclvl);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officeLvllList;
    }

    @Override
    public int saveNewOffice(Office office) {
        int retVal = 0;
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO G_OFFICE(OFF_CODE,OFF_EN,OFF_NAME,CATEGORY,SUFFIX,LVL,DEPARTMENT_CODE,DIST_CODE,STATE_CODE,IS_DDO,OFF_STATUS,ONLINE_BILL_SUBMISSION,DDO_CODE,P_OFF_CODE)"
                    + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            if (office.getOffCode() != null && !office.getOffCode().equals("")) {
                pst.setString(1, office.getOffCode());
            }
            if (office.getOffEn() != null && !office.getOffEn().equals("")) {
                pst.setString(2, office.getOffEn().toUpperCase());
            } else {
                pst.setString(2, office.getOffEn().toUpperCase());
            }
            if (office.getOffEn() != null && !office.getOffEn().equals("")) {
                pst.setString(3, office.getOffEn().toUpperCase());
            } else {
                pst.setString(3, office.getOffEn().toUpperCase());
            }

            if (office.getCategory() != null && !office.getCategory().equals("")) {
                pst.setString(4, office.getCategory().toUpperCase());
            } else {
                pst.setString(4, office.getCategory().toUpperCase());
            }
            pst.setString(5, office.getSuffix().toUpperCase());
            pst.setString(6, office.getLvl());
            if (office.getDeptCode() != null && !office.getDeptCode().equals("")) {
                pst.setString(7, office.getDeptCode());
            } else {
                pst.setString(7, office.getDeptCode());
            }
            if (office.getDistCode() != null && !office.getDistCode().equals("")) {
                pst.setString(8, office.getDistCode());
            } else {
                pst.setString(8, office.getDistCode());
            }
            pst.setString(9, office.getStateCode());
            if (office.getIsDdo() != null && !office.getIsDdo().equals("")) {
                pst.setString(10, office.getIsDdo());
            } else {
                pst.setString(10, office.getIsDdo());
            }
            pst.setString(11, office.getOffStatus());

            pst.setString(12, office.getOnlineBillSubmission());
            pst.setString(13, office.getDdoCode());

            pst.setString(14, office.getPoffCode());

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
    public String getMaxoffCode(String offCode) {
        String maxOffCode = null;
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            String sql = "Select substr(off_code,1,9)ddocode from g_office where off_code=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, offCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                String ddoCode = rs.getString("ddocode");
                if (ddoCode != null && !ddoCode.equals("")) {
                    String sql1 = "select length(cast(cast(max(substr(off_code,10,13)) as integer)+1 as text))length_postfixoffcode,cast(max(substr(off_code,10,13)) as integer)+1 postfixOffCode "
                            + "from g_office where substr(off_code,1,9)=?";
                    ps = con.prepareStatement(sql1);
                    ps.setString(1, ddoCode);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        int len_newNumeric = rs.getInt("length_postfixoffcode");
                        String newOfcNumeric = rs.getString("postfixOffCode");
                        if (len_newNumeric == 1) {
                            maxOffCode = ddoCode + "000" + newOfcNumeric;
                        } else if (len_newNumeric == 2) {
                            maxOffCode = ddoCode + "00" + newOfcNumeric;
                        } else if (len_newNumeric == 3) {
                            maxOffCode = ddoCode + "0" + newOfcNumeric;
                        }

                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return maxOffCode;
    }

    @Override
    public int saveBackLogOffice(Office office) {
        int c = 0;
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("INSERT INTO G_OFFICE (OFF_CODE,OFF_EN,OFF_NAME,CATEGORY,SUFFIX,OFF_STATUS,DEPARTMENT_CODE,DIST_CODE,P_OFF_CODE,LVL,IS_DDO,STATE_CODE)"
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, office.getMaxOfcCode());
            ps.setString(2, office.getOffEn());
            ps.setString(3, office.getOffEn());
            ps.setString(4, office.getCategory());
            ps.setString(5, office.getSuffix());
            ps.setString(6, office.getOffStatus());
            ps.setString(7, office.getHidDeptCode());
            ps.setString(8, office.getDistCode());
            ps.setString(9, office.getOffCode());
            ps.setString(10, office.getLvl());
            ps.setString(11, "B");
            ps.setString(12, "21");
            c = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return c;
    }

    @Override
    public Office getOfficeNameDeptName(String offCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Office ofc = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select off_code,off_en, g_department.department_code,department_name,g_district.dist_code,dist_name from \n"
                    + "g_office inner join g_department on g_office.department_code=g_department.department_code\n"
                    + "inner join g_district on g_office.dist_code=g_district.dist_code\n"
                    + "where off_code=?");
            ps.setString(1, offCode);
            rs = ps.executeQuery();
            if (rs.next()) {
                ofc = new Office();
                ofc.setDeptCode(rs.getString("DEPARTMENT_CODE"));
                ofc.setDeptName(rs.getString("DEPARTMENT_NAME"));
                ofc.setOffEn(rs.getString("OFF_EN"));
                ofc.setOffCode(rs.getString("OFF_CODE"));
                ofc.setDistCode(rs.getString("dist_code"));
                ofc.setDistName(rs.getString("dist_name")); 

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ofc;
    }

    @Override
    public List getDeptOfficeCode() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List deptddoList = new ArrayList();
        Office office = null;
        try {
            con = this.dataSource.getConnection();
            String sql = "SELECT department_name,g_department.department_code, ao_off_code FROM g_department WHERE g_department.if_active='Y'  ORDER BY department_code";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                office = new Office();
                office.setOffCode(rs.getString("ao_off_code"));
                office.setOffEn(rs.getString("department_name"));
                office.setDeptCode(rs.getString("department_code"));
                deptddoList.add(office);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deptddoList;
    }

    @Override
    public List getSuperannuatedDeptList(int year, int month, String acctType) {
        Connection con = null;
        ResultSet rs = null;
        List ofcList = new ArrayList();
        PreparedStatement ps = null;
        Office office = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select ao_off_code,cntemp,department_name from\n"
                    + "(select cur_off_code,count(*)cntemp,acct_type from emp_mast where EXTRACT(YEAR FROM DOS)=? AND EXTRACT(MONTH FROM DOS)=? AND ACCT_TYPE=?\n"
                    + " AND substr(cur_off_code,10,4)='0000'  \n"
                    + "group by cur_off_code,acct_type )emp\n"
                    + "inner join \n"
                    + "(SELECT department_name,g_department.department_code, ao_off_code FROM g_department WHERE g_department.if_active='Y' ORDER BY department_name)gd\n"
                    + "on emp.cur_off_code=gd.ao_off_code order by ao_off_code");
            ps.setInt(1, year);
            ps.setInt(2, month);
            ps.setString(3, acctType);
            rs = ps.executeQuery();
            while (rs.next()) {
                office = new Office();
                office.setOffCode(rs.getString("ao_off_code"));
                office.setOffEn(rs.getString("department_name"));
                ofcList.add(office);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ofcList;
    }

    @Override
    public List getSGOOfficeList() {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List officelist = new ArrayList();
        Office office = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT oth_spc,off_en FROM g_oth_spc WHERE oth_spc='SGO' and is_active='Y' order by off_en asc";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                office = new Office();
                office.setOffCode(rs.getString("oth_spc"));
                office.setOffName(rs.getString("off_en"));
                officelist.add(office);
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
    public List getGOIOfficeList(String category) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List officelist = new ArrayList();
        Office office = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT other_spc_id,off_en from g_oth_spc where oth_spc ='GOI' and category=? and is_active='Y' order by off_en asc";
            pst = con.prepareStatement(sql);
            pst.setString(1, category);
            rs = pst.executeQuery();
            while (rs.next()) {
                office = new Office();
                office.setOffCode(rs.getString("other_spc_id"));
                office.setOffName(rs.getString("off_en"));
                officelist.add(office);
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
    public List getInterStateOfficeList(String statecode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List officelist = new ArrayList();
        Office office = null;
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("SELECT off_code,off_en from g_office where state_code=? order by off_en");
            pst.setString(1, statecode);
            rs = pst.executeQuery();
            while (rs.next()) {
                office = new Office();
                office.setOffCode(rs.getString("off_code"));
                office.setOffName(rs.getString("off_en"));
                officelist.add(office);
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
    public List getOtherOrgOfficeList(String deptcode) {

        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List officelist = new ArrayList();
        Office office = null;
        try {
            con = dataSource.getConnection();
            if (deptcode != null && !deptcode.equals("O")) {
                pstmt = con.prepareStatement("SELECT other_spc_id,off_en FROM g_oth_spc WHERE oth_spc='SGO' and is_active='Y' and dept_name=? order by off_en asc");
                pstmt.setString(1, deptcode);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    office = new Office();
                    office.setOffCode(rs.getString("other_spc_id"));
                    office.setOffName(rs.getString("off_en"));
                    officelist.add(office);
                }
            } else if (deptcode != null && deptcode.equals("O")) {
                pstmt = con.prepareStatement("select other_spc_id,off_en from g_oth_spc where oth_spc='SGO' and (dept_name is null or dept_name='')");
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    office = new Office();
                    office.setOffCode(rs.getString("other_spc_id"));
                    office.setOffName(rs.getString("off_en"));
                    officelist.add(office);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officelist;
    }

    @Override
    public List getPoliceStations(String distcode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        List policeSationList = new ArrayList();
        Office office = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select * from g_office where department_code='14' and ((lvl='31' and is_ddo='N' and dist_code=?) or \n"
                    + "(is_ddo='Y' and dist_code=? and off_status='F')) order by lvl,off_en");
            pst.setString(1, distcode);
            pst.setString(2, distcode);
            rs = pst.executeQuery();
            while (rs.next()) {
                office = new Office();
                office.setOffEn(rs.getString("off_en"));
                office.setOffCode(rs.getString("off_code"));
                policeSationList.add(office);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return policeSationList;
    }

    @Override
    public List getBillGroupPrivilegeList(String offCode) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List billgrpList = new ArrayList();
        Office ofc = null;
        try {
            System.out.println("print");
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select g_spc.spn from \n"
                    + "(select DISTINCT SPC from bill_group_privilage where off_code=?)billGrprivilage\n"
                    + "inner join g_spc on billGrprivilage.spc=g_spc.spc ORDER BY SPN");
            pst.setString(1, offCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                ofc = new Office();
                ofc.setBillGrpSpn(rs.getString("spn"));
                billgrpList.add(ofc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billgrpList;
    }

    @Override
    public void deleteBillGroupPrivilegeList(String offCode) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("Delete from bill_group_privilage where off_code=?");
            pst.setString(1, offCode);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public int hasOfficePriv(String spc) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        int noofPriv = 0;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select count(*)noOfprivilege from g_privilege_map where spc=? and mod_id in ('137','138','176')");
            ps.setString(1, spc);
            rs = ps.executeQuery();
            if (rs.next()) {
                noofPriv = rs.getInt("noOfprivilege");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return noofPriv;
    }
}

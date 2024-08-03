/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.tab;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.SMSServices;
import hrms.model.login.LoginUserBean;
import hrms.model.login.UserExpertise;
import hrms.model.tab.CadreTreeAttr;
import hrms.model.tab.DepartmentTreeAttr;
import hrms.model.tab.DistrictTreeAttr;
import hrms.model.tab.EmployeeTree;
import hrms.model.tab.OfficeHelper;
import hrms.model.tab.OfficeTreeAttr;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Surendra
 */
public class TabDAOImpl implements TabDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;
    protected DataSource repodataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setRepodataSource(DataSource repodataSource) {
        this.repodataSource = repodataSource;
    }

    @Override
    public List getOfficeListXML(String empid) {
        Connection con = null;
        List li = new ArrayList();
        OfficeTreeAttr ofattr = null;
        OfficeHelper ofh = null;
        Statement st = null;
        ResultSet rs = null;
        String officeName = "";
        try {
            con = this.repodataSource.getConnection();
            st = con.createStatement();
            ArrayList currentOffices = getCurrentOffice(empid);

            for (int l = 0; l < currentOffices.size(); l++) {
                ArrayList regList = new ArrayList();

                ofattr = new OfficeTreeAttr();
                ofh = (OfficeHelper) currentOffices.get(l);

                EmployeeTree regular = new EmployeeTree();
                regular.setId("REG" + ofh.getPostCode());
                regular.setText("REGULAR EMPLOYEES");
                regular.setState("open");
                regular.setIconCls("office.png");
                regular.setChildren(getEmployeeList(ofh.getOfficeCode(), empid));

                regList.add(regular);

                ArrayList cont6Years = getContractualEmployeeList6Years(ofh.getOfficeCode());

                if (cont6Years.size() > 0) {

                    EmployeeTree contractual = new EmployeeTree();
                    contractual.setId("6CONT" + ofh.getPostCode());
                    contractual.setText("CONTRACTUAL (6 YEARS) EMPLOYEES");
                    contractual.setState("open");
                    contractual.setIconCls("office.png");
                    contractual.setChildren(cont6Years);

                    regList.add(contractual);
                }

                ArrayList workchargedList = getWorkchargedEmployeeList(ofh.getOfficeCode());

                if (workchargedList.size() > 0) {

                    EmployeeTree workcharged = new EmployeeTree();
                    workcharged.setId("WOR" + ofh.getPostCode());
                    workcharged.setText("WORKCHARGED EMPLOYEES (REGULAR SALARY)");
                    workcharged.setState("open");
                    workcharged.setIconCls("office.png");
                    workcharged.setChildren(workchargedList);

                    regList.add(workcharged);
                }

                ArrayList wagesList = getWagesEmployeeList(ofh.getOfficeCode());

                if (wagesList.size() > 0) {

                    EmployeeTree wages = new EmployeeTree();
                    wages.setId("WAG" + ofh.getPostCode());
                    wages.setText("WAGES EMPLOYEES (REGULAR SALARY)");
                    wages.setState("open");
                    wages.setIconCls("office.png");
                    wages.setChildren(wagesList);

                    regList.add(wages);
                }

                ArrayList al = getContractualEmployeeList(ofh.getOfficeCode(), empid);
                if (al.size() > 0) {

                    EmployeeTree contractual = new EmployeeTree();
                    contractual.setId("CON" + ofh.getPostCode());
                    contractual.setText("CONTRACTUAL EMPLOYEES");
                    contractual.setState("open");
                    contractual.setIconCls("office.png");
                    contractual.setChildren(al);

                    regList.add(contractual);
                }

                ArrayList ngaEmpList = getNonGovtAidedEmployeeList(ofh.getOfficeCode(), empid);
                if (ngaEmpList.size() > 0) {

                    EmployeeTree nongovtaided = new EmployeeTree();
                    nongovtaided.setId("NGA" + ofh.getPostCode());
                    nongovtaided.setText("NON GOVT. AIDED EMPLOYEES");
                    nongovtaided.setState("open");
                    nongovtaided.setIconCls("office.png");
                    nongovtaided.setChildren(ngaEmpList);

                    regList.add(nongovtaided);
                }

                ArrayList statteal = getLevelVExCadreEmployeeList(ofh.getOfficeCode());
                if (statteal.size() > 0) {

                    EmployeeTree excadreContract = new EmployeeTree();
                    excadreContract.setId("XCAD" + ofh.getPostCode());
                    excadreContract.setText("Level-V (Ex-Cadre)");
                    excadreContract.setState("open");
                    excadreContract.setIconCls("office.png");
                    excadreContract.setChildren(statteal);

                    regList.add(excadreContract);
                }

                ArrayList retList = getRetiredEmployeeList(ofh.getOfficeCode());
                if (retList.size() > 0) {

                    EmployeeTree retEmp = new EmployeeTree();
                    retEmp.setId("RET" + ofh.getPostCode());
                    retEmp.setText("RETIRED EMPLOYEES");
                    retEmp.setState("open");
                    retEmp.setIconCls("office.png");
                    //retEmp.setAttributes("RETIRED");
                    retEmp.setChildren(retList);

                    regList.add(retEmp);
                }
                ArrayList deceasedList = getDeceasedEmployeeList(ofh.getOfficeCode());
                if (deceasedList.size() > 0) {
                    //System.out.println("decsize:" + deceasedList.size());

                    EmployeeTree decEmplist = new EmployeeTree();
                    decEmplist.setId("DEC" + ofh.getPostCode());
                    decEmplist.setText("DECEASED EMPLOYEES");
                    decEmplist.setState("open");
                    decEmplist.setIconCls("office.png");
                    //decEmp.setAttributes("DECEASED");
                    decEmplist.setChildren(deceasedList);

                    regList.add(decEmplist);
                }
                ArrayList resignedList = getResignedEmployeeList(ofh.getOfficeCode());
                if (resignedList.size() > 0) {
                    //System.out.println("resgsize:" + resignedList.size());

                    EmployeeTree resEmplist = new EmployeeTree();
                    resEmplist.setId("RESG" + ofh.getPostCode());
                    resEmplist.setText("RESIGNED EMPLOYEES");
                    resEmplist.setState("open");
                    resEmplist.setIconCls("office.png");
                    //decEmp.setAttributes("RESIGNED");
                    resEmplist.setChildren(resignedList);

                    regList.add(resEmplist);
                }
                ArrayList spUsrCatgList = getSpecialUserCatgList(ofh.getOfficeCode());
                ArrayList splEmpList = getSpecialcategoryEmpList(ofh.getOfficeCode());

                EmployeeTree spcatgEmplist = new EmployeeTree();
                if (splEmpList.size() > 0) {
                    ArrayList speclCatg = new ArrayList();
                    Iterator itr = spUsrCatgList.iterator();
                    spcatgEmplist.setText("SPECIAL CATEGORY EMPLOYEES");
                    spcatgEmplist.setState("open");
                    //spcatgEmplist.setIconCls("office.png");
                    //spcatgEmplist.setChildren(spUsrCatgList);
                    while (itr.hasNext()) {
                        EmployeeTree subCatgList = new EmployeeTree();
                        OfficeTreeAttr ofctr = (OfficeTreeAttr) itr.next();
                        subCatgList.setText(ofctr.getText());
                        subCatgList.setState("Open");
                        //subCatgList.setIconCls("office.png");
                        subCatgList.setChildren(getSubUserCatgList(ofh.getOfficeCode(), ofctr.getText()));

                        speclCatg.add(subCatgList);
                        spcatgEmplist.setChildren(speclCatg);
                    }
                    regList.add(spcatgEmplist);
                }
                if (ofh.getOfflvl().equals("01")) {
                    ArrayList deputationEmplist = getdeputationEmployeeList(ofh.getDeptcode());
                    if (deputationEmplist.size() > 0) {
                        EmployeeTree depemplist = new EmployeeTree();
                        depemplist.setId("FSC" + ofh.getPostCode());
                        depemplist.setText("DEPUTATION EMPLOYEES");
                        depemplist.setState("open");
                        depemplist.setIconCls("office.png");
                        //decEmp.setAttributes("RESIGNED");
                        depemplist.setChildren(deputationEmplist);
                        regList.add(depemplist);
                    }

                }

                getSubOfficeListXML(ofh.getOfficeCode(), ofh.getPostCode(), regList);

                officeName = ofh.getOfficeName();
                ofattr.setId("PA" + CommonFunctions.encodedTxt(ofh.getPostCode()));
                ofattr.setText(officeName);
                ofattr.setState("close");
                ofattr.setIconCls("office.png");
                ofattr.setChildren(regList);

                li.add(ofattr);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;

    }

    @Override
    public void getSubOfficeListXML(String ddoCode, String spc, ArrayList pOffObject) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        OfficeTreeAttr ofattr = null;
        try {
            con = this.repodataSource.getConnection();
            ps = con.prepareStatement("select off_code, off_en from g_office where p_off_code=? and is_ddo <>'Y'");
            ps.setString(1, ddoCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("***************");
                ofattr = new OfficeTreeAttr();
                ofattr.setId("SO" + CommonFunctions.encodedTxt(rs.getString("off_code")));
                ofattr.setText(rs.getString("off_en"));
                ofattr.setState("open");
                ofattr.setIconCls("icon-office");
                //ofattr.setChildren(regList);
                pOffObject.add(ofattr);
            }

            /*
             pstmt = con.prepareStatement("SELECT OFF_CODE,OFF_EN FROM G_OFFICE WHERE P_OFF_CODE=? ORDER BY OFF_EN");
             pstmt.setString(1, parentOfficeCode);
             res = pstmt.executeQuery();
             while (res.next()) {
             OfficeTreeAttr childOfattr = new OfficeTreeAttr();
             childOfattr.setId(res.getString("OFF_CODE"));
             childOfattr.setText(res.getString("OFF_EN"));
             childOfattr.setIconCls("icon-office");
             childlist.add(childOfattr);
             }
             ofattr.setChildren(childlist);
             */
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public ArrayList getWorkchargedEmployeeList(String parentOfficeCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String sqlQuery = "";
        ArrayList al = new ArrayList();
        try {
            con = this.repodataSource.getConnection();
            sqlQuery = " SELECT SPC,POST_LEVEL,SPN,DEP_CODE,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULLNAME,F_NAME,EMP_ID,IS_REGULAR,BRASS_NO,POST FROM(SELECT SPC,POST_LEVEL,SPN,GPC FROM G_SPC WHERE OFF_CODE = ? AND (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) )G_SPC "
                    + "  LEFT OUTER JOIN EMP_MAST ON G_SPC.SPC = EMP_MAST.CUR_SPC "
                    + "  LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE WHERE EMP_ID IS NOT NULL  AND IS_REGULAR='W'"
                    + "  UNION "
                    + "  SELECT * FROM (SELECT '' SPC,0 POST_LEVEL,'' SPN,DEP_CODE,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULLNAME,F_NAME,EMP_ID,IS_REGULAR,BRASS_NO,'' POST FROM EMP_MAST WHERE (CUR_OFF_CODE =?  OR NEXT_OFFICE_CODE=? ) AND IS_REGULAR='W' AND (CUR_SPC IS NULL OR CUR_SPC='') AND (IF_RETIRED IS NULL OR IF_RETIRED='N') ) EMP_MAST ORDER BY F_NAME";
            ps = con.prepareStatement(sqlQuery);
            ps.setString(1, parentOfficeCode);
            ps.setString(2, parentOfficeCode);
            ps.setString(3, parentOfficeCode);
            ResultSet res = ps.executeQuery();
            EmployeeTree empTree = null;
            while (res.next()) {
                empTree = new EmployeeTree();
                empTree.setId("EMP@WORK-" + res.getString("EMP_ID"));
                if (res.getString("BRASS_NO") != null && !res.getString("BRASS_NO").equals("")) {
                    empTree.setText(res.getString("BRASS_NO") + ", " + StringUtils.defaultString(res.getString("FULLNAME")).toUpperCase() + " " + StringUtils.defaultString(res.getString("POST")).replaceAll("&", " AND "));
                } else {
                    empTree.setText(StringUtils.defaultString(res.getString("FULLNAME")).toUpperCase() + " , " + StringUtils.defaultString(res.getString("POST")).replaceAll("&", " AND "));
                }
                if (res.getString("DEP_CODE") != null && !res.getString("DEP_CODE").equals("")) {
                    if (res.getString("DEP_CODE").equalsIgnoreCase("00")) { // TERMINATED
                        empTree.setIconCls("icon-terminate");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("01")) { // LONG TERM LEAVE
                        empTree.setIconCls("icon-onltLeave");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("02")) {// ON DUTY
                        empTree.setIconCls("icon-onDuty");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("03")) {// WAITING FOR POSTING
                        empTree.setIconCls("icon-watingforpost");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("04")) {// ON TRAINING
                        empTree.setIconCls("icon-lttrain");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("05")) {// UNDER SUSPENSION
                        empTree.setIconCls("icon-suspension");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("06")) {// ON TRANSIT
                        empTree.setIconCls("icon-ontransit");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("07")) {// ON DEPUTATION
                        empTree.setIconCls("icon-ondeputation");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("08")) {// RESIGNED
                        empTree.setIconCls("icon-resigned");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("09")) {// SUPERANNUATED
                        empTree.setIconCls("icon-retired");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("10")) {// DECEASED
                        empTree.setIconCls("icon-retired");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("11")) {// PAY HELDUP 
                        empTree.setIconCls("icon-suspension");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("12")) {// ON REDEPLOYMENT
                        empTree.setIconCls("icon-onDuty");
                    }
                } else {
                    empTree.setIconCls("icon-watingforpost");
                }
                empTree.setState("close");
                al.add(empTree);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public ArrayList getWagesEmployeeList(String parentOfficeCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String sqlQuery = "";
        ArrayList al = new ArrayList();
        try {
            con = this.repodataSource.getConnection();
            sqlQuery = " SELECT SPC,POST_LEVEL,SPN,DEP_CODE,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULLNAME,F_NAME,EMP_ID,IS_REGULAR,BRASS_NO,POST FROM(SELECT SPC,POST_LEVEL,SPN,GPC FROM G_SPC WHERE OFF_CODE = ? AND (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) )G_SPC "
                    + "  LEFT OUTER JOIN EMP_MAST ON G_SPC.SPC = EMP_MAST.CUR_SPC "
                    + "  LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE WHERE EMP_ID IS NOT NULL  AND IS_REGULAR='G'"
                    + "  UNION "
                    + "  SELECT * FROM (SELECT '' SPC,0 POST_LEVEL,'' SPN,DEP_CODE,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULLNAME,F_NAME,EMP_ID,IS_REGULAR,BRASS_NO,'' POST FROM EMP_MAST WHERE (CUR_OFF_CODE =?  OR NEXT_OFFICE_CODE=? ) AND IS_REGULAR='G' AND (CUR_SPC IS NULL OR CUR_SPC='') AND (IF_RETIRED IS NULL OR IF_RETIRED='N') ) EMP_MAST ORDER BY F_NAME";
            ps = con.prepareStatement(sqlQuery);
            ps.setString(1, parentOfficeCode);
            ps.setString(2, parentOfficeCode);
            ps.setString(3, parentOfficeCode);
            ResultSet res = ps.executeQuery();
            EmployeeTree empTree = null;
            while (res.next()) {
                empTree = new EmployeeTree();
                empTree.setId("EMP@WORK-" + res.getString("EMP_ID"));
                if (res.getString("BRASS_NO") != null && !res.getString("BRASS_NO").equals("")) {
                    empTree.setText(res.getString("BRASS_NO") + ", " + StringUtils.defaultString(res.getString("FULLNAME")).toUpperCase() + " " + StringUtils.defaultString(res.getString("POST")).replaceAll("&", " AND "));
                } else {
                    empTree.setText(StringUtils.defaultString(res.getString("FULLNAME")).toUpperCase() + " , " + StringUtils.defaultString(res.getString("POST")).replaceAll("&", " AND "));
                }
                if (res.getString("DEP_CODE") != null && !res.getString("DEP_CODE").equals("")) {
                    if (res.getString("DEP_CODE").equalsIgnoreCase("00")) { // TERMINATED
                        empTree.setIconCls("icon-terminate");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("01")) { // LONG TERM LEAVE
                        empTree.setIconCls("icon-onltLeave");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("02")) {// ON DUTY
                        empTree.setIconCls("icon-onDuty");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("03")) {// WAITING FOR POSTING
                        empTree.setIconCls("icon-watingforpost");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("04")) {// ON TRAINING
                        empTree.setIconCls("icon-lttrain");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("05")) {// UNDER SUSPENSION
                        empTree.setIconCls("icon-suspension");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("06")) {// ON TRANSIT
                        empTree.setIconCls("icon-ontransit");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("07")) {// ON DEPUTATION
                        empTree.setIconCls("icon-ondeputation");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("08")) {// RESIGNED
                        empTree.setIconCls("icon-resigned");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("09")) {// SUPERANNUATED
                        empTree.setIconCls("icon-retired");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("10")) {// DECEASED
                        empTree.setIconCls("icon-retired");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("11")) {// PAY HELDUP 
                        empTree.setIconCls("icon-suspension");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("12")) {// ON REDEPLOYMENT
                        empTree.setIconCls("icon-onDuty");
                    }
                } else {
                    empTree.setIconCls("icon-watingforpost");
                }
                empTree.setState("close");
                al.add(empTree);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public ArrayList getContractualEmployeeList6Years(String parentOfficeCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String sqlQuery = "";
        ArrayList al = new ArrayList();
        try {
            con = this.repodataSource.getConnection();
            sqlQuery = "SELECT SPC,POST_LEVEL,SPN,DEP_CODE,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULLNAME,F_NAME,EMP_ID,IS_REGULAR,\n"
                    + "BRASS_NO,POST FROM(SELECT SPC,POST_LEVEL,SPN,GPC FROM G_SPC WHERE OFF_CODE = ? \n"
                    + "AND (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) )G_SPC\n"
                    + "LEFT OUTER JOIN EMP_MAST ON G_SPC.SPC = EMP_MAST.CUR_SPC \n"
                    + "LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE WHERE EMP_ID IS NOT NULL  \n"
                    + "AND (CUR_OFF_CODE =?  OR NEXT_OFFICE_CODE=? ) \n"
                    + "AND IS_REGULAR='C'\n"
                    + "UNION\n"
                    + "SELECT * FROM (SELECT '' SPC,0 POST_LEVEL,'' SPN,DEP_CODE,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULLNAME,\n"
                    + "F_NAME,EMP_ID,IS_REGULAR,BRASS_NO,'' POST FROM EMP_MAST WHERE (CUR_OFF_CODE =?  OR NEXT_OFFICE_CODE=? ) \n"
                    + "AND IS_REGULAR='C' AND (CUR_SPC IS NULL OR CUR_SPC='') AND \n"
                    + "(IF_RETIRED IS NULL OR IF_RETIRED='N') ) EMP_MAST ORDER BY F_NAME";
            ps = con.prepareStatement(sqlQuery);
            ps.setString(1, parentOfficeCode);
            ps.setString(2, parentOfficeCode);
            ps.setString(3, parentOfficeCode);
            ps.setString(4, parentOfficeCode);
            ps.setString(5, parentOfficeCode);
            ResultSet res = ps.executeQuery();
            EmployeeTree empTree = null;
            while (res.next()) {
                empTree = new EmployeeTree();
                empTree.setId("EMP@6CONT-" + res.getString("EMP_ID"));
                if (res.getString("BRASS_NO") != null && !res.getString("BRASS_NO").equals("")) {
                    empTree.setText(res.getString("BRASS_NO") + ", " + StringUtils.defaultString(res.getString("FULLNAME")).toUpperCase() + " " + StringUtils.defaultString(res.getString("POST")).replaceAll("&", " AND "));
                } else {
                    empTree.setText(StringUtils.defaultString(res.getString("FULLNAME")).toUpperCase() + " , " + StringUtils.defaultString(res.getString("POST")).replaceAll("&", " AND "));
                }
                if (res.getString("DEP_CODE") != null && !res.getString("DEP_CODE").equals("")) {
                    if (res.getString("DEP_CODE").equalsIgnoreCase("00")) { // TERMINATED
                        empTree.setIconCls("icon-terminate");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("01")) { // LONG TERM LEAVE
                        empTree.setIconCls("icon-onltLeave");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("02")) {// ON DUTY
                        empTree.setIconCls("icon-onDuty");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("03")) {// WAITING FOR POSTING
                        empTree.setIconCls("icon-watingforpost");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("04")) {// ON TRAINING
                        empTree.setIconCls("icon-lttrain");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("05")) {// UNDER SUSPENSION
                        empTree.setIconCls("icon-suspension");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("06")) {// ON TRANSIT
                        empTree.setIconCls("icon-ontransit");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("07")) {// ON DEPUTATION
                        empTree.setIconCls("icon-ondeputation");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("08")) {// RESIGNED
                        empTree.setIconCls("icon-resigned");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("09")) {// SUPERANNUATED
                        empTree.setIconCls("icon-retired");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("10")) {// DECEASED
                        empTree.setIconCls("icon-retired");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("11")) {// PAY HELDUP 
                        empTree.setIconCls("icon-suspension");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("12")) {// ON REDEPLOYMENT
                        empTree.setIconCls("icon-onDuty");
                    }
                } else {
                    empTree.setIconCls("icon-watingforpost");
                }
                empTree.setState("close");
                al.add(empTree);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public ArrayList getContractualEmployeeList(String parentOfficeCode, String empId) {
        Connection con = null;
        ResultSet res = null;
        PreparedStatement ps = null;
        ArrayList templist = new ArrayList();
        EmployeeTree empTree = new EmployeeTree();
        try {
            con = this.repodataSource.getConnection();
            ps = con.prepareStatement("SELECT EMP_MAST.EMP_ID, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME ,F_NAME,POST_NOMENCLATURE, dep_code,"
                    + " if_reengaged,if_retired,(select retired from emp_ret_res where emp_id=EMP_MAST.EMP_ID order by doe desc limit 1)retired FROM EMP_MAST"
                    + " WHERE (CUR_OFF_CODE = '" + parentOfficeCode + "'  OR NEXT_OFFICE_CODE='" + parentOfficeCode + "') AND IS_REGULAR ='N' ORDER BY F_NAME");

            res = ps.executeQuery();
            while (res.next()) {
                empTree = new EmployeeTree();
                empTree.setId("EMP@CONT-" + res.getString("EMP_ID"));
                if (res.getString("FULL_NAME") != null && !res.getString("FULL_NAME").equals("")) {
                    empTree.setText(res.getString("FULL_NAME").toUpperCase() + " , " + StringUtils.defaultString(res.getString("POST_NOMENCLATURE")).replaceAll("&", " AND "));
                } else {
                    empTree.setText(res.getString("FULL_NAME") + " , " + StringUtils.defaultString(res.getString("POST_NOMENCLATURE")).replaceAll("&", " AND "));
                }
                if (res.getString("DEP_CODE") != null && !res.getString("DEP_CODE").equals("")) {
                    if (res.getString("DEP_CODE").equalsIgnoreCase("00")) { // TERMINATED
                        empTree.setIconCls("icon-terminate");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("01")) { // LONG TERM LEAVE
                        empTree.setIconCls("icon-onltLeave");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("02")) {// ON DUTY
                        empTree.setIconCls("icon-onDuty");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("03")) {// WAITING FOR POSTING
                        empTree.setIconCls("icon-watingforpost");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("04")) {// ON TRAINING
                        empTree.setIconCls("icon-lttrain");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("05")) {// UNDER SUSPENSION
                        empTree.setIconCls("icon-suspension");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("06")) {// ON TRANSIT
                        empTree.setIconCls("icon-ontransit");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("07")) {// ON DEPUTATION
                        empTree.setIconCls("icon-ondeputation");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("08")) {// RESIGNED
                        empTree.setIconCls("icon-resigned");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("09")) {// SUPERANNUATED
                        empTree.setIconCls("icon-retired");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("10")) {// DECEASED
                        empTree.setIconCls("icon-retired");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("11")) {// PAY HELDUP 
                        empTree.setIconCls("icon-suspension");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("12")) {// ON REDEPLOYMENT
                        empTree.setIconCls("icon-onDuty");
                    }
                } else {
                    empTree.setIconCls("icon-watingforpost");
                }
                empTree.setState("close");

                templist.add(empTree);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return templist;
    }

    @Override
    public ArrayList getNonGovtAidedEmployeeList(String parentOfficeCode, String empId) {
        Connection con = null;
        ResultSet res = null;
        PreparedStatement ps = null;
        ArrayList templist = new ArrayList();
        EmployeeTree empTree = new EmployeeTree();
        try {
            con = this.repodataSource.getConnection();
            ps = con.prepareStatement("SELECT EMP_ID, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME ,F_NAME,POST_NOMENCLATURE, dep_code FROM EMP_MAST "
                    + " WHERE (CUR_OFF_CODE = '" + parentOfficeCode + "'  OR NEXT_OFFICE_CODE='" + parentOfficeCode + "') AND IS_REGULAR ='D' ORDER BY F_NAME");

            res = ps.executeQuery();
            while (res.next()) {
                empTree = new EmployeeTree();
                empTree.setId("EMP@NGA-" + res.getString("EMP_ID"));
                if (res.getString("FULL_NAME") != null && !res.getString("FULL_NAME").equals("")) {
                    empTree.setText(res.getString("FULL_NAME").toUpperCase() + " , " + StringUtils.defaultString(res.getString("POST_NOMENCLATURE")).replaceAll("&", " AND "));
                } else {
                    empTree.setText(res.getString("FULL_NAME") + " , " + StringUtils.defaultString(res.getString("POST_NOMENCLATURE")).replaceAll("&", " AND "));
                }
                if (res.getString("DEP_CODE") != null && !res.getString("DEP_CODE").equals("")) {
                    if (res.getString("DEP_CODE").equalsIgnoreCase("00")) { // TERMINATED
                        empTree.setIconCls("icon-terminate");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("01")) { // LONG TERM LEAVE
                        empTree.setIconCls("icon-onltLeave");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("02")) {// ON DUTY
                        empTree.setIconCls("icon-onDuty");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("03")) {// WAITING FOR POSTING
                        empTree.setIconCls("icon-watingforpost");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("04")) {// ON TRAINING
                        empTree.setIconCls("icon-lttrain");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("05")) {// UNDER SUSPENSION
                        empTree.setIconCls("icon-suspension");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("06")) {// ON TRANSIT
                        empTree.setIconCls("icon-ontransit");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("07")) {// ON DEPUTATION
                        empTree.setIconCls("icon-ondeputation");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("08")) {// RESIGNED
                        empTree.setIconCls("icon-resigned");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("09")) {// SUPERANNUATED
                        empTree.setIconCls("icon-retired");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("10")) {// DECEASED
                        empTree.setIconCls("icon-retired");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("11")) {// PAY HELDUP 
                        empTree.setIconCls("icon-suspension");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("12")) {// ON REDEPLOYMENT
                        empTree.setIconCls("icon-onDuty");
                    }
                } else {
                    empTree.setIconCls("icon-watingforpost");
                }
                empTree.setState("close");
                templist.add(empTree);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return templist;
    }

    @Override
    public ArrayList getLevelVExCadreEmployeeList(String parentOfficeCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ArrayList templist = new ArrayList();
        EmployeeTree empTree = new EmployeeTree();
        try {
            con = this.repodataSource.getConnection();
            ps = con.prepareStatement("SELECT EMP_ID, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME ,F_NAME,POST_NOMENCLATURE FROM EMP_MAST WHERE CUR_OFF_CODE=? AND IS_REGULAR ='B' ORDER BY F_NAME");
            ps.setString(1, parentOfficeCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                empTree = new EmployeeTree();
                empTree.setId("EMP@XCAD-" + rs.getString("EMP_ID"));
                if (rs.getString("FULL_NAME") != null && !rs.getString("FULL_NAME").equals("")) {
                    empTree.setText(rs.getString("FULL_NAME").toUpperCase() + " , " + StringUtils.defaultString(rs.getString("POST_NOMENCLATURE")).replaceAll("&", " AND "));
                } else {
                    empTree.setText(rs.getString("FULL_NAME") + " , " + StringUtils.defaultString(rs.getString("POST_NOMENCLATURE")).replaceAll("&", " AND "));
                }
                empTree.setIconCls("icon-onDuty");
                empTree.setState("close");
                templist.add(empTree);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return templist;
    }

    @Override
    public ArrayList getRetiredEmployeeList(String parentOfficeCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ArrayList templist = new ArrayList();
        EmployeeTree empTree = new EmployeeTree();
        try {
            con = this.repodataSource.getConnection();
            ps = con.prepareStatement("SELECT EMP_MAST.EMP_ID, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME ,F_NAME FROM EMP_MAST  "
                    + " WHERE CUR_OFF_CODE=? AND dep_code='09' and if_retired='Y' ORDER BY F_NAME");
            ps.setString(1, parentOfficeCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                empTree = new EmployeeTree();
                empTree.setId("EMP@RET-" + rs.getString("EMP_ID"));
                if (rs.getString("FULL_NAME") != null && !rs.getString("FULL_NAME").equals("")) {
                    empTree.setText(rs.getString("FULL_NAME").toUpperCase().replaceAll("&", " AND "));
                } else {
                    empTree.setText(rs.getString("FULL_NAME").replaceAll("&", " AND "));
                }
                empTree.setIconCls("icon-retired");
                empTree.setState("close");
                templist.add(empTree);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps, con);
        }
        return templist;
    }

    public ArrayList getEmployeeList(String parentOfficeCode, String empId) throws Exception {
        Connection con = null;
        ArrayList al = new ArrayList();
        Statement stamt2 = null;
        Statement stamt = null;
        ResultSet res = null;
        ResultSet rs2 = null;
        try {
            con = this.repodataSource.getConnection();

            stamt = con.createStatement();

            String biigrpId = "";
            String sqlQuery = "";

            boolean separateBillMapped = false;
            stamt2 = con.createStatement();

            rs2 = stamt2.executeQuery("SELECT BILL_GRP_ID FROM BILL_GROUP_PRIVILAGE WHERE SPC=(SELECT CUR_SPC FROM EMP_MAST WHERE EMP_ID='" + empId + "')");
            while (rs2.next()) {
                separateBillMapped = true;
                biigrpId = rs2.getString("BILL_GRP_ID");

                if (biigrpId != null && !biigrpId.equals("")) {
                    sqlQuery = "SELECT * FROM ( "
                            + "SELECT SPC,POST_LEVEL,SPN,DEP_CODE,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULLNAME,F_NAME,EMP_ID,POST,BRASS_NO,USERTYPE FROM ( "
                            + "SELECT G_SPC.SPC,POST_LEVEL,SPN,GPC FROM ( "
                            + "SELECT SPC FROM  ( "
                            + "SELECT SECTION_ID FROM BILL_SECTION_MAPPING  WHERE BILL_GROUP_ID ='" + biigrpId + "') BILL_SECTION_MAPPING "
                            + "INNER JOIN SECTION_POST_MAPPING ON BILL_SECTION_MAPPING.SECTION_ID=SECTION_POST_MAPPING.SECTION_ID) SECTION_POST_MAPPING "
                            + "INNER JOIN (SELECT SPC,POST_LEVEL,SPN,GPC FROM G_SPC WHERE OFF_CODE = '" + parentOfficeCode + "' AND (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) )G_SPC ON SECTION_POST_MAPPING.SPC=G_SPC.SPC ) G_SPC "
                            + "LEFT OUTER JOIN EMP_MAST ON G_SPC.SPC = EMP_MAST.CUR_SPC  "
                            + "LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                            + "UNION "
                            + "SELECT '' SPC,0 POST_LEVEL,'' SPN,DEP_CODE,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULLNAME,F_NAME,EMP_ID,'' POST,BRASS_NO,USERTYPE FROM EMP_MAST "
                            + "WHERE (CUR_OFF_CODE = '" + parentOfficeCode + "'  OR NEXT_OFFICE_CODE='" + parentOfficeCode + "') AND "
                            + "(CUR_SPC IS NULL OR CUR_SPC='') AND (IF_RETIRED IS NULL OR IF_RETIRED='N')  "
                            + "AND (IS_REGULAR!='N' AND IS_REGULAR!='C' AND IS_REGULAR!='B' AND IS_REGULAR!='W' AND IS_REGULAR!='G' AND IS_REGULAR!='D' AND IS_REGULAR!='A') "
                            + ") EMP_MAST ORDER BY F_NAME";

                    res = stamt.executeQuery(sqlQuery);
                    EmployeeTree empTree = null;
                    while (res.next()) {
                        empTree = new EmployeeTree();
                        empTree.setId("EMP@REG-" + res.getString("EMP_ID"));
                        if (res.getString("BRASS_NO") != null && !res.getString("BRASS_NO").equals("")) {
                            empTree.setText(res.getString("BRASS_NO") + ", " + res.getString("FULLNAME").toUpperCase() + " , " + StringUtils.defaultString(res.getString("POST")).replaceAll("&", " AND "));
                        } else {
                            empTree.setText(res.getString("FULLNAME").toUpperCase() + " , " + StringUtils.defaultString(res.getString("POST")).replaceAll("&", " AND "));
                        }

                        if (res.getString("DEP_CODE") != null && !res.getString("DEP_CODE").equals("")) {
                            if (res.getString("DEP_CODE").equalsIgnoreCase("00")) { // TERMINATED
                                empTree.setIconCls("icon-terminate");
                            } else if (res.getString("DEP_CODE").equalsIgnoreCase("01")) { // LONG TERM LEAVE
                                empTree.setIconCls("icon-onltLeave");
                            } else if (res.getString("DEP_CODE").equalsIgnoreCase("02")) {// ON DUTY
                                empTree.setIconCls("icon-onDuty");
                            } else if (res.getString("DEP_CODE").equalsIgnoreCase("03")) {// WAITING FOR POSTING
                                empTree.setIconCls("icon-watingforpost");
                            } else if (res.getString("DEP_CODE").equalsIgnoreCase("04")) {// ON TRAINING
                                empTree.setIconCls("icon-lttrain");
                            } else if (res.getString("DEP_CODE").equalsIgnoreCase("05")) {// UNDER SUSPENSION
                                empTree.setIconCls("icon-suspension");
                            } else if (res.getString("DEP_CODE").equalsIgnoreCase("06")) {// ON TRANSIT
                                empTree.setIconCls("icon-ontransit");
                            } else if (res.getString("DEP_CODE").equalsIgnoreCase("07")) {// ON DEPUTATION
                                empTree.setIconCls("icon-ondeputation");
                            } else if (res.getString("DEP_CODE").equalsIgnoreCase("08")) {// RESIGNED
                                empTree.setIconCls("icon-resigned");
                            } else if (res.getString("DEP_CODE").equalsIgnoreCase("09")) {// SUPERANNUATED
                                empTree.setIconCls("icon-retired");
                            } else if (res.getString("DEP_CODE").equalsIgnoreCase("10")) {// DECEASED
                                empTree.setIconCls("icon-retired");
                            } else if (res.getString("DEP_CODE").equalsIgnoreCase("11")) {// PAY HELDUP 
                                empTree.setIconCls("icon-suspension");
                            } else if (res.getString("DEP_CODE").equalsIgnoreCase("12")) {// ON REDEPLOYMENT
                                empTree.setIconCls("icon-onDuty");
                            }
                        } else {
                            empTree.setIconCls("icon-watingforpost");
                        }
                        empTree.setState("close");
                        al.add(empTree);
                    }
                }

            }
            if (separateBillMapped == false) {
                sqlQuery = " SELECT SPC,POST_LEVEL,SPN,DEP_CODE,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULLNAME,F_NAME,EMP_ID,IS_REGULAR,BRASS_NO,POST,USERTYPE FROM( "
                        + " SELECT SPC,POST_LEVEL,SPN,GPC FROM G_SPC WHERE OFF_CODE = '" + parentOfficeCode + "' AND (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) )G_SPC "
                        + " LEFT OUTER JOIN EMP_MAST ON G_SPC.SPC = EMP_MAST.CUR_SPC "
                        + " LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE WHERE EMP_ID IS NOT NULL  AND  (IS_REGULAR!='N' AND IS_REGULAR!='C' AND IS_REGULAR!='B'  AND IS_REGULAR!='W' AND IS_REGULAR!='G' AND IS_REGULAR!='D' AND IS_REGULAR!='A') "
                        + " UNION "
                        + " SELECT * FROM (SELECT '' SPC,0 POST_LEVEL,'' SPN,DEP_CODE,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULLNAME,F_NAME,EMP_ID,IS_REGULAR,BRASS_NO,USERTYPE,'' POST FROM EMP_MAST "
                        + " WHERE (CUR_OFF_CODE = '" + parentOfficeCode + "'  OR NEXT_OFFICE_CODE='" + parentOfficeCode + "') AND  (IS_REGULAR!='N' AND IS_REGULAR!='C' AND IS_REGULAR!='B'  "
                        + " AND IS_REGULAR!='W' AND IS_REGULAR!='G' AND IS_REGULAR!='D' AND IS_REGULAR!='A') "
                        + " AND CUR_SPC IS NULL AND ((IF_RETIRED IS NULL OR IF_RETIRED='N') OR DEP_CODE='02')AND DEP_CODE NOT IN ('08','10')"
                        + " )  EMP_MAST ORDER BY F_NAME";
                res = stamt.executeQuery(sqlQuery);
                EmployeeTree empTree = null;
                while (res.next()) {
                    empTree = new EmployeeTree();
                    empTree.setId("EMP@REG-" + res.getString("EMP_ID"));
                    if (res.getString("BRASS_NO") != null && !res.getString("BRASS_NO").equals("")) {
                        empTree.setText(res.getString("BRASS_NO") + ", " + StringUtils.defaultString(res.getString("FULLNAME")).toUpperCase() + " " + StringUtils.defaultString(res.getString("POST")).replaceAll("&", " AND "));
                    } else {
                        empTree.setText(StringUtils.defaultString(res.getString("FULLNAME")).toUpperCase() + " , " + StringUtils.defaultString(res.getString("POST")).replaceAll("&", " AND "));
                    }
                    if (res.getString("DEP_CODE") != null && !res.getString("DEP_CODE").equals("")) {
                        if (res.getString("DEP_CODE").equalsIgnoreCase("00")) { // TERMINATED
                            empTree.setIconCls("icon-terminate");
                        } else if (res.getString("DEP_CODE").equalsIgnoreCase("01")) { // LONG TERM LEAVE
                            empTree.setIconCls("icon-onltLeave");
                        } else if (res.getString("DEP_CODE").equalsIgnoreCase("02")) {// ON DUTY
                            empTree.setIconCls("icon-onDuty");
                        } else if (res.getString("DEP_CODE").equalsIgnoreCase("03")) {// WAITING FOR POSTING
                            empTree.setIconCls("icon-watingforpost");
                        } else if (res.getString("DEP_CODE").equalsIgnoreCase("04")) {// ON TRAINING
                            empTree.setIconCls("icon-lttrain");
                        } else if (res.getString("DEP_CODE").equalsIgnoreCase("05")) {// UNDER SUSPENSION
                            empTree.setIconCls("icon-suspension");
                        } else if (res.getString("DEP_CODE").equalsIgnoreCase("06")) {// ON TRANSIT
                            empTree.setIconCls("icon-ontransit");
                        } else if (res.getString("DEP_CODE").equalsIgnoreCase("07")) {// ON DEPUTATION
                            empTree.setIconCls("icon-ondeputation");
                        } else if (res.getString("DEP_CODE").equalsIgnoreCase("08")) {// RESIGNED
                            empTree.setIconCls("icon-resigned");
                        } else if (res.getString("DEP_CODE").equalsIgnoreCase("09")) {// SUPERANNUATED
                            empTree.setIconCls("icon-retired");
                        } else if (res.getString("DEP_CODE").equalsIgnoreCase("10")) {// DECEASED
                            empTree.setIconCls("icon-retired");
                        } else if (res.getString("DEP_CODE").equalsIgnoreCase("11")) {// PAY HELDUP 
                            empTree.setIconCls("icon-suspension");
                        } else if (res.getString("DEP_CODE").equalsIgnoreCase("12")) {// ON REDEPLOYMENT
                            empTree.setIconCls("icon-onDuty");
                        }
                    } else {
                        empTree.setIconCls("icon-watingforpost");
                    }
                    empTree.setState("close");
                    al.add(empTree);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, stamt);
            DataBaseFunctions.closeSqlObjects(rs2, stamt2);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    public ArrayList getCurrentOffice(String empid) throws Exception {
        ArrayList offlist = new ArrayList();
        PreparedStatement pstamt = null;
        ResultSet res = null;
        Connection con = null;
        try {
            con = this.repodataSource.getConnection();

            /*pstamt = con.prepareStatement("SELECT * FROM(SELECT CUR_SPC,CUR_OFF_CODE,OFF_EN FROM ( "
             + "SELECT 1 SLNO,CUR_SPC,CUR_OFF_CODE,OFF_EN FROM(SELECT CUR_SPC,CUR_OFF_CODE FROM EMP_MAST WHERE EMP_ID=? ) "
             + "EMP_MAST LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE=G_OFFICE.OFF_CODE "
             + "UNION "
             + "SELECT 2 SLNO,SPC CUR_SPC, AUTH_OFF CUR_OFF_CODE,OFF_EN FROM ( "
             + "select AUTH_OFF, SPC from emp_join where  EMP_ID=?  AND IF_AD_CHARGE='Y' "
             + "AND JOIN_ID::TEXT NOT IN (SELECT EMP_ID FROM EMP_RELIEVE WHERE EMP_ID=?)) EMP_JOIN "
             + "LEFT OUTER JOIN G_OFFICE ON EMP_JOIN.AUTH_OFF=G_OFFICE.OFF_CODE) office_tree ORDER BY SLNO,OFF_EN");*/
            pstamt = con.prepareStatement("SELECT * FROM (SELECT CUR_SPC,CUR_OFF_CODE,OFF_EN,department_code,lvl FROM (\n"
                    + "SELECT 1 SLNO,CUR_SPC,CUR_OFF_CODE,OFF_EN,department_code,lvl FROM(SELECT CUR_SPC,CUR_OFF_CODE FROM EMP_MAST WHERE EMP_ID=?)\n"
                    + "EMP_MAST LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE=G_OFFICE.OFF_CODE\n"
                    + "AND (G_OFFICE.DDO_HRMSID=?\n"
                    + "OR CUR_SPC IN (SELECT SPC FROM G_PRIVILEGE_MAP WHERE ROLE_ID='05'))\n"
                    + "UNION\n"
                    + "SELECT 2 SLNO,SPC CUR_SPC, AUTH_OFF CUR_OFF_CODE,OFF_EN,department_code,lvl FROM (\n"
                    + "select AUTH_OFF, SPC from emp_join where EMP_ID=?  AND IF_AD_CHARGE='Y'\n"
                    + "AND JOIN_ID::TEXT NOT IN (SELECT EMP_ID FROM EMP_RELIEVE WHERE EMP_ID=?)) EMP_JOIN\n"
                    + "LEFT OUTER JOIN G_OFFICE ON EMP_JOIN.AUTH_OFF=G_OFFICE.OFF_CODE) office_tree ORDER BY SLNO,OFF_EN)temp WHERE OFF_EN IS NOT NULL");
            pstamt.setString(1, empid);
            pstamt.setString(2, empid);
            pstamt.setString(3, empid);
            pstamt.setString(4, empid);
            res = pstamt.executeQuery();
            OfficeHelper ofh = null;

            while (res.next()) {
                ofh = new OfficeHelper();
                if (res.getString("CUR_SPC") != null && !res.getString("CUR_SPC").equals("")) {
                    ofh.setPostCode(res.getString("CUR_SPC"));
                } else {
                    ofh.setPostCode(null);
                }

                ofh.setOfficeCode(res.getString("CUR_OFF_CODE"));
                if (res.getString("OFF_EN") != null && !res.getString("OFF_EN").equals("")) {
                    ofh.setOfficeName(res.getString("OFF_EN").replaceAll("&", " AND "));
                }
                ofh.setDeptcode(res.getString("department_code"));
                ofh.setOfflvl(res.getString("lvl"));
                offlist.add(ofh);

            }

            /*
             pstamt=con.prepareStatement("select off_code, off_en from g_office where ddo_code=?");
             pstamt.setString(1, ofh.getOfficeCode());
             res=pstamt.executeQuery();
             while(res.next()){
             ofh = new OfficeHelper();
             ofh.setPostCode(res.getString("CUR_SPC"));
             ofh.setOfficeCode(res.getString("CUR_OFF_CODE"));
             if (res.getString("OFF_EN") != null && !res.getString("OFF_EN").equals("")) {
             ofh.setOfficeName(res.getString("OFF_EN").replaceAll("&", " AND "));
             }
             offlist.add(ofh);
             }
            
             */
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstamt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return offlist;

    }

    @Override
    public String saveExpertise(UserExpertise ue) {
        Connection con = null;

        PreparedStatement pst = null;
        Statement st = null;
        ResultSet rs = null;
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        String startTime = dateFormat.format(cal.getTime());

        String msg = "Success";
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            pst = con.prepareStatement("INSERT INTO g_emp_expertise(emp_id, emp_name, designation, grade, department"
                    + ", current_place_of_posting, office_stationed, area_of_expertise, area_of_interest, willingness_to_serve"
                    + ", mobile, landline_number, office_phone_number, email_id, date_updated)"
                    + " Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1, ue.getEmpid());
            pst.setString(2, ue.getName());
            pst.setString(3, ue.getDesignation());
            pst.setString(4, ue.getGrade());
            pst.setString(5, ue.getDeptname());
            pst.setString(6, ue.getPostingPlace());
            pst.setString(7, ue.getCurofficename());
            pst.setString(8, ue.getAreaOfExpertise());
            pst.setString(9, ue.getAreaOfInterest());
            pst.setString(10, ue.getVolWillingness());
            pst.setString(11, ue.getMobile());
            pst.setString(12, ue.getLandline());
            pst.setString(13, ue.getOfficePhone());
            pst.setString(14, ue.getEmailid());
            pst.setTimestamp(15, new Timestamp(dateFormat.parse(startTime).getTime()));

            pst.executeUpdate();
        } catch (Exception e) {
            msg = "Error";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    @Override
    public OfficeTreeAttr getHODOfficeListXML(String parentOfficeCode) {
        Connection con = null;
        ArrayList childlist = new ArrayList();
        PreparedStatement pstmt = null;
        ResultSet res = null;
        OfficeTreeAttr ofattr = new OfficeTreeAttr();
        try {
            con = repodataSource.getConnection();
            pstmt = con.prepareStatement("SELECT OFF_CODE,OFF_EN FROM G_OFFICE WHERE OFF_CODE=?");
            pstmt.setString(1, parentOfficeCode);
            res = pstmt.executeQuery();
            if (res.next()) {
                //ofattr.setId(CommonFunctions.encodedTxt(res.getString("OFF_CODE")));
                ofattr.setText(res.getString("OFF_EN"));
                ofattr.setState("open");
                ofattr.setIconCls("icon-office");
            }
            pstmt = con.prepareStatement("SELECT OFF_CODE,OFF_EN FROM G_OFFICE WHERE P_OFF_CODE=? ORDER BY OFF_EN");
            pstmt.setString(1, parentOfficeCode);
            res = pstmt.executeQuery();
            while (res.next()) {
                OfficeTreeAttr childOfattr = new OfficeTreeAttr();
                childOfattr.setId("PA" + res.getString("OFF_CODE"));
                childOfattr.setText(res.getString("OFF_EN"));
                childOfattr.setIconCls("icon-office");

                ArrayList regList = new ArrayList();

                EmployeeTree regular = new EmployeeTree();
                regular.setId("REG" + res.getString("OFF_CODE"));
                regular.setText("REGULAR EMPLOYEES");
                regular.setState("open");
                regular.setIconCls("office.png");
                regular.setChildren(getEmployeeList(res.getString("OFF_CODE"), ""));
                regList.add(regular);

                childOfattr.setChildren(regList);

                childlist.add(childOfattr);
            }
            ofattr.setChildren(childlist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ofattr;
    }

    @Override
    public DistrictTreeAttr getDistrictOfficeListXML(String districtCode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        DistrictTreeAttr distAttr = new DistrictTreeAttr();
        ArrayList childlist = new ArrayList();
        try {
            con = repodataSource.getConnection();
            pstmt = con.prepareStatement("SELECT DIST_CODE, DIST_NAME FROM G_district WHERE DIST_CODE=?");
            pstmt.setString(1, districtCode);
            res = pstmt.executeQuery();
            if (res.next()) {
                distAttr.setId(CommonFunctions.encodedTxt(res.getString("DIST_CODE")));
                distAttr.setText(res.getString("DIST_NAME"));
                distAttr.setState("open");
                distAttr.setIconCls("icon-office");
            }
            pstmt = con.prepareStatement("SELECT OFF_CODE,OFF_EN FROM G_OFFICE WHERE DIST_CODE=? ORDER BY OFF_EN");
            pstmt.setString(1, districtCode);
            res = pstmt.executeQuery();
            while (res.next()) {
                OfficeTreeAttr childOfattr = new OfficeTreeAttr();
                childOfattr.setId(res.getString("OFF_CODE"));
                childOfattr.setText(res.getString("OFF_EN"));
                childOfattr.setIconCls("icon-office");
                childlist.add(childOfattr);
            }
            distAttr.setChildren(childlist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return distAttr;
    }

    @Override
    public DepartmentTreeAttr getCadreList(String deptcode, String cadreType) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet res = null;
        DepartmentTreeAttr departmentTreeAttr = new DepartmentTreeAttr();
        ArrayList childlist = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT department_code, department_name FROM g_department WHERE department_code=?");
            pst.setString(1, deptcode);
            res = pst.executeQuery();
            if (res.next()) {
                departmentTreeAttr.setId(CommonFunctions.encodedTxt(res.getString("department_code")));
                departmentTreeAttr.setText(res.getString("department_name"));
                departmentTreeAttr.setState("open");
                departmentTreeAttr.setIconCls("icon-office");
            }
            DataBaseFunctions.closeSqlObjects(res, pst);
            pst = con.prepareStatement("SELECT cadre_code,cadre_name from g_cadre where department_code=? and cadre_type=? order by cadre_name asc");
            pst.setString(1, deptcode);
            pst.setString(2, cadreType);
            res = pst.executeQuery();
            while (res.next()) {
                CadreTreeAttr cadreTreeAttr = new CadreTreeAttr();
                cadreTreeAttr.setId(CommonFunctions.encodedTxt(res.getString("cadre_code")));
                cadreTreeAttr.setText(res.getString("cadre_name"));
                cadreTreeAttr.setState("close");
                cadreTreeAttr.setIconCls("icon-office");
                childlist.add(cadreTreeAttr);
            }
            departmentTreeAttr.setChildren(childlist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return departmentTreeAttr;
    }

    @Override
    public OfficeTreeAttr getCadreListXML(String loginoffcode, String loginspc) {

        Connection con = null;

        ArrayList childlist = new ArrayList();

        PreparedStatement pstmt = null;
        ResultSet res = null;

        OfficeTreeAttr ofattr = new OfficeTreeAttr();
        try {
            con = this.repodataSource.getConnection();

            pstmt = con.prepareStatement("SELECT OFF_CODE,OFF_EN FROM G_OFFICE WHERE OFF_CODE=?");
            pstmt.setString(1, loginoffcode);
            res = pstmt.executeQuery();
            if (res.next()) {
                //ofattr.setId(CommonFunctions.encodedTxt(res.getString("OFF_CODE")));
                ofattr.setText(res.getString("OFF_EN"));
                ofattr.setState("open");
                ofattr.setIconCls("icon-office");
            }
            pstmt = con.prepareStatement("select cadre_authority_admin.*,g_cadre.cadre_name from cadre_authority_admin"
                    + " inner join g_cadre on cadre_authority_admin.cadre_code=g_cadre.cadre_code where spc=? order by cadre_name");
            pstmt.setString(1, loginspc);
            res = pstmt.executeQuery();
            while (res.next()) {
                OfficeTreeAttr childOfattr = new OfficeTreeAttr();
                childOfattr.setId("PA" + res.getString("cadre_code"));
                childOfattr.setText(res.getString("cadre_name"));
                childOfattr.setIconCls("icon-office");

                ArrayList empList = new ArrayList();

                EmployeeTree emptree = new EmployeeTree();
                emptree.setId("REG" + res.getString("cadre_code"));
                emptree.setText("REGULAR EMPLOYEES");
                emptree.setState("open");
                emptree.setIconCls("office.png");
                emptree.setChildren(getCadreWiseEmployeeList(res.getString("cadre_code"), res.getString("cadre_grade")));
                empList.add(emptree);

                childOfattr.setChildren(empList);

                childlist.add(childOfattr);
            }
            ofattr.setChildren(childlist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ofattr;
    }

    public ArrayList getCadreWiseEmployeeList(String cadrecode, String cadregrade) throws Exception {

        Connection con = null;

        ArrayList emplist = new ArrayList();

        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.repodataSource.getConnection();

            String sql = "SELECT EMP_ID,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULLNAME,DEP_CODE,POST FROM EMP_MAST"
                    + " LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC"
                    + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
                    + " WHERE CUR_CADRE_CODE=?";
            if (cadregrade != null && !cadregrade.equals("")) {
                sql = sql + " AND CUR_CADRE_GRADE=?";
            }
            sql = sql + " ORDER BY F_NAME";
            pst = con.prepareStatement(sql);
            pst.setString(1, cadrecode);
            if (cadregrade != null && !cadregrade.equals("")) {
                pst.setString(2, cadregrade);
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                EmployeeTree empTree = null;

                empTree = new EmployeeTree();
                empTree.setId("EMP@REG-" + rs.getString("EMP_ID"));

                empTree.setText(rs.getString("FULLNAME").toUpperCase() + " , " + StringUtils.defaultString(rs.getString("POST")).replaceAll("&", " AND "));

                if (rs.getString("DEP_CODE") != null && !rs.getString("DEP_CODE").equals("")) {
                    if (rs.getString("DEP_CODE").equalsIgnoreCase("00")) { // TERMINATED
                        empTree.setIconCls("icon-terminate");
                    } else if (rs.getString("DEP_CODE").equalsIgnoreCase("01")) { // LONG TERM LEAVE
                        empTree.setIconCls("icon-onltLeave");
                    } else if (rs.getString("DEP_CODE").equalsIgnoreCase("02")) {// ON DUTY
                        empTree.setIconCls("icon-onDuty");
                    } else if (rs.getString("DEP_CODE").equalsIgnoreCase("03")) {// WAITING FOR POSTING
                        empTree.setIconCls("icon-watingforpost");
                    } else if (rs.getString("DEP_CODE").equalsIgnoreCase("04")) {// ON TRAINING
                        empTree.setIconCls("icon-lttrain");
                    } else if (rs.getString("DEP_CODE").equalsIgnoreCase("05")) {// UNDER SUSPENSION
                        empTree.setIconCls("icon-suspension");
                    } else if (rs.getString("DEP_CODE").equalsIgnoreCase("06")) {// ON TRANSIT
                        empTree.setIconCls("icon-ontransit");
                    } else if (rs.getString("DEP_CODE").equalsIgnoreCase("07")) {// ON DEPUTATION
                        empTree.setIconCls("icon-ondeputation");
                    } else if (rs.getString("DEP_CODE").equalsIgnoreCase("08")) {// RESIGNED
                        empTree.setIconCls("icon-resigned");
                    } else if (rs.getString("DEP_CODE").equalsIgnoreCase("09")) {// SUPERANNUATED
                        empTree.setIconCls("icon-retired");
                    } else if (rs.getString("DEP_CODE").equalsIgnoreCase("10")) {// DECEASED
                        empTree.setIconCls("icon-retired");
                    } else if (rs.getString("DEP_CODE").equalsIgnoreCase("11")) {// PAY HELDUP 
                        empTree.setIconCls("icon-suspension");
                    } else if (rs.getString("DEP_CODE").equalsIgnoreCase("12")) {// ON REDEPLOYMENT
                        empTree.setIconCls("icon-onDuty");
                    }
                }

                emplist.add(empTree);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public ArrayList getDeceasedEmployeeList(String parentOfficeCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ArrayList templist = new ArrayList();
        EmployeeTree empTree = new EmployeeTree();
        try {
            con = this.repodataSource.getConnection();
            ps = con.prepareStatement("SELECT EMP_MAST.EMP_ID, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME ,F_NAME FROM EMP_MAST  "
                    + " WHERE CUR_OFF_CODE=? AND dep_code='10' ORDER BY F_NAME");
            ps.setString(1, parentOfficeCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                empTree = new EmployeeTree();
                empTree.setId("EMP@DEC-" + rs.getString("EMP_ID"));
                if (rs.getString("FULL_NAME") != null && !rs.getString("FULL_NAME").equals("")) {
                    empTree.setText(rs.getString("FULL_NAME").toUpperCase().replaceAll("&", " AND "));
                } else {
                    empTree.setText(rs.getString("FULL_NAME").replaceAll("&", " AND "));
                }
                empTree.setIconCls("icon-retired");
                empTree.setState("close");
                templist.add(empTree);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps, con);
        }
        return templist;
    }

    @Override
    public ArrayList getResignedEmployeeList(String parentOfficeCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ArrayList templist = new ArrayList();
        EmployeeTree empTree = new EmployeeTree();
        try {
            con = this.repodataSource.getConnection();
            ps = con.prepareStatement("SELECT EMP_MAST.EMP_ID, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME ,F_NAME FROM\n"
                    + "(SELECT * FROM EMP_MAST WHERE CUR_OFF_CODE=? AND DEP_CODE='08')EMP_MAST\n"
                    + "INNER JOIN EMP_RELIEVE ON EMP_MAST.EMP_ID=EMP_RELIEVE.EMP_ID\n"
                    + "WHERE NOT_TYPE='RESIGNATION' ORDER BY F_NAME");
            ps.setString(1, parentOfficeCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                empTree = new EmployeeTree();
                empTree.setId("EMP@RES-" + rs.getString("EMP_ID"));
                if (rs.getString("FULL_NAME") != null && !rs.getString("FULL_NAME").equals("")) {
                    empTree.setText(rs.getString("FULL_NAME").toUpperCase().replaceAll("&", " AND "));
                } else {
                    empTree.setText(rs.getString("FULL_NAME").replaceAll("&", " AND "));
                }
                empTree.setIconCls("icon-resigned");
                empTree.setState("close");
                templist.add(empTree);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps, con);
        }
        return templist;
    }

    @Override
    public String saveVisitedData(LoginUserBean lub) {
        Connection con = null;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        PreparedStatement pst3 = null;
        Statement st = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        Date currDate = new Date();
        Calendar cal = Calendar.getInstance();
        Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        String msg = "Success";
        try {
            con = this.dataSource.getConnection();
            pst3 = con.prepareStatement("SELECT CURRENT_DATE as CURDATE");
            rs1 = pst3.executeQuery();
            if (rs1.next()) {
                currDate = rs1.getDate("CURDATE");
            }
            pst1 = con.prepareStatement("select * from districtcoordinator_visited where empid_asddo=?");
            pst1.setString(1, lub.getLoginempid());

            rs = pst1.executeQuery();
            if (rs.next()) {
                pst2 = con.prepareStatement("update districtcoordinator_visited set date_of_visit=?,if_visited=?, ddo_visit_hrmsweb=? where empid_asddo=?");
                Date visiteddate1 = formatter.parse(lub.getDateOfVisit());
                java.sql.Date visiteddateSql1 = new java.sql.Date(visiteddate1.getTime());
                pst2.setDate(1, visiteddateSql1);
                pst2.setString(2, "Y");
                java.sql.Date sqlDate = new java.sql.Date(currDate.getTime());
                pst2.setDate(3, sqlDate);
                pst2.setString(4, lub.getLoginempid());
                pst2.executeUpdate();
            } else {
                pst = con.prepareStatement("insert into districtcoordinator_visited(office_code,ddo_code,if_visited,date_of_visit,empid_asddo)values(?,?,?,?,?)");
                pst.setString(1, lub.getLoginoffcode());
                pst.setString(2, lub.getLoginDDOCode());
                pst.setString(3, "Y");
                Date visiteddate = formatter.parse(lub.getDateOfVisit());
                java.sql.Date visiteddateSql = new java.sql.Date(visiteddate.getTime());
                pst.setDate(4, visiteddateSql);
                pst.setString(5, lub.getLoginempid());
                pst.executeUpdate();
            }
        } catch (Exception e) {
            msg = "Error";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(pst1);
            DataBaseFunctions.closeSqlObjects(rs1, pst3);
            DataBaseFunctions.closeSqlObjects(pst2);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    @Override
    public String sendMsgToDc(LoginUserBean lub) {
        Connection con = null;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        String msg = "Success";
        String dcMobile = "";
        String offName = "";
        String successMsg = null;
        Date currDate = new Date();
        Calendar cal = Calendar.getInstance();
        Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            con = this.dataSource.getConnection();
            String offNeme = "SELECT off_name FROM g_office where off_code=?";
            pst1 = con.prepareStatement(offNeme);
            pst1.setString(1, lub.getLoginoffcode());
            pst1.executeQuery();
            rs1 = pst1.executeQuery();
            if (rs1.next()) {
                offName = rs1.getString("off_name");
            }
            DataBaseFunctions.closeSqlObjects(rs1, pst1);
            pst1 = con.prepareStatement("SELECT CURRENT_DATE as CURDATE");
            rs1 = pst1.executeQuery();
            if (rs1.next()) {
                currDate = rs1.getDate("CURDATE");
            }
            pst2 = con.prepareStatement("select * from districtcoordinator_visited where empid_asddo=?");
            pst2.setString(1, lub.getLoginempid());

            rs2 = pst2.executeQuery();
            if (rs2.next()) {
                pst2 = con.prepareStatement("update districtcoordinator_visited set ddo_visit_hrmsweb=? where empid_asddo=?");
                java.sql.Date sqlDate = new java.sql.Date(currDate.getTime());
                pst2.setDate(1, sqlDate);
                pst2.setString(2, lub.getLoginempid());
                pst2.executeUpdate();
                String sqlMobileDc = "SELECT mobile FROM emp_mast where userid=?";
                pst = con.prepareStatement(sqlMobileDc);
                pst.setString(1, lub.getLogindistrictcode());
                rs = pst.executeQuery();
                if (rs.next()) {
                    dcMobile = rs.getString("mobile");
                }

                successMsg = "You have not visited " + offName + " since last 60 days";
                // System.out.println(" Applicant message:" + msg);
                SMSServices smhttpApp = new SMSServices(dcMobile, successMsg, "1407167903976278520");
            } else {
                String insertVisitedWebsite = "insert into districtcoordinator_visited (office_code,ddo_code,if_visited,empid_asddo,ddo_visit_hrmsweb) values (?,?,?,?,?)";
                pst1 = con.prepareStatement(insertVisitedWebsite);
                pst1.setString(1, lub.getLoginoffcode());
                pst1.setString(2, lub.getLoginDDOCode());
                pst1.setString(3, "N");
                pst1.setString(4, lub.getLoginempid());
                java.sql.Date sqlDate = new java.sql.Date(currDate.getTime());
                pst1.setDate(5, sqlDate);
                pst1.executeUpdate();
                DataBaseFunctions.closeSqlObjects(rs1, pst1);
                String sqlMobileDc = "SELECT mobile FROM emp_mast where userid=?";
                pst = con.prepareStatement(sqlMobileDc);
                pst.setString(1, lub.getLogindistrictcode());
                rs = pst.executeQuery();
                if (rs.next()) {
                    dcMobile = rs.getString("mobile");
                }

                successMsg = "You have not visited " + offName + " since last 60 days";
                // System.out.println(" Applicant message:" + msg);
                SMSServices smhttpApp = new SMSServices(dcMobile, successMsg, "1407167903976278520");
            }
        } catch (Exception e) {
            msg = "Error";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs2, pst2);
            DataBaseFunctions.closeSqlObjects(rs1, pst1);
            DataBaseFunctions.closeSqlObjects(rs, pst);

            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    @Override
    public String noOfDaysOfLastEntry(LoginUserBean lub) {
        String msg = "Success";
        Connection con = null;
        PreparedStatement pst = null;
        Statement st = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        String dateOfVisitedWeb = "";
        int noOfdays;
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("SELECT ddo_visit_hrmsweb from districtcoordinator_visited where empid_asddo=?");
            pst.setString(1, lub.getLoginempid());
            rs = pst.executeQuery();
            if (rs.next()) {
                dateOfVisitedWeb = rs.getString("ddo_visit_hrmsweb");
                st = con.createStatement();
                if (dateOfVisitedWeb != null && !dateOfVisitedWeb.equals("")) {
                    rs1 = st.executeQuery("SELECT CURRENT_DATE - DATE '" + dateOfVisitedWeb + "' AS date_diff");
                    if (rs1.next()) {
                        noOfdays = rs1.getInt("date_diff");
                        if (noOfdays >= 15) {

                            msg = "Success";
                        } else {
                            msg = "Failure";
                        }
                    }
                } else {
                    msg = "Failure";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, st);
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;

    }

    @Override
    public ArrayList getSpecialcategoryEmpList(String parentOfficeCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ArrayList templist = new ArrayList();
        EmployeeTree empTree = new EmployeeTree();
        try {
            con = this.repodataSource.getConnection();
            ps = con.prepareStatement("SELECT EMP_MAST.EMP_ID, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME ,F_NAME FROM\n"
                    + "(SELECT * FROM EMP_MAST WHERE CUR_OFF_CODE=? AND IS_REGULAR='A' AND "
                    + "USERTYPE IN (SELECT CATEGORY_NAME FROM G_SUB_CATEGORY))EMP_MAST ORDER BY F_NAME");
            ps.setString(1, parentOfficeCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                empTree = new EmployeeTree();
                empTree.setId("EMP@RES-" + rs.getString("EMP_ID"));
                if (rs.getString("FULL_NAME") != null && !rs.getString("FULL_NAME").equals("")) {
                    empTree.setText(rs.getString("FULL_NAME").toUpperCase().replaceAll("&", " AND "));
                } else {
                    empTree.setText(rs.getString("FULL_NAME").replaceAll("&", " AND "));
                }
                empTree.setIconCls("icon-resigned");
                empTree.setState("close");
                templist.add(empTree);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps, con);
        }
        return templist;
    }

    public ArrayList getSpecialUserCatgList(String offcode) throws Exception {
        ArrayList specialCatgList = new ArrayList();
        PreparedStatement pstamt = null;
        ResultSet rs = null;
        Connection con = null;
        OfficeTreeAttr ofctr = null;
        try {
            con = this.repodataSource.getConnection();
            pstamt = con.prepareStatement("select distinct usertype FROM EMP_MAST where cur_off_code=?\n"
                    + "and IS_REGULAR='A' AND USERTYPE IN (SELECT CATEGORY_NAME FROM G_SUB_CATEGORY)");
            pstamt.setString(1, offcode);
            rs = pstamt.executeQuery();
            while (rs.next()) {
                ofctr = new OfficeTreeAttr();
                ofctr.setText(rs.getString("usertype"));
                specialCatgList.add(ofctr);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstamt, con);
        }
        return specialCatgList;
    }

    public ArrayList getSubUserCatgList(String offcode, String usertype) throws Exception {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ArrayList templist = new ArrayList();
        EmployeeTree empTree = new EmployeeTree();
        try {
            con = this.repodataSource.getConnection();
            ps = con.prepareStatement("SELECT EMP_MAST.EMP_ID,DEP_CODE, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME ,F_NAME FROM\n"
                    + "(SELECT * FROM EMP_MAST WHERE CUR_OFF_CODE=? AND IS_REGULAR='A' and USERTYPE=?)EMP_MAST ORDER BY F_NAME");
            ps.setString(1, offcode);
            ps.setString(2, usertype);
            rs = ps.executeQuery();
            while (rs.next()) {
                empTree = new EmployeeTree();
                empTree.setId("EMP@RES-" + rs.getString("EMP_ID"));
                //System.out.println("empTree.seitd::" + empTree.getId());
                if (rs.getString("FULL_NAME") != null && !rs.getString("FULL_NAME").equals("")) {
                    empTree.setText(rs.getString("FULL_NAME").toUpperCase().replaceAll("&", " AND "));
                } else {
                    empTree.setText(rs.getString("FULL_NAME").replaceAll("&", " AND "));
                }
                if (rs.getString("DEP_CODE") != null && !rs.getString("DEP_CODE").equals("")) {
                    if (rs.getString("DEP_CODE").equalsIgnoreCase("00")) { // TERMINATED
                        empTree.setIconCls("icon-terminate");
                    } else if (rs.getString("DEP_CODE").equalsIgnoreCase("01")) { // LONG TERM LEAVE
                        empTree.setIconCls("icon-onltLeave");
                    } else if (rs.getString("DEP_CODE").equalsIgnoreCase("02")) {// ON DUTY
                        empTree.setIconCls("icon-onDuty");
                    } else if (rs.getString("DEP_CODE").equalsIgnoreCase("03")) {// WAITING FOR POSTING
                        empTree.setIconCls("icon-watingforpost");
                    } else if (rs.getString("DEP_CODE").equalsIgnoreCase("04")) {// ON TRAINING
                        empTree.setIconCls("icon-lttrain");
                    } else if (rs.getString("DEP_CODE").equalsIgnoreCase("05")) {// UNDER SUSPENSION
                        empTree.setIconCls("icon-suspension");
                    } else if (rs.getString("DEP_CODE").equalsIgnoreCase("06")) {// ON TRANSIT
                        empTree.setIconCls("icon-ontransit");
                    } else if (rs.getString("DEP_CODE").equalsIgnoreCase("07")) {// ON DEPUTATION
                        empTree.setIconCls("icon-ondeputation");
                    } else if (rs.getString("DEP_CODE").equalsIgnoreCase("08")) {// RESIGNED
                        empTree.setIconCls("icon-resigned");
                    } else if (rs.getString("DEP_CODE").equalsIgnoreCase("09")) {// SUPERANNUATED
                        empTree.setIconCls("icon-retired");
                    } else if (rs.getString("DEP_CODE").equalsIgnoreCase("10")) {// DECEASED
                        empTree.setIconCls("icon-retired");
                    } else if (rs.getString("DEP_CODE").equalsIgnoreCase("11")) {// PAY HELDUP 
                        empTree.setIconCls("icon-suspension");
                    } else if (rs.getString("DEP_CODE").equalsIgnoreCase("12")) {// ON REDEPLOYMENT
                        empTree.setIconCls("icon-onDuty");
                    }
                }

                //empTree.setIconCls("icon-resigned");
                empTree.setState("close");
                templist.add(empTree);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps, con);
        }
        return templist;
    }

    @Override
    public ArrayList getdeputationEmployeeList(String deptcode) {
        Connection con = null;
        ResultSet res = null;
        PreparedStatement ps = null;
        ArrayList templist = new ArrayList();
        EmployeeTree empTree = new EmployeeTree();
        try {
            con = this.repodataSource.getConnection();
            ps = con.prepareStatement("select EMP_ID,cur_off_code,NEXT_OFFICE_CODE,cur_spc,dep_code, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME,\n"
                    + "deputation_offcode,deputation_spc,post FROM EMP_MAST inner join\n"
                    + "(select distinct(a1.emp_id) deptempid\n"
                    + "from (select emp_transfer.dept_code,emp_transfer.off_code,emp_transfer.next_spc,\n"
                    + "emp_deputation.* from\n"
                    + "(emp_deputation inner join \n"
                    + "emp_transfer on emp_deputation.not_id=emp_transfer.not_id\n"
                    + "inner join emp_notification on emp_deputation.not_id=emp_notification.not_id ) where\n"
                    + "emp_transfer.dept_code=? and emp_deputation.not_type='DEPUTATION'\n"
                    + "--and extract(year from wef_date)<=(select extract(year from current_date)-1))a1)deptdetails\n"
                    + "and (extract(year from current_date)-extract(year from wef_date)=0\n"
                    + "or extract(year from current_date)-extract(year from wef_date)=1) )a1)deptdetails	  \n"
                    + "on emp_mast.emp_id=deptdetails.deptempid\n"
                    + "left outer join g_post on substr(deputation_spc,14,6)=g_post.post_code");
            ps.setString(1, deptcode);
            res = ps.executeQuery();
            while (res.next()) {
                empTree = new EmployeeTree();
                empTree.setId("EMP@FSC-" + res.getString("EMP_ID"));
                if (res.getString("FULL_NAME") != null && !res.getString("FULL_NAME").equals("")) {
                    empTree.setText(res.getString("FULL_NAME").toUpperCase() + " , " + StringUtils.defaultString(res.getString("post")).replaceAll("&", " AND "));
                } else {
                    empTree.setText(res.getString("FULL_NAME") + " , " + StringUtils.defaultString(res.getString("post")).replaceAll("&", " AND "));
                }
                if (res.getString("DEP_CODE") != null && !res.getString("DEP_CODE").equals("")) {
                    if (res.getString("DEP_CODE").equalsIgnoreCase("00")) { // TERMINATED
                        empTree.setIconCls("icon-terminate");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("01")) { // LONG TERM LEAVE
                        empTree.setIconCls("icon-onltLeave");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("02")) {// ON DUTY
                        empTree.setIconCls("icon-onDuty");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("03")) {// WAITING FOR POSTING
                        empTree.setIconCls("icon-watingforpost");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("04")) {// ON TRAINING
                        empTree.setIconCls("icon-lttrain");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("05")) {// UNDER SUSPENSION
                        empTree.setIconCls("icon-suspension");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("06")) {// ON TRANSIT
                        empTree.setIconCls("icon-ontransit");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("07")) {// ON DEPUTATION
                        empTree.setIconCls("icon-ondeputation");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("08")) {// RESIGNED
                        empTree.setIconCls("icon-resigned");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("09")) {// SUPERANNUATED
                        empTree.setIconCls("icon-retired");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("10")) {// DECEASED
                        empTree.setIconCls("icon-retired");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("11")) {// PAY HELDUP 
                        empTree.setIconCls("icon-suspension");
                    } else if (res.getString("DEP_CODE").equalsIgnoreCase("12")) {// ON REDEPLOYMENT
                        empTree.setIconCls("icon-onDuty");
                    }
                } else {
                    empTree.setIconCls("icon-watingforpost");
                }
                empTree.setState("close");
                templist.add(empTree);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return templist;
    }

}

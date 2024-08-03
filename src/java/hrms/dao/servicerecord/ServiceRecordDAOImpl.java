package hrms.dao.servicerecord;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.model.servicerecord.ServiceRecordBean;
import hrms.model.servicerecord.ServiceRecordForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class ServiceRecordDAOImpl implements ServiceRecordDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;
    
    /*protected ServiceBookLanguageDAO sbDAO;
    
    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }*/

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getServiceRecordList(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList servicerecordList = new ArrayList();
        ServiceRecordBean srbean = null;

        String details = "";
        String subdetail = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT SR_ID,STATION,DETAILS,RTYPE,FDATE,FTIME,TDATE,TTIME,NOTE,PREFIX_DATE,PREFIX_TO,SUFFIX_FROM,SUFFIX_DATE,S_YEAR,T_YEAR,S_DAYS,SV_ID from EMP_SERVICERECORD where EMP_ID=? ORDER BY FDATE DESC";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                srbean = new ServiceRecordBean();
                srbean.setSrid(rs.getString("SR_ID"));

                srbean.setStation(rs.getString("STATION"));
                if (rs.getString("DETAILS") != null && rs.getString("RTYPE") != null) {
                    if (rs.getString("RTYPE").equalsIgnoreCase("L")) {
                        details = getLeaveType(rs.getString("DETAILS"));
                    } else if (rs.getString("RTYPE").equalsIgnoreCase("LS")) {
                        details = "LEAVE SURRENDER";
                    } else if (rs.getString("RTYPE").equalsIgnoreCase("UJTL")) {
                        details = "UNAVAILED JOINING TIME";
                    } else if (rs.getString("RTYPE").equalsIgnoreCase("J")) {
                        details = "JOINING TIME";
                    } else {
                        details = rs.getString("DETAILS");
                    }
                    if (details.length() > 34) {
                        subdetail = details.substring(0, 34);
                        srbean.setDetails(subdetail + "...");
                    } else {
                        srbean.setDetails(details);
                    } 
                }

                srbean.setFromDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("FDATE")));
                srbean.setToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("TDATE")));
                servicerecordList.add(srbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return servicerecordList;
    }

    @Override
    public void saveServiceRecord(ServiceRecordForm serviceRecordForm, String selectedEmpId) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String fromDate = null;
        String fromTime = null;
        String toDate = null;
        String toTime = null;
        String details = null;
        String preFrom = null;
        String preTo = null;
        String suffFrom = null;
        String suffTo = null;
        String lsot = null;
        String syear = null;
        String tyear = null;
        String sdays = null;

        int days = 0;
        try {
            con = this.dataSource.getConnection();

            if (serviceRecordForm.getRdModule() != null && serviceRecordForm.getRdModule().equalsIgnoreCase("J")) {
                details = serviceRecordForm.getTxtJoiningDetails();
                if (serviceRecordForm.getTxtJoiningFromDate() != null && !serviceRecordForm.getTxtJoiningFromDate().trim().equals("")) {
                    fromDate = serviceRecordForm.getTxtJoiningFromDate();
                }
                if (serviceRecordForm.getTxtJoiningToDate() != null && !serviceRecordForm.getTxtJoiningToDate().trim().equals("")) {
                    toDate = serviceRecordForm.getTxtJoiningToDate();
                }
                if (serviceRecordForm.getSltJoiningFromTime() != null && !serviceRecordForm.getSltJoiningFromTime().equals("")) {
                    fromTime = serviceRecordForm.getSltJoiningFromTime();
                }
                if (serviceRecordForm.getSltJoiningToTime() != null && !serviceRecordForm.getSltJoiningToTime().equals("")) {
                    toTime = serviceRecordForm.getSltJoiningToTime();
                }
            } else if (serviceRecordForm.getRdModule() != null && serviceRecordForm.getRdModule().equalsIgnoreCase("S")) {
                details = serviceRecordForm.getTxtServiceRecordDetails();
                if (serviceRecordForm.getTxtServiceRecordFromDate() != null && !serviceRecordForm.getTxtServiceRecordFromDate().trim().equals("")) {
                    fromDate = serviceRecordForm.getTxtServiceRecordFromDate();
                }
                if (serviceRecordForm.getTxtServiceRecordToDate() != null && !serviceRecordForm.getTxtServiceRecordToDate().trim().equals("")) {
                    toDate = serviceRecordForm.getTxtServiceRecordToDate();
                }
                if (serviceRecordForm.getSltServiceRecordFromTime() != null && !serviceRecordForm.getSltServiceRecordFromTime().equals("")) {
                    fromTime = serviceRecordForm.getSltServiceRecordFromTime();
                }
                if (serviceRecordForm.getSltServiceRecordToTime() != null && !serviceRecordForm.getSltServiceRecordToTime().equals("")) {
                    toTime = serviceRecordForm.getSltServiceRecordToTime();
                }
            } else if (serviceRecordForm.getRdModule() != null && serviceRecordForm.getRdModule().equalsIgnoreCase("L")) {
                details = serviceRecordForm.getSltLeaveType();
                preFrom = serviceRecordForm.getTxtLeaveSanctionPrefixFrom();
                preTo = serviceRecordForm.getTxtLeaveSanctionPrefixTo();
                suffFrom = serviceRecordForm.getTxtLeaveSanctionSuffixFrom();
                suffTo = serviceRecordForm.getTxtLeaveSanctionSuffixTo();
                lsot = "01";
                if (serviceRecordForm.getTxtLeaveSanctionFromDate() != null && !serviceRecordForm.getTxtLeaveSanctionFromDate().trim().equals("")) {
                    fromDate = serviceRecordForm.getTxtLeaveSanctionFromDate();
                }
                if (serviceRecordForm.getTxtLeaveSanctionToDate() != null && !serviceRecordForm.getTxtLeaveSanctionToDate().trim().equals("")) {
                    toDate = serviceRecordForm.getTxtLeaveSanctionToDate();
                }
                if (serviceRecordForm.getSltLeaveSanctionFromTime() != null && !serviceRecordForm.getSltLeaveSanctionFromTime().equals("")) {
                    fromTime = serviceRecordForm.getSltLeaveSanctionFromTime();
                }
                if (serviceRecordForm.getSltLeaveSanctionToTime() != null && !serviceRecordForm.getSltLeaveSanctionToTime().equals("")) {
                    toTime = serviceRecordForm.getSltLeaveSanctionToTime();
                }
            } else if (serviceRecordForm.getRdModule() != null && serviceRecordForm.getRdModule().equalsIgnoreCase("LS")) {
                details = "EL";
                syear = serviceRecordForm.getTxtLeaveSurrenderFromYear();
                tyear = serviceRecordForm.getTxtLeaveSurrenderToYear();
                sdays = serviceRecordForm.getTxtLeaveSurrenderNoDays();
                lsot = "02";
                if (serviceRecordForm.getTxtLeaveSurrenderFromDate() != null && !serviceRecordForm.getTxtLeaveSurrenderFromDate().trim().equals("")) {
                    fromDate = serviceRecordForm.getTxtLeaveSurrenderFromDate();
                }
                if (serviceRecordForm.getTxtLeaveSurrenderToDate() != null && !serviceRecordForm.getTxtLeaveSurrenderToDate().trim().equals("")) {
                    toDate = serviceRecordForm.getTxtLeaveSurrenderToDate();
                }

            } else if (serviceRecordForm.getRdModule() != null && serviceRecordForm.getRdModule().equalsIgnoreCase("UJTL")) {
                details = "EL";
                lsot = "06";
                if (serviceRecordForm.getTxtUnavailedJoiningFromDate() != null && !serviceRecordForm.getTxtUnavailedJoiningFromDate().trim().equals("")) {
                    fromDate = serviceRecordForm.getTxtUnavailedJoiningFromDate();
                }
                if (serviceRecordForm.getTxtUnavailedJoiningToDate() != null && !serviceRecordForm.getTxtUnavailedJoiningToDate().trim().equals("")) {
                    toDate = serviceRecordForm.getTxtUnavailedJoiningToDate();
                }
                if (serviceRecordForm.getSltUnavailedJoiningFromTime() != null && !serviceRecordForm.getSltUnavailedJoiningFromTime().equals("")) {
                    fromTime = serviceRecordForm.getSltUnavailedJoiningFromTime();
                }
                if (serviceRecordForm.getSltUnavailedJoiningToTime() != null && !serviceRecordForm.getSltUnavailedJoiningToTime().equals("")) {
                    toTime = serviceRecordForm.getSltUnavailedJoiningToTime();
                }
            }
            if (serviceRecordForm.getSrid() != null && !serviceRecordForm.getSrid().equals("")) {
                String sql = "UPDATE EMP_SERVICERECORD SET STATION=?,DETAILS=?,FDATE=?,FTIME=?,TDATE=?,TTIME=?,RTYPE=?,NOTE=?,DOE=?,IF_ASSUMED=?,TOE=?,PREFIX_DATE=?,PREFIX_TO=?,SUFFIX_FROM=?,SUFFIX_DATE=?,LSOT_ID=?,S_YEAR=?,T_YEAR=?,S_DAYS=?,ent_dept=?,ent_off=?,ent_auth=? where emp_id=? and sr_id=?";
                pst = con.prepareStatement(sql);
                if(serviceRecordForm.getTxtServiceRecordStation() != null && !serviceRecordForm.getTxtServiceRecordStation().equals("")){
                    pst.setString(1, serviceRecordForm.getTxtServiceRecordStation().toUpperCase());
                }else{
                    pst.setString(1, null);
                }
                if(details != null && !details.equals("")){
                    pst.setString(2, details.toUpperCase());
                }else{
                    pst.setString(2, null);
                }
                if (fromDate != null && !fromDate.trim().equals("")) {
                    pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(fromDate).getTime()));
                } else {
                    pst.setTimestamp(3, null);
                }
                pst.setString(4, fromTime);
                if (toDate != null && !toDate.trim().equals("")) {
                    pst.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(toDate).getTime()));
                } else {
                    pst.setTimestamp(5, null);
                }
                pst.setString(6, toTime);
                pst.setString(7, serviceRecordForm.getRdModule());
                pst.setString(8, serviceRecordForm.getNote());
                if (fromDate != null && !fromDate.trim().equals("")) {
                    pst.setTimestamp(9, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(fromDate).getTime()));
                    pst.setString(11, null);
                } else {
                    pst.setTimestamp(9, null);
                    pst.setString(11, null);
                }
                pst.setString(10, "Y");
                if (preFrom != null && !preFrom.trim().equals("")) {
                    pst.setTimestamp(12, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(preFrom).getTime()));
                } else {
                    pst.setTimestamp(12, null);
                }
                if (preTo != null && !preTo.trim().equals("")) {
                    pst.setTimestamp(13, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(preTo).getTime()));
                } else {
                    pst.setTimestamp(13, null);
                }
                if (suffFrom != null && !suffFrom.trim().equals("")) {
                    pst.setTimestamp(14, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(suffFrom).getTime()));
                } else {
                    pst.setTimestamp(14, null);
                }
                if (suffTo != null && !suffTo.trim().equals("")) {
                    pst.setTimestamp(15, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(suffTo).getTime()));
                } else {
                    pst.setTimestamp(15, null);
                }
                pst.setString(16, lsot);
                pst.setString(17, syear);
                pst.setString(18, tyear);
                
                if(sdays != null && !sdays.equals("")){
                    days = Integer.parseInt(sdays);
                }
                if (days == -1) {
                    pst.setString(19, null);
                } else {
                    pst.setInt(19, days);
                }
                pst.setString(20, null);
                pst.setString(21, null);
                pst.setString(22, null);
                pst.setString(23, selectedEmpId);
                pst.setInt(24, Integer.parseInt(serviceRecordForm.getSrid()));
                pst.executeUpdate();
                
                
             /* String sbLang = sbDAO.getServiceRecordDetails(serviceRecordForm);
              pst = con.prepareStatement("UPDATE emp_servicerecord SET SB_DESCRIPTION='" + sbLang + "'  WHERE sr_id='" + serviceRecordForm.getSrid() + "'");
              pst.executeUpdate(); */
                
                
            } else {
                String sql = "INSERT INTO EMP_SERVICERECORD(EMP_ID,STATION,DETAILS,FDATE,FTIME,TDATE,TTIME,RTYPE,NOTE,DOE,IF_ASSUMED,TOE,PREFIX_DATE,PREFIX_TO,SUFFIX_FROM,SUFFIX_DATE,LSOT_ID,S_YEAR,T_YEAR,S_DAYS,ent_dept,ent_off,ent_auth) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                pst = con.prepareStatement(sql);
                pst.setString(1, selectedEmpId);
                pst.setString(2, serviceRecordForm.getTxtServiceRecordStation().toUpperCase());
                pst.setString(3, details.toUpperCase());
                if (fromDate != null && !fromDate.trim().equals("")) {
                    pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(fromDate).getTime()));
                } else {
                    pst.setTimestamp(4, null);
                }
                pst.setString(5, fromTime);
                if (toDate != null && !toDate.trim().equals("")) {
                    pst.setTimestamp(6, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(toDate).getTime()));
                } else {
                    pst.setTimestamp(6, null);
                }
                pst.setString(7, toTime);
                pst.setString(8, serviceRecordForm.getRdModule());
                pst.setString(9, serviceRecordForm.getNote());
                if (fromDate != null && !fromDate.trim().equals("")) {
                    pst.setTimestamp(10, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(fromDate).getTime()));
                    pst.setString(12, null);
                } else {
                    pst.setTimestamp(10, null);
                    pst.setString(12, null);
                }
                pst.setString(11, "Y");
                if (preFrom != null && !preFrom.trim().equals("")) {
                    pst.setTimestamp(13, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(preFrom).getTime()));
                } else {
                    pst.setTimestamp(13, null);
                }
                if (preTo != null && !preTo.trim().equals("")) {
                    pst.setTimestamp(14, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(preTo).getTime()));
                } else {
                    pst.setTimestamp(14, null);
                }
                if (suffFrom != null && !suffFrom.trim().equals("")) {
                    pst.setTimestamp(15, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(suffFrom).getTime()));
                } else {
                    pst.setTimestamp(15, null);
                }
                if (suffTo != null && !suffTo.trim().equals("")) {
                    pst.setTimestamp(16, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(suffTo).getTime()));
                } else {
                    pst.setTimestamp(16, null);
                }
                pst.setString(17, lsot);
                pst.setString(18, syear);
                pst.setString(19, tyear);
                if (sdays != null && !sdays.equals("")) {
                    days = Integer.parseInt(sdays);
                }
                if (days == -1) {
                    pst.setDouble(20, 0);
                } else {
                    pst.setDouble(20, days);
                }
                pst.setString(21, null);
                pst.setString(22, null);
                pst.setString(23, null);
                pst.executeUpdate();
               
             /* int srId = 0; 
              srId = rs.getInt("sr_id"); 
              System.out.println("insert srid :" + srId);
              String sbLang = sbDAO.getServiceRecordDetails(serviceRecordForm);
              pst = con.prepareStatement("UPDATE emp_servicerecord SET SB_DESCRIPTION='" + sbLang + "'  WHERE sr_id='" + srId + "'");
              System.out.println("insert1 srid :" + srId);
              pst.executeUpdate(); */
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    private String getLeaveType(String tolId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String leaveType = null;

        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("SELECT TOL FROM G_LEAVE WHERE TOL_ID=?");
            pst.setString(1, tolId);
            rs = pst.executeQuery();
            if (rs.next()) {
                leaveType = rs.getString("TOL");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return leaveType;
    }

    @Override
    public ServiceRecordForm editServiceRecord(String empid, int srid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ServiceRecordForm srform = new ServiceRecordForm();
        try {
            con = this.dataSource.getConnection();

            String sql = "select STATION,DETAILS,FDATE,FTIME,TDATE,TTIME,RTYPE,NOTE,PREFIX_DATE,PREFIX_TO,SUFFIX_FROM,SUFFIX_DATE,S_YEAR,T_YEAR,S_DAYS,ent_dept,ent_off,ent_auth from EMP_SERVICERECORD where EMP_ID=? and SR_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, srid);
            rs = pst.executeQuery();
            if (rs.next()) {
                srform.setSrid(srid+"");
                srform.setTxtServiceRecordStation(rs.getString("STATION"));
                srform.setRdModule(rs.getString("RTYPE"));

                if (srform.getRdModule() != null && !srform.getRdModule().equals("")) {
                    if (srform.getRdModule().equalsIgnoreCase("J")) {
                        srform.setTxtJoiningDetails(rs.getString("DETAILS"));

                        srform.setTxtJoiningFromDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("FDATE")));
                        srform.setSltJoiningFromTime(rs.getString("FTIME"));
                        srform.setTxtJoiningToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("TDATE")));
                        srform.setSltJoiningToTime(rs.getString("TTIME"));
                    } else if (srform.getRdModule().equalsIgnoreCase("S")) {
                        srform.setTxtServiceRecordDetails(rs.getString("DETAILS"));

                        srform.setTxtServiceRecordFromDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("FDATE")));
                        srform.setSltServiceRecordFromTime(rs.getString("FTIME"));
                        srform.setTxtServiceRecordToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("TDATE")));
                        srform.setSltServiceRecordToTime(rs.getString("TTIME"));
                    } else if (srform.getRdModule().equalsIgnoreCase("L")) {
                        srform.setSltLeaveType(rs.getString("DETAILS"));

                        srform.setTxtLeaveSanctionFromDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("FDATE")));
                        srform.setSltLeaveSanctionFromTime(rs.getString("FTIME"));
                        srform.setTxtLeaveSanctionToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("TDATE")));
                        srform.setSltLeaveSanctionToTime(rs.getString("TTIME"));
                    } else if (srform.getRdModule().equalsIgnoreCase("LS")) {

                        srform.setTxtLeaveSurrenderFromDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("FDATE")));
                        srform.setTxtLeaveSurrenderToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("TDATE")));

                    } else {

                        srform.setTxtUnavailedJoiningFromDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("FDATE")));
                        srform.setSltUnavailedJoiningFromTime(rs.getString("FTIME"));
                        srform.setTxtUnavailedJoiningToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("TDATE")));
                        srform.setSltUnavailedJoiningToTime(rs.getString("TTIME"));
                    }

                    srform.setNote(rs.getString("NOTE"));
                    srform.setTxtLeaveSanctionPrefixFrom(CommonFunctions.getFormattedOutputDate1(rs.getDate("PREFIX_DATE")));
                    srform.setTxtLeaveSanctionPrefixTo(CommonFunctions.getFormattedOutputDate1(rs.getDate("PREFIX_TO")));
                    srform.setTxtLeaveSanctionSuffixFrom(CommonFunctions.getFormattedOutputDate1(rs.getDate("SUFFIX_FROM")));
                    srform.setTxtLeaveSanctionSuffixTo(CommonFunctions.getFormattedOutputDate1(rs.getDate("SUFFIX_DATE")));
                    srform.setTxtLeaveSurrenderFromYear(rs.getString("S_YEAR"));
                    srform.setTxtLeaveSurrenderToYear(rs.getString("T_YEAR"));
                    srform.setTxtLeaveSurrenderNoDays(rs.getString("S_DAYS"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return srform;
    }
    
    @Override 
    public void deleteServiceRecord(int srid) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM EMP_SERVICERECORD where SR_ID=?");
            pst.setInt(1, srid);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
}

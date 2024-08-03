/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.master.DistrictWiseEsignBean;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Surendra
 */
public class DistrictWiseEsignDAOImpl implements DistrictWiseEsignDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    @Override
    public ArrayList getEsignList(String distCode, int smonth, int syear) {

        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList qualList = new ArrayList();
        DistrictWiseEsignBean districtWiseEsignBean = null;
        try {
            con = dataSource.getConnection();

            st = con.prepareStatement("select off_code, off_en , dsign,esign from \n"
                    + "g_office LEFT OUTER join\n"
                    + "(select office_code, count(*)dsign from esign_log where signed_pdf_file is not null \n"
                    + " and signed_pdf_path is not null and report_ref_slno=2 and month=? and year=?\n"
                    + " AND signed_login_id is not null\n"
                    + " group by office_code order by office_code)dsg\n"
                    + "on g_office.off_code=dsg.office_code AND DIST_CODE=?\n"
                    + "left outer join\n"
                    + "(select office_code, count(*)esign from esign_log where month=? and year=? \n"
                    + " and report_ref_slno=2 and signature_type='ESIGN' \n"
                    + " group by office_code)esg\n"
                    + "on g_office.off_code=esg.office_code AND DIST_CODE=? \n"
                    + "where  off_status='F'  AND (dsign >0 OR esign>0 )\n"
                    + "order by off_CODE,OFF_EN");

            st.setInt(1, smonth);
            st.setInt(2, syear);
            st.setString(3, distCode);

            st.setInt(4, smonth);
            st.setInt(5, syear);
            st.setString(6, distCode);

            System.out.println("smonth" + smonth + 1);
            rs = st.executeQuery();
            while (rs.next()) {
                districtWiseEsignBean = new DistrictWiseEsignBean();
                districtWiseEsignBean.setOfficeCode(rs.getString("off_code"));
                districtWiseEsignBean.setOfficeName(rs.getString("off_en"));
                districtWiseEsignBean.setNoofDsign(rs.getInt("dsign"));
                districtWiseEsignBean.setNoofEsign(rs.getInt("esign"));
                qualList.add(districtWiseEsignBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return qualList;
    }

    @Override
    public List searchEsignbillDelete(DistrictWiseEsignBean districtWiseEsignBean) {

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List esignBillList = new ArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            con = this.dataSource.getConnection();

            String sql = "select g_bill_status.bill_status,esign_log.*,bill_desc,off_en,dist_code from\n"
                    + "(select * from esign_log where bill_no=? and report_ref_slno='2')esign_log\n"
                    + "inner join bill_mast on esign_log.bill_no=bill_mast.bill_no\n"
                    + "inner join g_bill_status on g_bill_status.id=bill_mast.bill_status_id \n"
                    + "inner join g_office on esign_log.office_code=g_office.off_code where bill_status_id not in (3,5,7)";

            ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(districtWiseEsignBean.getBillnumber()));
            //ps.setString(2, districtWiseEsignBean.getOfficeCode());
            rs = ps.executeQuery();
            while (rs.next()) {
                districtWiseEsignBean = new DistrictWiseEsignBean();
                districtWiseEsignBean.setMonth(CommonFunctions.getMonthAsString(rs.getInt("month")));
                districtWiseEsignBean.setYear(rs.getString("year"));
                districtWiseEsignBean.setBilldesc(rs.getString("bill_desc"));
                districtWiseEsignBean.setBill_status(rs.getString("bill_status"));
                districtWiseEsignBean.setDistcode(rs.getString("dist_code"));
                //districtWiseEsignBean.setCreate_pdf_date(rs.getString("create_pdf_date"));
                //districtWiseEsignBean.setEsign_pdf_date(rs.getString("esign_pdf_date"));
                districtWiseEsignBean.setUnsigned_pdf_file(rs.getString("unsigned_pdf_file"));
                districtWiseEsignBean.setSigned_pdf_file(rs.getString("signed_pdf_file"));
                districtWiseEsignBean.setUnsigned_pdf_path(rs.getString("unsigned_pdf_path"));
                districtWiseEsignBean.setSigned_pdf_path(rs.getString("signed_pdf_path"));
                districtWiseEsignBean.setBillno(rs.getInt("bill_no"));
                districtWiseEsignBean.setId_esign_log(rs.getInt("id_esign_log"));
                if (rs.getString("create_pdf_date") != null && !rs.getString("create_pdf_date").equals("")) {
                    districtWiseEsignBean.setCreate_pdf_date(sdf.format(rs.getTimestamp("create_pdf_date").getTime()));
                }
                if (rs.getString("esign_pdf_date") != null && !rs.getString("esign_pdf_date").equals("")) {
                    districtWiseEsignBean.setEsign_pdf_date(sdf.format(rs.getTimestamp("esign_pdf_date")));
                }
                esignBillList.add(districtWiseEsignBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return esignBillList;

    }

    @Override
    public DistrictWiseEsignBean getEsignbill(int billno, int esignId) {
        ResultSet rs = null;
        boolean flag = false;
        PreparedStatement pst = null;
        Connection con = null;
        DistrictWiseEsignBean dEsignBean = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        List esignbillsList = new ArrayList();
        //SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("select esign_log.*,dist_code from\n"
                    + "(esign_log inner join g_office \n"
                    + " on esign_log.office_code=g_office.off_code)\n"
                    + "where bill_no=? and id_esign_log=? ");
            pst.setInt(1, billno);
            pst.setInt(2, esignId);
            rs = pst.executeQuery();
            while (rs.next()) {
                dEsignBean = new DistrictWiseEsignBean();
                dEsignBean.setBillno(rs.getInt("bill_no"));
                dEsignBean.setDistcode(rs.getString("dist_code"));
                dEsignBean.setOfficeCode(rs.getString("office_code"));
                dEsignBean.setSigned_pdf_path(rs.getString("signed_pdf_path"));
                dEsignBean.setSigned_pdf_file(rs.getString("signed_pdf_file"));
                if (rs.getString("create_pdf_date") != null && !rs.getString("create_pdf_date").equals("")) {
                    dEsignBean.setCreate_pdf_date(sdf.format(rs.getTimestamp("create_pdf_date")));
                }
                if (rs.getString("esign_pdf_date") != null && !rs.getString("esign_pdf_date").equals("")) {
                    dEsignBean.setEsign_pdf_date(sdf.format(rs.getTimestamp("esign_pdf_date")));
                }

                dEsignBean.setReport_ref_slno(rs.getInt("report_ref_slno"));
                dEsignBean.setSigned_login_id(rs.getString("signed_login_id"));
                dEsignBean.setReference_no(rs.getString("reference_no"));
                dEsignBean.setTransaction_no(rs.getString("transaction_no"));

                dEsignBean.setSignature_type(rs.getString("signature_type"));
                dEsignBean.setId_esign_log(rs.getInt("id_esign_log"));

                dEsignBean.setUnsigned_pdf_path(rs.getString("unsigned_pdf_path"));
                dEsignBean.setUnsigned_pdf_file(rs.getString("unsigned_pdf_file"));
                //dEsignBean.setDistcode(rs.getString("dist_code"));
                //esignbillsList.add(dEsignBean);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return dEsignBean;
    }

    @Override
    public String esignFileRename(DistrictWiseEsignBean dwEsignBean, String signedfilepath) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String pdffileName = null;
        String newpdffileName = null;
        String pdfpath = null;
        String presigntype = null;
        String esignfiletype = null;
        String newfilepath = null;
        String filepath = null;
        try {

            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select * from equarter.esign_delete_log where bill_no=? \n"
                    + "and dl_logid=(select max(dl_logid) from equarter.esign_delete_log where bill_no=? ) ");
            pst.setString(1, Integer.toString(dwEsignBean.getBillno()));
            pst.setString(2, Integer.toString(dwEsignBean.getBillno()));
            rs = pst.executeQuery();
            if (rs.next()) {
                pdffileName = rs.getString("new_pdf_file");
                String[] esgfilename = pdffileName.split("_");
                String[] esgfilename3 = esgfilename[3].split("[.]", 0);
                int noofesigndelete = Integer.parseInt(esgfilename3[0]) + 1;
                esignfiletype = "_".concat(Integer.toString(noofesigndelete)).concat(".pdf");
                //old file path & name
                filepath = signedfilepath.concat(FILE_SEPARATOR).concat(dwEsignBean.getSigned_pdf_file());
                newpdffileName = esgfilename[0].concat("_").concat(esgfilename[1]).concat("_").concat(esgfilename[2]).concat(esignfiletype);
                //new file path & name
                newfilepath = signedfilepath.concat(FILE_SEPARATOR).concat(newpdffileName);
                //System.out.println("deletenewfile1:" + newfilepath);

            } else {

                pst = con.prepareStatement("select signed_pdf_file,signed_pdf_path from esign_log where bill_no=? and "
                        + "(unsigned_pdf_file is not null and unsigned_pdf_file<>'') and unsigned_pdf_path is not null");
                pst.setInt(1, dwEsignBean.getBillno());
                rs = pst.executeQuery();
                if (rs.next()) {
                    pdffileName = rs.getString("signed_pdf_file");
                    pdfpath = rs.getString("signed_pdf_path");
                    String[] esgfilename = pdffileName.split("_");
                    //System.out.println(esgfilename[0]);
                    //System.out.println("esgfilename:::" + esgfilename[0] + esgfilename[1] + esgfilename[2]);
                    presigntype = esgfilename[2];
                    //old file path & name                    
                    filepath = signedfilepath.concat(FILE_SEPARATOR).concat(pdffileName);

                    if (presigntype.equals("signed.pdf")) {
                        esignfiletype = "signed_1.pdf";
                        newpdffileName = esgfilename[0].concat("_").concat(esgfilename[1]).concat("_").concat(esignfiletype);
                        //new file path & name
                        newfilepath = signedfilepath.concat(FILE_SEPARATOR).concat(newpdffileName);
                        //System.out.println("deletenewfile2:" + newfilepath);
                    }

                }

            }

            System.out.println("filepath:" + filepath);
            System.out.println("new file path:" + newfilepath);

            File pdffile = new File(filepath);
            File newpdfesignfile = new File(newfilepath);
            boolean flag = pdffile.renameTo(newpdfesignfile);
            if (flag == true) {
                System.out.println("File Successfully Renamed");
            } else {
                System.out.println("Operation failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return newpdffileName;
    }

    @Override
    public int updateEsigndetails(DistrictWiseEsignBean dwEsignBean, String newpdffilename) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int resultQry = 0;

        try {
            con = this.dataSource.getConnection();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");

            pst = con.prepareStatement("INSERT INTO equarter.esign_delete_log(bill_no,off_code,signed_pdf_path,created_pdf_date,esign_pdf_date,report_ref_slno,signed_login_id,reference_no,transaction_no,"
                    + "signed_pdf_file,new_pdf_file,signature_type,esign_logid) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setInt(1, dwEsignBean.getBillno());
            pst.setString(2, dwEsignBean.getOfficeCode());
            //signed pdf path
            pst.setString(3, dwEsignBean.getSigned_pdf_path());
            pst.setTimestamp(4, new Timestamp(dateFormat.parse(dwEsignBean.getCreate_pdf_date()).getTime()));
            if (dwEsignBean.getEsign_pdf_date() != null && !dwEsignBean.getEsign_pdf_date().equals("")) {
                pst.setTimestamp(5, new Timestamp(dateFormat.parse(dwEsignBean.getEsign_pdf_date()).getTime()));
            } else {
                pst.setTimestamp(5, null);
            }

            pst.setInt(6, dwEsignBean.getReport_ref_slno());
            pst.setString(7, dwEsignBean.getSigned_login_id());
            pst.setString(8, dwEsignBean.getReference_no());
            pst.setString(9, dwEsignBean.getTransaction_no());
            //signed pdf file
            pst.setString(10, dwEsignBean.getSigned_pdf_file());
            pst.setString(11, newpdffilename);
            pst.setString(12, dwEsignBean.getSignature_type());
            pst.setInt(13, dwEsignBean.getId_esign_log());
            resultQry = pst.executeUpdate();

            /*pst = con.prepareStatement("update esign_log set unsigned_pdf_file=?,unsigned_pdf_path=?, signed_pdf_file=?,signed_pdf_path=?,create_pdf_date=?,esign_pdf_date=?,status=?,"
             + "report_ref_slno=?,login_id=?,reference_no=?,transaction_no=?,signature_type=? where bill_no=?");
             pst.setString(1, dwEsignBean.getUnsigned_pdf_file());
             pst.setString(2, dwEsignBean.getUnsigned_pdf_path());
             pst.setString(3, dwEsignBean.getSigned_pdf_file());
             pst.setString(4, dwEsignBean.getSigned_pdf_path());
             //pst.setTimestamp(5, null);
             //pst.setTimestamp(6, null);
             pst.setString(5, dwEsignBean.getCreate_pdf_date());
             pst.setString(6, dwEsignBean.getEsign_pdf_date());
             pst.setString(7, dwEsignBean.getBill_status());
             pst.setInt(8, dwEsignBean.getReport_ref_slno());
             pst.setString(9, null);
             pst.setString(10, dwEsignBean.getReference_no());
             pst.setString(11, dwEsignBean.getTransaction_no());
             pst.setString(12, dwEsignBean.getSignature_type());            
             pst.setInt(13, dwEsignBean.getBillno());
             pst.executeUpdate();*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return resultQry;
    }

    @Override
    public void deleteEsignBill(int billno, int esignId) {
        ResultSet rs = null;
        boolean flag = false;
        PreparedStatement pst = null;
        Connection con = null;
        String msg = null;
        DistrictWiseEsignBean dEsignBean = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        //SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("delete from esign_log where bill_no=? and id_esign_log=? ");
            pst.setInt(1, billno);
            pst.setInt(2, esignId);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.policemodule;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.policemodule.NominationForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.annotation.Resource;
import javax.sql.DataSource;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Manisha
 */
public class NominationRollRecommendCheckListDAOImpl implements NominationRollRecommendCheckListDAO {

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
    public void downloadNominationCheckListForSI2Inspector(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        int row = 0;
        try {
            con = this.repodataSource.getConnection();

            WritableSheet sheet = workbook.createSheet("Check List", 0);

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

            NominationForm nfm = new NominationForm();
            Label label = new Label(0, row, " Nomination for the post of SUB-INSPECTOR OF POLICE to INSPECTOR OF POLICE ", headcell);//column,row
            sheet.setRowView(row, 30 * 30);
            sheet.addCell(label);
            sheet.mergeCells(0, row, 25, row);

            row = row + 1;

            sheet.mergeCells(0, row, 0, row + 2);
            label = new Label(0, row, "SL No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(1, row, 1, row + 2);
            label = new Label(1, row, "Gradation Sl No", innerheadcell);
            sheet.setColumnView(1, 25);
            sheet.addCell(label);

            sheet.mergeCells(2, row, 2, row + 2);
            label = new Label(2, row, "Name ", innerheadcell);
            sheet.setColumnView(2, 50);
            sheet.addCell(label);

            sheet.mergeCells(3, row, 3, row + 2);
            label = new Label(3, row, "Father's Name", innerheadcell);
            sheet.setColumnView(3, 50);
            sheet.addCell(label);

            sheet.mergeCells(4, row, 4, row + 2);
            label = new Label(4, row, "Category", innerheadcell);
            sheet.setColumnView(4, 27);
            sheet.addCell(label);

            sheet.mergeCells(5, row, 5, row + 2);
            label = new Label(5, row, "Present place of Posting", innerheadcell);
            sheet.setColumnView(5, 27);
            sheet.addCell(label);

            sheet.mergeCells(6, row, 6, row + 2);
            label = new Label(6, row, "Date of Birth", innerheadcell);
            sheet.setColumnView(6, 27);
            sheet.addCell(label);

            sheet.mergeCells(7, row, 7, row + 2);
            label = new Label(7, row, "Date of enlistment ", innerheadcell);
            sheet.setColumnView(7, 27);
            sheet.addCell(label);

            sheet.mergeCells(8, row, 8, row + 2);
            label = new Label(8, row, "Date of Passing SI's course of training", innerheadcell);
            sheet.setColumnView(7, 27);
            sheet.addCell(label);

            sheet.mergeCells(9, row, 9, row + 2);
            label = new Label(9, row, "Departmental Proceedings/Vigilance/HRPC/Criminal Case pending, if any. ", innerheadcell);
            sheet.setColumnView(9, 27);
            sheet.addCell(label);

            label = new Label(10, row, "Punishment", innerheadcell);
            sheet.setColumnView(10, 20);
            sheet.addCell(label);
            sheet.mergeCells(10, row, 15, row);

            sheet.mergeCells(16, row, 16, row + 2);
            label = new Label(16, row, "Charge Served Detail", innerheadcell);
            sheet.setColumnView(16, 28);
            sheet.addCell(label);

            sheet.mergeCells(17, row, 17, row + 2);
            label = new Label(17, row, "Views of the Medical Officer", innerheadcell);
            sheet.setColumnView(17, 28);
            sheet.addCell(label);

            sheet.mergeCells(18, row, 18, row + 2);
            label = new Label(18, row, "whether property statement submitted", innerheadcell);
            sheet.setColumnView(18, 28);
            sheet.addCell(label);

            sheet.mergeCells(19, row, 19, row + 2);
            label = new Label(19, row, "property statement submitted By HRMS", innerheadcell);
            sheet.setColumnView(19, 28);
            sheet.addCell(label);

            sheet.mergeCells(20, row, 20, row + 2);
            label = new Label(20, row, "property statement submitted By Officer", innerheadcell);
            sheet.setColumnView(20, 28);
            sheet.addCell(label);

            sheet.mergeCells(21, row, 21, row + 2);
            label = new Label(21, row, "General Suitability", innerheadcell);
            sheet.setColumnView(21, 28);
            sheet.addCell(label);

            sheet.mergeCells(22, row, 22, row + 2);
            label = new Label(22, row, "Recommendation Status of Nominating Authority", innerheadcell);
            sheet.setColumnView(22, 28);
            sheet.addCell(label);

            sheet.mergeCells(23, row, 23, row + 2);
            label = new Label(23, row, "Remarks of Nominating Authority", innerheadcell);
            sheet.setColumnView(23, 28);
            sheet.addCell(label);

            sheet.mergeCells(24, row, 24, row + 2);
            label = new Label(24, row, "Recommendation Status Of Recommended Authority", innerheadcell);
            sheet.setColumnView(24, 28);
            sheet.addCell(label);

            sheet.mergeCells(25, row, 25, row + 2);
            label = new Label(25, row, "Remarks of Recommended Authority", innerheadcell);
            sheet.setColumnView(25, 28);
            sheet.addCell(label);

            sheet.mergeCells(26, row, 26, row + 2);
            label = new Label(26, row, "Nomination Received From District/Establishment", innerheadcell);
            sheet.setColumnView(26, 28);
            sheet.addCell(label);

            sheet.mergeCells(27, row, 27, row + 2);
            label = new Label(27, row, "Application Id", innerheadcell);
            sheet.setColumnView(27, 28);
            sheet.addCell(label);

            sheet.mergeCells(28, row, 28, row + 2);
            label = new Label(28, row, "Employee Id", innerheadcell);
            sheet.setColumnView(28, 29);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(10, row, "Prior to 01.01.2018", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(10, row, 11, row);

            label = new Label(12, row, "During last 5 years i.e. w.e.f. 1.1.2018 to 31.12.2022", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(12, row, 13, row);

            label = new Label(14, row, "From 1.1.23 till date of submission of Nomination Rolls", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(14, row, 15, row);

            row = row + 1;

            label = new Label(10, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(11, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(12, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(13, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(14, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(15, row, "Minor", innerheadcell);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(0, row, "1", innerheadcell);
            sheet.addCell(label);

            label = new Label(1, row, "2", innerheadcell);
            sheet.addCell(label);

            label = new Label(2, row, "3", innerheadcell);
            sheet.addCell(label);

            label = new Label(3, row, "4", innerheadcell);
            sheet.addCell(label);

            label = new Label(4, row, "5", innerheadcell);
            sheet.addCell(label);

            label = new Label(5, row, "6", innerheadcell);
            sheet.addCell(label);

            label = new Label(6, row, "7", innerheadcell);
            sheet.addCell(label);

            label = new Label(7, row, "8", innerheadcell);
            sheet.addCell(label);

            label = new Label(8, row, "9", innerheadcell);
            sheet.addCell(label);

            label = new Label(9, row, "10", innerheadcell);
            sheet.addCell(label);

            label = new Label(10, row, "11", innerheadcell);
            sheet.addCell(label);

            label = new Label(11, row, "12", innerheadcell);
            sheet.addCell(label);

            label = new Label(12, row, "13", innerheadcell);
            sheet.addCell(label);

            label = new Label(13, row, "14", innerheadcell);
            sheet.addCell(label);

            label = new Label(14, row, "15", innerheadcell);
            sheet.addCell(label);

            label = new Label(15, row, "16", innerheadcell);
            sheet.addCell(label);

            label = new Label(16, row, "17", innerheadcell);
            sheet.addCell(label);

            label = new Label(17, row, "18", innerheadcell);
            sheet.addCell(label);

            label = new Label(18, row, "19", innerheadcell);
            sheet.addCell(label);

            label = new Label(19, row, "20", innerheadcell);
            sheet.addCell(label);

            label = new Label(20, row, "21", innerheadcell);
            sheet.addCell(label);

            label = new Label(21, row, "22", innerheadcell);
            sheet.addCell(label);

            label = new Label(22, row, "23", innerheadcell);
            sheet.addCell(label);

            label = new Label(23, row, "24", innerheadcell);
            sheet.addCell(label);

            label = new Label(24, row, "25", innerheadcell);
            sheet.addCell(label);

            label = new Label(25, row, "26", innerheadcell);
            sheet.addCell(label);

            label = new Label(26, row, "27", innerheadcell);
            sheet.addCell(label);

            label = new Label(27, row, "28", innerheadcell);
            sheet.addCell(label);

            label = new Label(28, row, "29", innerheadcell);
            sheet.addCell(label);

            pst = con.prepareStatement("select empid,nomination_form_id, grade_serial_no,off_name_abbr, fullname, fathersname, category, to_char(dob, 'DD-Mon-YYYY') dob, "
                    + " to_char(postingunintdoj, 'DD-Mon-YYYY') postingunintdoj , passing_training_date, place_posting, to_char(doegov, 'DD-Mon-YYYY') doegov, "
                    + " to_char(to_date(jodinspector,'YYYY-MM-DD'), 'DD-Mon-YYYY') jodinspector, punishmentmajorprior, punishmentmajorduring, punishmentmajorfrom, \n"
                    + "  punishmentminorprior, punishmentminorduring, punishmentminorfrom, dpcifany, disc_details, remarks_of_cdmo, dateofserving,dateofserving_if_any_dp, to_char(dor, 'DD-Mon-YYYY') dor, "
                    + " remarksnominationgeneral,recommend_status , off_name_abbr, gradation_slno, pnm.entry_year,property_submitted_on_hrms,property_submitted_on_hrms_byofficer,remarksnominationgeneral, "
                    + " district_recommendation_status,property_submitted_if_any,district_recommendation_view,remarks_recommend_authority  from police_nomination_master pnm "
                    + " inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
                    + " inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid "
                    + " inner join police_office_list_data pold on pnm.created_office=pold.police_off_code "
                    + " where is_submitted='Y' and nomination_for_new_rank=? and is_archived='N' and pnm.entry_year=? "
                    + " order by gradation_slno ");
            int i = 0;
            pst.setString(1, nominatedForPost);
            pst.setString(2, fiscalyear);
            rs = pst.executeQuery();

            while (rs.next()) {

                i++;
                row = row + 1;
                label = new Label(0, row, i + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(1, row, rs.getInt("grade_serial_no") + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(2, row, rs.getString("fullname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(3, row, rs.getString("fathersname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(4, row, rs.getString("category"), innerheadcell);
                sheet.addCell(label);

                label = new Label(5, row, rs.getString("place_posting"), innerheadcell);
                sheet.addCell(label);

                label = new Label(6, row, rs.getString("dob"), innerheadcell);
                sheet.addCell(label);

                label = new Label(7, row, rs.getString("doegov"), innerheadcell);
                sheet.addCell(label);

                label = new Label(8, row, CommonFunctions.getFormattedOutputDate1(rs.getDate("passing_training_date")), innerheadcell);
                sheet.addCell(label);

                /*pst1 = con.prepareStatement("SELECT * FROM police_nomination_form_different_proceeding where nomination_form_id=? order by proceeding_detail_id");
                 pst1.setInt(1, rs.getInt("nomination_form_id"));
                 rs1 = pst1.executeQuery();

                 if (rs1.next()) {
                 label = new Label(9, row, rs1.getString("proceeding_detail"), innerheadcell);
                 sheet.addCell(label);
                 } else {
                 label = new Label(9, row, "", innerheadcell);
                 sheet.addCell(label);
                 } */
                if (rs.getString("dpcifany") != null && rs.getString("dpcifany").equals("Y")) {
                    label = new Label(9, row, "Yes" + "\n" + rs.getString("disc_details"), innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(9, row, "No", innerheadcell);
                    sheet.addCell(label);
                }

                label = new Label(10, row, rs.getString("punishmentmajorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(11, row, rs.getString("punishmentminorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(12, row, rs.getString("punishmentmajorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(13, row, rs.getString("punishmentminorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(14, row, rs.getString("punishmentmajorfrom"), innerheadcell);
                sheet.addCell(label);

                label = new Label(15, row, rs.getString("punishmentminorfrom"), innerheadcell);
                sheet.addCell(label);

                if (rs.getString("dateofserving_if_any_dp") != null && rs.getString("dateofserving_if_any_dp").equals("Y")) {
                    label = new Label(16, row, "Yes" + "\n" + rs.getString("dateofserving"), innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(16, row, "No", innerheadcell);
                    sheet.addCell(label);
                }

                label = new Label(17, row, rs.getString("remarks_of_cdmo"), innerheadcell);
                sheet.addCell(label);

                label = new Label(18, row, rs.getString("property_submitted_if_any"), innerheadcell);
                sheet.addCell(label);

                label = new Label(19, row, rs.getString("property_submitted_on_hrms"), innerheadcell);
                sheet.addCell(label);

                label = new Label(20, row, rs.getString("property_submitted_on_hrms_byofficer"), innerheadcell);
                sheet.addCell(label);

                label = new Label(21, row, rs.getString("remarksnominationgeneral"), innerheadcell);
                sheet.addCell(label);

                label = new Label(22, row, rs.getString("district_recommendation_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(23, row, rs.getString("district_recommendation_view"), innerheadcell);
                sheet.addCell(label);

                label = new Label(24, row, rs.getString("recommend_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(25, row, rs.getString("remarks_recommend_authority"), innerheadcell);
                sheet.addCell(label);

                label = new Label(26, row, rs.getString("off_name_abbr"), innerheadcell);
                sheet.addCell(label);

                label = new Label(27, row, rs.getString("nomination_form_id"), innerheadcell);
                sheet.addCell(label);

                label = new Label(28, row, rs.getString("empid"), innerheadcell);
                sheet.addCell(label);
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

    @Autowired
    public void downloadNominationCheckListForHavildar2HavildarMajor(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        int row = 0;
        try {
            con = this.repodataSource.getConnection();

            WritableSheet sheet = workbook.createSheet("Check List", 0);

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

            NominationForm nfm = new NominationForm();
            Label label = new Label(0, row, " Nomination for the post of HAVILDAR OF POLICE to HAVILDAR MAJOR OF POLICE ", headcell);//column,row
            sheet.setRowView(row, 30 * 30);
            sheet.addCell(label);
            sheet.mergeCells(0, row, 25, row);

            row = row + 1;

            sheet.mergeCells(0, row, 0, row + 2);
            label = new Label(0, row, "SL No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(1, row, 1, row + 2);
            label = new Label(1, row, "Gradation Sl No", innerheadcell);
            sheet.setColumnView(1, 25);
            sheet.addCell(label);

            sheet.mergeCells(2, row, 2, row + 2);
            label = new Label(2, row, "Name ", innerheadcell);
            sheet.setColumnView(2, 50);
            sheet.addCell(label);

            sheet.mergeCells(3, row, 3, row + 2);
            label = new Label(3, row, "Father's Name", innerheadcell);
            sheet.setColumnView(3, 50);
            sheet.addCell(label);

            sheet.mergeCells(4, row, 4, row + 2);
            label = new Label(4, row, "Category", innerheadcell);
            sheet.setColumnView(4, 23);
            sheet.addCell(label);

            sheet.mergeCells(5, row, 5, row + 2);
            label = new Label(5, row, "Date of Birth", innerheadcell);
            sheet.setColumnView(5, 23);
            sheet.addCell(label);

            sheet.mergeCells(6, row, 6, row + 2);
            label = new Label(6, row, "Date of enlistment", innerheadcell);
            sheet.setColumnView(6, 23);
            sheet.addCell(label);

            sheet.mergeCells(7, row, 7, row + 2);
            label = new Label(7, row, "Date of Passing DIC course of training ", innerheadcell);
            sheet.setColumnView(7, 23);
            sheet.addCell(label);

            sheet.mergeCells(8, row, 8, row + 2);
            label = new Label(8, row, "Date of joining in the rank of Havildar", innerheadcell);
            sheet.setColumnView(7, 23);
            sheet.addCell(label);

            sheet.mergeCells(9, row, 9, row + 2);
            label = new Label(9, row, "Departmental Proceedings/Vigilance/HRPC/Criminal Case pending, if any. ", innerheadcell);
            sheet.setColumnView(9, 23);
            sheet.addCell(label);

            label = new Label(10, row, "Punishment", innerheadcell);
            sheet.setColumnView(10, 20);
            sheet.addCell(label);
            sheet.mergeCells(10, row, 15, row);

            sheet.mergeCells(16, row, 16, row + 2);
            label = new Label(16, row, "General Suitability", innerheadcell);
            sheet.setColumnView(16, 23);
            sheet.addCell(label);

            sheet.mergeCells(17, row, 17, row + 2);
            label = new Label(17, row, "Recommendation Status of Nominating Authority", innerheadcell);
            sheet.setColumnView(17, 23);
            sheet.addCell(label);

            sheet.mergeCells(18, row, 18, row + 2);
            label = new Label(18, row, "Remarks of Nominating Authority", innerheadcell);
            sheet.setColumnView(18, 23);
            sheet.addCell(label);

            sheet.mergeCells(19, row, 19, row + 2);
            label = new Label(19, row, "Recommendation Status Of Recommended Authority", innerheadcell);
            sheet.setColumnView(19, 23);
            sheet.addCell(label);

            sheet.mergeCells(20, row, 20, row + 2);
            label = new Label(20, row, "Remarks of Recommended Authority", innerheadcell);
            sheet.setColumnView(20, 23);
            sheet.addCell(label);

            sheet.mergeCells(21, row, 21, row + 2);
            label = new Label(21, row, "Nomination Received From District/Establishment", innerheadcell);
            sheet.setColumnView(21, 23);
            sheet.addCell(label);

            sheet.mergeCells(22, row, 22, row + 2);
            label = new Label(22, row, "Application Id", innerheadcell);
            sheet.setColumnView(22, 23);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(10, row, "Prior to 01.01.2016", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(10, row, 11, row);

            label = new Label(12, row, "During last 5 years i.e. w.e.f. 1.1.2016 to 31.12.2020", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(12, row, 13, row);

            label = new Label(14, row, "From 1.1.21 till date of submission of Nomination Rolls", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(14, row, 15, row);

            row = row + 1;

            label = new Label(10, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(11, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(12, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(13, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(14, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(15, row, "Minor", innerheadcell);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(0, row, "1", innerheadcell);
            sheet.addCell(label);

            label = new Label(1, row, "2", innerheadcell);
            sheet.addCell(label);

            label = new Label(2, row, "3", innerheadcell);
            sheet.addCell(label);

            label = new Label(3, row, "4", innerheadcell);
            sheet.addCell(label);

            label = new Label(4, row, "5", innerheadcell);
            sheet.addCell(label);

            label = new Label(5, row, "6", innerheadcell);
            sheet.addCell(label);

            label = new Label(6, row, "7", innerheadcell);
            sheet.addCell(label);

            label = new Label(7, row, "8", innerheadcell);
            sheet.addCell(label);

            label = new Label(8, row, "9", innerheadcell);
            sheet.addCell(label);

            label = new Label(9, row, "10", innerheadcell);
            sheet.addCell(label);

            label = new Label(10, row, "11", innerheadcell);
            sheet.addCell(label);

            label = new Label(11, row, "12", innerheadcell);
            sheet.addCell(label);

            label = new Label(12, row, "13", innerheadcell);
            sheet.addCell(label);

            label = new Label(13, row, "14", innerheadcell);
            sheet.addCell(label);

            label = new Label(14, row, "15", innerheadcell);
            sheet.addCell(label);

            label = new Label(15, row, "16", innerheadcell);
            sheet.addCell(label);

            label = new Label(16, row, "17", innerheadcell);
            sheet.addCell(label);

            label = new Label(17, row, "18", innerheadcell);
            sheet.addCell(label);

            label = new Label(18, row, "19", innerheadcell);
            sheet.addCell(label);

            label = new Label(19, row, "20", innerheadcell);
            sheet.addCell(label);

            label = new Label(20, row, "21", innerheadcell);
            sheet.addCell(label);

            label = new Label(21, row, "22", innerheadcell);
            sheet.addCell(label);

            pst = con.prepareStatement("select nomination_form_id, gradation_number,off_name_abbr, fullname, fathersname, category, to_char(dob, 'DD-Mon-YYYY') dob, "
                    + " to_char(postingunintdoj, 'DD-Mon-YYYY') postingunintdoj , passing_training_date, place_posting, to_char(doegov, 'DD-Mon-YYYY') doegov, "
                    + " to_char(to_date(jodinspector,'YYYY-MM-DD'), 'DD-Mon-YYYY') jodinspector, punishmentmajorprior, punishmentmajorduring, punishmentmajorfrom, \n"
                    + " date_of_distraining,punishmentminorprior, punishmentminorduring, punishmentminorfrom, dpcifany, disc_details, remarks_of_cdmo, dateofserving, to_char(dor, 'DD-Mon-YYYY') dor, "
                    + " remarksnominationgeneral,recommend_status , off_name_abbr, gradation_slno, pnm.entry_year,property_submitted_on_hrms,property_submitted_on_hrms_byofficer,remarksnominationgeneral, "
                    + " district_recommendation_status,property_submitted_if_any,district_recommendation_view,remarks_recommend_authority  from police_nomination_master pnm "
                    + " inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
                    + " inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid "
                    + " inner join police_office_list_data pold on pnm.created_office=pold.police_off_code "
                    + " where is_submitted='Y' and nomination_for_new_rank=? and is_archived='N' and pnm.entry_year=? "
                    + " order by gradation_slno ");
            int i = 0;
            pst.setString(1, nominatedForPost);
            pst.setString(2, fiscalyear);
            rs = pst.executeQuery();

            while (rs.next()) {

                i++;
                row = row + 1;
                label = new Label(0, row, i + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(1, row, rs.getInt("gradation_slno") + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(2, row, rs.getString("fullname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(3, row, rs.getString("fathersname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(4, row, rs.getString("category"), innerheadcell);
                sheet.addCell(label);

                label = new Label(5, row, rs.getString("dob"), innerheadcell);
                sheet.addCell(label);

                label = new Label(6, row, rs.getString("doegov"), innerheadcell);
                sheet.addCell(label);

                label = new Label(7, row, CommonFunctions.getFormattedOutputDate1(rs.getDate("date_of_distraining")), innerheadcell);
                sheet.addCell(label);

                label = new Label(8, row, rs.getString("jodInspector"), innerheadcell);
                sheet.addCell(label);

                /*if (rs1.next()){
                 } else {
                 label = new Label(9, row, "", innerheadcell);
                 sheet.addCell(label);
                 }
                 */
                pst1 = con.prepareStatement("SELECT * FROM police_nomination_form_different_proceeding where nomination_form_id=? order by proceeding_detail_id");
                pst1.setInt(1, rs.getInt("nomination_form_id"));
                rs1 = pst1.executeQuery();

                if (rs1.next()) {
                    label = new Label(9, row, rs1.getString("proceeding_detail"), innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(9, row, "", innerheadcell);
                    sheet.addCell(label);
                }

                label = new Label(10, row, rs.getString("punishmentmajorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(11, row, rs.getString("punishmentminorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(12, row, rs.getString("punishmentmajorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(13, row, rs.getString("punishmentminorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(14, row, rs.getString("punishmentmajorfrom"), innerheadcell);
                sheet.addCell(label);

                label = new Label(15, row, rs.getString("punishmentminorfrom"), innerheadcell);
                sheet.addCell(label);

                label = new Label(16, row, rs.getString("remarksnominationgeneral"), innerheadcell);
                sheet.addCell(label);

                label = new Label(17, row, rs.getString("district_recommendation_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(18, row, rs.getString("district_recommendation_view"), innerheadcell);
                sheet.addCell(label);

                label = new Label(19, row, rs.getString("recommend_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(20, row, rs.getString("remarks_recommend_authority"), innerheadcell);
                sheet.addCell(label);

                label = new Label(21, row, rs.getString("off_name_abbr"), innerheadcell);
                sheet.addCell(label);

                label = new Label(22, row, rs.getString("nomination_form_id"), innerheadcell);
                sheet.addCell(label);
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

    @Autowired
    public void downloadNominationCheckListForInspector2DSP(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        int row = 0;
        try {
            con = this.repodataSource.getConnection();

            WritableSheet sheet = workbook.createSheet("Check List", 0);

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

            NominationForm nfm = new NominationForm();
            Label label = new Label(0, row, " Nomination for the post of INSPECTOR OF POLICE FOR PROMOTION TO DEPUTY SUPERINTENDENT OF POLICE ", headcell);//column,row
            sheet.setRowView(row, 30 * 30);
            sheet.addCell(label);
            sheet.mergeCells(0, row, 24, row);

            row = row + 1;

            sheet.mergeCells(0, row, 0, row + 2);
            label = new Label(0, row, "SL No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(1, row, 1, row + 2);
            label = new Label(1, row, "Gradation Sl No", innerheadcell);
            sheet.setColumnView(1, 26);
            sheet.addCell(label);

            sheet.mergeCells(2, row, 2, row + 2);
            label = new Label(2, row, "Hrms Id", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(3, row, 3, row + 2);
            label = new Label(3, row, "Gpf No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(4, row, 4, row + 2);
            label = new Label(4, row, "Name ", innerheadcell);
            sheet.setColumnView(4, 50);
            sheet.addCell(label);

            sheet.mergeCells(5, row, 5, row + 2);
            label = new Label(5, row, "Father's Name", innerheadcell);
            sheet.setColumnView(5, 50);
            sheet.addCell(label);

            sheet.mergeCells(6, row, 6, row + 2);
            label = new Label(6, row, "Category", innerheadcell);
            sheet.setColumnView(6, 27);
            sheet.addCell(label);

            sheet.mergeCells(7, row, 7, row + 2);
            label = new Label(7, row, "Date of Birth", innerheadcell);
            sheet.setColumnView(7, 27);
            sheet.addCell(label);

            sheet.mergeCells(8, row, 8, row + 2);
            label = new Label(8, row, "Date of entry into Govt. Service", innerheadcell);
            sheet.setColumnView(8, 27);
            sheet.addCell(label);

            sheet.mergeCells(9, row, 9, row + 2);
            label = new Label(9, row, "Date of appointent in the Present Grade", innerheadcell);
            sheet.setColumnView(9, 27);
            sheet.addCell(label);

            sheet.mergeCells(10, row, 10, row + 2);
            label = new Label(10, row, "Status of disciplinary/Proceeding/Criminal/Vigilance proceeding if any. ", innerheadcell);
            sheet.setColumnView(10, 26);
            sheet.addCell(label);

            sheet.mergeCells(11, row, 11, row + 2);
            label = new Label(11, row, "disciplinary/Proceeding/Criminal/Vigilance proceeding Details. ", innerheadcell);
            sheet.setColumnView(11, 27);
            sheet.addCell(label);

            label = new Label(12, row, "Punishment", innerheadcell);
            sheet.setColumnView(12, 27);
            sheet.addCell(label);
            sheet.mergeCells(12, row, 17, row);

            sheet.mergeCells(18, row, 18, row + 2);
            label = new Label(18, row, "Whether filling uptodate property statement By HRMS", innerheadcell);
            sheet.setColumnView(18, 27);
            sheet.addCell(label);

            sheet.mergeCells(19, row, 19, row + 2);
            label = new Label(19, row, "Whether filling uptodate property statement By Officer", innerheadcell);
            sheet.setColumnView(19, 27);
            sheet.addCell(label);

            sheet.mergeCells(20, row, 20, row + 2);
            label = new Label(20, row, "Date Of filling uptodate property statement By Officer", innerheadcell);
            sheet.setColumnView(20, 27);
            sheet.addCell(label);

            sheet.mergeCells(21, row, 21, row + 2);
            label = new Label(21, row, "Recommendation Status of Nominating Authority", innerheadcell);
            sheet.setColumnView(21, 27);
            sheet.addCell(label);

            sheet.mergeCells(22, row, 22, row + 2);
            label = new Label(22, row, "Remarks of Nominating Authority", innerheadcell);
            sheet.setColumnView(22, 27);
            sheet.addCell(label);

            sheet.mergeCells(23, row, 23, row + 2);
            label = new Label(23, row, "ReCommendation Status of Recommended Authority", innerheadcell);
            sheet.setColumnView(23, 27);
            sheet.addCell(label);

            sheet.mergeCells(24, row, 24, row + 2);
            label = new Label(24, row, "Remarks of Recommended Authority", innerheadcell);
            sheet.setColumnView(24, 27);
            sheet.addCell(label);

            sheet.mergeCells(25, row, 25, row + 2);
            label = new Label(25, row, "Nomination Received From District/Establishment", innerheadcell);
            sheet.setColumnView(25, 27);
            sheet.addCell(label);

            sheet.mergeCells(26, row, 26, row + 2);
            label = new Label(26, row, "Application Id", innerheadcell);
            sheet.setColumnView(26, 27);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(12, row, "Prior to 01.01.2017", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(12, row, 13, row);

            label = new Label(14, row, "During last 5 years i.e. w.e.f. 1.1.2017 to 31.12.2021", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(14, row, 15, row);

            label = new Label(16, row, "From 1.1.22 till date of submission of Nomination Rolls", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(16, row, 17, row);

            row = row + 1;

            label = new Label(12, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(13, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(14, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(15, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(16, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(17, row, "Minor", innerheadcell);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(0, row, "1", innerheadcell);
            sheet.addCell(label);

            label = new Label(1, row, "2", innerheadcell);
            sheet.addCell(label);

            label = new Label(2, row, "3", innerheadcell);
            sheet.addCell(label);

            label = new Label(3, row, "4", innerheadcell);
            sheet.addCell(label);

            label = new Label(4, row, "5", innerheadcell);
            sheet.addCell(label);

            label = new Label(5, row, "6", innerheadcell);
            sheet.addCell(label);

            label = new Label(6, row, "7", innerheadcell);
            sheet.addCell(label);

            label = new Label(7, row, "8", innerheadcell);
            sheet.addCell(label);

            label = new Label(8, row, "9", innerheadcell);
            sheet.addCell(label);

            label = new Label(9, row, "10", innerheadcell);
            sheet.addCell(label);

            label = new Label(10, row, "11", innerheadcell);
            sheet.addCell(label);

            label = new Label(11, row, "12", innerheadcell);
            sheet.addCell(label);

            label = new Label(12, row, "13", innerheadcell);
            sheet.addCell(label);

            label = new Label(13, row, "14", innerheadcell);
            sheet.addCell(label);

            label = new Label(14, row, "15", innerheadcell);
            sheet.addCell(label);

            label = new Label(15, row, "16", innerheadcell);
            sheet.addCell(label);

            label = new Label(16, row, "17", innerheadcell);
            sheet.addCell(label);

            label = new Label(17, row, "18", innerheadcell);
            sheet.addCell(label);

            label = new Label(18, row, "19", innerheadcell);
            sheet.addCell(label);

            label = new Label(19, row, "20", innerheadcell);
            sheet.addCell(label);

            label = new Label(20, row, "21", innerheadcell);
            sheet.addCell(label);

            label = new Label(21, row, "22", innerheadcell);
            sheet.addCell(label);

            label = new Label(22, row, "23", innerheadcell);
            sheet.addCell(label);

            label = new Label(23, row, "24", innerheadcell);
            sheet.addCell(label);

            label = new Label(24, row, "25", innerheadcell);
            sheet.addCell(label);

            label = new Label(25, row, "26", innerheadcell);
            sheet.addCell(label);

            label = new Label(26, row, "27", innerheadcell);
            sheet.addCell(label);

            pst = con.prepareStatement("select property_submitted_if_any,grade_serial_no,dpcifany,proceeding_detail,pnf.nomination_form_id, gradation_number,em.emp_id,pnd.gpf_no,off_name_abbr, fullname, fathersname, em.category, to_char(em.dob, 'DD-Mon-YYYY') dob, "
                    + "to_char(postingunintdoj, 'DD-Mon-YYYY') postingunintdoj , passing_training_date, place_posting, to_char(doegov, 'DD-Mon-YYYY') doegov, "
                    + "to_char(to_date(jodinspector,'YYYY-MM-DD'), 'DD-Mon-YYYY') jodinspector, punishmentmajorprior, punishmentmajorduring, punishmentmajorfrom, "
                    + " punishmentminorprior, punishmentminorduring, punishmentminorfrom, dpcifany, disc_details, remarks_of_cdmo, dateofserving, to_char(dor, 'DD-Mon-YYYY') dor, "
                    + "remarksnominationgeneral,recommend_status , off_name_abbr, gradation_slno, pnm.entry_year,property_submitted_on_hrms,property_submitted_on_hrms_byofficer,"
                    + "district_recommendation_status,district_recommendation_view,remarks_recommend_authority  from police_nomination_master pnm "
                    + "inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
                    + "inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid "
                    + "inner join police_office_list_data pold on pnm.created_office=pold.police_off_code "
                    + "inner join emp_mast em on pnd.gpf_no=em.gpf_no "
                    + "left outer join police_nomination_form_different_proceeding on pnf.nomination_form_id = police_nomination_form_different_proceeding.nomination_form_id "
                    + "where is_submitted='Y' and nomination_for_new_rank=? and is_archived='N' and pnm.entry_year=? order by grade_serial_no asc");
            int i = 0;
            pst.setString(1, nominatedForPost);
            pst.setString(2, fiscalyear);
            rs = pst.executeQuery();
            while (rs.next()) {

                i++;
                row = row + 1;
                label = new Label(0, row, i + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(1, row, rs.getInt("grade_serial_no") + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(2, row, rs.getString("emp_id"), innerheadcell);
                sheet.addCell(label);

                label = new Label(3, row, rs.getString("gpf_no"), innerheadcell);
                sheet.addCell(label);

                label = new Label(4, row, rs.getString("fullname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(5, row, rs.getString("fathersname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(6, row, rs.getString("category"), innerheadcell);
                sheet.addCell(label);

                label = new Label(7, row, rs.getString("dob"), innerheadcell);
                sheet.addCell(label);

                label = new Label(8, row, rs.getString("doegov"), innerheadcell);
                sheet.addCell(label);

                label = new Label(9, row, rs.getString("jodinspector"), innerheadcell);
                sheet.addCell(label);

                if (nfm.getDpcifany() != null && !nfm.getDpcifany().equals("") && nfm.getDpcifany().equals("Y")) {
                    label = new Label(10, row, "Yes", innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(10, row, "No", innerheadcell);
                    sheet.addCell(label);
                }

                label = new Label(11, row, rs.getString("proceeding_detail"), innerheadcell);
                sheet.addCell(label);

                label = new Label(12, row, rs.getString("punishmentmajorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(13, row, rs.getString("punishmentminorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(14, row, rs.getString("punishmentmajorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(15, row, rs.getString("punishmentminorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(16, row, rs.getString("punishmentmajorfrom"), innerheadcell);
                sheet.addCell(label);

                label = new Label(17, row, rs.getString("punishmentminorfrom"), innerheadcell);
                sheet.addCell(label);

                label = new Label(18, row, rs.getString("property_submitted_on_hrms"), innerheadcell);
                sheet.addCell(label);

                label = new Label(19, row, rs.getString("property_submitted_if_any"), innerheadcell);
                sheet.addCell(label);

                label = new Label(20, row, rs.getString("property_submitted_on_hrms_byofficer"), innerheadcell);
                sheet.addCell(label);

                label = new Label(21, row, rs.getString("district_recommendation_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(22, row, rs.getString("district_recommendation_view"), innerheadcell);
                sheet.addCell(label);

                label = new Label(23, row, rs.getString("recommend_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(24, row, rs.getString("remarks_recommend_authority"), innerheadcell);
                sheet.addCell(label);

                label = new Label(25, row, rs.getString("off_name_abbr"), innerheadcell);
                sheet.addCell(label);

                label = new Label(26, row, rs.getString("nomination_form_id"), innerheadcell);
                sheet.addCell(label);

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

    @Autowired
    public void downloadNominationCheckListForASI2SI(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        int row = 0;
        try {
            con = this.repodataSource.getConnection();

            WritableSheet sheet = workbook.createSheet("Check List", 0);

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

            NominationForm nfm = new NominationForm();
            Label label = new Label(0, row, " Nomination for the post of ASSISTANT SUB INSPECTOR OF POLICE FOR PROMOTION TO INSPECTOR DEPUTY SUPERINTENDENT OF POLICE ", headcell);//column,row
            sheet.setRowView(row, 30 * 30);
            sheet.addCell(label);
            sheet.mergeCells(0, row, 24, row);

            row = row + 1;

            sheet.mergeCells(0, row, 0, row + 2);
            label = new Label(0, row, "SL No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(1, row, 1, row + 2);
            label = new Label(1, row, "Gradation Sl No", innerheadcell);
            sheet.setColumnView(1, 33);
            sheet.addCell(label);

            sheet.mergeCells(2, row, 2, row + 2);
            label = new Label(2, row, "Hrms Id", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(3, row, 3, row + 2);
            label = new Label(3, row, "Gpf No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(4, row, 4, row + 2);
            label = new Label(4, row, "Name ", innerheadcell);
            sheet.setColumnView(4, 50);
            sheet.addCell(label);

            sheet.mergeCells(5, row, 5, row + 2);
            label = new Label(5, row, "Father's Name", innerheadcell);
            sheet.setColumnView(5, 50);
            sheet.addCell(label);

            sheet.mergeCells(6, row, 6, row + 2);
            label = new Label(6, row, "Category", innerheadcell);
            sheet.setColumnView(6, 32);
            sheet.addCell(label);

            sheet.mergeCells(7, row, 7, row + 2);
            label = new Label(7, row, "Date of Birth", innerheadcell);
            sheet.setColumnView(7, 32);
            sheet.addCell(label);

            sheet.mergeCells(8, row, 8, row + 2);
            label = new Label(8, row, "Date of entry into Govt. Service", innerheadcell);
            sheet.setColumnView(8, 32);
            sheet.addCell(label);

            sheet.mergeCells(9, row, 9, row + 2);
            label = new Label(9, row, "Date of Joining in the rank of ASI'S", innerheadcell);
            sheet.setColumnView(9, 32);
            sheet.addCell(label);

            sheet.mergeCells(10, row, 10, row + 2);
            label = new Label(10, row, "Date of Passing ASI's course of training", innerheadcell);
            sheet.setColumnView(10, 32);
            sheet.addCell(label);

            sheet.mergeCells(11, row, 11, row + 2);
            label = new Label(11, row, "Status of disciplinary/Proceeding/Criminal/Vigilance proceeding if any. ", innerheadcell);
            sheet.setColumnView(11, 32);
            sheet.addCell(label);

            sheet.mergeCells(12, row, 12, row + 2);
            label = new Label(12, row, "disciplinary/Proceeding/Criminal/Vigilance proceeding Details. ", innerheadcell);
            sheet.setColumnView(12, 32);
            sheet.addCell(label);

            label = new Label(13, row, "Punishment", innerheadcell);
            sheet.setColumnView(13, 32);
            sheet.addCell(label);
            sheet.mergeCells(13, row, 18, row);

            sheet.mergeCells(19, row, 19, row + 2);
            label = new Label(19, row, "Punishment Details", innerheadcell);
            sheet.setColumnView(19, 32);
            sheet.addCell(label);

            sheet.mergeCells(20, row, 20, row + 2);
            label = new Label(20, row, "Status of If charge has been served on the nominee in Departmental Proceeding ", innerheadcell);
            sheet.setColumnView(20, 32);
            sheet.addCell(label);

            sheet.mergeCells(21, row, 21, row + 2);
            label = new Label(21, row, "Serving Charge Detail ", innerheadcell);
            sheet.setColumnView(21, 32);
            sheet.addCell(label);

            sheet.mergeCells(22, row, 22, row + 2);
            label = new Label(22, row, "Views of the Medical Officer", innerheadcell);
            sheet.setColumnView(22, 32);
            sheet.addCell(label);

            sheet.mergeCells(23, row, 23, row + 2);
            label = new Label(23, row, "Whether filling uptodate property statement By HRMS", innerheadcell);
            sheet.setColumnView(23, 32);
            sheet.addCell(label);

            sheet.mergeCells(24, row, 24, row + 2);
            label = new Label(24, row, "Whether filling uptodate property statement By Officer", innerheadcell);
            sheet.setColumnView(24, 32);
            sheet.addCell(label);

            sheet.mergeCells(25, row, 25, row + 2);
            label = new Label(25, row, "Date Of filling uptodate property statement By Officer", innerheadcell);
            sheet.setColumnView(25, 32);
            sheet.addCell(label);

            sheet.mergeCells(26, row, 26, row + 2);
            label = new Label(26, row, "Recommendation Status of Nominating Authority", innerheadcell);
            sheet.setColumnView(26, 32);
            sheet.addCell(label);

            sheet.mergeCells(27, row, 27, row + 2);
            label = new Label(27, row, "Remarks of Nominating Authority", innerheadcell);
            sheet.setColumnView(27, 32);
            sheet.addCell(label);

            sheet.mergeCells(28, row, 28, row + 2);
            label = new Label(28, row, "ReCommendation Status of Recommended Authority", innerheadcell);
            sheet.setColumnView(28, 32);
            sheet.addCell(label);

            sheet.mergeCells(29, row, 29, row + 2);
            label = new Label(29, row, "Remarks of Recommended Authority", innerheadcell);
            sheet.setColumnView(29, 32);
            sheet.addCell(label);

            sheet.mergeCells(30, row, 30, row + 2);
            label = new Label(30, row, "Nomination Received From District/Establishment", innerheadcell);
            sheet.setColumnView(30, 32);
            sheet.addCell(label);

            sheet.mergeCells(31, row, 31, row + 2);
            label = new Label(31, row, "Application Id", innerheadcell);
            sheet.setColumnView(31, 32);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(13, row, "Prior to 01.01.2017", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(13, row, 14, row);

            label = new Label(15, row, "During last 5 years i.e. w.e.f. 1.1.2017 to 31.12.2021", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(15, row, 16, row);

            label = new Label(17, row, "From 1.1.22 till date of submission of Nomination Rolls", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(17, row, 18, row);

            row = row + 1;

            label = new Label(12, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(14, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(15, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(16, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(17, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(18, row, "Minor", innerheadcell);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(0, row, "1", innerheadcell);
            sheet.addCell(label);

            label = new Label(1, row, "2", innerheadcell);
            sheet.addCell(label);

            label = new Label(2, row, "3", innerheadcell);
            sheet.addCell(label);

            label = new Label(3, row, "4", innerheadcell);
            sheet.addCell(label);

            label = new Label(4, row, "5", innerheadcell);
            sheet.addCell(label);

            label = new Label(5, row, "6", innerheadcell);
            sheet.addCell(label);

            label = new Label(6, row, "7", innerheadcell);
            sheet.addCell(label);

            label = new Label(7, row, "8", innerheadcell);
            sheet.addCell(label);

            label = new Label(8, row, "9", innerheadcell);
            sheet.addCell(label);

            label = new Label(9, row, "10", innerheadcell);
            sheet.addCell(label);

            label = new Label(10, row, "11", innerheadcell);
            sheet.addCell(label);

            label = new Label(11, row, "12", innerheadcell);
            sheet.addCell(label);

            label = new Label(12, row, "13", innerheadcell);
            sheet.addCell(label);

            label = new Label(13, row, "14", innerheadcell);
            sheet.addCell(label);

            label = new Label(14, row, "15", innerheadcell);
            sheet.addCell(label);

            label = new Label(15, row, "16", innerheadcell);
            sheet.addCell(label);

            label = new Label(16, row, "17", innerheadcell);
            sheet.addCell(label);

            label = new Label(17, row, "18", innerheadcell);
            sheet.addCell(label);

            label = new Label(18, row, "19", innerheadcell);
            sheet.addCell(label);

            label = new Label(19, row, "20", innerheadcell);
            sheet.addCell(label);

            label = new Label(20, row, "21", innerheadcell);
            sheet.addCell(label);

            label = new Label(21, row, "22", innerheadcell);
            sheet.addCell(label);

            label = new Label(22, row, "23", innerheadcell);
            sheet.addCell(label);

            label = new Label(23, row, "24", innerheadcell);
            sheet.addCell(label);

            label = new Label(24, row, "25", innerheadcell);
            sheet.addCell(label);

            label = new Label(25, row, "26", innerheadcell);
            sheet.addCell(label);

            label = new Label(26, row, "27", innerheadcell);
            sheet.addCell(label);

            label = new Label(27, row, "28", innerheadcell);
            sheet.addCell(label);

            label = new Label(28, row, "29", innerheadcell);
            sheet.addCell(label);

            label = new Label(29, row, "30", innerheadcell);
            sheet.addCell(label);

            label = new Label(30, row, "31", innerheadcell);
            sheet.addCell(label);

            label = new Label(31, row, "32", innerheadcell);
            sheet.addCell(label);

            pst = con.prepareStatement("select officename,physicalfitness,passing_training_date,property_submitted_if_any,grade_serial_no,dpcifany,proceeding_detail,pnf.nomination_form_id, gradation_number,em.emp_id,pnd.gpf_no,off_name_abbr, fullname, fathersname, em.category, to_char(em.dob, 'DD-Mon-YYYY') dob, "
                    + "to_char(postingunintdoj, 'DD-Mon-YYYY') postingunintdoj , passing_training_date, place_posting, to_char(doegov, 'DD-Mon-YYYY') doegov, "
                    + "to_char(to_date(jodinspector,'YYYY-MM-DD'), 'DD-Mon-YYYY') jodinspector, punishmentmajorprior, punishmentmajorduring, punishmentmajorfrom, "
                    + " punishmentminorprior, punishmentminorduring, punishmentminorfrom,punishment_detail, dpcifany, disc_details, remarks_of_cdmo, dateofserving, to_char(dor, 'DD-Mon-YYYY') dor, "
                    + "remarksnominationgeneral,recommend_status , off_name_abbr, gradation_slno, pnm.entry_year,property_submitted_on_hrms,property_submitted_on_hrms_byofficer,"
                    + "district_recommendation_status,district_recommendation_view,remarks_recommend_authority,servingchargedetail,dateofserving_if_any_dp  from police_nomination_master pnm "
                    + "inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
                    + "inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid "
                    + "inner join police_office_list_data pold on pnm.created_office=pold.police_off_code "
                    + "inner join emp_mast em on pnd.gpf_no=em.gpf_no "
                    + "left outer join police_nomination_form_different_proceeding on pnf.nomination_form_id = police_nomination_form_different_proceeding.nomination_form_id "
                    + "where is_submitted='Y' and nomination_for_new_rank=? and is_archived='N' and pnm.entry_year=? order by grade_serial_no asc");
            int i = 0;
            pst.setString(1, nominatedForPost);
            pst.setString(2, fiscalyear);
            rs = pst.executeQuery();
            while (rs.next()) {

                i++;
                row = row + 1;
                label = new Label(0, row, i + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(1, row, rs.getInt("grade_serial_no") + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(2, row, rs.getString("emp_id"), innerheadcell);
                sheet.addCell(label);

                label = new Label(3, row, rs.getString("gpf_no"), innerheadcell);
                sheet.addCell(label);

                label = new Label(4, row, rs.getString("fullname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(5, row, rs.getString("fathersname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(6, row, rs.getString("category"), innerheadcell);
                sheet.addCell(label);

                label = new Label(7, row, rs.getString("dob"), innerheadcell);
                sheet.addCell(label);

                label = new Label(8, row, rs.getString("doegov"), innerheadcell);
                sheet.addCell(label);

                label = new Label(9, row, rs.getString("jodinspector"), innerheadcell);
                sheet.addCell(label);

                label = new Label(10, row, rs.getString("passing_training_date"), innerheadcell);
                sheet.addCell(label);

                if (nfm.getDpcifany() != null && !nfm.getDpcifany().equals("") && nfm.getDpcifany().equals("Yes")) {
                    label = new Label(11, row, "Yes", innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(11, row, "No", innerheadcell);
                    sheet.addCell(label);
                }

                label = new Label(12, row, rs.getString("disc_details"), innerheadcell);
                sheet.addCell(label);

                label = new Label(13, row, rs.getString("punishmentmajorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(14, row, rs.getString("punishmentminorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(15, row, rs.getString("punishmentmajorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(16, row, rs.getString("punishmentminorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(17, row, rs.getString("punishmentmajorfrom"), innerheadcell);
                sheet.addCell(label);

                label = new Label(18, row, rs.getString("punishmentminorfrom"), innerheadcell);
                sheet.addCell(label);

                label = new Label(19, row, rs.getString("punishment_detail"), innerheadcell);
                sheet.addCell(label);

                if (nfm.getDateofServingifAny() != null && !nfm.getDateofServingifAny().equals("") && nfm.getDateofServingifAny().equals("Y")) {
                    label = new Label(20, row, "Yes", innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(20, row, "No", innerheadcell);
                    sheet.addCell(label);
                }

                label = new Label(21, row, rs.getString("servingchargedetail"), innerheadcell);
                sheet.addCell(label);

                label = new Label(22, row, rs.getString("physicalfitness"), innerheadcell);
                sheet.addCell(label);

                label = new Label(23, row, rs.getString("property_submitted_on_hrms"), innerheadcell);
                sheet.addCell(label);

                label = new Label(24, row, rs.getString("property_submitted_if_any"), innerheadcell);
                sheet.addCell(label);

                label = new Label(25, row, rs.getString("property_submitted_on_hrms_byofficer"), innerheadcell);
                sheet.addCell(label);

                label = new Label(26, row, rs.getString("district_recommendation_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(26, row, rs.getString("district_recommendation_view"), innerheadcell);
                sheet.addCell(label);

                label = new Label(28, row, rs.getString("recommend_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(29, row, rs.getString("remarks_recommend_authority"), innerheadcell);
                sheet.addCell(label);

                label = new Label(30, row, rs.getString("officename"), innerheadcell);
                sheet.addCell(label);

                label = new Label(31, row, rs.getString("nomination_form_id"), innerheadcell);
                sheet.addCell(label);

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

    public void downloadNominationCheckListForSIArmed2InspectorArmedDistrictCadre(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        int row = 0;
        try {
            con = this.repodataSource.getConnection();

            WritableSheet sheet = workbook.createSheet("Check List", 0);

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

            NominationForm nfm = new NominationForm();
            Label label = new Label(0, row, " SERVICE PARTICUALRS OF SIs (ARMED) FOR PROMOTION TO THE RANK OF Inspector (ARMED) OF DISTRICT CADRE (GENERAL) ", headcell);//column,row
            sheet.setRowView(row, 30 * 30);
            sheet.addCell(label);
            sheet.mergeCells(0, row, 24, row);

            row = row + 1;

            sheet.mergeCells(0, row, 0, row + 2);
            label = new Label(0, row, "SL No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(1, row, 1, row + 2);
            label = new Label(1, row, "Gradation Sl No", innerheadcell);
            sheet.setColumnView(1, 26);
            sheet.addCell(label);

            sheet.mergeCells(2, row, 2, row + 2);
            label = new Label(2, row, "Hrms Id", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(3, row, 3, row + 2);
            label = new Label(3, row, "Gpf No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(4, row, 4, row + 2);
            label = new Label(4, row, "Name ", innerheadcell);
            sheet.setColumnView(4, 50);
            sheet.addCell(label);

            sheet.mergeCells(5, row, 5, row + 2);
            label = new Label(5, row, "Father's Name", innerheadcell);
            sheet.setColumnView(5, 50);
            sheet.addCell(label);

            sheet.mergeCells(6, row, 6, row + 2);
            label = new Label(6, row, "Category", innerheadcell);
            sheet.setColumnView(6, 30);
            sheet.addCell(label);

            sheet.mergeCells(7, row, 7, row + 2);
            label = new Label(7, row, "Date of Birth", innerheadcell);
            sheet.setColumnView(7, 30);
            sheet.addCell(label);

            sheet.mergeCells(8, row, 8, row + 2);
            label = new Label(8, row, "Date of entry into Govt. Service", innerheadcell);
            sheet.setColumnView(8, 30);
            sheet.addCell(label);

            sheet.mergeCells(9, row, 9, row + 2);
            label = new Label(9, row, "Date of appointent in the Present Grade", innerheadcell);
            sheet.setColumnView(9, 30);
            sheet.addCell(label);

            sheet.mergeCells(10, row, 10, row + 2);
            label = new Label(10, row, "Status of disciplinary/Proceeding/Criminal/Vigilance proceeding if any. ", innerheadcell);
            sheet.setColumnView(10, 30);
            sheet.addCell(label);

            sheet.mergeCells(11, row, 11, row + 2);
            label = new Label(11, row, "disciplinary/Proceeding/Criminal/Vigilance proceeding Details. ", innerheadcell);
            sheet.setColumnView(11, 30);
            sheet.addCell(label);

            label = new Label(12, row, "Punishment", innerheadcell);
            sheet.setColumnView(12, 30);
            sheet.addCell(label);
            sheet.mergeCells(12, row, 17, row);

            sheet.mergeCells(18, row, 18, row + 2);
            label = new Label(18, row, "Punishment Detail", innerheadcell);
            sheet.setColumnView(18, 34);
            sheet.addCell(label);

            sheet.mergeCells(19, row, 19, row + 2);
            label = new Label(19, row, "Status of charge has been served on the nominee in Departmental Proceeding. ", innerheadcell);
            sheet.setColumnView(19, 30);
            sheet.addCell(label);

            sheet.mergeCells(20, row, 20, row + 2);
            label = new Label(20, row, "Serving Charge Details. ", innerheadcell);
            sheet.setColumnView(20, 30);
            sheet.addCell(label);

            sheet.mergeCells(21, row, 21, row + 2);
            label = new Label(21, row, "Whether filling uptodate property statement By HRMS", innerheadcell);
            sheet.setColumnView(21, 30);
            sheet.addCell(label);

            sheet.mergeCells(22, row, 22, row + 2);
            label = new Label(22, row, "Whether filling uptodate property statement By Officer", innerheadcell);
            sheet.setColumnView(22, 30);
            sheet.addCell(label);

            sheet.mergeCells(23, row, 23, row + 2);
            label = new Label(23, row, "Date Of filling uptodate property statement By Officer", innerheadcell);
            sheet.setColumnView(23, 30);
            sheet.addCell(label);

            sheet.mergeCells(24, row, 24, row + 2);
            label = new Label(24, row, "Recommendation Status of Nominating Authority", innerheadcell);
            sheet.setColumnView(24, 30);
            sheet.addCell(label);

            sheet.mergeCells(25, row, 25, row + 2);
            label = new Label(25, row, "Remarks of Nominating Authority", innerheadcell);
            sheet.setColumnView(25, 30);
            sheet.addCell(label);

            sheet.mergeCells(26, row, 26, row + 2);
            label = new Label(26, row, "ReCommendation Status of Recommended Authority", innerheadcell);
            sheet.setColumnView(26, 30);
            sheet.addCell(label);

            sheet.mergeCells(27, row, 27, row + 2);
            label = new Label(27, row, "Remarks of Recommended Authority", innerheadcell);
            sheet.setColumnView(27, 30);
            sheet.addCell(label);

            sheet.mergeCells(28, row, 28, row + 2);
            label = new Label(28, row, "Nomination Received From District/Establishment", innerheadcell);
            sheet.setColumnView(28, 30);
            sheet.addCell(label);

            sheet.mergeCells(29, row, 29, row + 2);
            label = new Label(29, row, "Application Id", innerheadcell);
            sheet.setColumnView(29, 30);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(12, row, "Prior to 01.01.2018", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(12, row, 13, row);

            label = new Label(14, row, "During last 5 years i.e. w.e.f. 1.1.2018 to 31.12.2022", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(14, row, 15, row);

            label = new Label(16, row, "From 1.1.23 till date of submission of Nomination Rolls", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(16, row, 17, row);

            row = row + 1;

            label = new Label(12, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(13, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(14, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(15, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(16, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(17, row, "Minor", innerheadcell);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(0, row, "1", innerheadcell);
            sheet.addCell(label);

            label = new Label(1, row, "2", innerheadcell);
            sheet.addCell(label);

            label = new Label(2, row, "3", innerheadcell);
            sheet.addCell(label);

            label = new Label(3, row, "4", innerheadcell);
            sheet.addCell(label);

            label = new Label(4, row, "5", innerheadcell);
            sheet.addCell(label);

            label = new Label(5, row, "6", innerheadcell);
            sheet.addCell(label);

            label = new Label(6, row, "7", innerheadcell);
            sheet.addCell(label);

            label = new Label(7, row, "8", innerheadcell);
            sheet.addCell(label);

            label = new Label(8, row, "9", innerheadcell);
            sheet.addCell(label);

            label = new Label(9, row, "10", innerheadcell);
            sheet.addCell(label);

            label = new Label(10, row, "11", innerheadcell);
            sheet.addCell(label);

            label = new Label(11, row, "12", innerheadcell);
            sheet.addCell(label);

            label = new Label(12, row, "13", innerheadcell);
            sheet.addCell(label);

            label = new Label(13, row, "14", innerheadcell);
            sheet.addCell(label);

            label = new Label(14, row, "15", innerheadcell);
            sheet.addCell(label);

            label = new Label(15, row, "16", innerheadcell);
            sheet.addCell(label);

            label = new Label(16, row, "17", innerheadcell);
            sheet.addCell(label);

            label = new Label(17, row, "18", innerheadcell);
            sheet.addCell(label);

            label = new Label(18, row, "19", innerheadcell);
            sheet.addCell(label);

            label = new Label(19, row, "20", innerheadcell);
            sheet.addCell(label);

            label = new Label(20, row, "21", innerheadcell);
            sheet.addCell(label);

            label = new Label(21, row, "22", innerheadcell);
            sheet.addCell(label);

            label = new Label(22, row, "23", innerheadcell);
            sheet.addCell(label);

            label = new Label(23, row, "24", innerheadcell);
            sheet.addCell(label);

            label = new Label(24, row, "25", innerheadcell);
            sheet.addCell(label);

            label = new Label(25, row, "26", innerheadcell);
            sheet.addCell(label);

            label = new Label(26, row, "27", innerheadcell);
            sheet.addCell(label);

            label = new Label(27, row, "28", innerheadcell);
            sheet.addCell(label);

            label = new Label(28, row, "29", innerheadcell);
            sheet.addCell(label);

            label = new Label(29, row, "30", innerheadcell);
            sheet.addCell(label);

            pst = con.prepareStatement("select property_submitted_if_any,grade_serial_no,dpcifany,proceeding_detail,pnf.nomination_form_id, gradation_number,em.emp_id,pnd.gpf_no,off_name_abbr, fullname, fathersname, em.category, to_char(em.dob, 'DD-Mon-YYYY') dob, "
                    + "to_char(postingunintdoj, 'DD-Mon-YYYY') postingunintdoj , passing_training_date, place_posting, to_char(doegov, 'DD-Mon-YYYY') doegov, "
                    + "to_char(to_date(jodinspector,'YYYY-MM-DD'), 'DD-Mon-YYYY') jodinspector, punishmentmajorprior, punishmentmajorduring, punishmentmajorfrom, "
                    + " punishmentminorprior, punishmentminorduring, punishmentminorfrom, dpcifany, disc_details, remarks_of_cdmo, dateofserving, to_char(dor, 'DD-Mon-YYYY') dor, "
                    + "remarksnominationgeneral,recommend_status , off_name_abbr, gradation_slno, pnm.entry_year,property_submitted_on_hrms,property_submitted_on_hrms_byofficer,"
                    + "district_recommendation_status,district_recommendation_view,remarks_recommend_authority,punishment_detail,dateofserving_if_any_dp,servingchargedetail  from police_nomination_master pnm "
                    + "inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
                    + "inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid "
                    + "inner join police_office_list_data pold on pnm.created_office=pold.police_off_code "
                    + "inner join emp_mast em on pnd.gpf_no=em.gpf_no "
                    + "left outer join police_nomination_form_different_proceeding on pnf.nomination_form_id = police_nomination_form_different_proceeding.nomination_form_id "
                    + "where is_submitted='Y' and nomination_for_new_rank=? and is_archived='N' and pnm.entry_year=? order by grade_serial_no asc");
            int i = 0;
            pst.setString(1, nominatedForPost);
            pst.setString(2, fiscalyear);
            rs = pst.executeQuery();
            while (rs.next()) {

                i++;
                row = row + 1;
                label = new Label(0, row, i + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(1, row, rs.getInt("grade_serial_no") + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(2, row, rs.getString("emp_id"), innerheadcell);
                sheet.addCell(label);

                label = new Label(3, row, rs.getString("gpf_no"), innerheadcell);
                sheet.addCell(label);

                label = new Label(4, row, rs.getString("fullname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(5, row, rs.getString("fathersname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(6, row, rs.getString("category"), innerheadcell);
                sheet.addCell(label);

                label = new Label(7, row, rs.getString("dob"), innerheadcell);
                sheet.addCell(label);

                label = new Label(8, row, rs.getString("doegov"), innerheadcell);
                sheet.addCell(label);

                label = new Label(9, row, rs.getString("jodinspector"), innerheadcell);
                sheet.addCell(label);

                if (nfm.getDpcifany() != null && !nfm.getDpcifany().equals("") && nfm.getDpcifany().equals("Y")) {
                    label = new Label(10, row, "Yes", innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(10, row, "No", innerheadcell);
                    sheet.addCell(label);
                }

                label = new Label(11, row, rs.getString("proceeding_detail"), innerheadcell);
                sheet.addCell(label);

                label = new Label(12, row, rs.getString("punishmentmajorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(13, row, rs.getString("punishmentminorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(14, row, rs.getString("punishmentmajorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(15, row, rs.getString("punishmentminorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(16, row, rs.getString("punishmentmajorfrom"), innerheadcell);
                sheet.addCell(label);

                label = new Label(17, row, rs.getString("punishmentminorfrom"), innerheadcell);
                sheet.addCell(label);

                sheet.mergeCells(18, row, 18, row + 2);
                label = new Label(18, row, "Punishment Detail", innerheadcell);
                sheet.setColumnView(18, 34);
                sheet.addCell(label);

                sheet.mergeCells(19, row, 19, row + 2);
                label = new Label(19, row, "Status of charge has been served on the nominee in Departmental Proceeding. ", innerheadcell);
                sheet.setColumnView(19, 34);
                sheet.addCell(label);

                sheet.mergeCells(20, row, 20, row + 2);
                label = new Label(20, row, "Serving Charge Details. ", innerheadcell);
                sheet.setColumnView(20, 34);
                sheet.addCell(label);

                label = new Label(21, row, rs.getString("property_submitted_on_hrms"), innerheadcell);
                sheet.addCell(label);

                label = new Label(22, row, rs.getString("property_submitted_if_any"), innerheadcell);
                sheet.addCell(label);

                label = new Label(23, row, rs.getString("property_submitted_on_hrms_byofficer"), innerheadcell);
                sheet.addCell(label);

                label = new Label(24, row, rs.getString("district_recommendation_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(25, row, rs.getString("district_recommendation_view"), innerheadcell);
                sheet.addCell(label);

                label = new Label(26, row, rs.getString("recommend_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(27, row, rs.getString("remarks_recommend_authority"), innerheadcell);
                sheet.addCell(label);

                label = new Label(28, row, rs.getString("off_name_abbr"), innerheadcell);
                sheet.addCell(label);

                label = new Label(29, row, rs.getString("nomination_form_id"), innerheadcell);
                sheet.addCell(label);

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

    @Override
    public void downloadNominationCheckListForSIArmed2InspectorArmedBatalionCadreOdiaCoy(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        int row = 0;
        try {
            con = this.repodataSource.getConnection();

            WritableSheet sheet = workbook.createSheet("Check List", 0);

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

            NominationForm nfm = new NominationForm();
            Label label = new Label(0, row, " SERVICE PARTICUALRS OF SIs (ARMED) FOR PROMOTION TO THE RANK OF Inspector (ARMED) OF BATTALION (ODIA COY) ", headcell);//column,row
            sheet.setRowView(row, 30 * 30);
            sheet.addCell(label);
            sheet.mergeCells(0, row, 24, row);

            row = row + 1;

            sheet.mergeCells(0, row, 0, row + 2);
            label = new Label(0, row, "SL No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(1, row, 1, row + 2);
            label = new Label(1, row, "Gradation Sl No", innerheadcell);
            sheet.setColumnView(1, 26);
            sheet.addCell(label);

            sheet.mergeCells(2, row, 2, row + 2);
            label = new Label(2, row, "Hrms Id", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(3, row, 3, row + 2);
            label = new Label(3, row, "Gpf No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(4, row, 4, row + 2);
            label = new Label(4, row, "Name ", innerheadcell);
            sheet.setColumnView(4, 50);
            sheet.addCell(label);

            sheet.mergeCells(5, row, 5, row + 2);
            label = new Label(5, row, "Father's Name", innerheadcell);
            sheet.setColumnView(5, 50);
            sheet.addCell(label);

            sheet.mergeCells(6, row, 6, row + 2);
            label = new Label(6, row, "Category", innerheadcell);
            sheet.setColumnView(6, 30);
            sheet.addCell(label);

            sheet.mergeCells(7, row, 7, row + 2);
            label = new Label(7, row, "Date of Birth", innerheadcell);
            sheet.setColumnView(7, 30);
            sheet.addCell(label);

            sheet.mergeCells(8, row, 8, row + 2);
            label = new Label(8, row, "Date of entry into Govt. Service", innerheadcell);
            sheet.setColumnView(8, 30);
            sheet.addCell(label);

            sheet.mergeCells(9, row, 9, row + 2);
            label = new Label(9, row, "Date of appointent in the Present Grade", innerheadcell);
            sheet.setColumnView(9, 30);
            sheet.addCell(label);

            sheet.mergeCells(10, row, 10, row + 2);
            label = new Label(10, row, "Status of disciplinary/Proceeding/Criminal/Vigilance proceeding if any. ", innerheadcell);
            sheet.setColumnView(10, 30);
            sheet.addCell(label);

            sheet.mergeCells(11, row, 11, row + 2);
            label = new Label(11, row, "disciplinary/Proceeding/Criminal/Vigilance proceeding Details. ", innerheadcell);
            sheet.setColumnView(11, 30);
            sheet.addCell(label);

            label = new Label(12, row, "Punishment", innerheadcell);
            sheet.setColumnView(12, 30);
            sheet.addCell(label);
            sheet.mergeCells(12, row, 17, row);

            sheet.mergeCells(18, row, 18, row + 2);
            label = new Label(18, row, "Punishment Detail", innerheadcell);
            sheet.setColumnView(18, 30);
            sheet.addCell(label);

            sheet.mergeCells(19, row, 19, row + 2);
            label = new Label(19, row, "Status of charge has been served on the nominee in Departmental Proceeding. ", innerheadcell);
            sheet.setColumnView(19, 30);
            sheet.addCell(label);

            sheet.mergeCells(20, row, 20, row + 2);
            label = new Label(20, row, "Serving Charge Details. ", innerheadcell);
            sheet.setColumnView(20, 30);
            sheet.addCell(label);

            sheet.mergeCells(21, row, 21, row + 2);
            label = new Label(21, row, "Whether filling uptodate property statement By HRMS", innerheadcell);
            sheet.setColumnView(21, 30);
            sheet.addCell(label);

            sheet.mergeCells(22, row, 22, row + 2);
            label = new Label(22, row, "Whether filling uptodate property statement By Officer", innerheadcell);
            sheet.setColumnView(22, 30);
            sheet.addCell(label);

            sheet.mergeCells(23, row, 23, row + 2);
            label = new Label(23, row, "Date Of filling uptodate property statement By Officer", innerheadcell);
            sheet.setColumnView(23, 30);
            sheet.addCell(label);

            sheet.mergeCells(24, row, 24, row + 2);
            label = new Label(24, row, "Recommendation Status of Nominating Authority", innerheadcell);
            sheet.setColumnView(24, 30);
            sheet.addCell(label);

            sheet.mergeCells(25, row, 25, row + 2);
            label = new Label(25, row, "Remarks of Nominating Authority", innerheadcell);
            sheet.setColumnView(25, 30);
            sheet.addCell(label);

            sheet.mergeCells(26, row, 26, row + 2);
            label = new Label(26, row, "ReCommendation Status of Recommended Authority", innerheadcell);
            sheet.setColumnView(26, 30);
            sheet.addCell(label);

            sheet.mergeCells(27, row, 27, row + 2);
            label = new Label(27, row, "Remarks of Recommended Authority", innerheadcell);
            sheet.setColumnView(27, 30);
            sheet.addCell(label);

            sheet.mergeCells(28, row, 28, row + 2);
            label = new Label(28, row, "Nomination Received From District/Establishment", innerheadcell);
            sheet.setColumnView(28, 30);
            sheet.addCell(label);

            sheet.mergeCells(29, row, 29, row + 2);
            label = new Label(29, row, "Application Id", innerheadcell);
            sheet.setColumnView(29, 30);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(12, row, "Prior to 01.01.2018", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(12, row, 13, row);

            label = new Label(14, row, "During last 5 years i.e. w.e.f. 1.1.2018 to 31.12.2022", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(14, row, 15, row);

            label = new Label(16, row, "From 1.1.23 till date of submission of Nomination Rolls", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(16, row, 17, row);

            row = row + 1;

            label = new Label(12, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(13, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(14, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(15, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(16, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(17, row, "Minor", innerheadcell);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(0, row, "1", innerheadcell);
            sheet.addCell(label);

            label = new Label(1, row, "2", innerheadcell);
            sheet.addCell(label);

            label = new Label(2, row, "3", innerheadcell);
            sheet.addCell(label);

            label = new Label(3, row, "4", innerheadcell);
            sheet.addCell(label);

            label = new Label(4, row, "5", innerheadcell);
            sheet.addCell(label);

            label = new Label(5, row, "6", innerheadcell);
            sheet.addCell(label);

            label = new Label(6, row, "7", innerheadcell);
            sheet.addCell(label);

            label = new Label(7, row, "8", innerheadcell);
            sheet.addCell(label);

            label = new Label(8, row, "9", innerheadcell);
            sheet.addCell(label);

            label = new Label(9, row, "10", innerheadcell);
            sheet.addCell(label);

            label = new Label(10, row, "11", innerheadcell);
            sheet.addCell(label);

            label = new Label(11, row, "12", innerheadcell);
            sheet.addCell(label);

            label = new Label(12, row, "13", innerheadcell);
            sheet.addCell(label);

            label = new Label(13, row, "14", innerheadcell);
            sheet.addCell(label);

            label = new Label(14, row, "15", innerheadcell);
            sheet.addCell(label);

            label = new Label(15, row, "16", innerheadcell);
            sheet.addCell(label);

            label = new Label(16, row, "17", innerheadcell);
            sheet.addCell(label);

            label = new Label(17, row, "18", innerheadcell);
            sheet.addCell(label);

            label = new Label(18, row, "19", innerheadcell);
            sheet.addCell(label);

            label = new Label(19, row, "20", innerheadcell);
            sheet.addCell(label);

            label = new Label(20, row, "21", innerheadcell);
            sheet.addCell(label);

            label = new Label(21, row, "22", innerheadcell);
            sheet.addCell(label);

            label = new Label(22, row, "23", innerheadcell);
            sheet.addCell(label);

            label = new Label(23, row, "24", innerheadcell);
            sheet.addCell(label);

            label = new Label(24, row, "25", innerheadcell);
            sheet.addCell(label);

            label = new Label(25, row, "26", innerheadcell);
            sheet.addCell(label);

            label = new Label(26, row, "27", innerheadcell);
            sheet.addCell(label);

            label = new Label(27, row, "28", innerheadcell);
            sheet.addCell(label);

            label = new Label(28, row, "29", innerheadcell);
            sheet.addCell(label);

            label = new Label(29, row, "30", innerheadcell);
            sheet.addCell(label);

            pst = con.prepareStatement("select property_submitted_if_any,grade_serial_no,dpcifany,proceeding_detail,pnf.nomination_form_id, gradation_number,em.emp_id,pnd.gpf_no,off_name_abbr, fullname, fathersname, em.category, to_char(em.dob, 'DD-Mon-YYYY') dob, "
                    + "to_char(postingunintdoj, 'DD-Mon-YYYY') postingunintdoj , passing_training_date, place_posting, to_char(doegov, 'DD-Mon-YYYY') doegov, "
                    + "to_char(to_date(jodinspector,'YYYY-MM-DD'), 'DD-Mon-YYYY') jodinspector, punishmentmajorprior, punishmentmajorduring, punishmentmajorfrom, "
                    + " punishmentminorprior, punishmentminorduring, punishmentminorfrom, dpcifany, disc_details, remarks_of_cdmo, dateofserving, to_char(dor, 'DD-Mon-YYYY') dor, "
                    + "remarksnominationgeneral,recommend_status , off_name_abbr, gradation_slno, pnm.entry_year,property_submitted_on_hrms,property_submitted_on_hrms_byofficer,"
                    + "district_recommendation_status,district_recommendation_view,remarks_recommend_authority,punishment_detail,dateofserving_if_any_dp,servingchargedetail  from police_nomination_master pnm "
                    + "inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
                    + "inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid "
                    + "inner join police_office_list_data pold on pnm.created_office=pold.police_off_code "
                    + "inner join emp_mast em on pnd.gpf_no=em.gpf_no "
                    + "left outer join police_nomination_form_different_proceeding on pnf.nomination_form_id = police_nomination_form_different_proceeding.nomination_form_id "
                    + "where is_submitted='Y' and nomination_for_new_rank=? and is_archived='N' and pnm.entry_year=? order by grade_serial_no asc");
            int i = 0;
            pst.setString(1, nominatedForPost);
            pst.setString(2, fiscalyear);
            rs = pst.executeQuery();
            while (rs.next()) {

                i++;
                row = row + 1;
                label = new Label(0, row, i + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(1, row, rs.getInt("grade_serial_no") + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(2, row, rs.getString("emp_id"), innerheadcell);
                sheet.addCell(label);

                label = new Label(3, row, rs.getString("gpf_no"), innerheadcell);
                sheet.addCell(label);

                label = new Label(4, row, rs.getString("fullname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(5, row, rs.getString("fathersname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(6, row, rs.getString("category"), innerheadcell);
                sheet.addCell(label);

                label = new Label(7, row, rs.getString("dob"), innerheadcell);
                sheet.addCell(label);

                label = new Label(8, row, rs.getString("doegov"), innerheadcell);
                sheet.addCell(label);

                label = new Label(9, row, rs.getString("jodinspector"), innerheadcell);
                sheet.addCell(label);

                if (nfm.getDpcifany() != null && !nfm.getDpcifany().equals("") && nfm.getDpcifany().equals("Y")) {
                    label = new Label(10, row, "Yes", innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(10, row, "No", innerheadcell);
                    sheet.addCell(label);
                }

                label = new Label(11, row, rs.getString("proceeding_detail"), innerheadcell);
                sheet.addCell(label);

                label = new Label(12, row, rs.getString("punishmentmajorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(13, row, rs.getString("punishmentminorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(14, row, rs.getString("punishmentmajorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(15, row, rs.getString("punishmentminorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(16, row, rs.getString("punishmentmajorfrom"), innerheadcell);
                sheet.addCell(label);

                label = new Label(17, row, rs.getString("punishmentminorfrom"), innerheadcell);
                sheet.addCell(label);

                label = new Label(18, row, rs.getString("punishment_detail"), innerheadcell);
                sheet.addCell(label);

                if (rs.getString("dateofserving_if_any_dp").equals("Y")) {
                    label = new Label(19, row, "Yes", innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(19, row, "No", innerheadcell);
                    sheet.addCell(label);
                }

                label = new Label(20, row, rs.getString("servingchargedetail"), innerheadcell);
                sheet.addCell(label);

                label = new Label(21, row, rs.getString("property_submitted_on_hrms"), innerheadcell);
                sheet.addCell(label);

                label = new Label(22, row, rs.getString("property_submitted_if_any"), innerheadcell);
                sheet.addCell(label);

                label = new Label(23, row, rs.getString("property_submitted_on_hrms_byofficer"), innerheadcell);
                sheet.addCell(label);

                label = new Label(24, row, rs.getString("district_recommendation_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(25, row, rs.getString("district_recommendation_view"), innerheadcell);
                sheet.addCell(label);

                label = new Label(26, row, rs.getString("recommend_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(27, row, rs.getString("remarks_recommend_authority"), innerheadcell);
                sheet.addCell(label);

                label = new Label(28, row, rs.getString("off_name_abbr"), innerheadcell);
                sheet.addCell(label);

                label = new Label(29, row, rs.getString("nomination_form_id"), innerheadcell);
                sheet.addCell(label);

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

    public void downloadNominationCheckListForSIArmed2InspectorArmedBatalionCadreGurkhaCoy(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        int row = 0;
        try {
            con = this.repodataSource.getConnection();

            WritableSheet sheet = workbook.createSheet("Check List", 0);

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

            NominationForm nfm = new NominationForm();
            Label label = new Label(0, row, " SERVICE PARTICUALRS OF SIs (ARMED) FOR PROMOTION TO THE RANK OF Inspector (ARMED) OF BATTALION (GURKHA COY) ", headcell);//column,row
            sheet.setRowView(row, 30 * 30);
            sheet.addCell(label);
            sheet.mergeCells(0, row, 24, row);

            row = row + 1;

            sheet.mergeCells(0, row, 0, row + 2);
            label = new Label(0, row, "SL No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(1, row, 1, row + 2);
            label = new Label(1, row, "Gradation Sl No", innerheadcell);
            sheet.setColumnView(1, 33);
            sheet.addCell(label);

            sheet.mergeCells(2, row, 2, row + 2);
            label = new Label(2, row, "Hrms Id", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(3, row, 3, row + 2);
            label = new Label(3, row, "Gpf No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(4, row, 4, row + 2);
            label = new Label(4, row, "Name ", innerheadcell);
            sheet.setColumnView(4, 50);
            sheet.addCell(label);

            sheet.mergeCells(5, row, 5, row + 2);
            label = new Label(5, row, "Father's Name", innerheadcell);
            sheet.setColumnView(5, 50);
            sheet.addCell(label);

            sheet.mergeCells(6, row, 6, row + 2);
            label = new Label(6, row, "Category", innerheadcell);
            sheet.setColumnView(6, 33);
            sheet.addCell(label);

            sheet.mergeCells(7, row, 7, row + 2);
            label = new Label(7, row, "Date of Birth", innerheadcell);
            sheet.setColumnView(7, 33);
            sheet.addCell(label);

            sheet.mergeCells(8, row, 8, row + 2);
            label = new Label(8, row, "Date of entry into Govt. Service", innerheadcell);
            sheet.setColumnView(8, 33);
            sheet.addCell(label);

            sheet.mergeCells(9, row, 9, row + 2);
            label = new Label(9, row, "Date of appointent in the Present Grade", innerheadcell);
            sheet.setColumnView(9, 33);
            sheet.addCell(label);

            sheet.mergeCells(10, row, 10, row + 2);
            label = new Label(10, row, "Status of disciplinary/Proceeding/Criminal/Vigilance proceeding if any. ", innerheadcell);
            sheet.setColumnView(10, 33);
            sheet.addCell(label);

            sheet.mergeCells(11, row, 11, row + 2);
            label = new Label(11, row, "disciplinary/Proceeding/Criminal/Vigilance proceeding Details. ", innerheadcell);
            sheet.setColumnView(11, 33);
            sheet.addCell(label);

            label = new Label(12, row, "Punishment", innerheadcell);
            sheet.setColumnView(12, 33);
            sheet.addCell(label);
            sheet.mergeCells(12, row, 17, row);
            
            sheet.mergeCells(18, row, 18, row + 2);
            label = new Label(18, row, "Punishment Detail", innerheadcell);
            sheet.setColumnView(18, 33);
            sheet.addCell(label);
            
            sheet.mergeCells(19, row, 19, row + 2);
            label = new Label(19, row, "Status of charge has been served on the nominee in Departmental Proceeding. ", innerheadcell);
            sheet.setColumnView(19, 33);
            sheet.addCell(label);

            sheet.mergeCells(20, row, 20, row + 2);
            label = new Label(20, row, "Serving Charge Details. ", innerheadcell);
            sheet.setColumnView(20, 33);
            sheet.addCell(label);

            sheet.mergeCells(21, row, 21, row + 2);
            label = new Label(21, row, "Whether filling uptodate property statement By HRMS", innerheadcell);
            sheet.setColumnView(21, 33);
            sheet.addCell(label);

            sheet.mergeCells(22, row, 22, row + 2);
            label = new Label(22, row, "Whether filling uptodate property statement By Officer", innerheadcell);
            sheet.setColumnView(22, 33);
            sheet.addCell(label);

            sheet.mergeCells(23, row, 23, row + 2);
            label = new Label(23, row, "Date Of filling uptodate property statement By Officer", innerheadcell);
            sheet.setColumnView(23, 33);
            sheet.addCell(label);

            sheet.mergeCells(24, row, 24, row + 2);
            label = new Label(24, row, "Recommendation Status of Nominating Authority", innerheadcell);
            sheet.setColumnView(24, 33);
            sheet.addCell(label);

            sheet.mergeCells(25, row, 25, row + 2);
            label = new Label(25, row, "Remarks of Nominating Authority", innerheadcell);
            sheet.setColumnView(25, 33);
            sheet.addCell(label);

            sheet.mergeCells(26, row, 26, row + 2);
            label = new Label(26, row, "ReCommendation Status of Recommended Authority", innerheadcell);
            sheet.setColumnView(26, 33);
            sheet.addCell(label);

            sheet.mergeCells(27, row, 27, row + 2);
            label = new Label(27, row, "Remarks of Recommended Authority", innerheadcell);
            sheet.setColumnView(27, 33);
            sheet.addCell(label);

            sheet.mergeCells(28, row, 28, row + 2);
            label = new Label(28, row, "Nomination Received From District/Establishment", innerheadcell);
            sheet.setColumnView(28, 33);
            sheet.addCell(label);

            sheet.mergeCells(29, row, 29, row + 2);
            label = new Label(29, row, "Whether Passing Prepromotional training", innerheadcell);
            sheet.setColumnView(29, 33);
            sheet.addCell(label);

            sheet.mergeCells(30, row, 30, row + 2);
            label = new Label(30, row, "Order Number and Date of Passing", innerheadcell);
            sheet.setColumnView(30, 33);
            sheet.addCell(label);

            sheet.mergeCells(31, row, 31, row + 2);
            label = new Label(31, row, "Whether at least 2 years of Work experience in a  Battalion / SOG", innerheadcell);
            sheet.setColumnView(31, 33);
            sheet.addCell(label);

            sheet.mergeCells(32, row, 32, row + 2);
            label = new Label(32, row, "Application Id", innerheadcell);
            sheet.setColumnView(32, 33);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(12, row, "Prior to 01.01.2018", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(12, row, 13, row);

            label = new Label(14, row, "During last 5 years i.e. w.e.f. 1.1.2018 to 31.12.2022", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(14, row, 15, row);

            label = new Label(16, row, "From 1.1.23 till date of submission of Nomination Rolls", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(16, row, 17, row);

            row = row + 1;

            label = new Label(12, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(13, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(14, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(15, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(16, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(17, row, "Minor", innerheadcell);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(0, row, "1", innerheadcell);
            sheet.addCell(label);

            label = new Label(1, row, "2", innerheadcell);
            sheet.addCell(label);

            label = new Label(2, row, "3", innerheadcell);
            sheet.addCell(label);

            label = new Label(3, row, "4", innerheadcell);
            sheet.addCell(label);

            label = new Label(4, row, "5", innerheadcell);
            sheet.addCell(label);

            label = new Label(5, row, "6", innerheadcell);
            sheet.addCell(label);

            label = new Label(6, row, "7", innerheadcell);
            sheet.addCell(label);

            label = new Label(7, row, "8", innerheadcell);
            sheet.addCell(label);

            label = new Label(8, row, "9", innerheadcell);
            sheet.addCell(label);

            label = new Label(9, row, "10", innerheadcell);
            sheet.addCell(label);

            label = new Label(10, row, "11", innerheadcell);
            sheet.addCell(label);

            label = new Label(11, row, "12", innerheadcell);
            sheet.addCell(label);

            label = new Label(12, row, "13", innerheadcell);
            sheet.addCell(label);

            label = new Label(13, row, "14", innerheadcell);
            sheet.addCell(label);

            label = new Label(14, row, "15", innerheadcell);
            sheet.addCell(label);

            label = new Label(15, row, "16", innerheadcell);
            sheet.addCell(label);

            label = new Label(16, row, "17", innerheadcell);
            sheet.addCell(label);

            label = new Label(17, row, "18", innerheadcell);
            sheet.addCell(label);

            label = new Label(18, row, "19", innerheadcell);
            sheet.addCell(label);

            label = new Label(19, row, "20", innerheadcell);
            sheet.addCell(label);

            label = new Label(20, row, "21", innerheadcell);
            sheet.addCell(label);

            label = new Label(21, row, "22", innerheadcell);
            sheet.addCell(label);

            label = new Label(22, row, "23", innerheadcell);
            sheet.addCell(label);

            label = new Label(23, row, "24", innerheadcell);
            sheet.addCell(label);

            label = new Label(24, row, "25", innerheadcell);
            sheet.addCell(label);

            label = new Label(25, row, "26", innerheadcell);
            sheet.addCell(label);

            label = new Label(26, row, "27", innerheadcell);
            sheet.addCell(label);

            label = new Label(27, row, "28", innerheadcell);
            sheet.addCell(label);

            label = new Label(28, row, "29", innerheadcell);
            sheet.addCell(label);

            label = new Label(29, row, "30", innerheadcell);
            sheet.addCell(label);
            
            label = new Label(30, row, "31", innerheadcell);
            sheet.addCell(label);
            
            label = new Label(31, row, "32", innerheadcell);
            sheet.addCell(label);
            
            label = new Label(32, row, "33", innerheadcell);
            sheet.addCell(label);

            pst = con.prepareStatement("select pnf.cadre_code,property_submitted_if_any,grade_serial_no,dpcifany,proceeding_detail,pnf.nomination_form_id, gradation_number,em.emp_id,pnd.gpf_no,off_name_abbr, fullname, fathersname, em.category, to_char(em.dob, 'DD-Mon-YYYY') dob, "
                    + "to_char(postingunintdoj, 'DD-Mon-YYYY') postingunintdoj , passing_training_date, place_posting, to_char(doegov, 'DD-Mon-YYYY') doegov, "
                    + "to_char(to_date(jodinspector,'YYYY-MM-DD'), 'DD-Mon-YYYY') jodinspector, punishmentmajorprior, punishmentmajorduring, punishmentmajorfrom, "
                    + " punishmentminorprior, punishmentminorduring, punishmentminorfrom, dpcifany, disc_details, remarks_of_cdmo, dateofserving, to_char(dor, 'DD-Mon-YYYY') dor, "
                    + "remarksnominationgeneral,recommend_status , off_name_abbr, gradation_slno, pnm.entry_year,property_submitted_on_hrms,property_submitted_on_hrms_byofficer,"
                    + "district_recommendation_status,district_recommendation_view,remarks_recommend_authority,have_passed_training_asi,orderno_passing_training_asi,dateof_passing_training_asi,battalion_work_experienceifany,punishment_detail,dateofserving_if_any_dp,servingchargedetail  from police_nomination_master pnm "
                    + "inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
                    + "inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid "
                    + "inner join police_office_list_data pold on pnm.created_office=pold.police_off_code "
                    + "inner join emp_mast em on pnd.gpf_no=em.gpf_no "
                    + "left outer join police_nomination_form_different_proceeding on pnf.nomination_form_id = police_nomination_form_different_proceeding.nomination_form_id "
                    + "where is_submitted='Y' and nomination_for_new_rank=? and is_archived='N' and pnm.entry_year=? and pnf.cadre_code=? order by grade_serial_no asc");
            int i = 0;
            pst.setString(1, nominatedForPost);
            pst.setString(2, fiscalyear);
            pst.setString(3, cadreCode);
            rs = pst.executeQuery();
            while (rs.next()) {

                i++;
                row = row + 1;
                label = new Label(0, row, i + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(1, row, rs.getInt("grade_serial_no") + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(2, row, rs.getString("emp_id"), innerheadcell);
                sheet.addCell(label);

                label = new Label(3, row, rs.getString("gpf_no"), innerheadcell);
                sheet.addCell(label);

                label = new Label(4, row, rs.getString("fullname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(5, row, rs.getString("fathersname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(6, row, rs.getString("category"), innerheadcell);
                sheet.addCell(label);

                label = new Label(7, row, rs.getString("dob"), innerheadcell);
                sheet.addCell(label);

                label = new Label(8, row, rs.getString("doegov"), innerheadcell);
                sheet.addCell(label);

                label = new Label(9, row, rs.getString("jodinspector"), innerheadcell);
                sheet.addCell(label);

                if (nfm.getDpcifany() != null && !nfm.getDpcifany().equals("") && nfm.getDpcifany().equals("Y")) {
                    label = new Label(10, row, "Yes", innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(10, row, "No", innerheadcell);
                    sheet.addCell(label);
                }

                label = new Label(11, row, rs.getString("proceeding_detail"), innerheadcell);
                sheet.addCell(label);

                label = new Label(12, row, rs.getString("punishmentmajorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(13, row, rs.getString("punishmentminorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(14, row, rs.getString("punishmentmajorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(15, row, rs.getString("punishmentminorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(16, row, rs.getString("punishmentmajorfrom"), innerheadcell);
                sheet.addCell(label);

                label = new Label(17, row, rs.getString("punishmentminorfrom"), innerheadcell);
                sheet.addCell(label);
                
                label = new Label(18, row, rs.getString("punishment_detail"), innerheadcell);
                sheet.addCell(label);
                
                if (rs.getString("dateofserving_if_any_dp").equals("Y")) {
                    label = new Label(19, row, "Yes", innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(19, row, "No", innerheadcell);
                    sheet.addCell(label);
                }
                
                label = new Label(20, row, rs.getString("servingchargedetail"), innerheadcell);
                sheet.addCell(label);
                
                
                label = new Label(21, row, rs.getString("property_submitted_on_hrms"), innerheadcell);
                sheet.addCell(label);

                label = new Label(22, row, rs.getString("property_submitted_if_any"), innerheadcell);
                sheet.addCell(label);

                label = new Label(23, row, rs.getString("property_submitted_on_hrms_byofficer"), innerheadcell);
                sheet.addCell(label);

                label = new Label(24, row, rs.getString("district_recommendation_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(25, row, rs.getString("district_recommendation_view"), innerheadcell);
                sheet.addCell(label);

                label = new Label(26, row, rs.getString("recommend_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(27, row, rs.getString("remarks_recommend_authority"), innerheadcell);
                sheet.addCell(label);

                label = new Label(28, row, rs.getString("off_name_abbr"), innerheadcell);
                sheet.addCell(label);

                label = new Label(29, row, rs.getString("have_passed_training_asi"), innerheadcell);
                sheet.addCell(label);

                label = new Label(30, row, rs.getString("orderno_passing_training_asi") + "," + CommonFunctions.getFormattedOutputDate1(rs.getDate("dateof_passing_training_asi")), innerheadcell);
                sheet.addCell(label);

                label = new Label(31, row, rs.getString("battalion_work_experienceifany"), innerheadcell);
                sheet.addCell(label);

                label = new Label(32, row, rs.getString("nomination_form_id"), innerheadcell);
                sheet.addCell(label);

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

    @Override
    public void downloadNominationCheckListForSIArmed2InspectorArmedGeneralCadre(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        int row = 0;
        try {
            con = this.repodataSource.getConnection();

            WritableSheet sheet = workbook.createSheet("Check List", 0);

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

            NominationForm nfm = new NominationForm();
            Label label = new Label(0, row, " SERVICE PARTICUALRS OF SIs (ARMED) FOR PROMOTION TO THE RANK OF Inspector (ARMED) OF GENERAL CADRE ", headcell);//column,row
            sheet.setRowView(row, 30 * 30);
            sheet.addCell(label);
            sheet.mergeCells(0, row, 24, row);

            row = row + 1;

            sheet.mergeCells(0, row, 0, row + 2);
            label = new Label(0, row, "SL No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(1, row, 1, row + 2);
            label = new Label(1, row, "Gradation Sl No", innerheadcell);
            sheet.setColumnView(1, 26);
            sheet.addCell(label);

            sheet.mergeCells(2, row, 2, row + 2);
            label = new Label(2, row, "Hrms Id", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(3, row, 3, row + 2);
            label = new Label(3, row, "Gpf No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(4, row, 4, row + 2);
            label = new Label(4, row, "Name ", innerheadcell);
            sheet.setColumnView(4, 50);
            sheet.addCell(label);

            sheet.mergeCells(5, row, 5, row + 2);
            label = new Label(5, row, "Father's Name", innerheadcell);
            sheet.setColumnView(5, 50);
            sheet.addCell(label);

            sheet.mergeCells(6, row, 6, row + 2);
            label = new Label(6, row, "Category", innerheadcell);
            sheet.setColumnView(6, 33);
            sheet.addCell(label);

            sheet.mergeCells(7, row, 7, row + 2);
            label = new Label(7, row, "Date of Birth", innerheadcell);
            sheet.setColumnView(7, 33);
            sheet.addCell(label);

            sheet.mergeCells(8, row, 8, row + 2);
            label = new Label(8, row, "Date of entry into Govt. Service", innerheadcell);
            sheet.setColumnView(8, 33);
            sheet.addCell(label);

            sheet.mergeCells(9, row, 9, row + 2);
            label = new Label(9, row, "Date of appointent in the Present Grade", innerheadcell);
            sheet.setColumnView(9, 33);
            sheet.addCell(label);

            sheet.mergeCells(10, row, 10, row + 2);
            label = new Label(10, row, "Status of disciplinary/Proceeding/Criminal/Vigilance proceeding if any. ", innerheadcell);
            sheet.setColumnView(10, 33);
            sheet.addCell(label);

            sheet.mergeCells(11, row, 11, row + 2);
            label = new Label(11, row, "disciplinary/Proceeding/Criminal/Vigilance proceeding Details. ", innerheadcell);
            sheet.setColumnView(11, 33);
            sheet.addCell(label);

            label = new Label(12, row, "Punishment", innerheadcell);
            sheet.setColumnView(12, 33);
            sheet.addCell(label);
            sheet.mergeCells(12, row, 17, row);
            
            sheet.mergeCells(18, row, 18, row + 2);
            label = new Label(18, row, "Punishment Detail", innerheadcell);
            sheet.setColumnView(18, 33);
            sheet.addCell(label);
            
            sheet.mergeCells(19, row, 19, row + 2);
            label = new Label(19, row, "Status of charge has been served on the nominee in Departmental Proceeding. ", innerheadcell);
            sheet.setColumnView(19, 33);
            sheet.addCell(label);

            sheet.mergeCells(20, row, 20, row + 2);
            label = new Label(20, row, "Serving Charge Details. ", innerheadcell);
            sheet.setColumnView(20, 33);
            sheet.addCell(label);

            sheet.mergeCells(21, row, 21, row + 2);
            label = new Label(21, row, "Whether filling uptodate property statement By HRMS", innerheadcell);
            sheet.setColumnView(21, 33);
            sheet.addCell(label);

            sheet.mergeCells(22, row, 22, row + 2);
            label = new Label(22, row, "Whether filling uptodate property statement By Officer", innerheadcell);
            sheet.setColumnView(22, 33);
            sheet.addCell(label);

            sheet.mergeCells(23, row, 23, row + 2);
            label = new Label(23, row, "Date Of filling uptodate property statement By Officer", innerheadcell);
            sheet.setColumnView(23, 33);
            sheet.addCell(label);

            sheet.mergeCells(24, row, 24, row + 2);
            label = new Label(24, row, "Recommendation Status of Nominating Authority", innerheadcell);
            sheet.setColumnView(24, 33);
            sheet.addCell(label);

            sheet.mergeCells(25, row, 25, row + 2);
            label = new Label(25, row, "Remarks of Nominating Authority", innerheadcell);
            sheet.setColumnView(25, 33);
            sheet.addCell(label);

            sheet.mergeCells(26, row, 26, row + 2);
            label = new Label(26, row, "ReCommendation Status of Recommended Authority", innerheadcell);
            sheet.setColumnView(26, 33);
            sheet.addCell(label);

            sheet.mergeCells(27, row, 27, row + 2);
            label = new Label(27, row, "Remarks of Recommended Authority", innerheadcell);
            sheet.setColumnView(27, 33);
            sheet.addCell(label);

            sheet.mergeCells(28, row, 28, row + 2);
            label = new Label(28, row, "Nomination Received From District/Establishment", innerheadcell);
            sheet.setColumnView(28, 33);
            sheet.addCell(label);

           sheet.mergeCells(29, row, 29, row + 2);
            label = new Label(29, row, "Whether Passing Prepromotional training", innerheadcell);
            sheet.setColumnView(29, 33);
            sheet.addCell(label);

            sheet.mergeCells(30, row, 30, row + 2);
            label = new Label(30, row, "Order Number and Date of Passing", innerheadcell);
            sheet.setColumnView(30, 33);
            sheet.addCell(label);

            sheet.mergeCells(31, row, 31, row + 2);
            label = new Label(31, row, "Whether at least 2 years of Work experience in a  Battalion / SOG", innerheadcell);
            sheet.setColumnView(31, 33);
            sheet.addCell(label);

            sheet.mergeCells(32, row, 32, row + 2);
            label = new Label(32, row, "Application Id", innerheadcell);
            sheet.setColumnView(32, 33);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(12, row, "Prior to 01.01.2018", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(12, row, 13, row);

            label = new Label(14, row, "During last 5 years i.e. w.e.f. 1.1.2018 to 31.12.2022", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(14, row, 15, row);

            label = new Label(16, row, "From 1.1.23 till date of submission of Nomination Rolls", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(16, row, 17, row);

            row = row + 1;

            label = new Label(12, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(13, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(14, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(15, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(16, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(17, row, "Minor", innerheadcell);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(0, row, "1", innerheadcell);
            sheet.addCell(label);

            label = new Label(1, row, "2", innerheadcell);
            sheet.addCell(label);

            label = new Label(2, row, "3", innerheadcell);
            sheet.addCell(label);

            label = new Label(3, row, "4", innerheadcell);
            sheet.addCell(label);

            label = new Label(4, row, "5", innerheadcell);
            sheet.addCell(label);

            label = new Label(5, row, "6", innerheadcell);
            sheet.addCell(label);

            label = new Label(6, row, "7", innerheadcell);
            sheet.addCell(label);

            label = new Label(7, row, "8", innerheadcell);
            sheet.addCell(label);

            label = new Label(8, row, "9", innerheadcell);
            sheet.addCell(label);

            label = new Label(9, row, "10", innerheadcell);
            sheet.addCell(label);

            label = new Label(10, row, "11", innerheadcell);
            sheet.addCell(label);

            label = new Label(11, row, "12", innerheadcell);
            sheet.addCell(label);

            label = new Label(12, row, "13", innerheadcell);
            sheet.addCell(label);

            label = new Label(13, row, "14", innerheadcell);
            sheet.addCell(label);

            label = new Label(14, row, "15", innerheadcell);
            sheet.addCell(label);

            label = new Label(15, row, "16", innerheadcell);
            sheet.addCell(label);

            label = new Label(16, row, "17", innerheadcell);
            sheet.addCell(label);

            label = new Label(17, row, "18", innerheadcell);
            sheet.addCell(label);

            label = new Label(18, row, "19", innerheadcell);
            sheet.addCell(label);

            label = new Label(19, row, "20", innerheadcell);
            sheet.addCell(label);

            label = new Label(20, row, "21", innerheadcell);
            sheet.addCell(label);

            label = new Label(21, row, "22", innerheadcell);
            sheet.addCell(label);

            label = new Label(22, row, "23", innerheadcell);
            sheet.addCell(label);

            label = new Label(23, row, "24", innerheadcell);
            sheet.addCell(label);

            label = new Label(24, row, "25", innerheadcell);
            sheet.addCell(label);

            label = new Label(25, row, "26", innerheadcell);
            sheet.addCell(label);

            label = new Label(26, row, "27", innerheadcell);
            sheet.addCell(label);

            label = new Label(27, row, "28", innerheadcell);
            sheet.addCell(label);

            label = new Label(28, row, "29", innerheadcell);
            sheet.addCell(label);

            label = new Label(29, row, "30", innerheadcell);
            sheet.addCell(label);
            
            label = new Label(30, row, "31", innerheadcell);
            sheet.addCell(label);
            
             label = new Label(31, row, "32", innerheadcell);
            sheet.addCell(label);
            
             label = new Label(32, row, "33", innerheadcell);
            sheet.addCell(label);

            pst = con.prepareStatement("select pnf.cadre_code,property_submitted_if_any,grade_serial_no,dpcifany,proceeding_detail,pnf.nomination_form_id, gradation_number,em.emp_id,pnd.gpf_no,off_name_abbr, fullname, fathersname, em.category, to_char(em.dob, 'DD-Mon-YYYY') dob, "
                    + "to_char(postingunintdoj, 'DD-Mon-YYYY') postingunintdoj , passing_training_date, place_posting, to_char(doegov, 'DD-Mon-YYYY') doegov, "
                    + "to_char(to_date(jodinspector,'YYYY-MM-DD'), 'DD-Mon-YYYY') jodinspector, punishmentmajorprior, punishmentmajorduring, punishmentmajorfrom, "
                    + " punishmentminorprior, punishmentminorduring, punishmentminorfrom, dpcifany, disc_details, remarks_of_cdmo, dateofserving, to_char(dor, 'DD-Mon-YYYY') dor, "
                    + "remarksnominationgeneral,recommend_status , off_name_abbr, gradation_slno, pnm.entry_year,property_submitted_on_hrms,property_submitted_on_hrms_byofficer,"
                    + "district_recommendation_status,district_recommendation_view,remarks_recommend_authority,have_passed_training_asi,orderno_passing_training_asi,dateof_passing_training_asi,battalion_work_experienceifany, "
                    + "punishment_detail,dateofserving_if_any_dp,servingchargedetail from police_nomination_master pnm "
                    + "inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
                    + "inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid "
                    + "inner join police_office_list_data pold on pnm.created_office=pold.police_off_code "
                    + "inner join emp_mast em on pnd.gpf_no=em.gpf_no "
                    + "left outer join police_nomination_form_different_proceeding on pnf.nomination_form_id = police_nomination_form_different_proceeding.nomination_form_id "
                    + "where is_submitted='Y' and nomination_for_new_rank=? and is_archived='N' and pnm.entry_year=?  and pnf.cadre_code=? order by grade_serial_no asc");
            int i = 0;
            pst.setString(1, nominatedForPost);
            pst.setString(2, fiscalyear);
            pst.setString(3, cadreCode);
            rs = pst.executeQuery();
            while (rs.next()) {

                i++;
                row = row + 1;
                label = new Label(0, row, i + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(1, row, rs.getInt("grade_serial_no") + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(2, row, rs.getString("emp_id"), innerheadcell);
                sheet.addCell(label);

                label = new Label(3, row, rs.getString("gpf_no"), innerheadcell);
                sheet.addCell(label);

                label = new Label(4, row, rs.getString("fullname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(5, row, rs.getString("fathersname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(6, row, rs.getString("category"), innerheadcell);
                sheet.addCell(label);

                label = new Label(7, row, rs.getString("dob"), innerheadcell);
                sheet.addCell(label);

                label = new Label(8, row, rs.getString("doegov"), innerheadcell);
                sheet.addCell(label);

                label = new Label(9, row, rs.getString("jodinspector"), innerheadcell);
                sheet.addCell(label);

                if (nfm.getDpcifany() != null && !nfm.getDpcifany().equals("") && nfm.getDpcifany().equals("Y")) {
                    label = new Label(10, row, "Yes", innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(10, row, "No", innerheadcell);
                    sheet.addCell(label);
                }

                label = new Label(11, row, rs.getString("proceeding_detail"), innerheadcell);
                sheet.addCell(label);

                label = new Label(12, row, rs.getString("punishmentmajorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(13, row, rs.getString("punishmentminorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(14, row, rs.getString("punishmentmajorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(15, row, rs.getString("punishmentminorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(16, row, rs.getString("punishmentmajorfrom"), innerheadcell);
                sheet.addCell(label);

                label = new Label(17, row, rs.getString("punishmentminorfrom"), innerheadcell);
                sheet.addCell(label);
                
                label = new Label(18, row, rs.getString("punishment_detail"), innerheadcell);
                sheet.addCell(label);
                
                if (rs.getString("dateofserving_if_any_dp").equals("Y")) {
                    label = new Label(19, row, "Yes", innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(19, row, "No", innerheadcell);
                    sheet.addCell(label);
                }
                
                label = new Label(20, row, rs.getString("servingchargedetail"), innerheadcell);
                sheet.addCell(label);
                
                label = new Label(21, row, rs.getString("property_submitted_on_hrms"), innerheadcell);
                sheet.addCell(label);

                label = new Label(22, row, rs.getString("property_submitted_if_any"), innerheadcell);
                sheet.addCell(label);

                label = new Label(23, row, rs.getString("property_submitted_on_hrms_byofficer"), innerheadcell);
                sheet.addCell(label);

                label = new Label(24, row, rs.getString("district_recommendation_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(25, row, rs.getString("district_recommendation_view"), innerheadcell);
                sheet.addCell(label);

                label = new Label(26, row, rs.getString("recommend_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(27, row, rs.getString("remarks_recommend_authority"), innerheadcell);
                sheet.addCell(label);

                label = new Label(28, row, rs.getString("off_name_abbr"), innerheadcell);
                sheet.addCell(label);

                label = new Label(29, row, rs.getString("have_passed_training_asi"), innerheadcell);
                sheet.addCell(label);

                if (rs.getString("have_passed_training_asi").equals("Yes")) {
                    label = new Label(30, row, rs.getString("orderno_passing_training_asi") + "," + CommonFunctions.getFormattedOutputDate1(rs.getDate("dateof_passing_training_asi")), innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(30, row, "", innerheadcell);
                    sheet.addCell(label);
                }

                label = new Label(31, row, rs.getString("battalion_work_experienceifany"), innerheadcell);
                sheet.addCell(label);

                label = new Label(32, row, rs.getString("nomination_form_id"), innerheadcell);
                sheet.addCell(label);

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

    @Override
    public void downloadNominationCheckListForASIArmed2SubInspectorArmedBatalionCadreGurkhaCoy(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        int row = 0;
        try {
            con = this.repodataSource.getConnection();

            WritableSheet sheet = workbook.createSheet("Check List", 0);

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

            NominationForm nfm = new NominationForm();
            Label label = new Label(0, row, " SERVICE PARTICUALRS OF ASI (ARMED) FOR PROMOTION TO THE RANK OF SUB INSPECTOR (ARMED) OF BATALION CADRE(GURKHA COY) ", headcell);//column,row
            sheet.setRowView(row, 30 * 30);
            sheet.addCell(label);
            sheet.mergeCells(0, row, 24, row);

            row = row + 1;

            sheet.mergeCells(0, row, 0, row + 2);
            label = new Label(0, row, "SL No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(1, row, 1, row + 2);
            label = new Label(1, row, "Gradation Sl No", innerheadcell);
            sheet.setColumnView(1, 26);
            sheet.addCell(label);

            sheet.mergeCells(2, row, 2, row + 2);
            label = new Label(2, row, "Hrms Id", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(3, row, 3, row + 2);
            label = new Label(3, row, "Gpf No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(4, row, 4, row + 2);
            label = new Label(4, row, "Name ", innerheadcell);
            sheet.setColumnView(4, 50);
            sheet.addCell(label);

            sheet.mergeCells(5, row, 5, row + 2);
            label = new Label(5, row, "Father's Name", innerheadcell);
            sheet.setColumnView(5, 50);
            sheet.addCell(label);

            sheet.mergeCells(6, row, 6, row + 2);
            label = new Label(6, row, "Category", innerheadcell);
            sheet.setColumnView(6, 30);
            sheet.addCell(label);

            sheet.mergeCells(7, row, 7, row + 2);
            label = new Label(7, row, "Date of Birth", innerheadcell);
            sheet.setColumnView(7, 30);
            sheet.addCell(label);

            sheet.mergeCells(8, row, 8, row + 2);
            label = new Label(8, row, "Date of entry into Govt. Service", innerheadcell);
            sheet.setColumnView(8, 30);
            sheet.addCell(label);

            sheet.mergeCells(9, row, 9, row + 2);
            label = new Label(9, row, "Date of appointent in the Present Grade", innerheadcell);
            sheet.setColumnView(9, 30);
            sheet.addCell(label);

            sheet.mergeCells(10, row, 10, row + 2);
            label = new Label(10, row, "Status of disciplinary/Proceeding/Criminal/Vigilance proceeding if any. ", innerheadcell);
            sheet.setColumnView(10, 30);
            sheet.addCell(label);

            sheet.mergeCells(11, row, 11, row + 2);
            label = new Label(11, row, "disciplinary/Proceeding/Criminal/Vigilance proceeding Details. ", innerheadcell);
            sheet.setColumnView(11, 30);
            sheet.addCell(label);

            label = new Label(12, row, "Punishment", innerheadcell);
            sheet.setColumnView(12, 30);
            sheet.addCell(label);
            sheet.mergeCells(12, row, 17, row);

            sheet.mergeCells(18, row, 18, row + 2);
            label = new Label(18, row, "Punishment Detail", innerheadcell);
            sheet.setColumnView(18, 34);
            sheet.addCell(label);

            sheet.mergeCells(19, row, 19, row + 2);
            label = new Label(19, row, "Status of charge has been served on the nominee in Departmental Proceeding. ", innerheadcell);
            sheet.setColumnView(19, 34);
            sheet.addCell(label);

            sheet.mergeCells(20, row, 20, row + 2);
            label = new Label(20, row, "Serving Charge Details. ", innerheadcell);
            sheet.setColumnView(20, 34);
            sheet.addCell(label);

            sheet.mergeCells(21, row, 21, row + 2);
            label = new Label(21, row, "Whether filling uptodate property statement By HRMS", innerheadcell);
            sheet.setColumnView(21, 30);
            sheet.addCell(label);

            sheet.mergeCells(22, row, 22, row + 2);
            label = new Label(22, row, "Whether filling uptodate property statement By Officer", innerheadcell);
            sheet.setColumnView(22, 30);
            sheet.addCell(label);

            sheet.mergeCells(23, row, 23, row + 2);
            label = new Label(23, row, "Date Of filling uptodate property statement By Officer", innerheadcell);
            sheet.setColumnView(23, 30);
            sheet.addCell(label);

            sheet.mergeCells(24, row, 24, row + 2);
            label = new Label(24, row, "Recommendation Status of Nominating Authority", innerheadcell);
            sheet.setColumnView(24, 30);
            sheet.addCell(label);

            sheet.mergeCells(25, row, 25, row + 2);
            label = new Label(25, row, "Remarks of Nominating Authority", innerheadcell);
            sheet.setColumnView(25, 30);
            sheet.addCell(label);

            sheet.mergeCells(26, row, 26, row + 2);
            label = new Label(26, row, "ReCommendation Status of Recommended Authority", innerheadcell);
            sheet.setColumnView(26, 30);
            sheet.addCell(label);

            sheet.mergeCells(27, row, 27, row + 2);
            label = new Label(27, row, "Remarks of Recommended Authority", innerheadcell);
            sheet.setColumnView(27, 30);
            sheet.addCell(label);

            sheet.mergeCells(28, row, 28, row + 2);
            label = new Label(28, row, "Nomination Received From District/Establishment", innerheadcell);
            sheet.setColumnView(28, 30);
            sheet.addCell(label);

            sheet.mergeCells(29, row, 29, row + 2);
            label = new Label(29, row, "Application Id", innerheadcell);
            sheet.setColumnView(29, 30);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(12, row, "Prior to 01.01.2018", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(12, row, 13, row);

            label = new Label(14, row, "During last 5 years i.e. w.e.f. 1.1.2018 to 31.12.2022", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(14, row, 15, row);

            label = new Label(16, row, "From 1.1.23 till date of submission of Nomination Rolls", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(16, row, 17, row);

            row = row + 1;

            label = new Label(12, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(13, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(14, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(15, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(16, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(17, row, "Minor", innerheadcell);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(0, row, "1", innerheadcell);
            sheet.addCell(label);

            label = new Label(1, row, "2", innerheadcell);
            sheet.addCell(label);

            label = new Label(2, row, "3", innerheadcell);
            sheet.addCell(label);

            label = new Label(3, row, "4", innerheadcell);
            sheet.addCell(label);

            label = new Label(4, row, "5", innerheadcell);
            sheet.addCell(label);

            label = new Label(5, row, "6", innerheadcell);
            sheet.addCell(label);

            label = new Label(6, row, "7", innerheadcell);
            sheet.addCell(label);

            label = new Label(7, row, "8", innerheadcell);
            sheet.addCell(label);

            label = new Label(8, row, "9", innerheadcell);
            sheet.addCell(label);

            label = new Label(9, row, "10", innerheadcell);
            sheet.addCell(label);

            label = new Label(10, row, "11", innerheadcell);
            sheet.addCell(label);

            label = new Label(11, row, "12", innerheadcell);
            sheet.addCell(label);

            label = new Label(12, row, "13", innerheadcell);
            sheet.addCell(label);

            label = new Label(13, row, "14", innerheadcell);
            sheet.addCell(label);

            label = new Label(14, row, "15", innerheadcell);
            sheet.addCell(label);

            label = new Label(15, row, "16", innerheadcell);
            sheet.addCell(label);

            label = new Label(16, row, "17", innerheadcell);
            sheet.addCell(label);

            label = new Label(17, row, "18", innerheadcell);
            sheet.addCell(label);

            label = new Label(18, row, "19", innerheadcell);
            sheet.addCell(label);

            label = new Label(19, row, "20", innerheadcell);
            sheet.addCell(label);

            label = new Label(20, row, "21", innerheadcell);
            sheet.addCell(label);

            label = new Label(21, row, "22", innerheadcell);
            sheet.addCell(label);

            label = new Label(22, row, "23", innerheadcell);
            sheet.addCell(label);

            label = new Label(23, row, "24", innerheadcell);
            sheet.addCell(label);

            label = new Label(24, row, "25", innerheadcell);
            sheet.addCell(label);

            label = new Label(25, row, "26", innerheadcell);
            sheet.addCell(label);

            label = new Label(26, row, "27", innerheadcell);
            sheet.addCell(label);

            label = new Label(27, row, "28", innerheadcell);
            sheet.addCell(label);

            label = new Label(28, row, "29", innerheadcell);
            sheet.addCell(label);

            label = new Label(29, row, "30", innerheadcell);
            sheet.addCell(label);

            pst = con.prepareStatement("select pnf.cadre_code,property_submitted_if_any,grade_serial_no,dpcifany,proceeding_detail,pnf.nomination_form_id, gradation_number,em.emp_id,pnd.gpf_no,off_name_abbr, fullname, fathersname, em.category, to_char(em.dob, 'DD-Mon-YYYY') dob, "
                    + "to_char(postingunintdoj, 'DD-Mon-YYYY') postingunintdoj , passing_training_date, place_posting, to_char(doegov, 'DD-Mon-YYYY') doegov, "
                    + "to_char(to_date(jodinspector,'YYYY-MM-DD'), 'DD-Mon-YYYY') jodinspector, punishmentmajorprior, punishmentmajorduring, punishmentmajorfrom, "
                    + " punishmentminorprior, punishmentminorduring, punishmentminorfrom, dpcifany, disc_details, remarks_of_cdmo, dateofserving, to_char(dor, 'DD-Mon-YYYY') dor, "
                    + "remarksnominationgeneral,recommend_status , off_name_abbr, gradation_slno, pnm.entry_year,property_submitted_on_hrms,property_submitted_on_hrms_byofficer,"
                    + "district_recommendation_status,district_recommendation_view,remarks_recommend_authority,have_passed_training_asi,orderno_passing_training_asi,dateof_passing_training_asi,battalion_work_experienceifany, "
                    + "punishment_detail,dateofserving_if_any_dp,servingchargedetail,criminal_case_present_status_ifany,criminal_status_detail from police_nomination_master pnm "
                    + "inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
                    + "inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid "
                    + "inner join police_office_list_data pold on pnm.created_office=pold.police_off_code "
                    + "inner join emp_mast em on pnd.gpf_no=em.gpf_no "
                    + "left outer join police_nomination_form_different_proceeding on pnf.nomination_form_id = police_nomination_form_different_proceeding.nomination_form_id "
                    + "where is_submitted='Y' and nomination_for_new_rank=? and is_archived='N' and pnm.entry_year=?  and pnf.cadre_code=? order by grade_serial_no asc");
            int i = 0;
            pst.setString(1, nominatedForPost);
            pst.setString(2, fiscalyear);
            pst.setString(3, cadreCode);
            rs = pst.executeQuery();
            while (rs.next()) {

                i++;
                row = row + 1;
                label = new Label(0, row, i + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(1, row, rs.getInt("grade_serial_no") + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(2, row, rs.getString("emp_id"), innerheadcell);
                sheet.addCell(label);

                label = new Label(3, row, rs.getString("gpf_no"), innerheadcell);
                sheet.addCell(label);

                label = new Label(4, row, rs.getString("fullname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(5, row, rs.getString("fathersname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(6, row, rs.getString("category"), innerheadcell);
                sheet.addCell(label);

                label = new Label(7, row, rs.getString("dob"), innerheadcell);
                sheet.addCell(label);

                label = new Label(8, row, rs.getString("doegov"), innerheadcell);
                sheet.addCell(label);

                label = new Label(9, row, rs.getString("jodinspector"), innerheadcell);
                sheet.addCell(label);

                if (nfm.getDpcifany() != null && !nfm.getDpcifany().equals("") && nfm.getDpcifany().equals("Y")) {
                    label = new Label(10, row, "Yes", innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(10, row, "No", innerheadcell);
                    sheet.addCell(label);
                }

                label = new Label(11, row, rs.getString("proceeding_detail"), innerheadcell);
                sheet.addCell(label);

                label = new Label(12, row, rs.getString("punishmentmajorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(13, row, rs.getString("punishmentminorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(14, row, rs.getString("punishmentmajorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(15, row, rs.getString("punishmentminorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(16, row, rs.getString("punishmentmajorfrom"), innerheadcell);
                sheet.addCell(label);

                label = new Label(17, row, rs.getString("punishmentminorfrom"), innerheadcell);
                sheet.addCell(label);

                label = new Label(18, row, rs.getString("punishment_detail"), innerheadcell);
                sheet.addCell(label);

                if (rs.getString("dateofserving_if_any_dp").equals("Y")) {
                    label = new Label(19, row, "Yes", innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(19, row, "No", innerheadcell);
                    sheet.addCell(label);
                }

                label = new Label(20, row, rs.getString("servingchargedetail"), innerheadcell);
                sheet.addCell(label);

                label = new Label(21, row, rs.getString("property_submitted_on_hrms"), innerheadcell);
                sheet.addCell(label);

                label = new Label(22, row, rs.getString("property_submitted_if_any"), innerheadcell);
                sheet.addCell(label);

                label = new Label(23, row, rs.getString("property_submitted_on_hrms_byofficer"), innerheadcell);
                sheet.addCell(label);

                label = new Label(24, row, rs.getString("district_recommendation_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(25, row, rs.getString("district_recommendation_view"), innerheadcell);
                sheet.addCell(label);

                label = new Label(26, row, rs.getString("recommend_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(27, row, rs.getString("remarks_recommend_authority"), innerheadcell);
                sheet.addCell(label);

                label = new Label(28, row, rs.getString("off_name_abbr"), innerheadcell);
                sheet.addCell(label);

                label = new Label(29, row, rs.getString("nomination_form_id"), innerheadcell);
                sheet.addCell(label);

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

    @Override
    public void downloadNominationCheckListForASIArmed2SubInspectorArmedDistrictCadre(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        int row = 0;
        try {
            con = this.repodataSource.getConnection();

            WritableSheet sheet = workbook.createSheet("Check List", 0);

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

            NominationForm nfm = new NominationForm();
            Label label = new Label(0, row, " SERVICE PARTICUALRS OF ASI (ARMED) FOR PROMOTION TO THE RANK OF SUB Inspector (ARMED) OF DISTRICT CADRE ", headcell);//column,row
            sheet.setRowView(row, 30 * 30);
            sheet.addCell(label);
            sheet.mergeCells(0, row, 24, row);

            row = row + 1;

            sheet.mergeCells(0, row, 0, row + 2);
            label = new Label(0, row, "SL No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(1, row, 1, row + 2);
            label = new Label(1, row, "Gradation Sl No", innerheadcell);
            sheet.setColumnView(1, 26);
            sheet.addCell(label);

            sheet.mergeCells(2, row, 2, row + 2);
            label = new Label(2, row, "Hrms Id", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(3, row, 3, row + 2);
            label = new Label(3, row, "Gpf No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(4, row, 4, row + 2);
            label = new Label(4, row, "Name ", innerheadcell);
            sheet.setColumnView(4, 50);
            sheet.addCell(label);

            sheet.mergeCells(5, row, 5, row + 2);
            label = new Label(5, row, "Father's Name", innerheadcell);
            sheet.setColumnView(5, 50);
            sheet.addCell(label);

            sheet.mergeCells(6, row, 6, row + 2);
            label = new Label(6, row, "Category", innerheadcell);
            sheet.setColumnView(6, 30);
            sheet.addCell(label);

            sheet.mergeCells(7, row, 7, row + 2);
            label = new Label(7, row, "Date of Birth", innerheadcell);
            sheet.setColumnView(7, 30);
            sheet.addCell(label);

            sheet.mergeCells(8, row, 8, row + 2);
            label = new Label(8, row, "Date of entry into Govt. Service", innerheadcell);
            sheet.setColumnView(8, 30);
            sheet.addCell(label);

            sheet.mergeCells(9, row, 9, row + 2);
            label = new Label(9, row, "Date of appointent in the Present Grade", innerheadcell);
            sheet.setColumnView(9, 30);
            sheet.addCell(label);

            sheet.mergeCells(10, row, 10, row + 2);
            label = new Label(10, row, "Status of disciplinary/Proceeding/Criminal/Vigilance proceeding if any. ", innerheadcell);
            sheet.setColumnView(10, 30);
            sheet.addCell(label);

            sheet.mergeCells(11, row, 11, row + 2);
            label = new Label(11, row, "disciplinary/Proceeding/Criminal/Vigilance proceeding Details. ", innerheadcell);
            sheet.setColumnView(11, 30);
            sheet.addCell(label);

            label = new Label(12, row, "Punishment", innerheadcell);
            sheet.setColumnView(12, 30);
            sheet.addCell(label);
            sheet.mergeCells(12, row, 17, row);

            sheet.mergeCells(18, row, 18, row + 2);
            label = new Label(18, row, "Punishment Detail", innerheadcell);
            sheet.setColumnView(18, 34);
            sheet.addCell(label);

            sheet.mergeCells(19, row, 19, row + 2);
            label = new Label(19, row, "Status of charge has been served on the nominee in Departmental Proceeding. ", innerheadcell);
            sheet.setColumnView(19, 34);
            sheet.addCell(label);

            sheet.mergeCells(20, row, 20, row + 2);
            label = new Label(20, row, "Serving Charge Details. ", innerheadcell);
            sheet.setColumnView(20, 34);
            sheet.addCell(label);

            sheet.mergeCells(21, row, 21, row + 2);
            label = new Label(21, row, "Whether filling uptodate property statement By HRMS", innerheadcell);
            sheet.setColumnView(21, 30);
            sheet.addCell(label);

            sheet.mergeCells(22, row, 22, row + 2);
            label = new Label(22, row, "Whether filling uptodate property statement By Officer", innerheadcell);
            sheet.setColumnView(22, 30);
            sheet.addCell(label);

            sheet.mergeCells(23, row, 23, row + 2);
            label = new Label(23, row, "Date Of filling uptodate property statement By Officer", innerheadcell);
            sheet.setColumnView(23, 30);
            sheet.addCell(label);

            sheet.mergeCells(24, row, 24, row + 2);
            label = new Label(24, row, "Recommendation Status of Nominating Authority", innerheadcell);
            sheet.setColumnView(24, 30);
            sheet.addCell(label);

            sheet.mergeCells(25, row, 25, row + 2);
            label = new Label(25, row, "Remarks of Nominating Authority", innerheadcell);
            sheet.setColumnView(25, 30);
            sheet.addCell(label);

            sheet.mergeCells(26, row, 26, row + 2);
            label = new Label(26, row, "ReCommendation Status of Recommended Authority", innerheadcell);
            sheet.setColumnView(26, 30);
            sheet.addCell(label);

            sheet.mergeCells(27, row, 27, row + 2);
            label = new Label(27, row, "Remarks of Recommended Authority", innerheadcell);
            sheet.setColumnView(27, 30);
            sheet.addCell(label);

            sheet.mergeCells(28, row, 28, row + 2);
            label = new Label(28, row, "Nomination Received From District/Establishment", innerheadcell);
            sheet.setColumnView(28, 30);
            sheet.addCell(label);

            sheet.mergeCells(29, row, 29, row + 2);
            label = new Label(29, row, "Application Id", innerheadcell);
            sheet.setColumnView(29, 30);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(12, row, "Prior to 01.01.2018", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(12, row, 13, row);

            label = new Label(14, row, "During last 5 years i.e. w.e.f. 1.1.2018 to 31.12.2022", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(14, row, 15, row);

            label = new Label(16, row, "From 1.1.23 till date of submission of Nomination Rolls", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(16, row, 17, row);

            row = row + 1;

            label = new Label(12, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(13, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(14, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(15, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(16, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(17, row, "Minor", innerheadcell);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(0, row, "1", innerheadcell);
            sheet.addCell(label);

            label = new Label(1, row, "2", innerheadcell);
            sheet.addCell(label);

            label = new Label(2, row, "3", innerheadcell);
            sheet.addCell(label);

            label = new Label(3, row, "4", innerheadcell);
            sheet.addCell(label);

            label = new Label(4, row, "5", innerheadcell);
            sheet.addCell(label);

            label = new Label(5, row, "6", innerheadcell);
            sheet.addCell(label);

            label = new Label(6, row, "7", innerheadcell);
            sheet.addCell(label);

            label = new Label(7, row, "8", innerheadcell);
            sheet.addCell(label);

            label = new Label(8, row, "9", innerheadcell);
            sheet.addCell(label);

            label = new Label(9, row, "10", innerheadcell);
            sheet.addCell(label);

            label = new Label(10, row, "11", innerheadcell);
            sheet.addCell(label);

            label = new Label(11, row, "12", innerheadcell);
            sheet.addCell(label);

            label = new Label(12, row, "13", innerheadcell);
            sheet.addCell(label);

            label = new Label(13, row, "14", innerheadcell);
            sheet.addCell(label);

            label = new Label(14, row, "15", innerheadcell);
            sheet.addCell(label);

            label = new Label(15, row, "16", innerheadcell);
            sheet.addCell(label);

            label = new Label(16, row, "17", innerheadcell);
            sheet.addCell(label);

            label = new Label(17, row, "18", innerheadcell);
            sheet.addCell(label);

            label = new Label(18, row, "19", innerheadcell);
            sheet.addCell(label);

            label = new Label(19, row, "20", innerheadcell);
            sheet.addCell(label);

            label = new Label(20, row, "21", innerheadcell);
            sheet.addCell(label);

            label = new Label(21, row, "22", innerheadcell);
            sheet.addCell(label);

            label = new Label(22, row, "23", innerheadcell);
            sheet.addCell(label);

            label = new Label(23, row, "24", innerheadcell);
            sheet.addCell(label);

            label = new Label(24, row, "25", innerheadcell);
            sheet.addCell(label);

            label = new Label(25, row, "26", innerheadcell);
            sheet.addCell(label);

            label = new Label(26, row, "27", innerheadcell);
            sheet.addCell(label);

            label = new Label(27, row, "28", innerheadcell);
            sheet.addCell(label);

            label = new Label(28, row, "29", innerheadcell);
            sheet.addCell(label);

            label = new Label(29, row, "30", innerheadcell);
            sheet.addCell(label);

            pst = con.prepareStatement("select pnf.cadre_code,property_submitted_if_any,grade_serial_no,dpcifany,proceeding_detail,pnf.nomination_form_id, gradation_number,em.emp_id,pnd.gpf_no,off_name_abbr, fullname, fathersname, em.category, to_char(em.dob, 'DD-Mon-YYYY') dob, "
                    + "to_char(postingunintdoj, 'DD-Mon-YYYY') postingunintdoj , passing_training_date, place_posting, to_char(doegov, 'DD-Mon-YYYY') doegov, "
                    + "to_char(to_date(jodinspector,'YYYY-MM-DD'), 'DD-Mon-YYYY') jodinspector, punishmentmajorprior, punishmentmajorduring, punishmentmajorfrom, "
                    + " punishmentminorprior, punishmentminorduring, punishmentminorfrom, dpcifany, disc_details, remarks_of_cdmo, dateofserving, to_char(dor, 'DD-Mon-YYYY') dor, "
                    + "remarksnominationgeneral,recommend_status , off_name_abbr, gradation_slno, pnm.entry_year,property_submitted_on_hrms,property_submitted_on_hrms_byofficer,"
                    + "district_recommendation_status,district_recommendation_view,remarks_recommend_authority,have_passed_training_asi,orderno_passing_training_asi,dateof_passing_training_asi,battalion_work_experienceifany, "
                    + "punishment_detail,dateofserving_if_any_dp,servingchargedetail,criminal_case_present_status_ifany,criminal_status_detail from police_nomination_master pnm "
                    + "inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
                    + "inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid "
                    + "inner join police_office_list_data pold on pnm.created_office=pold.police_off_code "
                    + "inner join emp_mast em on pnd.gpf_no=em.gpf_no "
                    + "left outer join police_nomination_form_different_proceeding on pnf.nomination_form_id = police_nomination_form_different_proceeding.nomination_form_id "
                    + "where is_submitted='Y' and nomination_for_new_rank=? and is_archived='N' and pnm.entry_year=?  and pnf.cadre_code=? order by grade_serial_no asc");
            int i = 0;
            pst.setString(1, nominatedForPost);
            pst.setString(2, fiscalyear);
            pst.setString(3, cadreCode);
            rs = pst.executeQuery();
            while (rs.next()) {

                i++;
                row = row + 1;
                label = new Label(0, row, i + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(1, row, rs.getInt("grade_serial_no") + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(2, row, rs.getString("emp_id"), innerheadcell);
                sheet.addCell(label);

                label = new Label(3, row, rs.getString("gpf_no"), innerheadcell);
                sheet.addCell(label);

                label = new Label(4, row, rs.getString("fullname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(5, row, rs.getString("fathersname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(6, row, rs.getString("category"), innerheadcell);
                sheet.addCell(label);

                label = new Label(7, row, rs.getString("dob"), innerheadcell);
                sheet.addCell(label);

                label = new Label(8, row, rs.getString("doegov"), innerheadcell);
                sheet.addCell(label);

                label = new Label(9, row, rs.getString("jodinspector"), innerheadcell);
                sheet.addCell(label);

                if (nfm.getDpcifany() != null && !nfm.getDpcifany().equals("") && nfm.getDpcifany().equals("Y")) {
                    label = new Label(10, row, "Yes", innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(10, row, "No", innerheadcell);
                    sheet.addCell(label);
                }

                label = new Label(11, row, rs.getString("proceeding_detail"), innerheadcell);
                sheet.addCell(label);

                label = new Label(12, row, rs.getString("punishmentmajorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(13, row, rs.getString("punishmentminorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(14, row, rs.getString("punishmentmajorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(15, row, rs.getString("punishmentminorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(16, row, rs.getString("punishmentmajorfrom"), innerheadcell);
                sheet.addCell(label);

                label = new Label(17, row, rs.getString("punishmentminorfrom"), innerheadcell);
                sheet.addCell(label);

                label = new Label(18, row, rs.getString("punishment_detail"), innerheadcell);
                sheet.addCell(label);

                if (rs.getString("dateofserving_if_any_dp").equals("Y")) {
                    label = new Label(19, row, "Yes", innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(19, row, "No", innerheadcell);
                    sheet.addCell(label);
                }

                label = new Label(20, row, rs.getString("servingchargedetail"), innerheadcell);
                sheet.addCell(label);

                label = new Label(21, row, rs.getString("property_submitted_on_hrms"), innerheadcell);
                sheet.addCell(label);

                label = new Label(22, row, rs.getString("property_submitted_if_any"), innerheadcell);
                sheet.addCell(label);

                label = new Label(23, row, rs.getString("property_submitted_on_hrms_byofficer"), innerheadcell);
                sheet.addCell(label);

                label = new Label(24, row, rs.getString("district_recommendation_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(25, row, rs.getString("district_recommendation_view"), innerheadcell);
                sheet.addCell(label);

                label = new Label(26, row, rs.getString("recommend_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(27, row, rs.getString("remarks_recommend_authority"), innerheadcell);
                sheet.addCell(label);

                label = new Label(28, row, rs.getString("off_name_abbr"), innerheadcell);
                sheet.addCell(label);

                label = new Label(29, row, rs.getString("nomination_form_id"), innerheadcell);
                sheet.addCell(label);

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

    @Autowired
    public void downloadNominationCheckListForConstableHavtoASI(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        int row = 0;
        try {
            con = this.repodataSource.getConnection();

            WritableSheet sheet = workbook.createSheet("Check List", 0);

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

            NominationForm nfm = new NominationForm();
            Label label = new Label(0, row, " SERVICE PARTICUALRS OF CONSTABLE to ASSISTANT SUB INSPECTOR OF POLICE ", headcell);//column,row
            sheet.setRowView(row, 30 * 30);
            sheet.addCell(label);
            sheet.mergeCells(0, row, 24, row);

            row = row + 1;

            sheet.mergeCells(0, row, 0, row + 2);
            label = new Label(0, row, "SL No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(1, row, 1, row + 2);
            label = new Label(1, row, "Gradation Sl No", innerheadcell);
            sheet.setColumnView(1, 26);
            sheet.addCell(label);

            sheet.mergeCells(2, row, 2, row + 2);
            label = new Label(2, row, "Hrms Id", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(3, row, 3, row + 2);
            label = new Label(3, row, "Gpf No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(4, row, 4, row + 2);
            label = new Label(4, row, "Name ", innerheadcell);
            sheet.setColumnView(4, 50);
            sheet.addCell(label);

            sheet.mergeCells(5, row, 5, row + 2);
            label = new Label(5, row, "Father's Name", innerheadcell);
            sheet.setColumnView(5, 50);
            sheet.addCell(label);

            sheet.mergeCells(6, row, 6, row + 2);
            label = new Label(6, row, "Category", innerheadcell);
            sheet.setColumnView(6, 31);
            sheet.addCell(label);

            sheet.mergeCells(7, row, 7, row + 2);
            label = new Label(7, row, "Date of Birth", innerheadcell);
            sheet.setColumnView(7, 31);
            sheet.addCell(label);

            sheet.mergeCells(8, row, 8, row + 2);
            label = new Label(8, row, "Present place of Posting", innerheadcell);
            sheet.setColumnView(8, 31);
            sheet.addCell(label);

            sheet.mergeCells(9, row, 9, row + 2);
            label = new Label(9, row, "Date of enlistment", innerheadcell);
            sheet.setColumnView(9, 31);
            sheet.addCell(label);

            sheet.mergeCells(10, row, 10, row + 2);
            label = new Label(10, row, "Rank Join in Government Service", innerheadcell);
            sheet.setColumnView(10, 31);
            sheet.addCell(label);

            sheet.mergeCells(11, row, 11, row + 2);
            label = new Label(11, row, "Date of Passing Written Exam", innerheadcell);
            sheet.setColumnView(11, 31);
            sheet.addCell(label);

            sheet.mergeCells(12, row, 12, row + 2);
            label = new Label(12, row, "Date of joining in the present Rank ", innerheadcell);
            sheet.setColumnView(12, 31);
            sheet.addCell(label);

            sheet.mergeCells(13, row, 13, row + 2);
            label = new Label(13, row, "Departmental Proceedings/Vigilance/HRPC/Criminal Case pending, if any. ", innerheadcell);
            sheet.setColumnView(13, 31);
            sheet.addCell(label);

            sheet.mergeCells(14, row, 14, row + 2);
            label = new Label(14, row, "Departmental Proceedings/Vigilance/HRPC/Criminal Case pending, if any. ", innerheadcell);
            sheet.setColumnView(14, 31);
            sheet.addCell(label);

            label = new Label(15, row, "Punishment", innerheadcell);
            sheet.setColumnView(15, 20);
            sheet.addCell(label);
            sheet.mergeCells(15, row, 20, row);

            sheet.mergeCells(21, row, 21, row + 2);
            label = new Label(21, row, "Views of the Medical Officer", innerheadcell);
            sheet.setColumnView(21, 31);
            sheet.addCell(label);

            sheet.mergeCells(22, row, 22, row + 2);
            label = new Label(22, row, "Whether filling uptodate property statement By HRMS", innerheadcell);
            sheet.setColumnView(22, 31);
            sheet.addCell(label);

            sheet.mergeCells(23, row, 23, row + 2);
            label = new Label(23, row, "Whether filling uptodate property statement By Officer", innerheadcell);
            sheet.setColumnView(23, 31);
            sheet.addCell(label);

            sheet.mergeCells(24, row, 24, row + 2);
            label = new Label(24, row, "Date Of filling uptodate property statement By Officer", innerheadcell);
            sheet.setColumnView(24, 31);
            sheet.addCell(label);

            sheet.mergeCells(25, row, 25, row + 2);
            label = new Label(25, row, "Recommendation Status of Nominating Authority", innerheadcell);
            sheet.setColumnView(25, 31);
            sheet.addCell(label);

            sheet.mergeCells(26, row, 26, row + 2);
            label = new Label(26, row, "Remarks of Nominating Authority", innerheadcell);
            sheet.setColumnView(26, 31);
            sheet.addCell(label);

            sheet.mergeCells(27, row, 27, row + 2);
            label = new Label(27, row, "ReCommendation Status of Recommended Authority", innerheadcell);
            sheet.setColumnView(27, 31);
            sheet.addCell(label);

            sheet.mergeCells(28, row, 28, row + 2);
            label = new Label(28, row, "Remarks of Recommended Authority", innerheadcell);
            sheet.setColumnView(28, 31);
            sheet.addCell(label);

            sheet.mergeCells(29, row, 29, row + 2);
            label = new Label(29, row, "Nomination Received From District/Establishment", innerheadcell);
            sheet.setColumnView(29, 31);
            sheet.addCell(label);

            sheet.mergeCells(30, row, 30, row + 2);
            label = new Label(30, row, "Application Id", innerheadcell);
            sheet.setColumnView(30, 31);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(15, row, "Prior to 01.01.2017", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(15, row, 16, row);

            label = new Label(17, row, "During last 5 years i.e. w.e.f. 1.1.2017 to 31.12.2021", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(17, row, 18, row);

            label = new Label(19, row, "From 1.1.22 till date of submission of Nomination Rolls", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(19, row, 20, row);

            row = row + 1;

            label = new Label(15, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(16, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(17, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(18, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(19, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(20, row, "Minor", innerheadcell);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(0, row, "1", innerheadcell);
            sheet.addCell(label);

            label = new Label(1, row, "2", innerheadcell);
            sheet.addCell(label);

            label = new Label(2, row, "3", innerheadcell);
            sheet.addCell(label);

            label = new Label(3, row, "4", innerheadcell);
            sheet.addCell(label);

            label = new Label(4, row, "5", innerheadcell);
            sheet.addCell(label);

            label = new Label(5, row, "6", innerheadcell);
            sheet.addCell(label);

            label = new Label(6, row, "7", innerheadcell);
            sheet.addCell(label);

            label = new Label(7, row, "8", innerheadcell);
            sheet.addCell(label);

            label = new Label(8, row, "9", innerheadcell);
            sheet.addCell(label);

            label = new Label(9, row, "10", innerheadcell);
            sheet.addCell(label);

            label = new Label(10, row, "11", innerheadcell);
            sheet.addCell(label);

            label = new Label(11, row, "12", innerheadcell);
            sheet.addCell(label);

            label = new Label(12, row, "13", innerheadcell);
            sheet.addCell(label);

            label = new Label(13, row, "14", innerheadcell);
            sheet.addCell(label);

            label = new Label(14, row, "15", innerheadcell);
            sheet.addCell(label);

            label = new Label(15, row, "16", innerheadcell);
            sheet.addCell(label);

            label = new Label(16, row, "17", innerheadcell);
            sheet.addCell(label);

            label = new Label(17, row, "18", innerheadcell);
            sheet.addCell(label);

            label = new Label(18, row, "19", innerheadcell);
            sheet.addCell(label);

            label = new Label(19, row, "20", innerheadcell);
            sheet.addCell(label);

            label = new Label(20, row, "21", innerheadcell);
            sheet.addCell(label);

            label = new Label(21, row, "22", innerheadcell);
            sheet.addCell(label);

            label = new Label(22, row, "23", innerheadcell);
            sheet.addCell(label);

            label = new Label(23, row, "24", innerheadcell);
            sheet.addCell(label);

            label = new Label(24, row, "25", innerheadcell);
            sheet.addCell(label);

            label = new Label(25, row, "26", innerheadcell);
            sheet.addCell(label);

            label = new Label(26, row, "27", innerheadcell);
            sheet.addCell(label);

            label = new Label(27, row, "28", innerheadcell);
            sheet.addCell(label);

            label = new Label(28, row, "29", innerheadcell);
            sheet.addCell(label);

            label = new Label(29, row, "30", innerheadcell);
            sheet.addCell(label);

            label = new Label(30, row, "31", innerheadcell);
            sheet.addCell(label);

            pst = con.prepareStatement("select nomination_form_id,grade_serial_no, gradation_number,off_name_abbr, fullname, fathersname, pnf.category, to_char(pnf.dob, 'DD-Mon-YYYY') dob, "
                    + " to_char(postingunintdoj, 'DD-Mon-YYYY') postingunintdoj , passing_training_date, place_posting, to_char(doegov, 'DD-Mon-YYYY') doegov, "
                    + " to_char(to_date(jodinspector,'YYYY-MM-DD'), 'DD-Mon-YYYY') jodinspector, punishmentmajorprior, punishmentmajorduring, punishmentmajorfrom, \n"
                    + "  punishmentminorprior, punishmentminorduring, punishmentminorfrom, dpcifany, disc_details, physicalFitness, dateofserving, to_char(dor, 'DD-Mon-YYYY') dor, "
                    + " remarksnominationgeneral,recommend_status , off_name_abbr, gradation_slno, pnm.entry_year,orderdate_passing__constable_training,district_recommendation_status,rank_joinin_govservice,jodinspector, "
                    + " property_submitted_if_any,property_submitted_on_hrms,property_submitted_on_hrms_byofficer,district_recommendation_view,remarks_recommend_authority,em.emp_id,pnd.gpf_no from police_nomination_master pnm "
                    + " inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
                    + " inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid "
                    + " inner join police_office_list_data pold on pnm.created_office=pold.police_off_code "
                    + " inner join emp_mast em on pnd.gpf_no=em.gpf_no"
                    + " where is_submitted='Y' and nomination_for_new_rank=? and is_archived='N' and pnm.entry_year=? "
                    + " order by gradation_slno ");
            int i = 0;
            pst.setString(1, nominatedForPost);
            pst.setString(2, fiscalyear);
            rs = pst.executeQuery();
            while (rs.next()) {

                i++;
                row = row + 1;
                label = new Label(0, row, i + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(1, row, rs.getInt("grade_serial_no") + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(2, row, rs.getString("emp_id"), innerheadcell);
                sheet.addCell(label);

                label = new Label(3, row, rs.getString("gpf_no"), innerheadcell);
                sheet.addCell(label);

                label = new Label(4, row, rs.getString("fullname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(5, row, rs.getString("fathersname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(6, row, rs.getString("category"), innerheadcell);
                sheet.addCell(label);

                label = new Label(7, row, rs.getString("dob"), innerheadcell);
                sheet.addCell(label);

                label = new Label(8, row, rs.getString("place_posting"), innerheadcell);
                sheet.addCell(label);

                label = new Label(9, row, rs.getString("doegov"), innerheadcell);
                sheet.addCell(label);

                label = new Label(10, row, rs.getString("rank_joinin_govservice"), innerheadcell);
                sheet.addCell(label);

                label = new Label(11, row, CommonFunctions.getFormattedOutputDate1(rs.getDate("orderdate_passing__constable_training")), innerheadcell);
                sheet.addCell(label);

                label = new Label(12, row, rs.getString("jodinspector"), innerheadcell);
                sheet.addCell(label);

                if (nfm.getDpcifany() != null && !nfm.getDpcifany().equals("") && nfm.getDpcifany().equals("Y")) {
                    label = new Label(13, row, "Yes", innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(13, row, "No", innerheadcell);
                    sheet.addCell(label);
                }

                if (nfm.getDpcifany() != null && !nfm.getDpcifany().equals("") && nfm.getDpcifany().equals("Y")) {
                    label = new Label(14, row, rs.getString("proceeding_detail"), innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(14, row, "", innerheadcell);
                    sheet.addCell(label);
                }

                label = new Label(15, row, rs.getString("punishmentmajorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(16, row, rs.getString("punishmentminorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(16, row, rs.getString("punishmentmajorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(18, row, rs.getString("punishmentminorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(19, row, rs.getString("punishmentmajorfrom"), innerheadcell);
                sheet.addCell(label);

                label = new Label(20, row, rs.getString("punishmentminorfrom"), innerheadcell);
                sheet.addCell(label);

                label = new Label(21, row, rs.getString("physicalfitness"), innerheadcell);
                sheet.addCell(label);

                label = new Label(22, row, rs.getString("property_submitted_on_hrms"), innerheadcell);
                sheet.addCell(label);

                label = new Label(23, row, rs.getString("property_submitted_if_any"), innerheadcell);
                sheet.addCell(label);

                label = new Label(24, row, rs.getString("property_submitted_on_hrms_byofficer"), innerheadcell);
                sheet.addCell(label);

                label = new Label(25, row, rs.getString("district_recommendation_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(26, row, rs.getString("district_recommendation_view"), innerheadcell);
                sheet.addCell(label);

                label = new Label(27, row, rs.getString("recommend_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(28, row, rs.getString("remarks_recommend_authority"), innerheadcell);
                sheet.addCell(label);

                label = new Label(29, row, rs.getString("off_name_abbr"), innerheadcell);
                sheet.addCell(label);

                label = new Label(30, row, rs.getString("nomination_form_id"), innerheadcell);
                sheet.addCell(label);

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

    @Override
    public void downloadNominationCheckListForASIArmed2SubInspectorArmedBatalionCadreOdiaCoy(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        int row = 0;
        try {
            con = this.repodataSource.getConnection();

            WritableSheet sheet = workbook.createSheet("Check List", 0);

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

            NominationForm nfm = new NominationForm();
            Label label = new Label(0, row, " SERVICE PARTICUALRS OF ASI (ARMED) FOR PROMOTION TO THE RANK OF SUB INSPECTOR (ARMED) OF BATALION CADRE(ODIA COY) ", headcell);//column,row
            sheet.setRowView(row, 30 * 30);
            sheet.addCell(label);
            sheet.mergeCells(0, row, 24, row);

            row = row + 1;

            sheet.mergeCells(0, row, 0, row + 2);
            label = new Label(0, row, "SL No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(1, row, 1, row + 2);
            label = new Label(1, row, "Gradation Sl No", innerheadcell);
            sheet.setColumnView(1, 26);
            sheet.addCell(label);

            sheet.mergeCells(2, row, 2, row + 2);
            label = new Label(2, row, "Hrms Id", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(3, row, 3, row + 2);
            label = new Label(3, row, "Gpf No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(4, row, 4, row + 2);
            label = new Label(4, row, "Name ", innerheadcell);
            sheet.setColumnView(4, 50);
            sheet.addCell(label);

            sheet.mergeCells(5, row, 5, row + 2);
            label = new Label(5, row, "Father's Name", innerheadcell);
            sheet.setColumnView(5, 50);
            sheet.addCell(label);

            sheet.mergeCells(6, row, 6, row + 2);
            label = new Label(6, row, "Category", innerheadcell);
            sheet.setColumnView(6, 30);
            sheet.addCell(label);

            sheet.mergeCells(7, row, 7, row + 2);
            label = new Label(7, row, "Date of Birth", innerheadcell);
            sheet.setColumnView(7, 30);
            sheet.addCell(label);

            sheet.mergeCells(8, row, 8, row + 2);
            label = new Label(8, row, "Date of entry into Govt. Service", innerheadcell);
            sheet.setColumnView(8, 30);
            sheet.addCell(label);

            sheet.mergeCells(9, row, 9, row + 2);
            label = new Label(9, row, "Date of appointent in the Present Grade", innerheadcell);
            sheet.setColumnView(9, 30);
            sheet.addCell(label);

            sheet.mergeCells(10, row, 10, row + 2);
            label = new Label(10, row, "Status of disciplinary/Proceeding/Criminal/Vigilance proceeding if any. ", innerheadcell);
            sheet.setColumnView(10, 30);
            sheet.addCell(label);

            sheet.mergeCells(11, row, 11, row + 2);
            label = new Label(11, row, "disciplinary/Proceeding/Criminal/Vigilance proceeding Details. ", innerheadcell);
            sheet.setColumnView(11, 30);
            sheet.addCell(label);

            label = new Label(12, row, "Punishment", innerheadcell);
            sheet.setColumnView(12, 30);
            sheet.addCell(label);
            sheet.mergeCells(12, row, 17, row);

            sheet.mergeCells(18, row, 18, row + 2);
            label = new Label(18, row, "Punishment Detail", innerheadcell);
            sheet.setColumnView(18, 34);
            sheet.addCell(label);

            sheet.mergeCells(19, row, 19, row + 2);
            label = new Label(19, row, "Status of charge has been served on the nominee in Departmental Proceeding. ", innerheadcell);
            sheet.setColumnView(19, 34);
            sheet.addCell(label);

            sheet.mergeCells(20, row, 20, row + 2);
            label = new Label(20, row, "Serving Charge Details. ", innerheadcell);
            sheet.setColumnView(20, 34);
            sheet.addCell(label);

            sheet.mergeCells(21, row, 21, row + 2);
            label = new Label(21, row, "Whether filling uptodate property statement By HRMS", innerheadcell);
            sheet.setColumnView(21, 30);
            sheet.addCell(label);

            sheet.mergeCells(22, row, 22, row + 2);
            label = new Label(22, row, "Whether filling uptodate property statement By Officer", innerheadcell);
            sheet.setColumnView(22, 30);
            sheet.addCell(label);

            sheet.mergeCells(23, row, 23, row + 2);
            label = new Label(23, row, "Date Of filling uptodate property statement By Officer", innerheadcell);
            sheet.setColumnView(23, 30);
            sheet.addCell(label);

            sheet.mergeCells(24, row, 24, row + 2);
            label = new Label(24, row, "Recommendation Status of Nominating Authority", innerheadcell);
            sheet.setColumnView(24, 30);
            sheet.addCell(label);

            sheet.mergeCells(25, row, 25, row + 2);
            label = new Label(25, row, "Remarks of Nominating Authority", innerheadcell);
            sheet.setColumnView(25, 30);
            sheet.addCell(label);

            sheet.mergeCells(26, row, 26, row + 2);
            label = new Label(26, row, "ReCommendation Status of Recommended Authority", innerheadcell);
            sheet.setColumnView(26, 30);
            sheet.addCell(label);

            sheet.mergeCells(27, row, 27, row + 2);
            label = new Label(27, row, "Remarks of Recommended Authority", innerheadcell);
            sheet.setColumnView(27, 30);
            sheet.addCell(label);

            sheet.mergeCells(28, row, 28, row + 2);
            label = new Label(28, row, "Nomination Received From District/Establishment", innerheadcell);
            sheet.setColumnView(28, 30);
            sheet.addCell(label);

            sheet.mergeCells(29, row, 29, row + 2);
            label = new Label(29, row, "Application Id", innerheadcell);
            sheet.setColumnView(29, 30);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(12, row, "Prior to 01.01.2018", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(12, row, 13, row);

            label = new Label(14, row, "During last 5 years i.e. w.e.f. 1.1.2018 to 31.12.2022", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(14, row, 15, row);

            label = new Label(16, row, "From 1.1.23 till date of submission of Nomination Rolls", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(16, row, 17, row);

            row = row + 1;

            label = new Label(12, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(13, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(14, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(15, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(16, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(17, row, "Minor", innerheadcell);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(0, row, "1", innerheadcell);
            sheet.addCell(label);

            label = new Label(1, row, "2", innerheadcell);
            sheet.addCell(label);

            label = new Label(2, row, "3", innerheadcell);
            sheet.addCell(label);

            label = new Label(3, row, "4", innerheadcell);
            sheet.addCell(label);

            label = new Label(4, row, "5", innerheadcell);
            sheet.addCell(label);

            label = new Label(5, row, "6", innerheadcell);
            sheet.addCell(label);

            label = new Label(6, row, "7", innerheadcell);
            sheet.addCell(label);

            label = new Label(7, row, "8", innerheadcell);
            sheet.addCell(label);

            label = new Label(8, row, "9", innerheadcell);
            sheet.addCell(label);

            label = new Label(9, row, "10", innerheadcell);
            sheet.addCell(label);

            label = new Label(10, row, "11", innerheadcell);
            sheet.addCell(label);

            label = new Label(11, row, "12", innerheadcell);
            sheet.addCell(label);

            label = new Label(12, row, "13", innerheadcell);
            sheet.addCell(label);

            label = new Label(13, row, "14", innerheadcell);
            sheet.addCell(label);

            label = new Label(14, row, "15", innerheadcell);
            sheet.addCell(label);

            label = new Label(15, row, "16", innerheadcell);
            sheet.addCell(label);

            label = new Label(16, row, "17", innerheadcell);
            sheet.addCell(label);

            label = new Label(17, row, "18", innerheadcell);
            sheet.addCell(label);

            label = new Label(18, row, "19", innerheadcell);
            sheet.addCell(label);

            label = new Label(19, row, "20", innerheadcell);
            sheet.addCell(label);

            label = new Label(20, row, "21", innerheadcell);
            sheet.addCell(label);

            label = new Label(21, row, "22", innerheadcell);
            sheet.addCell(label);

            label = new Label(22, row, "23", innerheadcell);
            sheet.addCell(label);

            label = new Label(23, row, "24", innerheadcell);
            sheet.addCell(label);

            label = new Label(24, row, "25", innerheadcell);
            sheet.addCell(label);

            label = new Label(25, row, "26", innerheadcell);
            sheet.addCell(label);

            label = new Label(26, row, "27", innerheadcell);
            sheet.addCell(label);

            label = new Label(27, row, "28", innerheadcell);
            sheet.addCell(label);

            label = new Label(28, row, "29", innerheadcell);
            sheet.addCell(label);

            label = new Label(29, row, "30", innerheadcell);
            sheet.addCell(label);

            pst = con.prepareStatement("select pnf.cadre_code,property_submitted_if_any,grade_serial_no,dpcifany,proceeding_detail,pnf.nomination_form_id, gradation_number,em.emp_id,pnd.gpf_no,off_name_abbr, fullname, fathersname, em.category, to_char(em.dob, 'DD-Mon-YYYY') dob, "
                    + "to_char(postingunintdoj, 'DD-Mon-YYYY') postingunintdoj , passing_training_date, place_posting, to_char(doegov, 'DD-Mon-YYYY') doegov, "
                    + "to_char(to_date(jodinspector,'YYYY-MM-DD'), 'DD-Mon-YYYY') jodinspector, punishmentmajorprior, punishmentmajorduring, punishmentmajorfrom, "
                    + " punishmentminorprior, punishmentminorduring, punishmentminorfrom, dpcifany, disc_details, remarks_of_cdmo, dateofserving, to_char(dor, 'DD-Mon-YYYY') dor, "
                    + "remarksnominationgeneral,recommend_status , off_name_abbr, gradation_slno, pnm.entry_year,property_submitted_on_hrms,property_submitted_on_hrms_byofficer,"
                    + "district_recommendation_status,district_recommendation_view,remarks_recommend_authority,have_passed_training_asi,orderno_passing_training_asi,dateof_passing_training_asi,battalion_work_experienceifany, "
                    + "punishment_detail,dateofserving_if_any_dp,servingchargedetail,criminal_case_present_status_ifany,criminal_status_detail from police_nomination_master pnm "
                    + "inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
                    + "inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid "
                    + "inner join police_office_list_data pold on pnm.created_office=pold.police_off_code "
                    + "inner join emp_mast em on pnd.gpf_no=em.gpf_no "
                    + "left outer join police_nomination_form_different_proceeding on pnf.nomination_form_id = police_nomination_form_different_proceeding.nomination_form_id "
                    + "where is_submitted='Y' and nomination_for_new_rank=? and is_archived='N' and pnm.entry_year=?  and pnf.cadre_code=? order by grade_serial_no asc");
            int i = 0;
            pst.setString(1, nominatedForPost);
            pst.setString(2, fiscalyear);
            pst.setString(3, cadreCode);
            rs = pst.executeQuery();
            while (rs.next()) {

                i++;
                row = row + 1;
                label = new Label(0, row, i + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(1, row, rs.getInt("grade_serial_no") + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(2, row, rs.getString("emp_id"), innerheadcell);
                sheet.addCell(label);

                label = new Label(3, row, rs.getString("gpf_no"), innerheadcell);
                sheet.addCell(label);

                label = new Label(4, row, rs.getString("fullname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(5, row, rs.getString("fathersname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(6, row, rs.getString("category"), innerheadcell);
                sheet.addCell(label);

                label = new Label(7, row, rs.getString("dob"), innerheadcell);
                sheet.addCell(label);

                label = new Label(8, row, rs.getString("doegov"), innerheadcell);
                sheet.addCell(label);

                label = new Label(9, row, rs.getString("jodinspector"), innerheadcell);
                sheet.addCell(label);

                if (nfm.getDpcifany() != null && !nfm.getDpcifany().equals("") && nfm.getDpcifany().equals("Y")) {
                    label = new Label(10, row, "Yes", innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(10, row, "No", innerheadcell);
                    sheet.addCell(label);
                }

                label = new Label(11, row, rs.getString("proceeding_detail"), innerheadcell);
                sheet.addCell(label);

                label = new Label(12, row, rs.getString("punishmentmajorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(13, row, rs.getString("punishmentminorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(14, row, rs.getString("punishmentmajorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(15, row, rs.getString("punishmentminorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(16, row, rs.getString("punishmentmajorfrom"), innerheadcell);
                sheet.addCell(label);

                label = new Label(17, row, rs.getString("punishmentminorfrom"), innerheadcell);
                sheet.addCell(label);

                label = new Label(18, row, rs.getString("punishment_detail"), innerheadcell);
                sheet.addCell(label);

                if (rs.getString("dateofserving_if_any_dp").equals("Y")) {
                    label = new Label(19, row, "Yes", innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(19, row, "No", innerheadcell);
                    sheet.addCell(label);
                }

                label = new Label(20, row, rs.getString("servingchargedetail"), innerheadcell);
                sheet.addCell(label);

                if (rs.getString("criminal_case_present_status_ifany").equals("Y")) {
                    label = new Label(21, row, "Yes", innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(21, row, "No", innerheadcell);
                    sheet.addCell(label);
                }

                label = new Label(21, row, rs.getString("property_submitted_on_hrms"), innerheadcell);
                sheet.addCell(label);

                label = new Label(22, row, rs.getString("property_submitted_if_any"), innerheadcell);
                sheet.addCell(label);

                label = new Label(23, row, rs.getString("property_submitted_on_hrms_byofficer"), innerheadcell);
                sheet.addCell(label);

                label = new Label(24, row, rs.getString("district_recommendation_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(25, row, rs.getString("district_recommendation_view"), innerheadcell);
                sheet.addCell(label);

                label = new Label(26, row, rs.getString("recommend_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(27, row, rs.getString("remarks_recommend_authority"), innerheadcell);
                sheet.addCell(label);

                label = new Label(28, row, rs.getString("off_name_abbr"), innerheadcell);
                sheet.addCell(label);

                label = new Label(29, row, rs.getString("nomination_form_id"), innerheadcell);
                sheet.addCell(label);

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

    @Override
    public void downloadNominationCheckList(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        int row = 0;
        try {
            con = this.repodataSource.getConnection();

            WritableSheet sheet = workbook.createSheet("Check List", 0);

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

            if (nominatedForPost.equals("140293")) {
                Label label = new Label(0, row, " Nomination for the post of ASSISTANT SUB INSPECTOR OF POLICE to SUB INSPECTOR OF POLICE ", headcell);//column,row
                sheet.setRowView(row, 30 * 30);
                sheet.addCell(label);
                sheet.mergeCells(0, row, 19, row);

                row = row + 1;

                sheet.mergeCells(0, row, 0, row + 2);
                label = new Label(0, row, "SL No", innerheadcell);
                sheet.setRowView(row, 40 * 30);
                sheet.addCell(label);

                sheet.mergeCells(1, row, 1, row + 2);
                label = new Label(1, row, "Gradation Sl No", innerheadcell);
                sheet.setColumnView(1, 20);
                sheet.addCell(label);

                sheet.mergeCells(2, row, 2, row + 2);
                label = new Label(2, row, "Name ", innerheadcell);
                sheet.setColumnView(2, 50);
                sheet.addCell(label);

                sheet.mergeCells(3, row, 3, row + 2);
                label = new Label(3, row, "Father's Name", innerheadcell);
                sheet.setColumnView(3, 50);
                sheet.addCell(label);

                sheet.mergeCells(4, row, 4, row + 2);
                label = new Label(4, row, "Category", innerheadcell);
                sheet.setColumnView(4, 20);
                sheet.addCell(label);

                sheet.mergeCells(5, row, 5, row + 2);
                label = new Label(5, row, "Present place of Posting", innerheadcell);
                sheet.setColumnView(5, 20);
                sheet.addCell(label);

                sheet.mergeCells(6, row, 6, row + 2);
                label = new Label(6, row, "Date of Birth", innerheadcell);
                sheet.setColumnView(6, 20);
                sheet.addCell(label);

                sheet.mergeCells(7, row, 7, row + 2);
                label = new Label(7, row, "Date of Passing ASI's course of training", innerheadcell);
                sheet.setColumnView(7, 20);
                sheet.addCell(label);

                sheet.mergeCells(8, row, 8, row + 2);
                label = new Label(8, row, "Departmental Proceedings/Vigilance/HRPC/Criminal Case pending, if any. ", innerheadcell);
                sheet.setColumnView(8, 20);
                sheet.addCell(label);

                label = new Label(9, row, "Punishment", innerheadcell);
                sheet.setColumnView(9, 20);
                sheet.addCell(label);
                sheet.mergeCells(9, row, 14, row);

                sheet.mergeCells(15, row, 15, row + 2);
                label = new Label(15, row, "Views of the Medical Officer", innerheadcell);
                sheet.setColumnView(15, 20);
                sheet.addCell(label);

                sheet.mergeCells(16, row, 16, row + 2);
                label = new Label(16, row, "Remarks of Nominating Authority", innerheadcell);
                sheet.setColumnView(16, 20);
                sheet.addCell(label);

                sheet.mergeCells(17, row, 17, row + 2);
                label = new Label(17, row, "Remarks of Recommended Authority", innerheadcell);
                sheet.setColumnView(17, 20);
                sheet.addCell(label);

                sheet.mergeCells(18, row, 18, row + 2);
                label = new Label(18, row, "Nomination Received From District/Establishment", innerheadcell);
                sheet.setColumnView(18, 20);
                sheet.addCell(label);

                sheet.mergeCells(19, row, 19, row + 2);
                label = new Label(19, row, "Application Id", innerheadcell);
                sheet.setColumnView(19, 20);
                sheet.addCell(label);

                row = row + 1;

                label = new Label(9, row, "Prior to 01.01.2016", innerheadcell);
                sheet.addCell(label);
                sheet.mergeCells(9, row, 10, row);

                label = new Label(11, row, "During last 5 years i.e. w.e.f. 1.1.2016 to 31.12.2020", innerheadcell);
                sheet.addCell(label);
                sheet.mergeCells(11, row, 12, row);

                label = new Label(13, row, "From 1.1.21 till date of submission of Nomination Rolls", innerheadcell);
                sheet.addCell(label);
                sheet.mergeCells(13, row, 14, row);

                row = row + 1;

                label = new Label(9, row, "Major", innerheadcell);
                sheet.addCell(label);
                label = new Label(10, row, "Minor", innerheadcell);
                sheet.addCell(label);
                label = new Label(11, row, "Major", innerheadcell);
                sheet.addCell(label);
                label = new Label(12, row, "Minor", innerheadcell);
                sheet.addCell(label);
                label = new Label(13, row, "Major", innerheadcell);
                sheet.addCell(label);
                label = new Label(14, row, "Minor", innerheadcell);
                sheet.addCell(label);

                row = row + 1;

                label = new Label(0, row, "1", innerheadcell);
                sheet.addCell(label);

                label = new Label(1, row, "2", innerheadcell);
                sheet.addCell(label);

                label = new Label(2, row, "3", innerheadcell);
                sheet.addCell(label);

                label = new Label(3, row, "4", innerheadcell);
                sheet.addCell(label);

                label = new Label(4, row, "5", innerheadcell);
                sheet.addCell(label);

                label = new Label(5, row, "6", innerheadcell);
                sheet.addCell(label);

                label = new Label(6, row, "7", innerheadcell);
                sheet.addCell(label);

                label = new Label(7, row, "8", innerheadcell);
                sheet.addCell(label);

                label = new Label(8, row, "9", innerheadcell);
                sheet.addCell(label);

                label = new Label(9, row, "10", innerheadcell);
                sheet.addCell(label);

                label = new Label(10, row, "11", innerheadcell);
                sheet.addCell(label);

                label = new Label(11, row, "12", innerheadcell);
                sheet.addCell(label);

                label = new Label(12, row, "13", innerheadcell);
                sheet.addCell(label);

                label = new Label(13, row, "14", innerheadcell);
                sheet.addCell(label);

                label = new Label(14, row, "15", innerheadcell);
                sheet.addCell(label);

                label = new Label(15, row, "16", innerheadcell);
                sheet.addCell(label);

                label = new Label(16, row, "17", innerheadcell);
                sheet.addCell(label);

                label = new Label(17, row, "18", innerheadcell);
                sheet.addCell(label);

                label = new Label(18, row, "19", innerheadcell);
                sheet.addCell(label);

                label = new Label(19, row, "20", innerheadcell);
                sheet.addCell(label);

                pst = con.prepareStatement("select nomination_form_id, gradation_number,off_name_abbr, fullname, fathersname, category, to_char(dob, 'DD-Mon-YYYY') dob, "
                        + " to_char(postingunintdoj, 'DD-Mon-YYYY') postingunintdoj , passing_training_date, place_posting, to_char(doegov, 'DD-Mon-YYYY') doegov, "
                        + " to_char(to_date(jodinspector,'YYYY-MM-DD'), 'DD-Mon-YYYY') jodinspector, punishmentmajorprior, punishmentmajorduring, punishmentmajorfrom, \n"
                        + "  punishmentminorprior, punishmentminorduring, punishmentminorfrom, dpcifany, disc_details, remarks_of_cdmo, dateofserving, to_char(dor, 'DD-Mon-YYYY') dor, "
                        + " remarksnominationgeneral,recommend_status , off_name_abbr, gradation_slno, pnm.entry_year  from police_nomination_master pnm "
                        + " inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
                        + " inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid "
                        + " inner join police_office_list_data pold on pnm.created_office=pold.police_off_code "
                        + " where is_submitted='Y' and nomination_for_new_rank=? and is_archived='N' and pnm.entry_year=? "
                        + " order by gradation_slno ");
                int i = 0;
                pst.setString(1, nominatedForPost);
                pst.setString(2, fiscalyear);
                rs = pst.executeQuery();

                /*pst = con.prepareStatement("SELECT proceeding_detail FROM police_nomination_form_different_proceeding where nomination_form_id=? order by proceeding_detail_id");
                 pst.setInt(1, nominationFormId);
                 rs = pst.executeQuery();
                 System.out.println("proceedingDetailId is" +nominationFormId); */
                while (rs.next()) {

                    i++;
                    row = row + 1;
                    label = new Label(0, row, i + "", innerheadcell);
                    sheet.addCell(label);

                    label = new Label(1, row, rs.getInt("gradation_slno") + "", innerheadcell);
                    sheet.addCell(label);

                    label = new Label(2, row, rs.getString("fullname"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(3, row, rs.getString("fathersname"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(4, row, rs.getString("category"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(5, row, rs.getString("place_posting"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(6, row, rs.getString("dob"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(7, row, CommonFunctions.getFormattedOutputDate1(rs.getDate("passing_training_date")), innerheadcell);
                    sheet.addCell(label);

                    String dpListString = getDpListForASI(rs.getInt("nomination_form_id"));
                    if (dpListString != null && !dpListString.equals("")) {
                        label = new Label(8, row, dpListString, innerheadcell);
                    } else {
                        label = new Label(8, row, rs.getString("remarksnominationgeneral"), innerheadcell);
                    }
                    sheet.addCell(label);

                    label = new Label(9, row, rs.getString("punishmentmajorprior"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(10, row, rs.getString("punishmentminorprior"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(11, row, rs.getString("punishmentmajorduring"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(12, row, rs.getString("punishmentminorduring"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(13, row, rs.getString("punishmentmajorfrom"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(14, row, rs.getString("punishmentminorfrom"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(15, row, rs.getString("remarks_of_cdmo"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(16, row, rs.getString("remarksnominationgeneral"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(17, row, rs.getString("recommend_status"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(18, row, rs.getString("off_name_abbr"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(19, row, rs.getString("nomination_form_id"), innerheadcell);
                    sheet.addCell(label);

                }
            } else if (nominatedForPost.equals("140858")) {
                Label label = new Label(0, row, " Nomination for the post of CONSTABLE to ASSISTANT SUB INSPECTOR OF POLICE", headcell);//column,row
                sheet.setRowView(row, 30 * 30);
                sheet.addCell(label);
                sheet.mergeCells(0, row, 19, row);

                row = row + 1;

                sheet.mergeCells(0, row, 0, row + 2);
                label = new Label(0, row, "SL No", innerheadcell);
                sheet.setRowView(row, 40 * 30);
                sheet.addCell(label);

                sheet.mergeCells(1, row, 1, row + 2);
                label = new Label(1, row, "Gradation Sl No", innerheadcell);
                sheet.setColumnView(1, 20);
                sheet.addCell(label);

                sheet.mergeCells(2, row, 2, row + 2);
                label = new Label(2, row, "Name ", innerheadcell);
                sheet.setColumnView(2, 50);
                sheet.addCell(label);

                sheet.mergeCells(3, row, 3, row + 2);
                label = new Label(3, row, "Father's Name", innerheadcell);
                sheet.setColumnView(3, 50);
                sheet.addCell(label);

                sheet.mergeCells(4, row, 4, row + 2);
                label = new Label(4, row, "Category", innerheadcell);
                sheet.setColumnView(4, 27);
                sheet.addCell(label);

                sheet.mergeCells(5, row, 5, row + 2);
                label = new Label(5, row, "Present place of Posting", innerheadcell);
                sheet.setColumnView(5, 27);
                sheet.addCell(label);

                sheet.mergeCells(6, row, 6, row + 2);
                label = new Label(6, row, "Date of Birth", innerheadcell);
                sheet.setColumnView(6, 27);
                sheet.addCell(label);

                sheet.mergeCells(7, row, 7, row + 2);
                label = new Label(7, row, "Date of enlistment ", innerheadcell);
                sheet.setColumnView(7, 27);
                sheet.addCell(label);

                sheet.mergeCells(8, row, 8, row + 2);
                label = new Label(8, row, "Rank Join in Government Service ", innerheadcell);
                sheet.setColumnView(8, 27);
                sheet.addCell(label);

                sheet.mergeCells(9, row, 9, row + 2);
                label = new Label(9, row, "Date of Passing Written Exam", innerheadcell);
                sheet.setColumnView(9, 27);
                sheet.addCell(label);

                sheet.mergeCells(10, row, 10, row + 2);
                label = new Label(10, row, "Date of joining in the present Rank", innerheadcell);
                sheet.setColumnView(10, 27);
                sheet.addCell(label);

                sheet.mergeCells(11, row, 11, row + 2);
                label = new Label(11, row, "Departmental Proceedings/Vigilance/HRPC/Criminal Case pending, if any. ", innerheadcell);
                sheet.setColumnView(11, 27);
                sheet.addCell(label);

                label = new Label(12, row, "Punishment", innerheadcell);
                sheet.setColumnView(12, 27);
                sheet.addCell(label);
                sheet.mergeCells(12, row, 14, row);

                sheet.mergeCells(18, row, 18, row + 2);
                label = new Label(18, row, "Views of the Medical Officer", innerheadcell);
                sheet.setColumnView(18, 27);
                sheet.addCell(label);

                sheet.mergeCells(19, row, 19, row + 2);
                label = new Label(19, row, "Remarks of Nominating Authority", innerheadcell);
                sheet.setColumnView(9, 27);
                sheet.addCell(label);

                sheet.mergeCells(20, row, 20, row + 2);
                label = new Label(20, row, "Remarks of Recommended Authority", innerheadcell);
                sheet.setColumnView(20, 27);
                sheet.addCell(label);

                sheet.mergeCells(21, row, 21, row + 2);
                label = new Label(21, row, "Nomination Received From District/Establishment", innerheadcell);
                sheet.setColumnView(21, 27);
                sheet.addCell(label);

                sheet.mergeCells(22, row, 22, row + 2);
                label = new Label(22, row, "Application Id", innerheadcell);
                sheet.setColumnView(22, 27);
                sheet.addCell(label);

                row = row + 1;

                label = new Label(12, row, "Prior to 01.01.2017", innerheadcell);
                sheet.addCell(label);
                sheet.mergeCells(12, row, 13, row);

                label = new Label(14, row, "During last 5 years i.e. w.e.f. 1.1.2016 to 31.12.2020", innerheadcell);
                sheet.addCell(label);
                sheet.mergeCells(14, row, 15, row);

                label = new Label(16, row, "From 1.1.21 till date of submission of Nomination Rolls", innerheadcell);
                sheet.addCell(label);
                sheet.mergeCells(16, row, 17, row);

                row = row + 1;

                label = new Label(12, row, "Major", innerheadcell);
                sheet.addCell(label);
                label = new Label(13, row, "Minor", innerheadcell);
                sheet.addCell(label);
                label = new Label(14, row, "Major", innerheadcell);
                sheet.addCell(label);
                label = new Label(15, row, "Minor", innerheadcell);
                sheet.addCell(label);
                label = new Label(16, row, "Major", innerheadcell);
                sheet.addCell(label);
                label = new Label(17, row, "Minor", innerheadcell);
                sheet.addCell(label);

                row = row + 1;

                label = new Label(0, row, "1", innerheadcell);
                sheet.addCell(label);

                label = new Label(1, row, "2", innerheadcell);
                sheet.addCell(label);

                label = new Label(2, row, "3", innerheadcell);
                sheet.addCell(label);

                label = new Label(3, row, "4", innerheadcell);
                sheet.addCell(label);

                label = new Label(4, row, "5", innerheadcell);
                sheet.addCell(label);

                label = new Label(5, row, "6", innerheadcell);
                sheet.addCell(label);

                label = new Label(6, row, "7", innerheadcell);
                sheet.addCell(label);

                label = new Label(7, row, "8", innerheadcell);
                sheet.addCell(label);

                label = new Label(8, row, "9", innerheadcell);
                sheet.addCell(label);

                label = new Label(9, row, "10", innerheadcell);
                sheet.addCell(label);

                label = new Label(10, row, "11", innerheadcell);
                sheet.addCell(label);

                label = new Label(11, row, "12", innerheadcell);
                sheet.addCell(label);

                label = new Label(12, row, "13", innerheadcell);
                sheet.addCell(label);

                label = new Label(13, row, "14", innerheadcell);
                sheet.addCell(label);

                label = new Label(14, row, "15", innerheadcell);
                sheet.addCell(label);

                label = new Label(15, row, "16", innerheadcell);
                sheet.addCell(label);

                label = new Label(16, row, "17", innerheadcell);
                sheet.addCell(label);

                label = new Label(17, row, "18", innerheadcell);
                sheet.addCell(label);

                label = new Label(18, row, "19", innerheadcell);
                sheet.addCell(label);

                label = new Label(19, row, "20", innerheadcell);
                sheet.addCell(label);

                label = new Label(20, row, "21", innerheadcell);
                sheet.addCell(label);

                label = new Label(21, row, "22", innerheadcell);
                sheet.addCell(label);

                label = new Label(22, row, "23", innerheadcell);
                sheet.addCell(label);

                pst = con.prepareStatement("select nomination_form_id, gradation_number,off_name_abbr, fullname, fathersname, category, to_char(dob, 'DD-Mon-YYYY') dob, "
                        + " to_char(postingunintdoj, 'DD-Mon-YYYY') postingunintdoj , passing_training_date, place_posting, to_char(doegov, 'DD-Mon-YYYY') doegov, "
                        + " to_char(to_date(jodinspector,'YYYY-MM-DD'), 'DD-Mon-YYYY') jodinspector, punishmentmajorprior, punishmentmajorduring, punishmentmajorfrom, \n"
                        + "  punishmentminorprior, punishmentminorduring, punishmentminorfrom, dpcifany, disc_details, physicalFitness, dateofserving, to_char(dor, 'DD-Mon-YYYY') dor, "
                        + " remarksnominationgeneral,recommend_status , off_name_abbr, gradation_slno, pnm.entry_year,orderdate_passing__constable_training,district_recommendation_status,rank_joinin_govservice,jodinspector  from police_nomination_master pnm "
                        + " inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
                        + " inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid "
                        + " inner join police_office_list_data pold on pnm.created_office=pold.police_off_code "
                        + " where is_submitted='Y' and nomination_for_new_rank=? and is_archived='N' and pnm.entry_year=? "
                        + " order by gradation_slno ");
                int i = 0;
                pst.setString(1, nominatedForPost);
                pst.setString(2, fiscalyear);
                rs = pst.executeQuery();
                while (rs.next()) {

                    i++;
                    row = row + 1;
                    label = new Label(0, row, i + "", innerheadcell);
                    sheet.addCell(label);

                    label = new Label(1, row, rs.getInt("gradation_slno") + "", innerheadcell);
                    sheet.addCell(label);

                    label = new Label(2, row, rs.getString("fullname"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(3, row, rs.getString("fathersname"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(4, row, rs.getString("category"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(5, row, rs.getString("place_posting"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(6, row, rs.getString("dob"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(7, row, rs.getString("doegov"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(8, row, rs.getString("rank_joinin_govservice"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(9, row, CommonFunctions.getFormattedOutputDate1(rs.getDate("orderdate_passing__constable_training")), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(10, row, rs.getString("jodinspector"), innerheadcell);
                    sheet.addCell(label);

                    if (rs.getString("dpcifany").equals("Y")) {
                        label = new Label(11, row, rs.getString("disc_details"), innerheadcell);
                        sheet.addCell(label);
                    } else {
                        label = new Label(11, row, "NO", innerheadcell);
                        sheet.addCell(label);
                    }

                    label = new Label(12, row, rs.getString("punishmentmajorprior"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(13, row, rs.getString("punishmentminorprior"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(14, row, rs.getString("punishmentmajorduring"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(15, row, rs.getString("punishmentminorduring"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(16, row, rs.getString("punishmentmajorfrom"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(17, row, rs.getString("punishmentminorfrom"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(18, row, rs.getString("physicalFitness"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(19, row, rs.getString("district_recommendation_status"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(20, row, rs.getString("recommend_status"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(21, row, rs.getString("off_name_abbr"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(22, row, rs.getString("nomination_form_id"), innerheadcell);
                    sheet.addCell(label);

                }
            } else if (nominatedForPost.equals("141191")) {
                Label label = new Label(0, row, " Nomination for the post of ASI(ARMED)/HAVILDAR MAJOR OF POLICE to SUB INSPECTOR OF POLICE(ARMED) ", headcell);//column,row
                sheet.setRowView(row, 30 * 30);
                sheet.addCell(label);
                sheet.mergeCells(0, row, 19, row);

                row = row + 1;

                sheet.mergeCells(0, row, 0, row + 2);
                label = new Label(0, row, "SL No", innerheadcell);
                sheet.setRowView(row, 40 * 30);
                sheet.addCell(label);

                sheet.mergeCells(1, row, 1, row + 2);
                label = new Label(1, row, "Gradation Sl No", innerheadcell);
                sheet.setColumnView(1, 20);
                sheet.addCell(label);

                sheet.mergeCells(2, row, 2, row + 2);
                label = new Label(2, row, "Name ", innerheadcell);
                sheet.setColumnView(2, 50);
                sheet.addCell(label);

                sheet.mergeCells(3, row, 3, row + 2);
                label = new Label(3, row, "Father's Name", innerheadcell);
                sheet.setColumnView(3, 50);
                sheet.addCell(label);

                sheet.mergeCells(4, row, 4, row + 2);
                label = new Label(4, row, "Category", innerheadcell);
                sheet.setColumnView(4, 20);
                sheet.addCell(label);

                sheet.mergeCells(5, row, 5, row + 2);
                label = new Label(5, row, "Present place of Posting", innerheadcell);
                sheet.setColumnView(5, 20);
                sheet.addCell(label);

                sheet.mergeCells(6, row, 6, row + 2);
                label = new Label(6, row, "Date of Birth", innerheadcell);
                sheet.setColumnView(6, 20);
                sheet.addCell(label);

                sheet.mergeCells(7, row, 7, row + 2);
                label = new Label(7, row, "Date of joining (in Present Rank))", innerheadcell);
                sheet.setColumnView(7, 20);
                sheet.addCell(label);

                sheet.mergeCells(8, row, 8, row + 2);
                label = new Label(8, row, "Departmental Proceedings/Vigilance/HRPC/Criminal Case pending, if any. ", innerheadcell);
                sheet.setColumnView(8, 20);
                sheet.addCell(label);

                label = new Label(9, row, "Punishment", innerheadcell);
                sheet.setColumnView(9, 20);
                sheet.addCell(label);
                sheet.mergeCells(9, row, 14, row);

                sheet.mergeCells(16, row, 16, row + 2);
                label = new Label(16, row, "Remarks of Nominating Authority", innerheadcell);
                sheet.setColumnView(16, 20);
                sheet.addCell(label);

                sheet.mergeCells(17, row, 17, row + 2);
                label = new Label(17, row, "Remarks of Recommended Authority", innerheadcell);
                sheet.setColumnView(17, 20);
                sheet.addCell(label);

                sheet.mergeCells(18, row, 18, row + 2);
                label = new Label(18, row, "Nomination Received From District/Establishment", innerheadcell);
                sheet.setColumnView(18, 20);
                sheet.addCell(label);

                sheet.mergeCells(19, row, 19, row + 2);
                label = new Label(19, row, "Date of enlistment in Police Service", innerheadcell);
                sheet.setColumnView(19, 20);
                sheet.addCell(label);

                sheet.mergeCells(20, row, 20, row + 2);
                label = new Label(20, row, "Application Id", innerheadcell);
                sheet.setColumnView(20, 21);
                sheet.addCell(label);

                row = row + 1;

                label = new Label(9, row, "Prior to 01.01.2017", innerheadcell);
                sheet.addCell(label);
                sheet.mergeCells(9, row, 10, row);

                label = new Label(11, row, "During last 5 years i.e. w.e.f. 1.1.2017 to 31.12.2021", innerheadcell);
                sheet.addCell(label);
                sheet.mergeCells(11, row, 12, row);

                label = new Label(13, row, "From 1.1.22 till date of submission of Nomination Rolls", innerheadcell);
                sheet.addCell(label);
                sheet.mergeCells(13, row, 14, row);

                row = row + 1;

                label = new Label(9, row, "Major", innerheadcell);
                sheet.addCell(label);
                label = new Label(10, row, "Minor", innerheadcell);
                sheet.addCell(label);
                label = new Label(11, row, "Major", innerheadcell);
                sheet.addCell(label);
                label = new Label(12, row, "Minor", innerheadcell);
                sheet.addCell(label);
                label = new Label(13, row, "Major", innerheadcell);
                sheet.addCell(label);
                label = new Label(14, row, "Minor", innerheadcell);
                sheet.addCell(label);

                row = row + 1;

                label = new Label(0, row, "1", innerheadcell);
                sheet.addCell(label);

                label = new Label(1, row, "2", innerheadcell);
                sheet.addCell(label);

                label = new Label(2, row, "3", innerheadcell);
                sheet.addCell(label);

                label = new Label(3, row, "4", innerheadcell);
                sheet.addCell(label);

                label = new Label(4, row, "5", innerheadcell);
                sheet.addCell(label);

                label = new Label(5, row, "6", innerheadcell);
                sheet.addCell(label);

                label = new Label(6, row, "7", innerheadcell);
                sheet.addCell(label);

                label = new Label(7, row, "8", innerheadcell);
                sheet.addCell(label);

                label = new Label(8, row, "9", innerheadcell);
                sheet.addCell(label);

                label = new Label(9, row, "10", innerheadcell);
                sheet.addCell(label);

                label = new Label(10, row, "11", innerheadcell);
                sheet.addCell(label);

                label = new Label(11, row, "12", innerheadcell);
                sheet.addCell(label);

                label = new Label(12, row, "13", innerheadcell);
                sheet.addCell(label);

                label = new Label(13, row, "14", innerheadcell);
                sheet.addCell(label);

                label = new Label(14, row, "15", innerheadcell);
                sheet.addCell(label);

                label = new Label(15, row, "16", innerheadcell);
                sheet.addCell(label);

                label = new Label(16, row, "17", innerheadcell);
                sheet.addCell(label);

                label = new Label(17, row, "18", innerheadcell);
                sheet.addCell(label);

                label = new Label(18, row, "19", innerheadcell);
                sheet.addCell(label);

                label = new Label(19, row, "20", innerheadcell);
                sheet.addCell(label);

                label = new Label(20, row, "21", innerheadcell);
                sheet.addCell(label);

                pst = con.prepareStatement("select nomination_form_id, gradation_number,off_name_abbr, fullname, fathersname, category, to_char(dob, 'DD-Mon-YYYY') dob, "
                        + " to_char(postingunintdoj, 'DD-Mon-YYYY') postingunintdoj , passing_training_date, place_posting, to_char(doegov, 'DD-Mon-YYYY') doegov, "
                        + " to_char(to_date(jodinspector,'YYYY-MM-DD'), 'DD-Mon-YYYY') jodinspector, punishmentmajorprior, punishmentmajorduring, punishmentmajorfrom, \n"
                        + "  punishmentminorprior, punishmentminorduring, punishmentminorfrom, dpcifany, disc_details, remarks_of_cdmo, dateofserving, to_char(dor, 'DD-Mon-YYYY') dor, "
                        + " remarksnominationgeneral,recommend_status , off_name_abbr, gradation_slno, pnm.entry_year from police_nomination_master pnm "
                        + " inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
                        + " inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid "
                        + " inner join police_office_list_data pold on pnm.created_office=pold.police_off_code "
                        + " where is_submitted='Y' and nomination_for_new_rank=? and is_archived='N' and cadre_code=? and pnm.entry_year=? "
                        + " order by gradation_slno ");
                int i = 0;
                pst.setString(1, nominatedForPost);
                pst.setString(2, cadreCode);
                pst.setString(3, fiscalyear);
                rs = pst.executeQuery();
                while (rs.next()) {

                    i++;
                    row = row + 1;
                    label = new Label(0, row, i + "", innerheadcell);
                    sheet.addCell(label);

                    label = new Label(1, row, rs.getInt("gradation_slno") + "", innerheadcell);
                    sheet.addCell(label);

                    label = new Label(2, row, rs.getString("fullname"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(3, row, rs.getString("fathersname"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(4, row, rs.getString("category"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(5, row, rs.getString("place_posting"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(6, row, rs.getString("dob"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(7, row, rs.getString("postingunintdoj"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(8, row, rs.getString("disc_details"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(9, row, rs.getString("punishmentmajorprior"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(10, row, rs.getString("punishmentminorprior"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(11, row, rs.getString("punishmentmajorduring"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(12, row, rs.getString("punishmentminorduring"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(13, row, rs.getString("punishmentmajorfrom"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(14, row, rs.getString("punishmentminorfrom"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(15, row, rs.getString("remarks_of_cdmo"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(16, row, rs.getString("remarksnominationgeneral"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(17, row, rs.getString("recommend_status"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(18, row, rs.getString("off_name_abbr"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(19, row, rs.getString("doegov"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(20, row, rs.getString("nomination_form_id"), innerheadcell);
                    sheet.addCell(label);

                }

            } else if (nominatedForPost.equals("140871")) {
                Label label = new Label(0, row, " Nomination for the post of SUB INSPECTOR OF POLICE(ARMED) to INSPECTOR OF POLICE(ARMED) ", headcell);//column,row
                sheet.setRowView(row, 30 * 30);
                sheet.addCell(label);
                sheet.mergeCells(0, row, 19, row);

                row = row + 1;

                sheet.mergeCells(0, row, 0, row + 2);
                label = new Label(0, row, "SL No", innerheadcell);
                sheet.setRowView(row, 40 * 30);
                sheet.addCell(label);

                sheet.mergeCells(1, row, 1, row + 2);
                label = new Label(1, row, "Gradation Sl No", innerheadcell);
                sheet.setColumnView(1, 20);
                sheet.addCell(label);

                sheet.mergeCells(2, row, 2, row + 2);
                label = new Label(2, row, "Name ", innerheadcell);
                sheet.setColumnView(2, 50);
                sheet.addCell(label);

                sheet.mergeCells(3, row, 3, row + 2);
                label = new Label(3, row, "Father's Name", innerheadcell);
                sheet.setColumnView(3, 50);
                sheet.addCell(label);

                sheet.mergeCells(4, row, 4, row + 2);
                label = new Label(4, row, "Category", innerheadcell);
                sheet.setColumnView(4, 20);
                sheet.addCell(label);

                sheet.mergeCells(5, row, 5, row + 2);
                label = new Label(5, row, "Present place of Posting", innerheadcell);
                sheet.setColumnView(5, 20);
                sheet.addCell(label);

                sheet.mergeCells(6, row, 6, row + 2);
                label = new Label(6, row, "Date of Birth", innerheadcell);
                sheet.setColumnView(6, 20);
                sheet.addCell(label);

                sheet.mergeCells(7, row, 7, row + 2);
                label = new Label(7, row, "Date of joining (in Present Rank)", innerheadcell);
                sheet.setColumnView(7, 20);
                sheet.addCell(label);

                sheet.mergeCells(8, row, 8, row + 2);
                label = new Label(8, row, "Departmental Proceedings/Vigilance/HRPC/Criminal Case pending, if any. ", innerheadcell);
                sheet.setColumnView(8, 20);
                sheet.addCell(label);

                label = new Label(9, row, "Punishment", innerheadcell);
                sheet.setColumnView(9, 20);
                sheet.addCell(label);
                sheet.mergeCells(9, row, 14, row);

                sheet.mergeCells(16, row, 16, row + 2);
                label = new Label(16, row, "Remarks of Nominating Authority", innerheadcell);
                sheet.setColumnView(16, 20);
                sheet.addCell(label);

                sheet.mergeCells(17, row, 17, row + 2);
                label = new Label(17, row, "Recommendation Status", innerheadcell);
                sheet.setColumnView(17, 20);
                sheet.addCell(label);

                sheet.mergeCells(18, row, 18, row + 2);
                label = new Label(18, row, "Nomination Received From District/Establishment", innerheadcell);
                sheet.setColumnView(18, 20);
                sheet.addCell(label);

                sheet.mergeCells(19, row, 19, row + 2);
                label = new Label(19, row, "Application Id", innerheadcell);
                sheet.setColumnView(19, 20);
                sheet.addCell(label);

                row = row + 1;

                label = new Label(9, row, "Prior to 01.01.2017", innerheadcell);
                sheet.addCell(label);
                sheet.mergeCells(9, row, 10, row);

                label = new Label(11, row, "During last 5 years i.e. w.e.f. 1.1.2017 to 31.12.2021", innerheadcell);
                sheet.addCell(label);
                sheet.mergeCells(11, row, 12, row);

                label = new Label(13, row, "From 1.1.22 till date of submission of Nomination Rolls", innerheadcell);
                sheet.addCell(label);
                sheet.mergeCells(13, row, 14, row);

                row = row + 1;

                label = new Label(9, row, "Major", innerheadcell);
                sheet.addCell(label);
                label = new Label(10, row, "Minor", innerheadcell);
                sheet.addCell(label);
                label = new Label(11, row, "Major", innerheadcell);
                sheet.addCell(label);
                label = new Label(12, row, "Minor", innerheadcell);
                sheet.addCell(label);
                label = new Label(13, row, "Major", innerheadcell);
                sheet.addCell(label);
                label = new Label(14, row, "Minor", innerheadcell);
                sheet.addCell(label);

                row = row + 1;

                label = new Label(0, row, "1", innerheadcell);
                sheet.addCell(label);

                label = new Label(1, row, "2", innerheadcell);
                sheet.addCell(label);

                label = new Label(2, row, "3", innerheadcell);
                sheet.addCell(label);

                label = new Label(3, row, "4", innerheadcell);
                sheet.addCell(label);

                label = new Label(4, row, "5", innerheadcell);
                sheet.addCell(label);

                label = new Label(5, row, "6", innerheadcell);
                sheet.addCell(label);

                label = new Label(6, row, "7", innerheadcell);
                sheet.addCell(label);

                label = new Label(7, row, "8", innerheadcell);
                sheet.addCell(label);

                label = new Label(8, row, "9", innerheadcell);
                sheet.addCell(label);

                label = new Label(9, row, "10", innerheadcell);
                sheet.addCell(label);

                label = new Label(10, row, "11", innerheadcell);
                sheet.addCell(label);

                label = new Label(11, row, "12", innerheadcell);
                sheet.addCell(label);

                label = new Label(12, row, "13", innerheadcell);
                sheet.addCell(label);

                label = new Label(13, row, "14", innerheadcell);
                sheet.addCell(label);

                label = new Label(14, row, "15", innerheadcell);
                sheet.addCell(label);

                label = new Label(15, row, "16", innerheadcell);
                sheet.addCell(label);

                label = new Label(16, row, "17", innerheadcell);
                sheet.addCell(label);

                label = new Label(17, row, "18", innerheadcell);
                sheet.addCell(label);

                label = new Label(18, row, "19", innerheadcell);
                sheet.addCell(label);

                label = new Label(19, row, "20", innerheadcell);
                sheet.addCell(label);

                pst = con.prepareStatement("select nomination_form_id, gradation_number,off_name_abbr, fullname, fathersname, category, to_char(dob, 'DD-Mon-YYYY') dob, "
                        + " to_char(postingunintdoj, 'DD-Mon-YYYY') postingunintdoj , passing_training_date, place_posting, to_char(doegov, 'DD-Mon-YYYY') doegov, "
                        + " to_char(to_date(jodinspector,'YYYY-MM-DD'), 'DD-Mon-YYYY') jodinspector, punishmentmajorprior, punishmentmajorduring, punishmentmajorfrom, "
                        + "  punishmentminorprior, punishmentminorduring, punishmentminorfrom, dpcifany, disc_details, remarks_of_cdmo, dateofserving, to_char(dor, 'DD-Mon-YYYY') dor, "
                        + " remarksnominationgeneral,recommend_status , off_name_abbr, gradation_slno, pnm.entry_year  from police_nomination_master pnm "
                        + " inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
                        + " inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid "
                        + " inner join police_office_list_data pold on pnm.created_office=pold.police_off_code "
                        + " where is_submitted='Y' and nomination_for_new_rank=? and is_archived='N' and cadre_code=? and pnm.entry_year=?  "
                        + " order by gradation_slno ");
                int i = 0;
                pst.setString(1, nominatedForPost);
                pst.setString(2, cadreCode);
                pst.setString(3, fiscalyear);
                rs = pst.executeQuery();
                while (rs.next()) {

                    i++;
                    row = row + 1;
                    label = new Label(0, row, i + "", innerheadcell);
                    sheet.addCell(label);

                    label = new Label(1, row, rs.getInt("gradation_slno") + "", innerheadcell);
                    sheet.addCell(label);

                    label = new Label(2, row, rs.getString("fullname"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(3, row, rs.getString("fathersname"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(4, row, rs.getString("category"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(5, row, rs.getString("place_posting"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(6, row, rs.getString("dob"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(7, row, rs.getString("jodinspector"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(8, row, rs.getString("disc_details"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(9, row, rs.getString("punishmentmajorprior"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(10, row, rs.getString("punishmentminorprior"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(11, row, rs.getString("punishmentmajorduring"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(12, row, rs.getString("punishmentminorduring"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(13, row, rs.getString("punishmentmajorfrom"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(14, row, rs.getString("punishmentminorfrom"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(15, row, rs.getString("remarks_of_cdmo"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(16, row, rs.getString("remarksnominationgeneral"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(17, row, rs.getString("recommend_status"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(18, row, rs.getString("off_name_abbr"), innerheadcell);
                    sheet.addCell(label);

                    label = new Label(19, row, rs.getString("nomination_form_id"), innerheadcell);
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

    public String getDpListForASI(int nominationFormId) {
        StringBuilder dpDesc = new StringBuilder();
        Connection con = null;

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = this.repodataSource.getConnection();
            String sql1 = "SELECT proceeding_detail FROM police_nomination_form_different_proceeding where nomination_form_id=? order by proceeding_detail_id";
            ps = con.prepareStatement(sql1);
            ps.setInt(1, nominationFormId);
            rs = ps.executeQuery();
            while (rs.next()) {
                dpDesc.append(rs.getString("proceeding_detail") + ", ");
            }

        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps, con);
        }
        if (dpDesc.length() > 0) {
            return dpDesc.substring(0, dpDesc.length() - 2);
        } else {
            return dpDesc.toString();
        }

    }

    public void downloadNominationCheckListForInspectorArmed2AssistantCommandant(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        int row = 0;
        try {
            con = this.repodataSource.getConnection();

            WritableSheet sheet = workbook.createSheet("Check List", 0);

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

            NominationForm nfm = new NominationForm();
            Label label = new Label(0, row, " SERVICE PARTICUALRS OF Inspector (ARMED) FOR PROMOTION TO THE RANK OF ASSISTANT COMMANDANT ", headcell);//column,row
            sheet.setRowView(row, 30 * 30);
            sheet.addCell(label);
            sheet.mergeCells(0, row, 27, row);

            row = row + 1;

            sheet.mergeCells(0, row, 0, row + 2);
            label = new Label(0, row, "SL No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(1, row, 1, row + 2);
            label = new Label(1, row, "Gradation Sl No", innerheadcell);
            sheet.setColumnView(1, 34);
            sheet.addCell(label);

            sheet.mergeCells(2, row, 2, row + 2);
            label = new Label(2, row, "Hrms Id", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(3, row, 3, row + 2);
            label = new Label(3, row, "Gpf No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(4, row, 4, row + 2);
            label = new Label(4, row, "Name ", innerheadcell);
            sheet.setColumnView(4, 50);
            sheet.addCell(label);

            sheet.mergeCells(5, row, 5, row + 2);
            label = new Label(5, row, "Father's Name", innerheadcell);
            sheet.setColumnView(5, 50);
            sheet.addCell(label);

            sheet.mergeCells(6, row, 6, row + 2);
            label = new Label(6, row, "Category", innerheadcell);
            sheet.setColumnView(6, 34);
            sheet.addCell(label);

            sheet.mergeCells(7, row, 7, row + 2);
            label = new Label(7, row, "Date of Birth", innerheadcell);
            sheet.setColumnView(7, 34);
            sheet.addCell(label);

            sheet.mergeCells(8, row, 8, row + 2);
            label = new Label(8, row, "Date of entry into Govt. Service", innerheadcell);
            sheet.setColumnView(8, 34);
            sheet.addCell(label);

            sheet.mergeCells(9, row, 9, row + 2);
            label = new Label(9, row, "Date of appointent in the Present Grade", innerheadcell);
            sheet.setColumnView(9, 34);
            sheet.addCell(label);

            sheet.mergeCells(10, row, 10, row + 2);
            label = new Label(10, row, "Status of disciplinary/Proceeding/Criminal/Vigilance proceeding if any. ", innerheadcell);
            sheet.setColumnView(10, 34);
            sheet.addCell(label);

            sheet.mergeCells(11, row, 11, row + 2);
            label = new Label(11, row, "disciplinary/Proceeding/Criminal/Vigilance proceeding Details. ", innerheadcell);
            sheet.setColumnView(11, 34);
            sheet.addCell(label);

            label = new Label(12, row, "Punishment", innerheadcell);
            sheet.setColumnView(12, 34);
            sheet.addCell(label);
            sheet.mergeCells(12, row, 17, row);

            sheet.mergeCells(18, row, 18, row + 2);
            label = new Label(18, row, "Punishment Detail", innerheadcell);
            sheet.setColumnView(18, 34);
            sheet.addCell(label);

            sheet.mergeCells(19, row, 19, row + 2);
            label = new Label(19, row, "Status of charge has been served on the nominee in Departmental Proceeding. ", innerheadcell);
            sheet.setColumnView(19, 34);
            sheet.addCell(label);

            sheet.mergeCells(20, row, 20, row + 2);
            label = new Label(20, row, "Serving Charge Details. ", innerheadcell);
            sheet.setColumnView(20, 34);
            sheet.addCell(label);

            sheet.mergeCells(21, row, 21, row + 2);
            label = new Label(21, row, "Status of  the Criminal Case if pending. ", innerheadcell);
            sheet.setColumnView(21, 34);
            sheet.addCell(label);

            sheet.mergeCells(22, row, 22, row + 2);
            label = new Label(22, row, "Criminal Case Details. ", innerheadcell);
            sheet.setColumnView(22, 34);
            sheet.addCell(label);

            sheet.mergeCells(23, row, 23, row + 2);
            label = new Label(23, row, "Whether filling uptodate property statement By HRMS", innerheadcell);
            sheet.setColumnView(23, 34);
            sheet.addCell(label);

            sheet.mergeCells(24, row, 24, row + 2);
            label = new Label(24, row, "Date Of filling uptodate property statement By HRMS", innerheadcell);
            sheet.setColumnView(24, 34);
            sheet.addCell(label);

            sheet.mergeCells(25, row, 25, row + 2);
            label = new Label(25, row, "Whether filling uptodate property statement By Officer", innerheadcell);
            sheet.setColumnView(25, 34);
            sheet.addCell(label);

            sheet.mergeCells(26, row, 26, row + 2);
            label = new Label(26, row, "Whether passed departmental examination", innerheadcell);
            sheet.setColumnView(26, 34);
            sheet.addCell(label);

            sheet.mergeCells(27, row, 27, row + 2);
            label = new Label(27, row, "Order Number and Date For departmental examination", innerheadcell);
            sheet.setColumnView(27, 34);
            sheet.addCell(label);

            sheet.mergeCells(28, row, 28, row + 2);
            label = new Label(28, row, "Recommendation Status of Nominating Authority", innerheadcell);
            sheet.setColumnView(28, 34);
            sheet.addCell(label);

            sheet.mergeCells(29, row, 29, row + 2);
            label = new Label(29, row, "Remarks of Nominating Authority", innerheadcell);
            sheet.setColumnView(29, 34);
            sheet.addCell(label);

            sheet.mergeCells(30, row, 30, row + 2);
            label = new Label(30, row, "ReCommendation Status of Recommended Authority", innerheadcell);
            sheet.setColumnView(30, 34);
            sheet.addCell(label);

            sheet.mergeCells(31, row, 31, row + 2);
            label = new Label(31, row, "Remarks of Recommended Authority", innerheadcell);
            sheet.setColumnView(31, 34);
            sheet.addCell(label);

            sheet.mergeCells(32, row, 32, row + 2);
            label = new Label(32, row, "Nomination Received From District/Establishment", innerheadcell);
            sheet.setColumnView(32, 34);
            sheet.addCell(label);

            sheet.mergeCells(33, row, 33, row + 2);
            label = new Label(33, row, "Application Id", innerheadcell);
            sheet.setColumnView(33, 34);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(12, row, "Prior to 01.01.2018", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(12, row, 13, row);

            label = new Label(14, row, "During last 5 years i.e. w.e.f. 1.1.2018 to 31.12.2022", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(14, row, 15, row);

            label = new Label(16, row, "From 1.1.23 till date of submission of Nomination Rolls", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(16, row, 17, row);

            row = row + 1;

            label = new Label(12, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(13, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(14, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(15, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(16, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(17, row, "Minor", innerheadcell);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(0, row, "1", innerheadcell);
            sheet.addCell(label);

            label = new Label(1, row, "2", innerheadcell);
            sheet.addCell(label);

            label = new Label(2, row, "3", innerheadcell);
            sheet.addCell(label);

            label = new Label(3, row, "4", innerheadcell);
            sheet.addCell(label);

            label = new Label(4, row, "5", innerheadcell);
            sheet.addCell(label);

            label = new Label(5, row, "6", innerheadcell);
            sheet.addCell(label);

            label = new Label(6, row, "7", innerheadcell);
            sheet.addCell(label);

            label = new Label(7, row, "8", innerheadcell);
            sheet.addCell(label);

            label = new Label(8, row, "9", innerheadcell);
            sheet.addCell(label);

            label = new Label(9, row, "10", innerheadcell);
            sheet.addCell(label);

            label = new Label(10, row, "11", innerheadcell);
            sheet.addCell(label);

            label = new Label(11, row, "12", innerheadcell);
            sheet.addCell(label);

            label = new Label(12, row, "13", innerheadcell);
            sheet.addCell(label);

            label = new Label(13, row, "14", innerheadcell);
            sheet.addCell(label);

            label = new Label(14, row, "15", innerheadcell);
            sheet.addCell(label);

            label = new Label(15, row, "16", innerheadcell);
            sheet.addCell(label);

            label = new Label(16, row, "17", innerheadcell);
            sheet.addCell(label);

            label = new Label(17, row, "18", innerheadcell);
            sheet.addCell(label);

            label = new Label(18, row, "19", innerheadcell);
            sheet.addCell(label);

            label = new Label(19, row, "20", innerheadcell);
            sheet.addCell(label);

            label = new Label(20, row, "21", innerheadcell);
            sheet.addCell(label);

            label = new Label(21, row, "22", innerheadcell);
            sheet.addCell(label);

            label = new Label(22, row, "23", innerheadcell);
            sheet.addCell(label);

            label = new Label(23, row, "24", innerheadcell);
            sheet.addCell(label);

            label = new Label(24, row, "25", innerheadcell);
            sheet.addCell(label);

            label = new Label(25, row, "26", innerheadcell);
            sheet.addCell(label);

            label = new Label(26, row, "27", innerheadcell);
            sheet.addCell(label);

            label = new Label(27, row, "28", innerheadcell);
            sheet.addCell(label);

            label = new Label(28, row, "29", innerheadcell);
            sheet.addCell(label);

            label = new Label(29, row, "30", innerheadcell);
            sheet.addCell(label);

            label = new Label(30, row, "31", innerheadcell);
            sheet.addCell(label);

            label = new Label(31, row, "32", innerheadcell);
            sheet.addCell(label);

            label = new Label(32, row, "33", innerheadcell);
            sheet.addCell(label);

            label = new Label(33, row, "34", innerheadcell);
            sheet.addCell(label);

            pst = con.prepareStatement("select property_submitted_if_any,grade_serial_no,dpcifany,proceeding_detail,pnf.nomination_form_id, gradation_number,em.emp_id,pnd.gpf_no,off_name_abbr, fullname, fathersname, em.category, to_char(em.dob, 'DD-Mon-YYYY') dob, "
                    + "to_char(postingunintdoj, 'DD-Mon-YYYY') postingunintdoj , passing_training_date, place_posting, to_char(doegov, 'DD-Mon-YYYY') doegov, "
                    + "to_char(to_date(jodinspector,'YYYY-MM-DD'), 'DD-Mon-YYYY') jodinspector, punishmentmajorprior, punishmentmajorduring, punishmentmajorfrom, "
                    + " punishmentminorprior, punishmentminorduring, punishmentminorfrom, dpcifany, disc_details, remarks_of_cdmo, dateofserving, to_char(dor, 'DD-Mon-YYYY') dor, "
                    + "remarksnominationgeneral,recommend_status , off_name_abbr, gradation_slno, pnm.entry_year,property_submitted_on_hrms,property_submitted_on_hrms_byofficer,"
                    + "district_recommendation_status,district_recommendation_view,remarks_recommend_authority,deptexampassifany,office_order_no,passing_training_date,punishment_detail,dateofserving_if_any_dp,servingchargedetail,criminal_case_present_status_ifany,criminal_status_detail   "
                    + "from police_nomination_master pnm "
                    + "inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
                    + "inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid "
                    + "inner join police_office_list_data pold on pnm.created_office=pold.police_off_code "
                    + "inner join emp_mast em on pnd.gpf_no=em.gpf_no "
                    + "left outer join police_nomination_form_different_proceeding on pnf.nomination_form_id = police_nomination_form_different_proceeding.nomination_form_id "
                    + "where is_submitted='Y' and nomination_for_new_rank=? and is_archived='N' and pnm.entry_year=? order by grade_serial_no asc");
            int i = 0;
            pst.setString(1, nominatedForPost);
            pst.setString(2, fiscalyear);
            rs = pst.executeQuery();
            while (rs.next()) {

                i++;
                row = row + 1;
                label = new Label(0, row, i + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(1, row, rs.getInt("grade_serial_no") + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(2, row, rs.getString("emp_id"), innerheadcell);
                sheet.addCell(label);

                label = new Label(3, row, rs.getString("gpf_no"), innerheadcell);
                sheet.addCell(label);

                label = new Label(4, row, rs.getString("fullname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(5, row, rs.getString("fathersname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(6, row, rs.getString("category"), innerheadcell);
                sheet.addCell(label);

                label = new Label(7, row, rs.getString("dob"), innerheadcell);
                sheet.addCell(label);

                label = new Label(8, row, rs.getString("doegov"), innerheadcell);
                sheet.addCell(label);

                label = new Label(9, row, rs.getString("jodinspector"), innerheadcell);
                sheet.addCell(label);

                if (rs.getString("dpcifany").equals("Yes")) {
                    label = new Label(10, row, "Yes", innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(10, row, "No", innerheadcell);
                    sheet.addCell(label);
                }

                label = new Label(11, row, rs.getString("disc_details"), innerheadcell);
                sheet.addCell(label);

                label = new Label(12, row, rs.getString("punishmentmajorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(13, row, rs.getString("punishmentminorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(14, row, rs.getString("punishmentmajorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(15, row, rs.getString("punishmentminorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(16, row, rs.getString("punishmentmajorfrom"), innerheadcell);
                sheet.addCell(label);

                label = new Label(17, row, rs.getString("punishmentminorfrom"), innerheadcell);
                sheet.addCell(label);

                label = new Label(18, row, rs.getString("punishment_detail"), innerheadcell);
                sheet.addCell(label);

                if (rs.getString("dateofserving_if_any_dp").equals("Y")) {
                    label = new Label(19, row, "Yes", innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(19, row, "No", innerheadcell);
                    sheet.addCell(label);
                }

                label = new Label(20, row, rs.getString("servingchargedetail"), innerheadcell);
                sheet.addCell(label);

                if (rs.getString("criminal_case_present_status_ifany").equals("Y")) {
                    label = new Label(21, row, "Yes", innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(21, row, "No", innerheadcell);
                    sheet.addCell(label);
                }

                label = new Label(22, row, rs.getString("criminal_status_detail"), innerheadcell);
                sheet.addCell(label);

                label = new Label(23, row, rs.getString("property_submitted_if_any"), innerheadcell);
                sheet.addCell(label);

                label = new Label(24, row, rs.getString("property_submitted_on_hrms"), innerheadcell);
                sheet.addCell(label);

                label = new Label(25, row, rs.getString("property_submitted_on_hrms_byofficer"), innerheadcell);
                sheet.addCell(label);

                if (rs.getString("deptexampassifany").equals("Y")) {
                    label = new Label(26, row, "Yes", innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(26, row, "No", innerheadcell);
                    sheet.addCell(label);
                }

                if (rs.getString("deptexampassifany").equals("Y")) {
                    label = new Label(27, row, rs.getString("office_order_no") + "," + rs.getString("passing_training_date"), innerheadcell);
                    sheet.addCell(label);
                } else {
                    label = new Label(27, row, "", innerheadcell);
                    sheet.addCell(label);
                }

                label = new Label(28, row, rs.getString("district_recommendation_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(29, row, rs.getString("district_recommendation_view"), innerheadcell);
                sheet.addCell(label);

                label = new Label(30, row, rs.getString("recommend_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(31, row, rs.getString("remarks_recommend_authority"), innerheadcell);
                sheet.addCell(label);

                label = new Label(32, row, rs.getString("off_name_abbr"), innerheadcell);
                sheet.addCell(label);

                label = new Label(33, row, rs.getString("nomination_form_id"), innerheadcell);
                sheet.addCell(label);

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

    @Override
    public void downloadNominationCheckListForJuniorClerk2SeniorClerk(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        int row = 0;
        try {
            con = this.repodataSource.getConnection();

            WritableSheet sheet = workbook.createSheet("Check List", 0);

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

            NominationForm nfm = new NominationForm();
            Label label = new Label(0, row, " SERVICE PARTICUALRS OF Junior Clerk Of Police For Promotion to Senior Clerk ", headcell);//column,row
            sheet.setRowView(row, 30 * 30);
            sheet.addCell(label);
            sheet.mergeCells(0, row, 27, row);

            row = row + 1;

            sheet.mergeCells(0, row, 0, row + 2);
            label = new Label(0, row, "SL No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(1, row, 1, row + 2);
            label = new Label(1, row, "Gradation Sl No", innerheadcell);
            sheet.setColumnView(1, 22);
            sheet.addCell(label);

            sheet.mergeCells(2, row, 2, row + 2);
            label = new Label(2, row, "Name ", innerheadcell);
            sheet.setColumnView(2, 50);
            sheet.addCell(label);

            sheet.mergeCells(3, row, 3, row + 2);
            label = new Label(3, row, "Father's Name", innerheadcell);
            sheet.setColumnView(3, 50);
            sheet.addCell(label);

            sheet.mergeCells(4, row, 4, row + 2);
            label = new Label(4, row, "Category", innerheadcell);
            sheet.setColumnView(4, 22);
            sheet.addCell(label);

            sheet.mergeCells(5, row, 5, row + 2);
            label = new Label(5, row, "Date of Birth", innerheadcell);
            sheet.setColumnView(5, 22);
            sheet.addCell(label);

            sheet.mergeCells(6, row, 6, row + 2);
            label = new Label(6, row, "Date of entry into Govt. Service", innerheadcell);
            sheet.setColumnView(6, 22);
            sheet.addCell(label);

            sheet.mergeCells(7, row, 7, row + 2);
            label = new Label(7, row, "Date of appointent in the Present Grade", innerheadcell);
            sheet.setColumnView(7, 22);
            sheet.addCell(label);

            sheet.mergeCells(8, row, 8, row + 2);
            label = new Label(8, row, "Status of disciplinary/Proceeding/Criminal/Vigilance proceeding if any. ", innerheadcell);
            sheet.setColumnView(8, 22);
            sheet.addCell(label);

            label = new Label(9, row, "Punishment", innerheadcell);
            sheet.setColumnView(9, 22);
            sheet.addCell(label);
            sheet.mergeCells(9, row, 14, row);

            sheet.mergeCells(15, row, 15, row + 2);
            label = new Label(15, row, "Remarks Of CDMO", innerheadcell);
            sheet.setColumnView(15, 22);
            sheet.addCell(label);

            sheet.mergeCells(16, row, 16, row + 2);
            label = new Label(16, row, "Recommendation Status of Nominating Authority", innerheadcell);
            sheet.setColumnView(16, 22);
            sheet.addCell(label);

            sheet.mergeCells(17, row, 17, row + 2);
            label = new Label(17, row, "Remarks of Nominating Authority", innerheadcell);
            sheet.setColumnView(17, 22);
            sheet.addCell(label);

            sheet.mergeCells(18, row, 18, row + 2);
            label = new Label(18, row, "ReCommendation Status of Recommended Authority", innerheadcell);
            sheet.setColumnView(18, 22);
            sheet.addCell(label);

            sheet.mergeCells(19, row, 19, row + 2);
            label = new Label(19, row, "Remarks of Recommended Authority", innerheadcell);
            sheet.setColumnView(19, 22);
            sheet.addCell(label);

            sheet.mergeCells(20, row, 20, row + 2);
            label = new Label(20, row, "Nomination Received From District/Establishment", innerheadcell);
            sheet.setColumnView(20, 22);
            sheet.addCell(label);

            sheet.mergeCells(21, row, 21, row + 2);
            label = new Label(21, row, "Application Id", innerheadcell);
            sheet.setColumnView(21, 22);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(9, row, "Prior to 01.01.2016", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(9, row, 10, row);

            label = new Label(11, row, "During last 5 years i.e. w.e.f. 1.1.2016 to 31.12.2020", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(11, row, 12, row);

            label = new Label(13, row, "From 1.1.21 till date of submission of Nomination Rolls", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(13, row, 14, row);

            row = row + 1;

            label = new Label(9, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(10, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(11, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(12, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(13, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(14, row, "Minor", innerheadcell);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(0, row, "1", innerheadcell);
            sheet.addCell(label);

            label = new Label(1, row, "2", innerheadcell);
            sheet.addCell(label);

            label = new Label(2, row, "3", innerheadcell);
            sheet.addCell(label);

            label = new Label(3, row, "4", innerheadcell);
            sheet.addCell(label);

            label = new Label(4, row, "5", innerheadcell);
            sheet.addCell(label);

            label = new Label(5, row, "6", innerheadcell);
            sheet.addCell(label);

            label = new Label(6, row, "7", innerheadcell);
            sheet.addCell(label);

            label = new Label(7, row, "8", innerheadcell);
            sheet.addCell(label);

            label = new Label(8, row, "9", innerheadcell);
            sheet.addCell(label);

            label = new Label(9, row, "10", innerheadcell);
            sheet.addCell(label);

            label = new Label(10, row, "11", innerheadcell);
            sheet.addCell(label);

            label = new Label(11, row, "12", innerheadcell);
            sheet.addCell(label);

            label = new Label(12, row, "13", innerheadcell);
            sheet.addCell(label);

            label = new Label(13, row, "14", innerheadcell);
            sheet.addCell(label);

            label = new Label(14, row, "15", innerheadcell);
            sheet.addCell(label);

            label = new Label(15, row, "16", innerheadcell);
            sheet.addCell(label);

            label = new Label(16, row, "17", innerheadcell);
            sheet.addCell(label);

            label = new Label(17, row, "18", innerheadcell);
            sheet.addCell(label);

            label = new Label(18, row, "19", innerheadcell);
            sheet.addCell(label);

            label = new Label(19, row, "20", innerheadcell);
            sheet.addCell(label);

            label = new Label(20, row, "21", innerheadcell);
            sheet.addCell(label);

            label = new Label(21, row, "22", innerheadcell);
            sheet.addCell(label);

            pst = con.prepareStatement("select nomination_form_id, gradation_number,off_name_abbr, fullname, fathersname, category, to_char(dob, 'DD-Mon-YYYY') dob, "
                    + " to_char(postingunintdoj, 'DD-Mon-YYYY') postingunintdoj , passing_training_date, place_posting, to_char(doegov, 'DD-Mon-YYYY') doegov, "
                    + " to_char(to_date(jodinspector,'YYYY-MM-DD'), 'DD-Mon-YYYY') jodinspector, punishmentmajorprior, punishmentmajorduring, punishmentmajorfrom, \n"
                    + "  punishmentminorprior, punishmentminorduring, punishmentminorfrom, dpcifany, disc_details, remarks_of_cdmo, dateofserving, to_char(dor, 'DD-Mon-YYYY') dor, "
                    + " remarksnominationgeneral,recommend_status , off_name_abbr, gradation_slno, pnm.entry_year,district_recommendation_status,district_recommendation_view,remarks_recommend_authority   from police_nomination_master pnm "
                    + " inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
                    + " inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid "
                    + " inner join police_office_list_data pold on pnm.created_office=pold.police_off_code "
                    + " where is_submitted='Y' and nomination_for_new_rank=? and is_archived='N' and pnm.entry_year=? "
                    + " order by gradation_slno ");
            int i = 0;
            pst.setString(1, nominatedForPost);
            pst.setString(2, fiscalyear);
            rs = pst.executeQuery();
            while (rs.next()) {

                i++;
                row = row + 1;
                label = new Label(0, row, i + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(1, row, rs.getInt("gradation_slno") + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(2, row, rs.getString("fullname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(3, row, rs.getString("fathersname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(4, row, rs.getString("category"), innerheadcell);
                sheet.addCell(label);

                label = new Label(5, row, rs.getString("dob"), innerheadcell);
                sheet.addCell(label);

                label = new Label(6, row, rs.getString("doegov"), innerheadcell);
                sheet.addCell(label);

                label = new Label(7, row, rs.getString("jodinspector"), innerheadcell);
                sheet.addCell(label);

                label = new Label(8, row, rs.getString("disc_details"), innerheadcell);
                sheet.addCell(label);

                label = new Label(9, row, rs.getString("punishmentmajorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(10, row, rs.getString("punishmentminorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(11, row, rs.getString("punishmentmajorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(12, row, rs.getString("punishmentminorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(13, row, rs.getString("punishmentmajorfrom"), innerheadcell);
                sheet.addCell(label);

                label = new Label(14, row, rs.getString("punishmentminorfrom"), innerheadcell);
                sheet.addCell(label);

                label = new Label(15, row, rs.getString("remarks_of_cdmo"), innerheadcell);
                sheet.addCell(label);

                label = new Label(16, row, rs.getString("district_recommendation_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(17, row, rs.getString("district_recommendation_view"), innerheadcell);
                sheet.addCell(label);

                label = new Label(18, row, rs.getString("recommend_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(19, row, rs.getString("remarks_recommend_authority"), innerheadcell);
                sheet.addCell(label);

                label = new Label(20, row, rs.getString("off_name_abbr"), innerheadcell);
                sheet.addCell(label);

                label = new Label(21, row, rs.getString("nomination_form_id"), innerheadcell);
                sheet.addCell(label);

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

    @Override
    public void downloadNominationCheckListForGroupD2JuniorClerk(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        int row = 0;
        try {
            con = this.repodataSource.getConnection();

            WritableSheet sheet = workbook.createSheet("Check List", 0);

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

            NominationForm nfm = new NominationForm();
            Label label = new Label(0, row, " SERVICE PARTICUALRS OF GROUP D OF POLICE FOR PROMOTION TO JUNIOR CLERK ", headcell);//column,row
            sheet.setRowView(row, 30 * 30);
            sheet.addCell(label);
            sheet.mergeCells(0, row, 27, row);

            row = row + 1;

            sheet.mergeCells(0, row, 0, row + 2);
            label = new Label(0, row, "SL No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(1, row, 1, row + 2);
            label = new Label(1, row, "Gradation Sl No", innerheadcell);
            sheet.setColumnView(1, 22);
            sheet.addCell(label);

            sheet.mergeCells(2, row, 2, row + 2);
            label = new Label(2, row, "Name ", innerheadcell);
            sheet.setColumnView(2, 50);
            sheet.addCell(label);

            sheet.mergeCells(3, row, 3, row + 2);
            label = new Label(3, row, "Father's Name", innerheadcell);
            sheet.setColumnView(3, 50);
            sheet.addCell(label);

            sheet.mergeCells(4, row, 4, row + 2);
            label = new Label(4, row, "Category", innerheadcell);
            sheet.setColumnView(4, 22);
            sheet.addCell(label);

            sheet.mergeCells(5, row, 5, row + 2);
            label = new Label(5, row, "Date of Birth", innerheadcell);
            sheet.setColumnView(5, 22);
            sheet.addCell(label);

            sheet.mergeCells(6, row, 6, row + 2);
            label = new Label(6, row, "Date of entry into Govt. Service", innerheadcell);
            sheet.setColumnView(6, 22);
            sheet.addCell(label);

            sheet.mergeCells(7, row, 7, row + 2);
            label = new Label(7, row, "Date of appointent in the Present Grade", innerheadcell);
            sheet.setColumnView(7, 22);
            sheet.addCell(label);

            sheet.mergeCells(8, row, 8, row + 2);
            label = new Label(8, row, "Status of disciplinary/Proceeding/Criminal/Vigilance proceeding if any. ", innerheadcell);
            sheet.setColumnView(8, 22);
            sheet.addCell(label);

            label = new Label(9, row, "Punishment", innerheadcell);
            sheet.setColumnView(9, 22);
            sheet.addCell(label);
            sheet.mergeCells(9, row, 14, row);

            sheet.mergeCells(16, row, 16, row + 2);
            label = new Label(16, row, "Recommendation Status Of Nominating Authority", innerheadcell);
            sheet.setColumnView(16, 22);
            sheet.addCell(label);

            sheet.mergeCells(17, row, 17, row + 2);
            label = new Label(17, row, "Remarks of Nominating Authority", innerheadcell);
            sheet.setColumnView(17, 22);
            sheet.addCell(label);

            sheet.mergeCells(18, row, 18, row + 2);
            label = new Label(18, row, "ReCommendation Status of Recommended Authority", innerheadcell);
            sheet.setColumnView(18, 22);
            sheet.addCell(label);

            sheet.mergeCells(19, row, 19, row + 2);
            label = new Label(19, row, "Remarks of Recommended Authority", innerheadcell);
            sheet.setColumnView(19, 22);
            sheet.addCell(label);

            sheet.mergeCells(20, row, 20, row + 2);
            label = new Label(20, row, "Nomination Received From District/Establishment", innerheadcell);
            sheet.setColumnView(20, 22);
            sheet.addCell(label);

            sheet.mergeCells(21, row, 21, row + 2);
            label = new Label(21, row, "Application Id", innerheadcell);
            sheet.setColumnView(21, 22);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(9, row, "Prior to 01.01.2017", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(9, row, 10, row);

            label = new Label(11, row, "During last 5 years i.e. w.e.f. 1.1.2017 to 31.12.2021", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(11, row, 12, row);

            label = new Label(13, row, "From 1.1.22 till date of submission of Nomination Rolls", innerheadcell);
            sheet.addCell(label);
            sheet.mergeCells(13, row, 14, row);

            row = row + 1;

            label = new Label(9, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(10, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(11, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(12, row, "Minor", innerheadcell);
            sheet.addCell(label);
            label = new Label(13, row, "Major", innerheadcell);
            sheet.addCell(label);
            label = new Label(14, row, "Minor", innerheadcell);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(0, row, "1", innerheadcell);
            sheet.addCell(label);

            label = new Label(1, row, "2", innerheadcell);
            sheet.addCell(label);

            label = new Label(2, row, "3", innerheadcell);
            sheet.addCell(label);

            label = new Label(3, row, "4", innerheadcell);
            sheet.addCell(label);

            label = new Label(4, row, "5", innerheadcell);
            sheet.addCell(label);

            label = new Label(5, row, "6", innerheadcell);
            sheet.addCell(label);

            label = new Label(6, row, "7", innerheadcell);
            sheet.addCell(label);

            label = new Label(7, row, "8", innerheadcell);
            sheet.addCell(label);

            label = new Label(8, row, "9", innerheadcell);
            sheet.addCell(label);

            label = new Label(9, row, "10", innerheadcell);
            sheet.addCell(label);

            label = new Label(10, row, "11", innerheadcell);
            sheet.addCell(label);

            label = new Label(11, row, "12", innerheadcell);
            sheet.addCell(label);

            label = new Label(12, row, "13", innerheadcell);
            sheet.addCell(label);

            label = new Label(13, row, "14", innerheadcell);
            sheet.addCell(label);

            label = new Label(14, row, "15", innerheadcell);
            sheet.addCell(label);

            label = new Label(15, row, "16", innerheadcell);
            sheet.addCell(label);

            label = new Label(16, row, "17", innerheadcell);
            sheet.addCell(label);

            label = new Label(17, row, "18", innerheadcell);
            sheet.addCell(label);

            label = new Label(18, row, "19", innerheadcell);
            sheet.addCell(label);

            label = new Label(19, row, "20", innerheadcell);
            sheet.addCell(label);

            label = new Label(20, row, "21", innerheadcell);
            sheet.addCell(label);

            label = new Label(21, row, "22", innerheadcell);
            sheet.addCell(label);

            pst = con.prepareStatement("select nomination_form_id, gradation_number,off_name_abbr, fullname, fathersname, category, to_char(dob, 'DD-Mon-YYYY') dob, "
                    + " to_char(postingunintdoj, 'DD-Mon-YYYY') postingunintdoj , passing_training_date, place_posting, to_char(doegov, 'DD-Mon-YYYY') doegov, "
                    + " to_char(to_date(jodinspector,'YYYY-MM-DD'), 'DD-Mon-YYYY') jodinspector, punishmentmajorprior, punishmentmajorduring, punishmentmajorfrom, \n"
                    + "  punishmentminorprior, punishmentminorduring, punishmentminorfrom, dpcifany, disc_details, remarks_of_cdmo, dateofserving, to_char(dor, 'DD-Mon-YYYY') dor, "
                    + " remarksnominationgeneral,recommend_status , off_name_abbr, gradation_slno, pnm.entry_year,remarks_recommend_authority,district_recommendation_view,district_recommendation_status  from police_nomination_master pnm "
                    + " inner join police_nomination_detail pnd on pnm.nomination_master_id=pnd.nomination_master_id "
                    + " inner join police_nomination_form pnf on pnd.nomination_detail_id=pnf.nominationdetailid "
                    + " inner join police_office_list_data pold on pnm.created_office=pold.police_off_code "
                    + " where is_submitted='Y' and nomination_for_new_rank=? and is_archived='N' and pnm.entry_year=? "
                    + " order by gradation_slno ");
            int i = 0;
            pst.setString(1, nominatedForPost);
            pst.setString(2, fiscalyear);
            rs = pst.executeQuery();
            while (rs.next()) {

                i++;
                row = row + 1;
                label = new Label(0, row, i + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(1, row, rs.getInt("gradation_slno") + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(2, row, rs.getString("fullname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(3, row, rs.getString("fathersname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(4, row, rs.getString("category"), innerheadcell);
                sheet.addCell(label);

                label = new Label(5, row, rs.getString("dob"), innerheadcell);
                sheet.addCell(label);

                label = new Label(6, row, rs.getString("doegov"), innerheadcell);
                sheet.addCell(label);

                label = new Label(7, row, rs.getString("jodinspector"), innerheadcell);
                sheet.addCell(label);

                label = new Label(8, row, rs.getString("disc_details"), innerheadcell);
                sheet.addCell(label);

                label = new Label(9, row, rs.getString("punishmentmajorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(10, row, rs.getString("punishmentminorprior"), innerheadcell);
                sheet.addCell(label);

                label = new Label(11, row, rs.getString("punishmentmajorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(12, row, rs.getString("punishmentminorduring"), innerheadcell);
                sheet.addCell(label);

                label = new Label(13, row, rs.getString("punishmentmajorfrom"), innerheadcell);
                sheet.addCell(label);

                label = new Label(14, row, rs.getString("punishmentminorfrom"), innerheadcell);
                sheet.addCell(label);

                label = new Label(15, row, rs.getString("remarks_of_cdmo"), innerheadcell);
                sheet.addCell(label);

                label = new Label(16, row, rs.getString("district_recommendation_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(17, row, rs.getString("district_recommendation_view"), innerheadcell);
                sheet.addCell(label);

                label = new Label(18, row, rs.getString("recommend_status"), innerheadcell);
                sheet.addCell(label);

                label = new Label(19, row, rs.getString("remarks_recommend_authority"), innerheadcell);
                sheet.addCell(label);

                label = new Label(20, row, rs.getString("off_name_abbr"), innerheadcell);
                sheet.addCell(label);

                label = new Label(21, row, rs.getString("nomination_form_id"), innerheadcell);
                sheet.addCell(label);

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

}

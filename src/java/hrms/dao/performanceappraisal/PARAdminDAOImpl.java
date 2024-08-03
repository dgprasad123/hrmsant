/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.performanceappraisal;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.ParGrade;
import hrms.common.SMSServices;
import hrms.model.cadre.Cadre;
import hrms.model.empinfo.SMSGrievance;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.parmast.AbsenteeBean;
import hrms.model.parmast.AcceptingHelperBean;
import hrms.model.parmast.DepartmentPromotionBean;
import hrms.model.parmast.DepartmentPromotionDetail;
import hrms.model.parmast.DistrictWiseAuthorizationForPolice;
import hrms.model.parmast.FiscalYearWiseParData;
import hrms.model.parmast.PARMessageCommunication;
import hrms.model.parmast.PARSearchResult;
import hrms.model.parmast.PARViewDetailLogBean;
import hrms.model.parmast.ParAchievement;
import hrms.model.parmast.ParAdminProperties;
import hrms.model.parmast.ParAdminSearchCriteria;
import hrms.model.parmast.ParAdverseCommunicationDetail;
import hrms.model.parmast.ParApplyForm;
import hrms.model.parmast.ParAssignPrivilage;
import hrms.model.parmast.ParDeleteDetailBean;
import hrms.model.parmast.ParDetail;
import hrms.model.parmast.ParForceForwardBean;
import hrms.model.parmast.ParNrcAttachment;
import hrms.model.parmast.ParStatus;
import hrms.model.parmast.ParStatusBean;
import hrms.model.parmast.ParUnReviewedDetailBean;
import hrms.model.parmast.Parauthorityhelperbean;
import hrms.model.parmast.PerformanceAppraisalForm;
import hrms.model.parmast.ReportingHelperBean;
import hrms.model.parmast.ReviewingHelperBean;
import hrms.model.parmast.UploadPreviousPAR;
import hrms.model.policemodule.NominationForm;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author DurgaPrasad
 */
public class PARAdminDAOImpl implements PARAdminDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;
    protected DataSource repodataSource;

    private String uploadPath;
    // private static final Logger logger = LogManager.getLogger(PARAdminDAOImpl.class);

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setRepodataSource(DataSource repodataSource) {
        this.repodataSource = repodataSource;
    }

    @Override
    public PARSearchResult getPARList(ParAdminSearchCriteria parAdminSearchCriteria) {//rows,page
        String fiscalYear = parAdminSearchCriteria.getFiscalyear();
        String searchCriteria = parAdminSearchCriteria.getSearchCriteria();
        String searchString = parAdminSearchCriteria.getSearchString();
        int noofrows = parAdminSearchCriteria.getRows();
        int page = parAdminSearchCriteria.getPage();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet res = null;
        ArrayList parList = new ArrayList();
        PARSearchResult parSearchResult = new PARSearchResult();

        try {
            con = dataSource.getConnection();

            int offSet = noofrows * (page - 1);
            int limit = noofrows;
            String parstatus = "";
            if (parAdminSearchCriteria.getSearchParStatus() != null && !parAdminSearchCriteria.getSearchParStatus().equals("")) {
                parstatus = "AND PAR_STATUS='" + parAdminSearchCriteria.getSearchParStatus() + "'";
            }
            /*Specific Financial Year wise*/
            if (searchCriteria == null || searchCriteria.equals("") && !parAdminSearchCriteria.getFiscalyear().equals("all")) {
                pst = con.prepareStatement("SELECT COUNT(*) CNT FROM PAR_MASTER WHERE FISCAL_YEAR=?" + parstatus);
                pst.setString(1, fiscalYear);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_MAST.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,EMP_MAST.CUR_OFF_CODE,GPF_NO,DOB,is_reviewed,is_adversed FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,FISCAL_YEAR,is_reviewed,is_adversed FROM PAR_MASTER WHERE FISCAL_YEAR=? and is_deleted='N' " + parstatus + " )PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name asc LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, fiscalYear);

                /*All Financial Year wise*/
            } else if (searchCriteria == null || searchCriteria.equals("") && parAdminSearchCriteria.getFiscalyear().equals("all")) {
                pst = con.prepareStatement("SELECT COUNT(*) CNT FROM PAR_MASTER WHERE FISCAL_YEAR=? and is_deleted='N'" + parstatus);
                pst.setString(1, fiscalYear);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_MAST.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,EMP_MAST.CUR_OFF_CODE,GPF_NO,DOB,is_reviewed,is_adversed FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,FISCAL_YEAR,is_reviewed,is_adversed FROM PAR_MASTER  " + parstatus + " )PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);
                /*Specific Financial Year with empid wise*/
            } else if (searchCriteria.equals("empid") && !parAdminSearchCriteria.getFiscalyear().equals("all")) {
                pst = con.prepareStatement("SELECT * FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,DECODE(SPC,NULL,CUR_SPC,SPC) PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT PAR_MASTER.FISCAL_YEAR,INITIATEDBY,PARID,EMP_MAST.EMP_ID,CUR_SPC,CADRE_CODE,MOBILE,F_NAME,M_NAME,L_NAME,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT EMP_ID,CUR_SPC,MOBILE,F_NAME,M_NAME,L_NAME,POST_GRP_TYPE,CUR_OFF_CODE,GPF_NO,DOB FROM"
                        + " EMP_MAST) EMP_MAST"
                        + " INNER JOIN (SELECT distinct SPC,INITIATEDBY,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,FISCAL_YEAR,is_reviewed,is_adversed  FROM PAR_MASTER WHERE FISCAL_YEAR=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID"
                        + " LEFT OUTER JOIN PAR_VIEWED_BY_CUSTODIAN ON PAR_MASTER.PARID=PAR_VIEWED_BY_CUSTODIAN.PAR_ID) EMP_MAST"
                        + " LEFT OUTER JOIN G_CADRE"
                        + " ON EMP_MAST.CADRE_CODE=G_CADRE.CADRE_CODE LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC"
                        + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE) EMP_MAST ORDER BY F_NAME) EMP_MAST ) EMP_MAST WHERE EMP_ID=? LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, fiscalYear);
                pst.setString(2, searchString);

                /*All Financial Year with empid wise*/
            } else if (searchCriteria.equals("empid") && parAdminSearchCriteria.getFiscalyear().equals("all")) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE EMP_ID = ?)EMP_MAST "
                        + "LEFT OUTER JOIN (SELECT INITIATEDBY,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,FISCAL_YEAR,is_reviewed,is_adversed FROM PAR_MASTER WHERE is_deleted='N'" + parstatus + " )PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, searchString);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_MAST.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,EMP_MAST.CUR_OFF_CODE,GPF_NO,DOB,is_reviewed,is_adversed FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST WHERE EMP_ID = ?)EMP_MAST "
                        + "LEFT OUTER JOIN (SELECT INITIATEDBY,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,FISCAL_YEAR,is_reviewed,is_adversed FROM PAR_MASTER WHERE is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE ORDER BY FISCAL_YEAR desc");
                pst.setString(1, searchString);

                /*Specific Financial Year with gpfno wise*/
            } else if (searchCriteria.equals("gpfno") && !parAdminSearchCriteria.getFiscalyear().equals("all")) {
                pst = con.prepareStatement("SELECT * FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,DECODE(SPC,NULL,CUR_SPC,SPC) PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT PAR_MASTER.FISCAL_YEAR,INITIATEDBY,PARID,EMP_MAST.EMP_ID,CUR_SPC,CADRE_CODE,MOBILE,F_NAME,M_NAME,L_NAME,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT EMP_ID,CUR_SPC,MOBILE,F_NAME,M_NAME,L_NAME,POST_GRP_TYPE,CUR_OFF_CODE,GPF_NO,DOB FROM"
                        + " EMP_MAST) EMP_MAST"
                        + " INNER JOIN (SELECT distinct SPC,INITIATEDBY,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,FISCAL_YEAR,is_reviewed,is_adversed  FROM PAR_MASTER WHERE FISCAL_YEAR=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID"
                        + " LEFT OUTER JOIN PAR_VIEWED_BY_CUSTODIAN ON PAR_MASTER.PARID=PAR_VIEWED_BY_CUSTODIAN.PAR_ID) EMP_MAST"
                        + " LEFT OUTER JOIN G_CADRE"
                        + " ON EMP_MAST.CADRE_CODE=G_CADRE.CADRE_CODE LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC"
                        + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE) EMP_MAST ORDER BY F_NAME) EMP_MAST ) EMP_MAST WHERE GPF_NO=? LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, fiscalYear);
                pst.setString(2, searchString);
                /*All Financial Year with empid wise*/
            } else if (searchCriteria.equals("gpfno") && parAdminSearchCriteria.getFiscalyear().equals("all")) {
                pst = con.prepareStatement("SELECT * FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,DECODE(SPC,NULL,CUR_SPC,SPC) PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT PAR_MASTER.FISCAL_YEAR,INITIATEDBY,PARID,EMP_MAST.EMP_ID,CUR_SPC,CADRE_CODE,MOBILE,F_NAME,M_NAME,L_NAME,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed FROM ("
                        + " SELECT EMP_ID,CUR_SPC,MOBILE,F_NAME,M_NAME,L_NAME,POST_GRP_TYPE,CUR_OFF_CODE,GPF_NO,DOB FROM"
                        + " EMP_MAST) EMP_MAST"
                        + " INNER JOIN (SELECT distinct SPC,INITIATEDBY,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,FISCAL_YEAR,is_reviewed,is_adversed  FROM PAR_MASTER WHERE is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID"
                        + " LEFT OUTER JOIN PAR_VIEWED_BY_CUSTODIAN ON PAR_MASTER.PARID=PAR_VIEWED_BY_CUSTODIAN.PAR_ID) EMP_MAST"
                        + " LEFT OUTER JOIN G_CADRE"
                        + " ON EMP_MAST.CADRE_CODE=G_CADRE.CADRE_CODE LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC"
                        + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE) EMP_MAST ORDER BY F_NAME) EMP_MAST ) EMP_MAST WHERE GPF_NO=? ORDER BY FISCAL_YEAR desc LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, searchString);

                /*Specific Financial Year with empname wise*/
            } else if (searchCriteria.equals("empname") && !parAdminSearchCriteria.getFiscalyear().equals("all")) {
                pst = con.prepareStatement("SELECT * FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,DECODE(SPC,NULL,CUR_SPC,SPC) PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT PAR_MASTER.FISCAL_YEAR,INITIATEDBY,PARID,EMP_MAST.EMP_ID,CUR_SPC,CADRE_CODE,MOBILE,F_NAME,M_NAME,L_NAME,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT EMP_ID,CUR_SPC,MOBILE,F_NAME,M_NAME,L_NAME,POST_GRP_TYPE,CUR_OFF_CODE,GPF_NO,DOB FROM"
                        + " EMP_MAST) EMP_MAST"
                        + " INNER JOIN (SELECT distinct SPC,PARID,INITIATEDBY,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,FISCAL_YEAR,is_reviewed,is_adversed  FROM PAR_MASTER WHERE FISCAL_YEAR=? AND is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID"
                        + " LEFT OUTER JOIN PAR_VIEWED_BY_CUSTODIAN ON PAR_MASTER.PARID=PAR_VIEWED_BY_CUSTODIAN.PAR_ID) EMP_MAST"
                        + " LEFT OUTER JOIN G_CADRE"
                        + " ON EMP_MAST.CADRE_CODE=G_CADRE.CADRE_CODE LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC"
                        + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE) EMP_MAST ORDER BY F_NAME) EMP_MAST ) EMP_MAST WHERE F_NAME=? LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, fiscalYear);
                pst.setString(2, searchString);
                /*All Financial Year with empid wise*/
            } else if (searchCriteria.equals("empname") && parAdminSearchCriteria.getFiscalyear().equals("all")) {
                pst = con.prepareStatement("SELECT * FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,DECODE(SPC,NULL,CUR_SPC,SPC) PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT PAR_MASTER.FISCAL_YEAR,INITIATEDBY,PARID,EMP_MAST.EMP_ID,CUR_SPC,CADRE_CODE,MOBILE,F_NAME,M_NAME,L_NAME,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed FROM ("
                        + " SELECT EMP_ID,CUR_SPC,MOBILE,F_NAME,M_NAME,L_NAME,POST_GRP_TYPE,CUR_OFF_CODE,GPF_NO,DOB FROM"
                        + " EMP_MAST) EMP_MAST"
                        + " INNER JOIN (SELECT distinct SPC,PARID,INITIATEDBY,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,FISCAL_YEAR,is_reviewed,is_adversed  FROM PAR_MASTER WHERE is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID"
                        + " LEFT OUTER JOIN PAR_VIEWED_BY_CUSTODIAN ON PAR_MASTER.PARID=PAR_VIEWED_BY_CUSTODIAN.PAR_ID) EMP_MAST"
                        + " LEFT OUTER JOIN G_CADRE"
                        + " ON EMP_MAST.CADRE_CODE=G_CADRE.CADRE_CODE LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC"
                        + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE) EMP_MAST ORDER BY F_NAME) EMP_MAST ) EMP_MAST WHERE F_NAME=? ORDER BY FISCAL_YEAR desc LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, searchString);

                /*Specific Financial Year with dob wise*/
            } else if (searchCriteria.equals("dob") && !parAdminSearchCriteria.getFiscalyear().equals("all")) {
                pst = con.prepareStatement("SELECT * FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,DECODE(SPC,NULL,CUR_SPC,SPC) PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT PAR_MASTER.FISCAL_YEAR,INITIATEDBY,PARID,EMP_MAST.EMP_ID,CUR_SPC,CADRE_CODE,MOBILE,F_NAME,M_NAME,L_NAME,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed FROM ("
                        + " SELECT EMP_ID,CUR_SPC,MOBILE,F_NAME,M_NAME,L_NAME,POST_GRP_TYPE,CUR_OFF_CODE,GPF_NO,DOB FROM"
                        + " EMP_MAST) EMP_MAST"
                        + " INNER JOIN (SELECT distinct SPC,PARID,INITIATEDBY,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,FISCAL_YEAR,is_reviewed,is_adversed  FROM PAR_MASTER WHERE FISCAL_YEAR=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID"
                        + " LEFT OUTER JOIN PAR_VIEWED_BY_CUSTODIAN ON PAR_MASTER.PARID=PAR_VIEWED_BY_CUSTODIAN.PAR_ID) EMP_MAST"
                        + " LEFT OUTER JOIN G_CADRE"
                        + " ON EMP_MAST.CADRE_CODE=G_CADRE.CADRE_CODE LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC"
                        + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE) EMP_MAST ORDER BY F_NAME) EMP_MAST ) EMP_MAST WHERE dob= to_date(?,'DD-MM-yyyy') LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, fiscalYear);
                pst.setString(2, searchString);
                /*All Financial Year with empid wise*/
            } else if (searchCriteria.equals("dob") && parAdminSearchCriteria.getFiscalyear().equals("all")) {
                pst = con.prepareStatement("SELECT * FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,DECODE(SPC,NULL,CUR_SPC,SPC) PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT PAR_MASTER.FISCAL_YEAR,INITIATEDBY,PARID,EMP_MAST.EMP_ID,CUR_SPC,CADRE_CODE,MOBILE,F_NAME,M_NAME,L_NAME,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT EMP_ID,CUR_SPC,MOBILE,F_NAME,M_NAME,L_NAME,POST_GRP_TYPE,CUR_OFF_CODE,GPF_NO,DOB FROM"
                        + " EMP_MAST) EMP_MAST"
                        + " INNER JOIN (SELECT distinct SPC,PARID,INITIATEDBY,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,FISCAL_YEAR,is_reviewed,is_adversed  FROM PAR_MASTER WHERE is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID"
                        + " LEFT OUTER JOIN PAR_VIEWED_BY_CUSTODIAN ON PAR_MASTER.PARID=PAR_VIEWED_BY_CUSTODIAN.PAR_ID) EMP_MAST"
                        + " LEFT OUTER JOIN G_CADRE"
                        + " ON EMP_MAST.CADRE_CODE=G_CADRE.CADRE_CODE LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC"
                        + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE) EMP_MAST ORDER BY F_NAME) EMP_MAST ) EMP_MAST WHERE dob= to_date(?,'DD-MM-yyyy') ORDER BY FISCAL_YEAR desc LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, searchString);

                /*Specific Financial Year with lastname wise*/
            } else if (searchCriteria.equals("lastname") && !parAdminSearchCriteria.getFiscalyear().equals("all")) {
                pst = con.prepareStatement("SELECT * FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,DECODE(SPC,NULL,CUR_SPC,SPC) PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT PAR_MASTER.FISCAL_YEAR,INITIATEDBY,PARID,EMP_MAST.EMP_ID,CUR_SPC,CADRE_CODE,MOBILE,F_NAME,M_NAME,L_NAME,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT EMP_ID,CUR_SPC,MOBILE,F_NAME,M_NAME,L_NAME,POST_GRP_TYPE,CUR_OFF_CODE,GPF_NO,DOB FROM"
                        + " EMP_MAST) EMP_MAST"
                        + " INNER JOIN (SELECT distinct SPC,PARID,INITIATEDBY,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,FISCAL_YEAR,is_reviewed,is_adversed  FROM PAR_MASTER WHERE FISCAL_YEAR=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID"
                        + " LEFT OUTER JOIN PAR_VIEWED_BY_CUSTODIAN ON PAR_MASTER.PARID=PAR_VIEWED_BY_CUSTODIAN.PAR_ID) EMP_MAST"
                        + " LEFT OUTER JOIN G_CADRE"
                        + " ON EMP_MAST.CADRE_CODE=G_CADRE.CADRE_CODE LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC"
                        + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE) EMP_MAST ORDER BY F_NAME) EMP_MAST ) EMP_MAST WHERE L_NAME =? LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, fiscalYear);
                pst.setString(2, searchString);
                /*All Financial Year with empid wise*/
            } else if (searchCriteria.equals("lastname") && parAdminSearchCriteria.getFiscalyear().equals("all")) {
                pst = con.prepareStatement("SELECT * FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,DECODE(SPC,NULL,CUR_SPC,SPC) PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT PAR_MASTER.FISCAL_YEAR,INITIATEDBY,PARID,EMP_MAST.EMP_ID,CUR_SPC,CADRE_CODE,MOBILE,F_NAME,M_NAME,L_NAME,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT EMP_ID,CUR_SPC,MOBILE,F_NAME,M_NAME,L_NAME,POST_GRP_TYPE,CUR_OFF_CODE,GPF_NO,DOB FROM"
                        + " EMP_MAST) EMP_MAST"
                        + " INNER JOIN (SELECT distinct SPC,PARID,INITIATEDBY,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,FISCAL_YEAR,is_reviewed,is_adversed  FROM PAR_MASTER WHERE  is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID"
                        + " LEFT OUTER JOIN PAR_VIEWED_BY_CUSTODIAN ON PAR_MASTER.PARID=PAR_VIEWED_BY_CUSTODIAN.PAR_ID) EMP_MAST"
                        + " LEFT OUTER JOIN G_CADRE"
                        + " ON EMP_MAST.CADRE_CODE=G_CADRE.CADRE_CODE LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC"
                        + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE) EMP_MAST ORDER BY F_NAME) EMP_MAST ) EMP_MAST WHERE L_NAME =? ORDER BY FISCAL_YEAR desc LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, searchString);
                /*Specific Financial Year with mobileno wise*/
            } else if (searchCriteria.equals("mobileno") && !parAdminSearchCriteria.getFiscalyear().equals("all")) {
                pst = con.prepareStatement("SELECT * FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,DECODE(SPC,NULL,CUR_SPC,SPC) PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT PAR_MASTER.FISCAL_YEAR,INITIATEDBY,PARID,EMP_MAST.EMP_ID,CUR_SPC,CADRE_CODE,MOBILE,F_NAME,M_NAME,L_NAME,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT EMP_ID,CUR_SPC,MOBILE,F_NAME,M_NAME,L_NAME,POST_GRP_TYPE,CUR_OFF_CODE,GPF_NO,DOB FROM"
                        + " EMP_MAST) EMP_MAST"
                        + " INNER JOIN (SELECT distinct SPC,PARID,INITIATEDBY,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,FISCAL_YEAR,is_reviewed,is_adversed  FROM PAR_MASTER WHERE FISCAL_YEAR=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID"
                        + " LEFT OUTER JOIN PAR_VIEWED_BY_CUSTODIAN ON PAR_MASTER.PARID=PAR_VIEWED_BY_CUSTODIAN.PAR_ID) EMP_MAST"
                        + " LEFT OUTER JOIN G_CADRE"
                        + " ON EMP_MAST.CADRE_CODE=G_CADRE.CADRE_CODE LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC"
                        + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE) EMP_MAST ORDER BY F_NAME) EMP_MAST ) EMP_MAST WHERE mobile =? LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, fiscalYear);
                pst.setString(2, searchString);
                /*all Financial Year with mobileno wise*/
            } else if (searchCriteria.equals("mobileno") && parAdminSearchCriteria.getFiscalyear().equals("all")) {
                pst = con.prepareStatement("SELECT * FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_ID,EMP_MAST.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,"
                        + " CADRE_NAME,POST,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,DECODE(SPC,NULL,CUR_SPC,SPC) PSPC,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT PAR_MASTER.FISCAL_YEAR,INITIATEDBY,PARID,EMP_MAST.EMP_ID,CUR_SPC,CADRE_CODE,MOBILE,F_NAME,M_NAME,L_NAME,POST_GRP_TYPE,PAR_STATUS,CUR_OFF_CODE,GPF_NO,DOB,HAS_SEEN,POST_GROUP,is_reviewed,is_adversed  FROM ("
                        + " SELECT EMP_ID,CUR_SPC,MOBILE,F_NAME,M_NAME,L_NAME,POST_GRP_TYPE,CUR_OFF_CODE,GPF_NO,DOB FROM"
                        + " EMP_MAST) EMP_MAST"
                        + " INNER JOIN (SELECT distinct SPC,PARID,INITIATEDBY,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,FISCAL_YEAR,is_reviewed,is_adversed  FROM PAR_MASTER WHERE is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID"
                        + " LEFT OUTER JOIN PAR_VIEWED_BY_CUSTODIAN ON PAR_MASTER.PARID=PAR_VIEWED_BY_CUSTODIAN.PAR_ID) EMP_MAST"
                        + " LEFT OUTER JOIN G_CADRE"
                        + " ON EMP_MAST.CADRE_CODE=G_CADRE.CADRE_CODE LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC"
                        + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE) EMP_MAST ORDER BY F_NAME) EMP_MAST ) EMP_MAST WHERE mobile =? ORDER BY FISCAL_YEAR desc LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, searchString);
            }
            res = pst.executeQuery();
            int count = 0;
            while (res.next()) {
                count++;
                ParAdminProperties parAdminProperties = new ParAdminProperties();
                parAdminProperties.setEmpId(res.getString("EMP_ID"));
                String fname = res.getString("F_NAME").trim();
                String mname = "";
                String lname = "";

                if (res.getString("M_NAME") != null) {
                    mname = res.getString("M_NAME").trim();
                } else {
                    mname = "";
                }
                if (res.getString("L_NAME") != null && !res.getString("L_NAME").equals("")) {
                    lname = res.getString("L_NAME").trim();
                }
                parAdminProperties.setPageNo(((page - 1) * 20 + count) + "");
                parAdminProperties.setEmpName(fname + " " + mname + " " + lname);
                // parAdminProperties.setDob(res.getString("DOB"));
                parAdminProperties.setDob(CommonFunctions.getFormattedOutputDate6(res.getDate("DOB")));
                parAdminProperties.setGpfno(res.getString("GPF_NO"));
                parAdminProperties.setFiscalyear(res.getString("FISCAL_YEAR"));
                parAdminProperties.setCadreName(res.getString("CADRE_NAME"));
                parAdminProperties.setCurrentoffice(res.getString("CUR_OFF_CODE"));
                parAdminProperties.setGroupName(StringUtils.defaultString(res.getString("POST_GROUP"), ""));
                parAdminProperties.setPostName(StringUtils.defaultString(res.getString("POST"), ""));
                parAdminProperties.setMobile(StringUtils.defaultString(res.getString("MOBILE"), ""));
                parAdminProperties.setParId(res.getInt("PARID"));

                parAdminProperties.setIsreview(res.getString("is_reviewed"));
                parAdminProperties.setIsadversed(res.getString("is_adversed"));
                if (res.getString("INITIATEDBY") != null && !res.getString("INITIATEDBY").equals("")) {
                    parAdminProperties.setInitiatedByEmpId(res.getString("INITIATEDBY"));
                } else {
                    parAdminProperties.setInitiatedByEmpId("");
                }
                if (res.getString("PAR_STATUS") != null && !res.getString("PAR_STATUS").equals("")) {
                    if (res.getInt("PAR_STATUS") == 0) {
                        parAdminProperties.setParstatus("PAR CREATED BUT NOT SUBMITTED");
                    } else {
                        parAdminProperties.setParstatus(getStatusName(con, res.getString("PAR_STATUS")));
                    }
                } else {
                    parAdminProperties.setParstatus("PAR NOT CREATED");
                }
                parList.add(parAdminProperties);
            }

            parSearchResult.setParlist(parList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parSearchResult;
    }

    public PARSearchResult getReviewedParList(ParAdminSearchCriteria parAdminSearchCriteria) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet res = null;
        ArrayList parList = new ArrayList();
        PARSearchResult parSearchResult = new PARSearchResult();
        try {
            parSearchResult.setTotalPARFound(0);
            con = repodataSource.getConnection();
            int offSet = parAdminSearchCriteria.getRows() * (parAdminSearchCriteria.getPage() - 1);
            int limit = parAdminSearchCriteria.getRows();

            String additionalFilter = "";
            if (!parAdminSearchCriteria.getSearchParStatus().equals("")) {
                additionalFilter = " and PAR_STATUS=" + parAdminSearchCriteria.getSearchParStatus();
            }
            if (parAdminSearchCriteria.getSearchCriteria1().equals("reviewed")) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=? AND is_reviewed = 'Y' " + additionalFilter + ")PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=? AND is_reviewed = 'Y' " + additionalFilter + ")PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());

            } else if (parAdminSearchCriteria.getSearchCriteria1().equals("nonreviewed")) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=? AND is_reviewed = 'N' " + additionalFilter + ")PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE  LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=? AND is_reviewed = 'N' " + additionalFilter + ")PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());

            } else if (parAdminSearchCriteria.getSearchCriteria1().equals("adverse")) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=? AND is_adversed = 'Y' " + additionalFilter + ")PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE  LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=? AND is_adversed = 'Y' " + additionalFilter + ")PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());

            } else {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=? " + additionalFilter + ")PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=? " + additionalFilter + ")PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
            }

            res = pst.executeQuery();
            int count = 0;
            while (res.next()) {
                count++;
                ParAdminProperties parAdminProperties = new ParAdminProperties();
                parAdminProperties.setEmpId(res.getString("EMP_ID"));
                String fname = res.getString("F_NAME").trim();
                String mname = "";
                String lname = "";

                if (res.getString("M_NAME") != null) {
                    mname = res.getString("M_NAME").trim();
                } else {
                    mname = "";
                }
                if (res.getString("L_NAME") != null && !res.getString("L_NAME").equals("")) {
                    lname = res.getString("L_NAME").trim();
                }

                parAdminProperties.setPageNo("");
                parAdminProperties.setEmpName(fname + " " + mname + " " + lname);
                parAdminProperties.setDob(CommonFunctions.getFormattedOutputDate6(res.getDate("DOB")));
                parAdminProperties.setGpfno(res.getString("GPF_NO"));
                parAdminProperties.setCadreName(res.getString("CADRE_NAME"));
                parAdminProperties.setCurrentoffice(res.getString("OFF_CODE"));
                parAdminProperties.setGroupName(StringUtils.defaultString(res.getString("POST_GROUP"), ""));
                parAdminProperties.setPostName(StringUtils.defaultString(res.getString("POST"), ""));
                parAdminProperties.setMobile(StringUtils.defaultString(res.getString("MOBILE"), ""));
                parAdminProperties.setParId(res.getInt("PARID"));
                if (res.getString("PAR_STATUS") != null && !res.getString("PAR_STATUS").equals("")) {
                    if (res.getInt("PAR_STATUS") == 0) {
                        parAdminProperties.setParstatus("PAR CREATED BUT NOT SUBMITTED");
                    } else {
                        parAdminProperties.setParstatus(getStatusName(con, res.getString("PAR_STATUS")));
                    }
                } else {
                    parAdminProperties.setParstatus("PAR NOT CREATED");
                }
                parList.add(parAdminProperties);
            }

            parSearchResult.setParlist(parList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parSearchResult;
    }

    //(lub.getLoginspc(), parAdminSearchCriteria.getCadrecode(), parAdminSearchCriteria.getSearchCriteria(),parAdminSearchCriteria.getSearchString(), parAdminSearchCriteria.getFiscalyear(),parAdminSearchCriteria.getSearchParStatus());
    @Override
    public ArrayList getPendingPARListAtAuthorityEnd(String privilegedSpc, String cadrecode, String searchCriteria, String searchString, String fiscalyear, String searchParStatus) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet res = null;
        ArrayList pendingparList = new ArrayList();
        String parstatus = "";
        try {
            con = this.dataSource.getConnection();
            if ((fiscalyear != null && !fiscalyear.equals("") && searchParStatus != null && !searchParStatus.equals("") && !searchParStatus.equals("117")) && (cadrecode == null || cadrecode.equals(""))) {
                pst = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate,parid,par_status,FISCAL_YEAR,GPF_NO,DOB,PAR_MASTER.EMP_ID,F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE, "
                        + "PAR_MASTER.CADRE_CODE,G_CADRE.CADRE_NAME,POST,POST_GROUP,PAR_MASTER.OFF_CODE,G_OFFICE.off_en,get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname, "
                        + "getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,period_from,period_to FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER PAR_MASTER WHERE FISCAL_YEAR=?  and (par_type is null or par_type = '')  and par_status=?)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE");

                pst.setString(1, fiscalyear);
                pst.setInt(2, Integer.parseInt(searchParStatus));
                pst.setString(3, privilegedSpc);

            } else if (fiscalyear != null && !fiscalyear.equals("") && cadrecode != null && !cadrecode.equals("") && searchParStatus != null && !searchParStatus.equals("")) {
                pst = con.prepareStatement("SELECT FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,off_en, "
                        + "get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname,getspn(TASK_MASTER.pending_spc)as pendingauthorityspc,PENDING_AT,period_from,period_to FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,TASK_ID,period_from,period_to FROM PAR_MASTER WHERE FISCAL_YEAR=? and par_status=? and cadre_code=?)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID"
                        + "LEFT OUTER JOIN G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE");
                pst.setString(1, fiscalyear);
                pst.setInt(2, Integer.parseInt(searchParStatus));
                pst.setString(3, cadrecode);
                pst.setString(4, privilegedSpc);
            } else if ((fiscalyear != null && !fiscalyear.equals("") && searchParStatus != null && !searchParStatus.equals("") && searchParStatus.equals("117")) && (cadrecode == null || cadrecode.equals(""))) {
                pst = con.prepareStatement("select empmastid as emp_id,null as off_code,G_POST.post,CADRE_NAME,off_en,null as pendingparDate,null as parid,null as par_status,FISCAL_YEAR,GPF_NO,DOB, "
                        + "F_NAME,M_NAME,L_NAME,MOBILE,post_grp_type as post_group,null as pendingauthorityname, "
                        + "null as  pendingauthorityspc,null as PENDING_AT,null as period_from,null as period_to,* from "
                        + "(select emp_mast.emp_id as empmastid,par_master.parid,par_master.emp_id as parmastid,cur_off_code,cur_SPC,cadre_code,FISCAL_YEAR,GPF_NO,DOB, "
                        + "F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE,post_grp_type from "
                        + "(select emp_id,f_name,M_NAME,L_NAME,EMP_MAST.MOBILE,cur_off_code,cur_SPC,CADRE_CODE,GPF_NO,DOB,post_grp_type from emp_mast where  dep_code ='02' and post_grp_type in ('A','B')) as emp_mast "
                        + "left outer join "
                        + "(select emp_id,parid,FISCAL_YEAR from par_master where fiscal_year=?) as par_master "
                        + "on emp_mast.emp_id = par_master.emp_id)as t1 "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin "
                        + "ON par_authority_admin.OFF_CODE = t1.cur_off_code "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON t1.cur_SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN (select * from G_POST)as G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN (select * from g_cadre) G_CADRE ON t1.cadre_code=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_OFFICE ON t1.CUR_OFF_CODE=G_OFFICE.OFF_CODE "
                        + "where parmastid is null");
                pst.setString(1, fiscalyear);
                pst.setString(2, privilegedSpc);
            }
            res = pst.executeQuery();
            while (res.next()) {
                ParAdminProperties parAdminProperties = new ParAdminProperties();
                if (res.getString("pendingauthorityname") != null && !res.getString("pendingauthorityname").equals("")) {
                    parAdminProperties.setPendingAtAuthName(res.getString("pendingauthorityname"));
                } else {
                    parAdminProperties.setPendingAtAuthName("");
                }
                if (res.getString("pendingauthorityspc") != null && !res.getString("pendingauthorityspc").equals("")) {
                    parAdminProperties.setPendingAtSpc(res.getString("pendingauthorityspc"));
                } else {
                    parAdminProperties.setPendingAtSpc("");
                }
                if (res.getString("PENDING_AT") != null && !res.getString("PENDING_AT").equals("")) {
                    parAdminProperties.setPendingAtEmpId(res.getString("PENDING_AT"));
                } else {
                    parAdminProperties.setPendingAtEmpId("");
                }
                if (res.getString("PARID") != null && !res.getString("PARID").equals("")) {
                    parAdminProperties.setParId(res.getInt("PARID"));
                }
                parAdminProperties.setEmpId(res.getString("EMP_ID"));
                String fname = res.getString("F_NAME").trim();
                String mname = "";
                String lname = "";

                if (res.getString("M_NAME") != null) {
                    mname = res.getString("M_NAME").trim();
                } else {
                    mname = "";
                }
                if (res.getString("L_NAME") != null && !res.getString("L_NAME").equals("")) {
                    lname = res.getString("L_NAME").trim();
                }
                parAdminProperties.setEmpName(fname + " " + mname + " " + lname);
                parAdminProperties.setDob(CommonFunctions.getFormattedOutputDate6(res.getDate("DOB")));
                parAdminProperties.setCurrentOfficeName(res.getString("OFF_EN"));
                parAdminProperties.setGpfno(res.getString("GPF_NO"));
                parAdminProperties.setCadreName(res.getString("CADRE_NAME"));
                parAdminProperties.setCurrentoffice(res.getString("OFF_CODE"));
                parAdminProperties.setPrdFrmDate(res.getString("PERIOD_FROM"));
                parAdminProperties.setPrdToDate(res.getString("PERIOD_TO"));
                parAdminProperties.setGroupName(StringUtils.defaultString(res.getString("POST_GROUP"), ""));
                parAdminProperties.setPostName(StringUtils.defaultString(res.getString("POST"), ""));
                parAdminProperties.setMobile(StringUtils.defaultString(res.getString("MOBILE"), ""));
                parAdminProperties.setParId(res.getInt("PARID"));
                if (res.getString("PAR_STATUS") != null && !res.getString("PAR_STATUS").equals("")) {
                    if (res.getInt("PAR_STATUS") == 0) {
                        parAdminProperties.setParstatus("PAR CREATED BUT NOT SUBMITTED");
                    } else {
                        parAdminProperties.setParstatus(getStatusName(con, res.getString("PAR_STATUS")));
                    }
                } else {
                    parAdminProperties.setParstatus("PAR NOT CREATED");
                }
                parAdminProperties.setReportingauth(getParAuthority("REPORTING", parAdminProperties.getParId(), 0));
                parAdminProperties.setReviewingauth(getParAuthority("REVIEWING", parAdminProperties.getParId(), 0));
                parAdminProperties.setAcceptingauth(getParAuthority("ACCEPTING", parAdminProperties.getParId(), 0));
                pendingparList.add(parAdminProperties);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return pendingparList;

    }

    @Override
    public PARSearchResult getOfficeCadreSpecificPARList(ParAdminSearchCriteria parAdminSearchCriteria) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet res = null;
        ArrayList parList = new ArrayList();
        PARSearchResult parSearchResult = new PARSearchResult();
        try {
            parSearchResult.setTotalPARFound(0);
            con = repodataSource.getConnection();
            int offSet = parAdminSearchCriteria.getRows() * (parAdminSearchCriteria.getPage() - 1);
            int limit = parAdminSearchCriteria.getRows();
            String parstatus = "";

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            Long curtime = new Date().getTime();
            /*Search only by fiscal year*/
            if ((parAdminSearchCriteria.getSearchCriteria() == null || parAdminSearchCriteria.getSearchCriteria().equals("")) && (parAdminSearchCriteria.getSearchCriteria1() == null || parAdminSearchCriteria.getSearchCriteria1().equals("")) && (parAdminSearchCriteria.getCadrecode() == null || parAdminSearchCriteria.getCadrecode().equals("")) && (parAdminSearchCriteria.getSearchParStatus() == null || parAdminSearchCriteria.getSearchParStatus().equals(""))) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=?)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=?)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
                /*Search by fiscal year and cadre code*/
            } else if ((parAdminSearchCriteria.getSearchCriteria() == null || parAdminSearchCriteria.getSearchCriteria().equals("")) && (parAdminSearchCriteria.getSearchCriteria1() == null || parAdminSearchCriteria.getSearchCriteria1().equals("")) && parAdminSearchCriteria.getCadrecode() != null && (parAdminSearchCriteria.getSearchParStatus() == null || parAdminSearchCriteria.getSearchParStatus().equals(""))) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE  FISCAL_YEAR=? and cadre_code=?)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getCadrecode());
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=?)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getCadrecode());
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
                /*Search by fiscal year and parstatus*/
            } else if ((parAdminSearchCriteria.getSearchCriteria() == null || parAdminSearchCriteria.getSearchCriteria().equals("")) && (parAdminSearchCriteria.getSearchCriteria1() == null || parAdminSearchCriteria.getSearchCriteria1().equals("")) && (parAdminSearchCriteria.getCadrecode() == null || parAdminSearchCriteria.getCadrecode().equals("")) && parAdminSearchCriteria.getSearchParStatus() != null) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE  FISCAL_YEAR=? and par_status=?)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE ");
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setInt(2, Integer.parseInt(parAdminSearchCriteria.getSearchParStatus()));
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=? and par_status=?)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setInt(2, Integer.parseInt(parAdminSearchCriteria.getSearchParStatus()));
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
                /*Search by fiscal year,Cadre wise and parstatus*/
            } else if ((parAdminSearchCriteria.getSearchCriteria() == null || parAdminSearchCriteria.getSearchCriteria().equals("")) && (parAdminSearchCriteria.getSearchCriteria1() == null || parAdminSearchCriteria.getSearchCriteria1().equals("")) && (parAdminSearchCriteria.getCadrecode() != null && parAdminSearchCriteria.getSearchParStatus() != null)) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE  FISCAL_YEAR=? and cadre_code=? and par_status=?)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getCadrecode());
                pst.setInt(3, Integer.parseInt(parAdminSearchCriteria.getSearchParStatus()));
                pst.setString(4, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and par_status=?)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getCadrecode());
                pst.setInt(3, Integer.parseInt(parAdminSearchCriteria.getSearchParStatus()));
                pst.setString(4, parAdminSearchCriteria.getPrivilegedSpc());

                /*Search by empid*/
            } else if (parAdminSearchCriteria.getSearchCriteria().equals("empid") && (parAdminSearchCriteria.getFiscalyear() == null || parAdminSearchCriteria.getFiscalyear().equals(""))) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST WHERE EMP_ID = ?)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, parAdminSearchCriteria.getSearchString());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT PARID,FISCAL_YEAR,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST WHERE EMP_ID = ?)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, parAdminSearchCriteria.getSearchString());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
            } else if (parAdminSearchCriteria.getSearchCriteria().equals("empid")) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST WHERE EMP_ID = ?)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=?)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, parAdminSearchCriteria.getSearchString());
                pst.setString(2, parAdminSearchCriteria.getFiscalyear());
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST WHERE EMP_ID = ?)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=?)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, parAdminSearchCriteria.getSearchString());
                pst.setString(2, parAdminSearchCriteria.getFiscalyear());
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
            } else if (parAdminSearchCriteria.getSearchCriteria().equals("gpfno")) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST WHERE GPF_NO = ?)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=?)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, StringUtils.upperCase(parAdminSearchCriteria.getSearchString()));
                pst.setString(2, parAdminSearchCriteria.getFiscalyear());
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST WHERE GPF_NO = ?)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=?)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, StringUtils.upperCase(parAdminSearchCriteria.getSearchString()));
                pst.setString(2, parAdminSearchCriteria.getFiscalyear());
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
            } else if (parAdminSearchCriteria.getSearchCriteria().equals("empname")) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST WHERE F_NAME = ?)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=?)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, StringUtils.upperCase(parAdminSearchCriteria.getSearchString()));
                pst.setString(2, parAdminSearchCriteria.getFiscalyear());
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST WHERE F_NAME = ?)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=?)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, StringUtils.upperCase(parAdminSearchCriteria.getSearchString()));
                pst.setString(2, parAdminSearchCriteria.getFiscalyear());
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());

            } else if (parAdminSearchCriteria.getSearchCriteria().equals("lastname")) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST WHERE L_NAME = ?)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=?)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, parAdminSearchCriteria.getSearchString());
                pst.setString(2, parAdminSearchCriteria.getFiscalyear());
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST WHERE L_NAME = ?)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=?)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, parAdminSearchCriteria.getSearchString());
                pst.setString(2, parAdminSearchCriteria.getFiscalyear());
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
            } else if (parAdminSearchCriteria.getSearchCriteria().equals("dob")) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST WHERE dob= to_date(?,'DD-MM-yyyy'))EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=?)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, parAdminSearchCriteria.getSearchString());
                pst.setString(2, parAdminSearchCriteria.getFiscalyear());
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST WHERE dob= to_date(?,'DD-MM-yyyy'))EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=?)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, parAdminSearchCriteria.getSearchString());
                pst.setString(2, parAdminSearchCriteria.getFiscalyear());
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
            } else if (((parAdminSearchCriteria.getSearchCriteria() == null || parAdminSearchCriteria.getSearchCriteria().equals("")) && (parAdminSearchCriteria.getCadrecode() == null || parAdminSearchCriteria.getCadrecode().equals("") && parAdminSearchCriteria.getSearchParStatus() == null || parAdminSearchCriteria.getSearchParStatus().equals(""))) && (parAdminSearchCriteria.getSearchCriteria1().equals("reviewed"))) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=? AND is_reviewed = 'Y')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE  LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=? AND is_reviewed = 'Y')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
            } else if ((parAdminSearchCriteria.getSearchCriteria() == null || parAdminSearchCriteria.getSearchCriteria().equals("")) && (parAdminSearchCriteria.getCadrecode() == null || parAdminSearchCriteria.getCadrecode().equals("")) && (parAdminSearchCriteria.getSearchParStatus() != null && parAdminSearchCriteria.getSearchCriteria1().equals("reviewed"))) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE  FISCAL_YEAR=? and par_status=? and is_reviewed='Y')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE ");
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setInt(2, Integer.parseInt(parAdminSearchCriteria.getSearchParStatus()));
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=? and par_status=? and is_reviewed='Y')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setInt(2, Integer.parseInt(parAdminSearchCriteria.getSearchParStatus()));
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
            } else if ((parAdminSearchCriteria.getSearchCriteria() == null || parAdminSearchCriteria.getSearchCriteria().equals("")) && (parAdminSearchCriteria.getSearchParStatus() == null || parAdminSearchCriteria.getSearchParStatus().equals("")) && (parAdminSearchCriteria.getCadrecode() != null && parAdminSearchCriteria.getSearchCriteria1().equals("reviewed"))) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE  FISCAL_YEAR=? and cadre_code=? and is_reviewed='Y')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE ");
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getCadrecode());
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and is_reviewed='Y')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getCadrecode());
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
            } else if ((parAdminSearchCriteria.getSearchCriteria() == null || parAdminSearchCriteria.getSearchCriteria().equals("")) && (parAdminSearchCriteria.getSearchCriteria1().equals("reviewed")) && (parAdminSearchCriteria.getCadrecode() != null && parAdminSearchCriteria.getSearchParStatus() != null)) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE  FISCAL_YEAR=? and cadre_code=? and par_status=? and is_reviewed='Y')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getCadrecode());
                pst.setInt(3, Integer.parseInt(parAdminSearchCriteria.getSearchParStatus()));
                pst.setString(4, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }
                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and par_status=? and is_reviewed='Y')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getCadrecode());
                pst.setInt(3, Integer.parseInt(parAdminSearchCriteria.getSearchParStatus()));
                pst.setString(4, parAdminSearchCriteria.getPrivilegedSpc());
            } else if (((parAdminSearchCriteria.getSearchCriteria() == null || parAdminSearchCriteria.getSearchCriteria().equals("")) && (parAdminSearchCriteria.getCadrecode() == null || parAdminSearchCriteria.getCadrecode().equals("") && parAdminSearchCriteria.getSearchParStatus() == null || parAdminSearchCriteria.getSearchParStatus().equals(""))) && (parAdminSearchCriteria.getSearchCriteria1().equals("nonreviewed"))) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=? AND is_reviewed = 'N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE  LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=? AND is_reviewed = 'N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
            } else if ((parAdminSearchCriteria.getSearchCriteria() == null || parAdminSearchCriteria.getSearchCriteria().equals("")) && (parAdminSearchCriteria.getCadrecode() == null || parAdminSearchCriteria.getCadrecode().equals("")) && (parAdminSearchCriteria.getSearchParStatus() != null && parAdminSearchCriteria.getSearchCriteria1().equals("nonreviewed"))) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE  FISCAL_YEAR=? and par_status=? and is_reviewed='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE ");
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setInt(2, Integer.parseInt(parAdminSearchCriteria.getSearchParStatus()));
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=? and par_status=? and is_reviewed='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setInt(2, Integer.parseInt(parAdminSearchCriteria.getSearchParStatus()));
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
            } else if ((parAdminSearchCriteria.getSearchCriteria() == null || parAdminSearchCriteria.getSearchCriteria().equals("")) && (parAdminSearchCriteria.getSearchParStatus() == null || parAdminSearchCriteria.getSearchParStatus().equals("")) && (parAdminSearchCriteria.getCadrecode() != null && parAdminSearchCriteria.getSearchCriteria1().equals("nonreviewed"))) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE  FISCAL_YEAR=? and cadre_code=? and is_reviewed='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE ");
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getCadrecode());
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and is_reviewed='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP  AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getCadrecode());
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
            } else if ((parAdminSearchCriteria.getSearchCriteria() == null || parAdminSearchCriteria.getSearchCriteria().equals("")) && (parAdminSearchCriteria.getSearchCriteria1().equals("nonreviewed")) && (parAdminSearchCriteria.getCadrecode() != null && parAdminSearchCriteria.getSearchParStatus() != null)) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE  FISCAL_YEAR=? and cadre_code=? and par_status=? and is_reviewed='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP  AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getCadrecode());
                pst.setInt(3, Integer.parseInt(parAdminSearchCriteria.getSearchParStatus()));
                pst.setString(4, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }
                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and par_status=? and is_reviewed='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getCadrecode());
                pst.setInt(3, Integer.parseInt(parAdminSearchCriteria.getSearchParStatus()));
                pst.setString(4, parAdminSearchCriteria.getPrivilegedSpc());
            }

            res = pst.executeQuery();
            int count = 0;
            while (res.next()) {
                count++;
                ParAdminProperties parAdminProperties = new ParAdminProperties();
                parAdminProperties.setEmpId(res.getString("EMP_ID"));
                String fname = res.getString("F_NAME").trim();
                String mname = "";
                String lname = "";

                if (res.getString("M_NAME") != null) {
                    mname = res.getString("M_NAME").trim();
                } else {
                    mname = "";
                }
                if (res.getString("L_NAME") != null && !res.getString("L_NAME").equals("")) {
                    lname = res.getString("L_NAME").trim();
                }
                parAdminProperties.setPageNo(((parAdminSearchCriteria.getPage() - 1) * 20 + count) + "");
                parAdminProperties.setEmpName(fname + " " + mname + " " + lname);
                // parAdminProperties.setDob(res.getString("DOB"));
                parAdminProperties.setDob(CommonFunctions.getFormattedOutputDate6(res.getDate("DOB")));
                parAdminProperties.setGpfno(res.getString("GPF_NO"));
                parAdminProperties.setCadreName(res.getString("CADRE_NAME"));
                parAdminProperties.setCurrentoffice(res.getString("OFF_CODE"));
                parAdminProperties.setGroupName(StringUtils.defaultString(res.getString("POST_GROUP"), ""));
                parAdminProperties.setPostName(StringUtils.defaultString(res.getString("POST"), ""));
                parAdminProperties.setMobile(StringUtils.defaultString(res.getString("MOBILE"), ""));
                parAdminProperties.setParId(res.getInt("PARID"));
                //parAdminProperties.setFiscalyear(res.getString("FISCAL_YEAR"));
                if (res.getString("PAR_STATUS") != null && !res.getString("PAR_STATUS").equals("")) {
                    if (res.getInt("PAR_STATUS") == 0) {
                        parAdminProperties.setParstatus("PAR CREATED BUT NOT SUBMITTED");
                    } else {
                        parAdminProperties.setParstatus(getStatusName(con, res.getString("PAR_STATUS")));
                    }
                } else {
                    parAdminProperties.setParstatus("PAR NOT CREATED");
                }
                parList.add(parAdminProperties);
            }

            parSearchResult.setParlist(parList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parSearchResult;
    }

    @Override
    public PARSearchResult getOfficeSpecificPARList(ParAdminSearchCriteria parAdminSearchCriteria) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet res = null;
        ArrayList parList = new ArrayList();
        PARSearchResult parSearchResult = new PARSearchResult();
        try {
            parSearchResult.setTotalPARFound(0);
            con = repodataSource.getConnection();
            int offSet = parAdminSearchCriteria.getRows() * (parAdminSearchCriteria.getPage() - 1);
            int limit = parAdminSearchCriteria.getRows();
            String parstatus = "";

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            Long curtime = new Date().getTime();
            /*Search only by fiscal year*/
            if ((parAdminSearchCriteria.getSearchCriteria() == null || parAdminSearchCriteria.getSearchCriteria().equals("")) && (parAdminSearchCriteria.getSearchCriteria1() == null || parAdminSearchCriteria.getSearchCriteria1().equals("")) && (parAdminSearchCriteria.getCadrecode() == null || parAdminSearchCriteria.getCadrecode().equals("")) && (parAdminSearchCriteria.getSearchParStatus() == null || parAdminSearchCriteria.getSearchParStatus().equals("")) && !parAdminSearchCriteria.getFiscalyear().equals("all")) {
                pst = con.prepareStatement("SELECT count(*) cnt FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER PAR_MASTER WHERE FISCAL_YEAR=?  and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE");
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate,parid,par_status,FISCAL_YEAR,GPF_NO,DOB,PAR_MASTER.EMP_ID,F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE, "
                        + "PAR_MASTER.CADRE_CODE,G_CADRE.CADRE_NAME,POST,POST_GROUP,PAR_MASTER.OFF_CODE,G_OFFICE.off_en,get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname, "
                        + "getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,period_from,period_to FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER PAR_MASTER WHERE FISCAL_YEAR=?  and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE LIMIT " + limit + " OFFSET " + offSet);

                /* pst = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,off_en, "
                 + "get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname,getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,period_from,period_to FROM "
                 + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE,post_grp_type FROM EMP_MAST)EMP_MAST "
                 + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,TASK_ID,period_from,period_to FROM PAR_MASTER WHERE FISCAL_YEAR=?)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                 + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE and par_authority_admin.post_grp = EMP_MAST.post_grp_type "
                 + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                 + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                 + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                 + "LEFT OUTER JOIN TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                 + "LEFT OUTER JOIN G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE LIMIT " + limit + " OFFSET " + offSet); */
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
                /*Search by fiscal year and cadre code*/
            } else if ((parAdminSearchCriteria.getSearchCriteria() == null || parAdminSearchCriteria.getSearchCriteria().equals("")) && (parAdminSearchCriteria.getSearchCriteria1() == null || parAdminSearchCriteria.getSearchCriteria1().equals("")) && parAdminSearchCriteria.getCadrecode() != null && (parAdminSearchCriteria.getSearchParStatus() == null || parAdminSearchCriteria.getSearchParStatus().equals(""))) {
                pst = con.prepareStatement("SELECT count(*) cnt FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=?  and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE");
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getCadrecode());
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate,parid,par_status,FISCAL_YEAR,GPF_NO,DOB,PAR_MASTER.EMP_ID,F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE, "
                        + "PAR_MASTER.CADRE_CODE,G_CADRE.CADRE_NAME,POST,POST_GROUP,PAR_MASTER.OFF_CODE,G_OFFICE.off_en,get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname, "
                        + "getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,period_from,period_to FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=?  and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getCadrecode());
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
                /*Search by fiscal year and parstatus*/
            } else if ((parAdminSearchCriteria.getSearchCriteria() == null || parAdminSearchCriteria.getSearchCriteria().equals("")) && (parAdminSearchCriteria.getSearchCriteria1() == null || parAdminSearchCriteria.getSearchCriteria1().equals("")) && (parAdminSearchCriteria.getCadrecode() == null || parAdminSearchCriteria.getCadrecode().equals("")) && parAdminSearchCriteria.getSearchParStatus() != null && !parAdminSearchCriteria.getSearchParStatus().equals("117")) {
                pst = con.prepareStatement("SELECT count(*) cnt FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER PAR_MASTER WHERE FISCAL_YEAR=? and par_status=? and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE");
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setInt(2, Integer.parseInt(parAdminSearchCriteria.getSearchParStatus()));
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate,parid,par_status,FISCAL_YEAR,GPF_NO,DOB,PAR_MASTER.EMP_ID,F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE, "
                        + "PAR_MASTER.CADRE_CODE,G_CADRE.CADRE_NAME,POST,POST_GROUP,PAR_MASTER.OFF_CODE,G_OFFICE.off_en,get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname, "
                        + "getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,period_from,period_to FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER PAR_MASTER WHERE FISCAL_YEAR=? and par_status=?  and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE  LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setInt(2, Integer.parseInt(parAdminSearchCriteria.getSearchParStatus()));
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
                /*Search by fiscal year,parstatus 117*/
            } else if ((parAdminSearchCriteria.getSearchCriteria() == null || parAdminSearchCriteria.getSearchCriteria().equals("")) && (parAdminSearchCriteria.getSearchCriteria1() == null || parAdminSearchCriteria.getSearchCriteria1().equals("")) && (parAdminSearchCriteria.getCadrecode() == null || parAdminSearchCriteria.getCadrecode().equals("")) && parAdminSearchCriteria.getSearchParStatus().equals("117")) {
                /* previously written 
                
                 pst = con.prepareStatement("SELECT count(*) cnt FROM EMP_MAST "
                 + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?) par_authority_admin ON EMP_MAST.CUR_CADRE_CODE = par_authority_admin.CADRE_CODE AND EMP_MAST.POST_GRP_TYPE = par_authority_admin.POST_GRP  "
                 + "LEFT OUTER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,PAR_STATUS,EMP_ID AS PAR_EMP_ID,OFF_CODE,POST_GROUP,CADRE_CODE FROM PAR_MASTER WHERE FISCAL_YEAR=?)PAR_MASTER ON EMP_MAST.EMP_ID = PAR_MASTER.PAR_EMP_ID "
                 + "LEFT OUTER JOIN G_CADRE ON EMP_MAST.CADRE_CODE=G_CADRE.CADRE_CODE "
                 + "LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC  "
                 + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                 + "LEFT OUTER JOIN G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE WHERE PAR_EMP_ID IS NULL"); */

                pst = con.prepareStatement("select count (*) cnt from "
                        + "(select emp_mast.emp_id as empmastid,par_master.parid,par_master.emp_id as parmastid,cur_off_code,cur_SPC,cadre_code,FISCAL_YEAR,GPF_NO,DOB, "
                        + "F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE,post_grp_type from "
                        + "(select emp_id,f_name,M_NAME,L_NAME,EMP_MAST.MOBILE,cur_off_code,cur_SPC,CADRE_CODE,GPF_NO,DOB,post_grp_type from emp_mast where  dep_code ='02' and post_grp_type in ('A','B')) as emp_mast "
                        + "left outer join "
                        + "(select emp_id,parid,FISCAL_YEAR from par_master where fiscal_year=?) as par_master "
                        + "on emp_mast.emp_id = par_master.emp_id)as t1 "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin "
                        + "ON par_authority_admin.OFF_CODE = t1.cur_off_code "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON t1.cur_SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN (select * from G_POST)as G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN (select * from g_cadre) G_CADRE ON t1.cadre_code=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_OFFICE ON t1.CUR_OFF_CODE=G_OFFICE.OFF_CODE "
                        + "where parmastid is null LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("select empmastid as emp_id,null as off_code,G_POST.post,CADRE_NAME,off_en,null as pendingparDate,null as parid,null as par_status,FISCAL_YEAR,GPF_NO,DOB, "
                        + "F_NAME,M_NAME,L_NAME,MOBILE,post_grp_type as post_group,null as pendingauthorityname, "
                        + "null as  pendingauthorityspc,null as PENDING_AT,null as period_from,null as period_to,* from "
                        + "(select emp_mast.emp_id as empmastid,par_master.parid,par_master.emp_id as parmastid,cur_off_code,cur_SPC,cadre_code,FISCAL_YEAR,GPF_NO,DOB, "
                        + "F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE,post_grp_type from "
                        + "(select emp_id,f_name,M_NAME,L_NAME,EMP_MAST.MOBILE,cur_off_code,cur_SPC,CADRE_CODE,GPF_NO,DOB,post_grp_type from emp_mast where  dep_code ='02' and post_grp_type in ('A','B')) as emp_mast "
                        + "left outer join "
                        + "(select emp_id,parid,FISCAL_YEAR from par_master where fiscal_year=?) as par_master "
                        + "on emp_mast.emp_id = par_master.emp_id)as t1 "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin "
                        + "ON par_authority_admin.OFF_CODE = t1.cur_off_code "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON t1.cur_SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN (select * from G_POST)as G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN (select * from g_cadre) G_CADRE ON t1.cadre_code=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_OFFICE ON t1.CUR_OFF_CODE=G_OFFICE.OFF_CODE "
                        + "where parmastid is null LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());

                /*Search by fiscal year,Cadre wise and parstatus*/
            } else if ((parAdminSearchCriteria.getSearchCriteria() == null || parAdminSearchCriteria.getSearchCriteria().equals("")) && (parAdminSearchCriteria.getSearchCriteria1() == null || parAdminSearchCriteria.getSearchCriteria1().equals("")) && (parAdminSearchCriteria.getCadrecode() != null && parAdminSearchCriteria.getSearchParStatus() != null)) {
                pst = con.prepareStatement("SELECT count(*) cnt FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and par_status=?  and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE");
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getCadrecode());
                pst.setInt(3, Integer.parseInt(parAdminSearchCriteria.getSearchParStatus()));
                pst.setString(4, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate,parid,par_status,FISCAL_YEAR,GPF_NO,DOB,PAR_MASTER.EMP_ID,F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE, "
                        + "PAR_MASTER.CADRE_CODE,G_CADRE.CADRE_NAME,POST,POST_GROUP,PAR_MASTER.OFF_CODE,G_OFFICE.off_en,get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname, "
                        + "getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,period_from,period_to FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and par_status=? and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE LIMIT " + limit + " OFFSET " + offSet);

                /* pst = con.prepareStatement("SELECT FISCAL_YEAR,get_par_pending_date(PARID) as pendingparDate,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,off_en, "
                 + "get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname,getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,period_from,period_to FROM "
                 + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE,post_grp_type FROM EMP_MAST)EMP_MAST "
                 + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,TASK_ID,period_from,period_to FROM PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and par_status=?)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                 + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE and par_authority_admin.post_grp = EMP_MAST.post_grp_type "
                 + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                 + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                 + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                 + "LEFT OUTER JOIN TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                 + "LEFT OUTER JOIN G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE LIMIT " + limit + " OFFSET " + offSet); */
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getCadrecode());
                pst.setInt(3, Integer.parseInt(parAdminSearchCriteria.getSearchParStatus()));
                pst.setString(4, parAdminSearchCriteria.getPrivilegedSpc());

                /*Search by empid*/
            } else if (parAdminSearchCriteria.getSearchCriteria().equals("empid") && (parAdminSearchCriteria.getFiscalyear() == null || parAdminSearchCriteria.getFiscalyear().equals(""))) {
                pst = con.prepareStatement("SELECT count(*) cnt FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER PAR_MASTER WHERE FISCAL_YEAR=? and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE where emp_mast.EMP_ID =?");
                pst.setString(1, parAdminSearchCriteria.getSearchString());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate,parid,par_status,FISCAL_YEAR,GPF_NO,DOB,PAR_MASTER.EMP_ID,F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE, "
                        + "PAR_MASTER.CADRE_CODE,G_CADRE.CADRE_NAME,POST,POST_GROUP,PAR_MASTER.OFF_CODE,G_OFFICE.off_en,get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname, "
                        + "getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,period_from,period_to FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER PAR_MASTER WHERE FISCAL_YEAR=? and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE where emp_mast.EMP_ID =? LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getSearchString());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
                /*empid wise with financial Year*/
            } else if (parAdminSearchCriteria.getSearchCriteria().equals("empid") && !parAdminSearchCriteria.getFiscalyear().equals("all")) {
                pst = con.prepareStatement("SELECT count(*) cnt FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER PAR_MASTER WHERE FISCAL_YEAR=? and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE where emp_mast.EMP_ID =?");
                pst.setString(1, parAdminSearchCriteria.getSearchString());
                pst.setString(2, parAdminSearchCriteria.getFiscalyear());
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate,parid,par_status,FISCAL_YEAR,GPF_NO,DOB,PAR_MASTER.EMP_ID,F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE, "
                        + "PAR_MASTER.CADRE_CODE,G_CADRE.CADRE_NAME,POST,POST_GROUP,PAR_MASTER.OFF_CODE,G_OFFICE.off_en,get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname, "
                        + "getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,period_from,period_to FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER PAR_MASTER WHERE FISCAL_YEAR=? and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE where emp_mast.EMP_ID =? LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
                pst.setString(3, parAdminSearchCriteria.getSearchString());
            } else if (parAdminSearchCriteria.getSearchCriteria().equals("empid") && parAdminSearchCriteria.getFiscalyear().equals("all")) {
                pst = con.prepareStatement("SELECT count(*) cnt FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER PAR_MASTER where (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE where emp_mast.EMP_ID =?");

                pst.setString(1, parAdminSearchCriteria.getPrivilegedSpc());
                pst.setString(2, parAdminSearchCriteria.getSearchString());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate,parid,par_status,FISCAL_YEAR,GPF_NO,DOB,PAR_MASTER.EMP_ID,F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE, "
                        + "PAR_MASTER.CADRE_CODE,G_CADRE.CADRE_NAME,POST,POST_GROUP,PAR_MASTER.OFF_CODE,G_OFFICE.off_en,get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname, "
                        + "getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,period_from,period_to FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER PAR_MASTER where (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE where emp_mast.EMP_ID =? order by FISCAL_YEAR desc LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getPrivilegedSpc());
                pst.setString(2, parAdminSearchCriteria.getSearchString());
                /*gpf numberwise with all financial year*/
            } else if (parAdminSearchCriteria.getSearchCriteria().equals("gpfno") && !parAdminSearchCriteria.getFiscalyear().equals("all")) {
                pst = con.prepareStatement("SELECT count(*) cnt FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER PAR_MASTER WHERE FISCAL_YEAR=? and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE where emp_mast.gpf_no =?");

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
                pst.setString(3, StringUtils.upperCase(parAdminSearchCriteria.getSearchString()));
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate,parid,par_status,FISCAL_YEAR,GPF_NO,DOB,PAR_MASTER.EMP_ID,F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE, "
                        + "PAR_MASTER.CADRE_CODE,G_CADRE.CADRE_NAME,POST,POST_GROUP,PAR_MASTER.OFF_CODE,G_OFFICE.off_en,get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname, "
                        + "getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,period_from,period_to FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER PAR_MASTER WHERE FISCAL_YEAR=? and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE where emp_mast.gpf_no =? LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
                pst.setString(3, StringUtils.upperCase(parAdminSearchCriteria.getSearchString()));
                /*gpfno wise with all financial year*/
            } else if (parAdminSearchCriteria.getSearchCriteria().equals("gpfno") && parAdminSearchCriteria.getFiscalyear().equals("all")) {
                pst = con.prepareStatement("SELECT count(*) cnt FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER where (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE where emp_mast.gpf_no =?");

                pst.setString(1, parAdminSearchCriteria.getPrivilegedSpc());
                pst.setString(2, StringUtils.upperCase(parAdminSearchCriteria.getSearchString()));
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate,parid,par_status,FISCAL_YEAR,GPF_NO,DOB,PAR_MASTER.EMP_ID,F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE, "
                        + "PAR_MASTER.CADRE_CODE,G_CADRE.CADRE_NAME,POST,POST_GROUP,PAR_MASTER.OFF_CODE,G_OFFICE.off_en,get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname, "
                        + "getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,period_from,period_to FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER where (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE where emp_mast.gpf_no =? order by FISCAL_YEAR desc LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getPrivilegedSpc());
                pst.setString(2, StringUtils.upperCase(parAdminSearchCriteria.getSearchString()));
                /*empname with specific financial year*/
            } else if (parAdminSearchCriteria.getSearchCriteria().equals("empname") && !parAdminSearchCriteria.getFiscalyear().equals("all")) {
                pst = con.prepareStatement("SELECT count(*) cnt FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER PAR_MASTER WHERE FISCAL_YEAR=? and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE where emp_mast.f_name =?");
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
                pst.setString(3, StringUtils.upperCase(parAdminSearchCriteria.getSearchString()));
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate,parid,par_status,FISCAL_YEAR,GPF_NO,DOB,PAR_MASTER.EMP_ID,F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE, "
                        + "PAR_MASTER.CADRE_CODE,G_CADRE.CADRE_NAME,POST,POST_GROUP,PAR_MASTER.OFF_CODE,G_OFFICE.off_en,get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname, "
                        + "getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,period_from,period_to FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER PAR_MASTER WHERE FISCAL_YEAR=? and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE where emp_mast.f_name =? LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
                pst.setString(3, StringUtils.upperCase(parAdminSearchCriteria.getSearchString()));
                /*empname with all financial year*/
            } else if (parAdminSearchCriteria.getSearchCriteria().equals("empname") && parAdminSearchCriteria.getFiscalyear().equals("all")) {
                pst = con.prepareStatement("SELECT count(*) cnt FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER where (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE where emp_mast.f_name =?");

                pst.setString(1, parAdminSearchCriteria.getPrivilegedSpc());
                pst.setString(2, StringUtils.upperCase(parAdminSearchCriteria.getSearchString()));
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate,parid,par_status,FISCAL_YEAR,GPF_NO,DOB,PAR_MASTER.EMP_ID,F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE, "
                        + "PAR_MASTER.CADRE_CODE,G_CADRE.CADRE_NAME,POST,POST_GROUP,PAR_MASTER.OFF_CODE,G_OFFICE.off_en,get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname, "
                        + "getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,period_from,period_to FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER where (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE where emp_mast.f_name =? order by FISCAL_YEAR desc LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getPrivilegedSpc());
                pst.setString(2, StringUtils.upperCase(parAdminSearchCriteria.getSearchString()));
                /*last name with specific financial year*/
            } else if (parAdminSearchCriteria.getSearchCriteria().equals("lastname") && !parAdminSearchCriteria.getFiscalyear().equals("all")) {
                pst = con.prepareStatement("SELECT count(*) cnt FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER PAR_MASTER WHERE FISCAL_YEAR=? and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE where emp_mast.l_name =?");

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
                pst.setString(3, parAdminSearchCriteria.getSearchString());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate,parid,par_status,FISCAL_YEAR,GPF_NO,DOB,PAR_MASTER.EMP_ID,F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE, "
                        + "PAR_MASTER.CADRE_CODE,G_CADRE.CADRE_NAME,POST,POST_GROUP,PAR_MASTER.OFF_CODE,G_OFFICE.off_en,get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname, "
                        + "getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,period_from,period_to FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER PAR_MASTER WHERE FISCAL_YEAR=? and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE where emp_mast.l_name =? LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
                pst.setString(3, parAdminSearchCriteria.getSearchString());
            } else if (parAdminSearchCriteria.getSearchCriteria().equals("lastname") && parAdminSearchCriteria.getFiscalyear().equals("all")) {
                pst = con.prepareStatement("SELECT count(*) cnt FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER where (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE where emp_mast.L_name =?");

                pst.setString(1, parAdminSearchCriteria.getPrivilegedSpc());
                pst.setString(2, parAdminSearchCriteria.getSearchString());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate,parid,par_status,FISCAL_YEAR,GPF_NO,DOB,PAR_MASTER.EMP_ID,F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE, "
                        + "PAR_MASTER.CADRE_CODE,G_CADRE.CADRE_NAME,POST,POST_GROUP,PAR_MASTER.OFF_CODE,G_OFFICE.off_en,get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname, "
                        + "getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,period_from,period_to FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER where (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE where emp_mast.L_name =? ORDER BY FISCAL_YEAR LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getPrivilegedSpc());
                pst.setString(2, parAdminSearchCriteria.getSearchString());
            } else if (parAdminSearchCriteria.getSearchCriteria().equals("dob") && !parAdminSearchCriteria.getFiscalyear().equals("all")) {
                pst = con.prepareStatement("SELECT count(*) cnt FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER PAR_MASTER WHERE FISCAL_YEAR=? and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE where emp_mast.dob= to_date(?,'DD-MM-yyyy')");

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
                pst.setString(3, parAdminSearchCriteria.getSearchString());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate,parid,par_status,FISCAL_YEAR,GPF_NO,DOB,PAR_MASTER.EMP_ID,F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE, "
                        + "PAR_MASTER.CADRE_CODE,G_CADRE.CADRE_NAME,POST,POST_GROUP,PAR_MASTER.OFF_CODE,G_OFFICE.off_en,get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname, "
                        + "getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,period_from,period_to FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER WHERE FISCAL_YEAR=? and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE where emp_mast.dob= to_date(?,'DD-MM-yyyy') LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
                pst.setString(3, parAdminSearchCriteria.getSearchString());
                /*dob wise with all financial year*/
            } else if (parAdminSearchCriteria.getSearchCriteria().equals("dob") && parAdminSearchCriteria.getFiscalyear().equals("all")) {
                pst = con.prepareStatement("SELECT count(*) cnt FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER where (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE where emp_mast.dob= to_date(?,'DD-MM-yyyy')");

                pst.setString(1, parAdminSearchCriteria.getPrivilegedSpc());
                pst.setString(2, parAdminSearchCriteria.getSearchString());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate,parid,par_status,FISCAL_YEAR,GPF_NO,DOB,PAR_MASTER.EMP_ID,F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE, "
                        + "PAR_MASTER.CADRE_CODE,G_CADRE.CADRE_NAME,POST,POST_GROUP,PAR_MASTER.OFF_CODE,G_OFFICE.off_en,get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname, "
                        + "getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,period_from,period_to FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER where (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE where emp_mast.dob= to_date(?,'DD-MM-yyyy') ORDER BY FISCAL_YEAR LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getPrivilegedSpc());
                pst.setString(2, parAdminSearchCriteria.getSearchString());
            } else if (((parAdminSearchCriteria.getSearchCriteria() == null || parAdminSearchCriteria.getSearchCriteria().equals("")) && (parAdminSearchCriteria.getCadrecode() == null || parAdminSearchCriteria.getCadrecode().equals("") && parAdminSearchCriteria.getSearchParStatus() == null || parAdminSearchCriteria.getSearchParStatus().equals(""))) && (parAdminSearchCriteria.getSearchCriteria1().equals("reviewed"))) {
                pst = con.prepareStatement("SELECT count(*) cnt FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER WHERE FISCAL_YEAR=? and is_reviewed='Y' and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE");
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate,parid,par_status,FISCAL_YEAR,GPF_NO,DOB,PAR_MASTER.EMP_ID,F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE, "
                        + "PAR_MASTER.CADRE_CODE,G_CADRE.CADRE_NAME,POST,POST_GROUP,PAR_MASTER.OFF_CODE,G_OFFICE.off_en,get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname, "
                        + "getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,period_from,period_to FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER PAR_MASTER WHERE FISCAL_YEAR=? and is_reviewed='Y' and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
            } else if ((parAdminSearchCriteria.getSearchCriteria() == null || parAdminSearchCriteria.getSearchCriteria().equals("")) && (parAdminSearchCriteria.getCadrecode() == null || parAdminSearchCriteria.getCadrecode().equals("")) && (parAdminSearchCriteria.getSearchParStatus() != null && parAdminSearchCriteria.getSearchCriteria1().equals("reviewed"))) {
                pst = con.prepareStatement("SELECT count(*) cnt FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER PAR_MASTER WHERE FISCAL_YEAR=? and par_status=? AND IS_REVIEWED='Y' and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE");
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setInt(2, Integer.parseInt(parAdminSearchCriteria.getSearchParStatus()));
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate,parid,par_status,FISCAL_YEAR,GPF_NO,DOB,PAR_MASTER.EMP_ID,F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE, "
                        + "PAR_MASTER.CADRE_CODE,G_CADRE.CADRE_NAME,POST,POST_GROUP,PAR_MASTER.OFF_CODE,G_OFFICE.off_en,get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname, "
                        + "getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,period_from,period_to FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER WHERE FISCAL_YEAR=? and par_status=? AND IS_REVIEWED='Y' and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setInt(2, Integer.parseInt(parAdminSearchCriteria.getSearchParStatus()));
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
            } else if ((parAdminSearchCriteria.getSearchCriteria() == null || parAdminSearchCriteria.getSearchCriteria().equals("")) && (parAdminSearchCriteria.getSearchParStatus() == null || parAdminSearchCriteria.getSearchParStatus().equals("")) && (parAdminSearchCriteria.getCadrecode() != null && parAdminSearchCriteria.getSearchCriteria1().equals("reviewed"))) {
                pst = con.prepareStatement("SELECT count(*) cnt FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and is_reviewed='Y' and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE");
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getCadrecode());
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate,parid,par_status,FISCAL_YEAR,GPF_NO,DOB,PAR_MASTER.EMP_ID,F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE, "
                        + "PAR_MASTER.CADRE_CODE,G_CADRE.CADRE_NAME,POST,POST_GROUP,PAR_MASTER.OFF_CODE,G_OFFICE.off_en,get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname, "
                        + "getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,period_from,period_to FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and is_reviewed='Y' and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getCadrecode());
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
            } else if ((parAdminSearchCriteria.getSearchCriteria() == null || parAdminSearchCriteria.getSearchCriteria().equals("")) && (parAdminSearchCriteria.getSearchCriteria1().equals("reviewed")) && (parAdminSearchCriteria.getCadrecode() != null && parAdminSearchCriteria.getSearchParStatus() != null)) {
                pst = con.prepareStatement("SELECT count(*) cnt FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and par_status=? and is_reviewed='Y' and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE");
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getCadrecode());
                pst.setInt(3, Integer.parseInt(parAdminSearchCriteria.getSearchParStatus()));
                pst.setString(4, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }
                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate,parid,par_status,FISCAL_YEAR,GPF_NO,DOB,PAR_MASTER.EMP_ID,F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE, "
                        + "PAR_MASTER.CADRE_CODE,G_CADRE.CADRE_NAME,POST,POST_GROUP,PAR_MASTER.OFF_CODE,G_OFFICE.off_en,get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname, "
                        + "getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,period_from,period_to FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and par_status=? and is_reviewed='Y' and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getCadrecode());
                pst.setInt(3, Integer.parseInt(parAdminSearchCriteria.getSearchParStatus()));
                pst.setString(4, parAdminSearchCriteria.getPrivilegedSpc());
                /*Non Reviewed PAR with Financial Year*/
            } else if (((parAdminSearchCriteria.getSearchCriteria() == null || parAdminSearchCriteria.getSearchCriteria().equals("")) && (parAdminSearchCriteria.getCadrecode() == null || parAdminSearchCriteria.getCadrecode().equals("") && parAdminSearchCriteria.getSearchParStatus() == null || parAdminSearchCriteria.getSearchParStatus().equals(""))) && (parAdminSearchCriteria.getSearchCriteria1().equals("nonreviewed"))) {
                pst = con.prepareStatement("SELECT count(*) cnt FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER WHERE FISCAL_YEAR=? AND is_reviewed = 'N' and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE ");
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate,parid,par_status,FISCAL_YEAR,GPF_NO,DOB,PAR_MASTER.EMP_ID,F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE, "
                        + "PAR_MASTER.CADRE_CODE,G_CADRE.CADRE_NAME,POST,POST_GROUP,PAR_MASTER.OFF_CODE,G_OFFICE.off_en,get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname, "
                        + "getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,period_from,period_to FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER WHERE FISCAL_YEAR=? AND is_reviewed = 'N' and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
                /*Financial Year,PAR Status and Non Reviewed wise*/
            } else if ((parAdminSearchCriteria.getSearchCriteria() == null || parAdminSearchCriteria.getSearchCriteria().equals("")) && (parAdminSearchCriteria.getCadrecode() == null || parAdminSearchCriteria.getCadrecode().equals("")) && (parAdminSearchCriteria.getSearchParStatus() != null && parAdminSearchCriteria.getSearchCriteria1().equals("nonreviewed"))) {
                pst = con.prepareStatement("SELECT count(*) cnt FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER WHERE FISCAL_YEAR=? and par_status=? and is_reviewed='N' and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE ");
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setInt(2, Integer.parseInt(parAdminSearchCriteria.getSearchParStatus()));
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate,parid,par_status,FISCAL_YEAR,GPF_NO,DOB,PAR_MASTER.EMP_ID,F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE, "
                        + "PAR_MASTER.CADRE_CODE,G_CADRE.CADRE_NAME,POST,POST_GROUP,PAR_MASTER.OFF_CODE,G_OFFICE.off_en,get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname, "
                        + "getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,period_from,period_to FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER WHERE FISCAL_YEAR=? and par_status=? and is_reviewed='N' and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setInt(2, Integer.parseInt(parAdminSearchCriteria.getSearchParStatus()));
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
                /*Financial Year,Cadre code and Non Review wise*/
            } else if ((parAdminSearchCriteria.getSearchCriteria() == null || parAdminSearchCriteria.getSearchCriteria().equals("")) && (parAdminSearchCriteria.getSearchParStatus() == null || parAdminSearchCriteria.getSearchParStatus().equals("")) && (parAdminSearchCriteria.getCadrecode() != null && parAdminSearchCriteria.getSearchCriteria1().equals("nonreviewed"))) {
                pst = con.prepareStatement("SELECT count(*) cnt FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and is_reviewed='N' and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE");
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getCadrecode());
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate,parid,par_status,FISCAL_YEAR,GPF_NO,DOB,PAR_MASTER.EMP_ID,F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE, "
                        + "PAR_MASTER.CADRE_CODE,G_CADRE.CADRE_NAME,POST,POST_GROUP,PAR_MASTER.OFF_CODE,G_OFFICE.off_en,get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname, "
                        + "getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,period_from,period_to FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and is_reviewed='N' and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getCadrecode());
                pst.setString(3, parAdminSearchCriteria.getPrivilegedSpc());
                /*Fiscalyear,Non Reviewed,Cadrecode and ParStatus wise*/
            } else if ((parAdminSearchCriteria.getSearchCriteria() == null || parAdminSearchCriteria.getSearchCriteria().equals("")) && (parAdminSearchCriteria.getSearchCriteria1().equals("nonreviewed")) && (parAdminSearchCriteria.getCadrecode() != null && parAdminSearchCriteria.getSearchParStatus() != null)) {
                pst = con.prepareStatement("SELECT count(*) cnt FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER where FISCAL_YEAR=? and cadre_code=? and par_status=? and is_reviewed='N' and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE");
                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getCadrecode());
                pst.setInt(3, Integer.parseInt(parAdminSearchCriteria.getSearchParStatus()));
                pst.setString(4, parAdminSearchCriteria.getPrivilegedSpc());
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }
                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate,parid,par_status,FISCAL_YEAR,GPF_NO,DOB,PAR_MASTER.EMP_ID,F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE, "
                        + "PAR_MASTER.CADRE_CODE,G_CADRE.CADRE_NAME,POST,POST_GROUP,PAR_MASTER.OFF_CODE,G_OFFICE.off_en,get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname, "
                        + "getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,period_from,period_to FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER where FISCAL_YEAR=? and cadre_code=? and par_status=? and is_reviewed='N' and (par_type is null or par_type = ''))PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, parAdminSearchCriteria.getFiscalyear());
                pst.setString(2, parAdminSearchCriteria.getCadrecode());
                pst.setInt(3, Integer.parseInt(parAdminSearchCriteria.getSearchParStatus()));
                pst.setString(4, parAdminSearchCriteria.getPrivilegedSpc());
            }

            res = pst.executeQuery();
            int count = 0;
            while (res.next()) {
                count++;
                ParAdminProperties parAdminProperties = new ParAdminProperties();
                parAdminProperties.setEmpId(res.getString("EMP_ID"));
                String fname = res.getString("F_NAME").trim();
                String mname = "";
                String lname = "";
                String parPendingFromDate = "";

                if (res.getString("M_NAME") != null) {
                    mname = res.getString("M_NAME").trim();
                } else {
                    mname = "";
                }
                if (res.getString("L_NAME") != null && !res.getString("L_NAME").equals("")) {
                    lname = res.getString("L_NAME").trim();
                }
                parAdminProperties.setPageNo(((parAdminSearchCriteria.getPage() - 1) * 20 + count) + "");
                parAdminProperties.setEmpName(fname + " " + mname + " " + lname);
                // parAdminProperties.setDob(res.getString("DOB"));
                parAdminProperties.setDob(CommonFunctions.getFormattedOutputDate6(res.getDate("DOB")));
                if (res.getString("FISCAL_YEAR") != null && !res.getString("FISCAL_YEAR").equals("")) {
                    parAdminProperties.setFiscalyear(res.getString("FISCAL_YEAR"));
                } else {
                    parAdminProperties.setFiscalyear("");
                }
                parAdminProperties.setGpfno(res.getString("GPF_NO"));
                if (res.getString("CADRE_NAME") != null && !res.getString("CADRE_NAME").equals("")) {
                    parAdminProperties.setCadreName(res.getString("CADRE_NAME"));
                } else {
                    parAdminProperties.setCadreName("");
                }
                parAdminProperties.setCurrentoffice(res.getString("OFF_EN"));
                parAdminProperties.setGroupName(StringUtils.defaultString(res.getString("POST_GROUP"), ""));
                parAdminProperties.setPostName(StringUtils.defaultString(res.getString("POST"), ""));
                parAdminProperties.setMobile(StringUtils.defaultString(res.getString("MOBILE"), ""));
                parAdminProperties.setParId(res.getInt("PARID"));
                //parAdminProperties.setFiscalyear(res.getString("FISCAL_YEAR"));
                if (res.getString("PAR_STATUS") != null && !res.getString("PAR_STATUS").equals("")) {
                    if (res.getInt("PAR_STATUS") == 0) {
                        parAdminProperties.setParstatus("PAR CREATED BUT NOT SUBMITTED");
                    } else {
                        parAdminProperties.setParstatus(getStatusName(con, res.getString("PAR_STATUS")));
                    }
                } else {
                    parAdminProperties.setParstatus("PAR NOT CREATED");
                }
                if (res.getString("pendingauthorityname") != null && !res.getString("pendingauthorityname").equals("")) {
                    parAdminProperties.setPendingAtAuthName(res.getString("pendingauthorityname"));
                } else {
                    parAdminProperties.setPendingAtAuthName("");
                }
                if (res.getString("pendingauthorityspc") != null && !res.getString("pendingauthorityspc").equals("")) {
                    parAdminProperties.setPendingAtSpc(res.getString("pendingauthorityspc"));
                } else {
                    parAdminProperties.setPendingAtSpc("");
                }
                if (res.getString("PENDING_AT") != null && !res.getString("PENDING_AT").equals("")) {
                    parAdminProperties.setPendingAtEmpId(res.getString("PENDING_AT"));
                } else {
                    parAdminProperties.setPendingAtEmpId("");
                }
                if (res.getString("PERIOD_FROM") != null && !res.getString("PERIOD_FROM").equals("")) {
                    parAdminProperties.setPrdFrmDate(CommonFunctions.getFormattedOutputDate1(res.getDate("PERIOD_FROM")));
                } else {
                    parAdminProperties.setPrdFrmDate("");
                }
                if (res.getString("PERIOD_TO") != null && !res.getString("PERIOD_TO").equals("")) {
                    parAdminProperties.setPrdToDate(CommonFunctions.getFormattedOutputDate1(res.getDate("PERIOD_TO")));
                } else {
                    parAdminProperties.setPrdToDate("");
                }
                if (res.getString("pendingparDate") != null && !res.getString("pendingparDate").equals("")) {
                    parAdminProperties.setParPendingDateFrom(CommonFunctions.getFormattedOutputDate6(res.getDate("pendingparDate")));
                    // parAdminProperties.setParPendingDateFrom(res.getString("pendingparDate"));
                } else {
                    parAdminProperties.setParPendingDateFrom("");
                }

                parList.add(parAdminProperties);
            }

            parSearchResult.setParlist(parList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parSearchResult;
    }

    @Override
    public boolean hasCadrePrivilege(String privilegedSpc) {
        boolean cadrePrivelege = false;
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select count(*) as cnt FROM par_authority_admin WHERE SPC=?");
            pst.setString(1, privilegedSpc);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    cadrePrivelege = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return cadrePrivelege;
    }

    @Override
    public String[] getAssignedAdditionalPost(String empId) {
        Connection con = null;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs = null;
        String[] assignedAdditionalpost = new String[1];
        try {
            con = this.repodataSource.getConnection();
            String sql = "select SPC from emp_join where emp_id=? AND IF_AD_CHARGE='Y'";
            pst = con.prepareStatement(sql);
            pst.setString(1, empId);
            rs = pst.executeQuery();
            if (rs.next()) {
                assignedAdditionalpost[0] = rs.getString("SPC");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return assignedAdditionalpost;
    }

    @Override
    public PARSearchResult getPARList(String privilegedSpc, String fiscalYear, String cadrecode, String searchCriteria, String searchParStatus, String searchString, String searchCriteria1, int noofrows, int page) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet res = null;
        ArrayList parList = new ArrayList();
        PARSearchResult parSearchResult = new PARSearchResult();
        try {
            parSearchResult.setTotalPARFound(0);
            con = repodataSource.getConnection();
            int offSet = noofrows * (page - 1);
            int limit = noofrows;
            String parstatus = "";

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            Long curtime = new Date().getTime();
            /*Search only by fiscal year*/
            if ((searchCriteria == null || searchCriteria.equals("")) && (searchCriteria1 == null || searchCriteria1.equals("")) && (cadrecode == null || cadrecode.equals("")) && (searchParStatus == null || searchParStatus.equals("")) && !fiscalYear.equals("all")) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,INITIATEDBY,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE FISCAL_YEAR=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, fiscalYear);
                pst.setString(2, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT INITIATEDBY,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_ADVERSED FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE FISCAL_YEAR=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, fiscalYear);
                pst.setString(2, privilegedSpc);
                /*Search by fiscal year and cadre code*/
            } else if ((searchCriteria == null || searchCriteria.equals("")) && (searchCriteria1 == null || searchCriteria1.equals("")) && cadrecode != null && (searchParStatus == null || searchParStatus.equals(""))) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE  FISCAL_YEAR=? and cadre_code=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, fiscalYear);
                pst.setString(2, cadrecode);
                pst.setString(3, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT INITIATEDBY,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_ADVERSED FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, fiscalYear);
                pst.setString(2, cadrecode);
                pst.setString(3, privilegedSpc);
                /*Search by fiscal year and parstatus*/
            } else if ((searchCriteria == null || searchCriteria.equals("")) && (searchCriteria1 == null || searchCriteria1.equals("")) && (cadrecode == null || cadrecode.equals("")) && searchParStatus != null && !searchParStatus.equals("117")) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE  FISCAL_YEAR=? and par_status=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE ");
                pst.setString(1, fiscalYear);
                pst.setInt(2, Integer.parseInt(searchParStatus));
                pst.setString(3, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT INITIATEDBY,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_ADVERSED FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE FISCAL_YEAR=? and par_status=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, fiscalYear);
                pst.setInt(2, Integer.parseInt(searchParStatus));
                pst.setString(3, privilegedSpc);

                /*Search by fiscal year,parstatus 117*/
            } else if ((searchCriteria == null || searchCriteria.equals("")) && (searchCriteria1 == null || searchCriteria1.equals("")) && (cadrecode == null || cadrecode.equals("")) && searchParStatus.equals("117")) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT PARID,PAR_MASTER.CADRE_CODE,INITIATEDBY,PAR_EMP_ID,EMP_ID,EMP_MAST.CUR_CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,g_cadre.CADRE_NAME,POST,POST_GROUP,GPF_NO,DOB,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_ADVERSED FROM EMP_MAST "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?) par_authority_admin ON EMP_MAST.CUR_CADRE_CODE = par_authority_admin.CADRE_CODE AND EMP_MAST.POST_GRP_TYPE = par_authority_admin.POST_GRP  "
                        + "LEFT OUTER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,PAR_STATUS,EMP_ID AS PAR_EMP_ID,OFF_CODE,POST_GROUP,CADRE_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE FISCAL_YEAR=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID = PAR_MASTER.PAR_EMP_ID "
                        + "LEFT OUTER JOIN G_CADRE ON EMP_MAST.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC  "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE WHERE PAR_EMP_ID IS NULL)temp ");
                pst.setString(1, privilegedSpc);
                pst.setString(2, fiscalYear);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT FISCAL_YEAR,PARID,PAR_MASTER.CADRE_CODE,INITIATEDBY,PAR_EMP_ID,EMP_ID,EMP_MAST.CUR_CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,g_cadre.CADRE_NAME,POST,POST_GROUP,GPF_NO,DOB,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_ADVERSED FROM EMP_MAST "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?) par_authority_admin ON EMP_MAST.CUR_CADRE_CODE = par_authority_admin.CADRE_CODE AND EMP_MAST.POST_GRP_TYPE = par_authority_admin.POST_GRP  "
                        + "LEFT OUTER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,PAR_STATUS,EMP_ID AS PAR_EMP_ID,OFF_CODE,POST_GROUP,CADRE_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE FISCAL_YEAR=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID = PAR_MASTER.PAR_EMP_ID "
                        + "LEFT OUTER JOIN G_CADRE ON EMP_MAST.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC  "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE WHERE PAR_EMP_ID IS NULL order by f_name LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, privilegedSpc);
                pst.setString(2, fiscalYear);


                /*Search by fiscal year,Cadre wise and parstatus*/
            } else if ((searchCriteria == null || searchCriteria.equals("")) && (searchCriteria1 == null || searchCriteria1.equals("")) && (cadrecode != null && searchParStatus != null)) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE  FISCAL_YEAR=? and cadre_code=? and par_status=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, fiscalYear);
                pst.setString(2, cadrecode);
                pst.setInt(3, Integer.parseInt(searchParStatus));
                pst.setString(4, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT INITIATEDBY,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_ADVERSED FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and par_status=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, fiscalYear);
                pst.setString(2, cadrecode);
                pst.setInt(3, Integer.parseInt(searchParStatus));
                pst.setString(4, privilegedSpc);

                /*Search by empid*/
            } else if (searchCriteria.equals("empid") && !fiscalYear.equals("all") && (searchCriteria1 == null || searchCriteria.equals("") && searchParStatus == null || searchParStatus.equals("") && cadrecode == null || cadrecode.equals(""))) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE EMP_ID = ?)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE FISCAL_YEAR=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, searchString);
                pst.setString(2, fiscalYear);
                pst.setString(3, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT INITIATEDBY,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_ADVERSED FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE EMP_ID = ?)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE FISCAL_YEAR=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, searchString);
                pst.setString(2, fiscalYear);
                pst.setString(3, privilegedSpc);
            } else if (searchCriteria.equals("empid") && fiscalYear.equals("all")) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE EMP_ID = ?)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER where is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, searchString);
                pst.setString(2, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT INITIATEDBY,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_ADVERSED FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE EMP_ID = ?)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER where is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE ORDER BY FISCAL_YEAR desc LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, searchString);
                pst.setString(2, privilegedSpc);
            } else if (searchCriteria.equals("gpfno") && !fiscalYear.equals("all") && (searchCriteria1 == null || searchCriteria.equals("") && searchParStatus == null || searchParStatus.equals("") && cadrecode == null || cadrecode.equals(""))) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE GPF_NO = ?)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE FISCAL_YEAR=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, StringUtils.upperCase(searchString));
                pst.setString(2, fiscalYear);
                pst.setString(3, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT INITIATEDBY,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_ADVERSED FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE GPF_NO = ?)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE FISCAL_YEAR=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, StringUtils.upperCase(searchString));
                pst.setString(2, fiscalYear);
                pst.setString(3, privilegedSpc);
            } else if (searchCriteria.equals("gpfno") && fiscalYear.equals("all")) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE GPF_NO = ?)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER where is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, StringUtils.upperCase(searchString));
                pst.setString(2, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT INITIATEDBY,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_ADVERSED FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE GPF_NO = ?)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER where is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE ORDER BY FISCAL_YEAR desc LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, StringUtils.upperCase(searchString));
                pst.setString(2, privilegedSpc);
            } else if (searchCriteria.equals("empname") && !fiscalYear.equals("all") && (searchCriteria1 == null || searchCriteria.equals("") && searchParStatus == null || searchParStatus.equals("") && cadrecode == null || cadrecode.equals(""))) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE normalize_name(F_NAME) = ?)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE FISCAL_YEAR=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, StringUtils.upperCase(searchString));
                pst.setString(2, fiscalYear);
                pst.setString(3, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT INITIATEDBY,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_ADVERSED FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE normalize_name(F_NAME) = ?)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE FISCAL_YEAR=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, StringUtils.upperCase(searchString));
                pst.setString(2, fiscalYear);
                pst.setString(3, privilegedSpc);

            } else if (searchCriteria.equals("empname") && fiscalYear.equals("all")) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE normalize_name(F_NAME) = ?)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER where is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, StringUtils.upperCase(searchString));
                pst.setString(2, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT INITIATEDBY,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_ADVERSED FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE normalize_name(F_NAME) = ?)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER where is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE ORDER BY FISCAL_YEAR desc LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, StringUtils.upperCase(searchString));
                pst.setString(2, privilegedSpc);
            } else if (searchCriteria.equals("lastname") && !fiscalYear.equals("all") && (searchCriteria1 == null || searchCriteria.equals("") && searchParStatus == null || searchParStatus.equals("") && cadrecode == null || cadrecode.equals(""))) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE trim(L_NAME) = ?)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE FISCAL_YEAR=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, StringUtils.upperCase(searchString));
                pst.setString(2, fiscalYear);
                pst.setString(3, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT INITIATEDBY,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_ADVERSED FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE trim(L_NAME) = ?)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE FISCAL_YEAR=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, StringUtils.upperCase(searchString));
                pst.setString(2, fiscalYear);
                pst.setString(3, privilegedSpc);
            } else if (searchCriteria.equals("lastname") && fiscalYear.equals("all")) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE trim(L_NAME) = ?)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER where is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, StringUtils.upperCase(searchString));
                pst.setString(2, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT INITIATEDBY,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_ADVERSED FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE trim(L_NAME) = ?)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER where is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE ORDER BY FISCAL_YEAR desc LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, StringUtils.upperCase(searchString));
                pst.setString(2, privilegedSpc);
            } else if (searchCriteria.equals("dob") && !fiscalYear.equals("all") && (searchCriteria1 == null || searchCriteria.equals("") && searchParStatus == null || searchParStatus.equals("") && cadrecode == null || cadrecode.equals(""))) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE dob= to_date(?,'DD-MM-yyyy'))EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE FISCAL_YEAR=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, searchString);
                pst.setString(2, fiscalYear);
                pst.setString(3, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT INITIATEDBY,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_adversed FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE dob= to_date(?,'DD-MM-yyyy'))EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER WHERE FISCAL_YEAR=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, searchString);
                pst.setString(2, fiscalYear);
                pst.setString(3, privilegedSpc);
            } else if (searchCriteria.equals("dob") && fiscalYear.equals("all")) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE dob= to_date(?,'DD-MM-yyyy'))EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER where is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, searchString);
                pst.setString(2, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT INITIATEDBY,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_adversed FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE dob= to_date(?,'DD-MM-yyyy'))EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER where is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE ORDER BY FISCAL_YEAR desc LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, searchString);
                pst.setString(2, privilegedSpc);
            } else if ((searchCriteria1 == null || searchCriteria.equals("") && searchParStatus == null || searchParStatus.equals("")) && (searchCriteria.equals("empid") && !fiscalYear.equals("all") && cadrecode != null)) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE EMP_ID = ?)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, searchString);
                pst.setString(2, fiscalYear);
                pst.setString(3, cadrecode);
                pst.setString(4, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT INITIATEDBY,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_ADVERSED FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE EMP_ID = ?)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, searchString);
                pst.setString(2, fiscalYear);
                pst.setString(3, cadrecode);
                pst.setString(4, privilegedSpc);
            } else if ((searchCriteria1 == null || searchCriteria.equals("") && searchParStatus == null || searchParStatus.equals("")) && (searchCriteria.equals("gpfno") && !fiscalYear.equals("all") && cadrecode != null)) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE gpf_no = ?)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, searchString);
                pst.setString(2, fiscalYear);
                pst.setString(3, cadrecode);
                pst.setString(4, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT INITIATEDBY,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_ADVERSED FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE gpf_no = ?)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, searchString);
                pst.setString(2, fiscalYear);
                pst.setString(3, cadrecode);
                pst.setString(4, privilegedSpc);
            } else if ((searchCriteria1 == null || searchCriteria.equals("") && searchParStatus == null || searchParStatus.equals("")) && (searchCriteria.equals("empname") && !fiscalYear.equals("all") && cadrecode != null)) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE normalize_name(F_NAME) = ?)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, StringUtils.upperCase(searchString));
                pst.setString(2, fiscalYear);
                pst.setString(3, cadrecode);
                pst.setString(4, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT INITIATEDBY,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_ADVERSED FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE normalize_name(F_NAME) = ?)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, StringUtils.upperCase(searchString));
                pst.setString(2, fiscalYear);
                pst.setString(3, cadrecode);
                pst.setString(4, privilegedSpc);
            } else if ((searchCriteria1 == null || searchCriteria.equals("") && searchParStatus == null || searchParStatus.equals("")) && (searchCriteria.equals("lastname") && !fiscalYear.equals("all") && cadrecode != null)) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE trim(L_NAME) = ?)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, StringUtils.upperCase(searchString));
                pst.setString(2, fiscalYear);
                pst.setString(3, cadrecode);
                pst.setString(4, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT INITIATEDBY,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_ADVERSED FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE trim(L_NAME) = ?)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, StringUtils.upperCase(searchString));
                pst.setString(2, fiscalYear);
                pst.setString(3, cadrecode);
                pst.setString(4, privilegedSpc);
            } else if ((searchCriteria1 == null || searchCriteria.equals("") && searchParStatus == null || searchParStatus.equals("")) && (searchCriteria.equals("dob") && !fiscalYear.equals("all") && cadrecode != null)) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE dob= to_date(?,'DD-MM-yyyy') = ?)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, searchString);
                pst.setString(2, fiscalYear);
                pst.setString(3, cadrecode);
                pst.setString(4, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT INITIATEDBY,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_ADVERSED FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST WHERE dob= to_date(?,'DD-MM-yyyy') = ?)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_ADVERSED FROM PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, searchString);
                pst.setString(2, fiscalYear);
                pst.setString(3, cadrecode);
                pst.setString(4, privilegedSpc);
            } else if ((searchCriteria == null || searchCriteria.equals("")) && (searchParStatus == null || searchParStatus.equals("")) && (cadrecode != null && !cadrecode.equals("") && searchCriteria1.equals("reviewed"))) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER WHERE  FISCAL_YEAR=? and cadre_code=? and is_reviewed='Y' and is_reviewed='Y' and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE ");
                pst.setString(1, fiscalYear);
                pst.setString(2, cadrecode);
                pst.setString(3, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT INITIATEDBY,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_adversed FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and is_reviewed='Y' and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, fiscalYear);
                pst.setString(2, cadrecode);
                pst.setString(3, privilegedSpc);
            } else if ((searchCriteria == null || searchCriteria.equals("")) && (searchParStatus == null || searchParStatus.equals("")) && (cadrecode != null && !cadrecode.equals("") && searchCriteria1.equals("nonreviewed"))) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER WHERE  FISCAL_YEAR=? and cadre_code=? and is_reviewed='N' and is_ADVERSED <> 'Y' and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE ");
                pst.setString(1, fiscalYear);
                pst.setString(2, cadrecode);
                pst.setString(3, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT INITIATEDBY,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_adversed FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and is_reviewed='N' and is_ADVERSED <> 'Y' and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, fiscalYear);
                pst.setString(2, cadrecode);
                pst.setString(3, privilegedSpc);
            } else if ((searchCriteria == null || searchCriteria.equals("")) && (searchParStatus == null || searchParStatus.equals("")) && (cadrecode != null && !cadrecode.equals("") && searchCriteria1.equals("adverse"))) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER WHERE  FISCAL_YEAR=? and cadre_code=? and is_adversed='Y' and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE ");
                pst.setString(1, fiscalYear);
                pst.setString(2, cadrecode);
                pst.setString(3, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT INITIATEDBY,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_adversed FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and is_adversed='Y' and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, fiscalYear);
                pst.setString(2, cadrecode);
                pst.setString(3, privilegedSpc);
            } else if (((searchCriteria == null || searchCriteria.equals("")) && (cadrecode == null || cadrecode.equals("") && searchParStatus == null || searchParStatus.equals(""))) && (searchCriteria1.equals("reviewed"))) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,INITIATEDBY,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER WHERE FISCAL_YEAR=? AND is_reviewed = 'Y' and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE  LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, fiscalYear);
                pst.setString(2, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT FISCAL_YEAR,INITIATEDBY,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_adversed FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,INITIATEDBY,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER WHERE FISCAL_YEAR=? AND is_reviewed = 'Y' and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, fiscalYear);
                pst.setString(2, privilegedSpc);
            } else if (((searchCriteria == null || searchCriteria.equals("")) && (cadrecode == null || cadrecode.equals("") && searchParStatus == null || searchParStatus.equals(""))) && (searchCriteria1.equals("nonreviewed"))) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,INITIATEDBY,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER WHERE FISCAL_YEAR=? AND is_reviewed = 'N' and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE  LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, fiscalYear);
                pst.setString(2, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT FISCAL_YEAR,INITIATEDBY,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_adversed FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,INITIATEDBY,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER WHERE FISCAL_YEAR=? AND is_reviewed = 'N' and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, fiscalYear);
                pst.setString(2, privilegedSpc);
            } else if (((searchCriteria == null || searchCriteria.equals("")) && (cadrecode == null || cadrecode.equals("") && searchParStatus == null || searchParStatus.equals(""))) && (searchCriteria1.equals("adverse"))) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,INITIATEDBY,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER WHERE FISCAL_YEAR=? AND is_adversed = 'Y' and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE  LIMIT " + limit + " OFFSET " + offSet);
                pst.setString(1, fiscalYear);
                pst.setString(2, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT FISCAL_YEAR,INITIATEDBY,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_adversed FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,INITIATEDBY,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER WHERE FISCAL_YEAR=? AND is_adversed = 'Y' and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, fiscalYear);
                pst.setString(2, privilegedSpc);
            } else if ((searchCriteria == null || searchCriteria.equals("")) && (cadrecode == null || cadrecode.equals("")) && (searchParStatus != null && searchCriteria1.equals("reviewed"))) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,INITIATEDBY,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER WHERE FISCAL_YEAR=? and par_status=? AND is_reviewed = 'Y' and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE  LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, fiscalYear);
                pst.setInt(2, Integer.parseInt(searchParStatus));
                pst.setString(3, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT INITIATEDBY,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_adversed FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER WHERE FISCAL_YEAR=? and par_status=? and is_reviewed='Y' and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, fiscalYear);
                pst.setInt(2, Integer.parseInt(searchParStatus));
                pst.setString(3, privilegedSpc);
            } else if ((searchCriteria == null || searchCriteria.equals("")) && (cadrecode == null || cadrecode.equals("")) && (searchParStatus != null && searchCriteria1.equals("adverse"))) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,INITIATEDBY,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER WHERE FISCAL_YEAR=? and par_status=? AND is_adversed = 'Y' and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE  LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, fiscalYear);
                pst.setInt(2, Integer.parseInt(searchParStatus));
                pst.setString(3, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT INITIATEDBY,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_adversed FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER WHERE FISCAL_YEAR=? and par_status=? and is_adversed='Y' and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, fiscalYear);
                pst.setInt(2, Integer.parseInt(searchParStatus));
                pst.setString(3, privilegedSpc);
            } else if ((searchCriteria == null || searchCriteria.equals("")) && (searchParStatus == null || searchParStatus.equals("")) && (cadrecode != null && searchCriteria1.equals("reviewed"))) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,is_reviewed FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER WHERE  FISCAL_YEAR=? and cadre_code=? and is_reviewed='Y' and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE ");
                pst.setString(1, fiscalYear);
                pst.setString(2, cadrecode);
                pst.setString(3, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT INITIATEDBY,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_adversed FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and is_reviewed='Y' and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, fiscalYear);
                pst.setString(2, cadrecode);
                pst.setString(3, privilegedSpc);
            } else if ((searchCriteria == null || searchCriteria.equals("")) && (searchCriteria1.equals("reviewed")) && (cadrecode != null && searchParStatus != null)) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER WHERE  FISCAL_YEAR=? and cadre_code=? and par_status=? and is_reviewed='Y' and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, fiscalYear);
                pst.setString(2, cadrecode);
                pst.setInt(3, Integer.parseInt(searchParStatus));
                pst.setString(4, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }
                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT FISCAL_YEAR,INITIATEDBY,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_adversed FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,INITIATEDBY,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and par_status=? and is_reviewed='Y' and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, fiscalYear);
                pst.setString(2, cadrecode);
                pst.setInt(3, Integer.parseInt(searchParStatus));
                pst.setString(4, privilegedSpc);
            } else if ((searchCriteria == null || searchCriteria.equals("")) && ((cadrecode != null && searchParStatus != null) && searchCriteria1.equals("nonreviewed"))) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER WHERE  FISCAL_YEAR=? and cadre_code=? and par_status=? and is_reviewed='N' and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, fiscalYear);
                pst.setString(2, cadrecode);
                pst.setInt(3, Integer.parseInt(searchParStatus));
                pst.setString(4, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }
                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT FISCAL_YEAR,INITIATEDBY,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_adversed FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,INITIATEDBY,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and par_status=? and is_reviewed='N' and is_adversed != 'Y' and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, fiscalYear);
                pst.setString(2, cadrecode);
                pst.setInt(3, Integer.parseInt(searchParStatus));
                pst.setString(4, privilegedSpc);
            } else if ((searchCriteria == null || searchCriteria.equals("")) && (searchCriteria1.equals("adverse")) && (cadrecode != null && searchParStatus != null)) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER WHERE  FISCAL_YEAR=? and cadre_code=? and par_status=? and is_adversed='Y' and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
                pst.setString(1, fiscalYear);
                pst.setString(2, cadrecode);
                pst.setInt(3, Integer.parseInt(searchParStatus));
                pst.setString(4, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }
                DataBaseFunctions.closeSqlObjects(res, pst);
                pst = con.prepareStatement("SELECT FISCAL_YEAR,INITIATEDBY,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_adversed FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,INITIATEDBY,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER WHERE FISCAL_YEAR=? and cadre_code=? and par_status=? and is_adversed='Y' and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, fiscalYear);
                pst.setString(2, cadrecode);
                pst.setInt(3, Integer.parseInt(searchParStatus));
                pst.setString(4, privilegedSpc);
            } else if ((searchCriteria == null || searchCriteria.equals("")) && (cadrecode == null || cadrecode.equals("")) && (searchParStatus != null && searchCriteria1.equals("nonreviewed"))) {
                pst = con.prepareStatement("SELECT COUNT(*) as CNT FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER WHERE  FISCAL_YEAR=? and par_status=? and is_reviewed='N' and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE ");
                pst.setString(1, fiscalYear);
                pst.setInt(2, Integer.parseInt(searchParStatus));
                pst.setString(3, privilegedSpc);
                res = pst.executeQuery();
                if (res.next()) {
                    parSearchResult.setTotalPARFound(res.getInt("CNT"));
                }

                DataBaseFunctions.closeSqlObjects(res, pst);

                pst = con.prepareStatement("SELECT INITIATEDBY,FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,is_reviewed,is_adversed FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT INITIATEDBY,FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,is_reviewed,is_adversed FROM PAR_MASTER WHERE FISCAL_YEAR=? and par_status=? and is_reviewed='N'and is_deleted='N')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP FROM par_authority_admin WHERE SPC=?)par_authority_admin ON PAR_MASTER.CADRE_CODE = par_authority_admin.CADRE_CODE AND PAR_MASTER.POST_GROUP = par_authority_admin.POST_GRP "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE order by f_name LIMIT " + limit + " OFFSET " + offSet);

                pst.setString(1, fiscalYear);
                pst.setInt(2, Integer.parseInt(searchParStatus));
                pst.setString(3, privilegedSpc);
            }

            res = pst.executeQuery();
            int count = 0;
            while (res.next()) {
                count++;
                ParAdminProperties parAdminProperties = new ParAdminProperties();
                parAdminProperties.setEmpId(res.getString("EMP_ID"));
                String fname = res.getString("F_NAME").trim();
                String mname = "";
                String lname = "";

                if (res.getString("M_NAME") != null) {
                    mname = res.getString("M_NAME").trim();
                } else {
                    mname = "";
                }
                if (res.getString("L_NAME") != null && !res.getString("L_NAME").equals("")) {
                    lname = res.getString("L_NAME").trim();
                }
                parAdminProperties.setPageNo(((page - 1) * 20 + count) + "");
                parAdminProperties.setEmpName(fname + " " + mname + " " + lname);
                // parAdminProperties.setDob(res.getString("DOB"));
                parAdminProperties.setDob(CommonFunctions.getFormattedOutputDate6(res.getDate("DOB")));
                parAdminProperties.setGpfno(res.getString("GPF_NO"));
                parAdminProperties.setFiscalyear(res.getString("FISCAL_YEAR"));
                parAdminProperties.setCadreName(res.getString("CADRE_NAME"));
                parAdminProperties.setCurrentoffice(res.getString("OFF_CODE"));
                parAdminProperties.setGroupName(StringUtils.defaultString(res.getString("POST_GROUP"), ""));
                parAdminProperties.setPostName(StringUtils.defaultString(res.getString("POST"), ""));
                parAdminProperties.setMobile(StringUtils.defaultString(res.getString("MOBILE"), ""));
                parAdminProperties.setIsreview(res.getString("is_reviewed"));
                parAdminProperties.setIsadversed(res.getString("is_adversed"));
                if (res.getString("INITIATEDBY") != null && !res.getString("INITIATEDBY").equals("")) {
                    parAdminProperties.setInitiatedByEmpId(res.getString("INITIATEDBY"));
                } else {
                    parAdminProperties.setInitiatedByEmpId("");
                }
                parAdminProperties.setParId(res.getInt("PARID"));
                //parAdminProperties.setFiscalyear(res.getString("FISCAL_YEAR"));
                if (res.getString("PAR_STATUS") != null && !res.getString("PAR_STATUS").equals("")) {
                    if (res.getInt("PAR_STATUS") == 0) {
                        parAdminProperties.setParstatus("PAR CREATED BUT NOT SUBMITTED");
                    } else {
                        parAdminProperties.setParstatus(getStatusName(con, res.getString("PAR_STATUS")));
                    }
                } else {
                    parAdminProperties.setParstatus("PAR NOT CREATED");
                }
                parList.add(parAdminProperties);
            }

            parSearchResult.setParlist(parList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parSearchResult;
    }

    public String getStatusName(Connection con, String statusid) throws Exception {

        Statement stmt = null;
        ResultSet rs = null;

        String statusname = "";
        try {
            stmt = con.createStatement();
            String sql = "SELECT STATUS_NAME FROM G_PROCESS_STATUS WHERE STATUS_ID='" + statusid + "' AND PROCESS_ID='3'";
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                statusname = rs.getString("STATUS_NAME");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
        }
        return statusname;
    }

    public ArrayList getPARDetails(String HRMSID, String fiscalyear) {
        Connection con = null;
        ArrayList pardetail = new ArrayList();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT FISCAL_YEAR,INITIALS,F_NAME,M_NAME,L_NAME,initiatedby,post_group,is_reviewed,IS_ADVERSED,adverse_comm_status_id,PARMAST.EMP_ID,PARID,FISCAL_YEAR,PERIOD_FROM,PERIOD_TO,PAR_STATUS,PARMAST.SPC,POST,TASK_ID,STATUS_NAME,STATUS_ID,isNRCAttchPresent(parid) as hasAttachment FROM "
                    + "(SELECT initiatedby,post_group,is_reviewed,IS_ADVERSED,adverse_comm_status_id,PARID,EMP_ID,FISCAL_YEAR,PERIOD_FROM,PERIOD_TO,PAR_STATUS,SPC,CADRE_CODE,TASK_ID FROM "
                    + "PAR_MASTER WHERE EMP_ID=? AND FISCAL_YEAR=? AND IS_DELETED = 'N')PARMAST "
                    + "INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = PARMAST.EMP_ID "
                    + "LEFT OUTER JOIN G_SPC ON G_SPC.SPC=PARMAST.SPC "
                    + "LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "LEFT OUTER JOIN (select status_name,process_id,status_id from g_process_status where process_id=3)g_process_status ON PARMAST.PAR_STATUS=g_process_status.STATUS_ID");
            pstmt.setString(1, HRMSID);
            pstmt.setString(2, fiscalyear);
            rs = pstmt.executeQuery();
            int i = 0;
            while (rs.next()) {
                i++;
                ParAdminProperties pbf = new ParAdminProperties();
                pbf.setParslno(i);
                pbf.setFiscalyear(rs.getString("FISCAL_YEAR"));
                pbf.setInitiatedByEmpId(rs.getString("initiatedby"));
                pbf.setPostGroupType(rs.getString("post_group"));
                pbf.setEmpId(rs.getString("EMP_ID"));
                pbf.setSpc(rs.getString("SPC"));
                pbf.setParId(rs.getInt("PARID"));
                pbf.setPrdFrmDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_FROM")));
                pbf.setPrdToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_TO")));
                pbf.setPostName(rs.getString("POST"));
                pbf.setAdverseCommunicationStatusId(rs.getString("adverse_comm_status_id"));
                pbf.setEmpName(StringUtils.defaultString(rs.getString("INITIALS")) + " " + StringUtils.defaultString(rs.getString("F_NAME")) + " " + StringUtils.defaultString(rs.getString("M_NAME")) + " " + StringUtils.defaultString(rs.getString("L_NAME")));
                if (rs.getString("STATUS_NAME") != null && !rs.getString("STATUS_NAME").equals("")) {
                    pbf.setParstatus(rs.getString("STATUS_NAME"));
                } else {
                    pbf.setParstatus("PENDING");
                }
                //pbf.setTaskId(rs.getString("TASK_ID"));
                if (rs.getString("TASK_ID") != null && !rs.getString("TASK_ID").equals("")) {
                    pbf.setTaskId(rs.getString("TASK_ID"));
                } else {
                    pbf.setTaskId("");
                }
                if (rs.getString("is_reviewed") != null && rs.getString("is_reviewed").equals("Y")) {
                    pbf.setIsreview("Y");
                } else {
                    pbf.setIsreview("N");
                }
                //pbf.setParstatusid(rs.getString("STATUS_ID"));
                if (rs.getString("STATUS_ID") != null && !rs.getString("STATUS_ID").equals("")) {
                    pbf.setParstatusid(rs.getString("STATUS_ID"));
                    if (rs.getInt("STATUS_ID") == 17) {
                        /// pbf.setIsNRCAttchPresent(isNRCAttchPresent(con, rs.getString("PARID")));
                    }
                } else {
                    pbf.setParstatusid("");
                }
                if (rs.getInt("STATUS_ID") > 5) {
                    pbf.setIseditable("N");
                } else {
                    pbf.setIseditable("Y");
                }
                if (rs.getString("IS_ADVERSED") != null && rs.getString("IS_ADVERSED").equals("Y")) {
                    pbf.setPar_master_status("PAR IS ADVERSED");
                    pbf.setIsadversed("Y");
                } else {
                    pbf.setPar_master_status(rs.getString("STATUS_NAME"));
                }
                pbf.setIsNRCAttchPresent(rs.getString("hasAttachment"));
                pardetail.add(pbf);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return pardetail;
    }

    @Override
    public ParApplyForm getviewParDetail(int parId) {
        ParApplyForm paf = new ParApplyForm();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String applicantSpc = "";
        Connection con = null;
        String selfappraisal = "";
        String specialcontribution = "";
        String factors = "";
        try {
            int taskId = 0;
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT INITIATED_ON,IS_ADVERSED,PAR1.CADRE_CODE,PAR1.EMP_ID,FISCAL_YEAR,PAR1.OFF_CODE,PAR1.PARID,PAR_STATUS,REF_ID_OF_TABLE,PERIOD_FROM,PERIOD_TO,APPRISE_APC,APPRISE_SPN,APPRISE_OFFICE,PAR1.TASK_ID,SPECIALCONTRIBUTIION,SELFAPPRAISAL,HINDERREASON,PAR_APPRAISEE_TRAN.PLACE,DOB,INITIALS,F_NAME,M_NAME,L_NAME,G_CADRE.CADRE_NAME,PAR1.POST_GROUP,SUBMITTED_ON,REVIEWED_ONDATE,REVIEWED_BY_NAME,REVIEWED_BY_SPN,IS_REVIEWED,is_force_forward,is_force_forward_from_reviewing FROM "
                    + "(SELECT IS_ADVERSED,CADRE_CODE,EMP_ID,FISCAL_YEAR,OFF_CODE,PARID,PAR_STATUS,REF_ID_OF_TABLE,PERIOD_FROM,PERIOD_TO,SPC APPRISE_APC,GETSPN(SPC) APPRISE_SPN,GETOFFNAMEFROMSPC(SPC) APPRISE_OFFICE,TASK_ID,POST_GROUP,REVIEWED_ONDATE,getfullname(REVIEWED_BY_EMPID) as REVIEWED_BY_NAME,GETSPN(REVIEWED_BY_SPC) as REVIEWED_BY_SPN,IS_REVIEWED,is_force_forward,is_force_forward_from_reviewing FROM PAR_MASTER WHERE PARID=?)PAR1 "
                    + "LEFT OUTER JOIN PAR_APPRAISEE_TRAN ON PAR1.PARID = PAR_APPRAISEE_TRAN.PARID "
                    + "INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = PAR1.EMP_ID "
                    + " LEFT OUTER JOIN G_CADRE ON PAR1.CADRE_CODE=G_CADRE.CADRE_CODE"
                    + " LEFT OUTER JOIN TASK_MASTER ON PAR1.TASK_ID=TASK_MASTER.TASK_ID");
            pstmt.setInt(1, parId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                taskId = rs.getInt("TASK_ID");
                selfappraisal = StringUtils.defaultString(rs.getString("SELFAPPRAISAL"));
                specialcontribution = StringUtils.defaultString(rs.getString("SPECIALCONTRIBUTIION"));
                factors = StringUtils.defaultString(rs.getString("HINDERREASON"));
                paf.setParId(rs.getInt("PARID"));
                paf.setParstatus(rs.getInt("PAR_STATUS"));
                paf.setApprisespc(rs.getString("APPRISE_APC"));
                paf.setRefid(rs.getInt("REF_ID_OF_TABLE"));
                paf.setTaskId(rs.getInt("TASK_ID"));
                paf.setFiscalYear(rs.getString("FISCAL_YEAR"));
                paf.setPrdFrmDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_FROM")));
                paf.setPrdToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_TO")));
                paf.setEmpId(rs.getString("EMP_ID"));
                paf.setEmpName(StringUtils.defaultString(rs.getString("INITIALS")) + " " + StringUtils.defaultString(rs.getString("F_NAME")) + " " + StringUtils.defaultString(rs.getString("M_NAME")) + " " + StringUtils.defaultString(rs.getString("L_NAME")));
                paf.setApprisespc(rs.getString("APPRISE_SPN"));
                paf.setAppriseOffice(rs.getString("APPRISE_OFFICE"));
                paf.setSpecialcontribution(specialcontribution);
                paf.setSelfappraisal(selfappraisal);
                paf.setReason(factors);
                paf.setPlace(rs.getString("PLACE"));
                paf.setReviewedondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("reviewed_ondate")));
                paf.setReviewedby(rs.getString("REVIEWED_BY_NAME"));
                paf.setReviewedbyspc(rs.getString("REVIEWED_BY_SPN"));
                PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
                selfappraisal = policy.sanitize(selfappraisal);
                specialcontribution = policy.sanitize(specialcontribution);
                factors = policy.sanitize(factors);
                if (rs.getString("INITIATED_ON") != null && !rs.getString("INITIATED_ON").equals("")) {
                    paf.setSubmittedon(CommonFunctions.getFormattedOutputDate1(rs.getDate("INITIATED_ON")));
                } else {
                    paf.setSubmittedon("");
                }

                paf.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOB")));
                paf.setEmpService(rs.getString("CADRE_NAME"));
                paf.setEmpGroup(rs.getString("POST_GROUP"));
                paf.setSltAdminRemark(rs.getString("IS_ADVERSED"));

                paf.setLeaveAbsentee(getAbsenteeList(paf.getParId()));
                paf.setAchivementList(getAchievementList(paf.getParId()));
                paf.setIsForceForward(rs.getString("is_force_forward"));
                paf.setIsForceForwardFromReviewing(rs.getString("is_force_forward_from_reviewing"));

                paf.setReportingauth(getParAuthority("REPORTING", paf.getParId(), taskId));
                paf.setReviewingauth(getParAuthority("REVIEWING", paf.getParId(), taskId));
                paf.setAcceptingauth(getParAuthority("ACCEPTING", paf.getParId(), taskId));

                paf.setReportingdata(getReportingData(paf));
                paf.setReviewingdata(getReviewingData(paf));
                paf.setAcceptingdata(getAcceptingData(paf));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return paf;
    }

    @Override
    public ParApplyForm getParBriefDetail(int parId) {
        ParApplyForm paf = new ParApplyForm();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String applicantSpc = "";
        Connection con = null;
        String selfappraisal = "";
        String specialcontribution = "";
        String factors = "";
        try {
            int taskId = 0;
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT INITIATED_ON,adverse_comm_status_id,IS_ADVERSED,PAR1.CADRE_CODE,PAR1.EMP_ID,FISCAL_YEAR,PAR1.OFF_CODE,PAR1.PARID,PAR_STATUS,REF_ID_OF_TABLE,PERIOD_FROM,PERIOD_TO,APPRISE_APC,APPRISE_SPN,APPRISE_OFFICE,PAR1.TASK_ID,SPECIALCONTRIBUTIION,SELFAPPRAISAL,HINDERREASON,PAR_APPRAISEE_TRAN.PLACE,DOB,INITIALS,F_NAME,M_NAME,L_NAME,G_CADRE.CADRE_NAME,PAR1.POST_GROUP,SUBMITTED_ON,REVIEWED_ONDATE,REVIEWED_BY_NAME,REVIEWED_BY_SPN,IS_REVIEWED FROM "
                    + "(SELECT IS_ADVERSED,adverse_comm_status_id,CADRE_CODE,EMP_ID,FISCAL_YEAR,OFF_CODE,PARID,PAR_STATUS,REF_ID_OF_TABLE,PERIOD_FROM,PERIOD_TO,SPC APPRISE_APC,GETSPN(SPC) APPRISE_SPN,GETOFFNAMEFROMSPC(SPC) APPRISE_OFFICE,TASK_ID,POST_GROUP,REVIEWED_ONDATE,getfullname(REVIEWED_BY_EMPID) as REVIEWED_BY_NAME,GETSPN(REVIEWED_BY_SPC) as REVIEWED_BY_SPN,IS_REVIEWED FROM PAR_MASTER WHERE PARID=?)PAR1 "
                    + "LEFT OUTER JOIN PAR_APPRAISEE_TRAN ON PAR1.PARID = PAR_APPRAISEE_TRAN.PARID "
                    + "INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = PAR1.EMP_ID "
                    + " LEFT OUTER JOIN G_CADRE ON PAR1.CADRE_CODE=G_CADRE.CADRE_CODE"
                    + " LEFT OUTER JOIN TASK_MASTER ON PAR1.TASK_ID=TASK_MASTER.TASK_ID");
            pstmt.setInt(1, parId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                taskId = rs.getInt("TASK_ID");
                selfappraisal = StringUtils.defaultString(rs.getString("SELFAPPRAISAL"));
                specialcontribution = StringUtils.defaultString(rs.getString("SPECIALCONTRIBUTIION"));
                factors = StringUtils.defaultString(rs.getString("HINDERREASON"));
                paf.setParId(rs.getInt("PARID"));
                paf.setParstatus(rs.getInt("PAR_STATUS"));
                paf.setIsadversed(rs.getString("IS_ADVERSED"));
                paf.setAdverseCommunicationStatusId(rs.getInt("adverse_comm_status_id"));
                paf.setApprisespc(rs.getString("APPRISE_APC"));
                paf.setRefid(rs.getInt("REF_ID_OF_TABLE"));
                paf.setTaskId(rs.getInt("TASK_ID"));
                paf.setFiscalYear(rs.getString("FISCAL_YEAR"));
                paf.setPrdFrmDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_FROM")));
                paf.setPrdToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_TO")));
                paf.setEmpId(rs.getString("EMP_ID"));
                paf.setEmpName(StringUtils.defaultString(rs.getString("INITIALS")) + " " + StringUtils.defaultString(rs.getString("F_NAME")) + " " + StringUtils.defaultString(rs.getString("M_NAME")) + " " + StringUtils.defaultString(rs.getString("L_NAME")));
                paf.setApprisespc(rs.getString("APPRISE_SPN"));
                paf.setAppriseOffice(rs.getString("APPRISE_OFFICE"));
                paf.setSpecialcontribution(specialcontribution);
                paf.setSelfappraisal(selfappraisal);
                paf.setReason(factors);
                paf.setPlace(rs.getString("PLACE"));
                paf.setReviewedondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("reviewed_ondate")));
                paf.setReviewedby(rs.getString("REVIEWED_BY_NAME"));
                paf.setReviewedbyspc(rs.getString("REVIEWED_BY_SPN"));
                PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
                selfappraisal = policy.sanitize(selfappraisal);
                specialcontribution = policy.sanitize(specialcontribution);
                factors = policy.sanitize(factors);
                if (rs.getString("INITIATED_ON") != null && !rs.getString("INITIATED_ON").equals("")) {
                    paf.setSubmittedon(CommonFunctions.getFormattedOutputDate1(rs.getDate("INITIATED_ON")));
                } else {
                    paf.setSubmittedon("");
                }

                paf.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOB")));
                paf.setEmpService(rs.getString("CADRE_NAME"));
                paf.setEmpGroup(rs.getString("POST_GROUP"));
                paf.setLeaveAbsentee(getAbsenteeList(paf.getParId()));
                paf.setAchivementList(getAchievementList(paf.getParId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return paf;
    }

    @Override
    public String getAppriseSPCOfPar(int parId) {
        String apprisespc = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT SPC FROM PAR_MASTER WHERE PARID=?");
            pstmt.setInt(1, parId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                apprisespc = rs.getString("SPC");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return apprisespc;
    }

    @Override
    public ParApplyForm getviewParDetail(int parId, int taskId) {
        ParApplyForm paf = new ParApplyForm();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String applicantSpc = "";
        Connection con = null;
        String selfappraisal = "";
        String specialcontribution = "";
        String factors = "";
        try {
            con = dataSource.getConnection();
            if (taskId == 0) {
                pstmt = con.prepareStatement("SELECT INITIATED_ON,IS_ADVERSED,PAR1.CADRE_CODE,PAR1.EMP_ID,FISCAL_YEAR,PAR1.OFF_CODE,PAR1.PARID,"
                        + "PAR_STATUS,REF_ID_OF_TABLE,PERIOD_FROM,PERIOD_TO,APPRISE_APC,APPRISE_SPN,APPRISE_OFFICE,PAR1.TASK_ID,SPECIALCONTRIBUTIION,"
                        + "SELFAPPRAISAL,HINDERREASON,fivet_component_appraise,PAR_APPRAISEE_TRAN.PLACE,DOB,INITIALS,F_NAME,M_NAME,L_NAME,G_CADRE.CADRE_NAME,"
                        + "PAR1.POST_GROUP,SUBMITTED_ON,REVIEWED_ONDATE,REVIEWED_BY_NAME,IS_REVIEWED FROM "
                        + "(SELECT IS_ADVERSED,CADRE_CODE,EMP_ID,FISCAL_YEAR,OFF_CODE,PARID,PAR_STATUS,REF_ID_OF_TABLE,PERIOD_FROM,PERIOD_TO,SPC APPRISE_APC,"
                        + "GETSPN(SPC) APPRISE_SPN,GETOFFNAMEFROMSPC(SPC) APPRISE_OFFICE,TASK_ID,POST_GROUP,REVIEWED_ONDATE,getfullname(REVIEWED_BY_EMPID) as "
                        + "REVIEWED_BY_NAME,IS_REVIEWED FROM PAR_MASTER WHERE PARID=?)PAR1 "
                        + "LEFT OUTER JOIN PAR_APPRAISEE_TRAN ON PAR1.PARID = PAR_APPRAISEE_TRAN.PARID "
                        + "INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = PAR1.EMP_ID "
                        + " LEFT OUTER JOIN G_CADRE ON PAR1.CADRE_CODE=G_CADRE.CADRE_CODE"
                        + " LEFT OUTER JOIN TASK_MASTER ON PAR1.TASK_ID=TASK_MASTER.TASK_ID");
                pstmt.setInt(1, parId);
            } else {
                pstmt = con.prepareStatement("SELECT INITIATED_ON,IS_ADVERSED,PAR1.CADRE_CODE,PAR1.EMP_ID,FISCAL_YEAR,PAR1.OFF_CODE,PAR1.PARID,PAR_STATUS,REF_ID_OF_TABLE,PERIOD_FROM,PERIOD_TO,APPRISE_APC,APPRISE_SPN,APPRISE_OFFICE,PAR1.TASK_ID,SPECIALCONTRIBUTIION,SELFAPPRAISAL,HINDERREASON,fivet_component_appraise,PAR_APPRAISEE_TRAN.PLACE,DOB,INITIALS,F_NAME,M_NAME,L_NAME,G_CADRE.CADRE_NAME,PAR1.POST_GROUP,SUBMITTED_ON,REVIEWED_ONDATE,REVIEWED_BY_NAME,IS_REVIEWED FROM "
                        + "(SELECT IS_ADVERSED,CADRE_CODE,EMP_ID,FISCAL_YEAR,OFF_CODE,PARID,PAR_STATUS,REF_ID_OF_TABLE,PERIOD_FROM,PERIOD_TO,SPC APPRISE_APC,GETSPN(SPC) APPRISE_SPN,GETOFFNAMEFROMSPC(SPC) APPRISE_OFFICE,TASK_ID,POST_GROUP,REVIEWED_ONDATE,getfullname(REVIEWED_BY_EMPID) as REVIEWED_BY_NAME,IS_REVIEWED FROM PAR_MASTER WHERE TASK_ID=?)PAR1 "
                        + "LEFT OUTER JOIN PAR_APPRAISEE_TRAN ON PAR1.PARID = PAR_APPRAISEE_TRAN.PARID "
                        + "INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = PAR1.EMP_ID "
                        + " LEFT OUTER JOIN G_CADRE ON PAR1.CADRE_CODE=G_CADRE.CADRE_CODE"
                        + " INNER JOIN TASK_MASTER ON PAR1.TASK_ID=TASK_MASTER.TASK_ID");

                pstmt.setInt(1, taskId);
            }
            rs = pstmt.executeQuery();
            if (rs.next()) {
                selfappraisal = StringUtils.defaultString(rs.getString("SELFAPPRAISAL"));
                specialcontribution = StringUtils.defaultString(rs.getString("SPECIALCONTRIBUTIION"));
                factors = StringUtils.defaultString(rs.getString("HINDERREASON"));
                paf.setFiveTComponentappraise(rs.getString("fivet_component_appraise"));
                paf.setParId(rs.getInt("PARID"));
                paf.setParstatus(rs.getInt("PAR_STATUS"));
                paf.setApprisespc(rs.getString("APPRISE_APC"));
                paf.setRefid(rs.getInt("REF_ID_OF_TABLE"));
                paf.setTaskId(rs.getInt("TASK_ID"));
                paf.setFiscalYear(rs.getString("FISCAL_YEAR"));
                paf.setPrdFrmDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_FROM")));
                paf.setPrdToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_TO")));
                paf.setEmpId(rs.getString("EMP_ID"));
                paf.setEmpName(StringUtils.defaultString(rs.getString("INITIALS")) + " " + StringUtils.defaultString(rs.getString("F_NAME")) + " " + StringUtils.defaultString(rs.getString("M_NAME")) + " " + StringUtils.defaultString(rs.getString("L_NAME")));
                paf.setApprisespc(rs.getString("APPRISE_SPN"));
                paf.setAppriseOffice(rs.getString("APPRISE_OFFICE"));
                paf.setSpecialcontribution(specialcontribution);
                paf.setSelfappraisal(selfappraisal);
                paf.setReason(factors);
                paf.setPlace(rs.getString("PLACE"));
                paf.setReviewedondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("reviewed_ondate")));
                paf.setIsreviewed(rs.getString("IS_REVIEWED"));
                paf.setReviewedby(rs.getString("REVIEWED_BY_NAME"));
                PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
                selfappraisal = policy.sanitize(selfappraisal);
                specialcontribution = policy.sanitize(specialcontribution);
                factors = policy.sanitize(factors);
                if (rs.getString("INITIATED_ON") != null && !rs.getString("INITIATED_ON").equals("")) {
                    paf.setSubmittedon(CommonFunctions.getFormattedOutputDate1(rs.getDate("INITIATED_ON")));
                } else {
                    paf.setSubmittedon("");
                }

                paf.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOB")));
                paf.setEmpService(rs.getString("CADRE_NAME"));
                paf.setEmpGroup(rs.getString("POST_GROUP"));
                paf.setSltAdminRemark(rs.getString("IS_ADVERSED"));

                paf.setLeaveAbsentee(getAbsenteeList(paf.getParId()));
                paf.setAchivementList(getAchievementList(paf.getParId()));

                paf.setReportingauth(getParAuthority("REPORTING", paf.getParId(), taskId));
                paf.setReviewingauth(getParAuthority("REVIEWING", paf.getParId(), taskId));
                paf.setAcceptingauth(getParAuthority("ACCEPTING", paf.getParId(), taskId));

                paf.setReportingdata(getReportingData(paf));
                paf.setReviewingdata(getReviewingData(paf));
                paf.setAcceptingdata(getAcceptingData(paf));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return paf;
    }

    @Override
    public String revertPARByPARAdmin(String loginempid, ParDetail parDetail) {
        Connection con = null;
        PreparedStatement pst1 = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        boolean retVal = false;
        String appraiseEmpid = "";
        String appraiseSpc = "";
        int parStatus = 0;
        int logid = 0;
        String startTime = "";
        String revertAuth = "";
        try {
            con = dataSource.getConnection();
            String sql = "SELECT EMP_ID,SPC,par_status FROM PAR_MASTER WHERE PARID=?";
            pst1 = con.prepareStatement(sql);
            pst1.setInt(1, parDetail.getParid());
            rs = pst1.executeQuery();
            if (rs.next()) {
                appraiseEmpid = rs.getString("EMP_ID");
                appraiseSpc = rs.getString("SPC");
                parStatus = rs.getInt("par_status");
            }

            DataBaseFunctions.closeSqlObjects(rs, pst1);

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());
            if (parStatus == 6) {
                pst = con.prepareStatement("INSERT INTO WORKFLOW_LOG(REF_ID,ACTION_TAKEN_BY,FORWARDED_SPC,FORWARD_TO,NOTE,SPC_ONTIME,TASK_ACTION_DATE,TASK_ID,TASK_STATUS_ID,WORKFLOW_TYPE,AUTHORITY_TYPE) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
                pst.setInt(1, parDetail.getParid());
                pst.setString(2, loginempid);
                pst.setString(3, appraiseSpc);
                pst.setString(4, appraiseEmpid);
                pst.setString(5, parDetail.getRevertremarks());
                pst.setString(6, "");
                pst.setTimestamp(7, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setInt(8, parDetail.getTaskid());
                pst.setInt(9, 16);
                pst.setString(10, "PAR_REVERT");
                pst.setString(11, "REPORTING AUTHORITY");
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                int hierarchy = 0;
                sql = "SELECT HIERARCHY_NO FROM PAR_REPORTING_TRAN WHERE PAR_ID=? AND REPORTING_EMP_ID=?";
                pst1 = con.prepareStatement(sql);
                pst1.setInt(1, parDetail.getParid());
                pst1.setString(2, loginempid);
                rs = pst1.executeQuery();
                if (rs.next()) {
                    hierarchy = rs.getInt("HIERARCHY_NO");
                }

                DataBaseFunctions.closeSqlObjects(rs, pst1);

                pst = con.prepareStatement("DELETE FROM PAR_REPORTING_TRAN WHERE PAR_ID=? AND HIERARCHY_NO >= ?");
                pst.setInt(1, parDetail.getParid());
                pst.setInt(2, hierarchy);
                pst.executeUpdate();
                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("DELETE FROM PAR_REVIEWING_TRAN WHERE PAR_ID=?");
                pst.setInt(1, parDetail.getParid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("DELETE FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=?");
                pst.setInt(1, parDetail.getParid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("UPDATE TASK_MASTER SET STATUS_ID=?,PENDING_AT=?,APPLY_TO=?,PENDING_SPC=? WHERE TASK_ID=?");
                pst.setInt(1, 16);//PAR IS RETURNED BY REPORTING AUTHORITY
                pst.setString(2, appraiseEmpid);
                pst.setString(3, appraiseEmpid);
                pst.setString(4, appraiseSpc);
                pst.setInt(5, parDetail.getTaskid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("UPDATE PAR_MASTER SET PAR_STATUS=?,REF_ID_OF_TABLE=? WHERE PARID=?");
                pst.setInt(1, 16);
                pst.setInt(2, 0);
                pst.setInt(3, parDetail.getParid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                parDetail.setRevertdone("Y");
                revertAuth = "Reporting";

            } else if (parStatus == 7) {
                int hierarchy = 0;
                sql = "SELECT HIERARCHY_NO FROM PAR_REVIEWING_TRAN WHERE PAR_ID=? AND REVIEWING_EMP_ID=?";
                pst1 = con.prepareStatement(sql);
                pst1.setInt(1, parDetail.getParid());
                pst1.setString(2, loginempid);
                rs = pst1.executeQuery();
                if (rs.next()) {
                    hierarchy = rs.getInt("HIERARCHY_NO");
                }

                DataBaseFunctions.closeSqlObjects(rs, pst1);

                pst = con.prepareStatement("DELETE FROM PAR_REVIEWING_TRAN WHERE PAR_ID=? AND HIERARCHY_NO >= ?");
                pst.setInt(1, parDetail.getParid());
                pst.setInt(2, hierarchy);
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("DELETE FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=?");
                pst.setInt(1, parDetail.getParid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("UPDATE PAR_MASTER SET PAR_STATUS=?,REF_ID_OF_TABLE=? WHERE PARID=?");
                pst.setInt(1, 18);
                pst.setInt(2, 0);
                pst.setInt(3, parDetail.getParid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=?,PENDING_SPC=?,APPLY_TO=?,STATUS_ID=? WHERE TASK_ID=?");
                pst.setString(1, null);
                pst.setString(2, null);
                pst.setString(3, null);
                pst.setInt(4, 18);
                pst.setInt(5, parDetail.getTaskid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("INSERT INTO WORKFLOW_LOG(REF_ID,ACTION_TAKEN_BY,FORWARDED_SPC,FORWARD_TO,NOTE,SPC_ONTIME,TASK_ACTION_DATE,TASK_ID,TASK_STATUS_ID,WORKFLOW_TYPE,AUTHORITY_TYPE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
                pst.setInt(1, parDetail.getParid());
                pst.setString(2, loginempid);
                pst.setString(3, appraiseSpc);
                pst.setString(4, appraiseEmpid);
                pst.setString(5, parDetail.getRevertremarks());
                pst.setString(6, "");
                pst.setTimestamp(7, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setInt(8, parDetail.getTaskid());
                pst.setInt(9, 18);
                pst.setString(10, "PAR_REVERT");
                pst.setString(11, "REVIEWING AUTHORITY");
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                parDetail.setRevertdone("Y");
                revertAuth = "Reviewing";
            } else if (parStatus == 8) {
                int hierarchy = 0;
                sql = "SELECT HIERARCHY_NO FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=? AND ACCEPTING_EMP_ID=?";
                pst1 = con.prepareStatement(sql);
                pst1.setInt(1, parDetail.getParid());
                pst1.setString(2, loginempid);
                rs = pst1.executeQuery();
                if (rs.next()) {
                    hierarchy = rs.getInt("HIERARCHY_NO");
                }

                DataBaseFunctions.closeSqlObjects(rs, pst1);

                pst = con.prepareStatement("DELETE FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=? AND HIERARCHY_NO >= ?");
                pst.setInt(1, parDetail.getParid());
                pst.setInt(2, hierarchy);
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("UPDATE PAR_MASTER SET PAR_STATUS=?,REF_ID_OF_TABLE=? WHERE PARID=?");
                pst.setInt(1, 19);
                pst.setInt(2, 0);
                pst.setInt(3, parDetail.getParid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=?,PENDING_SPC=?,APPLY_TO=?,STATUS_ID=? WHERE TASK_ID=?");
                pst.setString(1, null);
                pst.setString(2, null);
                pst.setString(3, null);
                pst.setInt(4, 19);
                pst.setInt(5, parDetail.getTaskid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("INSERT INTO WORKFLOW_LOG(REF_ID,ACTION_TAKEN_BY,FORWARDED_SPC,FORWARD_TO,NOTE,SPC_ONTIME,TASK_ACTION_DATE,TASK_ID,TASK_STATUS_ID,WORKFLOW_TYPE,AUTHORITY_TYPE) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
                pst.setInt(1, parDetail.getParid());
                pst.setString(2, loginempid);
                pst.setString(3, appraiseSpc);
                pst.setString(4, appraiseEmpid);
                pst.setString(5, parDetail.getRevertremarks());
                pst.setString(6, "");
                pst.setTimestamp(7, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setInt(8, parDetail.getTaskid());
                pst.setInt(9, 19);
                pst.setString(10, "PAR_REVERT");
                pst.setString(11, "ACCEPTING AUTHORITY");
                pst.executeUpdate();

                parDetail.setRevertdone("Y");
                revertAuth = "Accepting";
            }
            if (parDetail.getRevertdone() != null && !parDetail.getRevertdone().equals("")) {
                if (parDetail.getRevertdone().equals("Y")) {
                    String mobile = "";
                    String eol = System.getProperty("line.separator");

                    String msg = "PAR for " + parDetail.getFiscalYear() + " has been reverted by " + revertAuth + " Authority. Kindly resubmit the PAR.";
                    msg = msg + eol + " hrmsodisha.gov.in";

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parDetail.getRevertdone();
    }

    @Override
    public String getLoginempid(String empid) {
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Connection con = null;
        String empname = "";
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select  ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME from emp_mast where emp_id=?");
            pstmt.setString(1, empid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                empname = rs.getString("EMPNAME");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empname;
    }

    @Override
    public ArrayList getAbsenteeList(int parId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        Statement st = null;
        ResultSet rs = null;
        ArrayList absenteeList = new ArrayList();
        AbsenteeBean pab = null;
        try {
            con = dataSource.getConnection();

            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM PAR_ABSENTEE WHERE PARID='" + parId + "'");
            while (rs.next()) {
                pab = new AbsenteeBean();
                pab.setFromDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("FROMDATE")));
                pab.setToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("TODATE")));
                pab.setAbsenceCause(rs.getString("LEAVEPURPOSE"));
                pab.setLeaveType(rs.getString("TYPEOFLEAVE"));
                absenteeList.add(pab);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return absenteeList;
    }

    @Override
    public ArrayList getAchievementList(int parid) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList achievementList = new ArrayList();
        ParAchievement pa = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM PAR_ACHIEVEMENT WHERE PARID=?");
            pstmt.setInt(1, parid);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                pa = new ParAchievement();
                pa.setTask(rs.getString("THETASK"));
                pa.setTarget(rs.getString("TARGET"));
                pa.setAchievement(rs.getString("ACHIEVEMENT"));
                pa.setPercentAchievement(rs.getString("ACHIEVEMENTPERCENT"));
                achievementList.add(pa);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return achievementList;
    }

    @Override
    public ArrayList getReportingData(ParApplyForm paf) {
        String isrepcomp = "Y";
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList reportingbeans = new ArrayList();
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(" SELECT SUBMITTED_ON,REPORTING_CUR_SPC,IS_COMPLETED,PRPTID,REPORTING_EMP_ID,REPORTING_AUTH_NOTE,"
                    + " RATEQUALITYOFWORK,RATINGATTITUDE,RATINGCOMSKILL,RATINGCOORDINATION,RATINGDECISIONMAKING,"
                    + " RATINGINITIATIVE,RATINGITSKILL,RATINGLEADERSHIP,RATINGRESPONSIBILITY,TEAMWORKRATING,INADEQUACIESNOTE,INTEGRITYNOTE,"
                    + "GRADE_ID,GRADINGNOTE,fivet_charter_tenpercent,fivet_charter_fivepercent,fivet_mosarkar,"
                    + "rating_stsc_section,rating_quality_ofoutput, ratingeffectiveness_handling_work,penpicture_of_oficernote, stateof_health,"
                    + "sick_report_ondate,sick_details,total_a, total_b, total_c, total_d,orginal_filename_grading_document_reporting,disk_filename_grading_document_reporting "
                    + "FROM PAR_REPORTING_TRAN WHERE PAR_ID=? ORDER BY HIERARCHY_NO");
            pstmt.setInt(1, paf.getParId());
            rs = pstmt.executeQuery();
            int i = 0;
            while (rs.next()) {
                i++;
                if (rs.getString("IS_COMPLETED") == null || rs.getString("IS_COMPLETED").equals("") || rs.getString("IS_COMPLETED").equals("N")) {
                    isrepcomp = "N";
                } else if (rs.getString("IS_COMPLETED").equals("F")) {
                    isrepcomp = "F";
                } else if (rs.getString("IS_COMPLETED").equals("Y")) {
                    isrepcomp = "Y";
                }
                int reftableid = paf.getRefid();

                ReportingHelperBean rhb = new ReportingHelperBean();
                /*if (paf.getParstatus() > 6) {
                 paf.setIsreportingcompleted("Y");
                 }*/
                if (paf.getParstatus() == 6 && rs.getInt("PRPTID") == reftableid) {
                    paf.setIsreportingcompleted(isrepcomp);
                    paf.setReportingempid(rs.getString("REPORTING_EMP_ID"));
                    paf.setAuthNote(rs.getString("REPORTING_AUTH_NOTE"));
                    paf.setRatequalityofwork(rs.getInt("RATEQUALITYOFWORK"));
                    paf.setRatingattitude(rs.getInt("RATINGATTITUDE"));
                    paf.setRatingcomskill(rs.getInt("RATINGCOMSKILL"));
                    paf.setRatingcoordination(rs.getInt("RATINGCOORDINATION"));
                    paf.setRatingdecisionmaking(rs.getInt("RATINGDECISIONMAKING"));
                    paf.setRatinginitiative(rs.getInt("RATINGINITIATIVE"));
                    paf.setRatingitskill(rs.getInt("RATINGITSKILL"));
                    paf.setRatingleadership(rs.getInt("RATINGLEADERSHIP"));
                    paf.setRatingresponsibility(rs.getInt("RATINGRESPONSIBILITY"));
                    paf.setTeamworkrating(rs.getInt("TEAMWORKRATING"));
                    paf.setInadequaciesNote(rs.getString("INADEQUACIESNOTE"));
                    paf.setIntegrityNote(rs.getString("INTEGRITYNOTE"));
                    paf.setSltGrading(rs.getString("GRADE_ID"));
                    paf.setGradingNote(rs.getString("GRADINGNOTE"));
                    paf.setFiveTChartertenpercent(rs.getString("fivet_charter_tenpercent"));
                    paf.setFiveTCharterfivePercent(rs.getString("fivet_charter_fivepercent"));
                    paf.setFiveTComponentmoSarkar(rs.getString("fivet_mosarkar"));
                    paf.setOriginalFileNamegradingDocumentReporting(rs.getString("orginal_filename_grading_document_reporting"));
                    paf.setDiskFileNameforgradingDocumentReporting(rs.getString("disk_filename_grading_document_reporting"));

                    paf.setSubmitted_on(CommonFunctions.getFormattedOutputDate1(rs.getDate("SUBMITTED_ON")));

                    //ADDITIONAL FIELDS FOR SI PAR    
                    paf.setRatingAttitudeStScSection(rs.getInt("rating_stsc_section"));
                    paf.setRatingQualityOfOutput(rs.getInt("rating_quality_ofoutput"));
                    paf.setRatingeffectivenessHandlingWork(rs.getInt("ratingeffectiveness_handling_work"));
                    //rating_stsc_section,rating_quality_ofoutput, ratingeffectiveness_handling_work

                    paf.setPenPictureOfOficerNote(rs.getString("penpicture_of_oficernote"));
                    paf.setStateOfHealth(rs.getString("stateof_health"));
                    paf.setSickReportOnDate(rs.getString("sick_report_ondate"));
                    paf.setSickDetails(rs.getString("sick_details"));
                    //penpicture_of_oficernote, stateof_health,sick_report_ondate,sick_details

                    paf.setTotalofA(rs.getInt("total_a"));
                    paf.setTotalofB(rs.getInt("total_b"));
                    paf.setTotalofC(rs.getInt("total_c"));
                    paf.setTotalofD(rs.getInt("total_d"));
                    //total_a, total_b, total_c, total_d

                    rhb.setPrtid(rs.getInt("PRPTID"));
                    rhb.setIscurrentreporting("Y");
                    rhb.setIsreportingcompleted(isrepcomp);
                    rhb.setReportingempid(rs.getString("REPORTING_EMP_ID"));

                    rhb.setReportingauthName(getAuthorityType(rs.getString("REPORTING_EMP_ID"), rs.getString("REPORTING_CUR_SPC")));
                    rhb.setAuthNote(rs.getString("REPORTING_AUTH_NOTE"));
                    rhb.setRatequalityofwork(rs.getInt("RATEQUALITYOFWORK"));
                    rhb.setRatingattitude(rs.getInt("RATINGATTITUDE"));
                    rhb.setRatingcomskill(rs.getInt("RATINGCOMSKILL"));
                    rhb.setRatingcoordination(rs.getInt("RATINGCOORDINATION"));
                    rhb.setRatingdecisionmaking(rs.getInt("RATINGDECISIONMAKING"));
                    rhb.setRatinginitiative(rs.getInt("RATINGINITIATIVE"));
                    rhb.setRatingitskill(rs.getInt("RATINGITSKILL"));
                    rhb.setRatingleadership(rs.getInt("RATINGLEADERSHIP"));
                    rhb.setRatingresponsibility(rs.getInt("RATINGRESPONSIBILITY"));
                    rhb.setTeamworkrating(rs.getInt("TEAMWORKRATING"));
                    rhb.setInadequaciesNote(rs.getString("INADEQUACIESNOTE"));
                    rhb.setIntegrityNote(rs.getString("INTEGRITYNOTE"));
                    rhb.setSltGrading(rs.getString("GRADE_ID"));
                    rhb.setGradingNote(rs.getString("GRADINGNOTE"));
                    rhb.setFiveTChartertenpercent(rs.getString("fivet_charter_tenpercent"));
                    rhb.setFiveTCharterfivePercent(rs.getString("fivet_charter_fivepercent"));
                    rhb.setFiveTComponentmoSarkar(rs.getString("fivet_mosarkar"));

                    //ADDITIONAL FIELDS FOR SI PAR    
                    rhb.setRatingAttitudeStScSection(rs.getInt("rating_stsc_section"));
                    rhb.setRatingQualityOfOutput(rs.getInt("rating_quality_ofoutput"));
                    rhb.setRatingeffectivenessHandlingWork(rs.getInt("ratingeffectiveness_handling_work"));
                    //rating_stsc_section,rating_quality_ofoutput, ratingeffectiveness_handling_work

                    rhb.setPenPictureOfOficerNote(rs.getString("penpicture_of_oficernote"));
                    rhb.setStateOfHealth(rs.getString("stateof_health"));
                    rhb.setSickReportOnDate(rs.getString("sick_report_ondate"));
                    rhb.setSickDetails(rs.getString("sick_details"));
                    //penpicture_of_oficernote, stateof_health,sick_report_ondate,sick_details

                    rhb.setTotalofA(rs.getInt("total_a"));
                    rhb.setTotalofB(rs.getInt("total_b"));
                    rhb.setTotalofC(rs.getInt("total_c"));
                    rhb.setTotalofD(rs.getInt("total_d"));
                    //total_a, total_b, total_c, total_d

                    rhb.setSubmittedon(CommonFunctions.getFormattedOutputDate1(rs.getDate("SUBMITTED_ON")));

                    rhb.setOriginalFileNamegradingDocumentReporting(rs.getString("orginal_filename_grading_document_reporting"));
                    rhb.setDiskFileNameforgradingDocumentReporting(rs.getString("disk_filename_grading_document_reporting"));
                } else {
                    rhb.setPrtid(rs.getInt("PRPTID"));
                    rhb.setIscurrentreporting("N");
                    rhb.setIsreportingcompleted(isrepcomp);
                    rhb.setReportingempid(rs.getString("REPORTING_EMP_ID"));
                    rhb.setReportingauthName(getAuthorityType(rs.getString("REPORTING_EMP_ID"), rs.getString("REPORTING_CUR_SPC")));
                    rhb.setAuthNote(rs.getString("REPORTING_AUTH_NOTE"));
                    rhb.setRatequalityofwork1(getGradingName(rs.getInt("RATEQUALITYOFWORK")));
                    rhb.setRatingattitude1(getGradingName(rs.getInt("RATINGATTITUDE")));
                    rhb.setRatingcomskill1(getGradingName(rs.getInt("RATINGCOMSKILL")));
                    rhb.setRatingcoordination1(getGradingName(rs.getInt("RATINGCOORDINATION")));
                    rhb.setRatingdecisionmaking1(getGradingName(rs.getInt("RATINGDECISIONMAKING")));
                    rhb.setRatinginitiative1(getGradingName(rs.getInt("RATINGINITIATIVE")));
                    rhb.setRatingitskill1(getGradingName(rs.getInt("RATINGITSKILL")));
                    rhb.setRatingleadership1(getGradingName(rs.getInt("RATINGLEADERSHIP")));
                    rhb.setRatingresponsibility1(getGradingName(rs.getInt("RATINGRESPONSIBILITY")));
                    rhb.setTeamworkrating1(getGradingName(rs.getInt("TEAMWORKRATING")));
                    rhb.setInadequaciesNote(rs.getString("INADEQUACIESNOTE"));
                    rhb.setIntegrityNote(rs.getString("INTEGRITYNOTE"));
                    rhb.setSltGrading(rs.getString("GRADE_ID"));
                    rhb.setSltGrading1(getGradingName(rs.getInt("GRADE_ID")));
                    rhb.setGradingNote(rs.getString("GRADINGNOTE"));
                    rhb.setFiveTChartertenpercent(rs.getString("fivet_charter_tenpercent"));
                    rhb.setFiveTCharterfivePercent(rs.getString("fivet_charter_fivepercent"));
                    rhb.setFiveTComponentmoSarkar(rs.getString("fivet_mosarkar"));

                    rhb.setSubmittedon(CommonFunctions.getFormattedOutputDate1(rs.getDate("SUBMITTED_ON")));

                    //ADDITIONAL FIELDS FOR SI PAR    
                    rhb.setRatingAttitudeStScSection(rs.getInt("rating_stsc_section"));
                    rhb.setRatingQualityOfOutput(rs.getInt("rating_quality_ofoutput"));
                    rhb.setRatingeffectivenessHandlingWork(rs.getInt("ratingeffectiveness_handling_work"));
                    //rating_stsc_section,rating_quality_ofoutput, ratingeffectiveness_handling_work

                    rhb.setPenPictureOfOficerNote(rs.getString("penpicture_of_oficernote"));
                    rhb.setStateOfHealth(rs.getString("stateof_health"));
                    rhb.setSickReportOnDate(rs.getString("sick_report_ondate"));
                    rhb.setSickDetails(rs.getString("sick_details"));
                    //penpicture_of_oficernote, stateof_health,sick_report_ondate,sick_details

                    rhb.setTotalofA(rs.getInt("total_a"));
                    rhb.setTotalofB(rs.getInt("total_b"));
                    rhb.setTotalofC(rs.getInt("total_c"));
                    rhb.setTotalofD(rs.getInt("total_d"));
                    //total_a, total_b, total_c, total_d

                    rhb.setOriginalFileNamegradingDocumentReporting(rs.getString("orginal_filename_grading_document_reporting"));
                    rhb.setDiskFileNameforgradingDocumentReporting(rs.getString("disk_filename_grading_document_reporting"));
                }
                reportingbeans.add(rhb);

            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();

        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return reportingbeans;

    }

    @Override
    public ArrayList getParAuthority(String authtype, int parId, int taskId) {
        ArrayList al = new ArrayList();
        PreparedStatement pstamt = null;
        PreparedStatement pstamtReviewing = null;
        PreparedStatement pstamtReporting = null;
        PreparedStatement pstamtAccepting = null;
        ResultSet resultset = null;
        ResultSet innerresult = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            if (authtype.equalsIgnoreCase("REPORTING")) {
                String spctype = "G";
                pstamt = con.prepareStatement("SELECT prptid,REPORTING_CUR_SPC FROM PAR_REPORTING_TRAN WHERE PAR_ID=? ORDER BY HIERARCHY_NO");
                pstamt.setInt(1, parId);
                resultset = pstamt.executeQuery();
                while (resultset.next()) {
                    if (resultset.getString("REPORTING_CUR_SPC") != null && !resultset.getString("REPORTING_CUR_SPC").equals("")) {
                        if (resultset.getString("REPORTING_CUR_SPC").length() > 2) {
                            spctype = "G";
                        } else {
                            spctype = "M";
                        }
                    }
                    if (spctype.equalsIgnoreCase("G")) {
                        pstamtReporting = con.prepareStatement("SELECT prptid,REPORTING_EMP_ID,REPORTING_SPN,INITIALS,F_NAME,M_NAME,L_NAME,FROMDATE,TODATE FROM(SELECT prptid,GETSPN(REPORTING_CUR_SPC)REPORTING_SPN,REPORTING_EMP_ID,FROMDATE,TODATE FROM PAR_REPORTING_TRAN WHERE PRPTID=? ORDER BY HIERARCHY_NO)T1 "
                                + "INNER JOIN EMP_MAST ON T1.REPORTING_EMP_ID = EMP_MAST.EMP_ID");
                        pstamtReporting.setInt(1, resultset.getInt("prptid"));
                        innerresult = pstamtReporting.executeQuery();

                        while (innerresult.next()) {
                            Parauthorityhelperbean parhelper = new Parauthorityhelperbean();
                            parhelper.setAuthid(resultset.getInt("prptid"));
                            parhelper.setAuthorityname(StringUtils.defaultString(innerresult.getString("INITIALS")) + " " + StringUtils.defaultString(innerresult.getString("F_NAME")) + " " + StringUtils.defaultString(innerresult.getString("M_NAME")) + " " + StringUtils.defaultString(innerresult.getString("L_NAME")));
                            parhelper.setAuthorityspn(innerresult.getString("REPORTING_SPN"));
                            parhelper.setFromdt(CommonFunctions.getFormattedOutputDate1(innerresult.getDate("FROMDATE")));
                            parhelper.setTodt(CommonFunctions.getFormattedOutputDate1(innerresult.getDate("TODATE")));
                            parhelper.setAuthorityempid(innerresult.getString("REPORTING_EMP_ID"));
                            parhelper.setIsPendingReportingAuthority(isPendingReportingAuthority(innerresult.getString("REPORTING_EMP_ID"), taskId));
                            al.add(parhelper);
                        }
                    } else if (spctype.equalsIgnoreCase("M")) {
                        pstamtReporting = con.prepareStatement("SELECT prptid,LMID,FULL_NAME,OFF_AS,OFF_NAME,FROMDATE,TODATE FROM ( SELECT prptid,LMID,ARRAY_TO_STRING(ARRAY[INITIALS, FNAME, MNAME,LNAME], ' ') FULL_NAME, OFF_AS,FROMDATE,TODATE FROM(SELECT prptid,REPORTING_CUR_SPC,REPORTING_EMP_ID,FROMDATE,TODATE FROM PAR_REPORTING_TRAN WHERE PRPTID=? ORDER BY HIERARCHY_NO)T1"
                                + " INNER JOIN LA_MEMBERS ON T1.REPORTING_EMP_ID = LA_MEMBERS.LMID::VARCHAR) LA_MEMBERS"
                                + " INNER JOIN G_OFFICIATING ON LA_MEMBERS.OFF_AS=G_OFFICIATING.OFF_ID");
                        pstamtReporting.setInt(1, resultset.getInt("prptid"));
                        innerresult = pstamtReporting.executeQuery();

                        while (innerresult.next()) {
                            Parauthorityhelperbean parhelper = new Parauthorityhelperbean();
                            parhelper.setAuthid(resultset.getInt("prptid"));
                            parhelper.setAuthorityname(innerresult.getString("FULL_NAME"));
                            parhelper.setAuthorityspn(innerresult.getString("OFF_NAME"));
                            parhelper.setFromdt(CommonFunctions.getFormattedOutputDate1(innerresult.getDate("FROMDATE")));
                            parhelper.setTodt(CommonFunctions.getFormattedOutputDate1(innerresult.getDate("TODATE")));
                            parhelper.setAuthorityempid(innerresult.getString("LMID"));
                            parhelper.setIsPendingReportingAuthority(isPendingReportingAuthority(innerresult.getString("LMID"), taskId));
                            al.add(parhelper);
                        }
                    }
                }
            } else if (authtype.equalsIgnoreCase("REVIEWING")) {
                String spctype = "G";
                pstamt = con.prepareStatement("SELECT PRVTID,REVIEWING_CUR_SPC FROM PAR_REVIEWING_TRAN WHERE PAR_ID=? ORDER BY HIERARCHY_NO");
                pstamt.setInt(1, parId);
                resultset = pstamt.executeQuery();
                while (resultset.next()) {
                    if (resultset.getString("REVIEWING_CUR_SPC") != null && !resultset.getString("REVIEWING_CUR_SPC").equals("")) {
                        if (resultset.getString("REVIEWING_CUR_SPC").length() > 2) {
                            spctype = "G";
                        } else {
                            spctype = "M";
                        }
                    }
                    if (spctype.equalsIgnoreCase("G")) {
                        pstamtReviewing = con.prepareStatement("SELECT prvtid,REVIEWING_EMP_ID,REVIEWING_SPN,INITIALS,F_NAME,M_NAME,L_NAME,FROMDATE,TODATE FROM (SELECT prvtid,GETSPN(REVIEWING_CUR_SPC)REVIEWING_SPN,REVIEWING_EMP_ID,FROMDATE,TODATE FROM PAR_REVIEWING_TRAN WHERE PRVTID=? ORDER BY HIERARCHY_NO)T1 "
                                + "INNER JOIN EMP_MAST ON T1.REVIEWING_EMP_ID = EMP_MAST.EMP_ID");
                        pstamtReviewing.setInt(1, resultset.getInt("prvtid"));
                        innerresult = pstamtReviewing.executeQuery();
                        while (innerresult.next()) {
                            Parauthorityhelperbean parhelper = new Parauthorityhelperbean();
                            parhelper.setAuthid(resultset.getInt("prvtid"));
                            parhelper.setAuthorityname(StringUtils.defaultString(innerresult.getString("INITIALS")) + " " + StringUtils.defaultString(innerresult.getString("F_NAME")) + " " + StringUtils.defaultString(innerresult.getString("M_NAME")) + " " + StringUtils.defaultString(innerresult.getString("L_NAME")));
                            parhelper.setAuthorityspn(innerresult.getString("REVIEWING_SPN"));
                            parhelper.setFromdt(CommonFunctions.getFormattedOutputDate1(innerresult.getDate("FROMDATE")));
                            parhelper.setTodt(CommonFunctions.getFormattedOutputDate1(innerresult.getDate("TODATE")));
                            parhelper.setAuthorityempid(innerresult.getString("REVIEWING_EMP_ID"));
                            parhelper.setIsPendingReviewingAuthority(isPendingReviewingAuthority(innerresult.getString("REVIEWING_EMP_ID"), taskId));
                            al.add(parhelper);
                        }
                    } else if (spctype.equalsIgnoreCase("M")) {
                        pstamtReviewing = con.prepareStatement("SELECT prvtid,LMID,FULL_NAME,OFF_AS,OFF_NAME,FROMDATE,TODATE FROM ( SELECT prvtid,LMID,ARRAY_TO_STRING(ARRAY[INITIALS, FNAME, MNAME,LNAME], ' ') FULL_NAME, OFF_AS,FROMDATE,TODATE FROM(SELECT prvtid,REVIEWING_CUR_SPC,REVIEWING_EMP_ID,FROMDATE,TODATE FROM PAR_REVIEWING_TRAN WHERE PRVTID=? ORDER BY HIERARCHY_NO)T1"
                                + " INNER JOIN LA_MEMBERS ON T1.REVIEWING_EMP_ID = LA_MEMBERS.LMID::VARCHAR) LA_MEMBERS"
                                + " INNER JOIN G_OFFICIATING ON LA_MEMBERS.OFF_AS=G_OFFICIATING.OFF_ID");
                        pstamtReviewing.setInt(1, resultset.getInt("prvtid"));
                        innerresult = pstamtReviewing.executeQuery();
                        while (innerresult.next()) {
                            Parauthorityhelperbean parhelper = new Parauthorityhelperbean();
                            parhelper.setAuthid(resultset.getInt("prvtid"));
                            parhelper.setAuthorityname(innerresult.getString("FULL_NAME"));
                            parhelper.setAuthorityspn(innerresult.getString("OFF_NAME"));
                            parhelper.setFromdt(CommonFunctions.getFormattedOutputDate1(innerresult.getDate("FROMDATE")));
                            parhelper.setTodt(CommonFunctions.getFormattedOutputDate1(innerresult.getDate("TODATE")));
                            parhelper.setAuthorityempid(innerresult.getString("LMID"));
                            parhelper.setIsPendingReviewingAuthority(isPendingReviewingAuthority(innerresult.getString("LMID"), taskId));
                            al.add(parhelper);
                        }
                    }
                }
            } else if (authtype.equalsIgnoreCase("ACCEPTING")) {
                String spctype = "G";
                pstamt = con.prepareStatement("SELECT PACTID,ACCEPTING_CUR_SPC FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=? ORDER BY HIERARCHY_NO");
                pstamt.setInt(1, parId);
                resultset = pstamt.executeQuery();
                while (resultset.next()) {
                    if (resultset.getString("ACCEPTING_CUR_SPC") != null && !resultset.getString("ACCEPTING_CUR_SPC").equals("")) {
                        if (resultset.getString("ACCEPTING_CUR_SPC").length() > 2) {
                            spctype = "G";
                        } else {
                            spctype = "M";
                        }
                    }
                    if (spctype.equalsIgnoreCase("G")) {
                        pstamtAccepting = con.prepareStatement("SELECT pactid,ACCEPTING_EMP_ID,ACCEPTING_SPN,INITIALS,F_NAME,M_NAME,L_NAME,FROMDATE,TODATE FROM(SELECT pactid,GETSPN(ACCEPTING_CUR_SPC)ACCEPTING_SPN,ACCEPTING_EMP_ID,FROMDATE,TODATE FROM PAR_ACCEPTING_TRAN WHERE PACTID=? ORDER BY HIERARCHY_NO)T1 "
                                + "INNER JOIN EMP_MAST ON T1.ACCEPTING_EMP_ID = EMP_MAST.EMP_ID");
                        pstamtAccepting.setInt(1, resultset.getInt("PACTID"));
                        innerresult = pstamtAccepting.executeQuery();
                        while (innerresult.next()) {
                            Parauthorityhelperbean parhelper = new Parauthorityhelperbean();
                            parhelper.setAuthid(resultset.getInt("pactid"));
                            parhelper.setAuthorityname(StringUtils.defaultString(innerresult.getString("INITIALS")) + " " + StringUtils.defaultString(innerresult.getString("F_NAME")) + " " + StringUtils.defaultString(innerresult.getString("M_NAME")) + " " + StringUtils.defaultString(innerresult.getString("L_NAME")));
                            parhelper.setAuthorityspn(innerresult.getString("ACCEPTING_SPN"));
                            parhelper.setFromdt(CommonFunctions.getFormattedOutputDate1(innerresult.getDate("FROMDATE")));
                            parhelper.setTodt(CommonFunctions.getFormattedOutputDate1(innerresult.getDate("TODATE")));
                            parhelper.setAuthorityempid(innerresult.getString("ACCEPTING_EMP_ID"));
                            parhelper.setIsPendingAcceptingAuthority(isPendingAcceptingAuthority(innerresult.getString("ACCEPTING_EMP_ID"), taskId));
                            al.add(parhelper);
                        }
                    } else if (spctype.equalsIgnoreCase("M")) {
                        pstamtAccepting = con.prepareStatement("SELECT pactid,LMID,FULL_NAME,OFF_AS,OFF_NAME,FROMDATE,TODATE FROM ( SELECT pactid,LMID,ARRAY_TO_STRING(ARRAY[INITIALS, FNAME, MNAME,LNAME], ' ') FULL_NAME, OFF_AS,FROMDATE,TODATE FROM(SELECT pactid,ACCEPTING_CUR_SPC,ACCEPTING_EMP_ID,FROMDATE,TODATE FROM PAR_ACCEPTING_TRAN WHERE PACTID=? ORDER BY HIERARCHY_NO)T1"
                                + " INNER JOIN LA_MEMBERS ON T1.ACCEPTING_EMP_ID = LA_MEMBERS.LMID::VARCHAR) LA_MEMBERS"
                                + " INNER JOIN G_OFFICIATING ON LA_MEMBERS.OFF_AS=G_OFFICIATING.OFF_ID");
                        pstamtAccepting.setInt(1, resultset.getInt("PACTID"));
                        innerresult = pstamtAccepting.executeQuery();
                        while (innerresult.next()) {
                            Parauthorityhelperbean parhelper = new Parauthorityhelperbean();
                            parhelper.setAuthid(resultset.getInt("pactid"));
                            parhelper.setAuthorityname(innerresult.getString("FULL_NAME"));
                            parhelper.setAuthorityspn(innerresult.getString("OFF_NAME"));
                            parhelper.setFromdt(CommonFunctions.getFormattedOutputDate1(innerresult.getDate("FROMDATE")));
                            parhelper.setTodt(CommonFunctions.getFormattedOutputDate1(innerresult.getDate("TODATE")));
                            parhelper.setAuthorityempid(innerresult.getString("LMID"));
                            parhelper.setIsPendingAcceptingAuthority(isPendingAcceptingAuthority(innerresult.getString("LMID"), taskId));
                            al.add(parhelper);
                        }
                    }
                }
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstamt, pstamtReporting, pstamtReviewing, pstamtAccepting);
            DataBaseFunctions.closeSqlObjects(innerresult, resultset);
            //DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public String isPendingReviewingAuthority(String empid, int taskid) {

        Statement st = null;
        ResultSet rs = null;
        Connection con = null;
        String isPending = "N";
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            String sql = "SELECT STATUS_ID FROM TASK_MASTER WHERE PENDING_AT='" + empid + "' AND TASK_ID='" + taskid + "'";
            rs = st.executeQuery(sql);
            if (rs.next()) {
                if (rs.getInt("STATUS_ID") == 7) {
                    isPending = "Y";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isPending;
    }

    @Override
    public String isPendingAcceptingAuthority(String empid, int taskid) {

        Statement stmt = null;
        ResultSet resultset = null;
        Connection con = null;
        PreparedStatement pstamt = null;

        String isPending = "N";
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();
            String sql = "SELECT STATUS_ID FROM TASK_MASTER WHERE PENDING_AT='" + empid + "' AND TASK_ID='" + taskid + "'";
            resultset = stmt.executeQuery(sql);
            if (resultset.next()) {
                if (resultset.getInt("STATUS_ID") == 8) {
                    isPending = "Y";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            DataBaseFunctions.closeSqlObjects(resultset);
            DataBaseFunctions.closeSqlObjects(pstamt);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return isPending;
    }

    @Override
    public String getAuthorityType(String empId, String spc) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String name = "";
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            if (empId != null) {
                if (empId.length() > 2) {
                    rs = st.executeQuery("SELECT FULLNAME,SPN FROM ( "
                            + "SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULLNAME,'" + spc + "'::text cur_spc FROM EMP_MAST WHERE EMP_ID='" + empId + "') EMP_MAST "
                            + " INNER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC");
                    while (rs.next()) {

                        name = rs.getString("fullname");
                        if (rs.getString("spn") != null) {
                            name = name + " (" + rs.getString("SPN") + ")";
                        }

                    }
                } else if (empId.length() <= 2) {
                    rs = st.executeQuery("select fullname,off_name from ( select ARRAY_TO_STRING(ARRAY[INITIALS, FNAME, MNAME,LNAME], ' ') fullname,off_as from LA_MEMBERS where lmid=" + empId + ") la left outer join g_officiating on la.off_as=g_officiating.off_id");
                    if (rs.next()) {

                        name = rs.getString("fullname");
                        if (rs.getString("off_name") != null) {
                            name = name + " (" + rs.getString("off_name") + ")";
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return name;
    }

    public String isPendingReportingAuthority(Connection con, String empid, int taskid) throws Exception {

        PreparedStatement pst = null;
        ResultSet rs = null;

        String isPending = "N";
        try {
            String sql = "SELECT STATUS_ID FROM TASK_MASTER WHERE PENDING_AT=? AND TASK_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, taskid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("STATUS_ID") == 6) {
                    isPending = "Y";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            //DataBaseFunctions.closeSqlObjects(con);
        }
        return isPending;
    }

    @Override
    public String getGradingName(int gradeid) {

        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Connection con = null;
        String gradename = "";
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT GRADE_ID,GRADE_NAME FROM G_PAR_GRADE WHERE GRADE_ID= ?");
            pstmt.setInt(1, gradeid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                gradename = rs.getString("GRADE_NAME");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return gradename;
    }

    @Override
    public ArrayList getReviewingData(ParApplyForm paf) {
        String isrevcomp = "Y";
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList reviewingbeans = new ArrayList();
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT SUBMITTED_ON,IS_COMPLETED,PRVTID,OVERALLGRADING,GENERALASSESSMENT,REVIEWING_EMP_ID,REVIEWING_CUR_SPC,orginal_filename_grading_document_reviewing,disk_filename_grading_document_reviewing FROM PAR_REVIEWING_TRAN WHERE PAR_ID=? ORDER BY HIERARCHY_NO");
            pstmt.setInt(1, paf.getParId());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (rs.getString("IS_COMPLETED") == null || rs.getString("IS_COMPLETED").equals("") || rs.getString("IS_COMPLETED").equals("N")) {
                    isrevcomp = "N";
                } else if (rs.getString("IS_COMPLETED").equals("F")) {
                    isrevcomp = "F";
                } else if (rs.getString("IS_COMPLETED").equals("Y")) {
                    isrevcomp = "Y";
                } else if (rs.getString("IS_COMPLETED").equals("T")) {
                    isrevcomp = "T";
                }

                int reftableid = paf.getRefid();
                paf.setIsreviewingcompleted(isrevcomp);
                if (paf.getParstatus() == 7 && rs.getInt("PRVTID") == reftableid) {
                    paf.setSltReviewGrading(rs.getString("OVERALLGRADING"));
                    paf.setReviewingNote(rs.getString("GENERALASSESSMENT"));
                }
                ReviewingHelperBean rhb = new ReviewingHelperBean();
                rhb.setIsreviewingcompleted(isrevcomp);
                rhb.setSltReviewGrading(rs.getInt("OVERALLGRADING"));
                rhb.setSltReviewGrading1(getGradingName(rs.getInt("OVERALLGRADING")));
                rhb.setReviewingNote(rs.getString("GENERALASSESSMENT"));

                rhb.setReviewingauthName(getAuthorityType(rs.getString("REVIEWING_EMP_ID"), rs.getString("REVIEWING_CUR_SPC")));
                rhb.setSubmittedon(CommonFunctions.getFormattedOutputDate1(rs.getDate("SUBMITTED_ON")));

                rhb.setOriginalFileNamegradingDocumentReviewing(rs.getString("orginal_filename_grading_document_reviewing"));
                rhb.setDiskFileNameforgradingDocumentReviewing(rs.getString("disk_filename_grading_document_reviewing"));
                rhb.setPrtid(rs.getInt("PRVTID"));
                reviewingbeans.add(rhb);
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();

        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return reviewingbeans;
    }

    @Override
    public ArrayList getAcceptingData(ParApplyForm paf) {
        String isacccomp = "Y";
        ArrayList acceptingbeans = new ArrayList();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT SUBMITTED_ON,ACCEPTING_EMP_ID,ACCEPTING_CUR_SPC,IS_COMPLETED,OVERALLGRADING,REMARK,PACTID,orginal_filename_grading_document_acceptting,disk_filename_grading_document_accepting FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=? ORDER BY HIERARCHY_NO");
            pstmt.setInt(1, paf.getParId());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (rs.getString("IS_COMPLETED") == null || rs.getString("IS_COMPLETED").equals("") || rs.getString("IS_COMPLETED").equals("N")) {
                    isacccomp = "N";
                } else if (rs.getString("IS_COMPLETED") == null || rs.getString("IS_COMPLETED").equals("") || rs.getString("IS_COMPLETED").equals("T")) {
                    isacccomp = "T";
                } else {
                    isacccomp = "Y";
                }
                int reftableid = paf.getRefid();
                paf.setIsacceptingcompleted(isacccomp);
                if (paf.getParstatus() == 8 && rs.getInt("PACTID") == reftableid) {
                    paf.setSltAcceptingGrading(rs.getString("OVERALLGRADING"));
                    paf.setAcceptingNote(rs.getString("REMARK"));
                }
                AcceptingHelperBean ahb = new AcceptingHelperBean();
                ahb.setIsacceptingcompleted(isacccomp);
                ahb.setAcceptingNote(rs.getString("REMARK"));
                ahb.setSltAcceptingGrading(rs.getInt("OVERALLGRADING"));
                ahb.setSltAcceptingGrading1(getGradingName(rs.getInt("OVERALLGRADING")));
                ahb.setAcceptingauthName(getAuthorityType(rs.getString("ACCEPTING_EMP_ID"), rs.getString("ACCEPTING_CUR_SPC")));
                ahb.setSubmittedon(CommonFunctions.getFormattedOutputDate1(rs.getDate("SUBMITTED_ON")));

                ahb.setOriginalFileNamegradingDocumentAccepting(rs.getString("orginal_filename_grading_document_acceptting"));
                ahb.setDiskFileNameforgradingDocumentAccepting(rs.getString("disk_filename_grading_document_accepting"));
                ahb.setPactid(rs.getInt("PACTID"));
                acceptingbeans.add(ahb);
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return acceptingbeans;
    }

    @Override
    public String getHasAdminPriviliged(LoginUserBean lub) {
        String hasAdminPriv = "N";
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT ROLE_ID FROM G_PRIVILEGE_MAP WHERE SPC=? AND ROLE_ID='10' GROUP BY ROLE_ID");
            pstmt.setString(1, lub.getLoginspc());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                hasAdminPriv = "Y";
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return hasAdminPriv;
    }

    @Override
    public ArrayList getCadreListForCustdian(String spc) {
        Connection con = null;
        ArrayList cadreList = new ArrayList();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select par_authority_admin.CADRE_CODE,g_cadre.cadre_name,department_name,post_grp from par_authority_admin  "
                    + "inner join g_cadre ON  par_authority_admin.CADRE_CODE = g_cadre.cadre_code "
                    + "inner join g_department on g_department.department_code=g_cadre.department_code where spc= ?");
            pstmt.setString(1, spc);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Cadre cadre = new Cadre();
                cadre.setCadreCode(rs.getString("CADRE_CODE"));
                cadre.setCadreName(rs.getString("cadre_name"));
                cadre.setDeptName(rs.getString("department_name"));
                cadre.setPostGrp(rs.getString("post_grp"));
                cadreList.add(cadre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return cadreList;
    }

    @Override
    public ParAssignPrivilage getAssignPrivilage(String spc) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ParAssignPrivilage pap = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select * from PAR_AUTHORITY_ADMIN where spc=?");
            pstmt.setString(1, spc);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                pap = new ParAssignPrivilage();
                pap.setCadreCode(rs.getString("cadre_code"));
                pap.setOffCode(rs.getString("off_code"));
                pap.setPostGrp(rs.getString("post_grp"));
                pap.setSpc(rs.getString("spc"));
                pap.setAuthorizationType(rs.getString("authorization_type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return pap;
    }

    @Override
    public void UpdateCadre(ParAdminProperties parAdminProperties) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            Long curtime = new Date().getTime();

            String IP = CommonFunctions.getISPIPAddress();

            pstmt = con.prepareStatement("UPDATE PAR_MASTER SET CADRE_CODE=?, post_group=?,cadre_updated_byadmin_ondate=?,cadre_updated_by=?,cadre_updated_by_ip=? WHERE PARID=?");
            pstmt.setString(1, parAdminProperties.getCadrecode());
            pstmt.setString(2, parAdminProperties.getEmpGroup());
            pstmt.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
            pstmt.setString(4, parAdminProperties.getCadreUpdatedBy());
            pstmt.setString(5, IP);
            pstmt.setInt(6, parAdminProperties.getParId());
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void SaveMapPost(ParApplyForm paf) {
        String msgType = "";
        String msg = "";
        int retVal = 0;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT emp_id FROM par_master  WHERE parid=?");
            pstmt.setInt(1, paf.getParId());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                paf.setEmpId(rs.getString("emp_id"));
            }

            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            pstmt = con.prepareStatement("SELECT CUR_SPC FROM EMP_MAST WHERE CUR_SPC=?");
            pstmt.setString(1, paf.getSpc());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                msgType = "info";
                msg = "Susbstantive Post Already Used in PostGres!";
            } else {
                pstmt = con.prepareStatement("UPDATE EMP_MAST SET CUR_OFF_CODE=?,CUR_SPC=?,DEP_CODE='02' WHERE EMP_ID=?");
                pstmt.setString(1, paf.getOffcode());
                pstmt.setString(2, paf.getSpc());
                pstmt.setString(3, paf.getEmpId());
                retVal = pstmt.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pstmt);

                pstmt = con.prepareStatement("UPDATE G_SPC SET is_sanctioned='Y' WHERE spc=?");
                pstmt.setString(1, paf.getSpc());
                pstmt.executeUpdate();

                if (retVal > 0) {
                    msgType = "info";
                    msg = "Post Saved Successfully in PostGres";
                } else {
                    msgType = "error";
                    msg = "There is Some Error updating PostGres!";
                }
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public String parReviewRemarks(ParApplyForm paf) {
        String reviewedRemarks = "";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            int taskId = 0;
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT PARID,is_force_forward,is_force_forward_from_reviewing,post_group,par_status from par_master where parid=?");
            pstmt.setInt(1, paf.getParId());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                paf.setParId(rs.getInt("PARID"));
                paf.setIsForceForward(rs.getString("is_force_forward"));
                paf.setIsForceForwardFromReviewing(rs.getString("is_force_forward_from_reviewing"));
                paf.setEmpGroup(rs.getString("post_group"));
                paf.setParstatus(rs.getInt("par_status"));

                paf.setReportingauth(getParAuthority("REPORTING", paf.getParId(), taskId));
                paf.setReviewingauth(getParAuthority("REVIEWING", paf.getParId(), taskId));
                paf.setAcceptingauth(getParAuthority("ACCEPTING", paf.getParId(), taskId));

                paf.setReportingdata(getReportingData(paf));
                paf.setReviewingdata(getReviewingData(paf));
                paf.setAcceptingdata(getAcceptingData(paf));
            }
            String reportingRemarks = "";
            String reviewingRemarks = "";
            String acceptingRemarks = "";

            int parstatus = rs.getInt("par_status");

            String reportingRevert = "";
            String reviewingRevert = "";
            String acceptingRevert = "";

            if (parstatus == 16 && paf.getReportingdata().size() == 0) {
                reportingRevert = "Y";
            }
            if (parstatus == 18 && paf.getReviewingdata().size() == 0) {
                reviewingRevert = "Y";
            }
            if (parstatus == 19 && paf.getAcceptingdata().size() == 0) {
                acceptingRevert = "Y";
            }

            ReportingHelperBean rhb = null;
            ReviewingHelperBean rvb = null;
            AcceptingHelperBean ahb = null;

            for (int i = 0; i < paf.getReportingdata().size(); i++) {
                rhb = (ReportingHelperBean) paf.getReportingdata().get(i);
                if (rhb.getIsreportingcompleted() == null || rhb.getIsreportingcompleted().equals("") || rhb.getIsreportingcompleted().equals("N")) {
                    reportingRemarks = "N";
                } else if (rhb.getIsreportingcompleted().equals("Y")) {
                    reportingRemarks = "Y";
                    break;
                } else if (rhb.getIsreportingcompleted().equals("F")) {
                    reportingRemarks = "N";
                }
            }

            for (int i = 0; i < paf.getReviewingdata().size(); i++) {
                rvb = (ReviewingHelperBean) paf.getReviewingdata().get(i);
                //System.out.println("rvb.getIsreviewingcompleted()" +rvb.getIsreviewingcompleted());
                if (rvb.getIsreviewingcompleted() == null || rvb.getIsreviewingcompleted().equals("") || rvb.getIsreviewingcompleted().equals("N")) {
                    reviewingRemarks = "N";
                } else if (rvb.getIsreviewingcompleted().equals("Y")) {
                    reviewingRemarks = "Y";
                    break;
                } else if (rvb.getIsreviewingcompleted().equals("F")) {
                    reviewingRemarks = "N";
                }
            }
            for (int i = 0; i < paf.getAcceptingdata().size(); i++) {
                ahb = (AcceptingHelperBean) paf.getAcceptingdata().get(i);
                if (ahb.getIsacceptingcompleted() == null || ahb.getIsacceptingcompleted().equals("") || ahb.getIsacceptingcompleted().equals("N")) {
                    acceptingRemarks = "N";
                } else if (ahb.getIsacceptingcompleted().equals("Y")) {
                    acceptingRemarks = "Y";
                    break;
                } else if (ahb.getIsacceptingcompleted().equals("F")) {
                    acceptingRemarks = "N";
                }
            }

            String orderNo = "";
            if (paf.getEmpGroup().equals("A")) {
                orderNo = "1199";
            } else if (paf.getEmpGroup().equals("B")) {
                orderNo = "1200";
            }

            if (reportingRemarks.equals("N") && reviewingRemarks.equals("Y") && acceptingRemarks.equals("Y")) {
                reviewedRemarks = "The remarks recorded by the Reviewing / Accepting Authority are accepted since the Reporting Authority has not recorded his remarks within schedule date as per para 7(iii) of G.A (SE) Deptt. PAR guideline issued vide Memo No." + orderNo + "/PRO dtd. 26.04.2006.";
            } else if (reportingRemarks.equals("Y") && reviewingRemarks.equals("N") && acceptingRemarks.equals("Y")) {
                reviewedRemarks = "The remarks recorded by the Reporting/ Accepting Authority are accepted since the Reviewing Authority has not recorded his remarks within schedule date as per para 7(iii) of G.A (SE) Deptt. PAR guideline issued vide Memo No. " + orderNo + "/PRO dtd. 26.04.2006.";
            } else if (reportingRemarks.equals("N") && reviewingRemarks.equals("Y") && acceptingRemarks.equals("N")) {
                reviewedRemarks = "The remarks recorded by the Reviewing Authority are accepted since the Reporting Authority and Accepting Authority have not recorded their remarks within schedule date as per para 7(iii) of G.A (SE) Deptt. PAR guideline issued vide Memo No. " + orderNo + "/PRO dtd. 26.04.2006.";
            } else if (reportingRemarks.equals("Y") && reviewingRemarks.equals("N") && acceptingRemarks.equals("N")) {
                reviewedRemarks = "The remarks recorded by the Reporting Authority are accepted since the Reviewing Authority and Accepting Authority have not recorded their remarks within schedule date as per para 7(iii) of G.A (SE) Deptt. PAR guideline issued vide Memo No. " + orderNo + "/PRO dtd. 26.04.2006.";
            } else if (reportingRemarks.equals("N") && reviewingRemarks.equals("N") && acceptingRemarks.equals("N")) {
                reviewedRemarks = "\"No remarks\" are recorded under para 11(e) of G.A (SE) Deptt. PAR guideline issued vide Memo No. 1199/PRO dtd. 26.04.2006 as the  Reporting, Reviewing and Accepting authorities have not recorded their remarks within schedule date. This shall be taken into account during Promotion/Selection/MACP etc. as per Category-II of G.A & P.G (SE) Deptt. letter No. 1103/SE dtd. 19.05.2020.";
            } else if (reportingRemarks.equals("Y") && reviewingRemarks.equals("Y") && acceptingRemarks.equals("N")) {
                reviewedRemarks = "The remarks recorded by the Reporting/Reviewing Authority are accepted  since the Accepting Authority has not recorded his remarks within schedule date as per para 7(iii) of G.A (SE) Deptt. PAR guideline issued vide Memo No. " + orderNo + "/PRO dtd. 26.04.2006 .";
            } else if (reportingRemarks.equals("N") && reviewingRemarks.equals("N") && acceptingRemarks.equals("Y")) {
                reviewedRemarks = "The remarks recorded by the Accepting Authority are accepted since the Reporting and Reviewing authorities have not recorded their remarks within schedule date as per para 7(iii) of G.A (SE) Deptt.PAR guideline issued vide Memo No. " + orderNo + "/PRO dtd.26.O4.2006.";
            } else if (reportingRevert.equals("Y")) {
                reviewedRemarks = "No remarks are recorded under para 11(e) of G.A (SE) Deptt. PAR guideline issued vide Memo No. 1199/PRO dtd. 26.04.2006 as the  Reporting, Reviewing and Accepting authorities have not recorded their remarks within schedule date. This shall be taken into account during Promotion/Selection/MACP etc. as per Category-II of G.A & P.G (SE) Deptt. letter No. 1103/SE dtd. 19.05.2020.";
            } else if (reviewingRevert.equals("Y") && reportingRemarks.equals("N")) {
                reviewedRemarks = "No remarks are recorded under para 11(e) of G.A (SE) Deptt. PAR guideline issued vide Memo No. 1199/PRO dtd. 26.04.2006 as the  Reporting, Reviewing and Accepting authorities have not recorded their remarks within schedule date. This shall be taken into account during Promotion/Selection/MACP etc. as per Category-II of G.A & P.G (SE) Deptt. letter No. 1103/SE dtd. 19.05.2020.";
            } else if (reviewingRevert.equals("Y") && reportingRemarks.equals("Y")) {
                reviewedRemarks = "The remarks recorded by the Reporting Authority are accepted since the Reviewing Authority and Accepting Authority have not recorded their remarks within schedule date as per para 7(iii) of G.A (SE) Deptt. PAR guideline issued vide Memo No. " + orderNo + "/PRO dtd. 26.04.2006.";
            } else if (acceptingRevert.equals("Y") && reportingRemarks.equals("Y") && reviewingRemarks.equals("Y")) {
                reviewedRemarks = "The remarks recorded by the Reporting / Reviewing Authority are accepted since the Accepting Authority have not recorded their remarks within schedule date as per para 7(iii) of G.A (SE) Deptt. PAR guideline issued vide Memo No. " + orderNo + "/PRO dtd. 26.04.2006.";
            } else if (acceptingRevert.equals("Y") && reportingRemarks.equals("N") && reviewingRemarks.equals("N")) {
                reviewedRemarks = "No remarks are recorded under para 11(e) of G.A (SE) Deptt. PAR guideline issued vide Memo No. 1199/PRO dtd. 26.04.2006 as the  Reporting, Reviewing and Accepting authorities have not recorded their remarks within schedule date. This shall be taken into account during Promotion/Selection/MACP etc. as per Category-II of G.A & P.G (SE) Deptt. letter No. 1103/SE dtd. 19.05.2020.";
            } else if (reportingRemarks.equals("Y") && reviewingRemarks.equals("Y") && acceptingRevert.equals("Y")) {
                reviewedRemarks = "The remarks recorded by the Reporting/Reviewing Authority are accepted  since the Accepting Authority has not recorded his remarks within schedule date as per para 7(iii) of G.A (SE) Deptt. PAR guideline issued vide Memo No. " + orderNo + "/PRO dtd. 26.04.2006 .";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return reviewedRemarks;
    }

    @Override
    public void markParAsReviewed(String reviewedByEmpId, String reviewedBySPC, int parId
    ) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            con = dataSource.getConnection();
            Long curtime = new Date().getTime();
            pstmt = con.prepareStatement("UPDATE PAR_MASTER SET is_reviewed=?,reviewed_ondate=?,reviewed_by_spc=?,reviewed_by_empid=? WHERE PARID=?");
            pstmt.setString(1, "Y");
            pstmt.setTimestamp(2, new Timestamp(curtime));
            pstmt.setString(3, reviewedBySPC);
            pstmt.setString(4, reviewedByEmpId);
            pstmt.setInt(5, parId);
            int i = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            DataBaseFunctions.closeSqlObjects(res, pstmt, con);
        }
    }

    @Override
    public void saveUnReviewedPARDetail(ParUnReviewedDetailBean parUnReviewedDetailBean
    ) {
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet res = null;
        try {
            con = dataSource.getConnection();
            Long curtime = new Date().getTime();
            pst = con.prepareStatement("INSERT INTO par_unreviewed_log(unreviewed_on,unreviewed_reason,unreviewed_by,parid) VALUES(?,?,?,?)");
            pst.setTimestamp(1, new Timestamp(curtime));
            pst.setString(2, parUnReviewedDetailBean.getUnReviewedReason());
            pst.setString(3, parUnReviewedDetailBean.getUnReviewedBy());
            pst.setInt(4, parUnReviewedDetailBean.getParId());
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE PAR_MASTER SET is_reviewed=null,reviewed_ondate=null,reviewed_by_spc=null,reviewed_by_empid=null WHERE PARID=?");
            pst.setInt(1, parUnReviewedDetailBean.getParId());
            int i = pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pst, con);
        }
    }

    @Override
    public void saveDeletePARDetail(ParDeleteDetailBean parDeleteDetailBean) {
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet res = null;
        try {
            con = dataSource.getConnection();
            Long curtime = new Date().getTime();
            pst = con.prepareStatement("INSERT INTO par_delete_log(deleted_on,deleted_reason,deleted_by,parid) VALUES(?,?,?,?)");
            pst.setTimestamp(1, new Timestamp(curtime));
            pst.setString(2, parDeleteDetailBean.getDeletedReason());
            pst.setString(3, parDeleteDetailBean.getDeletedBy());
            pst.setInt(4, parDeleteDetailBean.getParId());
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE PAR_MASTER SET is_deleted=? WHERE PARID=?");
            pst.setString(1, "Y");
            pst.setInt(2, parDeleteDetailBean.getParId());
            int i = pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pst, con);
        }
    }

    @Override
    public void parPdfReview(ParApplyForm paf, Document document) {

        try {
            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");

            PdfPTable table = null;
            PdfPTable innertable = null;

            table = new PdfPTable(4);
            table.setWidths(new int[]{2, 3, 3, 3});
            table.setWidthPercentage(100);

            PdfPCell cell = null;
            PdfPCell innercell = null;

            cell = new PdfPCell(new Phrase("Performance Appraisal Report (PAR) for Group  'A' & 'B' officers of Govt. of Orissa", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Transmission Record", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("(To be filled in by Appraisee )", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            Phrase phrs = new Phrase();
            Chunk c1 = new Chunk("Financial Year : ", f1);
            Chunk c2 = new Chunk(paf.getFiscalYear(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD | Font.UNDERLINE));
            Chunk c3 = new Chunk(" (for the period from ", f1);
            Chunk c4 = new Chunk(paf.getPrdFrmDate(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
            Chunk c5 = new Chunk(" to ", f1);
            Chunk c6 = new Chunk(paf.getPrdToDate(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
            Chunk c7 = new Chunk(")", f1);
            phrs.add(c1);
            phrs.add(c2);
            phrs.add(c3);
            phrs.add(c4);
            phrs.add(c5);
            phrs.add(c6);
            phrs.add(c7);
            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            phrs = new Phrase();
            c1 = new Chunk("Name & Designation of the Officer Reported Upon     ", f1);
            c2 = new Chunk(paf.getEmpName(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD | Font.UNDERLINE));
            phrs.add(c1);
            phrs.add(c2);
            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(paf.getApprisespc(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Service and Group (A/B) to which the  Officer belongs ", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(paf.getEmpService(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD | Font.UNDERLINE)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Group - " + StringUtils.defaultString(paf.getEmpGroup()), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD | Font.UNDERLINE)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Details of Transmission / Movement of PAR", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("(To be filled in at the time of transmission", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("by respective officer/staff)", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Transmission by", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Transmitted to whom (Name, Designation &Address)", f1));
            cell.setColspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Appraisee", f1));
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(paf.getEmpName(), f1));
            cell.setColspan(3);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(paf.getApprisespc(), f1));
            cell.setColspan(3);
            cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.RIGHT);
            table.addCell(cell);
            /*cell = new PdfPCell(new Phrase("", f1));
             cell.setBorder(Rectangle.LEFT);
             table.addCell(cell);
             cell = new PdfPCell(new Phrase("", f1));
             cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
             table.addCell(cell);*/

            cell = new PdfPCell(new Phrase("Reporting\nAuthority", f1));
            cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.BOTTOM);
            table.addCell(cell);
            ArrayList reportAuthlist = paf.getReportingauth();
            Parauthorityhelperbean parhelper = null;
            String reportAuthName = "";
            String reportAuthDesg = "";
            int slno = 0;
            for (int i = 0; i < reportAuthlist.size(); i++) {
                parhelper = (Parauthorityhelperbean) reportAuthlist.get(i);
                slno = i + 1;
                if (reportAuthName == "") {
                    reportAuthName = slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + ")\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
                } else {
                    reportAuthName = reportAuthName + "\n\n" + slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + ")\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
                }
                //reportAuthDesg = parhelper.getAuthorityspn();
            }
            cell = new PdfPCell(new Phrase(reportAuthName, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(3);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            table.addCell(cell);

            ArrayList reviewAuthlist = paf.getReviewingauth();
            parhelper = null;
            String reviewAuthName = "";
            String reviewAuthDesg = "";
            slno = 0;
            for (int i = 0; i < reviewAuthlist.size(); i++) {
                parhelper = (Parauthorityhelperbean) reviewAuthlist.get(i);
                slno = i + 1;
                if (reviewAuthName == "") {
                    reviewAuthName = slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + ")\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
                } else {
                    reviewAuthName = reviewAuthName + "\n\n" + slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + ")\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
                }
                //reviewAuthDesg = parhelper.getAuthorityspn();
            }
            cell = new PdfPCell(new Phrase("Reviewing\nAuthority", f1));
            cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.BOTTOM);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(reviewAuthName, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(3);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            table.addCell(cell);

            ArrayList acceptAuthlist = paf.getAcceptingauth();
            parhelper = null;
            String acceptAuthName = "";
            String acceptAuthDesg = "";
            slno = 0;
            for (int i = 0; i < acceptAuthlist.size(); i++) {
                parhelper = (Parauthorityhelperbean) acceptAuthlist.get(i);
                slno = i + 1;
                if (acceptAuthName == "") {
                    acceptAuthName = slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + "))\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
                } else {
                    acceptAuthName = acceptAuthName + "\n\n" + slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + "))\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
                }
                //acceptAuthDesg = parhelper.getAuthorityspn();
            }
            cell = new PdfPCell(new Phrase("Accepting \nAuthority", f1));
            cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(acceptAuthName, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(3);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            table.addCell(cell);

            document.add(table);
            document.newPage();
            //Second Page

            table = new PdfPTable(4);
            table.setWidths(new int[]{2, 3, 3, 3});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("PERFORMANCE APPRAISAL REPORT", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("for", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Group A and Group B Officers of Govt. of Orissa.", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            phrs = new Phrase();
            c1 = new Chunk("Report for the financial year ", f1);
            c2 = new Chunk(paf.getFiscalYear(), new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
            phrs.add(c1);
            phrs.add(c2);
            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            phrs = new Phrase();
            c1 = new Chunk("(  Period from ", f1);
            c2 = new Chunk(paf.getPrdFrmDate(), new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
            c3 = new Chunk(" to ", f1);
            c4 = new Chunk(paf.getPrdToDate(), new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
            c5 = new Chunk(")", f1);
            phrs.add(c1);
            phrs.add(c2);
            phrs.add(c3);
            phrs.add(c4);
            phrs.add(c5);
            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("PERSONAL DATA", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.TOP | Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + "PART-I", f1));
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 33) + "(To be filled in by the Appraisee )", f1));
            cell.setColspan(3);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 1. Full Name of the Officer:", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(paf.getEmpName(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(3);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            //try{
            /*String url = filePath + paf.getEmpId() + ".jpg";
             File f = null;
             f = new File(url);
             Image img1 = null;
             if (f.exists()) {
             img1 = Image.getInstance(url);
             }*/
            /*if (img1 != null) {
             img1.scalePercent(15f);
             cell = new PdfPCell(img1);
             cell.setRowspan(3);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setBorder(Rectangle.RIGHT);
             cell.setBorderWidth(1f);
             table.addCell(cell);
             //} else {*/
            cell = new PdfPCell();
            cell.setRowspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            //}
            //}catch(NullPointerException e){

            //}
            cell = new PdfPCell(new Phrase(" 2. Date of Birth:", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(paf.getDob(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            //cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(3);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 3. Service to which the Officer belongs:", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(paf.getEmpService(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 4. Group to which the Officer belongs(A or B):", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(paf.getEmpGroup(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 5. Designation  during the period of Report:", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(paf.getApprisespc(), new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 6. Office to which posted with Head Quarters:", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            String office = "";
            if (paf.getSltHeadQuarter() != null && !paf.getSltHeadQuarter().equals("")) {
                office = paf.getEmpOffice() + "," + paf.getSltHeadQuarter();
            } else {
                office = paf.getEmpOffice();
            }
            cell = new PdfPCell(new Phrase(office, new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 7. Period(s) of absence (on leave, training etc.,\n    if 30 days or more).Please mention date(s):", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            AbsenteeBean pab = null;
            ArrayList leaveAbsentee = paf.getLeaveAbsentee();
            String leaveFrmDt = "";
            String leaveToDt = "";
            for (int i = 0; i < leaveAbsentee.size(); i++) {
                pab = (AbsenteeBean) leaveAbsentee.get(i);
                innertable = new PdfPTable(2);
                innertable.setWidths(new int[]{2, 2});
                innertable.setWidthPercentage(100);
                //innercell = new 
                leaveFrmDt = pab.getFromDate();
                leaveToDt = pab.getToDate();
            }
            if ((leaveFrmDt != null && !leaveFrmDt.equals("")) && (leaveToDt != null && !leaveToDt.equals(""))) {
                cell = new PdfPCell(new Phrase("From " + leaveFrmDt + " to " + leaveToDt, f1));
            } else {
                cell = new PdfPCell();
            }
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 8. Name & Designation of the Reporting Authority\n    and period worked under him/her:", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(reportAuthName, new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 47), f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 9. Name & Designation of the Reviewing Authority\n    and period worked under him/her:", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(reviewAuthName, new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 47), f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 10. Name & Designation of the Accepting Authority\n    and period worked under him/her:", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(acceptAuthName, new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 47), f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(40);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Signature of the Appraisee", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(40);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            document.add(table);
            document.newPage();

            //Third Page
            table = new PdfPTable(5);
            table.setWidths(new int[]{1, 4, 5, 3, 3});
            table.setWidthPercentage(100);

            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("    PART-II" + StringUtils.repeat(" ", 51) + "SELF-APPRAISAL", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("(To be filled in by the Appraisee )", f1));
            cell.setColspan(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setFixedHeight(30);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 1. Brief description of duties/tasks entrusted.(in about 200 words)", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(htmlToText(paf.getSelfappraisal()), f1));
            cell.setColspan(4);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setFixedHeight(100);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 2. Physical/Financial Targets & Achievements", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            innertable = new PdfPTable(5);
            innertable.setWidths(new int[]{2, 2, 2, 2, 2});
            innertable.setWidthPercentage(100);

            innercell = new PdfPCell(new Phrase("SL. No", f1));
            innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //innercell.setBorder(Rectangle.LEFT);
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Task", f1));
            innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //innercell.setBorder(Rectangle.LEFT);
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Target", f1));
            innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //innercell.setBorder(Rectangle.LEFT);
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Achievement", f1));
            innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //innercell.setBorder(Rectangle.LEFT);
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("% of Achievement", f1));
            innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
            innertable.addCell(innercell);

            cell = new PdfPCell(innertable);
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            ArrayList achievementList = paf.getAchivementList();
            if (achievementList != null && achievementList.size() > 0) {
                ParAchievement ab = null;
                for (int i = 0; i < achievementList.size(); i++) {
                    ab = (ParAchievement) achievementList.get(i);
                    innertable = new PdfPTable(5);
                    innertable.setWidths(new int[]{2, 2, 2, 2, 2});
                    innertable.setWidthPercentage(100);
                    int j = i + 1;
                    innercell = new PdfPCell(new Phrase(j + "", f1));
                    innercell.setBorder(Rectangle.NO_BORDER);
                    innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(ab.getTask(), f1));
                    innercell.setBorder(Rectangle.NO_BORDER);
                    innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(ab.getTarget(), f1));
                    innercell.setBorder(Rectangle.NO_BORDER);
                    innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(ab.getAchievement(), f1));
                    innercell.setBorder(Rectangle.NO_BORDER);
                    innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(ab.getPercentAchievement(), f1));
                    innercell.setBorder(Rectangle.NO_BORDER);
                    innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    innertable.addCell(innercell);

                    cell = new PdfPCell(innertable);
                    cell.setColspan(5);
                    //cell.setFixedHeight(130);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                }
            }

            cell = new PdfPCell(new Phrase(" 3. Significant work, if any, done", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(htmlToText(paf.getSpecialcontribution()), f1));
            cell.setColspan(4);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            if (paf.getReason() != null && !paf.getReason().equals("")) {
                //Reason Start
                cell = new PdfPCell(new Phrase(" 4. Reason", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
                cell.setColspan(5);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setColspan(5);
                cell.setFixedHeight(10);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.LEFT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(htmlToText(paf.getReason()), f1));
                cell.setColspan(4);
                cell.setBorder(Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                //Reason Stop
                cell = new PdfPCell();
                cell.setColspan(5);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                document.add(table);
                //document.newPage();

                //Third Page
                table = new PdfPTable(5);
                table.setWidths(new int[]{1, 4, 5, 3, 3});
                table.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setColspan(5);
                cell.setFixedHeight(50);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            } else {
                cell = new PdfPCell();
                cell.setColspan(5);
                cell.setFixedHeight(50);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            }

            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Place: " + StringUtils.defaultString(paf.getPlace()), f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" Date " + StringUtils.defaultString(paf.getSubmittedon()), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Signature of Appraisee", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setFixedHeight(30);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            document.add(table);
            document.newPage();

            table = new PdfPTable(4);
            table.setWidths(new int[]{4, 2, 4, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Remarks of Reporting Authority", getDesired_PDF_Font(9, true, true)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP);
            cell.setBorderWidth(1f);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            ReportingHelperBean rhb = null;
            for (int i = 0; i < paf.getReportingdata().size(); i++) {
                rhb = (ReportingHelperBean) paf.getReportingdata().get(i);
                if (i > 0) {
                    cell = new PdfPCell();
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                }
                if (rhb.getIscurrentreporting().equals("Y") || rhb.getIsreportingcompleted().equals("Y")) {

                    cell = new PdfPCell(new Phrase(rhb.getReportingauthName(), getDesired_PDF_Font(9, false, true)));
                    cell.setColspan(4);
                    cell.setFixedHeight(20);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("1. Assessment of work output, attributes and functional competencies.(This should be on a relative scale of 1-5, with 1 referring to the lowest level and 5 to the highest level. Please indicate your rating for the officer against each item.)", getDesired_PDF_Font(10, false, false)));
                    cell.setColspan(4);
                    cell.setFixedHeight(40);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Description", getDesired_PDF_Font(9, false, true)));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Rating", getDesired_PDF_Font(9, false, true)));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Description", getDesired_PDF_Font(9, false, true)));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Rating", getDesired_PDF_Font(9, false, true)));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("(a)  Attitude to work :", getDesired_PDF_Font(9, false, false)));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(rhb.getRatingattitude1(), getDesired_PDF_Font(9, false, false)));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("(f) Co-ordination ability :", getDesired_PDF_Font(9, false, false)));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(rhb.getRatingcoordination1(), getDesired_PDF_Font(9, false, false)));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("(b)  Sense of responsibility:", getDesired_PDF_Font(9, false, false)));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(rhb.getRatingresponsibility1(), getDesired_PDF_Font(9, false, false)));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("(g) Ability to work in a team:", getDesired_PDF_Font(9, false, false)));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(rhb.getTeamworkrating1(), getDesired_PDF_Font(9, false, false)));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("(c)  Communication skill :", getDesired_PDF_Font(9, false, false)));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(rhb.getRatingcomskill1(), getDesired_PDF_Font(9, false, false)));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("(h) Knowledge of Rules/Procedures/ IT  Skills/ Relevant Subject:", getDesired_PDF_Font(9, false, false)));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(rhb.getRatingitskill1(), getDesired_PDF_Font(9, false, false)));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("(d)  Leadership Qualities :", getDesired_PDF_Font(9, false, false)));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(rhb.getRatingleadership1(), getDesired_PDF_Font(9, false, false)));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("(i) Initiative :", getDesired_PDF_Font(9, false, false)));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(rhb.getRatinginitiative1(), getDesired_PDF_Font(9, false, false)));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("(e)  Decision-making ability :", getDesired_PDF_Font(9, false, false)));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(rhb.getRatingdecisionmaking1(), getDesired_PDF_Font(9, false, false)));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("(j) Quality of Work :", getDesired_PDF_Font(9, false, false)));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(rhb.getRatequalityofwork1(), getDesired_PDF_Font(9, false, false)));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("2. General Assessment (Please give an overall assessment of the officer including   his/her   attitude towards  S.T/S.C/Weaker Sections &  relation  with public:)", getDesired_PDF_Font(10, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getAuthNote()), getDesired_PDF_Font(9, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("3. Inadequacies, deficiencies or shortcomings, if any (Remarks to be treated as adverse )", getDesired_PDF_Font(10, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getInadequaciesNote()), getDesired_PDF_Font(9, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("4. Integrity (If integrity is doubtful or  adverse please write \"Not certified\" in the space below and justify your remarks in box 4 above)", getDesired_PDF_Font(10, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getIntegrityNote()), getDesired_PDF_Font(9, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("5. Overall Grading", getDesired_PDF_Font(10, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getSltGrading1()), getDesired_PDF_Font(9, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("6. For  Overall Grading  \"Below Average\" /  \"Outstanding\"  please provide justification in the   space below.:", getDesired_PDF_Font(10, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getGradingNote()), getDesired_PDF_Font(9, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(rhb.getReportingauthName()), getDesired_PDF_Font(9, true, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Submitted on- " + StringUtils.defaultString(rhb.getSubmittedon()), getDesired_PDF_Font(9, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
                    cell.setBorderWidth(1f);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                }
            }
            //if((authorityType.indexOf("REVIEWING") > -1) || (authorityType.indexOf("ACCEPTING") > -1)){
            cell = new PdfPCell(new Phrase("Remarks of Reviewing Authority", getDesired_PDF_Font(9, true, true)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            ReviewingHelperBean rvb = null;
            for (int i = 0; i < paf.getReviewingdata().size(); i++) {
                rvb = (ReviewingHelperBean) paf.getReviewingdata().get(i);

                //if(rvb.getIscurrentreviewing() != null && (rvb.getIscurrentreviewing().equals("Y") || rvb.getIsreviewingcompleted().equals("Y"))){
                cell = new PdfPCell(new Phrase(rvb.getReviewingauthName(), getDesired_PDF_Font(9, false, true)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                if (rvb.getIsreviewingcompleted() != null && rvb.getIsreviewingcompleted().equals("Y")) {

                    cell = new PdfPCell(new Phrase("1. Please Indicate if you agree with the general assessment/ adverse remarks/ overall grading  made by the   Reporting Authority, and give your assessment.", getDesired_PDF_Font(10, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rvb.getReviewingNote()), getDesired_PDF_Font(9, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("2. Overall Grading Given By Reviewing Authority  :" + getGradingName(rvb.getSltReviewGrading()), getDesired_PDF_Font(10, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(rvb.getReviewingauthName()), getDesired_PDF_Font(9, true, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Submitted on- " + rvb.getSubmittedon(), getDesired_PDF_Font(9, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                }
            }

            //}
            //if(authorityType.indexOf("ACCEPTING") > -1){
            cell = new PdfPCell(new Phrase("Remarks of Accepting Authority", getDesired_PDF_Font(9, true, true)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            AcceptingHelperBean ahb = null;
            for (int i = 0; i < paf.getAcceptingdata().size(); i++) {
                ahb = (AcceptingHelperBean) paf.getAcceptingdata().get(i);

                //if(ahb.getIscurrentaccepting() != null && (ahb.getIscurrentaccepting().equals("Y") || ahb.getIsacceptingcompleted().equals("Y"))){
                cell = new PdfPCell(new Phrase(ahb.getAcceptingauthName(), getDesired_PDF_Font(9, false, true)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(ahb.getAcceptingNote()), getDesired_PDF_Font(10, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Overall Grading Given By Accepting Authority  :" + ParGrade.getGradingName(ahb.getSltAcceptingGrading()), CommonFunctions.getDesired_PDF_Font(10, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(StringUtils.defaultString(ahb.getAcceptingauthName()), getDesired_PDF_Font(9, true, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Submitted on- " + StringUtils.defaultString(ahb.getSubmittedon()), getDesired_PDF_Font(9, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                //}
            }
            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            /*while Reporting, Reviewing, Accepting Authority dont submited  there reply */
            // ReportingHelperBean rhb = null;
            String reportingRemarks = "";
            String reviewingRemarks = "";
            String acceptingRemarks = "";
            for (int i = 0; i < paf.getReportingdata().size(); i++) {
                rhb = (ReportingHelperBean) paf.getReportingdata().get(i);
                if ((rhb.getIsreportingcompleted() == null || rhb.getIsreportingcompleted().equals("") || rhb.getIsreportingcompleted().equals("N"))) {
                    reportingRemarks = reportingRemarks + "N";
                } else {
                    reportingRemarks = reportingRemarks + "Y";
                }
            }
            for (int i = 0; i < paf.getReviewingdata().size(); i++) {
                rvb = (ReviewingHelperBean) paf.getReviewingdata().get(i);
                if (rvb.getIsreviewingcompleted() == null || rvb.getIsreviewingcompleted().equals("") || rvb.getIsreviewingcompleted().equals("N")) {
                    reviewingRemarks = reviewingRemarks + "N";
                } else {
                    reviewingRemarks = reviewingRemarks + "Y";
                }
            }
            for (int i = 0; i < paf.getAcceptingdata().size(); i++) {
                ahb = (AcceptingHelperBean) paf.getAcceptingdata().get(i);
                if (ahb.getIsacceptingcompleted() == null || ahb.getIsacceptingcompleted().equals("") || ahb.getIsacceptingcompleted().equals("N")) {
                    acceptingRemarks = acceptingRemarks + "N";
                } else {
                    acceptingRemarks = acceptingRemarks + "Y";
                }
            }
            String orderNo = "";
            if (paf.getEmpGroup().equals("A")) {
                orderNo = "1199";
            } else if (paf.getEmpGroup().equals("B")) {
                orderNo = "1200";
            }

            String reviewedParRemark = parReviewRemarks(paf);
            /*case no 5*/
            if (!reviewedParRemark.equals("")) {
                cell = new PdfPCell(new Phrase(reviewedParRemark, f1));

                // cell.setFixedHeight(80);
                cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setColspan(4);
                cell.setFixedHeight(10);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                phrs = new Phrase();
                c1 = new Chunk("( PAR is Reviewed by: ", f1);
                c2 = new Chunk(paf.getReviewedby() + ", " + paf.getReviewedbyspc(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
                c3 = new Chunk(" on Date ", f1);
                c4 = new Chunk(paf.getReviewedondate(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
                c5 = new Chunk(")", f1);
                phrs.add(c1);
                phrs.add(c2);
                phrs.add(c3);
                phrs.add(c4);
                phrs.add(c5);
                cell = new PdfPCell(phrs);
                cell.setColspan(4);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setColspan(4);
                cell.setFixedHeight(10);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            } else {

                phrs = new Phrase();
                c1 = new Chunk("(  Par is Reviewed by: ", f1);
                c2 = new Chunk(paf.getReviewedby() + ", " + paf.getReviewedbyspc(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
                c3 = new Chunk(" on Date ", f1);
                c4 = new Chunk(paf.getReviewedondate(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
                c5 = new Chunk(")", f1);
                phrs.add(c1);
                phrs.add(c2);
                phrs.add(c3);
                phrs.add(c4);
                phrs.add(c5);
                cell = new PdfPCell(phrs);
                cell.setColspan(4);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setColspan(4);
                cell.setFixedHeight(10);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //}
            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

    }

    public Font getDesired_PDF_Font(int fontsize, boolean isBold, boolean isUnderline) throws Exception {
        Font f = null;
        try {
            if (isBold == false && isUnderline == false) {
                f = new Font(Font.FontFamily.TIMES_ROMAN, fontsize, Font.NORMAL);
            }
            if (isBold == true && isUnderline == false) {
                f = new Font(Font.FontFamily.TIMES_ROMAN, fontsize, Font.BOLD);
            }
            if (isBold == true && isUnderline == true) {
                f = new Font(Font.FontFamily.TIMES_ROMAN, fontsize, Font.BOLD | Font.UNDERLINE);
            }
            if (isBold == false && isUnderline == true) {
                f = new Font(Font.FontFamily.TIMES_ROMAN, fontsize, Font.UNDERLINE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return f;
    }

    public String htmlToText(String html) {
        html = StringUtils.replace(html, "<br />", "\n");
        html = StringUtils.replace(html, "&#34;", "\"");
        return html;
    }

    @Override
    public String forceForward(ParApplyForm parApplyForm) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String pending_at = "";
        int status_id = 0;
        int hierarchy_no = 0;
        String msg = "Y";
        try {
            con = dataSource.getConnection();
            Calendar cal = Calendar.getInstance();
            DateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
            pstmt = con.prepareStatement("SELECT PENDING_AT,STATUS_ID FROM TASK_MASTER WHERE TASK_ID=?");
            pstmt.setInt(1, (parApplyForm.getTaskId()));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                pending_at = rs.getString("PENDING_AT");
                status_id = rs.getInt("STATUS_ID");
            }

            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            if (status_id == 6) {
                pstmt = con.prepareStatement("SELECT HIERARCHY_NO FROM PAR_REPORTING_TRAN WHERE PAR_ID=? AND REPORTING_EMP_ID=?");
                pstmt.setInt(1, parApplyForm.getParId());
                pstmt.setString(2, pending_at);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    hierarchy_no = rs.getInt("HIERARCHY_NO") + 1;
                }

                DataBaseFunctions.closeSqlObjects(rs, pstmt);

                if (hierarchy_no > 0) {
                    pstmt = con.prepareStatement("SELECT PRPTID,REPORTING_EMP_ID,REPORTING_CUR_SPC FROM PAR_REPORTING_TRAN WHERE PAR_ID=? AND HIERARCHY_NO=?");
                    pstmt.setInt(1, parApplyForm.getParId());
                    pstmt.setInt(2, hierarchy_no);
                    rs = pstmt.executeQuery();
                    if (rs.next()) {

                        updateAuthorityforForceForward(pending_at, parApplyForm.getParId(), status_id, dateformat.format(cal.getTime()));
                        updatePARforForceForward(rs.getString("REPORTING_EMP_ID"), rs.getString("REPORTING_CUR_SPC"), 6, parApplyForm.getTaskId(), parApplyForm.getParId(), rs.getInt("PRPTID"));
                    } else {
                        pstmt = con.prepareStatement("SELECT PRVTID,REVIEWING_EMP_ID,REVIEWING_CUR_SPC FROM PAR_REVIEWING_TRAN WHERE PAR_ID=? ORDER BY HIERARCHY_NO");
                        pstmt.setInt(1, parApplyForm.getParId());
                        rs = pstmt.executeQuery();
                        if (rs.next()) {
                            updateAuthorityforForceForward(pending_at, parApplyForm.getParId(), status_id, dateformat.format(cal.getTime()));
                            updatePARforForceForward(rs.getString("REVIEWING_EMP_ID"), rs.getString("REVIEWING_CUR_SPC"), 7, parApplyForm.getTaskId(), parApplyForm.getParId(), rs.getInt("PRVTID"));
                        }
                    }
                }

            } else if (status_id == 7) {
                pstmt = con.prepareStatement("SELECT HIERARCHY_NO FROM PAR_REVIEWING_TRAN WHERE PAR_ID=? AND REVIEWING_EMP_ID=?");
                pstmt.setInt(1, parApplyForm.getParId());
                pstmt.setString(2, pending_at);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    hierarchy_no = rs.getInt("HIERARCHY_NO") + 1;
                    pstmt = con.prepareStatement("SELECT PRVTID,REVIEWING_EMP_ID,REVIEWING_CUR_SPC FROM PAR_REVIEWING_TRAN WHERE PAR_ID=? AND HIERARCHY_NO=?");
                    pstmt.setInt(1, parApplyForm.getParId());
                    pstmt.setInt(2, hierarchy_no);
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        updateAuthorityforForceForward(pending_at, parApplyForm.getParId(), status_id, dateformat.format(cal.getTime()));
                        updatePARforForceForward(rs.getString("REVIEWING_EMP_ID"), rs.getString("REVIEWING_CUR_SPC"), 7, parApplyForm.getTaskId(), parApplyForm.getParId(), rs.getInt("PRVTID"));
                    } else {
                        pstmt = con.prepareStatement("SELECT PACTID,ACCEPTING_EMP_ID,ACCEPTING_CUR_SPC FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=? ORDER BY HIERARCHY_NO");
                        pstmt.setInt(1, parApplyForm.getParId());
                        rs = pstmt.executeQuery();
                        if (rs.next()) {
                            updateAuthorityforForceForward(pending_at, parApplyForm.getParId(), status_id, dateformat.format(cal.getTime()));
                            updatePARforForceForward(rs.getString("ACCEPTING_EMP_ID"), rs.getString("ACCEPTING_CUR_SPC"), 8, parApplyForm.getTaskId(), parApplyForm.getParId(), rs.getInt("PACTID"));
                        }
                    }
                }

            } else if (status_id == 8) {
                pstmt = con.prepareStatement("SELECT HIERARCHY_NO FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=? AND ACCEPTING_EMP_ID=?");
                pstmt.setInt(1, parApplyForm.getParId());
                pstmt.setString(2, pending_at);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    hierarchy_no = rs.getInt("HIERARCHY_NO") + 1;
                    pstmt = con.prepareStatement("SELECT PACTID,ACCEPTING_EMP_ID,ACCEPTING_CUR_SPC FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=? AND HIERARCHY_NO=?");
                    pstmt.setInt(1, parApplyForm.getParId());
                    pstmt.setInt(2, hierarchy_no);
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        updateAuthorityforForceForward(pending_at, parApplyForm.getParId(), status_id, dateformat.format(cal.getTime()));
                        updatePARforForceForward(rs.getString("ACCEPTING_EMP_ID"), rs.getString("ACCEPTING_CUR_SPC"), 8, parApplyForm.getTaskId(), parApplyForm.getParId(), rs.getInt("PACTID"));
                    } else {
                        updateAuthorityforForceForward(pending_at, parApplyForm.getParId(), status_id, dateformat.format(cal.getTime()));
                        updatePARforForceForward(null, null, 9, parApplyForm.getTaskId(), parApplyForm.getParId(), 0);
                    }
                }
            }
        } catch (Exception e) {
            msg = "N";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    public ParApplyForm AdverseCommunication(int parId, int taskId) {
        ParApplyForm parApplyForm = new ParApplyForm();
        DataSource ds = null;
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement pstmt = null;

        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();
            pstmt = con.prepareStatement("SELECT EMP_MAST.EMP_ID,EMP_NAME,CUR_SPC,PAR_STATUS FROM (SELECT CUR_SPC,EMP_ID,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME FROM EMP_MAST)EMP_MAST"
                    + " INNER JOIN (SELECT EMP_ID,PAR_STATUS FROM PAR_MASTER WHERE PARID=?)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID");
            pstmt.setInt(1, parId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                parApplyForm.setApplicant(rs.getString("EMP_NAME"));
                parApplyForm.setApplicantempid(rs.getString("EMP_ID"));
                parApplyForm.setApprisespc(rs.getString("CUR_SPC"));
                parApplyForm.setParstatus(rs.getInt("PAR_STATUS"));
            }
            parApplyForm.setReportingauth(getParAuthority("REPORTING", parApplyForm.getParId(), parApplyForm.getTaskId()));
            parApplyForm.setReviewingauth(getParAuthority("REVIEWING", parApplyForm.getParId(), parApplyForm.getTaskId()));
            parApplyForm.setAcceptingauth(getParAuthority("ACCEPTING", parApplyForm.getParId(), parApplyForm.getTaskId()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parApplyForm;
    }

    private String getLAMemberDeptName(Connection con, int lmid) {

        PreparedStatement pst = null;
        ResultSet rs = null;

        String deptNames = "";
        try {
            String sql = "SELECT DEPARTMENT_NAME FROM G_DEPARTMENT WHERE LMID=? ORDER BY DEPARTMENT_NAME ASC";
            pst = con.prepareStatement(sql);
            pst.setInt(1, lmid);
            rs = pst.executeQuery();
            while (rs.next()) {
                if (deptNames.equals("")) {
                    deptNames = rs.getString("DEPARTMENT_NAME");
                } else {
                    deptNames = deptNames + "," + rs.getString("DEPARTMENT_NAME");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            //DataBaseFunctions.closeSqlObjects(con);
        }
        return deptNames;
    }

    @Override
    public List getAdverseParList() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List adverselist = new ArrayList();
        ParAdminProperties parAdminProperties = null;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT PARID,PAR_MASTER.EMP_ID,F_NAME,M_NAME,L_NAME,POST,PAR_STATUS,is_adversed FROM  "
                    + "(SELECT EMP_ID,F_NAME,M_NAME,L_NAME FROM EMP_MAST)EMP_MAST "
                    + "INNER JOIN (SELECT SPC,PARID,EMP_ID,PAR_STATUS,is_adversed FROM PAR_MASTER where is_adversed = 'Y' )PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                    + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC   "
                    + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                parAdminProperties = new ParAdminProperties();
                parAdminProperties.setEmpId(rs.getString("EMP_ID"));
                String fname = rs.getString("F_NAME").trim();
                String mname = "";
                String lname = "";

                if (rs.getString("M_NAME") != null) {
                    mname = rs.getString("M_NAME").trim();
                } else {
                    mname = "";
                }
                if (rs.getString("L_NAME") != null && !rs.getString("L_NAME").equals("")) {
                    lname = rs.getString("L_NAME").trim();
                }
                parAdminProperties.setEmpName(fname + " " + mname + " " + lname);
                parAdminProperties.setPostName(StringUtils.defaultString(rs.getString("POST"), ""));
                parAdminProperties.setParId(rs.getInt("PARID"));
                if (rs.getString("PAR_STATUS") != null && !rs.getString("PAR_STATUS").equals("")) {
                    if (rs.getInt("PAR_STATUS") == 0) {
                        parAdminProperties.setParstatus("PAR CREATED BUT NOT SUBMITTED");
                    } else {
                        parAdminProperties.setParstatus(getStatusName(con, rs.getString("PAR_STATUS")));
                    }
                } else {
                    parAdminProperties.setParstatus("PAR NOT CREATED");
                }
                adverselist.add(parAdminProperties);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return adverselist;

    }

    public void forceForwardFromReportingToReviewing(int parid) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE PAR_REPORTING_TRAN SET IS_COMPLETED=?,SUBMITTED_ON=? WHERE PAR_ID=? AND IS_COMPLETED is null");
            pst.setString(1, "F");
            pst.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            pst.setInt(3, parid);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, con);
        }
    }

    public void forceForwardFromReviewingToAccepting(int parid) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE par_reviewing_tran SET IS_COMPLETED=?,SUBMITTED_ON=? WHERE PAR_ID=? AND IS_COMPLETED is null");
            pst.setString(1, "F");
            pst.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            pst.setInt(3, parid);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, con);
        }
    }

    public void updateAuthorityforForceForward(String pending_at, int parid, int statusid, String submitted_date) throws Exception {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            if (statusid == 6) {
                pst = con.prepareStatement("UPDATE PAR_REPORTING_TRAN SET IS_COMPLETED=?,SUBMITTED_ON=? WHERE PAR_ID=? AND REPORTING_EMP_ID=?");
                pst.setString(1, "F");
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(submitted_date).getTime()));
                pst.setInt(3, parid);
                pst.setString(4, pending_at);
                pst.executeUpdate();
                DataBaseFunctions.closeSqlObjects(pst);
            } else if (statusid == 7) {
                pst = con.prepareStatement("UPDATE PAR_REVIEWING_TRAN SET IS_COMPLETED=?,SUBMITTED_ON=? WHERE PAR_ID=? AND REVIEWING_EMP_ID=?");
                pst.setString(1, "F");
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(submitted_date).getTime()));
                pst.setInt(3, parid);
                pst.setString(4, pending_at);
                pst.executeUpdate();
                DataBaseFunctions.closeSqlObjects(pst);
            } else if (statusid == 8) {
                pst = con.prepareStatement("UPDATE PAR_ACCEPTING_TRAN SET IS_COMPLETED=?,SUBMITTED_ON=? WHERE PAR_ID=? AND ACCEPTING_EMP_ID=?");
                pst.setString(1, "F");
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(submitted_date).getTime()));
                pst.setInt(3, parid);
                pst.setString(4, pending_at);
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, con);
        }
    }

    public void updatePARforForceForward(String pending_at, String pending_spc, int statusid, int taskid, int parid, int refid) throws Exception {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=?,APPLY_TO=?,PENDING_SPC=?,STATUS_ID=? WHERE TASK_ID=?");
            pst.setString(1, pending_at);
            pst.setString(2, pending_at);
            pst.setString(3, pending_spc);
            pst.setInt(4, statusid);
            pst.setInt(5, taskid);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE PAR_MASTER SET PAR_STATUS=?,REF_ID_OF_TABLE=?,is_force_forward=? WHERE PARID=?");
            pst.setInt(1, statusid);
            pst.setInt(2, refid);
            pst.setString(3, "Y");
            pst.setInt(4, parid);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, con);
        }
    }

    @Override
    public ParApplyForm getNRCDetails(int parId) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ParApplyForm parApplyForm = new ParApplyForm();

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select parid,par_master.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,DOB,fiscal_year,period_from,period_to,par_status,POST,G_CADRE.cadre_name,off_en,post_group,headquarter,reason_id,reason,remarks,is_reviewed,reviewed_ondate,getfullname(REVIEWED_BY_EMPID) as REVIEWED_BY_NAME,GETSPN(REVIEWED_BY_SPC) as REVIEWED_BY_SPN,nrc_submitted_on from par_master "
                    + " LEFT OUTER JOIN G_CADRE ON par_master.CADRE_CODE=G_CADRE.cadre_code "
                    + " INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = par_master.EMP_ID  "
                    + " LEFT OUTER JOIN g_office ON par_master.off_code = g_office.off_code   "
                    + " LEFT OUTER JOIN G_SPC ON G_SPC.SPC = par_master.SPC "
                    + " LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "LEFT OUTER JOIN g_par_nrc_reason ON par_master.nrcreason = g_par_nrc_reason.reason_id  where parId =?");

            pstmt.setInt(1, parId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                parApplyForm.setParId(rs.getInt("parid"));
                parApplyForm.setEmpId(rs.getString("emp_id"));
                parApplyForm.setEmpName(rs.getString("EMP_NAME"));
                parApplyForm.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOB")));
                parApplyForm.setFiscalYear(rs.getString("fiscal_year"));
                parApplyForm.setPrdFrmDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("period_from")));
                parApplyForm.setPrdToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("period_to")));
                parApplyForm.setParstatus(rs.getInt("par_status"));
                parApplyForm.setEmpDesg(rs.getString("POST"));
                parApplyForm.setCadrecode(rs.getString("cadre_name"));
                parApplyForm.setEmpOffice(rs.getString("off_en"));
                parApplyForm.setEmpGroup(rs.getString("post_group"));
                parApplyForm.setSltHeadQuarter(rs.getString("headquarter"));
                parApplyForm.setReasonId(rs.getString("reason_id"));
                parApplyForm.setNrcreason(rs.getString("reason"));
                parApplyForm.setNrcremarks(rs.getString("remarks"));
                parApplyForm.setIsreviewed(rs.getString("is_reviewed"));
                parApplyForm.setReviewedondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("reviewed_ondate")));
                parApplyForm.setReviewedby(rs.getString("REVIEWED_BY_NAME"));
                parApplyForm.setReviewedbyspc(rs.getString("REVIEWED_BY_SPN"));

                parApplyForm.setSubmitedonNRC(CommonFunctions.getFormattedOutputDate1(rs.getDate("nrc_submitted_on")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return parApplyForm;
    }

    @Override
    public void DownloadNRCPDF(ParApplyForm paf, Document document) {
        Connection con = null;
        try {
            document.open();
            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");

            PdfPTable table = null;
            PdfPCell cell = null;

            table = new PdfPTable(2);
            table.setWidths(new int[]{2, 3});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("REQUESTED FOR NRC", getDesired_PDF_Font(13, true, true)));
            cell.setColspan(2);
            cell.setFixedHeight(30);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Financial Year: " + StringUtils.defaultString(paf.getFiscalYear()), new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
            cell.setColspan(2);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("From Date: " + StringUtils.defaultString(paf.getPrdFrmDate()), f1));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("To Date: " + StringUtils.defaultString(paf.getPrdToDate()), f1));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("HRMS ID", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + paf.getEmpId(), f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Full name of the officer", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + paf.getEmpName(), f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Date of birth", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + paf.getDob(), f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Service to which the officer belongs", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(paf.getCadrecode()), f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Group to which the officer belongs", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(paf.getEmpGroup()), f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Designation during the period of report", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(paf.getEmpDesg()), f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Office to which posted", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(paf.getEmpOffice()), f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Head Quarter(if any)", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(paf.getSltHeadQuarter()), f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Reason for NRC", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(paf.getNrcreason()), f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(1);
            // table.setWidths(new int[]{2, 3});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Remarks", getDesired_PDF_Font(9, true, false)));
            cell.setColspan(5);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(paf.getNrcremarks()), f1));
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setFixedHeight(100);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Date Of Submission: " + StringUtils.defaultString(paf.getSubmitedonNRC()), new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
            cell.setColspan(2);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(1);
            // table.setWidths(new int[]{2, 3});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("No Remarks Certificate", getDesired_PDF_Font(13, true, true)));
            cell.setColspan(2);
            cell.setFixedHeight(30);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            if (paf.getReasonId().equals("01")) {
                cell = new PdfPCell(new Phrase("\"No Remarks\"  is Accepted since the period is less than 4 months as per para-11(b) of PAR guidelines\n"
                        + "issued vide erstwhile GA(SE) department Memo No. 1200/PRO DTD.26.04.2006 ", f1));
                // cell.setFixedHeight(80);
                cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

            } else {
                cell = new PdfPCell(new Phrase("\"No Remarks\" is Recorded for the period mentioned above since the period is _________\n"
                        + "as per the Given Para of PAR Guideline issued vide erstwhile  G.A (SE) Department Circular memo No.________ dated _________", f1));
                // cell.setFixedHeight(80);
                cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            if ((paf.getReviewedby() != null && !paf.getReviewedby().equals("")) && (paf.getReviewedondate() != null && !paf.getReviewedondate().equals(""))) {

                Phrase phrs = new Phrase();
                Chunk c1 = new Chunk("(  NRC is Accepted by: ", f1);
                Chunk c2 = new Chunk(paf.getReviewedby() + ", " + paf.getReviewedbyspc(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
                Chunk c3 = new Chunk(" on Date ", f1);
                Chunk c4 = new Chunk(paf.getReviewedondate(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
                Chunk c5 = new Chunk(")", f1);
                phrs.add(c1);
                phrs.add(c2);
                phrs.add(c3);
                phrs.add(c4);
                phrs.add(c5);
                cell = new PdfPCell(phrs);
                cell.setColspan(4);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            document.close();
        }

    }

    @Override
    public ParNrcAttachment DownloadNRCAttchment(String filepath, int parId) {
        ParNrcAttachment parNrcAttachment = new ParNrcAttachment();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT disk_file_name,org_file_name,file_type,fiscal_year FROM PAR_NRC_DOCUMENT "
                    + "inner join PAR_MASTER ON PAR_NRC_DOCUMENT.par_id = PAR_MASTER.parid WHERE PAR_NRC_DOCUMENT.par_id=? ");
            pst.setInt(1, parId);
            rs = pst.executeQuery();
            if (rs.next()) {
                parNrcAttachment.setDiskfilename(rs.getString("disk_file_name"));
                parNrcAttachment.setOriginalfilename(rs.getString("org_file_name"));
                parNrcAttachment.setFiletype(rs.getString("file_type"));
                parNrcAttachment.setFiscalYear(rs.getString("fiscal_year"));
            }
            File f = new File(filepath + parNrcAttachment.getFiscalYear() + CommonFunctions.getResourcePath() + parNrcAttachment.getDiskfilename());
            parNrcAttachment.setFilecontent(FileUtils.readFileToByteArray(f));

        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parNrcAttachment;
    }

    @Override
    public void NRCAccepted(ParApplyForm paf, Document document) {
        Connection con = null;
        try {
            document.open();
            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");

            PdfPTable table = null;
            PdfPCell cell = null;

            table = new PdfPTable(2);
            table.setWidths(new int[]{2, 3});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("REQUESTED FOR NRC", getDesired_PDF_Font(13, true, true)));
            cell.setColspan(2);
            cell.setFixedHeight(30);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Financial Year: " + StringUtils.defaultString(paf.getFiscalYear()), new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
            cell.setColspan(2);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("From Date: " + StringUtils.defaultString(paf.getPrdFrmDate()), f1));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("To Date: " + StringUtils.defaultString(paf.getPrdToDate()), f1));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("HRMS ID", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + paf.getEmpId(), f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Full name of the officer", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + paf.getEmpName(), f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Date of birth", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + paf.getDob(), f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Service to which the officer belongs", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(paf.getCadrecode()), f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Group to which the officer belongs", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(paf.getEmpGroup()), f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Designation during the period of report", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(paf.getEmpDesg()), f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Office to which posted", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(paf.getEmpOffice()), f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Head Quarter(if any)", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(paf.getSltHeadQuarter()), f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Reason for NRC", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(paf.getNrcreason()), f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(1);
            // table.setWidths(new int[]{2, 3});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Remarks", getDesired_PDF_Font(9, true, false)));
            cell.setColspan(5);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(paf.getNrcremarks()), f1));
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setFixedHeight(100);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Date Of Submission: " + StringUtils.defaultString(paf.getSubmitedonNRC()), new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
            cell.setColspan(2);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("No Remarks Certificate", getDesired_PDF_Font(13, true, true)));
            cell.setColspan(2);
            cell.setFixedHeight(30);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            String orderNo = "";
            if (paf.getEmpGroup() == null || paf.getEmpGroup().equals("")) {
                orderNo = " ";
            } else if ((paf.getEmpGroup() != null || !paf.getEmpGroup().equals("")) && paf.getEmpGroup().equals("A")) {
                orderNo = "1199";
            } else if ((paf.getEmpGroup() != null || !paf.getEmpGroup().equals("")) && paf.getEmpGroup().equals("B")) {
                orderNo = "1200";
            }
            if (paf.getReasonId().equals("01")) {
                cell = new PdfPCell(new Phrase("\"No Remarks\"  is Accepted since the period is less than 4 months as per para-11(b) of PAR guidelines\n"
                        + "issued vide erstwhile GA(SE) department Memo No. " + orderNo + "/PRO DTD.26.04.2006 ", f1));
                // cell.setFixedHeight(80);
                cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

            } else {
                cell = new PdfPCell(new Phrase("\"No Remarks\" is Recorded for the period mentioned above since the period is _________\n"
                        + "as per the Given Para of PAR Guideline issued vide erstwhile  G.A (SE) Department Circular memo No.________ dated _________", f1));
                // cell.setFixedHeight(80);
                cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            Phrase phrs = new Phrase();
            Chunk c1 = new Chunk("(  NRC is Accepted by: ", f1);
            Chunk c2 = new Chunk(paf.getReviewedby() + ", " + paf.getReviewedbyspc(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
            Chunk c3 = new Chunk(" on Date ", f1);
            Chunk c4 = new Chunk(paf.getReviewedondate(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
            Chunk c5 = new Chunk(")", f1);
            phrs.add(c1);
            phrs.add(c2);
            phrs.add(c3);
            phrs.add(c4);
            phrs.add(c5);
            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            document.close();
        }

    }

    public PerformanceAppraisalForm getNRCData(String empId, String parid) throws Exception {

        Statement stmt = null;
        ResultSet rs = null;
        PerformanceAppraisalForm paf = new PerformanceAppraisalForm();
        String fiscalyr = "";
        Connection con = null;

        try {
            stmt = con.createStatement();
            String sql = "select par_master.*,DISK_FILE_NAME,ORG_FILE_NAME,FILE_TYPE from (select * from par_master where parid='" + parid + "')par_master"
                    + " left outer join PAR_NRC_DOCUMENT on par_master.PARID=PAR_NRC_DOCUMENT.PAR_ID";
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                paf.setHidParId(rs.getString("PARID"));
                paf.setTxtFromDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_FROM")));
                paf.setTxtToDate(CommonFunctions.getFormattedOutputDate2(rs.getDate("PERIOD_TO")));
                paf.setNrcAttachmentname(rs.getString("ORG_FILE_NAME"));
                paf.setFiscalYear(rs.getString("FISCAL_YEAR"));
                paf.setHidSpcCode(rs.getString("SPC"));
                paf.setSltDesignation(getEmpPost(paf.getHidSpcCode()));
                paf.setSltPostedOffice(getEmpOffice(paf.getHidSpcCode()));
                //paf.setSltGroupOfPost(rs.getString("POST_GROUP"));
                paf.setNrcreason(rs.getString("NRCREASON"));
                paf.setSltHeadQuarter(rs.getString("HEADQUARTER"));
                paf.setTxtNrcRemarks(rs.getString("REMARKS"));
                paf.setSubmitedonNRC(CommonFunctions.getFormattedOutputDate1(rs.getDate("submitted_on")));
            }

            DataBaseFunctions.closeSqlObjects(rs, stmt);

            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT EMP_MAST.*,CADRE_NAME FROM (SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,"
                    + " CUR_CADRE_CODE,POST_GRP_TYPE,CUR_CADRE_NAME,CUR_SPC,DOB,CUR_OFF_CODE FROM EMP_MAST"
                    + " WHERE EMP_ID='" + empId + "')EMP_MAST LEFT OUTER JOIN G_CADRE ON EMP_MAST.CUR_CADRE_CODE=G_CADRE.CADRE_CODE");
            if (rs.next()) {
                paf.setTxtEmpId(empId);
                paf.setTxtEmpName(rs.getString("EMPNAME"));
                paf.setTxtDOB(CommonFunctions.getFormattedOutputDate3(rs.getDate("DOB")));
                paf.setSltGroupOfPost(rs.getString("POST_GRP_TYPE"));
                paf.setTxtServiceDetails(rs.getString("CADRE_NAME"));
                //paf.setHidSpcCode(rs.getString("CUR_SPC"));
                paf.setHidOffCode(rs.getString("CUR_OFF_CODE"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return paf;
    }

    public String getEmpPost(String empSpc) throws Exception {
        ResultSet rs = null;
        //SelectOption so = null;
        Statement st = null;
        String authorityPost = null;
        Connection con = null;
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT POST FROM (SELECT GPC FROM G_SPC WHERE SPC='" + empSpc + "')GSPC INNER JOIN G_POST ON "
                    + "GSPC.GPC=G_POST.POST_CODE");
            if (rs.next()) {
                authorityPost = rs.getString("POST");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return authorityPost;
    }

    public String getEmpOffice(String empSpc) throws Exception {
        ResultSet rs = null;
        //SelectOption so = null;
        Statement st = null;
        String authorityOff = null;
        Connection con = null;
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT OFF_EN FROM(SELECT CUR_OFF_CODE FROM EMP_MAST WHERE CUR_SPC='" + empSpc + "') "
                    + "EMPMAST INNER JOIN G_OFFICE ON G_OFFICE.OFF_CODE=EMPMAST.CUR_OFF_CODE");
            if (rs.next()) {
                authorityOff = rs.getString("OFF_EN");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return authorityOff;
    }

    public String getNRCReasonforDisplay(String id) throws Exception {
        Statement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        String nrcstring = "";
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();
            String sql = "SELECT REASON FROM G_PAR_NRC_REASON WHERE REASON_ID='" + id + "'";
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                nrcstring = rs.getString("REASON");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt, con);
        }
        return nrcstring;
    }

    @Override
    public String saveAssignCadre(ParApplyForm parApplyForm) {
        String msg = "Y";
        int priv_id = 0;
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            int recordFound = 0;
            con = dataSource.getConnection();

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            Long curtime = new Date().getTime();

            String IP = CommonFunctions.getISPIPAddress();

            pst = con.prepareStatement("SELECT COUNT(*) AS CNT FROM PAR_AUTHORITY_ADMIN WHERE SPC=? AND CADRE_CODE = ? AND POST_GRP = ?");
            pst.setString(1, parApplyForm.getSpc());
            pst.setString(2, parApplyForm.getCadrecode());
            pst.setString(3, parApplyForm.getPostGroup());
            rs = pst.executeQuery();
            if (rs.next()) {
                recordFound = rs.getInt("CNT");
            }
            DataBaseFunctions.closeSqlObjects(pst);
            if (recordFound == 0) {
                pst = con.prepareStatement("INSERT INTO PAR_AUTHORITY_ADMIN(SPC,CADRE_CODE,POST_GRP) VALUES(?,?,?)");
                pst.setString(1, parApplyForm.getSpc());
                pst.setString(2, parApplyForm.getCadrecode());
                pst.setString(3, parApplyForm.getPostGroup());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("INSERT INTO par_custodian_cadre_previgiled_log (loginby_id,previliged_cadre_code,previliged_postgrp,cadre_previligedby_ip,cadre_previligedon,cadre_previliged_to_spc) VALUES(?,?,?,?,?,?)");
                pst.setString(1, parApplyForm.getLoginById());
                pst.setString(2, parApplyForm.getCadrecode());
                pst.setString(3, parApplyForm.getPostGroup());
                pst.setString(4, IP);
                pst.setTimestamp(5, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setString(6, parApplyForm.getSpc());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                priv_id = CommonFunctions.getMaxCodeInteger("G_PRIVILEGE_MAP", "PRIV_MAP_ID", con);
                pst = con.prepareStatement("INSERT INTO G_PRIVILEGE_MAP(PRIV_MAP_ID,SPC,ROLE_ID,MOD_GRP_ID,MOD_ID) VALUES(?,?,?,?,?)");
                pst.setInt(1, priv_id);
                pst.setString(2, parApplyForm.getSpc());
                pst.setInt(3, 10);
                pst.setString(4, "038");
                pst.setInt(5, 214);
                pst.executeUpdate();
            } else {
                msg = "D";
            }
        } catch (Exception e) {
            msg = "N";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst, con);
        }
        return msg;
    }

    @Override
    public String saveAssignOfficewisePrivilige(ParApplyForm parApplyForm) {
        String msg = "Y";
        int priv_id = 0;
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            int recordFound = 0;
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT COUNT(*) AS CNT FROM PAR_AUTHORITY_ADMIN WHERE SPC=? AND off_code=? AND authorization_type=? AND POST_GRP = ? and par_for is null");
            pst.setString(1, parApplyForm.getSpc());
            pst.setString(2, parApplyForm.getOffcodeforofficewise());
            pst.setString(3, parApplyForm.getAuthorizationType());
            pst.setString(4, parApplyForm.getPostGroup());
            rs = pst.executeQuery();
            if (rs.next()) {
                recordFound = rs.getInt("CNT");
            }
            DataBaseFunctions.closeSqlObjects(pst);

            if (recordFound == 0) {
                pst = con.prepareStatement("INSERT INTO PAR_AUTHORITY_ADMIN(SPC,off_code,authorization_type,post_grp,par_for) VALUES(?,?,?,?,?)");
                pst.setString(1, parApplyForm.getSpc());
                pst.setString(2, parApplyForm.getOffcodeforofficewise());
                pst.setString(3, parApplyForm.getAuthorizationType());
                pst.setString(4, parApplyForm.getPostGroup());
                pst.setString(5, null);
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                priv_id = CommonFunctions.getMaxCodeInteger("G_PRIVILEGE_MAP", "PRIV_MAP_ID", con);
                pst = con.prepareStatement("INSERT INTO G_PRIVILEGE_MAP(PRIV_MAP_ID,SPC,ROLE_ID,MOD_GRP_ID,MOD_ID) VALUES(?,?,?,?,?)");
                pst.setInt(1, priv_id);
                pst.setString(2, parApplyForm.getSpc());
                pst.setInt(3, 16);
                pst.setString(4, "038");
                pst.setInt(5, 214);
                pst.executeUpdate();
            } else {
                msg = "D";
            }
        } catch (Exception e) {
            msg = "N";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst, con);
        }
        return msg;
    }

    @Override
    public ArrayList getAssignPrivilegedList(String cadrecode) {
        Connection con = null;
        ArrayList privilegedList = new ArrayList();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,PAR_AUTHORITY_ADMIN.SPC,SPN,PAR_AUTHORITY_ADMIN.POST_GRP FROM PAR_AUTHORITY_ADMIN "
                    + "INNER JOIN G_SPC ON PAR_AUTHORITY_ADMIN.SPC = G_SPC.SPC "
                    + "LEFT OUTER JOIN EMP_MAST ON PAR_AUTHORITY_ADMIN.SPC = EMP_MAST.CUR_SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "WHERE PAR_AUTHORITY_ADMIN.CADRE_CODE = ?");
            pstmt.setString(1, cadrecode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ParApplyForm parApplyForm = new ParApplyForm();
                parApplyForm.setEmpName(rs.getString("EMP_NAME"));
                parApplyForm.setSpc(rs.getString("SPC"));
                parApplyForm.setEmpDesg(rs.getString("SPN"));
                parApplyForm.setPostGroup(rs.getString("post_grp"));
                privilegedList.add(parApplyForm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return privilegedList;
    }

    @Override
    public void deleteCadrePrivilage(ParApplyForm parApplyForm) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            Long curtime = new Date().getTime();

            String IP = CommonFunctions.getISPIPAddress();
            
            pst = con.prepareStatement("DELETE FROM  PAR_AUTHORITY_ADMIN WHERE CADRE_CODE=? AND SPC=? AND POST_GRP=?");
            pst.setString(1, parApplyForm.getCadrecode());
            pst.setString(2, parApplyForm.getSpc());
            pst.setString(3, parApplyForm.getPostGroup());
            pst.executeUpdate();

            pst = con.prepareStatement("UPDATE par_custodian_cadre_previgiled_log SET cadre_removedby_ip=?,cadre_removedby_id=?,cadre_removedon=? WHERE previliged_cadre_code=? AND cadre_previliged_to_spc=? AND previliged_postgrp=?");
            pst.setString(1, IP);
            pst.setString(2, parApplyForm.getLoginById());
            pst.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(4, parApplyForm.getCadrecode());
            pst.setString(5, parApplyForm.getSpc());
            pst.setString(6, parApplyForm.getPostGroup());
            
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getOfficewiseAssignPrivilegedList(String offCode) {
        Connection con = null;
        ArrayList officewiseprivilegedList = new ArrayList();
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,PAR_AUTHORITY_ADMIN.SPC,SPN,PAR_AUTHORITY_ADMIN.POST_GRP FROM PAR_AUTHORITY_ADMIN "
                    + "INNER JOIN G_SPC ON PAR_AUTHORITY_ADMIN.SPC = G_SPC.SPC "
                    + "LEFT OUTER JOIN EMP_MAST ON PAR_AUTHORITY_ADMIN.SPC = EMP_MAST.CUR_SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "WHERE PAR_AUTHORITY_ADMIN.OFF_CODE = ? and par_for is null");
            pstmt.setString(1, offCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ParApplyForm parApplyForm = new ParApplyForm();
                parApplyForm.setEmpName(rs.getString("EMP_NAME"));
                parApplyForm.setSpc(rs.getString("SPC"));
                parApplyForm.setEmpDesg(rs.getString("SPN"));
                parApplyForm.setPostGroup(rs.getString("post_grp"));
                officewiseprivilegedList.add(parApplyForm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officewiseprivilegedList;
    }

    @Override
    public void deleteOfficewisePrivilage(String offCode, String spc, String postGroup) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM PAR_AUTHORITY_ADMIN WHERE OFF_CODE=? AND SPC=? AND POST_GRP=?");
            pst.setString(1, offCode);
            pst.setString(2, spc);
            pst.setString(3, postGroup);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getCadreList(String spc) {
        Connection con = null;
        ArrayList cadreList = new ArrayList();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select par_authority_admin.CADRE_CODE,g_cadre.cadre_name,post_grp from par_authority_admin  "
                    + "inner join g_cadre ON  par_authority_admin.CADRE_CODE = g_cadre.cadre_code where spc= ?");
            pstmt.setString(1, spc);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                DepartmentPromotionBean departmentPromotionBean = new DepartmentPromotionBean();
                departmentPromotionBean.setCadrecode(rs.getString("CADRE_CODE"));
                departmentPromotionBean.setCadrename(rs.getString("cadre_name"));
                departmentPromotionBean.setPostGroupType(rs.getString("post_grp"));
                cadreList.add(departmentPromotionBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return cadreList;
    }

    @Override
    public void saveDPCData(DepartmentPromotionBean departmentPromotionBean) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            Long curtime = new Date().getTime();
            if (departmentPromotionBean.getDpcId() == 0) {
                pst = con.prepareStatement("INSERT INTO dpc_master(fiscalyear_from,fiscalyear_to,cadre_code,created_by,created_on,dpc_name) VALUES(?,?,?,?,?,?)");
                pst.setString(1, departmentPromotionBean.getFiscalYearFrom());
                pst.setString(2, departmentPromotionBean.getFiscalYearTo());
                pst.setString(3, departmentPromotionBean.getCadrecode());
                pst.setString(4, departmentPromotionBean.getCreatedByEmpId());
                pst.setTimestamp(5, new Timestamp(curtime));
                pst.setString(6, departmentPromotionBean.getDpcName());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);
            } else {
                pst = con.prepareStatement("UPDATE dpc_master SET fiscalyear_from=?,fiscalyear_to=?,cadre_code=?,created_by=?,created_on=?,dpc_name=? where dpc_id = ?");
                pst.setString(1, departmentPromotionBean.getFiscalYearFrom());
                pst.setString(2, departmentPromotionBean.getFiscalYearTo());
                pst.setString(3, departmentPromotionBean.getCadrecode());
                pst.setString(4, departmentPromotionBean.getCreatedByEmpId());
                pst.setTimestamp(5, new Timestamp(curtime));
                pst.setString(6, departmentPromotionBean.getDpcName());
                pst.setInt(7, departmentPromotionBean.getDpcId());
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
    public ArrayList getDPCData(String createdByEmpId) {
        Connection con = null;
        ArrayList dpcDataList = new ArrayList();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("select dpc_id,fiscalyear_from,fiscalyear_to,dpc_master.cadre_code,g_cadre.cadre_name,created_on,dpc_name from dpc_master "
                    + "inner join g_cadre ON  dpc_master.CADRE_CODE = g_cadre.cadre_code where created_by = ?");
            pstmt.setString(1, createdByEmpId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                DepartmentPromotionBean departmentPromotionBean = new DepartmentPromotionBean();
                departmentPromotionBean.setDpcId(rs.getInt("dpc_id"));
                departmentPromotionBean.setFiscalYearFrom(rs.getString("fiscalyear_from"));
                departmentPromotionBean.setFiscalYearTo(rs.getString("fiscalyear_to"));
                departmentPromotionBean.setCadrecode(rs.getString("cadre_code"));
                departmentPromotionBean.setCadrename(rs.getString("cadre_name"));
                departmentPromotionBean.setCreatedOn(CommonFunctions.getFormattedOutputDate1(rs.getDate("created_on")));
                departmentPromotionBean.setDpcName(rs.getString("dpc_name"));
                //departmentPromotionBean.setPostGroupType(rs.getString("post_group"));
                dpcDataList.add(departmentPromotionBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return dpcDataList;
    }

    @Override
    public DepartmentPromotionBean getDPCData(int dpcId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        DepartmentPromotionBean departmentPromotionBean = new DepartmentPromotionBean();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT * FROM dpc_master WHERE dpc_id=?");
            pst.setInt(1, dpcId);
            rs = pst.executeQuery();
            if (rs.next()) {
                departmentPromotionBean.setDpcId(rs.getInt("dpc_id"));
                departmentPromotionBean.setFiscalYearFrom(rs.getString("fiscalyear_from"));
                departmentPromotionBean.setFiscalYearTo(rs.getString("fiscalyear_to"));
                departmentPromotionBean.setCadrecode(rs.getString("cadre_code"));
                departmentPromotionBean.setDpcName(rs.getString("dpc_name"));
                //departmentPromotionBean.setPostGroupType(rs.getString("post_group"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return departmentPromotionBean;
    }

    @Override
    public ArrayList getEmployeeListCadrewise(int dpcId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean flag = false;
        ArrayList employeeListCadrewise = new ArrayList();
        DepartmentPromotionBean departmentPromotionBean = getDPCData(dpcId);
        try {
            con = dataSource.getConnection();
            int fyearfrom = Integer.parseInt(departmentPromotionBean.getFiscalYearFrom().substring(0, 4));//2018-19
            int fyearto = Integer.parseInt(departmentPromotionBean.getFiscalYearTo().substring(0, 4));//2018-19
            String fiscalYearStr = "";
            for (int i = fyearfrom; i <= fyearto; i++) {
                String tfiscalYearStr = ((i + 1) + "").substring(2, 4);
                fiscalYearStr = fiscalYearStr + "'" + i + "-" + tfiscalYearStr + "'" + ",";

            }
            fiscalYearStr = fiscalYearStr.substring(0, fiscalYearStr.length() - 1);

            pst = con.prepareStatement("Select trim(ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ')) EMP_NAME,cur_spc,POST,par_master.emp_id,dpc_detail.hrms_id,post_grp_type,DOB,deploy_type from "
                    + "(select distinct emp_id,post_group from par_master where  cadre_code=? ) as par_master "
                    + "INNER JOIN (select * from emp_mast where dep_code in ('02','03','04','06','09'))empmast  "
                    + "ON  par_master.emp_id = empmast.emp_id "
                    + "left outer JOIN G_SPC ON empmast.CUR_SPC = G_SPC.SPC "
                    + "left outer JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "LEFT OUTER JOIN (SELECT * FROM dpc_detail WHERE dpc_id=?)dpc_detail ON dpc_detail.hrms_id = par_master.emp_id "
                    + "left outer join g_deploy_type on empmast.dep_code= g_deploy_type.deploy_code order by EMP_NAME asc");
            pst.setString(1, departmentPromotionBean.getCadrecode());
            pst.setInt(2, dpcId);
            rs = pst.executeQuery();

            while (rs.next()) {
                DepartmentPromotionDetail departmentPromotionDetail = new DepartmentPromotionDetail();
                departmentPromotionDetail.setHrmsId(rs.getString("emp_id"));
                departmentPromotionDetail.setEmpName(rs.getString("EMP_NAME"));
                departmentPromotionDetail.setSpc(rs.getString("cur_spc"));
                departmentPromotionDetail.setEmpPost(rs.getString("POST"));
                if (rs.getString("hrms_id") == null || rs.getString("hrms_id").equals("")) {
                    departmentPromotionDetail.setIsadded("N");
                } else {
                    departmentPromotionDetail.setIsadded("Y");
                }
                departmentPromotionDetail.setPostGroupType(rs.getString("post_grp_type"));
                departmentPromotionDetail.setDob(CommonFunctions.getFormattedOutputDate6(rs.getDate("dob")));
                departmentPromotionDetail.setDeployType(rs.getString("deploy_type"));
                employeeListCadrewise.add(departmentPromotionDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return employeeListCadrewise;
    }

    @Override
    public void deleteDPCData(int dpcId) {
        ResultSet rs = null;
        boolean flag = false;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("DELETE FROM dpc_master where dpc_id=?");
            pst.setInt(1, dpcId);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void savecadrewisenrcStatementDetailReport(DepartmentPromotionDetail departmentPromotionDetail) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO dpc_detail(hrms_id,dpc_id) VALUES(?,?)");
            pst.setString(1, departmentPromotionDetail.getHrmsId());
            pst.setInt(2, departmentPromotionDetail.getDpcId());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deletecadrewisenrcStatementDetailReport(DepartmentPromotionDetail departmentPromotionDetail) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("delete from dpc_detail where dpc_id = ? and hrms_id=?");
            pst.setInt(1, departmentPromotionDetail.getDpcId());
            pst.setString(2, departmentPromotionDetail.getHrmsId());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getcadrewisenrcStatementDetailReport(int dpcId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean flag = false;
        ArrayList cadrewisenrcReport = new ArrayList();
        DepartmentPromotionBean departmentPromotionBean = getDPCData(dpcId);
        try {
            con = dataSource.getConnection();
            int fyearfrom = Integer.parseInt(departmentPromotionBean.getFiscalYearFrom().substring(0, 4));//2018-19
            int fyearto = Integer.parseInt(departmentPromotionBean.getFiscalYearTo().substring(0, 4));//2018-19
            String fiscalYearStr = "";

            pst = con.prepareStatement("Select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,cur_spc,POST,dpc_detail.hrms_id,DOB from dpc_detail "
                    + "inner join emp_mast ON  dpc_detail.hrms_id = emp_mast.emp_id "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE where dpc_id=? ");
            pst.setInt(1, dpcId);
            rs = pst.executeQuery();
            while (rs.next()) {
                DepartmentPromotionDetail dpcdetail = new DepartmentPromotionDetail();
                dpcdetail.setHrmsId(rs.getString("hrms_id"));
                dpcdetail.setEmpName(rs.getString("EMP_NAME"));
                dpcdetail.setSpc(rs.getString("cur_spc"));
                dpcdetail.setEmpPost(rs.getString("POST"));

                dpcdetail.setDob(CommonFunctions.getFormattedOutputDate6(rs.getDate("DOB")));

                ArrayList nrcFiscalYearList = new ArrayList();
                for (int i = fyearfrom; i <= fyearto; i++) {
                    String tfiscalYearStr = ((i + 1) + "").substring(2, 4);
                    fiscalYearStr = i + "-" + tfiscalYearStr;

                    ArrayList yearwisePardata = getYearWiseParData(fiscalYearStr, dpcdetail.getHrmsId());
                    //if (yearwisePardata.size() > 0) {
                    FiscalYearWiseParData nrcfy = new FiscalYearWiseParData();
                    nrcfy.setFy(fiscalYearStr);
                    nrcfy.setYearwisedata(yearwisePardata);
                    nrcFiscalYearList.add(nrcfy);
                    //}

                }
                dpcdetail.setFiscalYearList(nrcFiscalYearList);
                cadrewisenrcReport.add(dpcdetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst, con);
        }

        return cadrewisenrcReport;
    }

    public ArrayList getYearWiseParData(String fiscalYear, String empId) {
        ArrayList<FiscalYearWiseParData.Parabstractdata> yearwisePardata = new ArrayList();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        FiscalYearWiseParData f = new FiscalYearWiseParData();
        boolean dataPresent = false;
        try {
            //Collections.sort(yearwisePardata);
            String fromFiscalYear = fiscalYear.substring(0, 4);
            con = dataSource.getConnection();
            /*NRC Data*/
            pst = con.prepareStatement("select par_master.is_reviewed,parid,period_from,period_to,par_status,reason,POST_GROUP from par_master "
                    + " LEFT OUTER JOIN g_par_nrc_reason ON par_master.nrcreason = g_par_nrc_reason.reason_id "
                    + " where emp_id=? and fiscal_year=? AND par_status='17'");

            pst.setString(1, empId);
            pst.setString(2, fiscalYear);
            rs = pst.executeQuery();
            while (rs.next()) {
                dataPresent = true;
                FiscalYearWiseParData.Parabstractdata parabstractdata = f.new Parabstractdata();
                parabstractdata.setPostGroup(rs.getString("post_group"));
                parabstractdata.setParid(rs.getInt("parid"));
                parabstractdata.setPeriodfrom(CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_FROM")));
                parabstractdata.setPeriodto(CommonFunctions.getFormattedOutputDate2(rs.getDate("PERIOD_TO")));
                parabstractdata.setParstatus(rs.getInt("par_status"));
                parabstractdata.setRemark(rs.getString("reason"));
                parabstractdata.setIsreviewed(rs.getString("is_reviewed"));
                yearwisePardata.add(parabstractdata);
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);
            /*NRC Data*/
            /*PAR Data*/
            pst = con.prepareStatement("select parid,period_from,period_to,par_status,is_reviewed,is_adversed,getpargradename(grade_id) as reporting_grade,getpargradename(overallgrading) as reviewing_grade,POST_GROUP from par_master "
                    + "LEFT OUTER JOIN (SELECT par_id,grade_id FROM par_reporting_tran WHERE IS_COMPLETED='Y' AND HIERARCHY_NO=1) AS par_reporting_tran on par_master.parid = par_reporting_tran.par_id  "
                    + "LEFT OUTER JOIN (SELECT par_id,overallgrading FROM par_reviewing_tran WHERE IS_COMPLETED='Y' AND HIERARCHY_NO=1) AS par_reviewing_tran on par_master.parid = par_reviewing_tran.par_id "
                    + "where emp_id=? and fiscal_year=? AND par_status != 17");

            pst.setString(1, empId);
            pst.setString(2, fiscalYear);
            rs = pst.executeQuery();

            while (rs.next()) {
                dataPresent = true;
                FiscalYearWiseParData.Parabstractdata parabstractdata = f.new Parabstractdata();
                parabstractdata.setPostGroup(rs.getString("post_group"));
                parabstractdata.setParid(rs.getInt("parid"));
                parabstractdata.setPeriodfrom(CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_FROM")));
                parabstractdata.setPeriodto(CommonFunctions.getFormattedOutputDate2(rs.getDate("PERIOD_TO")));
                parabstractdata.setParstatus(rs.getInt("par_status"));
                parabstractdata.setRemark("");
                parabstractdata.setIsreviewed(rs.getString("is_reviewed"));
                parabstractdata.setIsadversed(rs.getString("is_adversed"));
                parabstractdata.setGradeName(rs.getString("reporting_grade"));
                if (rs.getString("reviewing_grade") != null && !rs.getString("reviewing_grade").equals("")) {
                    parabstractdata.setGradeName(rs.getString("reviewing_grade"));
                } else if (parabstractdata.getParstatus() == 0) {
                    parabstractdata.setGrade("Not Initiated");
                }
                yearwisePardata.add(parabstractdata);
            }
            /*PAR Data*/

            /*Get Period where PAR or NRC is not Present*/
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date fromDate = sdf.parse("01/04/" + fromFiscalYear);
            for (int i = 0; i < yearwisePardata.size(); i++) {
                FiscalYearWiseParData.Parabstractdata parabstractdata = yearwisePardata.get(i);
                Date date1 = CommonFunctions.getDateFromString(parabstractdata.getPeriodfrom(), "dd-MMM-yyyy");
                if (date1.compareTo(fromDate) > 0 && i == 0) {
                    date1 = DateUtils.addDays(date1, -1);
                    /*Calculat no of Days*/
                    long difference = date1.getTime() - fromDate.getTime();
                    float daysBetween = (difference / (1000 * 60 * 60 * 24));
                    FiscalYearWiseParData.Parabstractdata wantingparabstractdata = f.new Parabstractdata();
                    wantingparabstractdata.setParid(0);
                    wantingparabstractdata.setPeriodfrom(CommonFunctions.getFormattedOutputDate1(fromDate));
                    wantingparabstractdata.setPeriodto(CommonFunctions.getFormattedOutputDate2(date1));
                    if (daysBetween <= 120) {
                        wantingparabstractdata.setGrade("NRC");
                    } else {
                        wantingparabstractdata.setGrade("Not Initiated");
                    }
                    wantingparabstractdata.setRemark("");
                    wantingparabstractdata.setIsreviewed("");
                    wantingparabstractdata.setGradeName("");
                    //yearwisePardata.add(wantingparabstractdata);
                } else if (date1.compareTo(fromDate) < 0) {

                } else {

                }
            }
            /*PAR Not Present*/
            if (!dataPresent) {
                FiscalYearWiseParData.Parabstractdata parabstractdata = f.new Parabstractdata();
                parabstractdata.setParid(0);
                String fromdate = "01/04/" + fromFiscalYear;
                String todate = "31/03/" + (Integer.parseInt(fromFiscalYear) + 1);
                parabstractdata.setPeriodfrom(CommonFunctions.getFormattedOutputDate2(CommonFunctions.getDateFromString(fromdate, "dd/MM/yyyy")));
                parabstractdata.setPeriodto(CommonFunctions.getFormattedOutputDate2(CommonFunctions.getDateFromString(todate, "dd/MM/yyyy")));
                parabstractdata.setGrade("Not initiated");
                parabstractdata.setRemark("");
                parabstractdata.setIsreviewed("");
                parabstractdata.setGradeName("");
                yearwisePardata.add(parabstractdata);
            }
            /*PAR Not Present*/

            /*PAR PRESENT BUT NOT SUBMITTED*/
            /* if (dataPresent) {

             System.out.println("Par Created But Not Submitted");
             FiscalYearWiseParData.Parabstractdata parabstractdata = f.new Parabstractdata();
             if (parabstractdata.getParstatus() == 0) {
             System.out.println("Par Created But Not Submitted****");
             parabstractdata.setParid(0);
             String fromdate = "01/04/" + fromFiscalYear;
             String todate = "31/03/" + (Integer.parseInt(fromFiscalYear) + 1);
             parabstractdata.setPeriodfrom(CommonFunctions.getFormattedOutputDate2(CommonFunctions.getDateFromString(fromdate, "dd/MM/yyyy")));
             parabstractdata.setPeriodto(CommonFunctions.getFormattedOutputDate2(CommonFunctions.getDateFromString(todate, "dd/MM/yyyy")));
             parabstractdata.setGrade("Par Created But Not Submitted");
             parabstractdata.setRemark("");
             parabstractdata.setIsreviewed("");
             parabstractdata.setGradeName("");
             }
             yearwisePardata.add(parabstractdata);
             } */
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst, con);
        }
        Collections.sort(yearwisePardata);
        return yearwisePardata;
    }

    @Override
    public List getParStatusList() {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List<ParStatus> parStatusList = new ArrayList();
        ParStatus parStatus = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT STATUS_ID,STATUS_NAME FROM G_PROCESS_STATUS WHERE PROCESS_ID=3 ORDER BY STATUS_NAME";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                parStatus = new ParStatus();
                parStatus.setStatusId(rs.getString("STATUS_ID"));
                parStatus.setStatusName(rs.getString("STATUS_NAME"));
                parStatusList.add(parStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parStatusList;
    }

    public void saveAcceptingRemarksForReview(ParApplyForm parApplyForm) {
        Connection con = null;

        PreparedStatement pstmt = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {

            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT pactid FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=?");
            pstmt.setInt(1, parApplyForm.getParId());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                parApplyForm.setPactid(rs.getInt("pactid"));
            }

            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            pstmt = con.prepareStatement("UPDATE PAR_ACCEPTING_TRAN SET overallgrading=? WHERE PAR_ID=? and pactid=?");
            pstmt.setInt(1, Integer.parseInt(parApplyForm.getSltAcceptingGrading()));
            pstmt.setInt(2, parApplyForm.getParId());
            pstmt.setInt(3, parApplyForm.getPactid());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void savecustodianremarksAdversePAR(ParAdverseCommunicationDetail parAdverseCommunicationDetail) {
        String diskfileName = null;
        PreparedStatement pst = null;
        Connection con = null;
        String originalFileName = null;
        String contentType = null;
        ResultSet rs = null;
        String toempId = null;
        String toSpc = null;
        String mobileno = null;
        try {
            con = dataSource.getConnection();
            String tauthoritytype = parAdverseCommunicationDetail.getAuthoritytype();
            //String authoritytype = tauthoritytype.substring(0, tauthoritytype.lastIndexOf("-"));
            //String authid1 = tauthoritytype.substring(tauthoritytype.lastIndexOf("-"));
            String[] authoritytypeArr = tauthoritytype.split("-");
            String authoritytype = authoritytypeArr[0];
            String authid1 = authoritytypeArr[1];

            Parauthorityhelperbean phelper = getParAuthorityForAdverse(authoritytype, Integer.parseInt(authid1));
            toempId = phelper.getAuthorityempid();
            toSpc = phelper.getAuthorityspc();

            if (!parAdverseCommunicationDetail.getUploadDocument().isEmpty()) {
                diskfileName = new Date().getTime() + "";
                originalFileName = parAdverseCommunicationDetail.getUploadDocument().getOriginalFilename();
                contentType = parAdverseCommunicationDetail.getUploadDocument().getContentType();
                byte[] bytes = parAdverseCommunicationDetail.getUploadDocument().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + diskfileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }
            pst = con.prepareStatement("SELECT mobile FROM EMP_MAST WHERE emp_id=?");
            pst.setString(1, toempId);
            rs = pst.executeQuery();
            if (rs.next()) {
                parAdverseCommunicationDetail.setMobileNo(rs.getString("mobile"));
                mobileno = parAdverseCommunicationDetail.getMobileNo();
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            Long curtime = new Date().getTime();

            pst = con.prepareStatement("INSERT INTO par_adverse_communication(parid,from_emp_id,from_spc,to_emp_id,to_spc,remarks,disk_file_name,org_file_name,file_type,remarks_on_date,file_path,communication_date,from_auth_type,to_auth_type) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, parAdverseCommunicationDetail.getParId());
            pst.setString(2, parAdverseCommunicationDetail.getFromempId());
            pst.setString(3, parAdverseCommunicationDetail.getFromspc());
            pst.setString(4, toempId);
            pst.setString(5, toSpc);
            pst.setString(6, parAdverseCommunicationDetail.getRemarksdetail());
            pst.setString(7, diskfileName);
            pst.setString(8, originalFileName);
            pst.setString(9, contentType);
            pst.setTimestamp(10, new Timestamp(curtime));
            pst.setString(11, this.uploadPath);
            pst.setTimestamp(12, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(13, parAdverseCommunicationDetail.getFromAuthType());
            pst.setString(14, parAdverseCommunicationDetail.getToAuthType());
            pst.executeUpdate();

            rs = pst.getGeneratedKeys();
            int communicationId = 0;
            if (rs.next()) {
                communicationId = rs.getInt("communication_id");
            }

            if (mobileno != null && !mobileno.equals("")) {
                String msg = "Dear Sir/Madam,\n" + "Substantiation on Representation of Authority " + toempId + " against Adverse Remark may be please submitted within 15 days.";
                new SMSServices(mobileno, msg, "1407165354380648668");
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            int taskId = 0;
            pst = con.prepareStatement("SELECT task_id FROM par_adverse_communication WHERE parid=?");
            pst.setInt(1, parAdverseCommunicationDetail.getParId());
            rs = pst.executeQuery();
            if (rs.next()) {
                taskId = rs.getInt("task_id");

                if (taskId == 0) {
                    //if (parAdverseCommunicationDetail.getTaskId() == 0) {
                    pst = con.prepareStatement("INSERT INTO TASK_MASTER(PROCESS_ID, INITIATED_BY, INITIATED_ON, STATUS_ID, PENDING_AT,INITIATED_SPC,PENDING_SPC,ref_id) Values (?,?,?,?,?,?,?,?)");
                    pst.setInt(1, 18);//In g_workflow_process for REVIEWING ADVERSE
                    pst.setString(2, parAdverseCommunicationDetail.getFromempId());
                    pst.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
                    pst.setInt(4, 103);//IN G_PROCESS_STATUS FOR AWAITED REMARKS FROM AUTHORITY
                    pst.setString(5, toempId);
                    pst.setString(6, parAdverseCommunicationDetail.getFromspc());
                    pst.setString(7, toSpc);
                    pst.setInt(8, communicationId);
                    pst.executeUpdate();

                    DataBaseFunctions.closeSqlObjects(pst);

                    pst = con.prepareStatement("UPDATE PAR_MASTER SET adverse_comm_status_id=103 WHERE parid=?");
                    pst.setInt(1, parAdverseCommunicationDetail.getParId());
                    pst.executeUpdate();

                    DataBaseFunctions.closeSqlObjects(pst);
                } else {
                    pst = con.prepareStatement("UPDATE TASK_MASTER SET PROCESS_ID=?, INITIATED_BY=?, INITIATED_ON=?, STATUS_ID = ?, PENDING_AT =?,INITIATED_SPC =?,PENDING_SPC =?,ref_id =? where task_id=?");
                    pst.setInt(1, 18);//In g_workflow_process for REVIEWING ADVERSE
                    pst.setString(2, parAdverseCommunicationDetail.getFromempId());
                    pst.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
                    pst.setInt(4, 103);//IN G_PROCESS_STATUS FOR AWAITED REMARKS FROM AUTHORITY
                    pst.setString(5, toempId);
                    pst.setString(6, parAdverseCommunicationDetail.getFromspc());
                    pst.setString(7, toSpc);
                    pst.setInt(8, communicationId);
                    pst.setInt(9, taskId);
                    pst.executeUpdate();

                    DataBaseFunctions.closeSqlObjects(pst);

                    pst = con.prepareStatement("UPDATE PAR_MASTER SET adverse_comm_status_id=103 WHERE parid=?");
                    pst.setInt(1, parAdverseCommunicationDetail.getParId());
                    pst.executeUpdate();

                    DataBaseFunctions.closeSqlObjects(pst);

                    pst = con.prepareStatement("UPDATE par_adverse_communication SET task_id=? WHERE communication_id=?");
                    pst.setInt(1, taskId);
                    pst.setInt(2, communicationId);
                    pst.executeUpdate();

                    DataBaseFunctions.closeSqlObjects(pst);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
    /*while Custodian Initiate Adverse Communication With Appraisee*/

    @Override
    public void savecustodianremarksAdversePARToAppraisee(ParAdverseCommunicationDetail parAdverseCommunicationDetail
    ) {
        String diskfileName = null;
        PreparedStatement pst = null;
        Connection con = null;
        String originalFileName = null;
        String contentType = null;
        ResultSet rs = null;
        String mobileno = null;
        try {
            con = dataSource.getConnection();

            if (!parAdverseCommunicationDetail.getUploadDocument().isEmpty()) {
                diskfileName = new Date().getTime() + "";
                originalFileName = parAdverseCommunicationDetail.getUploadDocument().getOriginalFilename();
                contentType = parAdverseCommunicationDetail.getUploadDocument().getContentType();
                byte[] bytes = parAdverseCommunicationDetail.getUploadDocument().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + diskfileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }

            pst = con.prepareStatement("SELECT mobile FROM EMP_MAST WHERE emp_id=?");
            pst.setString(1, parAdverseCommunicationDetail.getToempId());
            rs = pst.executeQuery();
            if (rs.next()) {
                parAdverseCommunicationDetail.setMobileNo(rs.getString("mobile"));
                mobileno = parAdverseCommunicationDetail.getMobileNo();
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            Long curtime = new Date().getTime();

            pst = con.prepareStatement("INSERT INTO par_adverse_communication(parid,from_emp_id,from_spc,to_emp_id,to_spc,remarks,disk_file_name,org_file_name,file_type,remarks_on_date,file_path,communication_date,from_auth_type,to_auth_type) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, parAdverseCommunicationDetail.getParId());
            pst.setString(2, parAdverseCommunicationDetail.getFromempId());
            pst.setString(3, parAdverseCommunicationDetail.getFromspc());
            pst.setString(4, parAdverseCommunicationDetail.getToempId());
            pst.setString(5, parAdverseCommunicationDetail.getTospc());
            pst.setString(6, parAdverseCommunicationDetail.getRemarksdetail());
            pst.setString(7, diskfileName);
            pst.setString(8, originalFileName);
            pst.setString(9, contentType);
            pst.setTimestamp(10, new Timestamp(curtime));
            pst.setString(11, this.uploadPath);
            pst.setTimestamp(12, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(13, parAdverseCommunicationDetail.getFromAuthType());
            pst.setString(14, parAdverseCommunicationDetail.getToAuthType());
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            int communicationId = 0;
            if (rs.next()) {
                communicationId = rs.getInt("communication_id");
            }

            if (mobileno != null && !mobileno.equals("")) {
                String msg = "Dear Sir/Madam,\n" + "Representation If any on the Adverse remarks communicated to you is awaited within 45 days.";
                new SMSServices(mobileno, msg, "1407162814759675173");
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            if (parAdverseCommunicationDetail.getTaskId() == 0) {
                pst = con.prepareStatement("INSERT INTO TASK_MASTER(PROCESS_ID, INITIATED_BY, INITIATED_ON, STATUS_ID, PENDING_AT,INITIATED_SPC,PENDING_SPC,ref_id) Values (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                pst.setInt(1, 18);//In g_workflow_process for REVIEWING ADVERSE
                pst.setString(2, parAdverseCommunicationDetail.getFromempId());
                pst.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setInt(4, 101);//IN G_PROCESS_STATUS FOR REVIEWING PAR IS ADVERSED BY CUSTODIAN
                pst.setString(5, parAdverseCommunicationDetail.getToempId());
                pst.setString(6, parAdverseCommunicationDetail.getFromspc());
                pst.setString(7, parAdverseCommunicationDetail.getTospc());
                pst.setInt(8, communicationId);
                pst.executeUpdate();

                rs = pst.getGeneratedKeys();
                int taskId = 0;
                if (rs.next()) {
                    taskId = rs.getInt("task_id");
                }
                // DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("UPDATE PAR_MASTER SET adverse_comm_status_id=101 WHERE parid=?");
                pst.setInt(1, parAdverseCommunicationDetail.getParId());
                pst.executeUpdate();

                pst = con.prepareStatement("UPDATE par_adverse_communication SET task_id=? WHERE parid=?");
                pst.setInt(1, taskId);
                pst.setInt(2, parAdverseCommunicationDetail.getParId());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);
            } else {
                pst = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=NULL,PENDING_SPC=NULL WHERE TASK_ID=?");
                pst.setInt(1, parAdverseCommunicationDetail.getTaskId());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void saveparAdverseCommunicationReply(ParAdverseCommunicationDetail parAdverseCommunicationDetail) {
        String diskfileName = null;
        PreparedStatement pst = null;
        Connection con = null;
        String originalFileName = null;
        String contentType = null;
        ResultSet rs = null;
        String mobileno = null;
        try {
            con = dataSource.getConnection();

            if (!parAdverseCommunicationDetail.getUploadDocument().isEmpty()) {
                diskfileName = new Date().getTime() + "";
                originalFileName = parAdverseCommunicationDetail.getUploadDocument().getOriginalFilename();
                contentType = parAdverseCommunicationDetail.getUploadDocument().getContentType();
                byte[] bytes = parAdverseCommunicationDetail.getUploadDocument().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + diskfileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            Long curtime = new Date().getTime();

            pst = con.prepareStatement("SELECT mobile FROM EMP_MAST WHERE emp_id=?");
            pst.setString(1, parAdverseCommunicationDetail.getToempId());
            rs = pst.executeQuery();
            if (rs.next()) {
                parAdverseCommunicationDetail.setMobileNo(rs.getString("mobile"));
                mobileno = parAdverseCommunicationDetail.getMobileNo();
            }
            DataBaseFunctions.closeSqlObjects(rs, pst);

            pst = con.prepareStatement("INSERT INTO par_adverse_communication(parid,from_emp_id,from_spc,to_emp_id,to_spc,remarks,disk_file_name,org_file_name,file_type,remarks_on_date,file_path,communication_date,from_auth_type,to_auth_type) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, parAdverseCommunicationDetail.getParId());
            pst.setString(2, parAdverseCommunicationDetail.getFromempId());
            pst.setString(3, parAdverseCommunicationDetail.getFromspc());
            pst.setString(4, parAdverseCommunicationDetail.getToempId());
            pst.setString(5, parAdverseCommunicationDetail.getTospc());
            pst.setString(6, parAdverseCommunicationDetail.getRemarksdetail());
            pst.setString(7, diskfileName);
            pst.setString(8, originalFileName);
            pst.setString(9, contentType);
            pst.setTimestamp(10, new Timestamp(curtime));
            pst.setString(11, this.uploadPath);
            pst.setTimestamp(12, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(13, parAdverseCommunicationDetail.getFromAuthType());
            pst.setString(14, parAdverseCommunicationDetail.getToAuthType());
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            if (parAdverseCommunicationDetail.getAdverseCommunicationStatusId() == 102) {
                if (mobileno != null && !mobileno.equals("")) {
                    String msg = "Dear Sir/Madam,\n" + "Representation Of Appraisee " + parAdverseCommunicationDetail.getToempId() + " on Adverse Remark is submitted by him/her.";
                    new SMSServices(mobileno, msg, "1407165354378285235");
                }
            } else if (parAdverseCommunicationDetail.getAdverseCommunicationStatusId() == 113) {
                if (mobileno != null && !mobileno.equals("")) {
                    String msg = "Dear Sir/Madam,\n" + "Substantiation Reply Of Appraisee " + parAdverseCommunicationDetail.getToempId() + " on Adverse Remark is submitted by him/her.";
                    new SMSServices(mobileno, msg, "1407165354383249115");
                }
            }

            pst = con.prepareStatement("UPDATE PAR_MASTER SET adverse_comm_status_id=? WHERE parid=?");//102
            pst.setInt(1, parAdverseCommunicationDetail.getAdverseCommunicationStatusId());
            pst.setInt(2, parAdverseCommunicationDetail.getParId());
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=NULL,PENDING_SPC=NULL,status_id=? WHERE TASK_ID=?");
            pst.setInt(1, parAdverseCommunicationDetail.getAdverseCommunicationStatusId());
            pst.setInt(2, parAdverseCommunicationDetail.getTaskId());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getcustodianremarksAdversePAR(int parId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList custodianadverseremarkList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select getspn(par_adverse_communication.from_spc) as from_post,get_empname_from_type(par_adverse_communication.from_emp_id ,'G') as from_empname,"
                    + " from_emp_id,from_spc,org_file_name,disk_file_name,remarks,get_empname_from_type(par_adverse_communication.to_emp_id ,'G') as to_empname,"
                    + " getspn(par_adverse_communication.to_spc) as to_post,to_emp_id,to_spc,communication_date,from_auth_type,to_auth_type,communication_id from par_adverse_communication where parid =?");
            pst.setInt(1, parId);
            rs = pst.executeQuery();
            while (rs.next()) {
                ParAdverseCommunicationDetail parAdverseCommunicationDetail = new ParAdverseCommunicationDetail();
                parAdverseCommunicationDetail.setFromPost(rs.getString("from_post"));
                parAdverseCommunicationDetail.setFromempName(rs.getString("from_empname"));
                parAdverseCommunicationDetail.setFromempId(rs.getString("from_emp_id"));
                parAdverseCommunicationDetail.setFromspc(rs.getString("from_spc"));
                parAdverseCommunicationDetail.setOriginalFilename(rs.getString("org_file_name"));
                parAdverseCommunicationDetail.setDiskFileName(rs.getString("disk_file_name"));
                parAdverseCommunicationDetail.setRemarksdetail(rs.getString("remarks"));
                parAdverseCommunicationDetail.setToempName(rs.getString("to_empname"));
                parAdverseCommunicationDetail.setToPost(rs.getString("to_post"));
                parAdverseCommunicationDetail.setToempId(rs.getString("to_emp_id"));
                parAdverseCommunicationDetail.setTospc(rs.getString("to_spc"));
                parAdverseCommunicationDetail.setAdverseCommunicationOnDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("communication_date")));
                parAdverseCommunicationDetail.setFromAuthType(rs.getString("from_auth_type"));
                parAdverseCommunicationDetail.setToAuthType(rs.getString("to_auth_type"));
                parAdverseCommunicationDetail.setCommunicationId(rs.getInt("communication_id"));

                custodianadverseremarkList.add(parAdverseCommunicationDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return custodianadverseremarkList;
    }

    @Override
    public ParAdverseCommunicationDetail getCommunicationDetails(int communicationId) {
        ParAdverseCommunicationDetail parAdverseCommunicationDetail = new ParAdverseCommunicationDetail();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select communication_id,parid,getspn(par_adverse_communication.from_spc) as from_post,get_empname_from_type(par_adverse_communication.from_emp_id ,'G') as from_empname,"
                    + "from_emp_id,from_spc,org_file_name,remarks,get_empname_from_type(par_adverse_communication.to_emp_id ,'G') as to_empname, to_emp_id,to_spc from par_adverse_communication where communication_id =?");
            pst.setInt(1, communicationId);
            rs = pst.executeQuery();
            if (rs.next()) {
                parAdverseCommunicationDetail.setCommunicationId(rs.getInt("communication_id"));
                parAdverseCommunicationDetail.setParId(rs.getInt("parid"));
                parAdverseCommunicationDetail.setFromPost(rs.getString("from_post"));
                parAdverseCommunicationDetail.setFromempName(rs.getString("from_empname"));
                parAdverseCommunicationDetail.setFromempId(rs.getString("from_emp_id"));
                parAdverseCommunicationDetail.setFromspc(rs.getString("from_spc"));
                parAdverseCommunicationDetail.setOriginalFilename(rs.getString("org_file_name"));
                parAdverseCommunicationDetail.setRemarksdetail(rs.getString("remarks"));
                parAdverseCommunicationDetail.setToempId(rs.getString("to_emp_id"));
                parAdverseCommunicationDetail.setTospc(rs.getString("to_spc"));
                parAdverseCommunicationDetail.setToempName(rs.getString("to_empname"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parAdverseCommunicationDetail;
    }

    public void markParAsAdverse(int parId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            con = dataSource.getConnection();
            Long curtime = new Date().getTime();
            pstmt = con.prepareStatement("UPDATE PAR_MASTER SET is_adversed=? WHERE PARID=?");
            pstmt.setString(1, "Y");
            pstmt.setInt(2, parId);
            int i = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            DataBaseFunctions.closeSqlObjects(res, pstmt, con);
        }
    }

    @Override
    public void getAppraiseForAdversePAR(ParAdverseCommunicationDetail parAdverseCommunicationDetail) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT par_master.emp_id,par_master.spc,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME, post ,cur_spc from par_master \n"
                    + "INNER JOIN emp_mast ON emp_mast.emp_id = par_master.emp_id "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "where parid =? ");
            pstmt.setInt(1, parAdverseCommunicationDetail.getParId());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                parAdverseCommunicationDetail.setToempId(rs.getString("emp_id"));
                parAdverseCommunicationDetail.setTospc(rs.getString("spc"));
                parAdverseCommunicationDetail.setToempName(rs.getString("EMPNAME"));
                parAdverseCommunicationDetail.setToPost(rs.getString("post"));
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public ParAdverseCommunicationDetail getAttachedFileforAdversePAR(int parId, int communicationinId) {
        ParAdverseCommunicationDetail parAdverseCommunicationDetail = new ParAdverseCommunicationDetail();
        Connection con = null;
        Statement st = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select org_file_name,disk_file_name,file_type,file_path from par_adverse_communication where parid = ? and communication_id=?");
            pst.setInt(1, parId);
            pst.setInt(2, communicationinId);
            rs = pst.executeQuery();
            String filepath = null;
            if (rs.next()) {
                parAdverseCommunicationDetail.setOriginalFilename(rs.getString("org_file_name"));
                parAdverseCommunicationDetail.setDiskFileName(rs.getString("disk_file_name"));
                parAdverseCommunicationDetail.setGetContentType("file_type");
                filepath = rs.getString("file_path");
            }
            File f = new File(filepath + File.separator + parAdverseCommunicationDetail.getDiskFileName());
            parAdverseCommunicationDetail.setFilecontent(FileUtils.readFileToByteArray(f));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return parAdverseCommunicationDetail;
    }

    public Parauthorityhelperbean getParAuthorityForAdverse(String authoritytype, int authid) {
        Connection con = null;
        PreparedStatement pstamt = null;
        ResultSet resultset = null;
        Parauthorityhelperbean parhelper = null;
        try {
            con = dataSource.getConnection();
            if (authoritytype.equalsIgnoreCase("REPORTING")) {

                pstamt = con.prepareStatement("SELECT REPORTING_EMP_ID,reporting_cur_spc,INITIALS,F_NAME,M_NAME,L_NAME FROM PAR_REPORTING_TRAN "
                        + "INNER JOIN EMP_MAST ON PAR_REPORTING_TRAN.REPORTING_EMP_ID = EMP_MAST.EMP_ID WHERE prptid =?");
                pstamt.setInt(1, authid);
                resultset = pstamt.executeQuery();
                if (resultset.next()) {
                    parhelper = new Parauthorityhelperbean();
                    parhelper.setAuthorityname(StringUtils.defaultString(resultset.getString("INITIALS")) + " " + StringUtils.defaultString(resultset.getString("F_NAME")) + " " + StringUtils.defaultString(resultset.getString("M_NAME")) + " " + StringUtils.defaultString(resultset.getString("L_NAME")));
                    parhelper.setAuthorityspc(resultset.getString("reporting_cur_spc"));
                    parhelper.setAuthorityempid(resultset.getString("REPORTING_EMP_ID"));
                }
            } else if (authoritytype.equalsIgnoreCase("REVIEWING")) {
                pstamt = con.prepareStatement("SELECT REVIEWING_EMP_ID,REVIEWING_CUR_SPC,INITIALS,F_NAME,M_NAME,L_NAME FROM PAR_REVIEWING_TRAN "
                        + "INNER JOIN EMP_MAST ON PAR_REVIEWING_TRAN.REVIEWING_EMP_ID = EMP_MAST.EMP_ID WHERE prvtid=?");
                pstamt.setInt(1, authid);
                resultset = pstamt.executeQuery();
                if (resultset.next()) {
                    parhelper = new Parauthorityhelperbean();
                    parhelper.setAuthorityname(StringUtils.defaultString(resultset.getString("INITIALS")) + " " + StringUtils.defaultString(resultset.getString("F_NAME")) + " " + StringUtils.defaultString(resultset.getString("M_NAME")) + " " + StringUtils.defaultString(resultset.getString("L_NAME")));
                    parhelper.setAuthorityspc(resultset.getString("REVIEWING_CUR_SPC"));
                    parhelper.setAuthorityempid(resultset.getString("REVIEWING_EMP_ID"));
                }
            } else if (authoritytype.equalsIgnoreCase("ACCEPTING")) {
                pstamt = con.prepareStatement("SELECT ACCEPTING_EMP_ID,accepting_cur_spc,INITIALS,F_NAME,M_NAME,L_NAME FROM PAR_ACCEPTING_TRAN "
                        + "INNER JOIN EMP_MAST ON PAR_ACCEPTING_TRAN.ACCEPTING_EMP_ID = EMP_MAST.EMP_ID  WHERE  pactid=?");
                pstamt.setInt(1, authid);
                resultset = pstamt.executeQuery();
                if (resultset.next()) {
                    parhelper = new Parauthorityhelperbean();
                    parhelper.setAuthorityname(StringUtils.defaultString(resultset.getString("INITIALS")) + " " + StringUtils.defaultString(resultset.getString("F_NAME")) + " " + StringUtils.defaultString(resultset.getString("M_NAME")) + " " + StringUtils.defaultString(resultset.getString("L_NAME")));
                    parhelper.setAuthorityspc(resultset.getString("accepting_cur_spc"));
                    parhelper.setAuthorityempid(resultset.getString("ACCEPTING_EMP_ID"));
                }
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(resultset);
            DataBaseFunctions.closeSqlObjects(pstamt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parhelper;
    }

    @Override
    public ArrayList getTotalAdverseCommunicationListForCustdian() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList totalAdverseCommunication = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select getspn(par_adverse_communication.from_spc) as from_post,get_empname_from_type(par_adverse_communication.from_emp_id ,'G') as from_empname,"
                    + " from_emp_id,from_spc,org_file_name,disk_file_name,remarks,get_empname_from_type(par_adverse_communication.to_emp_id ,'G') as to_empname,"
                    + " getspn(par_adverse_communication.to_spc) as to_post,to_emp_id,to_spc,communication_date,from_auth_type,to_auth_type,communication_id from par_adverse_communication");
            //pst.setString(1, spc);
            rs = pst.executeQuery();
            while (rs.next()) {
                ParAdverseCommunicationDetail parAdverseCommunicationDetail = new ParAdverseCommunicationDetail();
                parAdverseCommunicationDetail.setFromPost(rs.getString("from_post"));
                parAdverseCommunicationDetail.setFromempName(rs.getString("from_empname"));
                parAdverseCommunicationDetail.setFromempId(rs.getString("from_emp_id"));
                parAdverseCommunicationDetail.setFromspc(rs.getString("from_spc"));
                parAdverseCommunicationDetail.setOriginalFilename(rs.getString("org_file_name"));
                parAdverseCommunicationDetail.setDiskFileName(rs.getString("disk_file_name"));
                parAdverseCommunicationDetail.setRemarksdetail(rs.getString("remarks"));
                parAdverseCommunicationDetail.setToempName(rs.getString("to_empname"));
                parAdverseCommunicationDetail.setToPost(rs.getString("to_post"));
                parAdverseCommunicationDetail.setToempId(rs.getString("to_emp_id"));
                parAdverseCommunicationDetail.setTospc(rs.getString("to_spc"));
                parAdverseCommunicationDetail.setAdverseCommunicationOnDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("communication_date")));
                parAdverseCommunicationDetail.setFromAuthType(rs.getString("from_auth_type"));
                parAdverseCommunicationDetail.setToAuthType(rs.getString("to_auth_type"));
                parAdverseCommunicationDetail.setCommunicationId(rs.getInt("communication_id"));

                totalAdverseCommunication.add(parAdverseCommunicationDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return totalAdverseCommunication;
    }

    @Override
    public List getGroupWiseParStatus(String fiscalYear) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List parStatusList = new ArrayList();

        try {
            con = this.dataSource.getConnection();

            /*String sql = "select total_cnt,emp_mast_pgrp,par_submitted_cnt, post_group from "
             + "  (select count(*) as total_cnt,post_grp_type as emp_mast_pgrp from emp_mast where IS_REGULAR ='Y' AND  dep_code NOT IN('00', '08', '09', '10', '13', '14') "
             + "  group by post_grp_type)emp_mast "
             + "  inner join "
             + "  (select count(*) as par_submitted_cnt,post_group from par_master where fiscal_year=? and par_status>=0 group by post_group)par_master "
             + "  on emp_mast.emp_mast_pgrp = par_master.post_group ";*/
            String sql = "select emp_mast.*,parmast.*  from "
                    + "(select count(post_group) as par_submitted_cnt,post_group  from "
                    + "(select emp_id,department_code,post_group from (select emp_id,off_code,post_group from par_master where par_status> 0 "
                    + "and FISCAL_YEAR =? group by emp_id,off_code,post_group) par_mast "
                    + "left outer join g_office on par_mast.off_code=g_office.off_code)pm group by post_group) parmast "
                    + "inner join "
                    + "(select pn.total_cnt,post_grp_type from "
                    + "(select count(*)as total_cnt ,post_grp_type  from emp_mast where IS_REGULAR ='Y' AND  dep_code "
                    + " NOT IN('00', '08', '09', '10', '13', '14') and post_grp_type in ('A','B') group by post_grp_type)pn)emp_mast"
                    + " on parmast.post_group=emp_mast.post_grp_type  order by post_grp_type";
        
        
            pst = con.prepareStatement(sql);
            pst.setString(1, fiscalYear);
            rs = pst.executeQuery();

            while (rs.next()) {
                ParAdminProperties parAdminProperties = new ParAdminProperties();
                parAdminProperties.setTotalNopostgrouptype(rs.getString("post_grp_type"));
                parAdminProperties.setTotalNumberEmployee(rs.getString("total_cnt"));
                parAdminProperties.setTotalParSubmitted(rs.getString("par_submitted_cnt"));
                parStatusList.add(parAdminProperties);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parStatusList;
    }

    @Override
    public void saveEmployeeListForUploadPAR(UploadPreviousPAR uploadPreviousPAR) {
        String diskfileName = null;
        PreparedStatement pst = null;
        Connection con = null;
        String originalFileName = null;
        String contentType = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            if (!uploadPreviousPAR.getPreviousYearPARDocument().isEmpty()) {
                diskfileName = new Date().getTime() + "";
                originalFileName = uploadPreviousPAR.getPreviousYearPARDocument().getOriginalFilename();
                contentType = uploadPreviousPAR.getPreviousYearPARDocument().getContentType();
                byte[] bytes = uploadPreviousPAR.getPreviousYearPARDocument().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + diskfileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();
            }

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            Long curtime = new Date().getTime();

            pst = con.prepareStatement("INSERT INTO par_master(emp_id,fiscal_year,period_from,period_to,par_type_for_previous_year,spc,cadre_code,"
                    + "post_group,final_gradingfor_previousyr_par,original_filename_for_previousyr_par,disk_filename_for_previousyr_par,"
                    + "filetype_for_previousyr_par,filepath_for_previousyr_par,previousyr_par_upload_on,previousyr_par_uploaded_by_empid,"
                    + "previousyr_par_uploaded_by_spc,par_status,nrc_reason_previousyear_par,par_type, si_officer_type, place_of_posting) "
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, uploadPreviousPAR.getEmpId());
            pst.setString(2, uploadPreviousPAR.getFiscalyear());
            pst.setTimestamp(3, new Timestamp(sdf.parse(uploadPreviousPAR.getPrdFrmDate()).getTime()));
            pst.setTimestamp(4, new Timestamp(sdf.parse(uploadPreviousPAR.getPrdToDate()).getTime()));
            pst.setString(5, uploadPreviousPAR.getParTypeForPreviousYearPAR());

            pst.setString(6, uploadPreviousPAR.getHidspc());
            pst.setString(7, uploadPreviousPAR.getHidcadrename());
            pst.setString(8, uploadPreviousPAR.getPostGroupType());
            if (uploadPreviousPAR.getFinalGradingForPreviousYrPAR() != null && !uploadPreviousPAR.getFinalGradingForPreviousYrPAR().equals("") && uploadPreviousPAR.getParTypeForPreviousYearPAR().equals("PAR")) {
                pst.setInt(9, Integer.parseInt(uploadPreviousPAR.getFinalGradingForPreviousYrPAR()));
            } else {
                pst.setInt(9, 0);
            }
            pst.setString(10, originalFileName);
            pst.setString(11, diskfileName);
            pst.setString(12, contentType);
            pst.setString(13, this.uploadPath);
            pst.setTimestamp(14, new Timestamp(curtime));
            pst.setString(15, uploadPreviousPAR.getPreviousyrParUploadedbyempId());
            pst.setString(16, uploadPreviousPAR.getPreviousyrParUploadedbyspc());
            pst.setString(17, uploadPreviousPAR.getParTypeForPreviousYearPAR());
            if (uploadPreviousPAR.getParTypeForPreviousYearPAR() != null && !uploadPreviousPAR.getParTypeForPreviousYearPAR().equals("") && uploadPreviousPAR.getParTypeForPreviousYearPAR().equals("PAR")) {
                pst.setInt(17, 116);
            } else {
                pst.setInt(17, 120);
            }
            pst.setString(18, uploadPreviousPAR.getNrcReasonForPreviousYearPAR());
            if (uploadPreviousPAR.getParType() != null && !uploadPreviousPAR.getParType().equals("") && uploadPreviousPAR.getParType().equals("SiPar")) {
                pst.setString(19, uploadPreviousPAR.getParType());
                pst.setString(20, uploadPreviousPAR.getSubInspectorType());
                pst.setString(21, uploadPreviousPAR.getPlaceOfPostingDuringPeriod());
            } else {
                pst.setString(19, null);
                pst.setString(20, null);
                pst.setString(21, null);
            }
            int saveRes = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getPreviousYearPARUpload(String previousyrParUploadedbyempId) {
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList uploadPARList = new ArrayList();
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select par_master.par_type_for_previous_year,full_name,fiscal_year,par_master.emp_id,parid, "
                    + " ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,gpf_no,"
                    + " previousyr_par_uploaded_by_empid,previousyr_par_uploaded_by_spc,post,previousyr_par_upload_on,final_gradingfor_previousyr_par,"
                    + " GRADE_NAME,get_empname_from_type(par_master.previousyr_par_uploaded_by_empid ,'G') as uploadedauthorityname,"
                    + " getspn(par_master.previousyr_par_uploaded_by_spc)as uploadedauthoritypost,original_filename_for_previousyr_par,"
                    + " par_type, si_officer_type, place_of_posting from par_master"
                    + " INNER JOIN emp_mast ON emp_mast.emp_id = par_master.emp_id"
                    + " INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC"
                    + " INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + " left outer join user_master um on par_master.previousyr_par_uploaded_by_empid = um.user_id ::text "
                    + " INNER JOIN G_PAR_GRADE ON CAST (par_master.final_gradingfor_previousyr_par AS integer) = G_PAR_GRADE.GRADE_ID where previousyr_par_uploaded_by_empid=?");
            pstmt.setString(1, previousyrParUploadedbyempId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                UploadPreviousPAR uploadPreviousPAR = new UploadPreviousPAR();
                uploadPreviousPAR.setFiscalyear(rs.getString("fiscal_year"));
                uploadPreviousPAR.setParId(rs.getInt("parid"));
                uploadPreviousPAR.setEmpName(rs.getString("EMPNAME"));
                uploadPreviousPAR.setGpfNo(rs.getString("gpf_no"));
                uploadPreviousPAR.setPostName(rs.getString("post"));
                uploadPreviousPAR.setParTypeForPreviousYearPAR(rs.getString("par_type_for_previous_year"));

                String parType = rs.getString("par_type");
                uploadPreviousPAR.setParType(parType);
                if (parType.equals("SiPar")) {
                    //uploadPreviousPAR.setPreviousyrParUploadedbyempId(rs.getString("previousyr_par_uploaded_by_empid"));
                    //uploadPreviousPAR.setPreviousyrParUploadedbyspc(rs.getString("previousyr_par_uploaded_by_spc"));
                    uploadPreviousPAR.setPreviousyrParUploadedbyempName(rs.getString("full_name"));
                    uploadPreviousPAR.setPreviousyrParUploadedbypost("");
                } else {
                    uploadPreviousPAR.setPreviousyrParUploadedbyempId(rs.getString("previousyr_par_uploaded_by_empid"));
                    uploadPreviousPAR.setPreviousyrParUploadedbyspc(rs.getString("previousyr_par_uploaded_by_spc"));
                    uploadPreviousPAR.setPreviousyrParUploadedbyempName(rs.getString("uploadedauthorityname"));
                    uploadPreviousPAR.setPreviousyrParUploadedbypost(rs.getString("uploadedauthoritypost"));
                }

                uploadPreviousPAR.setPreviousyrParUploadOn(CommonFunctions.getFormattedOutputDate1(rs.getDate("previousyr_par_upload_on")));
                uploadPreviousPAR.setFinalGradingForPreviousYrPAR(rs.getString("final_gradingfor_previousyr_par"));
                uploadPreviousPAR.setFinalGradingNameForPreviousYrPAR(rs.getString("GRADE_NAME"));
                uploadPreviousPAR.setOriginalFileNameForpreviousYearPAR(rs.getString("original_filename_for_previousyr_par"));

                uploadPreviousPAR.setSubInspectorType(rs.getString("si_officer_type"));
                uploadPreviousPAR.setPlaceOfPostingDuringPeriod(rs.getString("place_of_posting"));

                uploadPARList.add(uploadPreviousPAR);
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();

        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return uploadPARList;
    }

    public UploadPreviousPAR getAttachedFileOfPreviousYearPAR(int parid) {
        UploadPreviousPAR uploadPreviousPAR = new UploadPreviousPAR();
        Connection con = null;
        Statement st = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select original_filename_for_previousyr_par,disk_filename_for_previousyr_par,filetype_for_previousyr_par,filepath_for_previousyr_par from par_master where parid = ?");
            pst.setInt(1, parid);
            rs = pst.executeQuery();
            String filepath = null;
            if (rs.next()) {
                uploadPreviousPAR.setOriginalFileNameForpreviousYearPAR(rs.getString("original_filename_for_previousyr_par"));
                uploadPreviousPAR.setDiskfileNameForpreviousYearPAR(rs.getString("disk_filename_for_previousyr_par"));
                uploadPreviousPAR.setGetContentType("filetype_for_previousyr_par");
                filepath = rs.getString("filepath_for_previousyr_par");
            }
            File f = new File(filepath + File.separator + uploadPreviousPAR.getDiskfileNameForpreviousYearPAR());
            uploadPreviousPAR.setFilecontent(FileUtils.readFileToByteArray(f));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return uploadPreviousPAR;
    }

    public Users getEmployeeProfileInfo(String hrmsid) {
        Users emp = new Users();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String sql = "";
        try {
            con = this.repodataSource.getConnection();
            sql = " SELECT A.EMP_ID,ARRAY_TO_STRING(ARRAY[A.INITIALS, A.F_NAME, A.M_NAME, A.L_NAME], ' ') EMPNAME, A.POST_GRP_TYPE,gpf_no,"
                    + " D.CADRE_NAME, D.CADRE_CODE,E.OFF_EN, E.OFF_CODE,E.ddo_code, B.SPC, B.SPN, B.GPC, C.POST, C.POST_CODE,"
                    + " C.DEPARTMENT_CODE,GD.department_name, if_profile_completed,isauthority, ddo_hrmsid, E.dist_code FROM EMP_MAST A "
                    + " left outer join G_CADRE D on A.CUR_CADRE_CODE=D.CADRE_CODE "
                    + " LEFT outer join G_SPC B on A.CUR_SPC=B.SPC "
                    + " left outer join g_deploy_type G ON A.dep_code=G.deploy_code "
                    + " left outer join g_post C on B.gpc=C.post_code "
                    + " LEFT OUTER JOIN g_department GD ON C.department_code = GD.department_code,  G_OFFICE E "
                    + " WHERE A.CUR_OFF_CODE=E.OFF_CODE AND A.EMP_ID=? ";
            ps = con.prepareStatement(sql);
            ps.setString(1, hrmsid);

            rs = ps.executeQuery();
            if (rs.next()) {
                emp.setEmpId(hrmsid);
                emp.setFullName(rs.getString("EMPNAME"));
                //emp.setDob(rs.getDate("DOB"));
                //emp.setMobile(rs.getString("MOBILE"));
                //emp.setEmailId(rs.getString("email_id"));
                emp.setGpfno(rs.getString("gpf_no"));
                emp.setPostgrp(rs.getString("POST_GRP_TYPE"));
                emp.setCadrecode(rs.getString("CADRE_CODE"));
                emp.setCadrename(rs.getString("CADRE_NAME"));
                emp.setSpn(rs.getString("SPN"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emp;
    }

    public String isAppraiseeSubmittedPAR(String empId, String fiscalYear) {
        String isAppraiseeSubmittedPAR = null;
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select parid from par_master where emp_id=? and fiscal_year=?");
            pst.setString(1, empId);
            pst.setString(2, fiscalYear);
            rs = pst.executeQuery();
            if (rs.next()) {
                isAppraiseeSubmittedPAR = "Y";
            } else {
                isAppraiseeSubmittedPAR = "N";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return isAppraiseeSubmittedPAR;
    }

    public void saveForceForwardDetail(ParForceForwardBean parForceForwardBean) {
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            Long curtime = new Date().getTime();

            pst = con.prepareStatement("INSERT INTO par_forceforward_log(fiscal_year,department_code,cadre_code,from_authority,to_authority,forceforward_on,forceforward_type) VALUES(?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, parForceForwardBean.getFiscalyear());
            pst.setString(2, parForceForwardBean.getDeptcode());
            pst.setString(3, parForceForwardBean.getCadreCode());
            pst.setString(4, parForceForwardBean.getFromAuthority());
            pst.setString(5, parForceForwardBean.getToAuthority());
            pst.setTimestamp(6, new Timestamp(curtime));
            pst.setString(7, parForceForwardBean.getForceForwardType());

            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public String forceForwardCadrewise(ParForceForwardBean parForceForwardBean) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        String pending_at = "";
        int status_id = 0;
        int hierarchy_no = 0;
        String msg = "Y";
        try {
            con = dataSource.getConnection();
            Calendar cal = Calendar.getInstance();
            DateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
            //ParForceForwardBean parForceForwardBean = new ParForceForwardBean();
            /*Force Forwar Reporting to Reviewing*/
            if ((parForceForwardBean.getFromAuthority() != null && !parForceForwardBean.getFromAuthority().equals("")) && parForceForwardBean.getFromAuthority().equals("reporting")) {
                status_id = 6;
                parForceForwardBean.setParstatus(6);
            } else if ((parForceForwardBean.getFromAuthority() != null && !parForceForwardBean.getFromAuthority().equals("")) && parForceForwardBean.getFromAuthority().equals("reviewing")) {
                parForceForwardBean.setParstatus(7);
                status_id = 7;
            } else if ((parForceForwardBean.getFromAuthority() != null && !parForceForwardBean.getFromAuthority().equals("")) && parForceForwardBean.getFromAuthority().equals("accepting")) {
                parForceForwardBean.setParstatus(8);
            }
            if ((parForceForwardBean.getFromAuthority() != null && !parForceForwardBean.getFromAuthority().equals("")) && parForceForwardBean.getFromAuthority().equals("reporting")) {
                pstmt = con.prepareStatement("select par_master.parid,task_master.task_id,task_master.pending_at,ref_id_of_table from par_master "
                        + "inner join task_master on par_master.task_id = task_master.task_id where fiscal_year=? AND PAR_STATUS=? AND  cadre_code=?  ");
                pstmt.setString(1, parForceForwardBean.getFiscalyear());
                pstmt.setInt(2, parForceForwardBean.getParstatus());
                pstmt.setString(3, parForceForwardBean.getCadreCode());
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    pending_at = rs.getString("pending_at");
                    pstmt1 = con.prepareStatement("SELECT PRVTID,REVIEWING_EMP_ID,REVIEWING_CUR_SPC FROM PAR_REVIEWING_TRAN WHERE PAR_ID=? ORDER BY HIERARCHY_NO");
                    pstmt1.setInt(1, rs.getInt("parid"));
                    rs1 = pstmt1.executeQuery();
                    if (rs1.next()) {
                        updateAuthorityforForceForward(pending_at, rs.getInt("parid"), status_id, dateformat.format(cal.getTime()));
                        updatePARforForceForward(rs1.getString("REVIEWING_EMP_ID"), rs1.getString("REVIEWING_CUR_SPC"), 7, rs.getInt("task_id"), rs.getInt("parid"), rs1.getInt("PRVTID"));
                    }
                }
                /*Force Forward Reviewing to Accepting*/
            } else if ((parForceForwardBean.getFromAuthority() != null && !parForceForwardBean.getFromAuthority().equals("")) && parForceForwardBean.getFromAuthority().equals("reviewing")) {
                pstmt = con.prepareStatement("select par_master.parid,task_master.task_id,task_master.pending_at from par_master "
                        + "inner join task_master on par_master.task_id = task_master.task_id WHERE fiscal_year=? AND PAR_STATUS=? AND  cadre_code=?  ");
                pstmt.setString(1, (parForceForwardBean.getFiscalyear()));
                pstmt.setInt(2, (parForceForwardBean.getParstatus()));
                pstmt.setString(3, (parForceForwardBean.getCadreCode()));
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    pending_at = rs.getString("pending_at");
                    pstmt2 = con.prepareStatement("SELECT PACTID,ACCEPTING_EMP_ID,ACCEPTING_CUR_SPC FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=? ORDER BY HIERARCHY_NO");
                    pstmt2.setInt(1, rs.getInt("parid"));
                    rs2 = pstmt2.executeQuery();
                    if (rs2.next()) {
                        updateAuthorityforForceForward(pending_at, rs.getInt("parid"), status_id, dateformat.format(cal.getTime()));
                        updatePARforForceForward(rs2.getString("ACCEPTING_EMP_ID"), rs2.getString("ACCEPTING_CUR_SPC"), 8, rs.getInt("task_id"), rs.getInt("parid"), rs2.getInt("PACTID"));
                    }
                }
            }
        } catch (Exception e) {
            msg = "N";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, rs1, rs2);
            DataBaseFunctions.closeSqlObjects(pstmt, pstmt1, pstmt2);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    @Override
    public String forceForwardAll(ParForceForwardBean parForceForwardBean) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs4 = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        String msg = "Y";
        int totalNumberAuthority = 0;
        try {
            con = dataSource.getConnection();
            Calendar cal = Calendar.getInstance();
            DateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
            /*Force Forwar Reporting to Reviewing*/
            if ((parForceForwardBean.getFromAuthority() != null && !parForceForwardBean.getFromAuthority().equals("")) && parForceForwardBean.getFromAuthority().equals("reporting")) {

                List<ParForceForwardBean> listofpar = getParForForceForward(parForceForwardBean.getFiscalyear(), ParForceForwardBean.PENDING_AT_REPORTING_STATUS);
                for (ParForceForwardBean pffb : listofpar) {
                    ParForceForwardBean reviewingForceForwardAuthorityDetail = getReviewingForceForwardAuthorityDetail(pffb.getParId());
                    updateforceForwardToReviewingAuthority(reviewingForceForwardAuthorityDetail, pffb);
                    updateforceForwardFromReportingAuthority(reviewingForceForwardAuthorityDetail, pffb);
                    updateforceForwardDetailInLogTable(reviewingForceForwardAuthorityDetail, pffb);
                }

                /*Force Forward Reviewing to Accepting*/
            } else if ((parForceForwardBean.getFromAuthority() != null && !parForceForwardBean.getFromAuthority().equals("")) && parForceForwardBean.getFromAuthority().equals("reviewing")) {
                List<ParForceForwardBean> listofpar = getParForForceForward(parForceForwardBean.getFiscalyear(), ParForceForwardBean.PENDING_AT_REVIEWING_STATUS);
                for (ParForceForwardBean pffb : listofpar) {
                    ParForceForwardBean acceptingForceForwardAuthorityDetail = getAcceptingForceForwardAuthorityDetail(pffb.getParId());
                    updateforceForwardToAcceptingAuthority(acceptingForceForwardAuthorityDetail, pffb);
                    updateforceForwardFromReviewingAuthority(acceptingForceForwardAuthorityDetail, pffb);
                    //updateforceForwardReviewingDetailInLogTable(acceptingForceForwardAuthorityDetail, pffb);
                    //forceForwardFromReviewingAuthority();
                }
            }
        } catch (Exception e) {
            msg = "N";
//            logger.log(Level.ERROR, e.getMessage());
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, rs1);
            DataBaseFunctions.closeSqlObjects(pstmt, pstmt1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    private void updateforceForwardFromReviewingAuthority(ParForceForwardBean acceptingForceForwardAuthorityDetail, ParForceForwardBean pffb) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("select * from par_reviewing_tran where par_id=? and (todate::DATE - fromdate::DATE) > 120 order by hierarchy_no");
            pstmt.setInt(1, pffb.getParId());
            res = pstmt.executeQuery();
            if (res.next()) {
                pstmt = con.prepareStatement("UPDATE par_reviewing_tran SET IS_COMPLETED=?,SUBMITTED_ON=? WHERE PAR_ID=? AND IS_COMPLETED is null and (todate::DATE - fromdate::DATE) > 120");
                pstmt.setString(1, "F");
                pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                pstmt.setInt(3, pffb.getParId());
                pstmt.executeUpdate();
                //System.out.println("111 update while  reviewing is > 120 days-----" + pffb.getParId() + "---status---" + "F" + "--submitted on--" + "pffb.getParId()");
            }
            pstmt = con.prepareStatement("select * from par_reviewing_tran where par_id=? and (todate::DATE - fromdate::DATE) < 120 order by hierarchy_no");
            pstmt.setInt(1, pffb.getParId());
            res = pstmt.executeQuery();
            if (res.next()) {
                pstmt = con.prepareStatement("UPDATE par_reviewing_tran SET IS_COMPLETED=?,SUBMITTED_ON=? WHERE PAR_ID=? AND IS_COMPLETED is null");
                pstmt.setString(1, null);
                pstmt.setTimestamp(2, null);
                pstmt.setInt(3, pffb.getParId());
                pstmt.executeUpdate();
            }

        } catch (SQLException e) {
//            logger.log(Level.ERROR, e.getMessage());
            //           logger.log(Level.ERROR, "Error at updateforceForwardFromReviewingAuthority:" + acceptingForceForwardAuthorityDetail);
//            logger.log(Level.ERROR, "Error at updateforceForwardFromReviewingAuthority:" + pffb);
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt, con);
        }
    }

    private void updateforceForwardToAcceptingAuthority(ParForceForwardBean acceptingForceForwardAuthorityDetail, ParForceForwardBean pffb) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            con = dataSource.getConnection();
            if (acceptingForceForwardAuthorityDetail == null) {
                /*No accepting authority period is more that 120 days*/
                pstmt = con.prepareStatement("UPDATE PAR_MASTER SET PAR_STATUS=?,REF_ID_OF_TABLE=?,is_force_forward_from_reviewing=? WHERE PARID=?");
                pstmt.setInt(1, ParForceForwardBean.PAR_COMPLETED_STATUS);
                pstmt.setInt(2, 0);
                pstmt.setString(3, "Y");
                pstmt.setInt(4, pffb.getParId());
                pstmt.executeUpdate();
                //System.out.println("parstatus is  ***** 9");
                //System.out.println("parid is *****" + pffb.getParId());

                pstmt = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=?,APPLY_TO=?,PENDING_SPC=?,STATUS_ID=? WHERE TASK_ID=?");
                pstmt.setString(1, null);
                pstmt.setString(2, null);
                pstmt.setString(3, null);
                pstmt.setInt(4, ParForceForwardBean.PAR_COMPLETED_STATUS);
                pstmt.setInt(5, pffb.getTaskId());
                pstmt.executeUpdate();

                pstmt = con.prepareStatement("UPDATE par_forceforward_detail_log SET status_id_after_forceforward=?,pending_at_after_forceforward=? WHERE PAR_ID=?");
                pstmt.setInt(1, ParForceForwardBean.PAR_COMPLETED_STATUS);
                pstmt.setString(2, null);
                pstmt.setInt(3, pffb.getParId());
                pstmt.executeUpdate();
                System.out.println("Inside log ***parstatus--9--" + ParForceForwardBean.PAR_COMPLETED_STATUS + "Pendingat" + "" + "ParId()---" + pffb.getParId());

                pstmt = con.prepareStatement("UPDATE par_accepting_tran SET IS_COMPLETED=?,SUBMITTED_ON=? WHERE PAR_ID=? AND IS_COMPLETED is null");
                pstmt.setString(1, "");
                pstmt.setTimestamp(2, null);
                pstmt.setInt(3, pffb.getParId());
                pstmt.executeUpdate();
                System.out.println("update while no accepting is 120 days-----" + pffb.getParId() + "---status---" + "" + "--submitted on--" + "");
            } else {
                pstmt = con.prepareStatement("UPDATE PAR_MASTER SET PAR_STATUS=?,REF_ID_OF_TABLE=?,is_force_forward_from_reviewing=? WHERE PARID=?");
                pstmt.setInt(1, ParForceForwardBean.PENDING_AT_ACCEPTING_STATUS);
                pstmt.setInt(2, acceptingForceForwardAuthorityDetail.getRefid());
                pstmt.setString(3, "Y");
                pstmt.setInt(4, pffb.getParId());
                pstmt.executeUpdate();
                System.out.println("parstatus is  ***** 8");
                System.out.println("parid is *****" + pffb.getParId());

                pstmt = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=?,APPLY_TO=?,PENDING_SPC=?,STATUS_ID=? WHERE TASK_ID=?");
                pstmt.setString(1, acceptingForceForwardAuthorityDetail.getPendingat());
                pstmt.setString(2, acceptingForceForwardAuthorityDetail.getPendingat());
                pstmt.setString(3, acceptingForceForwardAuthorityDetail.getPendingspc());
                pstmt.setInt(4, ParForceForwardBean.PENDING_AT_ACCEPTING_STATUS);
                pstmt.setInt(5, pffb.getTaskId());
                pstmt.executeUpdate();

                pstmt = con.prepareStatement("UPDATE par_forceforward_detail_log SET status_id_after_forceforward=?,pending_at_after_forceforward=? WHERE PAR_ID=?");
                pstmt.setInt(1, ParForceForwardBean.PENDING_AT_ACCEPTING_STATUS);
                pstmt.setString(2, acceptingForceForwardAuthorityDetail.getPendingat());
                pstmt.setInt(3, pffb.getParId());
                pstmt.executeUpdate();
                System.out.println("Inside log ***parstatus--8--" + ParForceForwardBean.PENDING_AT_ACCEPTING_STATUS + "Pendingat"
                        + acceptingForceForwardAuthorityDetail.getPendingat() + "ParId()---" + pffb.getParId());
            }
        } catch (SQLException e) {
//            logger.log(Level.ERROR, e.getMessage());
            //           logger.log(Level.ERROR, "Error At:" + acceptingForceForwardAuthorityDetail);
            //           logger.log(Level.ERROR, "Error At:" + pffb);
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt, con);
        }
    }

    private ParForceForwardBean getAcceptingForceForwardAuthorityDetail(int parId) {
        ParForceForwardBean acceptingForceForwardAuthorityDetail = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            con = repodataSource.getConnection();
            pstmt = con.prepareStatement("select * from par_accepting_tran where par_id=? and (todate::DATE - fromdate::DATE) > 120 order by hierarchy_no");
            pstmt.setInt(1, parId);
            res = pstmt.executeQuery();
            if (res.next()) {
                acceptingForceForwardAuthorityDetail = new ParForceForwardBean();
                acceptingForceForwardAuthorityDetail.setPendingat(res.getString("accepting_emp_id"));
                acceptingForceForwardAuthorityDetail.setPendingspc(res.getString("accepting_cur_spc"));
                acceptingForceForwardAuthorityDetail.setRefid(res.getInt("pactid"));
            }
        } catch (SQLException e) {
//            logger.log(Level.ERROR, e.getMessage());
            //          logger.log(Level.ERROR, "Error Occured at : " + acceptingForceForwardAuthorityDetail);
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt, con);
        }
        return acceptingForceForwardAuthorityDetail;
    }

    public List<ParForceForwardBean> getParForForceForward(String fiscalYear, int pendingAtStatus) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        List<ParForceForwardBean> listOfPar = new ArrayList<>();
        try {
            con = repodataSource.getConnection();
            pstmt = con.prepareStatement("select par_forceforward_detail_log.par_id,task_master.task_id,task_master.pending_at from par_master "
                    + "inner join task_master on par_master.task_id = task_master.task_id "
                    + "inner join par_forceforward_detail_log on par_master.parid = par_forceforward_detail_log.par_id "
                    + "WHERE par_master.fiscal_year=? AND PAR_STATUS=? and status_id_after_forceforward is null limit 1000");
//            logger.info("getParForForceForward: Fiscal Year:" + fiscalYear);
            //          logger.info("getParForForceForward: pendingAtStatus:" + pendingAtStatus);
            pstmt.setString(1, fiscalYear);
            pstmt.setInt(2, pendingAtStatus);
            res = pstmt.executeQuery();
            while (res.next()) {
                ParForceForwardBean pffb = new ParForceForwardBean();
                pffb.setParId(res.getInt("par_id"));
                pffb.setTaskId(res.getInt("task_id"));
                pffb.setPendingat(res.getString("pending_at"));
                listOfPar.add(pffb);
            }
//            logger.info("Total Par to be force forward:" + listOfPar.size());
        } catch (SQLException e) {
            //           logger.log(Level.ERROR, e.getMessage());
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt, con);
        }
        return listOfPar;
    }

    private ParForceForwardBean getReviewingForceForwardAuthorityDetail(int parId) {
        ParForceForwardBean reviewingForceForwardAuthorityDetail = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        boolean isReviewingForward = false;
        boolean isAcceptingForward = false;
        try {
            con = repodataSource.getConnection();
            pstmt = con.prepareStatement("select * from par_reviewing_tran where par_id=? and (todate::DATE - fromdate::DATE) > 120 order by hierarchy_no");
            pstmt.setInt(1, parId);
            // System.out.println("No Reviewing is Greater than 120 ,,parId is **" + parId);
            res = pstmt.executeQuery();
            if (res.next()) {
                reviewingForceForwardAuthorityDetail = new ParForceForwardBean();
                reviewingForceForwardAuthorityDetail.setPendingat(res.getString("reviewing_emp_id"));
                reviewingForceForwardAuthorityDetail.setPendingspc(res.getString("reviewing_cur_spc"));
                reviewingForceForwardAuthorityDetail.setRefid(res.getInt("prvtid"));
                reviewingForceForwardAuthorityDetail.setParstatus(7);
                isReviewingForward = true;
            }
            if (isReviewingForward == false) {
                pstmt = con.prepareStatement("select * from par_accepting_tran where par_id=? and (todate::DATE - fromdate::DATE) > 120 order by hierarchy_no");
                pstmt.setInt(1, parId);
                res = pstmt.executeQuery();
                if (res.next()) {
                    reviewingForceForwardAuthorityDetail = new ParForceForwardBean();
                    reviewingForceForwardAuthorityDetail.setPendingat(res.getString("accepting_emp_id"));
                    reviewingForceForwardAuthorityDetail.setPendingspc(res.getString("accepting_cur_spc"));
                    reviewingForceForwardAuthorityDetail.setRefid(res.getInt("pactid"));
                    reviewingForceForwardAuthorityDetail.setParstatus(8);
                    isAcceptingForward = true;
                }
            }
            if (isAcceptingForward == false && isReviewingForward == false) {
                reviewingForceForwardAuthorityDetail = new ParForceForwardBean();
                reviewingForceForwardAuthorityDetail.setParstatus(9);
            }

        } catch (SQLException e) {
            //logger.log(Level.ERROR, e.getMessage());
            //logger.log(Level.ERROR, "Error Occured at : " + acceptingForceForwardAuthorityDetail);
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt, con);
        }
        return reviewingForceForwardAuthorityDetail;
    }

    private void updateforceForwardFromReportingAuthority(ParForceForwardBean reviewingForceForwardAuthorityDetail, ParForceForwardBean pffb) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            con = dataSource.getConnection();
            if (reviewingForceForwardAuthorityDetail == null) {
                /*No Reviewing authority period is more that 120 days*/
                pstmt = con.prepareStatement("UPDATE par_reporting_tran SET IS_COMPLETED=?,SUBMITTED_ON=? WHERE PAR_ID=?");
                pstmt.setString(1, "");
                pstmt.setTimestamp(2, null);
                pstmt.setInt(3, pffb.getParId());
                pstmt.executeUpdate();
            } else {
                pstmt = con.prepareStatement("UPDATE par_reporting_tran SET IS_COMPLETED=?,SUBMITTED_ON=? WHERE PAR_ID=?");
                pstmt.setString(1, "F");
                pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                pstmt.setInt(3, pffb.getParId());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
//            logger.log(Level.ERROR, e.getMessage());
            //           logger.log(Level.ERROR, "Error at updateforceForwardFromReviewingAuthority:" + acceptingForceForwardAuthorityDetail);
//            logger.log(Level.ERROR, "Error at updateforceForwardFromReviewingAuthority:" + pffb);
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt, con);
        }
    }

    private void updateforceForwardToReviewingAuthority(ParForceForwardBean reviewingForceForwardAuthorityDetail,
            ParForceForwardBean pffb) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            con = dataSource.getConnection();
            if (reviewingForceForwardAuthorityDetail.getParstatus() == 8) {
                pstmt = con.prepareStatement("UPDATE PAR_MASTER SET PAR_STATUS=?,REF_ID_OF_TABLE=?,is_force_forward=? WHERE PARID=?");
                pstmt.setInt(1, ParForceForwardBean.PENDING_AT_ACCEPTING_STATUS);
                pstmt.setInt(2, reviewingForceForwardAuthorityDetail.getRefid());
                pstmt.setString(3, "Y");
                pstmt.setInt(4, pffb.getParId());
                pstmt.executeUpdate();

                pstmt = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=?,APPLY_TO=?,PENDING_SPC=?,STATUS_ID=? WHERE TASK_ID=?");
                pstmt.setString(1, reviewingForceForwardAuthorityDetail.getPendingat());
                pstmt.setString(2, reviewingForceForwardAuthorityDetail.getPendingat());
                pstmt.setString(3, reviewingForceForwardAuthorityDetail.getPendingspc());
                pstmt.setInt(4, ParForceForwardBean.PENDING_AT_ACCEPTING_STATUS);
                pstmt.setInt(5, pffb.getTaskId());
                pstmt.executeUpdate();
            } else if (reviewingForceForwardAuthorityDetail.getParstatus() == 7) {
                pstmt = con.prepareStatement("UPDATE PAR_MASTER SET PAR_STATUS=?,REF_ID_OF_TABLE=?,is_force_forward=? WHERE PARID=?");
                pstmt.setInt(1, ParForceForwardBean.PENDING_AT_REVIEWING_STATUS);
                pstmt.setInt(2, reviewingForceForwardAuthorityDetail.getRefid());
                pstmt.setString(3, "Y");
                pstmt.setInt(4, pffb.getParId());

                pstmt.executeUpdate();

                pstmt = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=?,APPLY_TO=?,PENDING_SPC=?,STATUS_ID=? WHERE TASK_ID=?");
                pstmt.setString(1, reviewingForceForwardAuthorityDetail.getPendingat());
                pstmt.setString(2, reviewingForceForwardAuthorityDetail.getPendingat());
                pstmt.setString(3, reviewingForceForwardAuthorityDetail.getPendingspc());
                pstmt.setInt(4, ParForceForwardBean.PENDING_AT_REVIEWING_STATUS);
                pstmt.setInt(5, pffb.getTaskId());
                pstmt.executeUpdate();
            } else if (reviewingForceForwardAuthorityDetail.getParstatus() == 9) {
                pstmt = con.prepareStatement("UPDATE PAR_MASTER SET PAR_STATUS=?,REF_ID_OF_TABLE=?,is_force_forward=? WHERE PARID=?");
                pstmt.setInt(1, ParForceForwardBean.PAR_COMPLETED_STATUS);
                pstmt.setInt(2, 0);
                pstmt.setString(3, "Y");
                pstmt.setInt(4, pffb.getParId());
                pstmt.executeUpdate();

                pstmt = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=?,APPLY_TO=?,PENDING_SPC=?,STATUS_ID=? WHERE TASK_ID=?");
                pstmt.setString(1, null);
                pstmt.setString(2, null);
                pstmt.setString(3, null);
                pstmt.setInt(4, ParForceForwardBean.PAR_COMPLETED_STATUS);
                pstmt.setInt(5, pffb.getTaskId());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
//            logger.log(Level.ERROR, e.getMessage());
            //           logger.log(Level.ERROR, "Error At:" + acceptingForceForwardAuthorityDetail);
            //           logger.log(Level.ERROR, "Error At:" + pffb);
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt, con);
        }
    }

    private void updateforceForwardDetailInLogTable(ParForceForwardBean reviewingForceForwardAuthorityDetail, ParForceForwardBean pffb) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("UPDATE par_forceforward_detail_log SET status_id_after_forceforward=?,pending_at_after_forceforward=? WHERE PAR_ID=?");
            pstmt.setInt(1, reviewingForceForwardAuthorityDetail.getParstatus());
            pstmt.setString(2, reviewingForceForwardAuthorityDetail.getPendingat());
            pstmt.setInt(3, pffb.getParId());
            pstmt.executeUpdate();

        } catch (SQLException e) {

        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt, con);
        }
    }

    private void updateforceForwardReviewingDetailInLogTable(ParForceForwardBean acceptingForceForwardAuthorityDetail, ParForceForwardBean pffb) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("UPDATE par_forceforward_detail_log SET status_id_after_forceforward=?,pending_at_after_forceforward=? WHERE PAR_ID=?");
            pstmt.setInt(1, acceptingForceForwardAuthorityDetail.getParstatus());
            pstmt.setString(2, acceptingForceForwardAuthorityDetail.getPendingat());
            pstmt.setInt(3, pffb.getParId());
            pstmt.executeUpdate();
            System.out.println("Inside log ***parstatus--" + acceptingForceForwardAuthorityDetail.getParstatus() + "Pendingat" + acceptingForceForwardAuthorityDetail.getPendingat()
                    + "ParId()---" + pffb.getParId());
        } catch (SQLException e) {

        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt, con);
        }
    }

    @Override
    public ArrayList getForceForwardDetailListCadrewise() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList forceForwardDetail = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select log_id,fiscal_year,par_forceforward_log.department_code,par_forceforward_log.cadre_code,from_authority,to_authority,forceforward_on,cadre_name,department_name,forceforward_type from par_forceforward_log "
                    + " inner join g_cadre on par_forceforward_log.cadre_code= g_cadre.cadre_code "
                    + " inner join g_department on par_forceforward_log.department_code= g_department.department_code where forceforward_type='cadrewiseforward'");
            rs = pst.executeQuery();
            while (rs.next()) {
                ParForceForwardBean parForceForwardBean = new ParForceForwardBean();
                parForceForwardBean.setLogId(rs.getInt("log_id"));
                parForceForwardBean.setFiscalyear(rs.getString("fiscal_year"));
                parForceForwardBean.setDeptcode(rs.getString("department_code"));
                parForceForwardBean.setCadreCode(rs.getString("cadre_code"));
                parForceForwardBean.setFromAuthority(rs.getString("from_authority"));
                parForceForwardBean.setToAuthority(rs.getString("to_authority"));
                parForceForwardBean.setForceforwardOn(CommonFunctions.getFormattedOutputDate1(rs.getDate("forceforward_on")));
                parForceForwardBean.setCadreName(rs.getString("cadre_name"));
                parForceForwardBean.setDeptName(rs.getString("department_name"));
                parForceForwardBean.setForceForwardType(rs.getString("forceforward_type"));
                forceForwardDetail.add(parForceForwardBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return forceForwardDetail;
    }

    @Override
    public ArrayList getForceForwardDetailListALLCadre() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList forceForwardDetailOfALLCadre = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select * from par_forceforward_log where forceforward_type='forwardall' ");
            rs = pst.executeQuery();
            while (rs.next()) {
                ParForceForwardBean parForceForwardBean = new ParForceForwardBean();
                parForceForwardBean.setLogId(rs.getInt("log_id"));
                parForceForwardBean.setFiscalyear(rs.getString("fiscal_year"));
                parForceForwardBean.setFromAuthority(rs.getString("from_authority"));
                parForceForwardBean.setToAuthority(rs.getString("to_authority"));
                parForceForwardBean.setForceforwardOn(CommonFunctions.getFormattedOutputDate1(rs.getDate("forceforward_on")));
                parForceForwardBean.setForceForwardType(rs.getString("forceforward_type"));
                forceForwardDetailOfALLCadre.add(parForceForwardBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return forceForwardDetailOfALLCadre;
    }

    public void getForceForwardError() {
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        Connection con = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;

        ArrayList totalPARList = new ArrayList();
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("select count (*) cnt from par_master where is_force_forward_from_reviewing = 'Y' ");
            rs = pst.executeQuery();
            while (rs.next()) {
                int totalCount = rs.getInt("cnt");
//                logger.log(Level.INFO, totalCount);
            }
            pst1 = con.prepareStatement("select count (*) from par_master where is_force_forward_from_reviewing = 'Y' AND par_status=7");
            rs1 = pst1.executeQuery();
            while (rs1.next()) {
                int totalCount1 = rs.getInt("cnt");
//                logger.log(Level.INFO, totalCount1);
            }
            pst2 = con.prepareStatement("select parid,emp_id,task_id from par_master where is_force_forward_from_reviewing = 'Y' AND par_status=7");
            rs2 = pst2.executeQuery();
            while (rs2.next()) {
                ParApplyForm parApplyForm = new ParApplyForm();
                parApplyForm.setParId(rs.getInt("par_id"));
                parApplyForm.setEmpId(rs.getString("emp_id"));
                parApplyForm.setTaskId(rs.getInt("task_id"));
                totalPARList.add(parApplyForm);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, rs1, rs2);
            DataBaseFunctions.closeSqlObjects(pst, pst1, pst2);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getForceForwardDetailListForAuthority(String fiscalYear, String reportingEmpId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList forceForwardListForAuthority = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select distinct par_master.parid,par_master.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,submitted_on,post "
                    + "from par_master "
                    + "inner join emp_mast on par_master.emp_id = emp_mast.emp_id "
                    + "inner join par_reporting_tran on par_master.parid = par_reporting_tran.par_id "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "where par_master.is_force_forward='Y' and fiscal_year=? and is_completed='F' and reporting_emp_id=?");
            pst.setString(1, fiscalYear);
            pst.setString(2, reportingEmpId);
            rs = pst.executeQuery();
            while (rs.next()) {
                ParForceForwardBean parForceForwardBean = new ParForceForwardBean();
                parForceForwardBean.setEmpId(rs.getString("emp_id"));
                parForceForwardBean.setParId(rs.getInt("parid"));
                parForceForwardBean.setForceforwardOn(CommonFunctions.getFormattedOutputDate1(rs.getDate("submitted_on")));
                parForceForwardBean.setEmpName(rs.getString("EMPNAME"));
                forceForwardListForAuthority.add(parForceForwardBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return forceForwardListForAuthority;
    }

    @Override
    public void getcheckForceForwardErrorFromReviewingtoAccepting() {
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        PreparedStatement pst3 = null;
        Connection con = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        int pactid = 0;

        ArrayList totalPARList = new ArrayList();
        ParApplyForm parApplyForm = null;
        try {
            con = repodataSource.getConnection();

            pst = con.prepareStatement("select * from par_master where is_force_forward_from_reviewing = 'Y' and fiscal_year='2021-22'");
            rs = pst.executeQuery();
            while (rs.next()) {
                parApplyForm = new ParApplyForm();
                parApplyForm.setParId(rs.getInt("parid"));
                parApplyForm.setEmpId(rs.getString("emp_id"));
                parApplyForm.setParstatus(rs.getInt("par_status"));
                parApplyForm.setRefid(rs.getInt("ref_id_of_table"));
                parApplyForm.setTaskId(rs.getInt("task_id"));
                totalPARList.add(parApplyForm);
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            pst1 = con.prepareStatement("select accepting_emp_id,accepting_cur_spc,is_completed,pactid from par_accepting_tran where par_id=? and (todate::DATE - fromdate::DATE) > 120 order by hierarchy_no asc limit 1");
            pst2 = con.prepareStatement("select pending_at,apply_to,pending_spc from task_master where task_id=? ");
            pst3 = con.prepareStatement("select is_completed from par_reviewing_tran where par_id=? ");

            for (Object totalPARList1 : totalPARList) {
                parApplyForm = (ParApplyForm) totalPARList1;
                pst1.setInt(1, parApplyForm.getParId());
                rs1 = pst1.executeQuery();
                if (rs1.next()) {
                    parApplyForm.setAcceptingempid(rs1.getString("accepting_emp_id"));
                    parApplyForm.setAcceptingSpc(rs1.getString("accepting_cur_spc"));
                    String iscompleted = rs1.getString("is_completed");
                    pactid = rs1.getInt("pactid");
                    if (parApplyForm.getRefid() != pactid) {
                        // logger.log(Level.ERROR, "Error Occured For refid and pactid not same : " + parApplyForm.getParId());
                    }

                }
                //DataBaseFunctions.closeSqlObjects(rs1, pst1);
                pst2.setInt(1, parApplyForm.getTaskId());
                rs2 = pst2.executeQuery();
                if (rs2.next()) {
                    String pendingAt = rs2.getString("pending_at");
                    String applyTo = rs2.getString("apply_to");
                    String pendingSpc = rs2.getString("pending_spc");
                    if (!pendingAt.equals(parApplyForm.getAcceptingempid())) {
//                        logger.log(Level.ERROR, "Error Occured For pendingAt " + parApplyForm.getParId());
                    }
                    if (!applyTo.equals(parApplyForm.getAcceptingempid())) {
                        //                       logger.log(Level.ERROR, "Error Occured For applyTo in taskmaster " + parApplyForm.getParId());
                    }
                    if (!pendingSpc.equals(parApplyForm.getAcceptingempid())) {
                        //                      logger.log(Level.ERROR, "Error Occured For applyTo in taskmaster " + parApplyForm.getParId());
                    }
                    /* if (!pendingAt.(parApplyForm.getAcceptingempid()) || applyTo.equals(parApplyForm.getAcceptingempid()) || pendingSpc.equals(parApplyForm.getAcceptingSpc())) {
                     logger.log(Level.ERROR, "Error Occured For pendingAt,applyto and pendingspc not update : " + parApplyForm.getParId());
                     }*/
                }
                /*pst3.setInt(1, parApplyForm.getParId());
                 rs3 = pst3.executeQuery();
                 if (rs3.next()) {
                 String iscompleted = rs3.getString("is_completed");
                 if (iscompleted != "F") {
                 logger.log(Level.ERROR, "Error Occured For Force forward Not updated : " + parApplyForm.getParId());
                 }
                 }*/
            }

        } catch (Exception e) {
            //           logger.log(Level.ERROR, e.getMessage());
//            logger.log(Level.ERROR, "Error Occured at : " +parApplyForm);
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, rs1, rs2);
            DataBaseFunctions.closeSqlObjects(pst, pst1, pst2);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void savePARStatusDetailByPARAdmin(ParStatusBean parStatusBean) {
        String diskfileName = null;
        PreparedStatement pst = null;
        Connection con = null;
        String originalFileName = null;
        String contentType = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            if (!parStatusBean.getParStatusdocument().isEmpty()) {
                diskfileName = new Date().getTime() + "";
                originalFileName = parStatusBean.getParStatusdocument().getOriginalFilename();
                contentType = parStatusBean.getParStatusdocument().getContentType();
                byte[] bytes = parStatusBean.getParStatusdocument().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + diskfileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            Long curtime = new Date().getTime();

            pst = con.prepareStatement("INSERT INTO par_status_log(financial_year,change_on_date,original_file_name_par_status,disk_file_name_par_status,file_type_par_status,file_path_par_status,par_period_for_appraisee,par_period_for_reporting,is_closed_for_appraisee,is_closed_for_authority,par_period_for_reviewing,par_period_for_accepting) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, parStatusBean.getFiscalyear());
            pst.setTimestamp(2, new Timestamp(curtime));
            pst.setString(3, originalFileName);
            pst.setString(4, diskfileName);
            pst.setString(5, contentType);
            pst.setString(6, this.uploadPath);
            pst.setTimestamp(7, new Timestamp(sdf.parse(parStatusBean.getParPeriodForAppraisee()).getTime()));
            pst.setTimestamp(8, new Timestamp(sdf.parse(parStatusBean.getParPeriodForReporting()).getTime()));
            pst.setString(9, parStatusBean.getIsClosedForAppraisee());
            pst.setString(10, parStatusBean.getIsClosedForAuthority());
            pst.setTimestamp(11, new Timestamp(sdf.parse(parStatusBean.getParPeriodForReviewing()).getTime()));
            pst.setTimestamp(12, new Timestamp(sdf.parse(parStatusBean.getParPeriodForAccepting()).getTime()));
            pst.executeUpdate();

            pst = con.prepareStatement("UPDATE financial_year SET is_closed=?,auth_remarks_closed=?,par_period_for_appraisee=?,par_period_for_reporting=?,par_period_for_reviewing=?,par_period_for_accepting=? where fy=?");
            if (parStatusBean.getIsClosedForAppraisee() != null && parStatusBean.getIsClosedForAppraisee().equals("closeforapp")) {
                pst.setString(1, "Y");
            } else {
                pst.setString(1, "N");
            }

            if (parStatusBean.getIsClosedForAuthority() != null && parStatusBean.getIsClosedForAuthority().equals("closeforauth")) {
                pst.setString(2, "Y");
            } else {
                pst.setString(2, "N");
            }
            pst.setTimestamp(3, new Timestamp(sdf.parse(parStatusBean.getParPeriodForAppraisee()).getTime()));
            pst.setTimestamp(4, new Timestamp(sdf.parse(parStatusBean.getParPeriodForReporting()).getTime()));
            pst.setTimestamp(5, new Timestamp(sdf.parse(parStatusBean.getParPeriodForReviewing()).getTime()));
            pst.setTimestamp(6, new Timestamp(sdf.parse(parStatusBean.getParPeriodForAccepting()).getTime()));
            pst.setString(7, parStatusBean.getFiscalyear());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public ArrayList getPARStatusDetailListByPARAdmin() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList parStatusDetail = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select * from par_status_log");
            rs = pst.executeQuery();
            while (rs.next()) {
                ParStatusBean parStatusBean = new ParStatusBean();
                parStatusBean.setLogId(rs.getInt("log_id"));
                parStatusBean.setFiscalyear(rs.getString("financial_year"));
                parStatusBean.setParStatusChangeOnDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("change_on_date")));
                parStatusBean.setOriginalFileNameForparStatus(rs.getString("original_file_name_par_status"));
                parStatusBean.setDiskfileNameForparStatus(rs.getString("disk_file_name_par_status"));
                parStatusBean.setParPeriodForAppraisee(CommonFunctions.getFormattedOutputDate1(rs.getDate("par_period_for_appraisee")));
                parStatusBean.setParPeriodForReporting(CommonFunctions.getFormattedOutputDate1(rs.getDate("par_period_for_reporting")));
                if (rs.getString("is_closed_for_appraisee") != null && rs.getString("is_closed_for_appraisee").equals("closeforapp")) {
                    parStatusBean.setIsClosedForAppraisee("Closed");
                } else {
                    parStatusBean.setIsClosedForAppraisee("Open");
                }
                if (rs.getString("is_closed_for_authority") != null && rs.getString("is_closed_for_authority").equals("closeforauth")) {
                    parStatusBean.setIsClosedForAuthority("Closed");
                } else {
                    parStatusBean.setIsClosedForAuthority("Open");
                }
                parStatusBean.setParPeriodForReviewing(CommonFunctions.getFormattedOutputDate1(rs.getDate("par_period_for_reviewing")));
                parStatusBean.setParPeriodForAccepting(CommonFunctions.getFormattedOutputDate1(rs.getDate("par_period_for_accepting")));

                parStatusDetail.add(parStatusBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parStatusDetail;
    }

    public ArrayList getPARStatusDetailListForMessageCommunication(String fiscalyear) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList parStatusForMessageCommunication = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select * from par_status_log where financial_year=?");
            pst.setString(1, fiscalyear);
            rs = pst.executeQuery();
            while (rs.next()) {
                ParStatusBean parStatusBean = new ParStatusBean();
                parStatusBean.setLogId(rs.getInt("log_id"));
                parStatusBean.setFiscalyear(rs.getString("financial_year"));
                parStatusBean.setParStatusChangeOnDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("change_on_date")));
                parStatusBean.setOriginalFileNameForparStatus(rs.getString("original_file_name_par_status"));
                parStatusBean.setDiskfileNameForparStatus(rs.getString("disk_file_name_par_status"));
                parStatusBean.setParPeriodForAppraisee(CommonFunctions.getFormattedOutputDate1(rs.getDate("par_period_for_appraisee")));
                parStatusBean.setParPeriodForReporting(CommonFunctions.getFormattedOutputDate1(rs.getDate("par_period_for_reporting")));
                if (rs.getString("is_closed_for_appraisee") != null && rs.getString("is_closed_for_appraisee").equals("closeforapp")) {
                    parStatusBean.setIsClosedForAppraisee("Closed");
                } else {
                    parStatusBean.setIsClosedForAppraisee("Open");
                }
                if (rs.getString("is_closed_for_authority") != null && rs.getString("is_closed_for_authority").equals("closeforauth")) {
                    parStatusBean.setIsClosedForAuthority("Closed");
                } else {
                    parStatusBean.setIsClosedForAuthority("Open");
                }
                parStatusBean.setParPeriodForReviewing(CommonFunctions.getFormattedOutputDate1(rs.getDate("par_period_for_reviewing")));
                parStatusBean.setParPeriodForAccepting(CommonFunctions.getFormattedOutputDate1(rs.getDate("par_period_for_accepting")));

                parStatusForMessageCommunication.add(parStatusBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parStatusForMessageCommunication;
    }

    public ParStatusBean getAttachedFileforChangePARStatus(int logId) {
        ParStatusBean parStatusBean = new ParStatusBean();
        Connection con = null;
        Statement st = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select original_file_name_par_status,disk_file_name_par_status,file_type_par_status,file_path_par_status from par_status_log where log_id=?");
            pst.setInt(1, logId);
            rs = pst.executeQuery();
            String filepath = null;
            if (rs.next()) {
                parStatusBean.setOriginalFileNameForparStatus(rs.getString("original_file_name_par_status"));
                parStatusBean.setDiskfileNameForparStatus(rs.getString("disk_file_name_par_status"));
                parStatusBean.setFileTypeForparStatus("file_type_par_status");
                filepath = rs.getString("file_path_par_status");
            }
            File f = new File(filepath + File.separator + parStatusBean.getDiskfileNameForparStatus());
            parStatusBean.setFilecontent(FileUtils.readFileToByteArray(f));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return parStatusBean;
    }

    public ArrayList getAppraiseDetailListForMessageCommunication(String fiscalYear) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList appraiseList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select  emp_mast.EMP_ID,emp_mast.CADRE_CODE,mobile,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,CUR_SPC,PARID,par_status,post from emp_mast "
                    + "LEFT OUTER JOIN PAR_MASTER ON emp_mast.EMP_ID = PAR_MASTER.emp_id "
                    + "LEFT OUTER JOIN G_SPC on emp_mast.CUR_SPC=G_SPC.SPC "
                    + "LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "where  (POST_GRP_TYPE = 'A' or POST_GRP_TYPE = 'B')  AND (PARID IS NULL or par_status =0) "
                    + "and fiscal_year=? AND emp_mast.CADRE_CODE <> '1101' AND (emp_mast.EMP_ID= '59003752' OR emp_mast.EMP_ID='70000346')");
            pst.setString(1, fiscalYear);
            rs = pst.executeQuery();
            while (rs.next()) {
                PARMessageCommunication parMessageCommunication = new PARMessageCommunication();
                parMessageCommunication.setMobileNumber(rs.getString("mobile"));
                parMessageCommunication.setParId(rs.getInt("PARID"));
                parMessageCommunication.setParStatus(rs.getInt("par_status"));
                parMessageCommunication.setAppraiseName(rs.getString("EMPNAME"));
                parMessageCommunication.setAppraiseSpc(rs.getString("CUR_SPC"));
                parMessageCommunication.setAppraisePost(rs.getString("post"));
                appraiseList.add(parMessageCommunication);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return appraiseList;
    }

    @Override
    public void saveSendMessageCommunicationToAppraisee(PARMessageCommunication parMessageCommunication) {
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        String appraiseEmpid = "";
        String appraiseSpc = "";
        String startTime = "";
        String mobile = "";
        try {
            con = dataSource.getConnection();
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());

            pst = con.prepareStatement("select emp_mast.emp_id,mobile,CUR_SPC from emp_mast "
                    + "LEFT OUTER JOIN PAR_MASTER ON emp_mast.EMP_ID = PAR_MASTER.emp_id "
                    + "where  (POST_GRP_TYPE = 'A' or POST_GRP_TYPE = 'B')  AND (PARID IS NULL or par_status =0) and fiscal_year=? AND emp_mast.CADRE_CODE <> '1101' AND (emp_mast.EMP_ID= '59003752' OR emp_mast.EMP_ID='70000346')");
            pst.setString(1, parMessageCommunication.getFiscalyear());
            rs = pst.executeQuery();
            while (rs.next()) {
                mobile = rs.getString("MOBILE");
                appraiseEmpid = rs.getString("emp_id");
                appraiseSpc = rs.getString("CUR_SPC");

                String eol = System.getProperty("line.separator");
                String msg = " Last Date for PAR submission for (parMessageCommunication.getFiscalyear()) to reporting Officer is {$var}. It appears your PAR is not yet submitted. Please submit PAR by due date. HRMS Team ";

                if (mobile != null && !mobile.equals("")) {
                    pst = con.prepareStatement("INSERT INTO SMS_LOG(MSG_ID,EMP_ID,MESSAGE_TEXT,MESSAGE_TYPE,MOBILE,SENT_ON) VALUES(?,?,?,?,?,?)");
                    pst.setInt(1, CommonFunctions.getMaxCode(con, "SMS_LOG", "MSG_ID"));
                    pst.setString(2, appraiseEmpid);
                    pst.setString(3, msg);
                    pst.setString(4, "PENDING_PAR_SUBMISSION_APPRAISE");
                    pst.setString(5, mobile);
                    pst.setTimestamp(6, new Timestamp(dateFormat.parse(startTime).getTime()));
                    pst.executeUpdate();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList<SMSGrievance> getMessageList() {
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        ArrayList<SMSGrievance> smss = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select * from SMS_LOG where MESSAGE_TYPE='PENDING_PAR_SUBMISSION_APPRAISE' and STATUS is null");
            rs = pst.executeQuery();

            while (rs.next()) {
                SMSGrievance smsG = new SMSGrievance();
                smsG.setMsgId(rs.getInt("MSG_ID"));
                smsG.setWho(rs.getString("MOBILE"));
                smsG.setWhat(rs.getString("MESSAGE_TEXT"));
                smss.add(smsG);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return smss;
    }

    public ArrayList getAssignPrivilegedListDistWiseForPolice() {
        Connection con = null;
        ArrayList districtwiseprivilegeList = new ArrayList();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,par_authority_admin.SPC,SPN,"
                    + "par_authority_admin.post_grp,par_authority_admin.OFF_CODE,off_en FROM par_authority_admin "
                    + " INNER JOIN G_SPC ON par_authority_admin.SPC = G_SPC.SPC "
                    + " LEFT OUTER JOIN EMP_MAST ON par_authority_admin.SPC = EMP_MAST.CUR_SPC "
                    + " INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + " LEFT OUTER JOIN g_office ON par_authority_admin.off_code = g_office.off_code "
                    + " WHERE authorization_type='S' and par_for is null");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                DistrictWiseAuthorizationForPolice districtWiseAuthorizationForPolice = new DistrictWiseAuthorizationForPolice();
                districtWiseAuthorizationForPolice.setAuthHrmsId(rs.getString("emp_id"));
                districtWiseAuthorizationForPolice.setPriviligeAuhName(rs.getString("EMP_NAME"));
                districtWiseAuthorizationForPolice.setSpc(rs.getString("SPC"));
                districtWiseAuthorizationForPolice.setPriviligeAuhDesignation(rs.getString("SPN"));
                districtWiseAuthorizationForPolice.setPostGrp(rs.getString("post_grp"));
                districtWiseAuthorizationForPolice.setOfficeName(rs.getString("off_en"));
                districtwiseprivilegeList.add(districtWiseAuthorizationForPolice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return districtwiseprivilegeList;
    }

    public void deletePrivilegedListDistWiseForPolice(String spc, String postgrp) {
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            String spc1 = spc;
            pst = con.prepareStatement("DELETE FROM  par_authority_admin WHERE SPC=? and post_grp=? and par_for is null");
            pst.setString(1, spc);
            pst.setString(2, postgrp);
            pst.executeUpdate();

            pst1 = con.prepareStatement("DELETE FROM G_PRIVILEGE_MAP WHERE SPC=? AND role_id = '16'");
            pst1.setString(1, spc1);
            pst1.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public ArrayList getPendingPARReportByAdmin(ParAdminSearchCriteria parAdminSearchCriteria) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String fiscalYear = parAdminSearchCriteria.getFiscalyear();
        String searchCriteria = parAdminSearchCriteria.getSearchCriteria();
        String searchString = parAdminSearchCriteria.getSearchString();
        ArrayList pendingPARReportList = new ArrayList();
        try {
            con = dataSource.getConnection();
            if ((searchCriteria == null || searchCriteria.equals(""))) {
                pst = con.prepareStatement("select initiated_by,initiated_on,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,cur_spc,post,par_status,status_name from task_master "
                        + "inner join emp_mast on task_master.initiated_by=emp_mast.emp_id "
                        + "inner join par_master on task_master.task_id=par_master.task_id "
                        + "inner join G_SPC ON task_master.initiated_spc = G_SPC.SPC "
                        + "inner join G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                        + "inner join g_process_status ON par_master.par_status = g_process_status.STATUS_ID "
                        + "where task_master.process_id=3  and fiscal_year=?");
                pst.setString(1, fiscalYear);
            } else if (searchCriteria.equals("empid")) {
                pst = con.prepareStatement("select initiated_by,initiated_on,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,cur_spc,post,par_status,status_name from task_master "
                        + "inner join emp_mast on task_master.initiated_by=emp_mast.emp_id "
                        + "inner join par_master on task_master.task_id=par_master.task_id "
                        + "inner join G_SPC ON task_master.initiated_spc = G_SPC.SPC "
                        + "inner join G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                        + "inner join g_process_status ON par_master.par_status = g_process_status.STATUS_ID "
                        + "where task_master.process_id=3  and fiscal_year=? AND pending_at=?");
                pst.setString(1, fiscalYear);
                pst.setString(2, searchString);
            } else if (searchCriteria.equals("gpfno")) {
                pst = con.prepareStatement("select initiated_by,initiated_on,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,cur_spc,post,par_status,status_name from task_master "
                        + "inner join emp_mast on task_master.initiated_by=emp_mast.emp_id "
                        + "inner join par_master on task_master.task_id=par_master.task_id "
                        + "inner join G_SPC ON task_master.initiated_spc = G_SPC.SPC "
                        + "inner join G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                        + "inner join g_process_status ON par_master.par_status = g_process_status.STATUS_ID "
                        + "where task_master.process_id=3  and fiscal_year=? AND pending_at=?");
                pst.setString(1, fiscalYear);
                pst.setString(2, searchString);
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                PARMessageCommunication parMessageCommunication = new PARMessageCommunication();
                parMessageCommunication.setEmpId(rs.getString("initiated_by"));
                parMessageCommunication.setParCreatedOn(CommonFunctions.getFormattedOutputDate1(rs.getDate("initiated_on")));
                parMessageCommunication.setAppraiseName(rs.getString("EMPNAME"));
                parMessageCommunication.setAppraiseSpc(rs.getString("cur_spc"));
                parMessageCommunication.setAppraisePost(rs.getString("post"));
                parMessageCommunication.setParStatus(rs.getInt("par_status"));
                parMessageCommunication.setStatusName(rs.getString("status_name"));
                pendingPARReportList.add(parMessageCommunication);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return pendingPARReportList;
    }

    public String hasPARCreated(String empid, String fiscalYear) {
        //boolean parCreated = false;
        String msg = "Y";
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select extract(epoch from period_to-period_from)/86400 as noofdays from par_master where emp_id =? and fiscal_year =?");
            pst.setString(1, empid);
            pst.setString(2, fiscalYear);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("noofdays") > 120) {
                    msg = "N";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return msg;
    }

    @Override
    public void updateReviewingCompletedPAR() {
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        PreparedStatement pst3 = null;
        Connection con = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ArrayList reviewingCompletedPARList = new ArrayList();
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("select * from(select * from(select par_id,par_master.task_id,count(*) as cnt from par_master "
                    + "inner join par_reviewing_tran on par_reviewing_tran.par_id=par_master.parid "
                    + "where FISCAL_YEAR='2021-22' and par_status=7 group by par_id,par_master.task_id) t1 where cnt = 1) t3 "
                    + "inner join "
                    + "(select * from(select par_id,par_master.task_id,count(*) as cnt from par_master "
                    + "inner join par_reviewing_tran on par_reviewing_tran.par_id=par_master.parid "
                    + "where FISCAL_YEAR='2021-22' and par_status=7 and is_completed='Y' group by par_id,par_master.task_id) t2) t4 "
                    + "on t3.par_id = t4.par_id;");
            rs = pst.executeQuery();
            while (rs.next()) {
                ParApplyForm parApplyForm = new ParApplyForm();
                parApplyForm.setParId(rs.getInt("par_id"));
                parApplyForm.setTaskId(rs.getInt("task_id"));
                reviewingCompletedPARList.add(parApplyForm);
            }

            pst1 = con.prepareStatement("select pactid,accepting_emp_id,accepting_cur_spc from par_accepting_tran where par_id=?");
            pst1.setInt(1, rs.getInt("par_id"));
            rs1 = pst1.executeQuery();
            while (rs1.next()) {
                ParApplyForm parApplyForm = new ParApplyForm();
                parApplyForm.setPactid(rs1.getInt("pactid"));
                parApplyForm.setAcceptingempid(rs1.getString("accepting_emp_id"));
                parApplyForm.setAcceptingSpc(rs1.getString("accepting_cur_spc"));
                reviewingCompletedPARList.add(parApplyForm);
            }

            pst2 = con.prepareStatement("update par_master set ref_id_of_table=? and par_status=8 where parid=?");
            pst2.setInt(1, rs1.getInt("pactid"));
            pst2.setInt(2, rs.getInt("par_id"));
            pst2.executeUpdate();

            pst3 = con.prepareStatement("update task_master set status_id=8 and pending_at=? and apply_to=? and pending_spc=? where task_id=?");
            pst3.setString(1, rs1.getString("accepting_emp_id"));
            pst3.setString(2, rs1.getString("accepting_emp_id"));
            pst3.setString(3, rs1.getString("accepting_cur_spc"));
            pst3.setInt(4, rs.getInt("task_id"));
            pst3.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, rs1);
            DataBaseFunctions.closeSqlObjects(pst, pst1, pst2, pst3);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateMoreThenOneReviewingCompletedPAR() {
        PreparedStatement pst = null;
        PreparedStatement pst2 = null;
        PreparedStatement pst3 = null;
        Connection con = null;
        ResultSet rs = null;
        ResultSet rs1 = null;

        ArrayList<ParApplyForm> morereviewingCompletedPARList = new ArrayList();
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("select * from(select * from(select par_id,par_master.task_id,count(*) as cnt from par_master "
                    + "inner join par_reviewing_tran on par_reviewing_tran.par_id=par_master.parid "
                    + "where FISCAL_YEAR='2021-22' and par_status=7 group by par_id,par_master.task_id) t1 where cnt > 1) t3 "
                    + "inner join "
                    + "(select * from(select par_id,par_master.task_id,count(*) as cnt1 from par_master "
                    + "inner join par_reviewing_tran on par_reviewing_tran.par_id=par_master.parid "
                    + "where FISCAL_YEAR='2021-22' and par_status=7 and is_completed='Y' group by par_id,par_master.task_id,hierarchy_no order by hierarchy_no) t2 where cnt1 >= 1) t4 "
                    + "on t3.par_id = t4.par_id;");
            rs = pst.executeQuery();
            while (rs.next()) {
                ParApplyForm parApplyForm = new ParApplyForm();
                parApplyForm.setParId(rs.getInt("par_id"));
                parApplyForm.setTaskId(rs.getInt("task_id"));
                morereviewingCompletedPARList.add(parApplyForm);
            }
            pst = con.prepareStatement("select * from par_reviewing_tran where par_id= ? and is_completed is null  order by hierarchy_no asc");
            for (ParApplyForm parApplyForm : morereviewingCompletedPARList) {
                pst.setInt(1, parApplyForm.getParId());
                rs = pst.executeQuery();
                if (rs.next()) {
                    int prvtid = rs.getInt("prvtid");
                    String reviewingempId = rs.getString("reviewing_emp_id");
                    String reviewingcurSpc = rs.getString("reviewing_cur_spc");
                    updateReviewingTran(prvtid, reviewingempId, reviewingcurSpc, parApplyForm.getParId(), parApplyForm.getTaskId());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, rs1);
            DataBaseFunctions.closeSqlObjects(pst, pst2, pst3);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    public void updateReviewingTran(int prvtid, String reviewingempId, String reviewingcurSpc, int parID, int taskId) {
        PreparedStatement pst = null;
        PreparedStatement pst2 = null;
        PreparedStatement pst3 = null;
        Connection con = null;
        try {
            pst2 = con.prepareStatement("update par_master set ref_id_of_table=? where parid=?");
            pst2.setInt(1, prvtid);
            pst2.setInt(2, parID);
            pst2.executeUpdate();

            pst3 = con.prepareStatement("update task_master set pending_at=? and apply_to=? and pending_spc=? where task_id=?");
            pst3.setString(1, reviewingempId);
            pst3.setString(2, reviewingempId);
            pst3.setString(3, reviewingcurSpc);
            pst3.setInt(4, taskId);
            pst3.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pst2, pst3);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void updateReviewingTranNrcCase(int prvtid, String reviewingempId, String reviewingcurSpc, int parID, int taskId) {
        PreparedStatement pst = null;
        PreparedStatement pst2 = null;
        PreparedStatement pst3 = null;
        Connection con = null;
        try {
            pst2 = con.prepareStatement("update par_master set ref_id_of_table=? where parid=?");
            pst2.setInt(1, prvtid);
            pst2.setInt(2, parID);
            pst2.executeUpdate();

            pst3 = con.prepareStatement("update task_master set pending_at=? and apply_to=? and pending_spc=? where task_id=?");
            pst3.setString(1, reviewingempId);
            pst3.setString(2, reviewingempId);
            pst3.setString(3, reviewingcurSpc);
            pst3.setInt(4, taskId);
            pst3.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pst2, pst3);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void updateMoreThenOneReviewingCompletedNRCCasePAR() {
        PreparedStatement pst = null;
        PreparedStatement pst2 = null;
        PreparedStatement pst3 = null;
        Connection con = null;
        ResultSet rs = null;
        ResultSet rs1 = null;

        ArrayList<ParApplyForm> morereviewingCompletedNRCPARList = new ArrayList();
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("select distinct parid,par_master.task_id from par_master \n"
                    + "inner join par_reviewing_tran on par_master.parid = par_reviewing_tran.par_id\n"
                    + "inner join task_master on par_master.task_id = task_master.task_id\n"
                    + "where FISCAL_YEAR='2021-22' and par_status=7 and par_reviewing_tran.is_completed is null and (todate::DATE - fromdate::DATE) < 120\n"
                    + "and task_master.pending_at = par_reviewing_tran.reviewing_emp_id");
            rs = pst.executeQuery();
            while (rs.next()) {
                ParApplyForm parApplyForm = new ParApplyForm();
                parApplyForm.setParId(rs.getInt("parid"));
                parApplyForm.setTaskId(rs.getInt("task_id"));
                morereviewingCompletedNRCPARList.add(parApplyForm);
            }
            pst = con.prepareStatement("select * from par_reviewing_tran where par_id= ? and is_completed is null and (todate::DATE - fromdate::DATE) > 120  order by hierarchy_no asc");
            for (ParApplyForm parApplyForm : morereviewingCompletedNRCPARList) {
                pst.setInt(1, parApplyForm.getParId());
                rs = pst.executeQuery();
                if (rs.next()) {
                    int prvtid = rs.getInt("prvtid");
                    String reviewingempId = rs.getString("reviewing_emp_id");
                    String reviewingcurSpc = rs.getString("reviewing_cur_spc");
                    updateReviewingTranNrcCase(prvtid, reviewingempId, reviewingcurSpc, parApplyForm.getParId(), parApplyForm.getTaskId());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, rs1);
            DataBaseFunctions.closeSqlObjects(pst, pst2, pst3);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Autowired
    public void downloadExcelForPARReportAtViewer(WritableWorkbook workbook, String privilegedSpc, String cadrecode, String searchCriteria, String searchString, String fiscalyear, String searchParStatus) {
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
            Label label = new Label(0, row, " Employee List ", headcell);//column,row
            sheet.setRowView(row, 30 * 30);
            sheet.addCell(label);
            sheet.mergeCells(0, row, 25, row);

            row = row + 1;

            sheet.mergeCells(0, row, 0, row + 2);
            label = new Label(0, row, "SL No", innerheadcell);
            sheet.setRowView(row, 40 * 30);
            sheet.addCell(label);

            sheet.mergeCells(1, row, 1, row + 2);
            label = new Label(1, row, "Employee ID", innerheadcell);
            sheet.setColumnView(1, 25);
            sheet.addCell(label);

            sheet.mergeCells(2, row, 2, row + 2);
            label = new Label(2, row, "GPF No ", innerheadcell);
            sheet.setColumnView(2, 14);
            sheet.addCell(label);

            sheet.mergeCells(3, row, 3, row + 2);
            label = new Label(3, row, "Employee Name", innerheadcell);
            sheet.setColumnView(3, 14);
            sheet.addCell(label);

            sheet.mergeCells(4, row, 4, row + 2);
            label = new Label(4, row, "Designation", innerheadcell);
            sheet.setColumnView(4, 14);
            sheet.addCell(label);

            sheet.mergeCells(5, row, 5, row + 2);
            label = new Label(5, row, "Date Of Birth", innerheadcell);
            sheet.setColumnView(5, 14);
            sheet.addCell(label);

            sheet.mergeCells(6, row, 6, row + 2);
            label = new Label(6, row, "Group", innerheadcell);
            sheet.setColumnView(6, 14);
            sheet.addCell(label);

            sheet.mergeCells(7, row, 7, row + 2);
            label = new Label(7, row, "Cadre ", innerheadcell);
            sheet.setColumnView(7, 14);
            sheet.addCell(label);

            sheet.mergeCells(8, row, 8, row + 2);
            label = new Label(8, row, "Current Office", innerheadcell);
            sheet.setColumnView(7, 14);
            sheet.addCell(label);

            sheet.mergeCells(9, row, 9, row + 2);
            label = new Label(9, row, "Par Period From ", innerheadcell);
            sheet.setColumnView(9, 14);
            sheet.addCell(label);

            sheet.mergeCells(10, row, 10, row + 2);
            label = new Label(10, row, "Par Period To ", innerheadcell);
            sheet.setColumnView(10, 14);
            sheet.addCell(label);

            sheet.mergeCells(11, row, 11, row + 2);
            label = new Label(11, row, "Status", innerheadcell);
            sheet.setColumnView(11, 14);
            sheet.addCell(label);

            sheet.mergeCells(12, row, 12, row + 2);
            label = new Label(12, row, "Authority Name", innerheadcell);
            sheet.setColumnView(12, 14);
            sheet.addCell(label);

            sheet.mergeCells(13, row, 13, row + 2);
            label = new Label(13, row, "Authority Designation", innerheadcell);
            sheet.setColumnView(13, 14);
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

            if ((fiscalyear != null && !fiscalyear.equals("") && searchParStatus != null && !searchParStatus.equals("") && !searchParStatus.equals("117")) && (cadrecode == null || cadrecode.equals(""))) {
                pst = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate,parid,par_status,FISCAL_YEAR,GPF_NO,DOB,PAR_MASTER.EMP_ID,F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE, "
                        + "PAR_MASTER.CADRE_CODE,G_CADRE.CADRE_NAME,POST,POST_GROUP,PAR_MASTER.OFF_CODE,G_OFFICE.off_en,get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname, "
                        + "getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,period_from,period_to FROM EMP_MAST "
                        + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,period_from,period_to FROM "
                        + "PAR_MASTER PAR_MASTER WHERE FISCAL_YEAR=?  and (par_type is null or par_type = '')  and par_status=?)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON "
                        + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                        + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE");

                pst.setString(1, fiscalyear);
                pst.setInt(2, Integer.parseInt(searchParStatus));
                pst.setString(3, privilegedSpc);

            } else if (fiscalyear != null && !fiscalyear.equals("") && cadrecode != null && !cadrecode.equals("") && searchParStatus != null && !searchParStatus.equals("")) {
                pst = con.prepareStatement("SELECT FISCAL_YEAR,PARID,PAR_MASTER.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE,F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,PAR_MASTER.OFF_CODE,GPF_NO,DOB,off_en, "
                        + "get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname,getspn(TASK_MASTER.pending_spc)as pendingauthorityspc,PENDING_AT,period_from,period_to FROM "
                        + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
                        + "INNER JOIN (SELECT FISCAL_YEAR,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,TASK_ID,period_from,period_to FROM PAR_MASTER WHERE FISCAL_YEAR=? and par_status=? and cadre_code=?)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin ON par_authority_admin.OFF_CODE = EMP_MAST.CUR_OFF_CODE "
                        + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID"
                        + "LEFT OUTER JOIN G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE");
                pst.setString(1, fiscalyear);
                pst.setInt(2, Integer.parseInt(searchParStatus));
                pst.setString(3, cadrecode);
                pst.setString(4, privilegedSpc);
            } else if ((fiscalyear != null && !fiscalyear.equals("") && searchParStatus != null && !searchParStatus.equals("") && searchParStatus.equals("117")) && (cadrecode == null || cadrecode.equals(""))) {
                pst = con.prepareStatement("select empmastid as emp_id,null as off_code,G_POST.post,CADRE_NAME,off_en,null as pendingparDate,null as parid,null as par_status,FISCAL_YEAR,GPF_NO,DOB, "
                        + "F_NAME,M_NAME,L_NAME,MOBILE,post_grp_type as post_group,null as pendingauthorityname, "
                        + "null as  pendingauthorityspc,null as PENDING_AT,null as period_from,null as period_to,* from "
                        + "(select emp_mast.emp_id as empmastid,par_master.parid,par_master.emp_id as parmastid,cur_off_code,cur_SPC,cadre_code,FISCAL_YEAR,GPF_NO,DOB, "
                        + "F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE,post_grp_type from "
                        + "(select emp_id,f_name,M_NAME,L_NAME,EMP_MAST.MOBILE,cur_off_code,cur_SPC,CADRE_CODE,GPF_NO,DOB,post_grp_type from emp_mast where  dep_code ='02' and post_grp_type in ('A','B')) as emp_mast "
                        + "left outer join "
                        + "(select emp_id,parid,FISCAL_YEAR from par_master where fiscal_year=?) as par_master "
                        + "on emp_mast.emp_id = par_master.emp_id)as t1 "
                        + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=?)par_authority_admin "
                        + "ON par_authority_admin.OFF_CODE = t1.cur_off_code "
                        + "LEFT OUTER JOIN hrmis2.G_SPC ON t1.cur_SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN (select * from G_POST)as G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN (select * from g_cadre) G_CADRE ON t1.cadre_code=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_OFFICE ON t1.CUR_OFF_CODE=G_OFFICE.OFF_CODE "
                        + "where parmastid is null");
                pst.setString(1, fiscalyear);
                pst.setString(2, privilegedSpc);
            }

            int i = 0;
            rs = pst.executeQuery();

            while (rs.next()) {

                i++;
                row = row + 1;
                label = new Label(0, row, i + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(1, row, rs.getInt("emp_id") + "", innerheadcell);
                sheet.addCell(label);

                label = new Label(2, row, rs.getString("GPF_NO"), innerheadcell);
                sheet.addCell(label);

                label = new Label(3, row, rs.getString("f_name"), innerheadcell);
                sheet.addCell(label);

                label = new Label(4, row, rs.getString("post"), innerheadcell);
                sheet.addCell(label);

                label = new Label(5, row, rs.getString("DOB"), innerheadcell);
                sheet.addCell(label);

                label = new Label(6, row, rs.getString("post_group"), innerheadcell);
                sheet.addCell(label);

                label = new Label(7, row, rs.getString("CADRE_NAME"), innerheadcell);
                sheet.addCell(label);

                label = new Label(8, row, rs.getString("off_en"), innerheadcell);
                sheet.addCell(label);

                label = new Label(9, row, rs.getString("period_from"), innerheadcell);
                sheet.addCell(label);

                label = new Label(10, row, rs.getString("period_to"), innerheadcell);
                sheet.addCell(label);

                label = new Label(11, row, rs.getString("PAR_STATUS"), innerheadcell);
                sheet.addCell(label);

                label = new Label(12, row, rs.getString("pendingauthorityname"), innerheadcell);
                sheet.addCell(label);

                label = new Label(13, row, rs.getString("pendingauthorityspc"), innerheadcell);
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
    public ArrayList getPARCadreChangeDetailList(String fiscalYear) {
        Connection con = null;
        ArrayList cadreChangeparlist = new ArrayList();
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {
            con = dataSource.getConnection();

            String IP = CommonFunctions.getISPIPAddress();

            pstmt = con.prepareStatement("select fiscal_year,cadre_updated_by,par_master.cadre_code,g_cadre.cadre_name,par_master.cadre_updated_byadmin_ondate,par_master.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME, "
                    + "cadre_updated_by_ip,post,gpf_no,mobile,post_group from "
                    + "par_master "
                    + "inner join emp_mast on par_master.emp_id = emp_mast.emp_id "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "LEFT OUTER JOIN G_CADRE  ON par_master.CADRE_CODE = G_CADRE.CADRE_CODE "
                    + "where par_master.cadre_updated_by is not null and fiscal_year=? "
                    + "group by cadre_updated_by,par_master.cadre_code,cadre_updated_byadmin_ondate,par_master.emp_id, "
                    + "fiscal_year,EMPNAME,post,g_cadre.cadre_name,gpf_no,mobile,par_master.cadre_updated_by_ip,post_group order by cadre_updated_byadmin_ondate desc");
            pstmt.setString(1, fiscalYear);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ParAdminProperties parAdminProperties = new ParAdminProperties();
                parAdminProperties.setFiscalyear(rs.getString("fiscal_year"));
                parAdminProperties.setCadreUpdatedBy(rs.getString("cadre_updated_by"));
                parAdminProperties.setCadrecode(rs.getString("cadre_code"));
                parAdminProperties.setCadreName(rs.getString("cadre_name"));
                parAdminProperties.setPostGroupType(rs.getString("post_group"));
                parAdminProperties.setEmpName(rs.getString("EMPNAME"));
                parAdminProperties.setEmpId(rs.getString("emp_id"));
                parAdminProperties.setPostName(rs.getString("post"));
                parAdminProperties.setGpfno(rs.getString("gpf_no"));
                parAdminProperties.setMobile(rs.getString("mobile"));
                parAdminProperties.setCadreUpdatedByAdminOnDate(rs.getString("cadre_updated_byadmin_ondate"));
                if (rs.getString("cadre_updated_by_ip") != null && !rs.getString("cadre_updated_by_ip").equals("")) {
                    parAdminProperties.setCadreUpdatedByIp(rs.getString("cadre_updated_by_ip"));
                } else {
                    parAdminProperties.setCadreUpdatedByIp("Previously Not Available");
                }
                cadreChangeparlist.add(parAdminProperties);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return cadreChangeparlist;
    }

    @Override
    public List getOfficeWiseParListForCustodian(String fiscalYear, String custodianOffCode, String custodianDistCode, String searchCriteria, String searchString) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet res = null;
        List parList = new ArrayList();

        try {
            con = dataSource.getConnection();
            /*Search only by fiscal year*/
            if ((searchCriteria == null || searchCriteria.equals(""))) {
                pst = con.prepareStatement("select parid,FISCAL_YEAR,PM.EMP_ID,F_NAME,M_NAME,L_NAME,EM.MOBILE,GPF_NO,DOB,PM.CADRE_CODE,G_CADRE.CADRE_NAME,"
                        + "POST,POST_GROUP,PM.OFF_CODE,G_OFFICE.off_en,place_of_posting,nrcreason,PM.task_id,par_status from par_master PM "
                        + "LEFT OUTER JOIN EMP_MAST EM ON EM.EMP_ID = PM.EMP_ID "
                        + "LEFT OUTER JOIN G_CADRE ON PM.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PM.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN TASK_MASTER ON PM.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN G_OFFICE ON PM.OFF_CODE=G_OFFICE.OFF_CODE "
                        + "where fiscal_year = ? and PM.off_code = ? order by f_name");
                pst.setString(1, fiscalYear);
                pst.setString(2, custodianOffCode);

            } else if (searchCriteria.equals("empid")) {
                pst = con.prepareStatement("select parid,FISCAL_YEAR,PM.EMP_ID,F_NAME,M_NAME,L_NAME,EM.MOBILE,GPF_NO,DOB,PM.CADRE_CODE,G_CADRE.CADRE_NAME,"
                        + "POST,POST_GROUP,PM.OFF_CODE,G_OFFICE.off_en,place_of_posting,nrcreason,PM.task_id,par_status from par_master PM "
                        + "LEFT OUTER JOIN EMP_MAST EM ON EM.EMP_ID = PM.EMP_ID "
                        + "LEFT OUTER JOIN G_CADRE ON PM.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PM.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN TASK_MASTER ON PM.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN G_OFFICE ON PM.OFF_CODE=G_OFFICE.OFF_CODE "
                        + "where fiscal_year = ? and PM.off_code = ? and PM.emp_id=?");

                pst.setString(1, fiscalYear);
                pst.setString(2, custodianOffCode);
                pst.setString(3, searchString);
            } else if (searchCriteria.equals("gpfno")) {
                pst = con.prepareStatement("select parid,FISCAL_YEAR,PM.EMP_ID,F_NAME,M_NAME,L_NAME,EM.MOBILE,GPF_NO,DOB,PM.CADRE_CODE,G_CADRE.CADRE_NAME,"
                        + "POST,POST_GROUP,PM.OFF_CODE,G_OFFICE.off_en,place_of_posting,nrcreason,PM.task_id,par_status from par_master PM "
                        + "LEFT OUTER JOIN EMP_MAST EM ON EM.EMP_ID = PM.EMP_ID "
                        + "LEFT OUTER JOIN G_CADRE ON PM.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PM.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN TASK_MASTER ON PM.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN G_OFFICE ON PM.OFF_CODE=G_OFFICE.OFF_CODE "
                        + "where fiscal_year = ? and PM.off_code = ? and EM.gpf_no=?");

                pst.setString(1, fiscalYear);
                pst.setString(2, custodianOffCode);
                pst.setString(3, searchString);
            } else if (searchCriteria.equals("empname")) {
                pst = con.prepareStatement("select parid,FISCAL_YEAR,PM.EMP_ID,F_NAME,M_NAME,L_NAME,EM.MOBILE,GPF_NO,DOB,PM.CADRE_CODE,G_CADRE.CADRE_NAME,"
                        + "POST,POST_GROUP,PM.OFF_CODE,G_OFFICE.off_en,place_of_posting,nrcreason,PM.task_id,par_status from par_master PM "
                        + "LEFT OUTER JOIN EMP_MAST EM ON EM.EMP_ID = PM.EMP_ID "
                        + "LEFT OUTER JOIN G_CADRE ON PM.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PM.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN TASK_MASTER ON PM.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN G_OFFICE ON PM.OFF_CODE=G_OFFICE.OFF_CODE "
                        + "where fiscal_year = ? and PM.off_code = ? and EM.F_NAME=? order by f_name");

                pst.setString(1, fiscalYear);
                pst.setString(2, custodianOffCode);
                pst.setString(3, searchString);
            } else if (searchCriteria.equals("lastname")) {
                pst = con.prepareStatement("select parid,FISCAL_YEAR,PM.EMP_ID,F_NAME,M_NAME,L_NAME,EM.MOBILE,GPF_NO,DOB,PM.CADRE_CODE,G_CADRE.CADRE_NAME,"
                        + "POST,POST_GROUP,PM.OFF_CODE,G_OFFICE.off_en,place_of_posting,nrcreason,PM.task_id,par_status from par_master PM "
                        + "LEFT OUTER JOIN EMP_MAST EM ON EM.EMP_ID = PM.EMP_ID "
                        + "LEFT OUTER JOIN G_CADRE ON PM.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PM.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN TASK_MASTER ON PM.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN G_OFFICE ON PM.OFF_CODE=G_OFFICE.OFF_CODE "
                        + "where fiscal_year = ? and PM.off_code = ? and EM.L_NAME=? order by f_name");

                pst.setString(1, fiscalYear);
                pst.setString(2, custodianOffCode);
                pst.setString(3, searchString);
            } else if (searchCriteria.equals("mobile")) {
                pst = con.prepareStatement("select parid,FISCAL_YEAR,PM.EMP_ID,F_NAME,M_NAME,L_NAME,EM.MOBILE,GPF_NO,DOB,PM.CADRE_CODE,G_CADRE.CADRE_NAME,"
                        + "POST,POST_GROUP,PM.OFF_CODE,G_OFFICE.off_en,place_of_posting,nrcreason,PM.task_id,par_status from par_master PM "
                        + "LEFT OUTER JOIN EMP_MAST EM ON EM.EMP_ID = PM.EMP_ID "
                        + "LEFT OUTER JOIN G_CADRE ON PM.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PM.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN TASK_MASTER ON PM.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN G_OFFICE ON PM.OFF_CODE=G_OFFICE.OFF_CODE "
                        + "where fiscal_year = ? and PM.off_code = ? and EM.mobile= ?");

                pst.setString(1, fiscalYear);
                pst.setString(2, custodianOffCode);
                pst.setString(3, searchString);
            } else if (searchCriteria.equals("dob")) {
                pst = con.prepareStatement("select parid,FISCAL_YEAR,PM.EMP_ID,F_NAME,M_NAME,L_NAME,EM.MOBILE,GPF_NO,DOB,PM.CADRE_CODE,G_CADRE.CADRE_NAME,"
                        + "POST,POST_GROUP,PM.OFF_CODE,G_OFFICE.off_en,place_of_posting,nrcreason,PM.task_id,par_status from par_master PM "
                        + "LEFT OUTER JOIN EMP_MAST EM ON EM.EMP_ID = PM.EMP_ID "
                        + "LEFT OUTER JOIN G_CADRE ON PM.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PM.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN TASK_MASTER ON PM.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN G_OFFICE ON PM.OFF_CODE=G_OFFICE.OFF_CODE "
                        + "where fiscal_year = ? and PM.off_code = ? and EM.dob= to_date(?,'DD-MM-yyyy') order by EM.F_NAME");

                pst.setString(1, fiscalYear);
                pst.setString(2, custodianOffCode);
                pst.setString(3, searchString);
            }
            res = pst.executeQuery();

            int count = 0;
            while (res.next()) {
                count++;
                ParAdminProperties parAdminProperties = new ParAdminProperties();
                String parId = res.getString("parid");
                parAdminProperties.setEncParId(parId);
                String fyYear = res.getString("FISCAL_YEAR");
                parAdminProperties.setFiscalyear(res.getString("FISCAL_YEAR"));
                String empHrmsId = res.getString("EMP_ID");
                parAdminProperties.setEmpId(res.getString("EMP_ID"));
                String fname = "";
                String mname = "";
                String lname = "";

                if (res.getString("F_NAME") != null) {
                    fname = res.getString("F_NAME").trim();
                } else {
                    fname = "";
                }
                if (res.getString("M_NAME") != null) {
                    mname = res.getString("M_NAME").trim();
                } else {
                    mname = "";
                }
                if (res.getString("L_NAME") != null && !res.getString("L_NAME").equals("")) {
                    lname = res.getString("L_NAME").trim();
                } else {
                    lname = "";
                }
                parAdminProperties.setEmpName(fname + " " + mname + " " + lname);
                parAdminProperties.setMobile(StringUtils.defaultString(res.getString("MOBILE"), ""));
                parAdminProperties.setGpfno(res.getString("GPF_NO"));
                parAdminProperties.setDob(CommonFunctions.getFormattedOutputDate6(res.getDate("DOB")));

                parAdminProperties.setCadreName(res.getString("CADRE_NAME"));
                parAdminProperties.setPostName(StringUtils.defaultString(res.getString("POST"), ""));
                parAdminProperties.setGroupName(StringUtils.defaultString(res.getString("POST_GROUP"), ""));
                parAdminProperties.setCurrentoffice(res.getString("off_en"));
                if (res.getString("PAR_STATUS") != null && !res.getString("PAR_STATUS").equals("")) {
                    if (res.getInt("PAR_STATUS") == 0) {
                        parAdminProperties.setParstatus("PAR CREATED BUT NOT SUBMITTED");
                    } else {
                        parAdminProperties.setParstatus(getStatusName(con, res.getString("PAR_STATUS")));
                    }
                } else {
                    parAdminProperties.setParstatus("PAR NOT CREATED");
                }

                if (res.getString("nrcreason") != null && !res.getString("nrcreason").equals("")) {
                    String nrcVal = res.getString("nrcreason");
                    if (nrcVal.equals("01")) {
                        nrcVal = "Period is less than 4 month";
                    } else if (nrcVal.equals("02")) {
                        nrcVal = "Availed Commuted Leave";
                    } else if (nrcVal.equals("03")) {
                        nrcVal = "Extension of Joining Time";
                    } else if (nrcVal.equals("04")) {
                        nrcVal = "On Training";
                    } else if (nrcVal.equals("05")) {
                        nrcVal = "Under Suspension";
                    } else if (nrcVal.equals("06")) {
                        nrcVal = "Other Reasons";
                    }
                    parAdminProperties.setNrcDetails(nrcVal);
                }

                if (res.getString("place_of_posting") != null && !res.getString("place_of_posting").equals("")) {
                    parAdminProperties.setCurrentOfficeName(res.getString("place_of_posting"));
                } else {
                    parAdminProperties.setCurrentOfficeName("");
                }
                List innerDataList = getSiParDataDetails(empHrmsId, fyYear, parId);
                parAdminProperties.setSiParInnerDataList(innerDataList);
                parList.add(parAdminProperties);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parList;
    }

    @Override
    public List getofficeWiseParDetails(String hrmsId, String fiscalYear, String encParId) {
        Connection con = null;
        ArrayList pardetail = new ArrayList();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        //pkm;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate, "
                    + "get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname,"
                    + "getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,INITIALS,F_NAME,M_NAME,L_NAME,initiatedby,post_group,"
                    + "is_reviewed,IS_ADVERSED,adverse_comm_status_id,PARMAST.EMP_ID,PARID,FISCAL_YEAR,PERIOD_FROM,PERIOD_TO,PAR_STATUS,PARMAST.SPC,"
                    + "POST,TASK_MASTER.TASK_ID,g_process_status.STATUS_NAME,g_process_status.STATUS_ID,isNRCAttchPresent(parid) as hasAttachment,"
                    + "nrcreason,nrc_submitted_on FROM (SELECT initiatedby,post_group,is_reviewed,IS_ADVERSED,adverse_comm_status_id,PARID,EMP_ID,"
                    + "FISCAL_YEAR,PERIOD_FROM,PERIOD_TO,PAR_STATUS,SPC,CADRE_CODE,TASK_ID,nrcreason,nrc_submitted_on FROM PAR_MASTER WHERE "
                    + "EMP_ID=? AND FISCAL_YEAR=? and parid = ?)PARMAST "
                    + "INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = PARMAST.EMP_ID "
                    + "LEFT OUTER JOIN G_SPC ON G_SPC.SPC=PARMAST.SPC "
                    + "LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "LEFT OUTER JOIN (select status_name,process_id,status_id from g_process_status where process_id=3)g_process_status ON "
                    + "PARMAST.PAR_STATUS=g_process_status.STATUS_ID "
                    + "LEFT OUTER JOIN TASK_MASTER ON PARMAST.TASK_ID=TASK_MASTER.TASK_ID order by PARID asc");
            pstmt.setString(1, hrmsId);
            pstmt.setString(2, fiscalYear);
            pstmt.setInt(3, Integer.parseInt(encParId));
            rs = pstmt.executeQuery();
            int i = 0;
            if (rs.next()) {
                i++;
                ParAdminProperties pbf = new ParAdminProperties();
                pbf.setParslno(i);
                pbf.setInitiatedByEmpId(rs.getString("initiatedby"));
                pbf.setPostGroupType(rs.getString("post_group"));
                pbf.setFiscalyear(rs.getString("FISCAL_YEAR"));
                pbf.setEmpId(rs.getString("EMP_ID"));
                pbf.setEmpName(StringUtils.defaultString(rs.getString("INITIALS")) + " " + StringUtils.defaultString(rs.getString("F_NAME")) + " " + StringUtils.defaultString(rs.getString("M_NAME")) + " " + StringUtils.defaultString(rs.getString("L_NAME")));

                pbf.setSpc(rs.getString("SPC"));
                pbf.setParId(rs.getInt("PARID"));
                pbf.setEncParId(rs.getInt("PARID") + "");
                pbf.setPrdFrmDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_FROM")));
                pbf.setPrdToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_TO")));
                pbf.setPostName(rs.getString("POST"));
                if (rs.getString("pendingparDate") != null && !rs.getString("pendingparDate").equals("")) {
                    pbf.setParPendingDateFrom(CommonFunctions.getFormattedOutputDate6(rs.getDate("pendingparDate")));
                } else {
                    pbf.setParPendingDateFrom("");
                }
                if (rs.getString("pendingauthorityname") != null && !rs.getString("pendingauthorityname").equals("")) {
                    pbf.setPendingAtAuthName(rs.getString("pendingauthorityname"));
                } else {
                    pbf.setPendingAtAuthName("");
                }
                if (rs.getString("pendingauthorityspc") != null && !rs.getString("pendingauthorityspc").equals("")) {
                    pbf.setPendingAtSpc(rs.getString("pendingauthorityspc"));
                } else {
                    pbf.setPendingAtSpc("");
                }
                pbf.setPendingAtEmpId(rs.getString("PENDING_AT"));

                pbf.setAdverseCommunicationStatusId(rs.getString("adverse_comm_status_id"));

                if (rs.getString("STATUS_NAME") != null && !rs.getString("STATUS_NAME").equals("")) {
                    pbf.setParstatus(rs.getString("STATUS_NAME"));
                } else {
                    pbf.setParstatus("PENDING");
                }
                // athi task id encodeing haba
                if (rs.getString("TASK_ID") != null && !rs.getString("TASK_ID").equals("")) {
                    pbf.setTaskId(rs.getString("TASK_ID"));
                    pbf.setEncTaskId(rs.getString("TASK_ID"));
                } else {
                    pbf.setTaskId("");
                }
                if (rs.getString("is_reviewed") != null && rs.getString("is_reviewed").equals("Y")) {
                    pbf.setIsreview("Y");
                } else {
                    pbf.setIsreview("N");
                }
                if (rs.getString("STATUS_ID") != null && !rs.getString("STATUS_ID").equals("")) {
                    pbf.setParstatusid(rs.getString("STATUS_ID"));
                    if (rs.getInt("STATUS_ID") == 17) {
                        //pbf.setIsNRCAttchPresent(isNRCAttchPresent(con, rs.getString("PARID")));
                        String nrcVal = rs.getString("nrcreason");
                        if (nrcVal.equals("01")) {
                            nrcVal = "Period is less than 4 month";
                        } else if (nrcVal.equals("02")) {
                            nrcVal = "Availed Commuted Leave";
                        } else if (nrcVal.equals("03")) {
                            nrcVal = "Extension of Joining Time";
                        } else if (nrcVal.equals("04")) {
                            nrcVal = "On Training";
                        } else if (nrcVal.equals("05")) {
                            nrcVal = "Under Suspension";
                        } else if (nrcVal.equals("06")) {
                            nrcVal = "Other Reasons";
                        }
                        String nrcDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("nrc_submitted_on"));
                        pbf.setNrcDetails(nrcVal);
                    }
                } else {
                    pbf.setParstatusid("");
                }
                if (rs.getInt("STATUS_ID") > 5) {
                    pbf.setIseditable("N");
                } else {
                    pbf.setIseditable("Y");
                }
                if (rs.getString("IS_ADVERSED") != null && rs.getString("IS_ADVERSED").equals("Y")) {
                    pbf.setPar_master_status("PAR IS ADVERSED");
                    pbf.setIsadversed("Y");
                } else {
                    pbf.setPar_master_status(rs.getString("STATUS_NAME"));
                }
                pbf.setIsNRCAttchPresent(rs.getString("hasAttachment"));
                pardetail.add(pbf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return pardetail;
    }

    @Override
    public List getAssignedPrivilegeListDistWiseForPoliceSiPar() {
        Connection con = null;
        List distWisePrivilegeList = new ArrayList();
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,emp_id,PAA.SPC,SPN, "
                    + " PAA.post_grp,PAA.OFF_CODE,off_en,g_district.dist_code, g_district.dist_name distName FROM par_authority_admin PAA "
                    + " INNER JOIN G_SPC ON PAA.SPC = G_SPC.SPC "
                    + " LEFT OUTER JOIN EMP_MAST ON PAA.SPC = EMP_MAST.CUR_SPC "
                    + " INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + " LEFT OUTER JOIN g_office ON PAA.off_code = g_office.off_code "
                    + " LEFT OUTER JOIN hrmis2.g_district ON g_office.dist_code = g_district.dist_code "
                    + " WHERE PAA.par_for='SiPar' order by g_district.dist_name");//authorization_type='S' 
            rs = pstmt.executeQuery();
            while (rs.next()) {
                DistrictWiseAuthorizationForPolice districtWiseAuthorizationForPolice = new DistrictWiseAuthorizationForPolice();
                districtWiseAuthorizationForPolice.setPriviligeAuhName(rs.getString("EMP_NAME"));
                districtWiseAuthorizationForPolice.setAuthHrmsId(rs.getString("emp_id"));
                districtWiseAuthorizationForPolice.setSpc(rs.getString("SPC"));
                districtWiseAuthorizationForPolice.setPriviligeAuhDesignation(rs.getString("SPN"));
                districtWiseAuthorizationForPolice.setDistName(rs.getString("distName"));
                districtWiseAuthorizationForPolice.setPostGrp(rs.getString("post_grp"));
                districtWiseAuthorizationForPolice.setOfficeName(rs.getString("off_en"));

                distWisePrivilegeList.add(districtWiseAuthorizationForPolice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return distWisePrivilegeList;
    }

    @Override
    public String saveAssignOfficewisePrivilige4SiPar(ParApplyForm parApplyForm) {
        String msg = "Y";
        int priv_id = 0;
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            int recordFound = 0;
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT COUNT(*) AS CNT FROM PAR_AUTHORITY_ADMIN WHERE SPC=? AND "
                    + "off_code=? AND authorization_type=? AND POST_GRP = ? and par_for = 'SiPar'");
            pst.setString(1, parApplyForm.getSpc());
            pst.setString(2, parApplyForm.getOffcodeforofficewise());
            //pst.setString(3, parApplyForm.getAuthorizationType());
            pst.setString(3, "S");
            pst.setString(4, parApplyForm.getPostGroup());
            rs = pst.executeQuery();
            if (rs.next()) {
                recordFound = rs.getInt("CNT");
            }
            DataBaseFunctions.closeSqlObjects(pst);
            if (recordFound == 0) {
                pst = con.prepareStatement("INSERT INTO PAR_AUTHORITY_ADMIN(SPC,off_code,authorization_type,post_grp,par_for) VALUES(?,?,?,?,?)");
                pst.setString(1, parApplyForm.getSpc());
                pst.setString(2, parApplyForm.getOffcodeforofficewise());
                pst.setString(3, parApplyForm.getAuthorizationType());
                pst.setString(4, parApplyForm.getPostGroup());
                pst.setString(5, "SiPar");
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                priv_id = CommonFunctions.getMaxCodeInteger("G_PRIVILEGE_MAP", "PRIV_MAP_ID", con);
                pst = con.prepareStatement("INSERT INTO G_PRIVILEGE_MAP(PRIV_MAP_ID,SPC,ROLE_ID,MOD_GRP_ID,MOD_ID) VALUES(?,?,?,?,?)");
                pst.setInt(1, priv_id);
                pst.setString(2, parApplyForm.getSpc());
                pst.setInt(3, 20);
                pst.setString(4, "038");
                pst.setInt(5, 0);
                //pst.setInt(5, 214);//No valid module mapped for this mod_id
                pst.executeUpdate();
            } else {
                msg = "D";
            }
        } catch (Exception e) {
            msg = "N";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst, con);
        }
        return msg;
    }

    @Override
    public List getOfficeWiseAssignedPrivilegeList4SiPar(String offCode) {
        Connection con = null;
        ArrayList officewiseprivilegedList = new ArrayList();
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,PAR_AUTHORITY_ADMIN.SPC,"
                    + "SPN,PAR_AUTHORITY_ADMIN.POST_GRP FROM PAR_AUTHORITY_ADMIN "
                    + "INNER JOIN G_SPC ON PAR_AUTHORITY_ADMIN.SPC = G_SPC.SPC "
                    + "LEFT OUTER JOIN EMP_MAST ON PAR_AUTHORITY_ADMIN.SPC = EMP_MAST.CUR_SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "WHERE PAR_AUTHORITY_ADMIN.OFF_CODE = ? and par_for='SiPar'");
            pstmt.setString(1, offCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ParApplyForm parApplyForm = new ParApplyForm();
                parApplyForm.setEmpName(rs.getString("EMP_NAME"));
                parApplyForm.setSpc(rs.getString("SPC"));
                parApplyForm.setEmpDesg(rs.getString("SPN"));
                parApplyForm.setPostGroup(rs.getString("post_grp"));
                officewiseprivilegedList.add(parApplyForm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officewiseprivilegedList;
    }

    @Override
    public List getAllSiPARFyWiseList(ParAdminSearchCriteria parAdminSearchCriteria) {
        String fiscalYear = parAdminSearchCriteria.getFiscalyear();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet res = null;
        List parList = new ArrayList();
        //PARSearchResult parSearchResult = new PARSearchResult();

        try {
            con = dataSource.getConnection();
//            pst = con.prepareStatement("SELECT FISCAL_YEAR,INITIATEDBY,PARID,EMP_MAST.EMP_ID,PAR_MASTER.CADRE_CODE,EMP_MAST.MOBILE, "
//                + "F_NAME,M_NAME,L_NAME,CADRE_NAME,POST,POST_GROUP,PAR_STATUS,EMP_MAST.CUR_OFF_CODE,GPF_NO,DOB,is_reviewed FROM "
//                + "(SELECT EMP_ID,MOBILE,F_NAME,M_NAME,L_NAME,GPF_NO,DOB,CUR_OFF_CODE FROM EMP_MAST)EMP_MAST "
//                + "INNER JOIN (SELECT INITIATEDBY,SPC,PARID,EMP_ID,PAR_STATUS,POST_GROUP,CADRE_CODE,OFF_CODE,FISCAL_YEAR,is_reviewed "
//                + "FROM PAR_MASTER WHERE FISCAL_YEAR = ?)PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
//                + "LEFT OUTER JOIN G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
//                + "LEFT OUTER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
//                + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE");
            String siParQry = "SELECT fiscal_year, parid, pm.emp_id, F_NAME,M_NAME,L_NAME,GPF_NO,DOB,MOBILE, period_from, period_to, par_status, "
                    + "pm.spc pmSpc,gs.spn, pm.cadre_code, gc.CADRE_NAME, pm.off_code,off_en,post_group,gp.POST,pm.POST_GROUP, headquarter,nrcreason, "
                    + "nrc_submitted_on, remarks, is_adversed,is_reviewed,INITIATEDBY,si_officer_type, place_of_posting FROM PAR_MASTER pm "
                    + "LEFT OUTER JOIN EMP_MAST em on pm.EMP_ID = em.EMP_ID "
                    + "LEFT OUTER JOIN G_CADRE gc ON pm.CADRE_CODE = gc.CADRE_CODE "
                    + "LEFT OUTER JOIN G_SPC gs ON pm.SPC = gs.SPC "
                    + "LEFT OUTER JOIN G_POST gp ON gs.GPC = gp.POST_CODE "
                    + "LEFT OUTER JOIN G_OFFICE gof ON gof.off_code = pm.off_code "
                    + "WHERE FISCAL_YEAR = ? and par_type='SiPar' order by F_NAME, parid desc";
            pst = con.prepareStatement(siParQry);
            pst.setString(1, fiscalYear);
            res = pst.executeQuery();
            while (res.next()) {
                ParAdminProperties parAdminProperties = new ParAdminProperties();

                String fyVal = res.getString("FISCAL_YEAR");
                parAdminProperties.setFiscalyear(res.getString("FISCAL_YEAR"));
                String parId = res.getString("parid");
                parAdminProperties.setParId(res.getInt("PARID"));
                parAdminProperties.setEncParId(parId);
                String empHrmsId = res.getString("EMP_ID");
                parAdminProperties.setEmpId(res.getString("EMP_ID"));

                String fname = "";
                String mname = "";
                String lname = "";
                if (res.getString("F_NAME") != null) {
                    fname = res.getString("F_NAME").trim();
                } else {
                    fname = "";
                }

                if (res.getString("M_NAME") != null) {
                    mname = res.getString("M_NAME").trim();
                } else {
                    mname = "";
                }
                if (res.getString("L_NAME") != null && !res.getString("L_NAME").equals("")) {
                    lname = res.getString("L_NAME").trim();
                } else {
                    lname = "";
                }

                parAdminProperties.setEmpName(fname + " " + mname + " " + lname);
                parAdminProperties.setDob(CommonFunctions.getFormattedOutputDate6(res.getDate("DOB")));
                parAdminProperties.setGpfno(res.getString("GPF_NO"));
                parAdminProperties.setMobile(StringUtils.defaultString(res.getString("MOBILE"), ""));

//               period_from, period_to, par_status, pm.spc pmSpc,gs.spn, pm.cadre_code, gc.CADRE_NAME, pm.off_code,off_en,
//               post_group,gp.POST,pm.POST_GROUP, headquarter,nrcreason,nrc_submitted_on, remarks, is_adversed,is_reviewed,
//               INITIATEDBY,si_officer_type, place_of_posting;
//                parAdminProperties.setPrdFrmDate(CommonFunctions.getFormattedOutputDate1(res.getDate("period_from")));
//                parAdminProperties.setPrdToDate(CommonFunctions.getFormattedOutputDate1(res.getDate("period_to")));
                parAdminProperties.setSubmittedon(CommonFunctions.getFormattedOutputDate1(res.getDate("period_from")) + " - " + CommonFunctions.getFormattedOutputDate1(res.getDate("period_to"))); // no getter method for period from and to date. So i have asign the val here

                if (res.getString("PAR_STATUS") != null && !res.getString("PAR_STATUS").equals("")) {
                    if (res.getInt("PAR_STATUS") == 0) {
                        parAdminProperties.setParstatus("PAR CREATED BUT NOT SUBMITTED");
                    } else {
                        parAdminProperties.setParstatus(getStatusName(con, res.getString("PAR_STATUS")));
                    }
                } else {
                    parAdminProperties.setParstatus("PAR NOT CREATED");
                }
                if (res.getInt("PAR_STATUS") == 17) {
                    String nrcVal = res.getString("nrcreason");
                    if (nrcVal.equals("01")) {
                        nrcVal = "Period is less than 4 month";
                    } else if (nrcVal.equals("02")) {
                        nrcVal = "Availed Commuted Leave";
                    } else if (nrcVal.equals("03")) {
                        nrcVal = "Extension of Joining Time";
                    } else if (nrcVal.equals("04")) {
                        nrcVal = "On Training";
                    } else if (nrcVal.equals("05")) {
                        nrcVal = "Under Suspension";
                    } else if (nrcVal.equals("06")) {
                        nrcVal = "Other Reasons";
                    }
                    parAdminProperties.setNrcDetails(nrcVal);
                }

                parAdminProperties.setCadreName(res.getString("CADRE_NAME"));
                parAdminProperties.setPostName(StringUtils.defaultString(res.getString("spn"), ""));
                parAdminProperties.setCurrentoffice(res.getString("off_en"));
                parAdminProperties.setCurrentOfficeName(res.getString("place_of_posting"));
                parAdminProperties.setPostName(StringUtils.defaultString(res.getString("POST"), ""));
                parAdminProperties.setGroupName(StringUtils.defaultString(res.getString("POST_GROUP"), ""));
//                
//                parAdminProperties.setCurrentoffice(res.getString("CUR_OFF_CODE"));
//                parAdminProperties.setGroupName(StringUtils.defaultString(res.getString("POST_GROUP"), ""));
//                parAdminProperties.setPostName(StringUtils.defaultString(res.getString("POST"), ""));
//                
//                parAdminProperties.setIsreview(res.getString("is_reviewed"));
//                if (res.getString("INITIATEDBY") != null && !res.getString("INITIATEDBY").equals("")) {
//                    parAdminProperties.setInitiatedByEmpId(res.getString("INITIATEDBY"));
//                } else {
//                    parAdminProperties.setInitiatedByEmpId("");
//                }

                List innerDataList = getSiParDataDetails(empHrmsId, fyVal, parId);
                parAdminProperties.setSiParInnerDataList(innerDataList);
                parList.add(parAdminProperties);
            }

            //parSearchResult.setParlist(parList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        //return parSearchResult;
        return parList;
    }

    @Override
    public List getOfficeSpecificSiPARList(ParAdminSearchCriteria parAdminSearchCriteria) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet res = null;
        List parList = new ArrayList();
        //PARSearchResult parSearchResult = new PARSearchResult();

        try {
            con = repodataSource.getConnection();
            /*Search only by fiscal year*/
            String offWiseSiParQry = "SELECT parid,par_status,FISCAL_YEAR,GPF_NO,DOB,PAR_MASTER.EMP_ID,F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE,"
                    + "PAR_MASTER.CADRE_CODE,G_CADRE.CADRE_NAME,POST,POST_GROUP,PAR_MASTER.OFF_CODE,G_OFFICE.off_en,place_of_posting,nrcreason FROM EMP_MAST "
                    + "INNER JOIN (SELECT DISTINCT parid,EMP_ID,fiscal_year,OFF_CODE,PAR_MASTER.CADRE_CODE,SPC,POST_GROUP,TASK_ID,par_status,place_of_posting,nrcreason FROM "
                    + "PAR_MASTER PAR_MASTER WHERE FISCAL_YEAR=? and par_type='SiPar')PAR_MASTER ON EMP_MAST.EMP_ID=PAR_MASTER.EMP_ID "
                    + "INNER JOIN (SELECT CADRE_CODE,POST_GRP,OFF_CODE FROM par_authority_admin WHERE SPC=? and par_for='SiPar')par_authority_admin ON "
                    + "par_authority_admin.OFF_CODE = PAR_MASTER.OFF_CODE and par_authority_admin.post_grp = PAR_MASTER.post_group "
                    + "LEFT OUTER JOIN hrmis2.G_CADRE ON PAR_MASTER.CADRE_CODE=G_CADRE.CADRE_CODE "
                    + "LEFT OUTER JOIN hrmis2.G_SPC ON PAR_MASTER.SPC=G_SPC.SPC "
                    + "LEFT OUTER JOIN hrmis2.G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                    + "LEFT OUTER JOIN hrmis2.TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID "
                    + "LEFT OUTER JOIN hrmis2.G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE order by F_NAME";

            pst = con.prepareStatement(offWiseSiParQry);
            pst.setString(1, parAdminSearchCriteria.getFiscalyear());
            pst.setString(2, parAdminSearchCriteria.getPrivilegedSpc());
            res = pst.executeQuery();
            while (res.next()) {

                ParAdminProperties parAdminProperties = new ParAdminProperties();
                String parId = res.getString("parid");
                parAdminProperties.setEncParId(parId);
                String fyYear = res.getString("FISCAL_YEAR");
                parAdminProperties.setFiscalyear(res.getString("FISCAL_YEAR"));
                parAdminProperties.setGpfno(res.getString("GPF_NO"));
                String empHrmsId = res.getString("EMP_ID");
                parAdminProperties.setEmpId(res.getString("EMP_ID"));
                String fname = "";
                String mname = "";
                String lname = "";

                if (res.getString("F_NAME") != null) {
                    fname = res.getString("F_NAME").trim();
                } else {
                    fname = "";
                }
                if (res.getString("M_NAME") != null) {
                    mname = res.getString("M_NAME").trim();
                } else {
                    mname = "";
                }
                if (res.getString("L_NAME") != null && !res.getString("L_NAME").equals("")) {
                    lname = res.getString("L_NAME").trim();
                } else {
                    lname = "";
                }
                parAdminProperties.setEmpName(fname + " " + mname + " " + lname);
                parAdminProperties.setMobile(StringUtils.defaultString(res.getString("MOBILE"), ""));
                parAdminProperties.setDob(CommonFunctions.getFormattedOutputDate6(res.getDate("DOB")));
                parAdminProperties.setCadreName(res.getString("CADRE_NAME"));
                parAdminProperties.setPostName(StringUtils.defaultString(res.getString("POST"), ""));
                parAdminProperties.setGroupName(StringUtils.defaultString(res.getString("POST_GROUP"), ""));
                parAdminProperties.setCurrentoffice(res.getString("off_en"));
                if (res.getString("PAR_STATUS") != null && !res.getString("PAR_STATUS").equals("")) {
                    if (res.getInt("PAR_STATUS") == 0) {
                        parAdminProperties.setParstatus("PAR CREATED BUT NOT SUBMITTED");
                    } else {
                        parAdminProperties.setParstatus(getStatusName(con, res.getString("PAR_STATUS")));
                    }
                } else {
                    parAdminProperties.setParstatus("PAR NOT CREATED");
                }

                if (res.getString("nrcreason") != null && !res.getString("nrcreason").equals("")) {
                    String nrcVal = res.getString("nrcreason");
                    if (nrcVal.equals("01")) {
                        nrcVal = "Period is less than 4 month";
                    } else if (nrcVal.equals("02")) {
                        nrcVal = "Availed Commuted Leave";
                    } else if (nrcVal.equals("03")) {
                        nrcVal = "Extension of Joining Time";
                    } else if (nrcVal.equals("04")) {
                        nrcVal = "On Training";
                    } else if (nrcVal.equals("05")) {
                        nrcVal = "Under Suspension";
                    } else if (nrcVal.equals("06")) {
                        nrcVal = "Other Reasons";
                    }
                    parAdminProperties.setNrcDetails(nrcVal);
                }

                if (res.getString("place_of_posting") != null && !res.getString("place_of_posting").equals("")) {
                    parAdminProperties.setCurrentOfficeName(res.getString("place_of_posting"));
                } else {
                    parAdminProperties.setCurrentOfficeName("");
                }

                List innerDataList = getSiParDataDetails(empHrmsId, fyYear, parId);
                parAdminProperties.setSiParInnerDataList(innerDataList);
                parList.add(parAdminProperties);
            }

            //parSearchResult.setParlist(parList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        //return parSearchResult;
        return parList;
    }

    public List getSiParDataDetails(String hrmsId, String fiscalYear, String parId) {
        Connection con = null;
        ArrayList pardetail = new ArrayList();
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate, "
                    + "get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname, getspn(TASK_MASTER.pending_spc)as pendingauthorityspc,"
                    + "PENDING_AT,is_reviewed,IS_ADVERSED,adverse_comm_status_id,PARMAST.EMP_ID,PARID,PAR_STATUS,TASK_MASTER.TASK_ID,"
                    + "g_process_status.STATUS_NAME,g_process_status.STATUS_ID,isNRCAttchPresent(parid) as hasAttachment,PERIOD_FROM,PERIOD_TO,nrcreason,nrc_submitted_on "
                    + "FROM (SELECT PERIOD_FROM,PERIOD_TO,initiatedby,post_group,is_reviewed,IS_ADVERSED,adverse_comm_status_id,PARID,EMP_ID,PAR_STATUS,TASK_ID,nrcreason,"
                    + "nrc_submitted_on FROM PAR_MASTER WHERE EMP_ID=? AND FISCAL_YEAR=? and parid = ? and par_type='SiPar')PARMAST "
                    + "INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = PARMAST.EMP_ID "
                    + "LEFT OUTER JOIN (select status_name,process_id,status_id from g_process_status where process_id=3)g_process_status ON "
                    + "PARMAST.PAR_STATUS=g_process_status.STATUS_ID "
                    + "LEFT OUTER JOIN TASK_MASTER ON PARMAST.TASK_ID=TASK_MASTER.TASK_ID order by PARID asc");
            pstmt.setString(1, hrmsId);
            pstmt.setString(2, fiscalYear);
            pstmt.setInt(3, Integer.parseInt(parId));
            rs = pstmt.executeQuery();
            int i = 0;
            while (rs.next()) {
                i++;
                ParAdminProperties pbf = new ParAdminProperties();
                pbf.setParslno(i);
                //pbf.setInitiatedByEmpId(rs.getString("initiatedby"));
                //pbf.setPostGroupType(rs.getString("post_group"));
                //pbf.setFiscalyear(rs.getString("FISCAL_YEAR"));
                pbf.setEmpId(rs.getString("EMP_ID"));
                //pbf.setEmpName(StringUtils.defaultString(rs.getString("INITIALS")) + " " + StringUtils.defaultString(rs.getString("F_NAME")) + " " + StringUtils.defaultString(rs.getString("M_NAME")) + " " + StringUtils.defaultString(rs.getString("L_NAME")));

                //pbf.setSpc(rs.getString("SPC"));
                pbf.setParId(rs.getInt("PARID"));
                pbf.setEncParId(rs.getInt("PARID") + "");
                pbf.setPrdFrmDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_FROM")));
                pbf.setPrdToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_TO")));
                //pbf.setPostName(rs.getString("POST"));
                if (rs.getString("pendingparDate") != null && !rs.getString("pendingparDate").equals("")) {
                    pbf.setParPendingDateFrom(CommonFunctions.getFormattedOutputDate6(rs.getDate("pendingparDate")));
                } else {
                    pbf.setParPendingDateFrom("");
                }
                if (rs.getString("pendingauthorityname") != null && !rs.getString("pendingauthorityname").equals("")) {
                    pbf.setPendingAtAuthName(rs.getString("pendingauthorityname"));
                } else {
                    pbf.setPendingAtAuthName("");
                }
                if (rs.getString("pendingauthorityspc") != null && !rs.getString("pendingauthorityspc").equals("")) {
                    pbf.setPendingAtSpc(rs.getString("pendingauthorityspc"));
                } else {
                    pbf.setPendingAtSpc("");
                }
                pbf.setPendingAtEmpId(rs.getString("PENDING_AT"));

                pbf.setAdverseCommunicationStatusId(rs.getString("adverse_comm_status_id"));

                if (rs.getString("STATUS_NAME") != null && !rs.getString("STATUS_NAME").equals("")) {
                    pbf.setParstatus(rs.getString("STATUS_NAME"));
                } else {
                    pbf.setParstatus("PENDING");
                }
                // athi task id encodeing haba
                if (rs.getString("TASK_ID") != null && !rs.getString("TASK_ID").equals("")) {
                    pbf.setTaskId(rs.getString("TASK_ID"));
                    pbf.setEncTaskId(rs.getString("TASK_ID"));
                } else {
                    pbf.setTaskId("");
                }
                if (rs.getString("is_reviewed") != null && rs.getString("is_reviewed").equals("Y")) {
                    pbf.setIsreview("Y");
                } else {
                    pbf.setIsreview("N");
                }
                if (rs.getString("STATUS_ID") != null && !rs.getString("STATUS_ID").equals("")) {
                    pbf.setParstatusid(rs.getString("STATUS_ID"));
                    if (rs.getInt("STATUS_ID") == 17) {
                        //pbf.setIsNRCAttchPresent(isNRCAttchPresent(con, rs.getString("PARID")));
                        String nrcVal = rs.getString("nrcreason");
                        if (nrcVal.equals("01")) {
                            nrcVal = "Period is less than 4 month";
                        } else if (nrcVal.equals("02")) {
                            nrcVal = "Availed Commuted Leave";
                        } else if (nrcVal.equals("03")) {
                            nrcVal = "Extension of Joining Time";
                        } else if (nrcVal.equals("04")) {
                            nrcVal = "On Training";
                        } else if (nrcVal.equals("05")) {
                            nrcVal = "Under Suspension";
                        } else if (nrcVal.equals("06")) {
                            nrcVal = "Other Reasons";
                        }
                        String nrcDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("nrc_submitted_on"));
                        pbf.setNrcDetails(nrcVal);
                    }
                } else {
                    pbf.setParstatusid("");
                }
                if (rs.getInt("STATUS_ID") > 5) {
                    pbf.setIseditable("N");
                } else {
                    pbf.setIseditable("Y");
                }
                if (rs.getString("IS_ADVERSED") != null && rs.getString("IS_ADVERSED").equals("Y")) {
                    pbf.setPar_master_status("PAR IS ADVERSED");
                    pbf.setIsadversed("Y");
                } else {
                    pbf.setPar_master_status(rs.getString("STATUS_NAME"));
                }
                pbf.setIsNRCAttchPresent(rs.getString("hasAttachment"));
                pardetail.add(pbf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return pardetail;
    }

    @Override
    public List getSiParDetails(String hrmsId, String fiscalYear, String encParId) {
        Connection con = null;
        ArrayList pardetail = new ArrayList();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        //pkm;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT get_par_pending_date(PARID) as pendingparDate, "
                    + "get_empname_from_type(TASK_MASTER.PENDING_AT ,'G') as pendingauthorityname,"
                    + "getspn(TASK_MASTER.pending_spc)as  pendingauthorityspc,PENDING_AT,INITIALS,F_NAME,M_NAME,L_NAME,initiatedby,post_group,"
                    + "is_reviewed,IS_ADVERSED,adverse_comm_status_id,PARMAST.EMP_ID,PARID,FISCAL_YEAR,PERIOD_FROM,PERIOD_TO,PAR_STATUS,PARMAST.SPC,"
                    + "POST,TASK_MASTER.TASK_ID,g_process_status.STATUS_NAME,g_process_status.STATUS_ID,isNRCAttchPresent(parid) as hasAttachment,"
                    + "nrcreason,nrc_submitted_on FROM (SELECT initiatedby,post_group,is_reviewed,IS_ADVERSED,adverse_comm_status_id,PARID,EMP_ID,"
                    + "FISCAL_YEAR,PERIOD_FROM,PERIOD_TO,PAR_STATUS,SPC,CADRE_CODE,TASK_ID,nrcreason,nrc_submitted_on FROM PAR_MASTER WHERE "
                    + "EMP_ID=? AND FISCAL_YEAR=? and parid = ? and par_type='SiPar')PARMAST "
                    + "INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = PARMAST.EMP_ID "
                    + "LEFT OUTER JOIN G_SPC ON G_SPC.SPC=PARMAST.SPC "
                    + "LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "LEFT OUTER JOIN (select status_name,process_id,status_id from g_process_status where process_id=3)g_process_status ON "
                    + "PARMAST.PAR_STATUS=g_process_status.STATUS_ID "
                    + "LEFT OUTER JOIN TASK_MASTER ON PARMAST.TASK_ID=TASK_MASTER.TASK_ID order by PARID asc");
            pstmt.setString(1, hrmsId);
            pstmt.setString(2, fiscalYear);
            pstmt.setInt(3, Integer.parseInt(encParId));
            rs = pstmt.executeQuery();
            int i = 0;
            if (rs.next()) {
                i++;
                ParAdminProperties pbf = new ParAdminProperties();
                pbf.setParslno(i);
                pbf.setInitiatedByEmpId(rs.getString("initiatedby"));
                pbf.setPostGroupType(rs.getString("post_group"));
                pbf.setFiscalyear(rs.getString("FISCAL_YEAR"));
                pbf.setEmpId(rs.getString("EMP_ID"));
                pbf.setEmpName(StringUtils.defaultString(rs.getString("INITIALS")) + " " + StringUtils.defaultString(rs.getString("F_NAME")) + " " + StringUtils.defaultString(rs.getString("M_NAME")) + " " + StringUtils.defaultString(rs.getString("L_NAME")));

                pbf.setSpc(rs.getString("SPC"));
                pbf.setParId(rs.getInt("PARID"));
                pbf.setEncParId(rs.getInt("PARID") + "");
                pbf.setPrdFrmDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_FROM")));
                pbf.setPrdToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_TO")));
                pbf.setPostName(rs.getString("POST"));
                if (rs.getString("pendingparDate") != null && !rs.getString("pendingparDate").equals("")) {
                    pbf.setParPendingDateFrom(CommonFunctions.getFormattedOutputDate6(rs.getDate("pendingparDate")));
                } else {
                    pbf.setParPendingDateFrom("");
                }
                if (rs.getString("pendingauthorityname") != null && !rs.getString("pendingauthorityname").equals("")) {
                    pbf.setPendingAtAuthName(rs.getString("pendingauthorityname"));
                } else {
                    pbf.setPendingAtAuthName("");
                }
                if (rs.getString("pendingauthorityspc") != null && !rs.getString("pendingauthorityspc").equals("")) {
                    pbf.setPendingAtSpc(rs.getString("pendingauthorityspc"));
                } else {
                    pbf.setPendingAtSpc("");
                }
                pbf.setPendingAtEmpId(rs.getString("PENDING_AT"));

                pbf.setAdverseCommunicationStatusId(rs.getString("adverse_comm_status_id"));

                if (rs.getString("STATUS_NAME") != null && !rs.getString("STATUS_NAME").equals("")) {
                    pbf.setParstatus(rs.getString("STATUS_NAME"));
                } else {
                    pbf.setParstatus("PENDING");
                }
                // athi task id encodeing haba
                if (rs.getString("TASK_ID") != null && !rs.getString("TASK_ID").equals("")) {
                    pbf.setTaskId(rs.getString("TASK_ID"));
                    pbf.setEncTaskId(rs.getString("TASK_ID"));
                } else {
                    pbf.setTaskId("");
                }
                if (rs.getString("is_reviewed") != null && rs.getString("is_reviewed").equals("Y")) {
                    pbf.setIsreview("Y");
                } else {
                    pbf.setIsreview("N");
                }
                if (rs.getString("STATUS_ID") != null && !rs.getString("STATUS_ID").equals("")) {
                    pbf.setParstatusid(rs.getString("STATUS_ID"));
                    if (rs.getInt("STATUS_ID") == 17) {
                        //pbf.setIsNRCAttchPresent(isNRCAttchPresent(con, rs.getString("PARID")));
                        String nrcVal = rs.getString("nrcreason");
                        if (nrcVal.equals("01")) {
                            nrcVal = "Period is less than 4 month";
                        } else if (nrcVal.equals("02")) {
                            nrcVal = "Availed Commuted Leave";
                        } else if (nrcVal.equals("03")) {
                            nrcVal = "Extension of Joining Time";
                        } else if (nrcVal.equals("04")) {
                            nrcVal = "On Training";
                        } else if (nrcVal.equals("05")) {
                            nrcVal = "Under Suspension";
                        } else if (nrcVal.equals("06")) {
                            nrcVal = "Other Reasons";
                        }
                        String nrcDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("nrc_submitted_on"));
                        pbf.setNrcDetails(nrcVal);
                    }
                } else {
                    pbf.setParstatusid("");
                }
                if (rs.getInt("STATUS_ID") > 5) {
                    pbf.setIseditable("N");
                } else {
                    pbf.setIseditable("Y");
                }
                if (rs.getString("IS_ADVERSED") != null && rs.getString("IS_ADVERSED").equals("Y")) {
                    pbf.setPar_master_status("PAR IS ADVERSED");
                    pbf.setIsadversed("Y");
                } else {
                    pbf.setPar_master_status(rs.getString("STATUS_NAME"));
                }
                pbf.setIsNRCAttchPresent(rs.getString("hasAttachment"));
                pardetail.add(pbf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return pardetail;
    }

    @Override
    public String checkParViewPrivilege(String spc) {
        String privType = "N";
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Connection con = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT authorization_type,emp_id,PAA.SPC,PAA.post_grp,"
                    + "PAA.OFF_CODE,g_district.dist_code FROM par_authority_admin PAA "
                    + "INNER JOIN hrmis2.G_SPC ON PAA.SPC = G_SPC.SPC "
                    + "LEFT OUTER JOIN hrmis2.EMP_MAST ON PAA.SPC = EMP_MAST.CUR_SPC "
                    + "INNER JOIN hrmis2.G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "LEFT OUTER JOIN hrmis2.g_office ON PAA.off_code = g_office.off_code "
                    + "LEFT OUTER JOIN hrmis2.g_district ON g_office.dist_code = g_district.dist_code "
                    + "WHERE PAA.par_for='SiPar' and PAA.SPC = ? and authorization_type='all'");
            pstmt.setString(1, spc);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                privType = "Y";
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return privType;
    }

    @Override
    public ParApplyForm viewTotalSiParDetail(int parId, int taskId) {
        ParApplyForm paf = new ParApplyForm();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String applicantSpc = "";
        Connection con = null;
        String selfappraisal = "";
        String specialcontribution = "";
        String factors = "";
        String parDataQry = "";

        try {
            con = dataSource.getConnection();
            if (taskId == 0) {
                parDataQry = "SELECT INITIATED_ON,HEADQUARTER,PAR1.par_type, PAR1.si_officer_type,PAR1.place_of_posting,PAR1.CADRE_CODE,PAR1.EMP_ID,"
                        + "FISCAL_YEAR,PAR1.OFF_CODE,PAR1.PARID,PAR_STATUS,REF_ID_OF_TABLE,PERIOD_FROM,PERIOD_TO,APPRISE_APC,APPRISE_SPN,APPRISE_OFFICE,"
                        + "PAR1.TASK_ID,SPECIALCONTRIBUTIION,SELFAPPRAISAL,HINDERREASON,fivet_component_appraise,SUBMITTED_ON,PAR_APPRAISEE_TRAN.PLACE,"
                        + "DOB,INITIALS,F_NAME,M_NAME,L_NAME,G_CADRE.CADRE_NAME,PAR1.POST_GROUP,IS_REVIEWED, REVIEWED_ONDATE, IS_ADVERSED FROM "
                        + "(SELECT HEADQUARTER,CADRE_CODE,EMP_ID,FISCAL_YEAR,OFF_CODE,PARID,PAR_STATUS,REF_ID_OF_TABLE,PERIOD_FROM,PERIOD_TO,SPC APPRISE_APC,"
                        + "GETSPN(SPC) APPRISE_SPN,GETOFFNAMEFROMSPC(SPC) APPRISE_OFFICE,TASK_ID,POST_GROUP,par_type,si_officer_type,place_of_posting, "
                        + "IS_REVIEWED, REVIEWED_ONDATE, IS_ADVERSED FROM PAR_MASTER WHERE PARID=?)PAR1 "
                        + "LEFT OUTER JOIN PAR_APPRAISEE_TRAN ON PAR1.PARID = PAR_APPRAISEE_TRAN.PARID "
                        + "INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = PAR1.EMP_ID "
                        + " LEFT OUTER JOIN G_CADRE ON PAR1.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + " LEFT OUTER JOIN TASK_MASTER ON PAR1.TASK_ID=TASK_MASTER.TASK_ID";
                pstmt = con.prepareStatement(parDataQry);
                pstmt.setInt(1, parId);
            } else {
                parDataQry = "SELECT INITIATED_ON,HEADQUARTER,PAR1.par_type,PAR1.si_officer_type,PAR1.place_of_posting,PAR1.CADRE_CODE,"
                        + "PAR1.EMP_ID,FISCAL_YEAR,PAR1.OFF_CODE,PAR1.PARID,PAR_STATUS,IS_REVIEWED, REVIEWED_ONDATE, IS_ADVERSED, "
                        + "REF_ID_OF_TABLE,PERIOD_FROM,PERIOD_TO,APPRISE_APC,APPRISE_SPN,APPRISE_OFFICE,PAR1.TASK_ID,SPECIALCONTRIBUTIION,SELFAPPRAISAL,"
                        + "HINDERREASON,fivet_component_appraise,SUBMITTED_ON,PAR_APPRAISEE_TRAN.PLACE,DOB,INITIALS,F_NAME,M_NAME,L_NAME,"
                        + "G_CADRE.CADRE_NAME,PAR1.POST_GROUP FROM "
                        + "(SELECT HEADQUARTER,CADRE_CODE,EMP_ID,FISCAL_YEAR,OFF_CODE,PARID,PAR_STATUS,REF_ID_OF_TABLE,PERIOD_FROM,PERIOD_TO,SPC APPRISE_APC,"
                        + "GETSPN(SPC) APPRISE_SPN,GETOFFNAMEFROMSPC(SPC) APPRISE_OFFICE,TASK_ID,POST_GROUP,par_type, si_officer_type,place_of_posting, "
                        + "IS_REVIEWED, REVIEWED_ONDATE, IS_ADVERSED FROM PAR_MASTER WHERE TASK_ID=?)PAR1 "
                        + "LEFT OUTER JOIN PAR_APPRAISEE_TRAN ON PAR1.PARID = PAR_APPRAISEE_TRAN.PARID "
                        + "INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = PAR1.EMP_ID "
                        + " LEFT OUTER JOIN G_CADRE ON PAR1.CADRE_CODE=G_CADRE.CADRE_CODE"
                        + " INNER JOIN TASK_MASTER ON PAR1.TASK_ID=TASK_MASTER.TASK_ID";
                pstmt = con.prepareStatement(parDataQry);
                pstmt.setInt(1, taskId);
            }
            rs = pstmt.executeQuery();
            if (rs.next()) {

                selfappraisal = StringUtils.defaultString(rs.getString("SELFAPPRAISAL"));
                selfappraisal = selfappraisal.replaceAll("<h4>", "");
                selfappraisal = selfappraisal.replaceAll("</h4>", "");

                specialcontribution = StringUtils.defaultString(rs.getString("SPECIALCONTRIBUTIION"));
                specialcontribution = specialcontribution.replaceAll("<h4>", "");
                specialcontribution = specialcontribution.replaceAll("</h4>", "");

                factors = StringUtils.defaultString(rs.getString("HINDERREASON"));
                factors = factors.replaceAll("<h4>", "");
                factors = factors.replaceAll("</h4>", "");

                paf.setFiveTComponentappraise(rs.getString("fivet_component_appraise"));
                paf.setParId(rs.getInt("PARID"));
                paf.setEncryptedParid(CommonFunctions.encodedTxt(rs.getInt("PARID") + ""));
                paf.setParstatus(rs.getInt("PAR_STATUS"));
                paf.setApprisespc(rs.getString("APPRISE_APC"));
                paf.setRefid(rs.getInt("REF_ID_OF_TABLE"));
                paf.setTaskId(rs.getInt("TASK_ID"));
                paf.setEncryptedTaskid(CommonFunctions.encodedTxt(rs.getInt("TASK_ID") + ""));
                paf.setFiscalYear(rs.getString("FISCAL_YEAR"));

                paf.setPrdFrmDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_FROM")));
                paf.setPrdToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_TO")));
                paf.setEmpId(rs.getString("EMP_ID"));
                paf.setEmpName(StringUtils.defaultString(rs.getString("INITIALS")) + " " + StringUtils.defaultString(rs.getString("F_NAME")) + " " + StringUtils.defaultString(rs.getString("M_NAME")) + " " + StringUtils.defaultString(rs.getString("L_NAME")));
                paf.setApprisespc(rs.getString("APPRISE_SPN"));
                paf.setAppriseOffice(rs.getString("APPRISE_OFFICE"));
                paf.setEmpOffice(rs.getString("APPRISE_OFFICE"));

                paf.setSpecialcontribution(specialcontribution);
                paf.setSelfappraisal(selfappraisal);
                paf.setReason(factors);

                if (rs.getString("INITIATED_ON") != null && !rs.getString("INITIATED_ON").equals("")) {
                    paf.setSubmittedon(CommonFunctions.getFormattedOutputDate1(rs.getDate("INITIATED_ON")));
                } else {
                    paf.setSubmittedon("");
                }
//                selfappraisal = policy.sanitize(selfappraisal);
//                specialcontribution = policy.sanitize(specialcontribution);
//                factors = policy.sanitize(factors);
                paf.setPlace(rs.getString("PLACE"));
                paf.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOB")));
                paf.setEmpService(rs.getString("CADRE_NAME"));
                paf.setEmpGroup(rs.getString("POST_GROUP"));
                paf.setSltHeadQuarter(rs.getString("HEADQUARTER"));

                if (rs.getString("par_type") == null || rs.getString("par_type").equals("")) {
                    paf.setParType("");
                    paf.setSiType("");
                    paf.setPlaceOfPostingSi("");
                } else {
                    paf.setParType(rs.getString("par_type"));
                    if (rs.getString("si_officer_type") != null && !rs.getString("si_officer_type").equals("")) {
                        switch (rs.getString("si_officer_type")) {
                            case "Si1":
                                paf.setSiType("Sub Inspector (Civil)");
                                break;
                            case "Si2":
                                paf.setSiType("Sub Inspector (Armed)");
                                break;
                            case "Si3":
                                paf.setSiType("Sub Inspector (Equivalent)");
                                break;
                        }
                        paf.setPlaceOfPostingSi(rs.getString("place_of_posting"));
                    }
                }

                paf.setReviewedondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("reviewed_ondate")));
                paf.setIsreviewed(rs.getString("IS_REVIEWED"));
                //paf.setReviewedby(rs.getString("REVIEWED_BY_NAME"));
                PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);

                paf.setSltAdminRemark(rs.getString("IS_ADVERSED"));
                paf.setLeaveAbsentee(getAbsenteeList(paf.getParId()));
                paf.setAchivementList(getAchievementList(paf.getParId()));

                paf.setReportingauth(getParAuthority("REPORTING", paf.getParId(), taskId));
                paf.setReviewingauth(getParAuthority("REVIEWING", paf.getParId(), taskId));
                paf.setAcceptingauth(getParAuthority("ACCEPTING", paf.getParId(), taskId));

                paf.setReportingdata(getReportingData(paf));
                paf.setReviewingdata(getReviewingData(paf));
                paf.setAcceptingdata(getAcceptingData(paf));

                //paf.setIsClosedFiscalYearAuthority(getIsActiveForAuthority(con, rs.getString("FISCAL_YEAR")));
                //paf.setSltHeadQuarter(rs.getString("HEADQUARTER"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return paf;
    }

    @Override
    public void deleteOfficeWiseSiPrivilage(String offCode, String spc, String postGroup) {
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            String spc1 = spc;
            pst = con.prepareStatement("DELETE FROM PAR_AUTHORITY_ADMIN WHERE OFF_CODE=? AND SPC=? AND POST_GRP=? and par_for = 'SiPar'");
            pst.setString(1, offCode);
            pst.setString(2, spc);
            pst.setString(3, postGroup);
            pst.executeUpdate();

            pst1 = con.prepareStatement("DELETE FROM G_PRIVILEGE_MAP WHERE SPC=? AND role_id = '20'");
            pst1.setString(1, spc1);
            pst1.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deletePrivilegedListDistWiseForPoliceSi(String spc, String postgrp) {
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            String spc1 = spc;
            pst = con.prepareStatement("DELETE FROM  par_authority_admin WHERE SPC = ? and post_grp = ? and par_for = 'SiPar'");
            pst.setString(1, spc);
            pst.setString(2, postgrp);
            pst.executeUpdate();

            pst1 = con.prepareStatement("DELETE FROM G_PRIVILEGE_MAP WHERE SPC = ? AND role_id = '20'");
            pst1.setString(1, spc1);
            pst1.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void transferPARListForForceForwardFromReportingToReviewingToLog(ParForceForwardBean parForceForwardBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        ResultSet res = null;
        ArrayList listOfParForceForward = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt1 = con.prepareStatement("insert into par_forceforward_detail_log(par_id,task_id,pending_at_before_forceforward,status_id_before_forceford,fiscal_year,forceforward_from_authority_type) values (?,?,?,?,?,?)");
            pstmt = con.prepareStatement("select par_master.emp_id,par_master.parid,task_master.task_id,task_master.pending_at from par_master "
                    + "inner join task_master on par_master.task_id = task_master.task_id WHERE fiscal_year='2022-23' AND PAR_STATUS=6");
            res = pstmt.executeQuery();
            while (res.next()) {
                ParForceForwardBean pffb = new ParForceForwardBean();
                pffb.setEmpId(res.getString("emp_id"));
                pffb.setParId(res.getInt("parid"));
                pffb.setTaskId(res.getInt("task_id"));
                pffb.setPendingat(res.getString("pending_at"));
                listOfParForceForward.add(pffb);

                pstmt1.setInt(1, pffb.getParId());
                pstmt1.setInt(2, pffb.getTaskId());
                pstmt1.setString(3, pffb.getPendingat());
                pstmt1.setInt(4, 6);
                pstmt1.setString(5, "2022-23");
                pstmt1.setString(6, "Reporting");
                pstmt1.executeUpdate();
            }

        } catch (SQLException e) {
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt, con);
        }
    }

    @Override
    public void transferPARListForForceForwardFromReviewingToAcceptingToLog(ParForceForwardBean parForceForwardBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        ResultSet res = null;
        ArrayList listOfParForceForward = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt1 = con.prepareStatement("insert into par_forceforward_detail_log(par_id,task_id,pending_at_before_forceforward,status_id_before_forceford,fiscal_year,forceforward_from_authority_type) values (?,?,?,?,?,?)");
            pstmt = con.prepareStatement("select par_master.emp_id,par_master.parid,task_master.task_id,task_master.pending_at from par_master "
                    + "inner join task_master on par_master.task_id = task_master.task_id WHERE fiscal_year='2022-23' AND PAR_STATUS=7");
            res = pstmt.executeQuery();
            while (res.next()) {
                ParForceForwardBean pffb = new ParForceForwardBean();
                pffb.setEmpId(res.getString("emp_id"));
                pffb.setParId(res.getInt("parid"));
                pffb.setTaskId(res.getInt("task_id"));
                pffb.setPendingat(res.getString("pending_at"));
                listOfParForceForward.add(pffb);

                pstmt1.setInt(1, pffb.getParId());
                pstmt1.setInt(2, pffb.getTaskId());
                pstmt1.setString(3, pffb.getPendingat());
                pstmt1.setInt(4, 6);
                pstmt1.setString(5, "2022-23");
                pstmt1.setString(6, "Reviewing");
                pstmt1.executeUpdate();
            }
        } catch (SQLException e) {
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt, con);
        }
    }

    @Override
    public List getSiParFyWiseCustodianList(String fiscalYear, String custodianOffCode, String custodianDistCode, String searchCriteria, String searchString) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet res = null;
        List parList = new ArrayList();

        try {
            con = dataSource.getConnection();
            /*Search only by fiscal year*/
            if ((searchCriteria == null || searchCriteria.equals(""))) {
                pst = con.prepareStatement("select parid,FISCAL_YEAR,PM.EMP_ID,F_NAME,M_NAME,L_NAME,EM.MOBILE,GPF_NO,DOB,PM.CADRE_CODE,G_CADRE.CADRE_NAME,"
                        + "POST,POST_GROUP,PM.OFF_CODE,G_OFFICE.off_en,place_of_posting,nrcreason,PM.task_id,par_status from par_master PM "
                        + "LEFT OUTER JOIN EMP_MAST EM ON EM.EMP_ID = PM.EMP_ID "
                        + "LEFT OUTER JOIN G_CADRE ON PM.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PM.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN TASK_MASTER ON PM.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN G_OFFICE ON PM.OFF_CODE=G_OFFICE.OFF_CODE "
                        + "where fiscal_year = ? and par_type='SiPar' and PM.off_code = ? order by f_name");
                pst.setString(1, fiscalYear);
                pst.setString(2, custodianOffCode);

            } else if (searchCriteria.equals("empid")) {
                pst = con.prepareStatement("select parid,FISCAL_YEAR,PM.EMP_ID,F_NAME,M_NAME,L_NAME,EM.MOBILE,GPF_NO,DOB,PM.CADRE_CODE,G_CADRE.CADRE_NAME,"
                        + "POST,POST_GROUP,PM.OFF_CODE,G_OFFICE.off_en,place_of_posting,nrcreason,PM.task_id,par_status from par_master PM "
                        + "LEFT OUTER JOIN EMP_MAST EM ON EM.EMP_ID = PM.EMP_ID "
                        + "LEFT OUTER JOIN G_CADRE ON PM.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PM.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN TASK_MASTER ON PM.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN G_OFFICE ON PM.OFF_CODE=G_OFFICE.OFF_CODE "
                        + "where fiscal_year = ? and par_type='SiPar' and PM.off_code = ? and PM.emp_id=?");

                pst.setString(1, fiscalYear);
                pst.setString(2, custodianOffCode);
                pst.setString(3, searchString);
            } else if (searchCriteria.equals("gpfno")) {
                pst = con.prepareStatement("select parid,FISCAL_YEAR,PM.EMP_ID,F_NAME,M_NAME,L_NAME,EM.MOBILE,GPF_NO,DOB,PM.CADRE_CODE,G_CADRE.CADRE_NAME,"
                        + "POST,POST_GROUP,PM.OFF_CODE,G_OFFICE.off_en,place_of_posting,nrcreason,PM.task_id,par_status from par_master PM "
                        + "LEFT OUTER JOIN EMP_MAST EM ON EM.EMP_ID = PM.EMP_ID "
                        + "LEFT OUTER JOIN G_CADRE ON PM.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PM.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN TASK_MASTER ON PM.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN G_OFFICE ON PM.OFF_CODE=G_OFFICE.OFF_CODE "
                        + "where fiscal_year = ? and par_type='SiPar' and PM.off_code = ? and EM.gpf_no=?");

                pst.setString(1, fiscalYear);
                pst.setString(2, custodianOffCode);
                pst.setString(3, searchString);
            } else if (searchCriteria.equals("empname")) {
                pst = con.prepareStatement("select parid,FISCAL_YEAR,PM.EMP_ID,F_NAME,M_NAME,L_NAME,EM.MOBILE,GPF_NO,DOB,PM.CADRE_CODE,G_CADRE.CADRE_NAME,"
                        + "POST,POST_GROUP,PM.OFF_CODE,G_OFFICE.off_en,place_of_posting,nrcreason,PM.task_id,par_status from par_master PM "
                        + "LEFT OUTER JOIN EMP_MAST EM ON EM.EMP_ID = PM.EMP_ID "
                        + "LEFT OUTER JOIN G_CADRE ON PM.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PM.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN TASK_MASTER ON PM.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN G_OFFICE ON PM.OFF_CODE=G_OFFICE.OFF_CODE "
                        + "where fiscal_year = ? and par_type='SiPar' and PM.off_code = ? and EM.F_NAME=? order by f_name");

                pst.setString(1, fiscalYear);
                pst.setString(2, custodianOffCode);
                pst.setString(3, searchString);
            } else if (searchCriteria.equals("lastname")) {
                pst = con.prepareStatement("select parid,FISCAL_YEAR,PM.EMP_ID,F_NAME,M_NAME,L_NAME,EM.MOBILE,GPF_NO,DOB,PM.CADRE_CODE,G_CADRE.CADRE_NAME,"
                        + "POST,POST_GROUP,PM.OFF_CODE,G_OFFICE.off_en,place_of_posting,nrcreason,PM.task_id,par_status from par_master PM "
                        + "LEFT OUTER JOIN EMP_MAST EM ON EM.EMP_ID = PM.EMP_ID "
                        + "LEFT OUTER JOIN G_CADRE ON PM.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PM.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN TASK_MASTER ON PM.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN G_OFFICE ON PM.OFF_CODE=G_OFFICE.OFF_CODE "
                        + "where fiscal_year = ? and par_type='SiPar' and PM.off_code = ? and EM.L_NAME=? order by f_name");

                pst.setString(1, fiscalYear);
                pst.setString(2, custodianOffCode);
                pst.setString(3, searchString);
            } else if (searchCriteria.equals("mobile")) {
                pst = con.prepareStatement("select parid,FISCAL_YEAR,PM.EMP_ID,F_NAME,M_NAME,L_NAME,EM.MOBILE,GPF_NO,DOB,PM.CADRE_CODE,G_CADRE.CADRE_NAME,"
                        + "POST,POST_GROUP,PM.OFF_CODE,G_OFFICE.off_en,place_of_posting,nrcreason,PM.task_id,par_status from par_master PM "
                        + "LEFT OUTER JOIN EMP_MAST EM ON EM.EMP_ID = PM.EMP_ID "
                        + "LEFT OUTER JOIN G_CADRE ON PM.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PM.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN TASK_MASTER ON PM.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN G_OFFICE ON PM.OFF_CODE=G_OFFICE.OFF_CODE "
                        + "where fiscal_year = ? and par_type='SiPar' and PM.off_code = ? and EM.mobile= ?");

                pst.setString(1, fiscalYear);
                pst.setString(2, custodianOffCode);
                pst.setString(3, searchString);
            } else if (searchCriteria.equals("dob")) {
                pst = con.prepareStatement("select parid,FISCAL_YEAR,PM.EMP_ID,F_NAME,M_NAME,L_NAME,EM.MOBILE,GPF_NO,DOB,PM.CADRE_CODE,G_CADRE.CADRE_NAME,"
                        + "POST,POST_GROUP,PM.OFF_CODE,G_OFFICE.off_en,place_of_posting,nrcreason,PM.task_id,par_status from par_master PM "
                        + "LEFT OUTER JOIN EMP_MAST EM ON EM.EMP_ID = PM.EMP_ID "
                        + "LEFT OUTER JOIN G_CADRE ON PM.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + "LEFT OUTER JOIN G_SPC ON PM.SPC=G_SPC.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + "LEFT OUTER JOIN TASK_MASTER ON PM.TASK_ID=TASK_MASTER.TASK_ID "
                        + "LEFT OUTER JOIN G_OFFICE ON PM.OFF_CODE=G_OFFICE.OFF_CODE "
                        + "where fiscal_year = ? and par_type='SiPar' and PM.off_code = ? and EM.dob= to_date(?,'DD-MM-yyyy') order by EM.F_NAME");

                pst.setString(1, fiscalYear);
                pst.setString(2, custodianOffCode);
                pst.setString(3, searchString);
            }
            res = pst.executeQuery();

            int count = 0;
            while (res.next()) {
                count++;
                ParAdminProperties parAdminProperties = new ParAdminProperties();
                String parId = res.getString("parid");
                parAdminProperties.setEncParId(parId);
                String fyYear = res.getString("FISCAL_YEAR");
                parAdminProperties.setFiscalyear(res.getString("FISCAL_YEAR"));
                String empHrmsId = res.getString("EMP_ID");
                parAdminProperties.setEmpId(res.getString("EMP_ID"));
                String fname = "";
                String mname = "";
                String lname = "";

                if (res.getString("F_NAME") != null) {
                    fname = res.getString("F_NAME").trim();
                } else {
                    fname = "";
                }
                if (res.getString("M_NAME") != null) {
                    mname = res.getString("M_NAME").trim();
                } else {
                    mname = "";
                }
                if (res.getString("L_NAME") != null && !res.getString("L_NAME").equals("")) {
                    lname = res.getString("L_NAME").trim();
                } else {
                    lname = "";
                }
                parAdminProperties.setEmpName(fname + " " + mname + " " + lname);
                parAdminProperties.setMobile(StringUtils.defaultString(res.getString("MOBILE"), ""));
                parAdminProperties.setGpfno(res.getString("GPF_NO"));
                parAdminProperties.setDob(CommonFunctions.getFormattedOutputDate6(res.getDate("DOB")));

                parAdminProperties.setCadreName(res.getString("CADRE_NAME"));
                parAdminProperties.setPostName(StringUtils.defaultString(res.getString("POST"), ""));
                parAdminProperties.setGroupName(StringUtils.defaultString(res.getString("POST_GROUP"), ""));
                parAdminProperties.setCurrentoffice(res.getString("off_en"));
                if (res.getString("PAR_STATUS") != null && !res.getString("PAR_STATUS").equals("")) {
                    if (res.getInt("PAR_STATUS") == 0) {
                        parAdminProperties.setParstatus("PAR CREATED BUT NOT SUBMITTED");
                    } else {
                        parAdminProperties.setParstatus(getStatusName(con, res.getString("PAR_STATUS")));
                    }
                } else {
                    parAdminProperties.setParstatus("PAR NOT CREATED");
                }

                if (res.getString("nrcreason") != null && !res.getString("nrcreason").equals("")) {
                    String nrcVal = res.getString("nrcreason");
                    if (nrcVal.equals("01")) {
                        nrcVal = "Period is less than 4 month";
                    } else if (nrcVal.equals("02")) {
                        nrcVal = "Availed Commuted Leave";
                    } else if (nrcVal.equals("03")) {
                        nrcVal = "Extension of Joining Time";
                    } else if (nrcVal.equals("04")) {
                        nrcVal = "On Training";
                    } else if (nrcVal.equals("05")) {
                        nrcVal = "Under Suspension";
                    } else if (nrcVal.equals("06")) {
                        nrcVal = "Other Reasons";
                    }
                    parAdminProperties.setNrcDetails(nrcVal);
                }

                if (res.getString("place_of_posting") != null && !res.getString("place_of_posting").equals("")) {
                    parAdminProperties.setCurrentOfficeName(res.getString("place_of_posting"));
                } else {
                    parAdminProperties.setCurrentOfficeName("");
                }
                List innerDataList = getSiParDataDetails(empHrmsId, fyYear, parId);
                parAdminProperties.setSiParInnerDataList(innerDataList);
                parList.add(parAdminProperties);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parList;
    }

    @Override
    public void getAppraiseForAdverseSiPAR(ParAdverseCommunicationDetail parAdverseCommunicationDetail) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT par_master.emp_id,par_master.spc,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME, post ,cur_spc from par_master \n"
                    + "INNER JOIN emp_mast ON emp_mast.emp_id = par_master.emp_id "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "where parid =? ");
            pstmt.setInt(1, parAdverseCommunicationDetail.getParId());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                parAdverseCommunicationDetail.setToempId(rs.getString("emp_id"));
                parAdverseCommunicationDetail.setTospc(rs.getString("spc"));
                parAdverseCommunicationDetail.setToempName(rs.getString("EMPNAME"));
                parAdverseCommunicationDetail.setToPost(rs.getString("post"));
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getcustodianremarksAdverseSiPAR(int parId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList custodianadverseremarkList = new ArrayList();
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("select getspn(par_adverse_communication.from_spc) as from_post,get_empname_from_type(par_adverse_communication.from_emp_id ,'G') as from_empname,"
                    + " get_empame_adversepar_si(par_adverse_communication.from_emp_id ,'B') as from_empname_custodian,get_empame_adversepar_si(par_adverse_communication.to_emp_id ,'B') as to_empname_custodian,"
                    + " from_emp_id,from_spc,org_file_name,disk_file_name,remarks,get_empname_from_type(par_adverse_communication.to_emp_id ,'G') as to_empname,"
                    + " getspn(par_adverse_communication.to_spc) as to_post,to_emp_id,to_spc,communication_date,from_auth_type,to_auth_type,communication_id from par_adverse_communication where parid =?");

            pst.setInt(1, parId);
            rs = pst.executeQuery();

            while (rs.next()) {
                ParAdverseCommunicationDetail parAdverseCommunicationDetail = new ParAdverseCommunicationDetail();
                parAdverseCommunicationDetail.setFromPost(rs.getString("from_post"));
                parAdverseCommunicationDetail.setFromempId(rs.getString("from_emp_id"));
                parAdverseCommunicationDetail.setFromspc(rs.getString("from_spc"));
                parAdverseCommunicationDetail.setOriginalFilename(rs.getString("org_file_name"));
                parAdverseCommunicationDetail.setDiskFileName(rs.getString("disk_file_name"));
                parAdverseCommunicationDetail.setRemarksdetail(rs.getString("remarks"));
                parAdverseCommunicationDetail.setToPost(rs.getString("to_post"));
                parAdverseCommunicationDetail.setToempId(rs.getString("to_emp_id"));
                parAdverseCommunicationDetail.setTospc(rs.getString("to_spc"));
                parAdverseCommunicationDetail.setAdverseCommunicationOnDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("communication_date")));
                parAdverseCommunicationDetail.setFromAuthType(rs.getString("from_auth_type"));
                parAdverseCommunicationDetail.setToAuthType(rs.getString("to_auth_type"));
                if (rs.getString("from_auth_type").equals("Custodian")) {
                    parAdverseCommunicationDetail.setFromempName(rs.getString("from_empname_custodian"));
                } else {
                    parAdverseCommunicationDetail.setFromempName(rs.getString("from_empname"));
                }
                if (rs.getString("to_auth_type").equals("Custodian")) {
                    parAdverseCommunicationDetail.setToempName(rs.getString("to_empname_custodian"));
                } else {
                    parAdverseCommunicationDetail.setToempName(rs.getString("to_empname"));
                }
                parAdverseCommunicationDetail.setCommunicationId(rs.getInt("communication_id"));

                custodianadverseremarkList.add(parAdverseCommunicationDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return custodianadverseremarkList;
    }

    /*while Custodian Initiate Adverse Communication With Appraisee in SIPAR*/
    @Override
    public void savecustodianremarksAdversePARToAppraiseeSiPAR(ParAdverseCommunicationDetail parAdverseCommunicationDetail) {
        String diskfileName = null;
        PreparedStatement pst = null;
        Connection con = null;
        String originalFileName = null;
        String contentType = null;
        ResultSet rs = null;
        String mobileno = null;
        try {
            con = dataSource.getConnection();

            if (!parAdverseCommunicationDetail.getUploadDocument().isEmpty()) {
                diskfileName = new Date().getTime() + "";
                originalFileName = parAdverseCommunicationDetail.getUploadDocument().getOriginalFilename();
                contentType = parAdverseCommunicationDetail.getUploadDocument().getContentType();
                byte[] bytes = parAdverseCommunicationDetail.getUploadDocument().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + diskfileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }

            pst = con.prepareStatement("SELECT mobile FROM EMP_MAST WHERE emp_id=?");
            pst.setString(1, parAdverseCommunicationDetail.getToempId());
            rs = pst.executeQuery();
            if (rs.next()) {
                parAdverseCommunicationDetail.setMobileNo(rs.getString("mobile"));
                mobileno = parAdverseCommunicationDetail.getMobileNo();
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            Long curtime = new Date().getTime();

            pst = con.prepareStatement("INSERT INTO par_adverse_communication(parid,from_emp_id,from_spc,to_emp_id,to_spc,remarks,disk_file_name,org_file_name,file_type,remarks_on_date,file_path,communication_date,from_auth_type,to_auth_type,PAR_TYPE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, parAdverseCommunicationDetail.getParId());
            pst.setString(2, parAdverseCommunicationDetail.getFromempId());
            pst.setString(3, parAdverseCommunicationDetail.getFromspc());
            pst.setString(4, parAdverseCommunicationDetail.getToempId());
            pst.setString(5, parAdverseCommunicationDetail.getTospc());
            pst.setString(6, parAdverseCommunicationDetail.getRemarksdetail());
            pst.setString(7, diskfileName);
            pst.setString(8, originalFileName);
            pst.setString(9, contentType);
            pst.setTimestamp(10, new Timestamp(curtime));
            pst.setString(11, this.uploadPath);
            pst.setTimestamp(12, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(13, parAdverseCommunicationDetail.getFromAuthType());
            pst.setString(14, parAdverseCommunicationDetail.getToAuthType());
            pst.setString(15, "SiPAR");
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            int communicationId = 0;
            if (rs.next()) {
                communicationId = rs.getInt("communication_id");
            }

            if (mobileno != null && !mobileno.equals("")) {
                String msg = "Representation If any on the Adverse remarks communicated to you is awaited within 45 days.";
                new SMSServices(mobileno, msg, "1407162814759675173");
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            if (parAdverseCommunicationDetail.getTaskId() == 0) {
                pst = con.prepareStatement("INSERT INTO TASK_MASTER(PROCESS_ID, INITIATED_BY, INITIATED_ON, STATUS_ID, PENDING_AT,INITIATED_SPC,PENDING_SPC,ref_id) Values (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                pst.setInt(1, 26);//In g_workflow_process for PAR ADVERSE SIPAR
                pst.setString(2, parAdverseCommunicationDetail.getFromempId());
                pst.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setInt(4, 121);//IN G_PROCESS_STATUS FOR REVIEWING PAR IS ADVERSED BY CUSTODIAN IN SIPAR
                pst.setString(5, parAdverseCommunicationDetail.getToempId());
                pst.setString(6, parAdverseCommunicationDetail.getFromspc());
                pst.setString(7, parAdverseCommunicationDetail.getTospc());
                pst.setInt(8, communicationId);
                pst.executeUpdate();

                rs = pst.getGeneratedKeys();
                int taskId = 0;
                if (rs.next()) {
                    taskId = rs.getInt("task_id");
                }
                // DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("UPDATE PAR_MASTER SET is_adversed='Y',adverse_comm_status_id=121 WHERE parid=?");
                pst.setInt(1, parAdverseCommunicationDetail.getParId());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("UPDATE par_adverse_communication SET task_id=? WHERE parid=?");
                pst.setInt(1, taskId);
                pst.setInt(2, parAdverseCommunicationDetail.getParId());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);
            } else {
                pst = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=NULL,PENDING_SPC=NULL WHERE TASK_ID=?");
                pst.setInt(1, parAdverseCommunicationDetail.getTaskId());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void savecustodianremarksAdversesiPAR(ParAdverseCommunicationDetail parAdverseCommunicationDetail) {
        String diskfileName = null;
        PreparedStatement pst = null;
        Connection con = null;
        String originalFileName = null;
        String contentType = null;
        ResultSet rs = null;
        String toempId = null;
        String toSpc = null;
        String mobileno = null;
        try {
            con = dataSource.getConnection();
            String tauthoritytype = parAdverseCommunicationDetail.getAuthoritytype();
            //String authoritytype = tauthoritytype.substring(0, tauthoritytype.lastIndexOf("-"));
            //String authid1 = tauthoritytype.substring(tauthoritytype.lastIndexOf("-"));
            String[] authoritytypeArr = tauthoritytype.split("-");
            String authoritytype = authoritytypeArr[0];
            String authid1 = authoritytypeArr[1];

            Parauthorityhelperbean phelper = getParAuthorityForAdverse(authoritytype, Integer.parseInt(authid1));
            toempId = phelper.getAuthorityempid();
            toSpc = phelper.getAuthorityspc();

            if (!parAdverseCommunicationDetail.getUploadDocument().isEmpty()) {
                diskfileName = new Date().getTime() + "";
                originalFileName = parAdverseCommunicationDetail.getUploadDocument().getOriginalFilename();
                contentType = parAdverseCommunicationDetail.getUploadDocument().getContentType();
                byte[] bytes = parAdverseCommunicationDetail.getUploadDocument().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + diskfileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }
            pst = con.prepareStatement("SELECT mobile FROM EMP_MAST WHERE emp_id=?");
            pst.setString(1, toempId);
            rs = pst.executeQuery();
            if (rs.next()) {
                parAdverseCommunicationDetail.setMobileNo(rs.getString("mobile"));
                mobileno = parAdverseCommunicationDetail.getMobileNo();
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            Long curtime = new Date().getTime();

            pst = con.prepareStatement("INSERT INTO par_adverse_communication(parid,from_emp_id,from_spc,to_emp_id,to_spc,remarks,disk_file_name,org_file_name,file_type,remarks_on_date,file_path,communication_date,from_auth_type,to_auth_type,PAR_TYPE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, parAdverseCommunicationDetail.getParId());
            pst.setString(2, parAdverseCommunicationDetail.getFromempId());
            pst.setString(3, parAdverseCommunicationDetail.getFromspc());
            pst.setString(4, toempId);
            pst.setString(5, toSpc);
            pst.setString(6, parAdverseCommunicationDetail.getRemarksdetail());
            pst.setString(7, diskfileName);
            pst.setString(8, originalFileName);
            pst.setString(9, contentType);
            pst.setTimestamp(10, new Timestamp(curtime));
            pst.setString(11, this.uploadPath);
            pst.setTimestamp(12, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(13, parAdverseCommunicationDetail.getFromAuthType());
            pst.setString(14, parAdverseCommunicationDetail.getToAuthType());
            pst.setString(15, "SiPAR");
            pst.executeUpdate();

            rs = pst.getGeneratedKeys();
            int communicationId = 0;
            if (rs.next()) {
                communicationId = rs.getInt("communication_id");
            }
            String msg = "Substantiation on Representation of Authority  " + toempId + "  against Adverse Remark may be please submitted within 15 days.";
            if (mobileno != null && !mobileno.equals("")) {
                new SMSServices(mobileno, msg, "1407162814779581969");
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            int taskId = 0;
            pst = con.prepareStatement("SELECT task_id FROM par_adverse_communication WHERE parid=?");
            pst.setInt(1, parAdverseCommunicationDetail.getParId());
            rs = pst.executeQuery();
            if (rs.next()) {
                taskId = rs.getInt("task_id");
                if (taskId == 0) {
                    pst = con.prepareStatement("INSERT INTO TASK_MASTER(PROCESS_ID, INITIATED_BY, INITIATED_ON, STATUS_ID, PENDING_AT,INITIATED_SPC,PENDING_SPC,ref_id) Values (?,?,?,?,?,?,?,?)");
                    pst.setInt(1, 26);//In g_workflow_process for  ADVERSE SIPAR
                    pst.setString(2, parAdverseCommunicationDetail.getFromempId());
                    pst.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
                    pst.setInt(4, 123);//IN G_PROCESS_STATUS FOR AWAITED REMARKS FROM AUTHORITY SIPAR
                    pst.setString(5, toempId);
                    pst.setString(6, parAdverseCommunicationDetail.getFromspc());
                    pst.setString(7, toSpc);
                    pst.setInt(8, communicationId);
                    pst.executeUpdate();

                    DataBaseFunctions.closeSqlObjects(pst);

                    pst = con.prepareStatement("UPDATE PAR_MASTER SET adverse_comm_status_id=123 WHERE parid=?");
                    pst.setInt(1, parAdverseCommunicationDetail.getParId());
                    pst.executeUpdate();

                    DataBaseFunctions.closeSqlObjects(pst);
                } else {
                    pst = con.prepareStatement("UPDATE TASK_MASTER SET PROCESS_ID=?, INITIATED_BY=?, INITIATED_ON=?, STATUS_ID = ?, PENDING_AT =?,INITIATED_SPC =?,PENDING_SPC =?,ref_id =? where task_id=?");
                    pst.setInt(1, 26);//In g_workflow_process for  ADVERSE SIPAR
                    pst.setString(2, parAdverseCommunicationDetail.getFromempId());
                    pst.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
                    pst.setInt(4, 123);//IN G_PROCESS_STATUS FOR AWAITED REMARKS FROM AUTHORITY SIPAR
                    pst.setString(5, toempId);
                    pst.setString(6, parAdverseCommunicationDetail.getFromspc());
                    pst.setString(7, toSpc);
                    pst.setInt(8, communicationId);
                    pst.setInt(9, taskId);
                    pst.executeUpdate();

                    DataBaseFunctions.closeSqlObjects(pst);

                    pst = con.prepareStatement("UPDATE PAR_MASTER SET adverse_comm_status_id=123 WHERE parid=?");
                    pst.setInt(1, parAdverseCommunicationDetail.getParId());
                    pst.executeUpdate();

                    DataBaseFunctions.closeSqlObjects(pst);

                    pst = con.prepareStatement("UPDATE par_adverse_communication SET task_id=? WHERE communication_id=?");
                    pst.setInt(1, taskId);
                    pst.setInt(2, communicationId);
                    pst.executeUpdate();

                    DataBaseFunctions.closeSqlObjects(pst);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveparAdverseCommunicationReplySiPAR(ParAdverseCommunicationDetail parAdverseCommunicationDetail) {
        String diskfileName = null;
        PreparedStatement pst = null;
        Connection con = null;
        String originalFileName = null;
        String contentType = null;
        ResultSet rs = null;
        String mobileno = null;
        try {
            con = dataSource.getConnection();

            if (!parAdverseCommunicationDetail.getUploadDocument().isEmpty()) {
                diskfileName = new Date().getTime() + "";
                originalFileName = parAdverseCommunicationDetail.getUploadDocument().getOriginalFilename();
                contentType = parAdverseCommunicationDetail.getUploadDocument().getContentType();
                byte[] bytes = parAdverseCommunicationDetail.getUploadDocument().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + diskfileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            Long curtime = new Date().getTime();

            pst = con.prepareStatement("SELECT mobile FROM EMP_MAST WHERE emp_id=?");
            pst.setString(1, parAdverseCommunicationDetail.getToempId());
            rs = pst.executeQuery();
            if (rs.next()) {
                parAdverseCommunicationDetail.setMobileNo(rs.getString("mobile"));
                mobileno = parAdverseCommunicationDetail.getMobileNo();
            }
            DataBaseFunctions.closeSqlObjects(rs, pst);

            pst = con.prepareStatement("INSERT INTO par_adverse_communication(parid,from_emp_id,from_spc,to_emp_id,to_spc,remarks,disk_file_name,org_file_name,file_type,remarks_on_date,file_path,communication_date,from_auth_type,to_auth_type) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, parAdverseCommunicationDetail.getParId());
            pst.setString(2, parAdverseCommunicationDetail.getFromempId());
            pst.setString(3, parAdverseCommunicationDetail.getFromspc());
            pst.setString(4, parAdverseCommunicationDetail.getToempId());
            pst.setString(5, parAdverseCommunicationDetail.getTospc());
            pst.setString(6, parAdverseCommunicationDetail.getRemarksdetail());
            pst.setString(7, diskfileName);
            pst.setString(8, originalFileName);
            pst.setString(9, contentType);
            pst.setTimestamp(10, new Timestamp(curtime));
            pst.setString(11, this.uploadPath);
            pst.setTimestamp(12, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(13, parAdverseCommunicationDetail.getFromAuthType());
            pst.setString(14, parAdverseCommunicationDetail.getToAuthType());
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            if (parAdverseCommunicationDetail.getAdverseCommunicationStatusId() == 121) {
                if (mobileno != null && !mobileno.equals("")) {
                    String msg = "Representation Of Appraisee " + parAdverseCommunicationDetail.getToempId() + " on Adverse Remark is submitted by him/her.";
                    new SMSServices(mobileno, msg, "1407162814770295534");
                }
            } else if (parAdverseCommunicationDetail.getAdverseCommunicationStatusId() == 123) {
                if (mobileno != null && !mobileno.equals("")) {
                    String msg = "Substantiation Reply Of Appraisee " + parAdverseCommunicationDetail.getToempId() + " on Adverse Remark is submitted by him/her.";
                    new SMSServices(mobileno, msg, "1407162814786707636");
                }
            }

            pst = con.prepareStatement("UPDATE PAR_MASTER SET adverse_comm_status_id=? WHERE parid=?");//102
            pst.setInt(1, parAdverseCommunicationDetail.getAdverseCommunicationStatusId());
            pst.setInt(2, parAdverseCommunicationDetail.getParId());
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=NULL,PENDING_SPC=NULL,status_id=? WHERE TASK_ID=?");
            pst.setInt(1, parAdverseCommunicationDetail.getAdverseCommunicationStatusId());
            pst.setInt(2, parAdverseCommunicationDetail.getTaskId());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ParAdverseCommunicationDetail getCommunicationDetailsSiPAR(int communicationId) {
        ParAdverseCommunicationDetail parAdverseCommunicationDetail = new ParAdverseCommunicationDetail();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select communication_id,parid,getspn(par_adverse_communication.from_spc) as from_post,get_empname_from_type(par_adverse_communication.from_emp_id ,'G') as from_empname,"
                    + " get_empame_adversepar_si(par_adverse_communication.from_emp_id ,'B') as from_empname_custodian,get_empame_adversepar_si(par_adverse_communication.to_emp_id ,'B') as to_empname_custodian,"
                    + " from_auth_type,to_auth_type,from_emp_id,from_spc,org_file_name,remarks,get_empname_from_type(par_adverse_communication.to_emp_id ,'G') as to_empname, to_emp_id,to_spc from par_adverse_communication where communication_id =?");
            pst.setInt(1, communicationId);
            rs = pst.executeQuery();
            if (rs.next()) {
                parAdverseCommunicationDetail.setCommunicationId(rs.getInt("communication_id"));
                parAdverseCommunicationDetail.setParId(rs.getInt("parid"));
                parAdverseCommunicationDetail.setFromPost(rs.getString("from_post"));
                parAdverseCommunicationDetail.setFromempId(rs.getString("from_emp_id"));
                parAdverseCommunicationDetail.setFromspc(rs.getString("from_spc"));
                parAdverseCommunicationDetail.setOriginalFilename(rs.getString("org_file_name"));
                parAdverseCommunicationDetail.setRemarksdetail(rs.getString("remarks"));
                parAdverseCommunicationDetail.setToempId(rs.getString("to_emp_id"));
                parAdverseCommunicationDetail.setTospc(rs.getString("to_spc"));
                parAdverseCommunicationDetail.setFromAuthType(rs.getString("from_auth_type"));
                parAdverseCommunicationDetail.setToAuthType(rs.getString("to_auth_type"));
                if (rs.getString("from_auth_type").equals("Custodian")) {
                    parAdverseCommunicationDetail.setFromempName(rs.getString("from_empname_custodian"));
                } else {
                    parAdverseCommunicationDetail.setFromempName(rs.getString("from_empname"));
                }
                if (rs.getString("to_auth_type").equals("Custodian")) {
                    parAdverseCommunicationDetail.setToempName(rs.getString("to_empname_custodian"));
                } else {
                    parAdverseCommunicationDetail.setToempName(rs.getString("to_empname"));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parAdverseCommunicationDetail;
    }

    @Override
    public List getSiEqivAllOfficerDataList(String custodianOffCode) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet res = null;
        List employeeList = new ArrayList();

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select emp_id,gpf_no,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,"
                    + "dob,mobile,cur_spc,post,deploy_type,post_grp_type,emp_mast.cur_cadre_code,gc.cadre_name from emp_mast "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "INNER JOIN g_deploy_type on emp_mast.dep_code= g_deploy_type.deploy_code "
                    + "left outer join g_cadre gc on emp_mast.cur_cadre_code = gc.cadre_code "
                    + "where emp_mast.cur_off_code = ? and dep_code NOT IN('00', '08', '09', '10', '13', '14') "
                    + "and G_POST.department_code='14' and (post like 'SUB INSPECTOR%' OR post like 'S.I%' or post like 'SUB-INSPECTOR%' "
                    + "or post like 'ASSISTANT SECTION OFFICER%' or post like 'SUPERINTENDENT ISSUE%' or post like 'SENIOR GRADE STENOGRAPHER%' "
                    + "or post like 'SENIOR STENOGRAPHER' or post like 'STENO SUB INSPECTOR%' or post like 'DEPUTY SUBEDAR%') order by F_NAME");
            pst.setString(1, custodianOffCode);
            res = pst.executeQuery();
            while (res.next()) {

                ParAdminProperties parAdminProperties = new ParAdminProperties();
                parAdminProperties.setEmpId(res.getString("EMP_ID"));
                parAdminProperties.setGpfno(res.getString("GPF_NO"));
                parAdminProperties.setEmpName(res.getString("EMPNAME"));
                parAdminProperties.setDob(CommonFunctions.getFormattedOutputDate6(res.getDate("DOB")));
                parAdminProperties.setMobile(StringUtils.defaultString(res.getString("MOBILE"), ""));
                parAdminProperties.setPostName(StringUtils.defaultString(res.getString("POST"), ""));
                parAdminProperties.setGroupName(StringUtils.defaultString(res.getString("post_grp_type"), ""));
                parAdminProperties.setCadreName(res.getString("cadre_name"));

                employeeList.add(parAdminProperties);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return employeeList;
    }

    @Override
    public ParAdverseCommunicationDetail getSiParAdverseRemarkIntimationPdfData(String custEmpId, int parid) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet res = null;
        ParAdverseCommunicationDetail empData = new ParAdverseCommunicationDetail();

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select pm.parid,pm.emp_id,fiscal_year,period_from,period_to,off_code,pm.task_id,"
                    + "si_officer_type,place_of_posting,gpf_no,f_name,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,"
                    + "mobile,cur_off_code,pac.remarks,remarks_on_date from par_master pm  "
                    + "inner join emp_mast em on pm.emp_id = em.emp_id "
                    + "inner join par_adverse_communication pac on pac.parid = pm.parid "
                    + "where pm.parid=? and pm.par_type='SiPar' and pac.from_emp_id = ?");
            pst.setInt(1, parid);
            pst.setString(2, custEmpId);
            res = pst.executeQuery();
            if (res.next()) {

                int parId = res.getInt("parid");
                empData.setParId(parId);
                empData.setEncParId(CommonFunctions.encodedTxt(parId + ""));
                empData.setToempId(res.getString("emp_id"));
                empData.setFiscalYear(res.getString("fiscal_year"));
                empData.setParPeriodFrom(CommonFunctions.getFormattedOutputDate6(res.getDate("period_from")));
                empData.setParPeriodTo(CommonFunctions.getFormattedOutputDate6(res.getDate("period_to")));
                String siType = res.getString("si_officer_type");
                if (siType.equals("Si1")) {
                    empData.setSiType("Sub Inspector (Civil)");
                } else if (siType.equals("Si2")) {
                    empData.setSiType("Sub Inspector (Armed)");
                } else if (siType.equals("Si3")) {
                    empData.setSiType("Sub Inspector (Equivalent)");
                } else {
                    empData.setSiType("");
                }
                empData.setAppraisePlaceOfPosting(res.getString("place_of_posting"));
                empData.setAppraiseGpfNo(res.getString("gpf_no"));
                empData.setAppraiseFname(res.getString("f_name"));
                empData.setMobileNo(StringUtils.defaultString(res.getString("mobile"), ""));
                empData.setToempName(res.getString("EMP_NAME"));
                empData.setRemarksdetail(res.getString("remarks"));
                empData.setRemarksOnDate(CommonFunctions.getFormattedOutputDate6(res.getDate("remarks_on_date")));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empData;
    }

    @Autowired
    public List getSiPARNotInitiatedOfficerList(String custodianOffCode, String fiscalyear) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet res = null;
        List employeeListNotInitiated = new ArrayList();

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select empmastid as emp_id,G_POST.post,CADRE_NAME,off_en,GPF_NO,DOB, "
                    + "ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME ,MOBILE,post_grp_type from "
                    + "(select emp_mast.emp_id as empmastid,par_master.emp_id as parmastid,cur_off_code,cur_SPC,cadre_code,FISCAL_YEAR,GPF_NO,DOB, "
                    + "INITIALS,F_NAME,M_NAME,L_NAME,EMP_MAST.MOBILE,post_grp_type from "
                    + "(select emp_id,INITIALS,f_name,M_NAME,L_NAME,EMP_MAST.MOBILE,cur_off_code,cur_SPC,CADRE_CODE,GPF_NO,DOB,post_grp_type from emp_mast where  "
                    + " dep_code ='02' and CUR_OFF_CODE=?)  as emp_mast "
                    + "left outer join "
                    + "(select emp_id,FISCAL_YEAR from par_master where fiscal_year=?) as par_master "
                    + "on emp_mast.emp_id = par_master.emp_id)as t1 "
                    + "INNER JOIN hrmis2.G_SPC ON t1.cur_SPC=G_SPC.SPC "
                    + "INNER JOIN (select * from G_POST where (post like 'SUB INSPECTOR%' OR post like 'S.I%' or post like 'SUB-INSPECTOR%' "
                    + "or post like 'ASSISTANT SECTION OFFICER%' or post like 'SUPERINTENDENT ISSUE%' or post like 'SENIOR GRADE STENOGRAPHER%' "
                    + "or post like 'DEPUTY SUBEDAR%'))as G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                    + "LEFT OUTER JOIN (select * from g_cadre) G_CADRE ON t1.cadre_code=G_CADRE.CADRE_CODE "
                    + "LEFT OUTER JOIN G_OFFICE ON t1.CUR_OFF_CODE=G_OFFICE.OFF_CODE "
                    + "where parmastid is null order by F_NAME");
            pst.setString(1, custodianOffCode);
            pst.setString(2, fiscalyear);
            res = pst.executeQuery();
            while (res.next()) {
                ParAdminProperties parAdminProperties = new ParAdminProperties();
                parAdminProperties.setEmpId(res.getString("EMP_ID"));
                parAdminProperties.setGpfno(res.getString("GPF_NO"));
                parAdminProperties.setEmpName(res.getString("EMPNAME"));
                parAdminProperties.setDob(CommonFunctions.getFormattedOutputDate6(res.getDate("DOB")));
                parAdminProperties.setMobile(StringUtils.defaultString(res.getString("MOBILE"), ""));
                parAdminProperties.setPostName(StringUtils.defaultString(res.getString("POST"), ""));
                parAdminProperties.setGroupName(StringUtils.defaultString(res.getString("post_grp_type"), ""));
                parAdminProperties.setCurrentOfficeName(res.getString("off_en"));
                employeeListNotInitiated.add(parAdminProperties);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return employeeListNotInitiated;
    }

    @Override
    public void saveAdverseRemarksDetailAtCustodianSIPAR(ParAdverseCommunicationDetail parAdverseCommunicationDetail) {
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            Long curtime = new Date().getTime();

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

            if (parAdverseCommunicationDetail.getDetailIdForCustodianEntry() == 0) {
                pst = con.prepareStatement("INSERT INTO par_adverse_appraise_detail_sipar(custodian_fullname,custodian_designation,custodian_address, "
                        + "fiscal_year,custodian_remarks_detail,final_grading,communication_on_date,appraise_name,appraise_hrmsid,appraise_mobile,"
                        + "appraise_posting_detail,web_address,web_site,pin_code,adverse_remarks_entry_by,adverse_remarks_entry_on,is_active,parid,"
                        + "tel_fax_no,custodian_empid) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

                pst.setString(1, parAdverseCommunicationDetail.getCustodianFullName());
                pst.setString(2, parAdverseCommunicationDetail.getCustodianDesignation());
                pst.setString(3, parAdverseCommunicationDetail.getCustodianAddress());
                pst.setString(4, parAdverseCommunicationDetail.getFiscalYear());
                pst.setString(5, parAdverseCommunicationDetail.getRemarksdetail());
                pst.setString(6, parAdverseCommunicationDetail.getFinalGrading());
                pst.setTimestamp(7, new Timestamp(sdf.parse(parAdverseCommunicationDetail.getAdverseCommunicationOnDate()).getTime()));
                pst.setString(8, parAdverseCommunicationDetail.getAppraiseFname());
                pst.setString(9, parAdverseCommunicationDetail.getAppraiseHrmsId());
                pst.setString(10, parAdverseCommunicationDetail.getMobileNo());
                pst.setString(11, parAdverseCommunicationDetail.getAppraisePostingDetail());
                pst.setString(12, parAdverseCommunicationDetail.getWebAddress());
                pst.setString(13, parAdverseCommunicationDetail.getWebSite());
                pst.setString(14, parAdverseCommunicationDetail.getPinCode());
                pst.setString(15, parAdverseCommunicationDetail.getCustodianLoginId());
                pst.setTimestamp(16, new Timestamp(curtime));
                pst.setString(17, "Y");
                pst.setInt(18, parAdverseCommunicationDetail.getParId());
                pst.setString(19, parAdverseCommunicationDetail.getTelFaxNo());
                pst.setString(20, parAdverseCommunicationDetail.getCustodianEmpId());
                pst.executeUpdate();

            } else {
                pst = con.prepareStatement("UPDATE par_adverse_appraise_detail_sipar set custodian_fullname=?,custodian_designation=?,"
                        + "custodian_address=?,fiscal_year=?,custodian_remarks_detail=?,final_grading=?,communication_on_date=?,appraise_name=?,"
                        + "appraise_hrmsid=?,appraise_mobile=?,appraise_posting_detail=?,web_address=?,web_site=?,pin_code=?,"
                        + "adverse_remarks_entry_by=?,tel_fax_no=?,custodian_empid=? where parid=?");

                pst.setString(1, parAdverseCommunicationDetail.getCustodianFullName());
                pst.setString(2, parAdverseCommunicationDetail.getCustodianDesignation());
                pst.setString(3, parAdverseCommunicationDetail.getCustodianAddress());
                pst.setString(4, parAdverseCommunicationDetail.getFiscalYear());
                pst.setString(5, parAdverseCommunicationDetail.getRemarksdetail());
                pst.setString(6, parAdverseCommunicationDetail.getFinalGrading());
                pst.setTimestamp(7, new Timestamp(sdf.parse(parAdverseCommunicationDetail.getAdverseCommunicationOnDate()).getTime()));
                pst.setString(8, parAdverseCommunicationDetail.getAppraiseFname());
                pst.setString(9, parAdverseCommunicationDetail.getAppraiseHrmsId());
                pst.setString(10, parAdverseCommunicationDetail.getMobileNo());
                pst.setString(11, parAdverseCommunicationDetail.getAppraisePostingDetail());
                pst.setString(12, parAdverseCommunicationDetail.getWebAddress());
                pst.setString(13, parAdverseCommunicationDetail.getWebSite());
                pst.setString(14, parAdverseCommunicationDetail.getPinCode());
                pst.setString(15, parAdverseCommunicationDetail.getCustodianLoginId());
                pst.setString(16, parAdverseCommunicationDetail.getTelFaxNo());
                pst.setString(17, parAdverseCommunicationDetail.getCustodianEmpId());
                pst.setInt(18, parAdverseCommunicationDetail.getParId());
                pst.executeUpdate();
            }

            DataBaseFunctions.closeSqlObjects(pst);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getAdverseRemarksDetailListAtCustodianSIPAR(int parid) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList custodianEntryList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select * from par_adverse_appraise_detail_sipar where parid =? and is_active='Y'");
            pst.setInt(1, parid);
            rs = pst.executeQuery();
            if (rs.next()) {
                ParAdverseCommunicationDetail parAdverseCommunicationDetail = new ParAdverseCommunicationDetail();
                parAdverseCommunicationDetail.setCustodianFullName(rs.getString("custodian_fullname"));
                parAdverseCommunicationDetail.setCustodianDesignation(rs.getString("custodian_designation"));
                parAdverseCommunicationDetail.setCustodianAddress(rs.getString("custodian_address"));
                parAdverseCommunicationDetail.setFiscalYear(rs.getString("fiscal_year"));
                parAdverseCommunicationDetail.setRemarksdetail(rs.getString("custodian_remarks_detail"));
                parAdverseCommunicationDetail.setFinalGrading(rs.getString("final_grading"));
                parAdverseCommunicationDetail.setAdverseCommunicationOnDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("communication_on_date")));
                parAdverseCommunicationDetail.setAppraiseFname(rs.getString("appraise_name"));
                parAdverseCommunicationDetail.setAppraiseHrmsId(rs.getString("appraise_hrmsid"));
                parAdverseCommunicationDetail.setMobileNo(rs.getString("appraise_mobile"));
                parAdverseCommunicationDetail.setAppraisePostingDetail(rs.getString("appraise_posting_detail"));
                parAdverseCommunicationDetail.setWebAddress(rs.getString("web_address"));
                parAdverseCommunicationDetail.setWebSite(rs.getString("web_site"));
                parAdverseCommunicationDetail.setPinCode(rs.getString("pin_code"));
                parAdverseCommunicationDetail.setCustodianEmpId(rs.getString("custodian_empid"));
                parAdverseCommunicationDetail.setParId(rs.getInt("parid"));
                parAdverseCommunicationDetail.setEncParId(CommonFunctions.encodedTxt(rs.getInt("parid") + ""));
                parAdverseCommunicationDetail.setDetailIdForCustodianEntry(rs.getInt("detail_id"));
                custodianEntryList.add(parAdverseCommunicationDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return custodianEntryList;
    }

    @Override
    public ParAdverseCommunicationDetail getAdverseRemarksDetailAtCustodianSIPAR(int parid) {
        ParAdverseCommunicationDetail parAdverseCommunicationDetail = new ParAdverseCommunicationDetail();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select * from par_adverse_appraise_detail_sipar where parid =? and is_active = 'Y'");
            pst.setInt(1, parid);
            rs = pst.executeQuery();
            if (rs.next()) {
                parAdverseCommunicationDetail.setCustodianFullName(rs.getString("custodian_fullname"));
                parAdverseCommunicationDetail.setCustodianDesignation(rs.getString("custodian_designation"));
                parAdverseCommunicationDetail.setCustodianAddress(rs.getString("custodian_address"));
                parAdverseCommunicationDetail.setFiscalYear(rs.getString("fiscal_year"));
                parAdverseCommunicationDetail.setRemarksdetail(rs.getString("custodian_remarks_detail"));
                parAdverseCommunicationDetail.setFinalGrading(rs.getString("final_grading"));
                parAdverseCommunicationDetail.setAdverseCommunicationOnDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("communication_on_date")));
                parAdverseCommunicationDetail.setAppraiseFname(rs.getString("appraise_name"));
                parAdverseCommunicationDetail.setAppraiseHrmsId(rs.getString("appraise_hrmsid"));
                parAdverseCommunicationDetail.setMobileNo(rs.getString("appraise_mobile"));
                parAdverseCommunicationDetail.setAppraisePostingDetail(rs.getString("appraise_posting_detail"));
                parAdverseCommunicationDetail.setWebAddress(rs.getString("web_address"));
                parAdverseCommunicationDetail.setWebSite(rs.getString("web_site"));
                parAdverseCommunicationDetail.setPinCode(rs.getString("pin_code"));
                parAdverseCommunicationDetail.setTelFaxNo(rs.getString("tel_fax_no"));
                parAdverseCommunicationDetail.setCustodianEmpId(rs.getString("custodian_empid"));
                parAdverseCommunicationDetail.setDetailIdForCustodianEntry(rs.getInt("detail_id"));

            }
            parAdverseCommunicationDetail.setParId(parid);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parAdverseCommunicationDetail;
    }

    @Override
    public void getAppraiseDetailForAdverseAtCustodianSiPAR(ParAdverseCommunicationDetail parAdverseCommunicationDetail) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT par_master.emp_id,par_master.spc,ARRAY_TO_STRING(ARRAY[F_NAME, M_NAME,L_NAME], ' ') EMPNAME, post ,cur_spc,mobile from par_master \n"
                    + "INNER JOIN emp_mast ON emp_mast.emp_id = par_master.emp_id "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "where parid =? ");
            pstmt.setInt(1, parAdverseCommunicationDetail.getParId());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                parAdverseCommunicationDetail.setAppraiseHrmsId(rs.getString("emp_id"));
                parAdverseCommunicationDetail.setTospc(rs.getString("spc"));
                parAdverseCommunicationDetail.setAppraiseFname(rs.getString("EMPNAME"));
                parAdverseCommunicationDetail.setMobileNo(rs.getString("mobile"));
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void UpdatePARViewDetailLog(PARViewDetailLogBean pARViewDetailLogBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        ResultSet res = null;
        try {
            con = dataSource.getConnection();

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            Long curtime = new Date().getTime();

            //String IP = CommonFunctions.getISPIPAddress();
            pstmt = con.prepareStatement("insert into  par_view_log (login_id,par_id,applicant_empid,loginby_ip,viewby_id,view_ondate,viewed_by_usertype,view_of_financial_year,applicant_cadre_code,applicant_off_code,applicant_spc,login_spc) values (?,?,?,?,?,?,?,?,?,?,?,?)");

            pstmt1 = con.prepareStatement("select parid,emp_id,fiscal_year,spc,cadre_code,off_code from par_master where parid=?");
            pstmt1.setInt(1, pARViewDetailLogBean.getParId());
            res = pstmt1.executeQuery();

            while (res.next()) {
                PARViewDetailLogBean pffb = new PARViewDetailLogBean();
                pffb.setApplicantEmpId(res.getString("emp_id"));
                pffb.setParId(res.getInt("parid"));
                pffb.setFiscalyear(res.getString("fiscal_year"));
                pffb.setApplicantSpc(res.getString("spc"));
                pffb.setApplicantCadreCode(res.getString("cadre_code"));
                pffb.setApplicantOffCode(res.getString("off_code"));

                pstmt.setString(1, pARViewDetailLogBean.getLoginById());
                pstmt.setInt(2, pARViewDetailLogBean.getParId());
                pstmt.setString(3, pffb.getApplicantEmpId());
                pstmt.setString(4, pARViewDetailLogBean.getLoginByIp());
                pstmt.setString(5, pARViewDetailLogBean.getViewedById());
                pstmt.setTimestamp(6, new Timestamp(dateFormat.parse(startTime).getTime()));
                pstmt.setString(7, pARViewDetailLogBean.getViewedByUserType());
                pstmt.setString(8, pffb.getFiscalyear());
                pstmt.setString(9, pffb.getApplicantCadreCode());
                pstmt.setString(10, pffb.getApplicantOffCode());
                pstmt.setString(11, pffb.getApplicantSpc());
                pstmt.setString(12, pffb.getLoginBySpc());
                pstmt.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getPARViewDetailLogList(String fiscalyear) {
        Connection con = null;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs = null;
        ArrayList parListViewed = new ArrayList();
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("select login_id,par_id,applicant_empid,loginby_ip,viewby_id,view_ondate,viewed_by_usertype,view_of_financial_year, "
                    + "ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,post,off_en,G_CADRE.cadre_name,get_empname_from_type(par_view_log.login_id,'G') as viewed_by_empname,getspn(par_view_log.login_spc) as viewed_by_emppost "
                    + "from par_view_log "
                    + "inner join emp_mast on par_view_log.applicant_empid = emp_mast.emp_id "
                    + "INNER JOIN G_SPC ON par_view_log.applicant_spc = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "INNER JOIN g_office ON par_view_log.applicant_off_code = g_office.off_code "
                    + "INNER JOIN G_CADRE ON par_view_log.applicant_cadre_code=G_CADRE.CADRE_CODE where view_of_financial_year=? and login_id !='97000072'");

            pst.setString(1, fiscalyear);
            //pst.setString(2, reportingEmpId);
            rs = pst.executeQuery();
            while (rs.next()) {
                PARViewDetailLogBean pARViewDetailLogBean = new PARViewDetailLogBean();
                pARViewDetailLogBean.setLoginById(rs.getString("login_id"));
                pARViewDetailLogBean.setParId(rs.getInt("par_id"));
                pARViewDetailLogBean.setApplicantEmpId(rs.getString("applicant_empid"));
                pARViewDetailLogBean.setLoginByIp(rs.getString("loginby_ip"));
                pARViewDetailLogBean.setViewedById(rs.getString("viewby_id"));
                if (rs.getString("viewed_by_usertype") != null && !rs.getString("viewed_by_usertype").equals("") && rs.getString("viewed_by_usertype").equals("G")) {
                    pARViewDetailLogBean.setViewedByUserType("PAR CUSTODIAN");
                } else if (rs.getString("viewed_by_usertype") != null && !rs.getString("viewed_by_usertype").equals("") && rs.getString("viewed_by_usertype").equals("A")) {
                    pARViewDetailLogBean.setViewedByUserType("PAR ADMIN");
                }
                pARViewDetailLogBean.setFiscalyear(rs.getString("view_of_financial_year"));
                pARViewDetailLogBean.setApplicantName(rs.getString("EMPNAME"));
                pARViewDetailLogBean.setApplicantPost(rs.getString("post"));
                pARViewDetailLogBean.setApplicantOfficeName(rs.getString("off_en"));
                pARViewDetailLogBean.setApplicantCadreName(rs.getString("cadre_name"));
                pARViewDetailLogBean.setViewedOnDate(CommonFunctions.getFormattedOutputDate6(rs.getDate("view_ondate")));
                if (rs.getString("viewed_by_empname") != null && !rs.getString("viewed_by_empname").equals("")) {
                    pARViewDetailLogBean.setViewedByUserName(rs.getString("viewed_by_empname"));
                } else {
                    pARViewDetailLogBean.setViewedByUserName("");
                }
                if (rs.getString("viewed_by_emppost") != null && !rs.getString("viewed_by_emppost").equals("")) {
                    pARViewDetailLogBean.setViewedByUserPost(rs.getString("viewed_by_emppost"));
                } else {
                    pARViewDetailLogBean.setViewedByUserPost("");
                }
                parListViewed.add(pARViewDetailLogBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parListViewed;
    }

    @Override
    public String isPendingReportingAuthority(String empid, int taskid) {

        Statement st = null;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        String isPending = "N";
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            String sql = "SELECT STATUS_ID FROM TASK_MASTER WHERE PENDING_AT='" + empid + "' AND TASK_ID='" + taskid + "'";
            rs = st.executeQuery(sql);
            if (rs.next()) {
                if (rs.getInt("STATUS_ID") == 6) {
                    isPending = "Y";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isPending;
    }

}

package hrms.dao.report.substantivepostlist;

import hrms.common.DataBaseFunctions;
import hrms.model.report.officewisepostgroup.OfficeWisePostGroup;
import hrms.model.report.substantivepost.SubstantivePost;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class SubstantivePostListImpl implements SubstantivePostListDAO{

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getSubstantivePostList(String offCode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList postList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("select POST_LEVEL,POST_SL,IFUCLEAN,IF_ABOLISHED,SANCTIONING_SPC,ORDER_NO,ORDER_DATE,POST_TYPE,POST,POST_GROUP_TYPE,INITIALS||' '||F_NAME||' '||M_NAME||' '||L_NAME as EMPNAME  "
                    + "from(select IFUCLEAN,IF_ABOLISHED,SANCTIONING_SPC,POST,POST_GROUP_TYPE,SPC,ORDER_NO,ORDER_DATE,POST_LEVEL,POST_SL from "
                    + " (select IFUCLEAN,IF_ABOLISHED,SANCTIONING_SPC,GPC,SPC,ORDER_NO,ORDER_DATE,POST_LEVEL,POST_SL from G_SPC where OFF_CODE=?)tab1 inner join G_POST on tab1.GPC=G_POST.POST_CODE)tab2  "
                    + "left outer join EMP_MAST on tab2.SPC=EMP_MAST.CUR_SPC order by POST_LEVEL DESC,POST_SL ,POST");
            pstmt.setString(1, offCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SubstantivePost substantivepost = new SubstantivePost();
                substantivepost.setTxtPost(rs.getString("POST"));
                substantivepost.setGroup(rs.getString("POST_GROUP_TYPE"));
                substantivepost.setPlan(rs.getString("POST_TYPE"));
                substantivepost.setMeninposition(rs.getString("EMPNAME"));
                postList.add(substantivepost);
            }
        } catch (Exception exe) {
            exe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return postList;
    }
}

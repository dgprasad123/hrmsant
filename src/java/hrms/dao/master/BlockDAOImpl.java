package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.Bank;
import hrms.model.master.Block;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class BlockDAOImpl implements BlockDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getAllBlockList() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList blockList = new ArrayList();
        Block block = null;
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT BL_CODE,BL_NAME FROM G_BLOCK ORDER BY BL_NAME");

            rs = st.executeQuery();
            while (rs.next()) {
                block = new Block();
                block.setBlockCode(rs.getString("BL_CODE"));
                block.setBlockName(rs.getString("BL_NAME"));
                blockList.add(block);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return blockList;
    }

    public ArrayList getBlockList(String distCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList blockList = new ArrayList();
        Block block = null;
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT BL_CODE,BL_NAME FROM G_BLOCK WHERE DIST_CODE=? ORDER BY BL_NAME");
            st.setString(1, distCode);
            rs = st.executeQuery();
            while (rs.next()) {
                block = new Block();
                block.setBlockCode(rs.getString("BL_CODE"));
                block.setBlockName(rs.getString("BL_NAME"));
                blockList.add(block);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return blockList;
    }

    @Override
    public Block getBlockDetails(String blockCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        Block block = new Block();
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT BL_CODE,BL_NAME FROM G_BLOCK WHERE BL_CODE=? ");
            st.setString(1, blockCode);
            rs = st.executeQuery();
            if (rs.next()) {
                block.setBlockCode(rs.getString("BL_CODE"));
                block.setBlockName(rs.getString("BL_NAME"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return block;
    }

    @Override
    public Block editBlock(Block block) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();
            if (block.getBlockCode() != null && !block.getBlockCode().equals("")) {
                String sql = "SELECT bl_code,bl_name,gdist.dist_code ,gst.state_code\n"
                        + "FROM (select * from g_block where bl_code=?)g_block\n"
                        + "left outer join g_district gdist on g_block.dist_code=gdist.dist_code\n"
                        + "left outer join g_state gst on gdist.state_code=gst.state_code";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(block.getBlockCode()));
                rs = pst.executeQuery();
                if (rs.next()) {

                    block.setHidblockCode(rs.getString("bl_code"));
                    block.setBlockCode(rs.getString("bl_code"));
                    block.setBlockName(rs.getString("bl_name"));
                    block.setDistCode(rs.getString("dist_code"));
                    block.setStateCode(rs.getString("state_code"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return block;

    }

    @Override
    public void saveNewBlock(Block block) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            int maxblkCode = 0;

            /*pst = con.prepareStatement("SELECT MAX( bl_code ) ::INTEGER +1 maxblkcode FROM g_block where dist_code=?");
             pst.setString(1, block.getHiddistCode());
             rs = pst.executeQuery();                
             if (rs.next()) {
             maxblkCode = rs.getInt("maxblkcode");                 
             }*/
            pst = con.prepareStatement("INSERT INTO g_block(dist_code, bl_name) VALUES(?,?)");
            pst.setString(1, block.getHiddistCode());
            pst.setString(2, block.getBlockName().toUpperCase());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateNewBlock(Block block) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE g_block SET bl_name=? where bl_code=?");
            pst.setString(1, block.getBlockName().toUpperCase());
            if (block.getBlockCode() != null && !block.getBlockCode().equals("")) {
                pst.setInt(2, Integer.parseInt(block.getBlockCode()));
            }
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
}

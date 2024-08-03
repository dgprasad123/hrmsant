/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.ticketAttachment;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.ticketAttachment.TicketAttachment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Surendra
 */
public class TicketAttachMentDAOImpl implements TicketAttachMentDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addAttachDocumentInfo(TicketAttachment attach) {

        Connection con = null;
        PreparedStatement ps = null;
        
        try {
            con = dataSource.getConnection();
            int refId=CommonFunctions.getMaxCode(con, "ticket", "ticket_id");
            ps = con.prepareStatement("INSERT INTO ticket_attachment (attachment_id,o_file_name,d_file_name,ref_id,ref_type,file_path,file_type) VALUES (?,?,?,?,?,?,?)");
            ps.setInt(1, CommonFunctions.getMaxCode(con, "ticket_attachment", "attachment_id"));
            ps.setString(2, attach.getOfileName());
            ps.setString(3, attach.getDfileName());
            ps.setInt(4, refId);
            ps.setString(5, attach.getRefType());
            ps.setString(6, attach.getFilePath());
            ps.setString(7, attach.getFileType());
            ps.execute();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
}

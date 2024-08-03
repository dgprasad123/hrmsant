/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.notification;

/**
 *
 * @author Surendra
 */
public interface MaxNotificationIdDAO {
    public String getMaxNotId();
    public String getMaxCode(String tblname, String fieldname);
}

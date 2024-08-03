/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.notification;

import hrms.model.notification.NotificationBean;

/**
 *
 * @author Surendra
 */
public interface NotificationDAO {

    public int insertNotificationData(NotificationBean nfb);

    public int modifyNotificationData(NotificationBean nfb);

    public NotificationBean dispalyNotificationData(int notid, String nottype);

    public int deleteNotificationData(int notid, String nottype);
    
    public String saveServiceBookLanguage(String sbdescription,int notid,String nottype);
    
    public void updateSupersedeNotificationData(int notid, int linkidnotid);
    
    public void updateCancellationNotificationData(int notid, int linkidnotid);
    
    public int insertNotificationDataSBCorrection(NotificationBean nfb);
    
    public int modifyNotificationDataSBCorrection(NotificationBean nfb);
    
    public int deleteNotificationDataSBCorrection(int notid, String nottype);
}

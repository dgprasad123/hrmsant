/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.service;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author Surendra
 */
@WebListener
public class OnlineUsersCounter implements HttpSessionListener {

    private static int numberOfUsersOnline;

    public OnlineUsersCounter() {
        numberOfUsersOnline = 0;
    }

    public static int getNumberOfUsersOnline() {
        return numberOfUsersOnline;
    }

    public void sessionCreated(HttpSessionEvent event) {

        synchronized (this) {
            numberOfUsersOnline++;
        }

    }

    public void sessionDestroyed(HttpSessionEvent event) {

        synchronized (this) {
            numberOfUsersOnline--;
        }

    }
}

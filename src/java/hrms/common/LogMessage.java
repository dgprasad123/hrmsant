/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.common;

/**
 *
 * @author Manas Jena
 */
public class LogMessage {
    public static String message = "";

    public static String getMessage() {
        return message;
    }

    public static void setMessage(String message) {
        LogMessage.message = LogMessage.message+" <br/> "+message;
    }
    
    public static void reset(){
        LogMessage.message = "";
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.ws;

import hrms.dao.login.LoginDAO;
import hrms.model.login.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
/**
 *
 * @author Manas Jena
 */
@WebService
public class LoginWS {
    @Autowired
    public LoginDAO loginDao;
    
    @WebMethod(exclude=true)
    public void setLoginDao(LoginDAO loginDao) {
        this.loginDao = loginDao;
    }
    
    @WebMethod(operationName = "login")
    public UserDetails login(@WebParam(name = "username")String username,@WebParam(name = "userpassword")String userpassword) {
        return loginDao.checkWSLogin(username,userpassword);
    }
}

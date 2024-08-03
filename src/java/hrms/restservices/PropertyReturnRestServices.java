/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.restservices;

import hrms.dao.propertystatement.PropertyStatementAdminDAO;
import hrms.model.propertystatement.PropertyAdminSearchCriteria;
import hrms.model.propertystatement.PropertySearchResult;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ServletContextAware;

/**
 *
 * @author Manas
 */
@RestController
@RequestMapping("/propertyReturnRestServices")
public class PropertyReturnRestServices implements ServletContextAware {

    @Autowired
    PropertyStatementAdminDAO propertyStatementAdminDAO;
    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }
    
    @ResponseBody
    @RequestMapping(value = "/iprfiledsearch", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object iprfiledsearch(HttpServletResponse response, @RequestBody PropertyAdminSearchCriteria propertyAdminSearchCriteria) throws IOException, JSONException {        
        PropertySearchResult propertySearchResult = propertyStatementAdminDAO.getPropertyStatement(propertyAdminSearchCriteria);
        return propertySearchResult;
    }
}

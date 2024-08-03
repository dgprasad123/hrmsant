/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.restservices;

import hrms.dao.employee.EmployeeDAO;
import hrms.dao.login.LoginDAO;
import hrms.dao.master.CadreDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PostDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.master.SubStantivePostDAOImpl;
import hrms.model.employee.Employee;
import hrms.model.master.Office;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ServletContextAware;

/**
 *
 * @author Surendra
 */
@RestController
@RequestMapping("/officeRestService")
public class OfficePS implements ServletContextAware {

    @Autowired
    public LoginDAO loginDao;

    @Autowired
    public EmployeeDAO employeeDAO;

    @Autowired
    OfficeDAO officeDao;

    @Autowired
    DepartmentDAO departmentDao;

    @Autowired
    PostDAO postDAO;

    @Autowired
    SubStantivePostDAO substantivePostDAO;
    
    @Autowired
    CadreDAO cadreDAO;
    
    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @ResponseBody
    @RequestMapping(value = "/ortpsa/getPostList/{officeCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getPostListOfficeWise(HttpServletResponse response, @PathVariable("officeCode") String officeCode) throws JSONException {

        Office off = officeDao.getOfficeDetails(officeCode);

        List postList = postDAO.getPostList(off.getDeptCode(), officeCode);

        JSONArray arr = new JSONArray(postList);

        JSONObject jobj = new JSONObject();
        jobj.put("postList", arr);
        return jobj.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/ortpsa/getPostWithEmployee/{officeCode}/{gpc}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getPostWithEmployee(HttpServletResponse response, @PathVariable("gpc") String postCode, @PathVariable("officeCode") String officeCode) throws JSONException {

        List spclist = substantivePostDAO.getEmployeeNameWithSPC(officeCode, postCode);
        JSONArray arr = new JSONArray(spclist);

        JSONObject jobj = new JSONObject();
        jobj.put("spcList", arr);
        return jobj.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/departmentlist", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object departmentList(HttpServletResponse response) throws IOException, JSONException {
        List deptlist = departmentDao.getDepartmentList();
        return deptlist;
    }

    @ResponseBody
    @RequestMapping(value = "/departmentWiseCadreList/{deptcode}", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object departmentWiseCadreList(HttpServletResponse response, @PathVariable("deptcode") String deptcode) throws IOException, JSONException {
        List cadrelist = cadreDAO.getCadreList(deptcode);
        return cadrelist;
    }
    
    @ResponseBody
    @RequestMapping(value = "/departmentWiseDDOOfficeList/{deptcode}", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object departmentWiseDDOOfficeList(HttpServletResponse response, @PathVariable("deptcode") String deptcode) throws IOException, JSONException {
        List ddoofficelist = officeDao.getTotalDDOOfficeList(deptcode);
        return ddoofficelist;
    }
    
    @ResponseBody
    @RequestMapping(value = "/departmentWiseTotalOfficeList/{deptcode}", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object departmentWiseTotalOfficeList(HttpServletResponse response, @PathVariable("deptcode") String deptcode) throws IOException, JSONException {
        List totalofficelist = officeDao.getTotalOfficeList(deptcode);
        return totalofficelist;
    }
    
    //public List getTotalDDOOfficeList(String deptcode)
}

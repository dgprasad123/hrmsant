/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.restservices;

import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.WaterRent;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Manas
 */
@RestController
@RequestMapping("/waterRentRestService")
public class WaterRentPS {

    @Autowired
    ScheduleDAO comonScheduleDao;

    @ResponseBody
    @RequestMapping(value = "/schedule/watertax/{calcyear}/{calcmonth}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getWaterRentData(HttpServletResponse response, @PathVariable("calcyear") int calcyear, @PathVariable("calcmonth") int calcmonth) throws JSONException {
        WaterRent waterRent[] = comonScheduleDao.getWaterRentData(calcmonth,calcyear);        
        JSONArray arr = new JSONArray(waterRent);
        JSONObject jobj = new JSONObject();
        jobj.put("postList", arr);
        return jobj.toString();
    }

}

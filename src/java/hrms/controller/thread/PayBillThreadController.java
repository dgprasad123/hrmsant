/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.thread;

import hrms.model.login.LoginUserBean;
import hrms.thread.paybill.CallableRegularPayBill;
import hrms.thread.paybill.ThreadProcessBillIntoQueue;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas
 */
@Controller
public class PayBillThreadController {

    @Autowired
    public CallableRegularPayBill callableRegularPayBill;

    @Autowired
    public ThreadProcessBillIntoQueue threadProcessBillIntoQueue;

    @RequestMapping(value = "runPayBillThread", method = RequestMethod.GET)
    public ModelAndView runPayBillThread(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws IOException {
        if (callableRegularPayBill.getThreadStatus() == 0) {
            Thread t = new Thread(callableRegularPayBill);
            t.start();
        }
        ModelAndView mv = new ModelAndView("redirect:/getThreadServiceList.htm");
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "runPayBillThreadStatus")
    public void getThreadStatus(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws Exception {
        response.setContentType("application/json");
        PrintWriter out = null;
        out = response.getWriter();
        JSONObject job = new JSONObject();
        job.append("ThreadStatus", callableRegularPayBill.threadStatus);
        out.write(job.toString());
        out.close();
    }

    @ResponseBody
    @RequestMapping(value = "processbillsIntoQueue", method = {RequestMethod.GET, RequestMethod.POST})
    public void processbillsIntoQueue(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("billgroupId") String billgroupId, @RequestParam("billno") String billno, @RequestParam("processValue") String processValue,
            @RequestParam("billmonth") String billmonth, @RequestParam("billyear") String billyear) throws Exception {
        Calendar cal = Calendar.getInstance();

        response.setContentType("application/json");
        PrintWriter out = null;
        String threadStatus = "N";
        try {
            //System.out.println("billgroupId or Offcode:" + billgroupId +"month&year::"+billmonth+":"+billyear);
            
            out = response.getWriter();
            JSONObject obj = new JSONObject();
            if (processValue.equals("All")) {
                threadProcessBillIntoQueue.setThreadValue(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR), processValue, billgroupId, Integer.parseInt(billno));
            } else {
                threadProcessBillIntoQueue.setThreadValue(Integer.parseInt(billmonth), Integer.parseInt(billyear), processValue, billgroupId, Integer.parseInt(billno));
            }

            Thread t = new Thread(threadProcessBillIntoQueue);
            t.start();
            threadStatus = "Y";
            obj.append("msg", threadStatus);

            out.write(obj.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

}

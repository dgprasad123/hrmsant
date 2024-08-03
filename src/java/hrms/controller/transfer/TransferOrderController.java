/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.transfer;

import hrms.model.login.Users;
import hrms.model.transfer.TransferForm;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class TransferOrderController {

    @RequestMapping(value = "transferOrderList")
    public ModelAndView TransferList(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/transfer/TransferOrderList");
        return mv;
    }
    
    @RequestMapping(value = "transferOrderView")
    public ModelAndView TransferOrderView(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/transfer/TransferOrderView");
        return mv;
    }
    
    @RequestMapping(value = "transferOrderDetail")
    public ModelAndView TransferOrderDetail(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/transfer/TransferOrderDetail");
        return mv;
    }
}

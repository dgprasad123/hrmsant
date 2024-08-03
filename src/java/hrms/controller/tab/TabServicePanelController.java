/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.tab;

import hrms.common.CommonFunctions;
import hrms.dao.employee.EmployeeDAO;
import hrms.dao.login.LoginDAO;
import hrms.dao.tab.ServicePanelDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.tab.RollwiseGroupInfoBean;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Surendra
 */
@Controller

@SessionAttributes({"LoginUserBean", "SelectedEmpObj", "SelectedEmpOffice"})
public class TabServicePanelController {

    @Autowired
    public ServicePanelDAO servicePanelDAO;

    @Autowired
    EmployeeDAO employeeDAO;

    @RequestMapping(value = "getRollWiseLink", method = RequestMethod.GET)
    public ModelAndView getServicePanel(@ModelAttribute("RollwiseGroupInfoBean") RollwiseGroupInfoBean rgi, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            BindingResult result, @RequestParam("rollId") String rollId, @RequestParam("nodeID") String nodeId) {
        ModelAndView mav = new ModelAndView();
        String path = "index";
        String ifEmpSpecific = "";
        ArrayList servicesList = new ArrayList();
        String SelectedEmpOffice = "";
        try {
            System.out.println("nodeid:" + nodeId);

            hrms.model.login.Users selectedEmpObj = new Users();
            mav.addObject("SelectedEmpObj", selectedEmpObj);

            String roll = CommonFunctions.decodedTxt(rollId);

            if (roll != null && (roll.equals("05"))) {
                /*
                 Below code of Block is written for My office Tab 
                 Where Roll Id =05 for My Office Tab
                 */
                if (nodeId != null && !nodeId.equals("")) {
                    if (nodeId.substring(0, 2).equals("PA")) { /* This block will be execute when user will press Parent Node */

                        SelectedEmpOffice = nodeId.substring(2);
                        if (nodeId.length() > 15) {
                            SelectedEmpOffice = CommonFunctions.decodedTxt(SelectedEmpOffice);
                            SelectedEmpOffice = SelectedEmpOffice.substring(0, 13);
                        }
                        mav.addObject("SelectedEmpOffice", SelectedEmpOffice);
                        ifEmpSpecific = "N";
                        servicesList = servicePanelDAO.getRollWiseGrpInfo(roll, lub.getLoginspc(), ifEmpSpecific, false, "NA");
                    } else if (nodeId.substring(0, 2).equals("SO")) { /* This block will be execute when user will press Sub Office Node*/

                        SelectedEmpOffice = nodeId.substring(2);
                        if (nodeId.length() > 15) {
                            SelectedEmpOffice = CommonFunctions.decodedTxt(SelectedEmpOffice);
                            SelectedEmpOffice = SelectedEmpOffice.substring(0, 13);

                        }
                        mav.addObject("SelectedEmpOffice", SelectedEmpOffice);
                        ifEmpSpecific = "N";
                        servicesList = servicePanelDAO.getRollWiseGrpInfo(roll, lub.getLoginspc(), ifEmpSpecific, false, "NA");
                    } else if (nodeId != null && (nodeId.substring(0, 2).equals("RE") || nodeId.substring(0, 2).equals("DE"))) { /* This block will be execute when user will press Regular Employee Node */

                        SelectedEmpOffice = "";
                        if (nodeId.length() > 15) {
                            SelectedEmpOffice = nodeId.substring(3);
                            SelectedEmpOffice = SelectedEmpOffice.substring(0, 13);
                        }

                        mav.addObject("SelectedEmpOffice", SelectedEmpOffice);
                        ifEmpSpecific = "N";
                        servicesList = servicePanelDAO.getRollWiseGrpInfo(roll, lub.getLoginspc(), ifEmpSpecific, false, "R");
                    } else if (nodeId != null && nodeId.substring(0, 2).equals("WO")) { /* This block will be execute when user will press Workcharged Employee Node */

                        SelectedEmpOffice = "";
                        if (nodeId.length() > 15) {

                            SelectedEmpOffice = nodeId.substring(3);
                            SelectedEmpOffice = SelectedEmpOffice.substring(0, 13);
                        }
                        mav.addObject("SelectedEmpOffice", SelectedEmpOffice);
                        ifEmpSpecific = "N";
                        servicesList = servicePanelDAO.getRollWiseGrpInfo(roll, lub.getLoginspc(), ifEmpSpecific, false, "R");
                    } else if (nodeId != null && nodeId.substring(0, 2).equals("WA")) { /* This block will be execute when user will press Wages Employee Node */

                        SelectedEmpOffice = "";
                        if (nodeId.length() > 15) {

                            SelectedEmpOffice = nodeId.substring(3);
                            SelectedEmpOffice = SelectedEmpOffice.substring(0, 13);
                        }
                        mav.addObject("SelectedEmpOffice", SelectedEmpOffice);
                        ifEmpSpecific = "N";
                        servicesList = servicePanelDAO.getRollWiseGrpInfo(roll, lub.getLoginspc(), ifEmpSpecific, false, "R");
                    } else if (nodeId != null && nodeId.substring(0, 2).equals("6C")) { /* This block will be execute when user will press Six Year Contractual Node */

                        SelectedEmpOffice = "";
                        if (nodeId.length() > 15) {
                            SelectedEmpOffice = nodeId.substring(3);
                            SelectedEmpOffice = SelectedEmpOffice.substring(0, 13);
                        }

                        mav.addObject("SelectedEmpOffice", SelectedEmpOffice);
                        ifEmpSpecific = "N";
                        servicesList = servicePanelDAO.getRollWiseGrpInfo(roll, lub.getLoginspc(), ifEmpSpecific, false, "R");
                    } else if (nodeId != null && nodeId.substring(0, 2).equals("CO")) { /* This block will be execute when user will press Contractual Employee Node */

                        SelectedEmpOffice = "";
                        if (nodeId.length() > 15) {

                            SelectedEmpOffice = nodeId.substring(3);
                            SelectedEmpOffice = SelectedEmpOffice.substring(0, 13);
                        }
                        mav.addObject("SelectedEmpOffice", SelectedEmpOffice);
                        ifEmpSpecific = "N";
                        servicesList = servicePanelDAO.getRollWiseGrpInfo(roll, lub.getLoginspc(), ifEmpSpecific, false, "C");
                    } else if (nodeId != null && nodeId.substring(0, 2).equals("XC")) { /* This block will be execute when user will press XCADRE Node */

                        SelectedEmpOffice = "";
                        if (nodeId.length() > 15) {

                            SelectedEmpOffice = nodeId.substring(3);
                            SelectedEmpOffice = SelectedEmpOffice.substring(0, 13);
                        }
                        mav.addObject("SelectedEmpOffice", SelectedEmpOffice);
                        ifEmpSpecific = "N";
                        servicesList = servicePanelDAO.getRollWiseGrpInfo(roll, lub.getLoginspc(), ifEmpSpecific, false, "C");
                    } else if (nodeId != null && nodeId.substring(0, 2).equals("NG")) { /* This block will be execute when user will press Non Govt. Aided Employee Node */

                        System.out.println("NGA");

                        SelectedEmpOffice = "";
                        if (nodeId.length() > 15) {

                            SelectedEmpOffice = nodeId.substring(3);
                            SelectedEmpOffice = SelectedEmpOffice.substring(0, 13);
                        }
                        mav.addObject("SelectedEmpOffice", SelectedEmpOffice);
                        ifEmpSpecific = "N";
                        servicesList = servicePanelDAO.getRollWiseGrpInfo(roll, lub.getLoginspc(), ifEmpSpecific, false, "C");
                    } else if (nodeId != null && nodeId.substring(0, 2).equals("FS")) { // This block will be execute when user will press Deputation Employee Node */ /*System.out.println("Deputation");

                        SelectedEmpOffice = "";
                        if (nodeId.length() > 15) {

                            SelectedEmpOffice = nodeId.substring(3);
                            SelectedEmpOffice = SelectedEmpOffice.substring(0, 13);
                        }
                        mav.addObject("SelectedEmpOffice", SelectedEmpOffice);
                        ifEmpSpecific = "N";
                        servicesList = servicePanelDAO.getRollWiseGrpInfo(roll, lub.getLoginspc(), ifEmpSpecific, false, "F");
                    } else { /* This block will be execute when user will press Employee Node */

                        System.out.println("elseNodeId:" + nodeId);

                        ifEmpSpecific = "Y";
                        String empId = "";
                        String empType = "";

                        /*When User clicked the employee the clicked Employee is set in the session*/
                        String str[] = nodeId.split("-");
                        empId = str[1];

                        String str2[] = str[0].split("@");

                        if (str2[1].equalsIgnoreCase("CONT")) {
                            empType = "C";
                        } else if (str2[1].equalsIgnoreCase("6CONT")) {
                            empType = "R";
                        } else if (str2[1].equalsIgnoreCase("REG")) {
                            empType = "R";
                        } else if (str2[1].equalsIgnoreCase("WOR")) {
                            empType = "R";
                        } else if (str2[1].equalsIgnoreCase("RET")) {
                            empType = "S";
                        } else if (str2[1].equalsIgnoreCase("XCAD")) {
                            empType = "R";
                        } else if (str2[1].equalsIgnoreCase("WAG")) {
                            empType = "R";
                        } else if (str2[1].equalsIgnoreCase("NGA")) {
                            empType = "C";
                        } /*else if (str2[1].equalsIgnoreCase("DPTN")) {
                         empType = "D";
                         }*/ else {
                            System.out.println("deceased");
                            empType = "R";
                        }

                        selectedEmpObj = servicePanelDAO.getSelectedEmployeeInfo(empId);
                        mav.addObject("SelectedEmpObj", selectedEmpObj);
                        SelectedEmpOffice = selectedEmpObj.getOffcode();
                        mav.addObject("SelectedEmpOffice", SelectedEmpOffice);
                        /*When User clicked the employee the clicked Employee is set in the session*/
                        servicesList = servicePanelDAO.getRollWiseGrpInfo(roll, lub.getLoginspc(), ifEmpSpecific, false, empType);
                    }
                } else {
                    ifEmpSpecific = "N";
                    servicesList = servicePanelDAO.getRollWiseGrpInfo(roll, lub.getLoginspc(), ifEmpSpecific, false, "NA");

                }

            } else if (roll != null && (roll.equals("04"))) {
                if (nodeId != null && !nodeId.equals("")) {
                    if (nodeId.substring(0, 2).equals("PA")) { /* This block will be execute when user will press Parent Node */

                        ifEmpSpecific = "N";
                        mav.addObject("SelectedEmpOffice", nodeId);
                        servicesList = servicePanelDAO.getRollWiseGrpInfo(roll, lub.getLoginspc(), ifEmpSpecific, false, "NA");
                    } else if (nodeId != null && nodeId.substring(0, 2).equals("RE")) { /* This block will be execute when user will press Regular Employee Node */

                        SelectedEmpOffice = "";
                        if (nodeId.length() > 15) {
                            SelectedEmpOffice = nodeId.substring(3);
                            SelectedEmpOffice = SelectedEmpOffice.substring(0, 13);
                        }

                        mav.addObject("SelectedEmpOffice", SelectedEmpOffice);
                        ifEmpSpecific = "N";
                        servicesList = servicePanelDAO.getRollWiseGrpInfo(roll, lub.getLoginspc(), ifEmpSpecific, false, "R");
                    } else {
                        /*mav.addObject("SelectedEmpOffice", nodeId);
                         ifEmpSpecific = "N";
                         servicesList = servicePanelDAO.getRollWiseGrpInfo(roll, lub.getLoginspc(), ifEmpSpecific, false, "NA");*/

                        ifEmpSpecific = "Y";
                        String empId = "";
                        String empType = "";

                        /*When User clicked the employee the clicked Employee is set in the session*/
                        String str[] = nodeId.split("-");
                        empId = str[1];

                        String str2[] = str[0].split("@");

                        if (str2[1].equalsIgnoreCase("CONT")) {
                            empType = "C";
                        } else if (str2[1].equalsIgnoreCase("6CONT")) {
                            empType = "R";
                        } else if (str2[1].equalsIgnoreCase("REG")) {
                            empType = "R";
                        } else if (str2[1].equalsIgnoreCase("WOR")) {
                            empType = "R";
                        } else if (str2[1].equalsIgnoreCase("RET")) {
                            empType = "S";
                        } else if (str2[1].equalsIgnoreCase("XCAD")) {
                            empType = "R";
                        } else if (str2[1].equalsIgnoreCase("WAG")) {
                            empType = "R";
                        } else if (str2[1].equalsIgnoreCase("NGA")) {
                            empType = "C";
                        } /*else if (str2[1].equalsIgnoreCase("DPTN")) {
                         empType = "R";
                         }*/ else {
                            empType = "R";
                        }

                        selectedEmpObj = servicePanelDAO.getSelectedEmployeeInfo(empId);
                        mav.addObject("SelectedEmpObj", selectedEmpObj);
                        SelectedEmpOffice = selectedEmpObj.getOffcode();
                        mav.addObject("SelectedEmpOffice", SelectedEmpOffice);
                        /*When User clicked the employee the clicked Employee is set in the session*/
                        servicesList = servicePanelDAO.getRollWiseGrpInfo(roll, lub.getLoginspc(), ifEmpSpecific, false, empType);
                    }
                } else {
                    ifEmpSpecific = "N";
                    servicesList = servicePanelDAO.getRollWiseGrpInfo(roll, lub.getLoginspc(), ifEmpSpecific, false, "NA");

                }
            } else if (roll != null && roll.equals("03")) {
                if (nodeId.substring(0, 2).equals("PA")) { /* This block will be execute when user will press Parent Node */

                    ifEmpSpecific = "N";
                    servicesList = servicePanelDAO.getRollWiseGrpInfo(roll, lub.getLoginspc(), ifEmpSpecific, false, "NA");
                } else { /* This block will be execute when user will press Regular Employee Node */

                    ifEmpSpecific = "N";
                    servicesList = servicePanelDAO.getRollWiseGrpInfo(roll, lub.getLoginspc(), ifEmpSpecific, false, "NA");
                }
            } else if (roll != null && roll.equals("01")) {
                /* This block will be execute when user will press Regular Employee Node */

                ifEmpSpecific = "N";
                servicesList = servicePanelDAO.getRollWiseGrpInfo(roll, lub.getLoginspc(), ifEmpSpecific, false, "NA");

            } /*else {
             System.out.println("Deputation_tabcontroller");
             ifEmpSpecific = "N";
             servicesList = servicePanelDAO.getRollWiseGrpInfo(roll, lub.getLoginspc(), ifEmpSpecific, true, "D");
             }*/

            rgi.setGrpList(servicesList);
            rgi.setIsAccessible("Yes");
            rgi.setCurOffice(SelectedEmpOffice);

            mav.addObject("isregular", selectedEmpObj.getIsRegular());
            mav.addObject("ifreengaged", selectedEmpObj.getIfReengaged());
            mav.addObject("ifretired", selectedEmpObj.getIfRetired());

            mav.addObject("RollwiseGroupInfoBean", rgi);
            path = "/tab/Services";

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "getRollWiseLinkDC", method = RequestMethod.GET)
    public ModelAndView getRollWiseLinkDC(@ModelAttribute("RollwiseGroupInfoBean") RollwiseGroupInfoBean rgi, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            BindingResult result, @RequestParam("nodeID") String nodeId) {
        ModelAndView mav = new ModelAndView();
        boolean employeedistrict = false;
        String SelectedEmpOffice = "";
        // System.out.println("nodeId==="+nodeId);
        employeedistrict = employeeDAO.verifyEmployeeDistrict(lub.getLogindistrictcode(), nodeId);
        if (lub.getLoginusertype().equalsIgnoreCase("A") || lub.getLoginusertype().equalsIgnoreCase("S")) {
            employeedistrict = true;
        }
        String path = "";
        if (employeedistrict == true) {
            // System.out.println("nodeId===" + nodeId);
            Users selectedEmpObj = servicePanelDAO.getSelectedEmployeeInfo(nodeId);
            if (selectedEmpObj.getOffcode() != null) {
                SelectedEmpOffice = selectedEmpObj.getOffcode();
            }

            String roll = "05";
            String ifEmpSpecific = "Y";
            //When User clicked the employee the clicked Employee is set in the session
            ArrayList servicesList = servicePanelDAO.getRollWiseGrpInfo(roll, "OLSCOP00100000401860001", ifEmpSpecific, true, "ALL");
            rgi.setGrpList(servicesList);
            rgi.setIsAccessible("Yes");

            mav.addObject("isForeignbody",lub.getLoginAsForeignbody());
            mav.addObject("SelectedEmpOffice", SelectedEmpOffice);
            mav.addObject("SelectedEmpObj", selectedEmpObj);
            mav.addObject("RollwiseGroupInfoBean", rgi);
            path = "/tab/ServicesDC";
        } else {
            path = "under_const";
        }
        mav.setViewName(path);
        return mav;

    }
}

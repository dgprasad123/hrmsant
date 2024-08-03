/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.payroll.allowancededuction;

import hrms.common.Message;
import hrms.dao.payroll.allowancededcution.AllowanceDeductionDAO;
import hrms.dao.payroll.billbrowser.BillGroupDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.payroll.allowancededcution.AllowanceDeduction;
import hrms.model.payroll.allowancededcution.OfficeWiseAllowanceAndDeductionForm;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas Jena
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class AllowanceDeductionController {

    @Autowired
    AllowanceDeductionDAO allowanceDeductionDAO;

    @Autowired
    public BillGroupDAO billGroupDAO;

    @RequestMapping(value = "employeeWiseUpdatedADAction")
    public ModelAndView EmployeeWiseUpdatedADAction(ModelMap model, @ModelAttribute("AllowanceDeductionbean") AllowanceDeduction albean, @ModelAttribute("SelectedEmpObj") Users lub, BindingResult result, HttpServletResponse response) {
        ArrayList adlist = new ArrayList();
        albean.setWhereupdated("E");
        albean.setUpdationRefCode(lub.getEmpId());
        ModelAndView mav = new ModelAndView("/allowancededuction/EmploeeSpecificAD", "command", albean);
        if (albean.getAdtype() != null) {
            if (albean.getAdtype().equals("A")) {
                adlist = allowanceDeductionDAO.getEmployeeWiseAllowance(albean.getUpdationRefCode());
            } else if (albean.getAdtype().equals("D")) {
                adlist = allowanceDeductionDAO.getEmployeeWiseDeduction(albean.getUpdationRefCode());
            } else if (albean.getAdtype().equals("P")) {
                adlist = allowanceDeductionDAO.getEmployeeWisePvtDeduction(albean.getUpdationRefCode());
            }
            mav.addObject("adlist", adlist);
        }
        return mav;
    }

    @RequestMapping(value = "allowanceAndDeductionEdit")
    public ModelAndView AllowanceAndDeductionEdit(ModelMap model, @ModelAttribute("AllowanceDeductionbean") AllowanceDeduction albean, @ModelAttribute("SelectedEmpObj") Users lub, BindingResult result, HttpServletResponse response) {
        AllowanceDeduction talbean = allowanceDeductionDAO.getAllowanceDeductionDetail(albean.getAdcode(), lub.getEmpId(), "E");
        albean.setAddesc(talbean.getAddesc());
        albean.setAdamttype(talbean.getAdamttype());
        albean.setAdtype(talbean.getAdtype());
        albean.setUpdationRefCode(lub.getEmpId());
        albean.setHead(talbean.getHead());
        albean = allowanceDeductionDAO.getUpdatedAllowanceDeduction(albean);
        ArrayList formulaList = allowanceDeductionDAO.getFormulaList(albean.getAdcode());
        ModelAndView mav = new ModelAndView("/allowancededuction/EmployeeSpecificADEdit", "command", albean);
        mav.addObject("formulaList", formulaList);
        return mav;
    }

    @RequestMapping(value = "officeAllowanceAndDeductionEdit")
    public ModelAndView officeAllowanceAndDeductionEdit(ModelMap model, @ModelAttribute("AllowanceDeductionbean") AllowanceDeduction albean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        AllowanceDeduction talbean = allowanceDeductionDAO.getAllowanceDeductionDetail(albean.getAdcode(), lub.getLoginoffcode(), "O");
        String adType = albean.getAdtype();
        int adCount = 0;
        ArrayList formulaList = allowanceDeductionDAO.getFormulaList(albean.getAdcode());
        albean.setUpdationRefCode(lub.getLoginoffcode());
        albean.setWhereupdated("O");
        albean = allowanceDeductionDAO.getUpdatedAllowanceDeduction(albean);
        albean.setAddesc(talbean.getAddesc());
        albean.setAdamttype(albean.getAdamttype());
        albean.setUpdationRefCode(lub.getLoginoffcode());
        albean.setHead(talbean.getHead());
        albean.setAdvalue(albean.getAdvalue());
        if (albean.getFormula() == null) {
            albean.setFormula(talbean.getFormula());
        }
        albean.setAdtype(adType);
        ModelAndView mav = new ModelAndView("/allowancededuction/OfficeSpecificADEdit", "AllowanceDeductionbean", albean);
        mav.addObject("formulaList", formulaList);
        mav.addObject("adCount", adCount);
        return mav;
    }

    @RequestMapping(value = "saveAllowanceAndDeduction", params = "action=Save")
    public ModelAndView SaveAllowanceAndDeduction(ModelMap model, @ModelAttribute("AllowanceDeductionbean") AllowanceDeduction albean, @ModelAttribute("SelectedEmpObj") Users lub, BindingResult result, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("/allowancededuction/EmploeeSpecificAD", "command", albean);
        ArrayList adlist = new ArrayList();
        albean.setWhereupdated("E");
        albean.setUpdationRefCode(lub.getEmpId());
        allowanceDeductionDAO.saveAllowanceDeductionDetail(albean);
        if (albean.getAdtype() != null) {
            if (albean.getAdtype().equals("A")) {
                adlist = allowanceDeductionDAO.getEmployeeWiseAllowance(albean.getUpdationRefCode());
            } else if (albean.getAdtype().equals("D")) {
                adlist = allowanceDeductionDAO.getEmployeeWiseDeduction(albean.getUpdationRefCode());
            } else if (albean.getAdtype().equals("P")) {
                adlist = allowanceDeductionDAO.getEmployeeWisePvtDeduction(albean.getUpdationRefCode());
            }
            mav.addObject("adlist", adlist);
        }
        return mav;
    }

    @RequestMapping(value = "saveAllowanceAndDeduction", params = "action=Delete")
    public ModelAndView DeleteAllowanceAndDeduction(ModelMap model, @ModelAttribute("AllowanceDeductionbean") AllowanceDeduction albean, @ModelAttribute("SelectedEmpObj") Users lub, BindingResult result, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("/allowancededuction/EmploeeSpecificAD", "command", albean);
        ArrayList adlist = new ArrayList();
        albean.setWhereupdated("E");
        albean.setUpdationRefCode(lub.getEmpId());
        allowanceDeductionDAO.deleteAllowanceDeductionDetail(albean);
        if (albean.getAdtype() != null) {
            if (albean.getAdtype().equals("A")) {
                adlist = allowanceDeductionDAO.getEmployeeWiseAllowance(albean.getUpdationRefCode());
            } else if (albean.getAdtype().equals("D")) {
                adlist = allowanceDeductionDAO.getEmployeeWiseDeduction(albean.getUpdationRefCode());
            } else if (albean.getAdtype().equals("P")) {
                adlist = allowanceDeductionDAO.getEmployeeWisePvtDeduction(albean.getUpdationRefCode());
            }
            mav.addObject("adlist", adlist);
        }
        return mav;
    }

    @RequestMapping(value = "saveAllowanceAndDeduction", params = "action=Cancel")
    public ModelAndView CancelAllowanceAndDeductionEdit(ModelMap model, @ModelAttribute("AllowanceDeductionbean") AllowanceDeduction albean, @ModelAttribute("SelectedEmpObj") Users lub, BindingResult result, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("/allowancededuction/EmploeeSpecificAD", "command", albean);
        ArrayList adlist = new ArrayList();
        albean.setWhereupdated("E");
        albean.setUpdationRefCode(lub.getEmpId());

        if (albean.getAdtype() != null) {
            if (albean.getAdtype().equals("A")) {
                adlist = allowanceDeductionDAO.getEmployeeWiseAllowance(albean.getUpdationRefCode());
            } else if (albean.getAdtype().equals("D")) {
                adlist = allowanceDeductionDAO.getEmployeeWiseDeduction(albean.getUpdationRefCode());
            } else if (albean.getAdtype().equals("P")) {
                adlist = allowanceDeductionDAO.getEmployeeWisePvtDeduction(albean.getUpdationRefCode());
            }
            mav.addObject("adlist", adlist);
        }
        return mav;
    }

    @RequestMapping(value = "saveOfficeAllowanceAndDeduction", params = "action=Save")
    public ModelAndView SaveOfficeAllowanceAndDeduction(@ModelAttribute("AllowanceDeductionbean") AllowanceDeduction albean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("/allowancededuction/OfficeSpecificAD", "command", albean);
        ArrayList adlist = new ArrayList();
        albean.setWhereupdated("O");
        albean.setUpdationRefCode(lub.getLoginoffcode());
        allowanceDeductionDAO.saveOfficeAllowanceDeductionDetail(albean);
        if (albean.getAdtype() != null) {
            if (albean.getAdtype().equals("A")) {
                adlist = allowanceDeductionDAO.getOfficeWiseAllowance(lub.getLoginoffcode());
            } else if (albean.getAdtype().equals("D")) {
                adlist = allowanceDeductionDAO.getOfficeWiseDeduction(lub.getLoginoffcode());
            } else if (albean.getAdtype().equals("P")) {
                adlist = allowanceDeductionDAO.getOfficeWisePvtDeduction(lub.getLoginoffcode());
            }
            mav.addObject("adlist", adlist);
        }

        return mav;
    }

    @RequestMapping(value = "saveOfficeAllowanceAndDeduction", params = "action=Cancel")
    public ModelAndView SaveOfficeAllowanceAndDeductionEdit(ModelMap model, @ModelAttribute("AllowanceDeductionbean") AllowanceDeduction albean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("/allowancededuction/OfficeSpecificAD", "command", albean);
        ArrayList adlist = new ArrayList();
        albean.setWhereupdated("O");
        albean.setUpdationRefCode(lub.getLoginoffcode());
        if (albean.getAdtype() != null) {
            if (albean.getAdtype().equals("A")) {
                adlist = allowanceDeductionDAO.getOfficeWiseAllowance(lub.getLoginoffcode());
            } else if (albean.getAdtype().equals("D")) {
                adlist = allowanceDeductionDAO.getOfficeWiseDeduction(lub.getLoginoffcode());
            } else if (albean.getAdtype().equals("P")) {
                adlist = allowanceDeductionDAO.getOfficeWisePvtDeduction(lub.getLoginoffcode());
            }
            mav.addObject("adlist", adlist);
        }
        return mav;
    }

    @RequestMapping(value = "saveOfficeAllowanceAndDeduction", params = "action=Delete")
    public ModelAndView DeleteOfficeAllowanceAndDeductionEdit(ModelMap model, @ModelAttribute("AllowanceDeductionbean") AllowanceDeduction albean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("/allowancededuction/OfficeSpecificAD", "command", albean);
        ArrayList adlist = new ArrayList();
        albean.setWhereupdated("O");
        albean.setUpdationRefCode(lub.getLoginoffcode());
        allowanceDeductionDAO.deleteAdInfo(lub.getLoginoffcode(), albean.getAdcode(), "O");
        if (albean.getAdtype() != null) {
            if (albean.getAdtype().equals("A")) {
                adlist = allowanceDeductionDAO.getOfficeWiseAllowance(lub.getLoginoffcode());
            } else if (albean.getAdtype().equals("D")) {
                adlist = allowanceDeductionDAO.getOfficeWiseDeduction(lub.getLoginoffcode());
            } else if (albean.getAdtype().equals("P")) {
                adlist = allowanceDeductionDAO.getOfficeWisePvtDeduction(lub.getLoginoffcode());
            }
            mav.addObject("adlist", adlist);
        }
        return mav;
    }

    @RequestMapping(value = "saveOfficeAllowanceAndDeduction", params = "action=Advance")
    public ModelAndView AdvanceOfficeAllowanceAndDeductionEdit(ModelMap model, @ModelAttribute("AllowanceDeductionbean") AllowanceDeduction albean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("/allowancededuction/AdvanceAD", "command", albean);
        ArrayList adlist = new ArrayList();
        albean.setWhereupdated("P");
        albean.setUpdationRefCode(lub.getLoginoffcode());
        ArrayList pl = new ArrayList();

        ArrayList al = allowanceDeductionDAO.getFormulaList(albean.getAdcode());
        ArrayList ol = allowanceDeductionDAO.getofficeWisePostListAdvance(lub.getLoginoffcode(), albean.getAdcode());

        if (albean.getAdtype() != null) {
            if (albean.getAdtype().equals("A")) {
                mav.addObject("adType", "Allowance");
            } else if (albean.getAdtype().equals("D")) {
                mav.addObject("adType", "Deduction");
            } else if (albean.getAdtype().equals("P")) {
                mav.addObject("adType", "Pvt. Deduction");
            }
            mav.addObject("adlist", adlist);
        }
        mav.addObject("adlist", ol);
        mav.addObject("formulaList", al);
        mav.addObject("adCode", albean.getAdcode());
        mav.addObject("adDesc", albean.getAddesc());
        mav.addObject("adType1", albean.getAdtype());
        return mav;
    }

    @RequestMapping(value = "SaveAdvanceADAction", params = "action=Save Config")
    public ModelAndView SaveAdvanceADAction(@ModelAttribute("AllowanceDeductionbean") AllowanceDeduction albean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("/allowancededuction/AdvanceAD", "command", albean);
        ArrayList adlist = new ArrayList();
        albean.setWhereupdated("P");
        albean.setUpdationRefCode(lub.getLoginoffcode());
        allowanceDeductionDAO.saveAdvanceAllowanceDeductionDetail(albean, lub.getLoginoffcode());
        ArrayList al = allowanceDeductionDAO.getFormulaList(albean.getAdcode());
        ArrayList ol = allowanceDeductionDAO.getofficeWisePostListAdvance(lub.getLoginoffcode(), albean.getAdcode());

        if (albean.getAdtype() != null) {
            if (albean.getAdtype().equals("A")) {
                mav.addObject("adType", "Allowance");
            } else if (albean.getAdtype().equals("D")) {
                mav.addObject("adType", "Deduction");
            } else if (albean.getAdtype().equals("P")) {
                mav.addObject("adType", "Pvt. Deduction");
            }
            mav.addObject("adlist", adlist);
        }
        mav.addObject("adlist", ol);
        mav.addObject("formulaList", al);
        mav.addObject("adCode", albean.getAdcode());
        mav.addObject("adType1", albean.getAdtype());
        mav.addObject("adDesc", albean.getAddesc());
        return mav;
    }

    @RequestMapping(value = "SaveAdvanceADAction", params = "action=Cancel")
    public ModelAndView CancelAdvanceADAction(@ModelAttribute("AllowanceDeductionbean") AllowanceDeduction albean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("/allowancededuction/OfficeSpecificAD", "command", albean);
        ArrayList adlist = new ArrayList();
        albean.setWhereupdated("O");
        albean.setUpdationRefCode(lub.getLoginoffcode());
        if (albean.getAdtype() != null) {
            if (albean.getAdtype().equals("A")) {
                adlist = allowanceDeductionDAO.getOfficeWiseAllowance(lub.getLoginoffcode());
            } else if (albean.getAdtype().equals("D")) {
                adlist = allowanceDeductionDAO.getOfficeWiseDeduction(lub.getLoginoffcode());
            } else if (albean.getAdtype().equals("P")) {
                adlist = allowanceDeductionDAO.getOfficeWisePvtDeduction(lub.getLoginoffcode());
            }
            mav.addObject("adlist", adlist);
        }
        return mav;
    }

    @RequestMapping(value = "officeWiseUpdatedADAction", method = RequestMethod.GET)
    public ModelAndView OfficeWiseUpdatedADAction(ModelMap model, @ModelAttribute("AllowanceDeductionbean") AllowanceDeduction albean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {//
        ModelAndView mav = new ModelAndView("/allowancededuction/OfficeSpecificAD", "command", albean);

        return mav;
    }

    @RequestMapping(value = "officeWiseUpdatedADAction", method = RequestMethod.POST)
    public ModelAndView officeWiseUpdatedADAction(@ModelAttribute("AllowanceDeductionbean") AllowanceDeduction albean, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mav = new ModelAndView("/allowancededuction/OfficeSpecificAD", "command", albean);
        ArrayList adlist = new ArrayList();
        if (albean.getAdtype().equals("A")) {
            adlist = allowanceDeductionDAO.getOfficeWiseAllowance(lub.getLoginoffcode());
        } else if (albean.getAdtype().equals("D")) {
            adlist = allowanceDeductionDAO.getOfficeWiseDeduction(lub.getLoginoffcode());
        } else if (albean.getAdtype().equals("P")) {
            adlist = allowanceDeductionDAO.getOfficeWisePvtDeduction(lub.getLoginoffcode());
        }
        mav.addObject("adlist", adlist);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "getEmployeeWiseADList", method = RequestMethod.GET)
    public void getEmployeeWiseADList(HttpServletRequest request, @ModelAttribute("AllowanceDeductionbean") AllowanceDeduction albean, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList adlist = new ArrayList();
        if (albean.getAdtype().equals("A")) {
            adlist = allowanceDeductionDAO.getEmployeeWiseAllowance("08000437");
        } else if (albean.getAdtype().equals("D")) {
            adlist = allowanceDeductionDAO.getEmployeeWiseDeduction("08000437");
        } else if (albean.getAdtype().equals("P")) {
            adlist = allowanceDeductionDAO.getEmployeeWisePvtDeduction("08000437");
        }

        JSONArray json = new JSONArray(adlist);
        out = response.getWriter();
        out.write(json.toString());
    }

    @ResponseBody
    @RequestMapping(value = "getOfficeWiseADList")
    public void getOfficeWiseADList(HttpServletRequest request, @ModelAttribute("AllowanceDeductionbean") AllowanceDeduction albean, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList adlist = new ArrayList();
        if (albean.getAdtype().equals("A")) {
            adlist = allowanceDeductionDAO.getOfficeWiseAllowance(lub.getLoginoffcode());
        } else if (albean.getAdtype().equals("D")) {
            adlist = allowanceDeductionDAO.getOfficeWiseDeduction(lub.getLoginoffcode());
        } else if (albean.getAdtype().equals("P")) {
            adlist = allowanceDeductionDAO.getOfficeWisePvtDeduction(lub.getLoginoffcode());
        }

        JSONArray json = new JSONArray(adlist);
        out = response.getWriter();
        out.write(json.toString());
    }

    @RequestMapping(value = "editAllowanceDeductionAction", method = RequestMethod.GET)
    public void EditAllowanceDeductionAction(ModelMap model, @ModelAttribute("AllowanceDeduction") AllowanceDeduction adbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        AllowanceDeduction ad = new AllowanceDeduction();
        if (adbean.getWhereupdated().equals("G")) {
            ad = allowanceDeductionDAO.getAllowanceDeductionDetail(adbean.getAdcode(), lub.getLoginempid(), "E");
        } else if (adbean.getWhereupdated().equals("O")) {
            ad = allowanceDeductionDAO.getUpdatedAllowanceDeductionDetail(adbean.getAdcode());
        }
        JSONObject job = new JSONObject(ad);
        out = response.getWriter();
        out.write(job.toString());
    }

    @RequestMapping(value = "saveAllowanceDeductionAction", method = RequestMethod.POST)
    public void SaveAllowanceDeductionAction(ModelMap model, @ModelAttribute("AllowanceDeduction") AllowanceDeduction adbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        Message msg = null;
        if (adbean.getWhereupdated().equals("G")) {
            adbean.setUpdationRefCode(lub.getLoginoffcode());
            msg = allowanceDeductionDAO.saveAllowanceDeductionDetail(adbean);
        } else if (adbean.getWhereupdated().equals("O")) {

        }

        JSONObject job = new JSONObject(msg);
        out = response.getWriter();
        out.write(job.toString());
    }

    @RequestMapping(value = "ImportDeduction")
    public ModelAndView ImportDeduction(@ModelAttribute("AllowanceDeductionbean") AllowanceDeduction adbean, @RequestParam Map<String, String> requestParams) {

        ModelAndView mav = null;

        try {

            String status = requestParams.get("status");

            ArrayList privateDednList = allowanceDeductionDAO.getPrivateDednList();
            List deductionList = allowanceDeductionDAO.getDednList();

            mav = new ModelAndView("/allowancededuction/ImportDeduction", "AllowanceDeductionbean", adbean);
            mav.addObject("privateDednList", privateDednList);
            mav.addObject("deductionList", deductionList);
            mav.addObject("status", status);
            mav.addObject("hrafommulalist",allowanceDeductionDAO.getHRAFormulaList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "UploadDeductionExcel", params = {"action=Save Private Deduction"})
    public ModelAndView UploadDeductionExcel(@ModelAttribute("AllowanceDeductionbean") AllowanceDeduction adbean) {

        ModelAndView mav = null;

        MultipartFile uploadedFile = adbean.getUploadedFile();

        InputStream inputStream = null;
        Workbook workbook = null;
        try {
            //System.out.println("Private Deduction");
            if (uploadedFile != null && !uploadedFile.isEmpty()) {
                inputStream = uploadedFile.getInputStream();
                if (uploadedFile.getOriginalFilename().endsWith("xlsx")) {
                    workbook = new XSSFWorkbook(inputStream);
                    allowanceDeductionDAO.uploadDeductionExcelData(adbean.getPvtDedn(), workbook);
                } else if (uploadedFile.getOriginalFilename().endsWith("xls")) {
                    workbook = new HSSFWorkbook(inputStream);
                    allowanceDeductionDAO.uploadDeductionExcelData(adbean.getPvtDedn(), workbook);
                } else {
                    throw new IllegalArgumentException("The specified file is not Excel file");
                }
            }
            //mav = new ModelAndView("/allowancededuction/ImportDeduction", "AllowanceDeductionbean", adbean);
            return new ModelAndView("redirect:/ImportDeduction.htm?status=S");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ie) {
                ie.printStackTrace();
            }
        }
        return mav;
    }

    @RequestMapping(value = "UploadDeductionExcel", params = {"action=Save Deduction"})
    public ModelAndView UploadDeductionDataExcel(@ModelAttribute("AllowanceDeductionbean") AllowanceDeduction adbean) {

        ModelAndView mav = null;

        MultipartFile uploadedDedFile = adbean.getUploadedDednFile();

        InputStream inputStream = null;
        Workbook workbook = null;
        try {
            //System.out.println("deduction");
            if (uploadedDedFile != null && !uploadedDedFile.isEmpty()) {
                inputStream = uploadedDedFile.getInputStream();
                if (uploadedDedFile.getOriginalFilename().endsWith("xlsx")) {
                    workbook = new XSSFWorkbook(inputStream);
                    allowanceDeductionDAO.uploadDeductionExcelData(adbean.getDeduction(), workbook);
                } else if (uploadedDedFile.getOriginalFilename().endsWith("xls")) {
                    workbook = new HSSFWorkbook(inputStream);
                    allowanceDeductionDAO.uploadDeductionExcelData(adbean.getDeduction(), workbook);
                } else {
                    throw new IllegalArgumentException("The specified file is not Excel file");
                }
            }
            //mav = new ModelAndView("/allowancededuction/ImportDeduction", "AllowanceDeductionbean", adbean);
            return new ModelAndView("redirect:/ImportDeduction.htm?status=D");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ie) {
                ie.printStackTrace();
            }
        }
        return mav;
    }
    
    @RequestMapping(value = "UploadDeductionExcel", params = {"action=Save Allowance"})
    public ModelAndView UploadAllowanceDataExcel(@ModelAttribute("AllowanceDeductionbean") AllowanceDeduction adbean) {

        ModelAndView mav = null;

        MultipartFile uploadedDedFile = adbean.getUploadedAllowanceFile();

        InputStream inputStream = null;
        Workbook workbook = null;
        try {
            //System.out.println("deduction");
            if (uploadedDedFile != null && !uploadedDedFile.isEmpty()) {
                inputStream = uploadedDedFile.getInputStream();
                if (uploadedDedFile.getOriginalFilename().endsWith("xlsx")) {
                    workbook = new XSSFWorkbook(inputStream);
                    allowanceDeductionDAO.uploadAllowanceExcelData(adbean.getAllowance(), workbook,adbean.getSltAllowanceFormula());
                } else if (uploadedDedFile.getOriginalFilename().endsWith("xls")) {
                    workbook = new HSSFWorkbook(inputStream);
                    allowanceDeductionDAO.uploadAllowanceExcelData(adbean.getAllowance(), workbook,adbean.getSltAllowanceFormula());
                } else {
                    throw new IllegalArgumentException("The specified file is not Excel file");
                }
            }
            return new ModelAndView("redirect:/ImportDeduction.htm?status=A");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ie) {
                ie.printStackTrace();
            }
        }
        return mav;
    }

    @RequestMapping(value = "getOfficeAllowanceAndDeductionList")
    public ModelAndView getOfficeAllowanceAndDeductionList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("allowanceDeductionForm") OfficeWiseAllowanceAndDeductionForm allowanceDeductionForm) {

        ModelAndView mav = null;

        try {
            mav = new ModelAndView("/allowancededuction/OfficeWiseAllowanceAndDeduction", "allowanceDeductionForm", allowanceDeductionForm);

            List billGrpList = billGroupDAO.getBillGroupList(lub.getLoginoffcode());
            List deductionList = allowanceDeductionDAO.getDeductionList();

            if (allowanceDeductionForm.getSltBillGroup() != null && !allowanceDeductionForm.getSltBillGroup().equals("")) {
                if (allowanceDeductionForm.getSltDeductionName() != null && !allowanceDeductionForm.getSltDeductionName().equals("")) {
                    List emplist = allowanceDeductionDAO.getBillWiseAllowanceAndDeductionList(lub.getLoginoffcode(), allowanceDeductionForm.getSltBillGroup(), allowanceDeductionForm.getSltDeductionName());
                    mav.addObject("emplist", emplist);
                }
            }

            mav.addObject("billGrpList", billGrpList);
            mav.addObject("deductionList", deductionList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "getOfficeAllowanceAndDeductionList", params = {"btnSubmit=Update"})
    public ModelAndView Update(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("allowanceDeductionForm") OfficeWiseAllowanceAndDeductionForm allowanceDeductionForm) {

        ModelAndView mav = null;

        try {
            mav = new ModelAndView("/allowancededuction/OfficeWiseAllowanceAndDeduction", "allowanceDeductionForm", allowanceDeductionForm);

            allowanceDeductionDAO.updateDeductionData(allowanceDeductionForm);
            List billGrpList = billGroupDAO.getBillGroupList(lub.getLoginoffcode());
            List deductionList = allowanceDeductionDAO.getDeductionList();

            mav.addObject("billGrpList", billGrpList);
            mav.addObject("deductionList", deductionList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "getOfficeAllowanceAndDeductionList", params = {"btnSubmit=Delete"})
    public ModelAndView Delete(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("allowanceDeductionForm") OfficeWiseAllowanceAndDeductionForm allowanceDeductionForm) {

        ModelAndView mav = null;

        try {
            mav = new ModelAndView("/allowancededuction/OfficeWiseAllowanceAndDeduction", "allowanceDeductionForm", allowanceDeductionForm);

            allowanceDeductionDAO.deleteDeductionData(allowanceDeductionForm);

            return new ModelAndView("redirect:/getOfficeAllowanceAndDeductionList.htm?sltBillGroup=" + allowanceDeductionForm.getSltBillGroup() + "&sltDeductionName=" + allowanceDeductionForm.getSltDeductionName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "billGroupWiseDeductionList", method = RequestMethod.GET)
    public ModelAndView billGroupWiseDeductionList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {//
        String path = "/allowancededuction/billGroupWiseDeductionList";
        ArrayList billGroupList = allowanceDeductionDAO.billGroupWiseDeductionList(lub.getLoginoffcode());

        ModelAndView mav = new ModelAndView();
        mav.addObject("groupList", billGroupList);
        mav.setViewName(path);
        return mav;

    }

    @RequestMapping(value = "billWiseDeductionListDetails", method = RequestMethod.GET)
    public ModelAndView billWiseDeductionListDetails(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("groupId") String groupId) {//
        String path = "/allowancededuction/billWiseDeductionListDetails";
        ArrayList billGroupList = allowanceDeductionDAO.billWiseDeductionListDetails(groupId);

        ModelAndView mav = new ModelAndView();
        mav.addObject("groupList", billGroupList);
        mav.setViewName(path);
        return mav;

    }

    @RequestMapping(value = "ManageADEmployeeWise")
    public ModelAndView manageADEmployeeWise(@ModelAttribute("manageADEmployeeForm") AllowanceDeduction manageADEmployeeForm) {

        ModelAndView mav = null;

        try {
            mav = new ModelAndView("/allowancededuction/ManageADEmployeeWise", "manageADEmployeeForm", manageADEmployeeForm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "ManageADEmployeeWise", params = {"btnManageADEmployee=Get List"})
    public ModelAndView getEmployeeList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("manageADEmployeeForm") AllowanceDeduction manageADEmployeeForm) {

        ModelAndView mav = null;

        try {
            mav = new ModelAndView("/allowancededuction/ManageADEmployeeWise", "manageADEmployeeForm", manageADEmployeeForm);

            ArrayList emplist = allowanceDeductionDAO.getADEmployeeList(lub.getLoginoffcode(), manageADEmployeeForm.getSltAllowanceDeductionData());
            mav.addObject("emplist", emplist);

            ArrayList adlist = allowanceDeductionDAO.getAllowanceDeductionData(manageADEmployeeForm.getSltAllowanceDeduction());
            mav.addObject("adlist", adlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "ManageADEmployeeWise", params = {"btnManageADEmployee=Delete"})
    public ModelAndView deleteADEmployee(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("manageADEmployeeForm") AllowanceDeduction manageADEmployeeForm) {

        ModelAndView mav = null;

        try {
            mav = new ModelAndView("/allowancededuction/ManageADEmployeeWise", "manageADEmployeeForm", manageADEmployeeForm);

            allowanceDeductionDAO.deleteADEmployee(lub.getLoginoffcode(), manageADEmployeeForm.getSltAllowanceDeductionData(), manageADEmployeeForm.getChkEmployee());

            ArrayList emplist = allowanceDeductionDAO.getADEmployeeList(lub.getLoginoffcode(), manageADEmployeeForm.getSltAllowanceDeductionData());
            mav.addObject("emplist", emplist);

            ArrayList adlist = allowanceDeductionDAO.getAllowanceDeductionData(manageADEmployeeForm.getSltAllowanceDeduction());
            mav.addObject("adlist", adlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "getAllowanceDeductionData")
    public void getAllowanceDeductionData(HttpServletResponse response, @RequestParam("adtype") String adtype) throws IOException {

        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;

        try {
            ArrayList adlist = allowanceDeductionDAO.getAllowanceDeductionData(adtype);

            json = new JSONArray(adlist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
}

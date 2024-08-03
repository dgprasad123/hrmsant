/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.importemployee;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.createEmployee.CreateEmployee;
import hrms.dao.importemployee.ImportEmpDetailsExcelDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 *
 * @author Madhusmita
 */
public class ImportEmpDetailsExcelDAOImpl implements ImportEmpDetailsExcelDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void createEmployees(Workbook workbook) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        String maxUserDetailsID = null;
        String password = null;
        int result = 0;
        int result1 = 0;
        String maxEmpId = null;
        String maxEmpIDChk = null;
        int maxEmpCtr = 0;

        String isDuplicateGPF = "N";
        String isDuplicateMobile = "N";
        CreateEmployee ce = new CreateEmployee();
        ArrayList ar = new ArrayList();
        String dob1 = null;
        int c = 0;
        String genCategory = null;

        try {
            con = this.dataSource.getConnection();
            Sheet sheet = workbook.getSheet(0);
            int noofRows = sheet.getRows();
            int noofCols = sheet.getColumns();
            for (int r = 1; r < noofRows; r++) {
                ce = new CreateEmployee();
                isDuplicateGPF = "N";
                isDuplicateMobile = "N";
                for (c = 0; c < noofCols; c++) {
                    if (sheet.getCell(c, r).getContents() != null && sheet.getCell(c, r).getContents().length() != 0) {

                        switch (c) {
                            case 0:
                                if (sheet.getCell(c, r).getContents() != null && !sheet.getCell(c, r).getContents().equals("")) {
                                    ce.setTitle(sheet.getCell(c, r).getContents().trim());
                                }
                                break;
                            case 1:
                                if (sheet.getCell(c, r).getContents() != null && !sheet.getCell(c, r).getContents().equals("")) {
                                    ce.setFirstnmae(sheet.getCell(c, r).getContents().trim());
                                }
                                break;
                            case 2:
                                if (sheet.getCell(c, r).getContents() != null && !sheet.getCell(c, r).getContents().equals("")) {
                                    ce.setMname(sheet.getCell(c, r).getContents().trim());
                                }
                                break;
                            case 3:
                                if (sheet.getCell(c, r).getContents() != null && !sheet.getCell(c, r).getContents().equals("")) {
                                    ce.setLname(sheet.getCell(c, r).getContents().trim());
                                }
                            case 4:
                                if (sheet.getCell(c, r).getContents() != null && !sheet.getCell(c, r).getContents().equals("")) {
                                    if ((sheet.getCell(c, r).getContents().trim().equals("MALE")) || (sheet.getCell(c, r).getContents().trim().equals("M"))) {
                                        genCategory = "M";
                                    }
                                    if ((sheet.getCell(c, r).getContents().trim().equals("FEMALE")) || (sheet.getCell(c, r).getContents().trim().equals("F"))) {
                                        genCategory = "F";
                                    }

                                    ce.setGender(genCategory);
                                }
                                break;
                            case 5:
                                if (sheet.getCell(c, r).getContents() != null && !sheet.getCell(c, r).getContents().equals("")) {
                                    if (sheet.getCell(c, r).getContents().length() >= 10 && sheet.getCell(c, r).getContents().length() <= 11) {
                                        ce.setMobile(sheet.getCell(c, r).getContents());
                                        
                                    } else {
                                        ce.setMobile("");
                                        
                                    }

                                } else {
                                    
                                    ce.setMobile(sheet.getCell(c, r).getContents());
                                }
                                
                                break;
                            case 6:
                                ce.setEmpType(sheet.getCell(c, r).getContents());
                                break;
                            case 7:
                                if (sheet.getCell(c, r).getContents() != null && !sheet.getCell(c, r).getContents().equals("")) {
                                    ce.setBasicpay(Integer.parseInt(sheet.getCell(c, r).getContents()));
                                }
                                break;
                            case 8:
                                if (sheet.getCell(c, r).getContents() != null && !sheet.getCell(c, r).getContents().equals("")) {
                                    ce.setGp(Integer.parseInt(sheet.getCell(c, r).getContents()));
                                }
                                break;
                            case 9:
                                if (sheet.getCell(c, r).getContents() != null && !sheet.getCell(c, r).getContents().equals("")) {
                                    ce.setrTitle(sheet.getCell(c, r).getContents().trim());
                                }
                                break;
                            case 10:
                                if (sheet.getCell(c, r).getContents() != null && !sheet.getCell(c, r).getContents().equals("")) {
                                    ce.setrFirstnmae(sheet.getCell(c, r).getContents().trim());
                                }
                                break;
                            case 11:
                                if (sheet.getCell(c, r).getContents() != null && !sheet.getCell(c, r).getContents().equals("")) {
                                    ce.setrMname(sheet.getCell(c, r).getContents().trim());
                                }
                                break;
                            case 12:
                                if (sheet.getCell(c, r).getContents() != null && !sheet.getCell(c, r).getContents().equals("")) {
                                    ce.setrLname(sheet.getCell(c, r).getContents().trim());
                                }
                                break;
                            case 13:
                                if (sheet.getCell(c, r).getContents() != null && !sheet.getCell(c, r).getContents().equals("")) {
                                    ce.setRelation(sheet.getCell(c, r).getContents());
                                }
                                break;
                            case 14:
                                ce.setAccountType(sheet.getCell(c, r).getContents());
                                break;
                            case 15:
                                ce.setGpfNo(sheet.getCell(c, r).getContents());
                                break;
                            case 16:
                                ce.setDob(sheet.getCell(c, r).getContents());
                                break;
                            case 17:
                                if (sheet.getCell(c, r).getContents() != null && !sheet.getCell(c, r).getContents().equals("")) {
                                    ce.setDoj(sheet.getCell(c, r).getContents());
                                }
                                break;
                            case 18:
                                ce.setOffCode(sheet.getCell(c, r).getContents());
                                break;
                            case 19:
                                if (sheet.getCell(c, r).getContents() != null && !sheet.getCell(c, r).getContents().equals("")) {
                                    ce.setPostNmclture(sheet.getCell(c, r).getContents());
                                }
                                break;
                        }

                    }
                }
                
                ar.add(ce);

            }

            for (int i = 0; i < ar.size(); i++) {
                isDuplicateGPF = "N";

                CreateEmployee cr = (CreateEmployee) ar.get(i);

                String chkGpfNo = "SELECT GPF_NO FROM EMP_MAST WHERE GPF_NO = ?";
                pst = con.prepareStatement(chkGpfNo);
                pst.setString(1, cr.getGpfNo());
                rs = pst.executeQuery();
                if (rs.next()) {
                    isDuplicateGPF = "Y";
                    
                }
                String chkMobile = "SELECT MOBILE FROM EMP_MAST WHERE MOBILE = ?";
                pst = con.prepareStatement(chkMobile);
                pst.setString(1, cr.getMobile());
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    if (cr.getMobile() != null && !cr.getMobile().equals("")) {
                        isDuplicateMobile = "Y";
                    }

                    
                }
                if (isDuplicateMobile.equals("Y")) {
                    cr.setMobile("");
                    isDuplicateMobile = "N";
                }
                if (isDuplicateGPF.equals("N") && isDuplicateMobile.equals("N")) {
                    String maxCode = CommonFunctions.getEmpID(con);
                    String chkEmpId = " SELECT EMP_ID FROM EMP_MAST WHERE EMP_ID = ? ";
                    pst = con.prepareStatement(chkEmpId);
                    pst.setString(1, maxCode);
                    rs1 = pst.executeQuery();
                    if (rs1.next()) {
                        maxEmpIDChk = rs1.getString("EMP_ID");
                        if (maxEmpIDChk != null && !maxEmpIDChk.equals("")) {
                            maxEmpCtr = Integer.parseInt(maxEmpIDChk);
                            maxEmpCtr++;
                            maxEmpId = Integer.toString(maxEmpCtr);
                        }

                    } else {
                        maxEmpId = maxCode;
                    }

                    String insertQry = "INSERT INTO emp_mast (emp_id,gpf_no,acct_type,initials,f_name,m_name,l_name,gender,mobile,cur_basic_salary,gp,dob,doe_gov,is_regular,cur_off_code,post_nomenclature,dep_code,if_gpf_assumed) "
                            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pst = con.prepareStatement(insertQry);
                    pst.setString(1, maxEmpId);
                    
                    pst.setString(2, cr.getGpfNo());
                    pst.setString(3, cr.getAccountType());
                    if (cr.getTitle() != null && !cr.getTitle().equals("")) {
                        pst.setString(4, cr.getTitle().toUpperCase());
                    } else {
                        pst.setString(4, cr.getTitle());
                    }
                    if (cr.getFirstnmae() != null && !cr.getFirstnmae().equals("")) {
                        pst.setString(5, cr.getFirstnmae().toUpperCase());
                    } else {
                        pst.setString(5, cr.getFirstnmae());
                    }
                    if (cr.getMname() != null && !cr.getMname().equals("")) {
                        pst.setString(6, cr.getMname().toUpperCase());
                    } else {
                        pst.setString(6, cr.getMname());
                    }
                    if (cr.getLname() != null && !cr.getLname().equals("")) {
                        pst.setString(7, cr.getLname().toUpperCase());
                    } else {
                        pst.setString(7, cr.getLname());
                    }

                    pst.setString(8, cr.getGender());
                    pst.setString(9, cr.getMobile());
                    pst.setInt(10, cr.getBasicpay());
                    pst.setInt(11, cr.getGp());
                    if (cr.getDob() != null && !cr.getDob().equals("")) {
                        pst.setTimestamp(12, new Timestamp(new SimpleDateFormat("MM/dd/yyyy").parse(cr.getDob()).getTime()));
                    } else {
                        pst.setTimestamp(12, null);
                    }
                    if (cr.getDoj() != null && !cr.getDoj().equals("")) {
                        pst.setTimestamp(13, new Timestamp(new SimpleDateFormat("MM/dd/yyyy").parse(cr.getDoj()).getTime()));
                    } else {
                        pst.setTimestamp(13, null);
                    }

                    pst.setString(14, cr.getEmpType());
                    pst.setString(15, cr.getOffCode());
                    if (cr.getEmpType() != null && !cr.getEmpType().equals("")) {
                        if (cr.getEmpType().equals("N")) {
                            pst.setString(16, cr.getPostNmclture());
                            pst.setString(17, "02");

                        } else {
                            pst.setString(16, null);
                            pst.setString(17, "03");
                        }

                    }
                    pst.setString(18, "Y");
                    if (cr.getOffCode() != null && !cr.getOffCode().equals("")) {
                        if (cr.getOffCode().length() == 13) {
                            result = pst.executeUpdate();
                        }
                    }

                    if (result > 0) {
                        if (cr.getrFirstnmae() != null && !cr.getrFirstnmae().equals("")) {

                            String insertEmpRel = "INSERT INTO emp_relation (emp_id,relation,initials,f_name,m_name,l_name) "
                                    + "VALUES (?,?,?,?,?,?)";
                            pst = con.prepareStatement(insertEmpRel);
                            pst.setString(1, maxEmpId);
                            
                            if (cr.getRelation() != null && !cr.getRelation().equals("")) {
                                pst.setString(2, cr.getRelation().toUpperCase());
                            } else {
                                pst.setString(2, cr.getRelation());
                            }

                            if (cr.getrTitle() != null && !cr.getrTitle().equals("")) {
                                pst.setString(3, cr.getrTitle().toUpperCase());
                            } else {
                                pst.setString(3, cr.getrTitle());
                            }
                            if (cr.getrFirstnmae() != null && !cr.getrFirstnmae().equals("")) {
                                pst.setString(4, cr.getrFirstnmae().toUpperCase());

                            } else {
                                pst.setString(4, cr.getrFirstnmae());
                            }
                            if (cr.getrMname() != null && !cr.getrMname().equals("")) {
                                pst.setString(5, cr.getrMname().toUpperCase());

                            } else {
                                pst.setString(5, cr.getrMname());
                            }
                            if (cr.getrLname() != null && !cr.getrLname().equals("")) {
                                pst.setString(6, cr.getrLname().toUpperCase());

                            } else {
                                pst.setString(6, cr.getrLname());
                            }
                            result1 = pst.executeUpdate();
                        }

                    }
                    dob1 = cr.getDob();
                    if (dob1 != null && !dob1.equals("")) {
                        String dob4digit = dob1.substring(6, 10);
                        String username = cr.getFirstnmae().replaceAll("\\s", "") + "." + cr.getLname().replaceAll("\\s", "") + dob4digit;
                        username = username.toLowerCase();
                        password = cr.getLname().replaceAll("\\s", "") + "." + dob4digit;
                        password = password.toLowerCase();
                    }

                    if (result > 0) {
                        String insertdetails = "INSERT INTO user_details (username,password,enable,accountnonexpired,accountnonlocked,credentialsnonexpired,usertype,linkid) "
                                + "VALUES (?,?,?,?,?,?,?,?)";
                        pst = con.prepareStatement(insertdetails);
                        pst.setString(1, maxEmpId);
                        pst.setString(2, password);
                        pst.setInt(3, 1);
                        pst.setInt(4, 1);
                        pst.setInt(5, 1);
                        pst.setInt(6, 1);
                        pst.setString(7, "G");
                        pst.setString(8, maxEmpId);
                        pst.executeUpdate();
                    }
                    
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, rs, con);
        }
    }
}

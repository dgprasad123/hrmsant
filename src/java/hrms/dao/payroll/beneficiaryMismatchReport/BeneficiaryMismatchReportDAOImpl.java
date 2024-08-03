package hrms.dao.payroll.beneficiaryMismatchReport;

import hrms.common.DataBaseFunctions;
import hrms.model.employee.ArrEmpBillGroup;
import hrms.model.employee.Employee;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class BeneficiaryMismatchReportDAOImpl implements BeneficiaryMismatchReportDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getBeneficiaryMismatchReport(String offCode) {

        Connection con = null;

        PreparedStatement ps1 = null;
        ResultSet rs1 = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        List list = null;
        ArrayList outList = new ArrayList();
        ArrEmpBillGroup eList = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "";

            ps1 = con.prepareStatement("select bg.bill_group_id, bg.bill_type bill_type_in_number, bg.description, bg.off_code,bill_section_mapping.section_id, section_name, no_of_emp, g_section.bill_type  from bill_group_master bg "
                    + "	inner join bill_section_mapping on bg.bill_group_id=bill_section_mapping.bill_group_id "
                    + "	inner join g_section on bill_section_mapping.section_id=g_section.section_id "
                    + "	where bg.off_code=? order by g_section.bill_type desc, bg.bill_type, description, section_name");
            ps1.setString(1, offCode);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {

                list = new ArrayList();
                int cnt = 0;
                String billGroupId = rs1.getString("section_id");
                String description = "Bill Group: " + rs1.getString("description") + " &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style='font-size:13pt;color:#A5520D'>Section: " + rs1.getString("section_name") + " <span style='color:#000000;font-weight:bold;'>Bill Type:</span> " + rs1.getString("bill_type") + "</span>";
                

                if (rs1.getString("bill_type").equalsIgnoreCase("CONTRACTUAL")) {
                    sql = "SELECT EM.EMP_ID,GPF_NO,F_NAME,M_NAME,L_NAME,EM.MOBILE hrms_mobile,bank_acc_no hrms_bank_acc_no,ifsc_code hrms_ifs_code, "
                            + "	bdifms.benf_mobile_number bdifms_mobile,bdifms.benf_acct_no bdifms_bank_acc_no,bdifms.benf_bank_ifsc_code bdifms_ifs_code FROM bill_section_mapping BM "
                            + "	INNER JOIN section_post_mapping SPM ON SPM.section_id = BM.section_id "
                            + "	INNER JOIN emp_mast EM ON EM.emp_id = SPM.spc "
                            + "	left outer join g_branch on EM.branch_code=g_branch.branch_code "
                            + "	left outer join beneficiary_detail_ifms bdifms on EM.bank_acc_no=bdifms.benf_acct_no "
                            + "	WHERE BM.section_id = ? and EM.IS_REGULAR='N' order by SPM.POST_SL_NO";
                } else {
                    sql = "SELECT EM.EMP_ID,GPF_NO,F_NAME,M_NAME,L_NAME,EM.MOBILE hrms_mobile,bank_acc_no hrms_bank_acc_no,ifsc_code hrms_ifs_code,"
                            + " bdifms.benf_mobile_number bdifms_mobile,bdifms.benf_acct_no bdifms_bank_acc_no,bdifms.benf_bank_ifsc_code bdifms_ifs_code FROM bill_section_mapping BM"
                            + " INNER JOIN section_post_mapping SPM ON SPM.section_id = BM.section_id"
                            + " INNER JOIN emp_mast EM ON EM.cur_spc = SPM.spc"
                            + " left outer join g_branch on EM.branch_code=g_branch.branch_code"
                            + " left outer join beneficiary_detail_ifms bdifms on EM.bank_acc_no=bdifms.benf_acct_no"
                            + " WHERE BM.section_id = ? order by SPM.POST_SL_NO";
                }
                ps = con.prepareStatement(sql);

                ps.setBigDecimal(1, new BigDecimal(billGroupId));
                rs = ps.executeQuery();
                while (rs.next()) {
                    boolean found = true;
                    cnt++;
                    Employee employee = new Employee();
                    employee.setEmpid(rs.getString("EMP_ID"));
                    employee.setFname(rs.getString("F_NAME"));
                    employee.setMname(rs.getString("M_NAME"));
                    employee.setLname(rs.getString("L_NAME"));
                    employee.setGpfno(rs.getString("GPF_NO"));
                    employee.setMobile(rs.getString("hrms_mobile"));
                    employee.setBankaccno(rs.getString("hrms_bank_acc_no"));
                    employee.setSltbranch(rs.getString("hrms_ifs_code"));
                    employee.setIfmsMobile(rs.getString("bdifms_mobile"));
                    employee.setIfmsBankAccNo(rs.getString("bdifms_bank_acc_no"));
                    employee.setIfmsIFSCode(rs.getString("bdifms_ifs_code"));

                    if (employee.getBankaccno() != null && !employee.getBankaccno().equals("") && employee.getIfmsBankAccNo() != null && !employee.getIfmsBankAccNo().equals("")) {
                        if (!employee.getBankaccno().equals(employee.getIfmsBankAccNo())) {
                            found = false;
                        }
                    }
                    if (employee.getSltbranch() != null && employee.getIfmsIFSCode() != null) {
                        if (!employee.getSltbranch().equals(employee.getIfmsIFSCode())) {
                            found = false;
                        }
                    }
                    
                    if(employee.getIfmsBankAccNo()==null || employee.getIfmsBankAccNo().equals("")){
                        found = false;
                    }
                    
                    if(employee.getBankaccno()==null || employee.getBankaccno().equals("")){
                        found = false;
                    }
                    if (found) {
                        employee.setMismatchFound("N");
                    }
                    list.add(employee);
                }
                eList = new ArrEmpBillGroup();
                eList.setBillGroupName(description);
                eList.setEmpList(list);
                if (cnt > 0) {
                    outList.add(eList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, ps1);
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    private String formatDate(Date input) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = "";
        try {
            formattedDate = sdf.format(input);
        } catch (Exception exp) {
            formattedDate = "";
        }
        return formattedDate;
    }

    
}

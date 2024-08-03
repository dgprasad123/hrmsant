package hrms.model.annualBudget;

import javax.mail.Multipart;
import org.springframework.web.multipart.MultipartFile;

public class AnnualBudgetForm {
    
    private String financialYear;
    
    private MultipartFile uploadedBudgetFile;
    
    private String budgetid;
    private String uploadDedType;
    
    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

    public MultipartFile getUploadedBudgetFile() {
        return uploadedBudgetFile;
    }

    public void setUploadedBudgetFile(MultipartFile uploadedBudgetFile) {
        this.uploadedBudgetFile = uploadedBudgetFile;
    }

    public String getBudgetid() {
        return budgetid;
    }

    public void setBudgetid(String budgetid) {
        this.budgetid = budgetid;
    }

    public String getUploadDedType() {
        return uploadDedType;
    }

    public void setUploadDedType(String uploadDedType) {
        this.uploadDedType = uploadDedType;
    }
}

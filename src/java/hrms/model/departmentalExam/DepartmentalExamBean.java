package hrms.model.departmentalExam;

public class DepartmentalExamBean {
    
    private String empid;
    private String examId;
    private String dateofentry;
    private String examinationName;
    private String fromDate;
    private String toDate;
    private String examinationResult;

    public String getDateofentry() {
        return dateofentry;
    }

    public void setDateofentry(String dateofentry) {
        this.dateofentry = dateofentry;
    }

    public String getExaminationName() {
        return examinationName;
    }

    public void setExaminationName(String examinationName) {
        this.examinationName = examinationName;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getExaminationResult() {
        return examinationResult;
    }

    public void setExaminationResult(String examinationResult) {
        this.examinationResult = examinationResult;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }
    
}

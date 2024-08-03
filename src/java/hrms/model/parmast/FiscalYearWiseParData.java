/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.parmast;

import hrms.common.CommonFunctions;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author manisha
 */
public class FiscalYearWiseParData {

    private String fy;
    private ArrayList<Parabstractdata> yearwisedata = new ArrayList();
    private int noofperiod;

    public String getFy() {
        return fy;
    }

    public void setFy(String fy) {
        this.fy = fy;
    }

    public ArrayList<Parabstractdata> getYearwisedata() {
        return yearwisedata;
    }

    public void setYearwisedata(ArrayList<Parabstractdata> yearwisedata) {
        this.yearwisedata = yearwisedata;
    }

    public int getNoofperiod() {
        return yearwisedata.size();
    }

    public class Parabstractdata implements Comparable<Parabstractdata> {

        int parid = 0;
        Integer taskId;
        int parstatus = 0;
        String periodfrom = null;
        String periodto = null;
        String grade = null;
        String remark = null;
        String isreviewed = null;
        String isadversed = null;
        String gradeName = null;
        String postGroup = null;

        public int getParid() {
            return parid;
        }

        public void setParid(int parid) {
            this.parid = parid;
        }

        public Integer getTaskId() {
            return taskId;
        }

        public void setTaskId(Integer taskId) {
            this.taskId = taskId;
        }

        public int getParstatus() {
            return parstatus;
        }

        public void setParstatus(int parstatus) {
            this.parstatus = parstatus;
        }

        public String getPeriodfrom() {
            return periodfrom;
        }

        public void setPeriodfrom(String periodfrom) {
            this.periodfrom = periodfrom;
        }

        public String getPeriodto() {
            return periodto;
        }

        public void setPeriodto(String periodto) {
            this.periodto = periodto;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getIsreviewed() {
            return isreviewed;
        }

        public void setIsreviewed(String isreviewed) {
            this.isreviewed = isreviewed;
        }

        public String getIsadversed() {
            return isadversed;
        }

        public void setIsadversed(String isadversed) {
            this.isadversed = isadversed;
        }
        

        public String getGradeName() {
            return gradeName;
        }

        public void setGradeName(String gradeName) {
            this.gradeName = gradeName;
        }

        public String getPostGroup() {
            return postGroup;
        }

        public void setPostGroup(String postGroup) {
            this.postGroup = postGroup;
        }
        

        @Override
        public int compareTo(Parabstractdata o) {
            Date date1 = CommonFunctions.getDateFromString(this.getPeriodfrom(), "dd-MMM-yyyy");
            Date date2 = CommonFunctions.getDateFromString(o.getPeriodfrom(), "dd-MMM-yyyy");
            if (date1.compareTo(date2) > 0) {
                return 1;
            } else if (date1.compareTo(date2) < 0) {
                return -1;
            } else {
                return 0;
            }
        }

    }
}


package hrms.model.report.officewisepostgroup;

public class OfficeWisePostGroup {

    private String post = null;
    private String postgrtype = null;
    private String cadretopost = null;
    private int sancstrength;
    private int curstrength;
    private int vacancy;

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPostgrtype() {
        return postgrtype;
    }

    public void setPostgrtype(String postgrtype) {
        this.postgrtype = postgrtype;
    }

    public String getCadretopost() {
        return cadretopost;
    }

    public void setCadretopost(String cadretopost) {
        this.cadretopost = cadretopost;
    }

    public int getSancstrength() {
        return sancstrength;
    }

    public void setSancstrength(int sancstrength) {
        this.sancstrength = sancstrength;
    }

    public int getCurstrength() {
        return curstrength;
    }

    public void setCurstrength(int curstrength) {
        this.curstrength = curstrength;
    }

    public int getVacancy() {
        return vacancy;
    }

    public void setVacancy(int vacancy) {
        this.vacancy = vacancy;
    }
    
}

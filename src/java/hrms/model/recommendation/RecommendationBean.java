/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.recommendation;

/**
 *
 * @author Manisha
 */
public class RecommendationBean {

    private int recommendationId;
    private String createdondate;
    private String submittedondate;
    private String initiatedByspc;
    private String initiatedByempId;
    private String initiatedByempname;
    private String initiatedBypost;
    private String recommenadationType;

    public int getRecommendationId() {
        return recommendationId;
    }

    public void setRecommendationId(int recommendationId) {
        this.recommendationId = recommendationId;
    }

    public String getCreatedondate() {
        return createdondate;
    }

    public void setCreatedondate(String createdondate) {
        this.createdondate = createdondate;
    }

    public String getSubmittedondate() {
        return submittedondate;
    }

    public void setSubmittedondate(String submittedondate) {
        this.submittedondate = submittedondate;
    }    

    public String getInitiatedByspc() {
        return initiatedByspc;
    }

    public void setInitiatedByspc(String initiatedByspc) {
        this.initiatedByspc = initiatedByspc;
    }

    public String getInitiatedByempId() {
        return initiatedByempId;
    }

    public void setInitiatedByempId(String initiatedByempId) {
        this.initiatedByempId = initiatedByempId;
    }

    public String getInitiatedByempname() {
        return initiatedByempname;
    }

    public void setInitiatedByempname(String initiatedByempname) {
        this.initiatedByempname = initiatedByempname;
    }

    public String getInitiatedBypost() {
        return initiatedBypost;
    }

    public void setInitiatedBypost(String initiatedBypost) {
        this.initiatedBypost = initiatedBypost;
    }

    public String getRecommenadationType() {
        return recommenadationType;
    }

    public void setRecommenadationType(String recommenadationType) {
        this.recommenadationType = recommenadationType;
    }        
    

}

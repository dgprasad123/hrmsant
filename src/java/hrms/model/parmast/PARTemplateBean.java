/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.parmast;

/**
 *
 * @author Manisha
 */
public class PARTemplateBean {
    
    private int templateId;
    private String hrmsId;
    private String templateHeading;
    private String templateContent;

    
    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public String getHrmsId() {
        return hrmsId;
    }

    public void setHrmsId(String hrmsId) {
        this.hrmsId = hrmsId;
    }

    public String getTemplateHeading() {
        return templateHeading;
    }

    public void setTemplateHeading(String templateHeading) {
        this.templateHeading = templateHeading;
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }
    
    
}

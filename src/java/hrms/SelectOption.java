package hrms;

import java.io.Serializable;

public class SelectOption implements Serializable {

    private String label = null;
    private String value = null;
    private String desc = null;
    public SelectOption(){
        
    }
    public SelectOption(String label,String value){
        this.label = label;
        this.value = value;
    }
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

package hrms.model.privilege;

import java.util.ArrayList;


public class ModuleProperty {

    private String id="";
    private String text="";
    private String iconCls="";
    private String state;
    private ArrayList children=null;
    private boolean checked;
    private String moduleId=null;
    private String moduleName=null;
    private String assigned=null;
    private String linkurl=null;
    private String ifspecific=null;
    private String convertUrl=null;
    private String newWindow=null;
    private String helpUrl=null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ArrayList getChildren() {
        return children;
    }

    public void setChildren(ArrayList children) {
        this.children = children;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    
    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    public String getAssigned() {
        return assigned;
    }

    public void setLinkurl(String linkurl) {
        this.linkurl = linkurl;
    }

    public String getLinkurl() {
        return linkurl;
    }

    public void setIfspecific(String ifspecific) {
        this.ifspecific = ifspecific;
    }

    public String getIfspecific() {
        return ifspecific;
    }

    public void setConvertUrl(String convertUrl) {
        this.convertUrl = convertUrl;
    }

    public String getConvertUrl() {
        return convertUrl;
    }


    public void setNewWindow(String newWindow) {
        this.newWindow = newWindow;
    }

    public String getNewWindow() {
        return newWindow;
    }

    public void setHelpUrl(String helpUrl) {
        this.helpUrl = helpUrl;
    }

    public String getHelpUrl() {
        return helpUrl;
    }
}

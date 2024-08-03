package hrms.model.parmast;

public class HWAttachements {
    
    private int attid;
    
    private int taskid;
    
    private String originalfilename;
    
    private String diskfilename;
    
    private String filetype;
    
    private String attachyear;
    
    private String attachflag;

    public int getAttid() {
        return attid;
    }

    public void setAttid(int attid) {
        this.attid = attid;
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public String getOriginalfilename() {
        return originalfilename;
    }

    public void setOriginalfilename(String originalfilename) {
        this.originalfilename = originalfilename;
    }

    public String getDiskfilename() {
        return diskfilename;
    }

    public void setDiskfilename(String diskfilename) {
        this.diskfilename = diskfilename;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getAttachyear() {
        return attachyear;
    }

    public void setAttachyear(String attachyear) {
        this.attachyear = attachyear;
    }

    public String getAttachflag() {
        return attachflag;
    }

    public void setAttachflag(String attachflag) {
        this.attachflag = attachflag;
    }
}

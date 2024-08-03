/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.empinfo;

/**
 *
 * @author manisha
 */
public class Attachment {
    private int attachmentid;
    private String attachementname;
    private String filetype;

    public int getAttachmentid() {
        return attachmentid;
    }

    public void setAttachmentid(int attachmentid) {
        this.attachmentid = attachmentid;
    }

    public String getAttachementname() {
        return attachementname;
    }

    public void setAttachementname(String attachementname) {
        this.attachementname = attachementname;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }
    
    
}

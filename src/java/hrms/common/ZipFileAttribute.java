/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.common;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Surendra
 */
public class ZipFileAttribute {
    public List<String> diskFileName;
    public Map orgGFileName;
    

    public List getDiskFileName() {
        return diskFileName;
    }

    public void setDiskFileName(List diskFileName) {
        this.diskFileName = diskFileName;
    }

    public Map getOrgGFileName() {
        return orgGFileName;
    }

    public void setOrgGFileName(Map orgGFileName) {
        this.orgGFileName = orgGFileName;
    }
}

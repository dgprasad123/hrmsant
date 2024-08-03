package hrms.model.common;

import java.io.File;

public class FileAttribute {
    
    private String originalFileName;
    private String diskFileName;
    private String fileType;
    private File uploadAttachment;

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getDiskFileName() {
        return diskFileName;
    }

    public void setDiskFileName(String diskFileName) {
        this.diskFileName = diskFileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public File getUploadAttachment() {
        return uploadAttachment;
    }

    public void setUploadAttachment(File uploadAttachment) {
        this.uploadAttachment = uploadAttachment;
    }
    
}

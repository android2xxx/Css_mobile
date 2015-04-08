package entity;

/**
 * Created by HieuHT on 04/08/2015.
 */
public class EFileUploadResponse {
    private String FileName;
    private String FileReName;
    private String Message;
    private Boolean Status;

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFileReName() {
        return FileReName;
    }

    public void setFileReName(String fileReName) {
        FileReName = fileReName;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(Boolean status) {
        Status = status;
    }
}

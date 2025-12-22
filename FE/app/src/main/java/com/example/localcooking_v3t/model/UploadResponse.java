package com.example.localcooking_v3t.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class UploadResponse {
    @SerializedName("success")
    private Boolean success;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("fileName")
    private String fileName;
    
    @SerializedName("fileUrl")
    private String fileUrl;
    
    @SerializedName("fileUrls")
    private List<String> fileUrls;
    
    @SerializedName("count")
    private Integer count;

    public Boolean getSuccess() { return success; }
    public String getMessage() { return message; }
    public String getFileName() { return fileName; }
    public String getFileUrl() { return fileUrl; }
    public List<String> getFileUrls() { return fileUrls; }
    public Integer getCount() { return count; }
    
    public boolean isSuccess() {
        return success != null && success;
    }
}

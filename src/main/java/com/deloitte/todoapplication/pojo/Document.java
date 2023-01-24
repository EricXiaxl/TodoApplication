package com.deloitte.todoapplication.pojo;

import javax.persistence.*;
import java.time.Instant;
import java.util.Arrays;

@Entity
@Table(name = "document")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fileName;
    private Long size;
    private String fileType;
    private byte[] data;
    private String aliasName;
    private Instant createTime;
    private Long uploadorId;

    public Document() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Long getUploadorId() {
        return uploadorId;
    }

    public void setUploadorId(Long uploadorId) {
        this.uploadorId = uploadorId;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", size=" + size +
                ", fileType='" + fileType + '\'' +
                ", data=" + Arrays.toString(data) +
                ", aliasName='" + aliasName + '\'' +
                ", createTime=" + createTime +
                ", uploadorId=" + uploadorId +
                '}';
    }
}

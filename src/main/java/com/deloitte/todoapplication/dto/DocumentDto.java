package com.deloitte.todoapplication.dto;

import java.io.Serializable;

public class DocumentDto {
    private Long documentId;
    private String documentName;

    public DocumentDto() {
    }

    public DocumentDto(Long documentId, String documentName) {
        this.documentId = documentId;
        this.documentName = documentName;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }
}

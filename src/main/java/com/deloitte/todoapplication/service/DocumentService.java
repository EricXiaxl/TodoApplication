package com.deloitte.todoapplication.service;


import com.deloitte.todoapplication.pojo.Document;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {
    /**
     * Upload document
     *
     * @param file file being uploaded
     * @return uploaded document
     */
    Document saveDocument(MultipartFile file, String userId);
}

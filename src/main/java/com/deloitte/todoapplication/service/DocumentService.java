package com.deloitte.todoapplication.service;


import com.deloitte.todoapplication.pojo.Document;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface DocumentService {
    /**
     * Upload document
     *
     * @param file file being uploaded
     * @return uploaded document
     */
    Document saveDocument(MultipartFile file, String userId);

    void queryDocument(long id, HttpServletResponse response);
}

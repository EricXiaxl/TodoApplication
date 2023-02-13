package com.deloitte.todoapplication.controller;

import com.deloitte.todoapplication.domain.ResponseResult;
import com.deloitte.todoapplication.dto.DocumentDto;
import com.deloitte.todoapplication.pojo.Document;
import com.deloitte.todoapplication.service.DocumentService;
import com.deloitte.todoapplication.util.DocumentUtil;
import com.deloitte.todoapplication.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

@RestController
public class DocumentController {
    @Resource
    DocumentService documentService;

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentController.class);

    @PostMapping(value = "/uploadDocument")
    public @ResponseBody ResponseResult uploadDocument(@RequestBody MultipartFile file, HttpServletRequest httpServletRequest){
        if(file==null||file.isEmpty()){
            return new ResponseResult(HttpStatus.PRECONDITION_FAILED.value(), "File is empty");
        }

        try {
            /*String token = httpServletRequest.getHeader("token");
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();

            if (null == userId) {
                return new ResponseResult(HttpStatus.PRECONDITION_FAILED.value(), "UserId is null");
            }*/

            String userId = "1";
            File pdfFile = DocumentUtil.generatePdfFile(file);
            MultipartFile multipartFile = DocumentUtil.fileToMultipartFile(pdfFile);
            Document document = documentService.saveDocument(multipartFile, userId);
            if(document==null){
                LOGGER.error("save document error !!!");
                throw new Exception();
            }
            DocumentDto dto = new DocumentDto(document.getId(),document.getFileName());
            return new ResponseResult(HttpStatus.OK.value(), dto);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(HttpStatus.EXPECTATION_FAILED.value(), "Failed to upload");
        }
    }

    /**
     * Download document
     * @param id document id
     */
    @GetMapping(value = "/downloadDocument/{id}")
    public @ResponseBody void downLoadDocument(@PathVariable long id, HttpServletResponse response) {
        documentService.queryDocument(id,response);
    }
}

package com.deloitte.todoapplication.service.impl;

import com.deloitte.todoapplication.dao.DocumentDao;
import com.deloitte.todoapplication.pojo.Document;
import com.deloitte.todoapplication.service.DocumentService;
import com.deloitte.todoapplication.util.DocumentUtil;
import com.deloitte.todoapplication.util.IDGeneratorUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.util.Arrays;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Resource
    DocumentDao documentDao;

    @Override
    public Document saveDocument(MultipartFile file, String userId) {
        try{
            // analyse file
            String fileName = file.getOriginalFilename(); //document Name
            String prefix = fileName.substring(fileName.lastIndexOf(".") + 1); //fileType of document
            Document document = new Document();
            document.setUploadorId(Long.valueOf(userId));
            document.setFileName(fileName);
            document.setFileType(prefix);
            document.setSize(file.getSize());
            String aliasName = IDGeneratorUtil.getMessageID()+"."+prefix;
            document.setAliasName(aliasName);

            byte[] data = DocumentUtil.convertFileToByteArray(file); //convert the file into a byte array
            if(data==null){
                throw new Exception("parsing file exception");
            }

            document.setData(data);
            document.setCreateTime(Instant.now());
            return documentDao.save(document);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void queryDocument(long id,HttpServletResponse response) {
        OutputStream outputStream = null;
        try {
            Document document = documentDao.findDocumentById(id);
            if(document==null){
                throw new Exception("The document is empty.");
            }
            String fileName  = document.getFileName();
            String suffix = document.getFileType(); //get file type
            String[] images = {"jpg","jpeg","bmp","gif","png"};

            byte[] data = document.getData();

            if (data != null) {
                //File Download Settings
                response.reset();
                //Determine whether the file is an image
                if(suffix!=null && useList(images,suffix.toLowerCase())){
                    response.setContentType("image/jpg");
                }else{
                    response.setContentType("application/force-download;charset=utf-8");
                }
                response.setCharacterEncoding("utf-8");
                response.setHeader("Content-Disposition", "attachment;fileName="+fileName);
                outputStream = response.getOutputStream();
                outputStream.write(data);
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean useList(String[] arr,String targetValue){
        return Arrays.asList(arr).contains(targetValue);
    }
}

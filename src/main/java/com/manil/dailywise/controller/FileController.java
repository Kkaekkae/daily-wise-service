package com.manil.dailywise.controller;

import com.manil.dailywise.exception.KkeaException;
import com.manil.dailywise.dto.common.SingleItemResponse;
import com.manil.dailywise.dto.file.FileDTO;
import com.manil.dailywise.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/files")
public class FileController{
    private FileUploadService fileUploadService;

    @Autowired
    public void setFileUploadService(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping
    public ResponseEntity<SingleItemResponse<FileDTO>> uploadFile(
            @RequestParam("file") MultipartFile file) throws KkeaException {


        return ResponseEntity.ok(SingleItemResponse.create(
                fileUploadService.uploadFile(file)
        ));
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> uploadImages(@PathVariable String fileName) {
        try {

            return fileUploadService.getFile(fileName);

        } catch (Exception e) {
            return null;
        }
    }
}

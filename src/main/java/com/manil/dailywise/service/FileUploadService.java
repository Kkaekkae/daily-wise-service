package com.manil.dailywise.service;

import com.manil.dailywise.auth.UserPrincipal;
import com.manil.dailywise.dto.file.FileDTO;
import com.manil.dailywise.entity.FileUpload;
import com.manil.dailywise.exception.KkeaException;
import com.manil.dailywise.dto.common.RCode;
import com.manil.dailywise.repository.FileUploadRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FileUploadService {
    private FileUploadRepository fileUploadRepository;
    @Autowired
    public void setFileRepository(FileUploadRepository fileUploadRepository) {
        this.fileUploadRepository = fileUploadRepository;
    }

    @Value("${upload.path}")
    private String uploadPath;

    private String makeFileFullPath(String fileName) {
        return uploadPath+fileName;
    }

    private String makeFilePathForAPI(String fileName) {
        return "/files/"+fileName;
    }
    private String makeFilePath(Long fileId, String originFileName) {
        StringBuilder path = new StringBuilder();

        return path
                .append("_")
                .append(fileId.toString())
                .append("_")
                .append(originFileName)
                .toString();
    }


    private FileUpload saveFile(MultipartFile file) {
        try {
            FileUpload fileUpload = new FileUpload();

            fileUpload.setCreatedAt(LocalDateTime.now());
            fileUpload.setSize(file.getSize());
            fileUpload.setName(file.getOriginalFilename());

            fileUploadRepository.save(fileUpload);

            String fileName = makeFilePath(fileUpload.getId(), file.getOriginalFilename());
            String fileFullPath = makeFileFullPath(fileName);

            file.transferTo(new File(fileFullPath));

            File storedFile = new File(fileName);
            URLConnection connection = storedFile.toURL().openConnection();
            String mimeType = connection.getContentType();

            fileUpload.setFilePath(makeFilePathForAPI(fileName));
            fileUpload.setMimeType(mimeType);
            fileUploadRepository.save(fileUpload);

            log.info("mimeType  = {}", mimeType);

            return fileUpload;
        } catch (KkeaException e) {
            throw e;
        } catch (Exception e) {
            throw new KkeaException(RCode.INTERNAL_DATABASE_ERROR, e);
        }
    }

    public FileDTO uploadFile(MultipartFile file) {
        try {
            FileUpload uploadFile = saveFile(file);

            return FileDTO.of(uploadFile);

        } catch (KkeaException e) {
            throw e;
        } catch (Exception e) {
            throw new KkeaException(RCode.INTERNAL_DATABASE_ERROR, e);
        }
    }

    private String getAppIdFromFileName(String fileName) {
        String[] result = fileName.split("_");

        return result[0];
    }

    private String getFileIdFromFileName(String fileName) {
        String[] result = fileName.split("_");

        return result[1];
    }

    public ResponseEntity<Resource> getFile(String fileName) {
        try {
            String fileId = getFileIdFromFileName(fileName);
            String fileFullPath = makeFileFullPath(fileName);
            FileUpload fileDocument = fileUploadRepository.findById(Long.parseLong(fileId))
                    .orElseThrow(() -> new KkeaException(RCode.FILE_NOT_FOUND, fileName));

            File file = new File(fileFullPath);

            InputStream is = new FileInputStream(file);

            return ResponseEntity.ok()
                    .contentType(MediaType
                            .parseMediaType(fileDocument.getMimeType()))
                    .body(new InputStreamResource(is));
        } catch (KkeaException e) {
            throw e;
        } catch (Exception e) {
            throw new KkeaException(RCode.INTERNAL_DATABASE_ERROR, e);
        }
    }

    private void deleteFileWithPath(String path) {
        File file = new File(path);
        if (file.exists()){
            Boolean result = file.delete();
            log.info("file Delete  = {}", result);
        }
    }
}

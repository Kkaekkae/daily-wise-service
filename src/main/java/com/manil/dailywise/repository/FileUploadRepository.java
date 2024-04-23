package com.manil.dailywise.repository;

import com.manil.dailywise.entity.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {
}

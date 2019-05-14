package com.julu666.course.api.repositories;

import com.julu666.course.api.jpa.TKFile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FileRepository extends CrudRepository<TKFile, Long> {
    Optional<TKFile> getByFileIdAndFileIdNotNull (String fileId);


    Optional<TKFile> findByFileIdAndSourceId(String fileId, String sourceId);

    @Query("SELECT m FROM TKFile m WHERE m.fileId = :fileId ")
    Optional<TKFile> findByFileId(String fileId);

    Integer deleteByFileId(String fileId);


}

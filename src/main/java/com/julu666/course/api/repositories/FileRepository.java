package com.julu666.course.api.repositories;

import com.julu666.course.api.jpa.TKFile;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FileRepository extends CrudRepository<TKFile, Long> {
    Optional<TKFile> getByFileIdAndFileIdNotNull (String fileId);


    Optional<TKFile> findByFileIdAndSourceId(String fileId, String sourceId);

    Integer deleteByFileId(String fileId);
}

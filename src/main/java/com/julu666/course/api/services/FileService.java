package com.julu666.course.api.services;

import com.julu666.course.api.configuration.StorageProperties;
import com.julu666.course.api.exceptions.StorageException;
import com.julu666.course.api.exceptions.StorageFileNotFoundException;
import com.julu666.course.api.interfaces.Storage;

import com.julu666.course.api.utils.FileNameExt;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.api.PictureWithMetadata;

import org.jcodec.common.io.NIOUtils;

import org.jcodec.scale.AWTUtil;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;


@Service
public class FileService implements Storage {


    private final Path rootLocation;

    @Autowired
    public FileService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation())
                .toAbsolutePath()
                .normalize();
        try {
            Files.createDirectories(this.rootLocation);
        } catch (Exception ex) {
            throw new StorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public String store(MultipartFile file) {

        String filename = UUID.randomUUID().toString() + "-" + StringUtils.cleanPath(file.getOriginalFilename());
        String fileNoExtName = FileNameExt.getFileNameNoEx(filename);
        String thumbnailName = fileNoExtName + ".png";
        // 文件路径
        String filePath = dayPath() + "/" + filename;
        // 绝对路径
        String fileDirectory = this.rootLocation.toString() + "/" + dayPath();

        Path pa = Paths.get(fileDirectory)
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(pa);
        } catch (Exception ex) {
            throw new StorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }

        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }

            InputStream inputStream = file.getInputStream();

            Files.copy(inputStream, this.rootLocation.resolve(fileDirectory + "/" + filename),
                        StandardCopyOption.REPLACE_EXISTING);

            if (file.getContentType().contains("video")) {
                saveThumbnail(fileDirectory + "/" + filename, thumbnailName);
            }
            return  filePath;
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    public void saveThumbnail(String filename, String thumbnailName) {
        int frameNumber = 20;
        String thumbnailFileDirectory = this.rootLocation.toString() + "/thumbnail/" + dayPath();
        Path path = Paths.get(thumbnailFileDirectory)
                .toAbsolutePath()
                .normalize();
        try {
            Files.createDirectories(path);
            FrameGrab grab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(new File(filename)));
            grab.seekToFramePrecise(frameNumber);
            PictureWithMetadata pictureWithMetadata = grab.getNativeFrameWithMetadata();
            BufferedImage bufferedImage = AWTUtil.toBufferedImage(pictureWithMetadata.getPicture(), pictureWithMetadata.getOrientation());
            ImageIO.write(bufferedImage, "png", new File(thumbnailFileDirectory + "/" + thumbnailName));
        } catch (IOException | JCodecException ex) {
            throw new StorageException("Failed to store thumbnail " + filename, ex);
        }
    }

    // 日期路径
    public String dayPath() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "/" + month + "/" + day;
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }




}

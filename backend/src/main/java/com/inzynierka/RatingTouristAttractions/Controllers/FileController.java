package com.inzynierka.RatingTouristAttractions.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private HttpServletRequest request;

    public FileController() {
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                     @RequestParam String fileDescription) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Empty file");
        }
        Path currentRelativePath = Paths.get("");
        String filePath = currentRelativePath.toAbsolutePath() + "\\src\\main\\resources\\images\\";
        if(fileDescription.equals("User")) {
            filePath = filePath + "user\\";
        } else if(fileDescription.equals("Attraction")) {
            filePath = filePath + "attraction\\";
        }
        try {
            String realPath = filePath + file.getOriginalFilename();
            file.transferTo(new File(realPath));
            return ResponseEntity.ok().body("File uploaded");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("File upload failed");
        }
    }
}

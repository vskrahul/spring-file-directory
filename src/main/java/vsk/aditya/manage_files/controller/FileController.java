package vsk.aditya.manage_files.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import vsk.aditya.manage_files.service.FileService;

import java.util.List;

@Controller
public class FileController {
    @Autowired
    private FileService fileService;

    /**
     * Displays the index page with a list of uploaded files.
     * @param model the model to pass data to the view
     * @return the name of the HTML template to be rendered
     */
    @GetMapping("/")
    public String getIndex(Model model) {
        List<String> fileNames = fileService.getAllFiles();
        model.addAttribute("files", fileNames);
        return "index";
    }

    /**
     * Handles file upload requests.
     * @param file the file to be uploaded
     * @param model the model to pass data to the view
     * @return the redirect URL to the index page
     */
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        fileService.saveFile(file);
        model.addAttribute("message", "File uploaded successfully!");
        return "redirect:/";
    }

    /**
     * Handles file download requests.
     * @param fileName the name of the file to be downloaded
     * @return the ResponseEntity containing the file resource
     */
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        Resource resource = fileService.loadFile(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}

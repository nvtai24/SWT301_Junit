package controller;

import dao.UserDAO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1 MB
    maxFileSize = 1024 * 1024 * 10,  // 10 MB
    maxRequestSize = 1024 * 1024 * 15 // 15 MB
)
public class AddAndEditAuthorController extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String uploadPath = getServletContext().getRealPath("") + File.separator + "images" + File.separator + "author";
        
        // Ensure upload directory exists
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        // Get common parameters
        String authorName = request.getParameter("authorName");
        String authorDescription = request.getParameter("authorDescription");
        UserDAO authorDAO = new UserDAO();
        
        // Process the file upload
        Part filePart = request.getPart("authorImageFile");
        String imagePath = null;
        
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            // Generate a unique filename to prevent overwriting
            String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
            String filePath = uploadPath + File.separator + uniqueFileName;
            
            // Write the file to disk
            filePart.write(filePath);
            
            // Set the relative image path for database storage
            imagePath = "images/author/" + uniqueFileName;
        }
        
        if (action.equals("edit")) {
            int authorId = Integer.parseInt(request.getParameter("authorId"));
            
            // If no new image was uploaded, get the existing image path from the database
            if (imagePath == null) {
                // We need to get the current image path from the database
                // This method needs to be added to your DAO
                imagePath = authorDAO.getAuthorImageById(authorId);
            }
            
            // Call DAO method to update author information
            authorDAO.updateAuthor(authorId, authorName, imagePath, authorDescription);
            
        } else if (action.equals("add")) {
            // For new authors with no image uploaded, use a default image
            if (imagePath == null) {
                imagePath = "images/author/author1.jpg";
            }
            
            // Add new author
            authorDAO.addAuthor(authorName, imagePath, authorDescription);
        }
        
        response.sendRedirect("manager-author");
    }
}
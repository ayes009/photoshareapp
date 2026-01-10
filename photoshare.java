// PhotoShareApplication.java
package com.photoshare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PhotoShareApplication {
    public static void main(String[] args) {
        SpringApplication.run(PhotoShareApplication.class, args);
    }
}

// ============================================
// AzureBlobStorageConfig.java
// ============================================
package com.photoshare.config;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureBlobStorageConfig {
    
    @Value("${azure.storage.connection-string}")
    private String connectionString;
    
    @Bean
    public BlobServiceClient blobServiceClient() {
        return new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
    }
}

// ============================================
// WebConfig.java
// ============================================
package com.photoshare.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}

// ============================================
// User.java
// ============================================
package com.photoshare.model;

import java.time.LocalDateTime;

public class User {
    private String id;
    private String username;
    private String password;
    private String role;
    private LocalDateTime createdAt;
    
    public User() {}
    
    public User(String id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

// ============================================
// Photo.java
// ============================================
package com.photoshare.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Photo {
    private String id;
    private String url;
    private String title;
    private String caption;
    private String location;
    private String tags;
    private String creatorId;
    private String creatorName;
    private int likes;
    private List<String> likedBy;
    private List<Comment> comments;
    private double rating;
    private int ratingCount;
    private LocalDateTime uploadedAt;
    
    public Photo() {
        this.likedBy = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.uploadedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    
    public String getCreatorId() { return creatorId; }
    public void setCreatorId(String creatorId) { this.creatorId = creatorId; }
    
    public String getCreatorName() { return creatorName; }
    public void setCreatorName(String creatorName) { this.creatorName = creatorName; }
    
    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }
    
    public List<String> getLikedBy() { return likedBy; }
    public void setLikedBy(List<String> likedBy) { this.likedBy = likedBy; }
    
    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }
    
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    
    public int getRatingCount() { return ratingCount; }
    public void setRatingCount(int ratingCount) { this.ratingCount = ratingCount; }
    
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}

// ============================================
// Comment.java
// ============================================
package com.photoshare.model;

import java.time.LocalDateTime;

public class Comment {
    private String id;
    private String userId;
    private String username;
    private String text;
    private LocalDateTime timestamp;
    
    public Comment() {
        this.timestamp = LocalDateTime.now();
    }
    
    public Comment(String id, String userId, String username, String text) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.text = text;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}

// ============================================
// BlobStorageService.java
// ============================================
package com.photoshare.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.PublicAccessType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BlobStorageService {
    
    @Autowired
    private BlobServiceClient blobServiceClient;
    
    private final ObjectMapper objectMapper;
    
    public BlobStorageService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    
    private BlobContainerClient getOrCreateContainer(String containerName) {
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        if (!containerClient.exists()) {
            containerClient.create();
            containerClient.setAccessPolicy(PublicAccessType.BLOB, null);
        }
        return containerClient;
    }
    
    public <T> void saveObject(String containerName, String blobName, T object) throws Exception {
        BlobContainerClient containerClient = getOrCreateContainer(containerName);
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        
        String json = objectMapper.writeValueAsString(object);
        byte[] data = json.getBytes();
        
        InputStream inputStream = new ByteArrayInputStream(data);
        BlobHttpHeaders headers = new BlobHttpHeaders().setContentType("application/json");
        
        blobClient.upload(inputStream, data.length, true);
        blobClient.setHttpHeaders(headers);
    }
    
    public <T> T getObject(String containerName, String blobName, Class<T> clazz) throws Exception {
        BlobContainerClient containerClient = getOrCreateContainer(containerName);
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        
        if (!blobClient.exists()) {
            return null;
        }
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blobClient.download(outputStream);
        String json = outputStream.toString();
        
        return objectMapper.readValue(json, clazz);
    }
    
    public <T> List<T> listObjects(String containerName, Class<T> clazz) throws Exception {
        BlobContainerClient containerClient = getOrCreateContainer(containerName);
        List<T> objects = new ArrayList<>();
        
        containerClient.listBlobs().forEach(blobItem -> {
            try {
                BlobClient blobClient = containerClient.getBlobClient(blobItem.getName());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                blobClient.download(outputStream);
                String json = outputStream.toString();
                objects.add(objectMapper.readValue(json, clazz));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        return objects;
    }
    
    public void deleteObject(String containerName, String blobName) {
        BlobContainerClient containerClient = getOrCreateContainer(containerName);
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        blobClient.delete();
    }
    
    public String uploadImage(MultipartFile file) throws Exception {
        BlobContainerClient containerClient = getOrCreateContainer("images");
        String blobName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        
        BlobHttpHeaders headers = new BlobHttpHeaders().setContentType(file.getContentType());
        blobClient.upload(file.getInputStream(), file.getSize(), true);
        blobClient.setHttpHeaders(headers);
        
        return blobClient.getBlobUrl();
    }
}

// ============================================
// AuthController.java
// ============================================
package com.photoshare.controller;

import com.photoshare.model.User;
import com.photoshare.service.BlobStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private BlobStorageService blobStorageService;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");
            String role = request.get("role");
            
            if (username == null || password == null || role == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "All fields are required"));
            }
            
            // Check if user exists
            User existingUser = blobStorageService.getObject("users", username + ".json", User.class);
            if (existingUser != null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Username already exists"));
            }
            
            // Create new user
            String hashedPassword = passwordEncoder.encode(password);
            User user = new User(UUID.randomUUID().toString(), username, hashedPassword, role);
            
            // Save to blob storage
            blobStorageService.saveObject("users", username + ".json", user);
            
            // Create token (simple UUID for now)
            String token = "token-" + UUID.randomUUID().toString();
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "role", user.getRole()
            ));
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Signup failed"));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");
            String role = request.get("role");
            
            if (username == null || password == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Credentials required"));
            }
            
            // Get user
            User user = blobStorageService.getObject("users", username + ".json", User.class);
            if (user == null || !user.getRole().equals(role)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid credentials"));
            }
            
            // Verify password
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid credentials"));
            }
            
            // Create token
            String token = "token-" + UUID.randomUUID().toString();
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "role", user.getRole()
            ));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Login failed"));
        }
    }
}

// ============================================
// PhotoController.java
// ============================================
package com.photoshare.controller;

import com.photoshare.model.Comment;
import com.photoshare.model.Photo;
import com.photoshare.service.BlobStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {
    
    @Autowired
    private BlobStorageService blobStorageService;
    
    @PostMapping
    public ResponseEntity<?> uploadPhoto(
            @RequestParam(required = false) MultipartFile image,
            @RequestParam String title,
            @RequestParam(required = false) String caption,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) String imageUrl,
            @RequestParam String userId,
            @RequestParam String username) {
        try {
            if (title == null || title.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Title is required"));
            }
            
            String url;
            if (image != null && !image.isEmpty()) {
                url = blobStorageService.uploadImage(image);
            } else if (imageUrl != null && !imageUrl.isEmpty()) {
                url = imageUrl;
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Image is required"));
            }
            
            Photo photo = new Photo();
            photo.setId(UUID.randomUUID().toString());
            photo.setUrl(url);
            photo.setTitle(title);
            photo.setCaption(caption != null ? caption : "");
            photo.setLocation(location != null ? location : "");
            photo.setTags(tags != null ? tags : "");
            photo.setCreatorId(userId);
            photo.setCreatorName(username);
            
            blobStorageService.saveObject("photos", photo.getId() + ".json", photo);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(photo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Upload failed"));
        }
    }
    
    @GetMapping
    public ResponseEntity<?> getAllPhotos() {
        try {
            List<Photo> photos = blobStorageService.listObjects("photos", Photo.class);
            // Sort by upload date (newest first)
            photos = photos.stream()
                    .sorted((a, b) -> b.getUploadedAt().compareTo(a.getUploadedAt()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(photos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch photos"));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getPhoto(@PathVariable String id) {
        try {
            Photo photo = blobStorageService.getObject("photos", id + ".json", Photo.class);
            if (photo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Photo not found"));
            }
            return ResponseEntity.ok(photo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch photo"));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePhoto(@PathVariable String id, @RequestBody Photo updatedPhoto) {
        try {
            Photo photo = blobStorageService.getObject("photos", id + ".json", Photo.class);
            if (photo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Photo not found"));
            }
            
            photo.setTitle(updatedPhoto.getTitle());
            photo.setCaption(updatedPhoto.getCaption());
            photo.setLocation(updatedPhoto.getLocation());
            photo.setTags(updatedPhoto.getTags());
            
            blobStorageService.saveObject("photos", id + ".json", photo);
            return ResponseEntity.ok(photo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Update failed"));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePhoto(@PathVariable String id, @RequestParam String userId) {
        try {
            Photo photo = blobStorageService.getObject("photos", id + ".json", Photo.class);
            if (photo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Photo not found"));
            }
            
            if (!photo.getCreatorId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Not authorized"));
            }
            
            blobStorageService.deleteObject("photos", id + ".json");
            return ResponseEntity.ok(Map.of("message", "Deleted successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Delete failed"));
        }
    }
    
    @PostMapping("/{id}/like")
    public ResponseEntity<?> likePhoto(@PathVariable String id, @RequestParam String userId) {
        try {
            Photo photo = blobStorageService.getObject("photos", id + ".json", Photo.class);
            if (photo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Photo not found"));
            }
            
            if (!photo.getLikedBy().contains(userId)) {
                photo.getLikedBy().add(userId);
                photo.setLikes(photo.getLikes() + 1);
                blobStorageService.saveObject("photos", id + ".json", photo);
            }
            
            return ResponseEntity.ok(photo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Like failed"));
        }
    }
    
    @PostMapping("/{id}/comment")
    public ResponseEntity<?> addComment(@PathVariable String id, @RequestBody Map<String, String> request) {
        try {
            String text = request.get("text");
            String userId = request.get("userId");
            String username = request.get("username");
            
            if (text == null || text.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Comment text required"));
            }
            
            Photo photo = blobStorageService.getObject("photos", id + ".json", Photo.class);
            if (photo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Photo not found"));
            }
            
            Comment comment = new Comment(UUID.randomUUID().toString(), userId, username, text);
            photo.getComments().add(comment);
            
            blobStorageService.saveObject("photos", id + ".json", photo);
            return ResponseEntity.ok(comment);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Comment failed"));
        }
    }
    
    @PostMapping("/{id}/rate")
    public ResponseEntity<?> ratePhoto(@PathVariable String id, @RequestBody Map<String, Integer> request) {
        try {
            Integer rating = request.get("rating");
            if (rating == null || rating < 1 || rating > 5) {
                return ResponseEntity.badRequest().body(Map.of("error", "Rating must be 1-5"));
            }
            
            Photo photo = blobStorageService.getObject("photos", id + ".json", Photo.class);
            if (photo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Photo not found"));
            }
            
            int newRatingCount = photo.getRatingCount() + 1;
            double newRating = ((photo.getRating() * photo.getRatingCount()) + rating) / newRatingCount;
            
            photo.setRating(newRating);
            photo.setRatingCount(newRatingCount);
            
            blobStorageService.saveObject("photos", id + ".json", photo);
            return ResponseEntity.ok(photo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Rating failed"));
        }
    }
}

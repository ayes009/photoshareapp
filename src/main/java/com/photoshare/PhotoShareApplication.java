package com.photoshare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
public class PhotoShareApplication {
    public static void main(String[] args) {
        SpringApplication.run(PhotoShareApplication.class, args);
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}
```

**Continue creating all the other Java files** from the artifact in the correct package structure.

---

## 🔧 **FIX IT - METHOD 2: Upload Correct ZIP to GitHub**

### **Step 1: Create Correct Structure on Your Computer**

1. **Create a folder:** `photoshare-backend`
2. **Inside it, create:**
```
photoshare-backend/
├── pom.xml
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── photoshare/
│       │           ├── PhotoShareApplication.java
│       │           ├── config/
│       │           ├── controller/
│       │           ├── model/
│       │           └── service/
│       └── resources/
│           └── application.properties
```

### **Step 2: Delete Old Repository**

1. **Go to GitHub repository**
2. **Settings** (tab at top)
3. **Scroll to bottom** → **"Delete this repository"**
4. **Follow prompts**

### **Step 3: Create New Repository**

1. **Go to:** https://github.com/new
2. **Name:** `photoshare-backend`
3. **Public**
4. **✅ Add README**
5. **Create**

### **Step 4: Upload Correct Files**

1. **Click "Add file"** → **"Upload files"**
2. **Drag ONLY the CONTENTS** of `photoshare-backend` folder
   - NOT the folder itself!
   - Just drag: `pom.xml`, `src/` folder, etc.
3. **Commit**

---

## 🔧 **FIX IT - METHOD 3: Configure Azure Build Path**

If you want to keep your current structure, tell Azure where to find the pom.xml:

### **In Azure Portal:**

1. **Go to your App Service**
2. **Deployment Center** → **Settings**
3. **Look for "Build configuration"**
4. **Add this:**
   - **Build command:** `cd YOUR_FOLDER_NAME && mvn clean package`
   - **Startup command:** `java -jar YOUR_FOLDER_NAME/target/photoshare-backend-1.0.0.jar`

Replace `YOUR_FOLDER_NAME` with wherever your pom.xml actually is.

---

## ✅ **CORRECT FINAL STRUCTURE**

Your GitHub repo should look EXACTLY like this:
```
photoshareapp/ (or photoshare-backend/)
│
├── pom.xml                          ← Must be here!
├── README.md
│
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── photoshare/
        │           ├── PhotoShareApplication.java
        │           ├── config/
        │           │   ├── AzureBlobStorageConfig.java
        │           │   └── WebConfig.java
        │           ├── controller/
        │           │   ├── AuthController.java
        │           │   └── PhotoController.java
        │           ├── model/
        │           │   ├── Comment.java
        │           │   ├── Photo.java
        │           │   └── User.java
        │           └── service/
        │               └── BlobStorageService.java
        └── resources/
            └── application.properties

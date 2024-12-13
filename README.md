# PawCheck - Pet Care Management System

PawCheck: Advanced Eye Scan Disease Detection Technology for Easier Health Checks

## Main Features
- Checking symptoms of disease in dogs with AI Machine Learning
- Displays news articles related to dogs

## Tools Used
- Android Studio
- Firebase: Backend for authentication and data storage.
- Room Database: For storing data offline.

## Installation Guide
- Clone repository [https://github.com/PawCheck/Android](https://github.com/PawCheck/Android)
- Open the project in Android Studio.
- Follow the [Firebase setup](https://firebase.google.com/docs/android/setup?hl=en).  
 ```
    implementation(platform(libs.firebase.bom))
    implementation(libs.google.firebase.auth.ktx)
    implementation(libs.google.firebase.firestore.ktx)
```
- Run the application on the emulator or Android device.

## Features in the App
- **Splash Screen**  
The introductory screen displayed when the app is launched.

- **Authentication System** (Firebase Auth)

  - **Login**: Users can securely log in using their email and password to access the application.

  - **Sign Up**: Enables users to register independently for a personalized account.

  - **Logout**: Allows users to securely log out of their accounts.

- **Edit Profile Name** (Firestore Database)
  - Allows users to edit their profile name in firestore and local (room)

- **Home**  
The main screen displayed upon successful login, serving as the starting point for navigation.

- **Scan Dog's Eye Disease**  
Provides functionality to scan and analyze a dog's eyes for potential health issues using AI/ML-based detection.

- **Results**  
Displays the outcomes or diagnosis from the dog's eye scan.

- **Pet Health Articles**  
  - Provides educational articles about pet health.
  - Includes an Offline Mode for accessing articles even without an internet connection (using Room database).

- **Drug Products**  
Lists and provides details on recommended or related drug products for pets.

- **Profile**  
Displays the user's information and allows updates to personal details in offline mode.

- **Settings**  
Theme Settings: Users can customize the app's theme (light or dark mode).

## **Dependencies**  
- Firebase Authentication
- Firebase Firestore
- Room Database
- TensorFlow Lite (AI/ML Kit)
- Retrofit (Networking)
- Glide (Image Loading)
- CameraX (Camera API)
- Hilt (Dependency Injection)  

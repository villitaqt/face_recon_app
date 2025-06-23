# Face Recognition App

An Android application built with Kotlin, Jetpack Compose, and modern Android libraries for face recognition using camera capture and API integration.

## Features

- **Camera Integration**: Uses CameraX for camera preview and image capture
- **Face Recognition**: Captures images and sends them to a backend API for face recognition
- **Modern UI**: Built with Jetpack Compose and Material 3 design
- **Dependency Injection**: Uses Hilt for clean dependency management
- **Networking**: Retrofit for API communication
- **Permission Handling**: Runtime camera permission requests using Accompanist Permissions
- **Backend Health Check**: Test connection to Python backend before capturing images

## Project Structure

```
app/src/main/java/com/facerecon/app/
├── data/
│   ├── api/
│   │   └── ApiService.kt          # Retrofit API service interface
│   ├── models/
│   │   ├── User.kt                # User data model
│   │   └── ApiResponse.kt         # API response models
│   └── repository/
│       └── FaceRecognitionRepository.kt  # Repository for API calls
├── di/
│   └── NetworkModule.kt           # Hilt dependency injection module
├── ui/
│   ├── screens/
│   │   └── CameraScreen.kt        # Main camera screen with preview
│   ├── theme/                     # Compose theme definitions
│   └── viewmodel/
│       └── FaceRecognitionViewModel.kt  # ViewModel for state management
├── utils/
│   ├── CameraUtils.kt             # Camera utility functions
│   └── NetworkConfig.kt           # Network configuration utility
├── FaceRecognitionApplication.kt  # Hilt application class
└── MainActivity.kt                # Main activity with Compose UI
```

## Setup

### 1. **Network Configuration**

The app is configured to connect to your Python backend. Update the configuration in `NetworkConfig.kt`:

```kotlin
// For Android Emulator - connects to localhost on host machine
const val USE_EMULATOR = true  // Set to false for physical device

// For Physical Device - update this to your PC's IP address
private const val PHYSICAL_DEVICE_BASE_URL = "http://192.168.18.194:8000/"
```

**For Android Emulator:**
- Set `USE_EMULATOR = true`
- Uses `http://10.0.2.2:8000/` (special Android emulator IP for localhost)

**For Physical Device:**
- Set `USE_EMULATOR = false`
- Update `PHYSICAL_DEVICE_BASE_URL` to your PC's actual IP address
- Ensure both devices are on the same network

### 2. **Python Backend Requirements**

Your Python backend should have these endpoints:
- `GET /health` - Returns `{"status": "healthy", "message": "API funcionando correctamente"}`
- `POST /recognize` - Accepts multipart form data with an image file

### 3. **Build and Run**

The app is ready to build and run on Android devices/emulators

## API Integration

The app expects a face recognition API with the following endpoints:

### Health Check Endpoint:
- `GET /health` - Returns backend status
- Expected response: `{"status": "healthy", "message": "API funcionando correctamente"}`

### Recognition Endpoint:
- `POST /recognize` - Accepts multipart form data with an image file
- Returns JSON response with user recognition data

### Expected API Response Format:
```json
{
  "success": true,
  "user": {
    "id": "user123",
    "name": "John Doe",
    "confidence": 0.95
  },
  "message": "Face recognized successfully"
}
```

## Testing the Connection

1. **Launch the app** and grant camera permissions
2. **Check Network Configuration** - The app shows which mode you're in
3. **Test Backend Connection** - Tap "Test Backend Connection" to verify connectivity
4. **Capture and Recognize** - Take a photo to test face recognition

## Permissions

The app requires the following permissions:
- `CAMERA` - For camera access
- `INTERNET` - For API communication
- `ACCESS_NETWORK_STATE` - For network state monitoring

## Testing

The project includes a basic UI test that verifies the main screen launches correctly:
- `MainActivityTest.kt` - Tests that the camera permission screen displays

## Dependencies

- **Jetpack Compose**: Modern UI toolkit
- **CameraX**: Camera functionality
- **Hilt**: Dependency injection
- **Retrofit**: Network communication
- **Accompanist Permissions**: Runtime permission handling
- **Material 3**: Design system

## Minimum Requirements

- Android API Level 24 (Android 7.0)
- Target API Level 34 (Android 14)
- Kotlin 1.9+
- Java 17

## Troubleshooting

### Connection Issues:
1. **Emulator**: Ensure Python backend is running on `localhost:8000`
2. **Physical Device**: 
   - Check that both devices are on the same network
   - Verify your PC's IP address is correct
   - Ensure firewall allows connections on port 8000

### Camera Issues:
1. Grant camera permissions when prompted
2. Ensure device has a front camera (app uses front camera by default)

### Backend Issues:
1. Use the "Test Backend Connection" button to verify connectivity
2. Check backend logs for any errors
3. Ensure backend is running and accessible # face_recon_app

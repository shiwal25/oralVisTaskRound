
# OralVis App

OralVis is an Android application for managing and visualizing user sessions containing metadata and images. The app allows you to create, save, and view sessions efficiently while handling local storage for images and SQLite DB formetadata.


## Features

- Create sessions with multiple images.

- Store session metadata (name, age, session ID).

- Pass data between fragments and activities.

- Handle runtime permissions for camera and storage.



## Project Structure
```
OralVis/
│
├─ app/
│  ├─ src/
│  │  ├─ main/
│  │  │  ├─ java/com/example/oralvistaskround/
│  │  │  │  ├─ activities/         
│  │  │  │  ├─ fragments/         
│  │  │  │  ├─ models/             
│  │  │  │  └─ utils/              
│  │  │  ├─ res/
│  │  │  │  ├─ layout/            
│  │  │  │  ├─ drawable/           
│  │  │  │  └─ values/             
│  │  │  └─ AndroidManifest.xml
│  │  └─ build.gradle
├─ build.gradle
└─ settings.gradle
```
## Dependencies
Add the following in app/build.gradle:
```
dependencies {
    val fragmentVersion = "1.8.9"
    implementation(libs.androidx.fragment.ktx)
    val camerax_version = "1.3.4"
    implementation (libs.androidx.camera.core)
    implementation (libs.androidx.camera.camera2)
    implementation (libs.androidx.camera.lifecycle)
    implementation (libs.androidx.camera.view)
    implementation (libs.androidx.camera.extensions)
}
```
## Permissions
Add the following in AndroidManifest.xml:
```
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```
## How to Run the App

1. Clone the Repository
git clone https://github.com/shiwal25/oralVisTaskRound.git
cd OralVis


2. Open in Android Studio
File → Open → Select the project folder.

3. Sync Gradle
Click “Sync Now” if prompted.

4. Set SDK
Minimum SDK: 29
Target SDK: 33

5. Run on Device/Emulator
Grant runtime permissions for Camera and Storage.
Test adding, viewing, and deleting sessions.

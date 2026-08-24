# GDrive Music Player for Android TV (Optimized for Mi 4x TV)

A production-grade, highly-robust Android TV application designed specifically for playing audio files directly synced from a Google Drive folder. Optimized for D-pad navigation, low-memory footprints (ideal for Mi 4x TV), and continuous 24/7 background audio playback.

---

## 🚀 Key Features & Implementation Details

### 1. 📺 Android TV UI & D-Pad Optimization
* Built using **Jetpack Compose for TV** (`androidx.tv:tv-material3` and `androidx.tv:tv-foundation`).
* Uses a custom TV-optimized component [`DpadFocusableItem`](file:///Users/tejas.dhanoa/GDriveMusicTVApp/app/src/main/java/com/musicplayer/gdrivetv/ui/components/DpadFocusableItem.kt) that highlights focus changes instantly with a green border and background contrast. This provides distinct D-pad focus state visibility across the interface.
* Implements a 3-column responsive grid landscape layout matching TV widescreen layouts perfectly.

### 2. ☁️ Google Drive Syncing & Recursive Scanning
* Uses [`GDriveHelper`](file:///Users/tejas.dhanoa/GDriveMusicTVApp/app/src/main/java/com/musicplayer/gdrivetv/sync/GDriveHelper.kt) to query the Google Drive API v3.
* Configured by default to target your Google Drive folder: `1-mRwu6GyYVIX_AJYI2dP-HtY6Hoip3Zd`.
* Recursively walks folder directories, indexing all audio files and filtering out irrelevant document/video extensions while cataloging track titles and remote media URIs.

### 3. 💾 Offline Playback & Local Cache
* Integrates a local **Room Database** ([`AppDatabase`](file:///Users/tejas.dhanoa/GDriveMusicTVApp/app/src/main/java/com/musicplayer/gdrivetv/database/AppDatabase.kt)) caching metadata locally.
* Uses [`DownloadWorker`](file:///Users/tejas.dhanoa/GDriveMusicTVApp/app/src/main/java/com/musicplayer/gdrivetv/sync/DownloadWorker.kt) via **WorkManager** to queue downloads of remote songs to the local storage directories.
* The playback engine automatically switches from the remote Google Drive streaming URL to local file paths when a song is marked `isDownloaded = true`, enabling seamless offline playback.

### 4. 🔄 Automatic Library Refreshing
* A periodic [`DriveSyncWorker`](file:///Users/tejas.dhanoa/GDriveMusicTVApp/app/src/main/java/com/musicplayer/gdrivetv/sync/DriveSyncWorker.kt) is scheduled via **WorkManager** to execute every 6 hours.
* It compares local database records with remote Google Drive folder directory states, automatically scheduling new items for download and deleting cached items no longer on Google Drive.

### 5. ♾️ Robust 24/7 Playback (Quality Not Decreasing)
* Built on top of **Google Jetpack Media3 (ExoPlayer & MediaSessionService)**, ensuring maximum performance, hardware decoding, and native notification/system control integration.
* **CPU WakeLocks & WifiLocks**: The service starts as a Foreground Service and acquires persistent Partial CPU WakeLocks and High-Performance Wi-Fi Locks during playback to prevent Android OS (specifically Xiaomi's aggressive PatchWall optimization) from putting the hardware/network to sleep.
* **Auto-Recovery**: Handles network interruptions or transient decoding errors in the player listener by automatically attempting to resume playback or skipping to the next available track instead of stopping/crashing.
* **Audio Focus Management**: Intercepts focus changes, ducking music volume when notifications sound and resuming automatically.

### 6. 🗂️ Local Playlists
* Includes a custom Room table schema allowing users to create playlists, view them, add tracks from the main library, and play them in sequence.

### 7. ⚙️ Mi 4x TV Optimizations (1GB - 2GB RAM / API 28 / Android 9)
* **Large Heap Request**: Manifest enables `android:largeHeap="true"` to prevent OutOfMemory crashes during extensive metadata/image cache loading.
* **Low-Memory Image Loading**: Coil image config is scaled down, preventing heavy bitmap caching.
* **No Heavy Animations**: UI transitions are kept simple and lightweight to maintain 60 FPS scrolling and reduce CPU rendering stress.

---

## 🛠️ Project Structure

* **[`settings.gradle`](file:///Users/tejas.dhanoa/GDriveMusicTVApp/settings.gradle)**: Project structure definition.
* **[`build.gradle`](file:///Users/tejas.dhanoa/GDriveMusicTVApp/build.gradle)**: Root build configuration.
* **[`app/build.gradle`](file:///Users/tejas.dhanoa/GDriveMusicTVApp/app/build.gradle)**: Module configuration including Media3, Room, WorkManager, and Compose dependencies.
* **[`AndroidManifest.xml`](file:///Users/tejas.dhanoa/GDriveMusicTVApp/app/src/main/AndroidManifest.xml)**: TV launcher declarations, background media services, and WakeLock/Internet permissions.
* **Source Directories**:
  * **[`database/`](file:///Users/tejas.dhanoa/GDriveMusicTVApp/app/src/main/java/com/musicplayer/gdrivetv/database/)**: Tables, DAOs, and DB config.
  * **[`sync/`](file:///Users/tejas.dhanoa/GDriveMusicTVApp/app/src/main/java/com/musicplayer/gdrivetv/sync/)**: Retrofit GDrive queries, Sync Worker, and Download Worker.
  * **[`playback/`](file:///Users/tejas.dhanoa/GDriveMusicTVApp/app/src/main/java/com/musicplayer/gdrivetv/playback/)**: Foreground MediaSession playback service with active locks.
  * **[`ui/`](file:///Users/tejas.dhanoa/GDriveMusicTVApp/app/src/main/java/com/musicplayer/gdrivetv/ui/)**: TV layout components, main navigation wrapper, themes, library grid, and playing screen.

---

## 🏗️ How to Build and Run the App

1. **Prerequisites**: Install [Android Studio](https://developer.android.com/studio) and ensure you have Java Development Kit (JDK 17) configured.
2. **Open the Project**: Open the directory `/Users/tejas.dhanoa/GDriveMusicTVApp` in Android Studio.
3. **Google API Credentials**:
   * For private folders, go to the **Settings** screen in the app and set your API Key or OAuth Token.
   * If the folder is publicly shared (like the specified folder), it can be queried using a public API Key.
4. **Compile and Run**: Run the `app` module on a connected Android TV device or emulator.

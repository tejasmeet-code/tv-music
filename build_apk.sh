#!/bin/bash
set -e

echo "=== GDrive Music TV: Automated APK Builder ==="

# 1. Setup Java Home (using the portable JDK 17 we installed)
export JAVA_HOME="/Users/tejas.dhanoa/local_tools/jdk-17.0.20.1+1/Contents/Home"
export PATH="$JAVA_HOME/bin:$PATH"

echo "Using Java: $(java -version 2>&1 | head -n 1)"

# 2. Setup Android SDK
export ANDROID_HOME="/Users/tejas.dhanoa/local_tools/android-sdk"
mkdir -p "$ANDROID_HOME"

if [ ! -d "$ANDROID_HOME/cmdline-tools" ]; then
    echo "Downloading Android Command Line Tools..."
    mkdir -p "/Users/tejas.dhanoa/local_tools/tmp"
    curl -L "https://dl.google.com/android/repository/commandlinetools-mac-11076708_latest.zip" -o "/Users/tejas.dhanoa/local_tools/tmp/cmdline-tools.zip"
    
    echo "Extracting Command Line Tools..."
    unzip -q "/Users/tejas.dhanoa/local_tools/tmp/cmdline-tools.zip" -d "/Users/tejas.dhanoa/local_tools/tmp"
    
    # Correct structure for sdkmanager
    mkdir -p "$ANDROID_HOME/cmdline-tools/latest"
    mv /Users/tejas.dhanoa/local_tools/tmp/cmdline-tools/* "$ANDROID_HOME/cmdline-tools/latest/" || true
    rm -rf "/Users/tejas.dhanoa/local_tools/tmp"
fi

export PATH="$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$PATH"

# 3. Install SDK Packages
echo "Accepting SDK Licenses..."
yes | sdkmanager --licenses --sdk_root="$ANDROID_HOME" > /dev/null

echo "Installing Android Platform 34 & Build Tools..."
sdkmanager --sdk_root="$ANDROID_HOME" "platforms;android-34" "build-tools;34.0.0" "platform-tools" > /dev/null

# 4. Download and extract Gradle if not present
GRADLE_HOME="/Users/tejas.dhanoa/local_tools/gradle-8.0"
if [ ! -d "$GRADLE_HOME" ]; then
    echo "Downloading Gradle 8.0..."
    curl -L "https://services.gradle.org/distributions/gradle-8.0-bin.zip" -o "/Users/tejas.dhanoa/local_tools/gradle-8.0-bin.zip"
    echo "Extracting Gradle..."
    unzip -q "/Users/tejas.dhanoa/local_tools/gradle-8.0-bin.zip" -d "/Users/tejas.dhanoa/local_tools/"
    rm "/Users/tejas.dhanoa/local_tools/gradle-8.0-bin.zip"
fi

# 5. Build Debug APK (Run local gradle directly to bypass wrapper downloads)
echo "Compiling Android TV App APK..."
$GRADLE_HOME/bin/gradle assembleDebug

echo "=== BUILD SUCCESSFUL ==="
echo "Your downloadable APK is ready at:"
echo "/Users/tejas.dhanoa/tv-music-ref/app/build/outputs/apk/debug/app-debug.apk"

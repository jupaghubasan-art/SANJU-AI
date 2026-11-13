# SANJU AI — Stage 1

Native Android starter project for the SANJU AI Roblox storytelling studio.

## Stage 1 scope
- Native Kotlin + Jetpack Compose + Material 3 dashboard.
- Dark SANJU AI visual identity.
- Master prompt editor with live word/character counter and clear action.
- Three model choices (Gemini, Claude, GPT) as UI selections only.
- Two generation-mode selections (One-click and Scene-by-scene).
- Studio / Projects / Settings navigation.
- Honest status messaging: AI and video generation are not connected in Stage 1.

## Requirements
- Android Studio with Android SDK Platform 36 installed.
- JDK 17.
- Android Gradle Plugin 8.13.2 and Gradle 8.13.
- Kotlin 2.2.21.
- Minimum Android version: Android 7.0 (API 24).

## Build
1. Open this folder in Android Studio.
2. Allow Gradle sync to finish.
3. Select the `app` run configuration and run on a device/emulator.
4. To build a debug APK: **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
5. APK output: `app/build/outputs/apk/debug/app-debug.apk`.

## GitHub Actions
Push the project to GitHub. The workflow at `.github/workflows/android.yml` builds a debug APK and uploads it as an Actions artifact.

## Important
Stage 1 is the working UI foundation, not the finished AI video generator. No model API or video provider is called. API key security, prompt persistence, actual story generation, provider adapters, scene rendering, and export are later stages. Do not put API keys in source code or commit them to Git.

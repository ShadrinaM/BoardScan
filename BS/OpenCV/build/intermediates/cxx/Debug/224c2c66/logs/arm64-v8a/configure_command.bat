@echo off
"C:\\AndroidSdk\\cmake\\3.22.1\\bin\\cmake.exe" ^
  "-HM:\\Git\\BoardScan\\BS\\OpenCV\\libcxx_helper" ^
  "-DCMAKE_SYSTEM_NAME=Android" ^
  "-DCMAKE_EXPORT_COMPILE_COMMANDS=ON" ^
  "-DCMAKE_SYSTEM_VERSION=21" ^
  "-DANDROID_PLATFORM=android-21" ^
  "-DANDROID_ABI=arm64-v8a" ^
  "-DCMAKE_ANDROID_ARCH_ABI=arm64-v8a" ^
  "-DANDROID_NDK=C:\\AndroidSdk\\ndk\\27.0.12077973" ^
  "-DCMAKE_ANDROID_NDK=C:\\AndroidSdk\\ndk\\27.0.12077973" ^
  "-DCMAKE_TOOLCHAIN_FILE=C:\\AndroidSdk\\ndk\\27.0.12077973\\build\\cmake\\android.toolchain.cmake" ^
  "-DCMAKE_MAKE_PROGRAM=C:\\AndroidSdk\\cmake\\3.22.1\\bin\\ninja.exe" ^
  "-DCMAKE_LIBRARY_OUTPUT_DIRECTORY=M:\\Git\\BoardScan\\BS\\OpenCV\\build\\intermediates\\cxx\\Debug\\224c2c66\\obj\\arm64-v8a" ^
  "-DCMAKE_RUNTIME_OUTPUT_DIRECTORY=M:\\Git\\BoardScan\\BS\\OpenCV\\build\\intermediates\\cxx\\Debug\\224c2c66\\obj\\arm64-v8a" ^
  "-DCMAKE_BUILD_TYPE=Debug" ^
  "-BM:\\Git\\BoardScan\\BS\\OpenCV\\.cxx\\Debug\\224c2c66\\arm64-v8a" ^
  -GNinja ^
  "-DANDROID_STL=c++_shared"

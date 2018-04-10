

cmake_minimum_required(VERSION 3.4.1)

include_directories("/home/zfq/Mobile/Demo/Android/MyNdk/app/ComputeLibrary" "/home/zfq/Mobile/Demo/Android/MyNdk/app/ComputeLibrary/include" )
link_directories("/home/zfq/Mobile/Demo/Android/MyNdk/app/lib/arm64-v8a")
add_library( # Sets the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/native-lib.cpp
             src/main/cpp/Utils.cpp)

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       native-lib

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib}
                       libarm_compute.so
                       libarm_compute_core.so)

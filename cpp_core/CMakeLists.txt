cmake_minimum_required(VERSION 3.15)
project(ProdlyCore VERSION 1.0.0 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Find Java for JNI
find_package(JNI REQUIRED)

# Include directories
include_directories(${JNI_INCLUDE_DIRS})
include_directories(${JNI_INCLUDE_DIRS2})
include_directories(include)

# Source files - Core DSA implementations
set(CORE_SOURCES
    src/Graph.cpp
    src/AVLTree.cpp
)

# Source files - Module implementations
set(MODULE_SOURCES
    src/VendorHashTable.cpp
    src/VendorGraph.cpp
    src/VendorLockInAnalyzer.cpp
    src/MigrationGraph.cpp
    src/MigrationDifficultyAnalyzer.cpp
    src/VendorAVLTree.cpp
    src/ExitReadinessGraph.cpp
    src/ExitReadinessDashboard.cpp
)

# Source files - JNI interface
set(JNI_SOURCES
    JNI/ProdlyJNI.cpp
)

# Create shared library for JNI
add_library(prodlyjni SHARED
    ${CORE_SOURCES}
    ${MODULE_SOURCES}
    ${JNI_SOURCES}
)

target_link_libraries(prodlyjni ${JNI_LIBRARIES})

# Platform-specific settings
if(WIN32)
    set_target_properties(prodlyjni PROPERTIES
        OUTPUT_NAME "prodlyjni"
        PREFIX ""
    )
elseif(UNIX AND NOT APPLE)
    set_target_properties(prodlyjni PROPERTIES
        OUTPUT_NAME "prodlyjni"
        PREFIX "lib"
    )
elseif(APPLE)
    set_target_properties(prodlyjni PROPERTIES
        OUTPUT_NAME "prodlyjni"
        PREFIX "lib"
    )
endif()

# Installation
install(TARGETS prodlyjni
    LIBRARY DESTINATION lib
    ARCHIVE DESTINATION lib
)

# Copy headers for reference (optional)
install(DIRECTORY include/
    DESTINATION include/prodly
    FILES_MATCHING PATTERN "*.h"
)


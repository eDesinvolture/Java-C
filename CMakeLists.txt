cmake_minimum_required(VERSION 3.16)
project(HeroGDExtension)



#  set(CMAKE_C_COMPILER gcc)
#  set(CMAKE_CXX_COMPILER gcc)
# set(CMAKE_CXX_STANDARD 17)
# set(CMAKE_CXX_STANDARD_REQUIRED ON)

# надо тестить
# set(CMAKE_CXX_STANDARD 23)
# set(CMAKE_CXX_STANDARD_REQUIRED ON)

message(STATUS "[[CXX FLAGS]]" ${CMAKE_CXX_FLAGS})
# message(STATUS "[[CXX FLAGS_DEBUG]] " ${CMAKE_CXX_FLAGS_DEBUG})
# message(STATUS "[[CXX FLAGS_RELEASE]]" ${CMAKE_CXX_FLAGS_DEBUG})
# message(STATUS "[[CXX FLAGS_MINSIZERELEASE]]" ${CMAKE_CXX_FLAGS_DEBUG})
# message(STATUS "[[CXX FLAGS_REL_WITH_DEBUG_INFO]]" ${CMAKE_CXX_FLAGS_DEBUG})
message(STATUS "Build type ${CMAKE_BUILD_TYPE}")

string(TOUPPER "${CMAKE_BUILD_TYPE}" BUILD_TYPE_UPPER)
message(STATUS "Build type flags ${CMAKE_CXX_FLAGS_${BUILD_TYPE_UPPER}}")
message(STATUS "[[Effective CXX flags]] ${CMAKE_CXX_FLAGS} ${CMAKE_CXX_FLAGS_${BUILD_TYPE_UPPER}}")



set(CMAKE_EXPORT_COMPILE_COMMANDS ON)
# Пути к Godot C++ API

# Определим пути к сборкам godot-cpp

# if (not GODOT_CPP_RELEASE_PATH)
#     set(GODOT_CPP_RELEASE_PATH "${CMAKE_SOURCE_DIR}/builds/build_godot_cpp_release_gcc_flto")
# endif()


# По умолчанию будем использовать debug

# Проверяем текущую конфигурацию


# Добавляем путь к линковке


# Файлы проекта
file(GLOB_RECURSE SOURCES src/*.cpp)
# Создание библиотеки




# set_source_files_properties(src/blocks/chunk.cpp
#     PROPERTIES COMPILE_OPTIONS "-Rpass=.*"
# )



#  set(REPORT_SOURCES
#         src/blocks/chunk.cpp
#         CACHE STRING "Source files for detailed compiler reports"
#     )

# set(REPORT_FLAGS
#             -Rpass=.*
#         )
# set_source_files_properties(${REPORT_SOURCES}
#             PROPERTIES COMPILE_OPTIONS "${REPORT_FLAGS}"
#         )

# set(REPORT_SOURCES
#     src/blocks/chunk.cpp
#     # CACHE STRING "Source files for detailed compiler reports"
# )

set(REPORT_FLAGS -Rpass=.*)

# # Добавим отладочный вывод
# message(STATUS "REPORT_SOURCES = ${REPORT_SOURCES}")
# message(STATUS "REPORT_FLAGS = ${REPORT_FLAGS}")

# set_source_files_properties(${REPORT_SOURCES}
#     PROPERTIES COMPILE_OPTIONS "${REPORT_FLAGS}"
# )

# Проверим, что применилось
# get_source_file_property(APPLIED_FLAGS src/blocks/chunk.cpp COMPILE_OPTIONS)
# message(STATUS "Applied flags to chunk.cpp: ${APPLIED_FLAGS}")


set(ENABLE_COMPILER_REPORTS true)

if(ENABLE_COMPILER_REPORTS)
    message(STATUS "[[Reports enabled]]")
    set(REPORT_SOURCES
        # src/blocks/chunk.cpp
        src/Main.cpp        #
        # src/gdobj/sphere_generator.cpp
        # src/register_types.cpp
        # src/blocks/chunk.cpp

        # CACHE STRING "Source files for detailed compiler reports"
    )
    # REPORT_FLAGS DO OVERRIDE CMAKE_CXX_FLAGS
    if(CMAKE_CXX_COMPILER_ID MATCHES "GNU")
        set(REPORT_FLAGS
            # does not work :(
            # ${CMAKE_CXX_FLAGS}
            -O3 -march=native -flto

            -fopt-info-vec
            -fopt-info-vec-missed
            -fopt-info-inline
            -fopt-info-loop

        )

        message(STATUS "[[Using GCC report flags: ${REPORT_FLAGS}]]")
    elseif(CMAKE_CXX_COMPILER_ID MATCHES "Clang")
        set(REPORT_FLAGS
            -Rpass=.*
            -Rpass-missed=.*
            -Rpass-analysis=.*
            -O3 -march=native -fvectorize -flto
            # ${CMAKE_CXX_FLAGS}

            # -Rpass=loop*
            # -Rpass-analysis=loop*
            # -Rpass-missed=loop*


            # -Rpass=loop-vectorize
            # -Rpass-analysis=loop-vectorize
            # -Rpass-missed=loop-vectorize
            # -Rpass=inline
            # -Rpass-analysis=inline
            # -Rpass-missed=inline
            # -Rpass=loop-interchange
            # -Rpass-analysis=loop-interchange
            # -Rpass-missed=loop-interchange
        )

        message(STATUS "[[Using Clang report flags: ${REPORT_FLAGS}]]")
    endif()

    # Применяем к выбранным файлам
    if(REPORT_SOURCES AND REPORT_FLAGS)

        # DOES NOT WORK
        #         separate_arguments(_cxx_flags NATIVE_COMMAND "${CMAKE_CXX_FLAGS}")
        #         set(RES_FLAGS ${_cxx_flags} ${REPORT_FLAGS})
        # message(STATUS "[[set custom report flags]]" "${RES_FLAGS}" )
        #         set_source_files_properties(${REPORT_SOURCES}
        #             PROPERTIES COMPILE_OPTIONS "${RES_FLAGS}"
        #         )

        message(STATUS "[[set custom report flags!!!! do not use in release]]")
        # полностью вытесняет флаги((((
        set_source_files_properties(${REPORT_SOURCES}
            PROPERTIES COMPILE_OPTIONS "${REPORT_FLAGS}"
        )
    endif()
endif()




find_package(Java COMPONENTS Development REQUIRED)
find_package(JNI REQUIRED)



# говно для команды
# Указываем, куда класть .class файлы. Создадим папку 'classes' в директории сборки
set(JAVA_CLASS_OUTPUT_DIR "${CMAKE_BINARY_DIR}/classes")

# Указываем CMake, чтобы он клал нашу .dll В ТУ ЖЕ ПАПКУ, что и .class файлы.

# set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${JAVA_CLASS_OUTPUT_DIR})


# Создаем кастомную команду для компиляции Java кода.
# Она будет запускать javac и класть результат в JAVA_CLASS_OUTPUT_DIR
# команда
add_custom_command(
    OUTPUT "${JAVA_CLASS_OUTPUT_DIR}/Main.class"
    COMMAND "${Java_JAVAC_EXECUTABLE}"
        -d "${JAVA_CLASS_OUTPUT_DIR}"
        -encoding UTF-8
        "${CMAKE_SOURCE_DIR}/src/Main.java"
    DEPENDS "${CMAKE_SOURCE_DIR}/src/Main.java"
    COMMENT "Компиляция Java исходников..."
)
# \ команда

# Теперь, когда мы собираем 'compile_java', CMake сначала скомпилирует Java код.
add_custom_target(compile_java ALL DEPENDS "${JAVA_CLASS_OUTPUT_DIR}/Main.class")

add_custom_target(run
    # Команда для запуска: java -cp <путь_к_классам> -Djava.library.path=<путь_к_dll> Main
    COMMAND "${Java_JAVA_EXECUTABLE}" -cp "${JAVA_CLASS_OUTPUT_DIR}" -Djava.library.path="${CMAKE_BINARY_DIR}" Main
    # Цель 'run' зависит от сборки C++ библиотеки и компиляции Java
    DEPENDS Java_C_expansion
    # Указываем рабочую директорию, чтобы все работало как надо
    WORKING_DIRECTORY "${CMAKE_BINARY_DIR}"
)



add_library(Java_C_expansion SHARED ${SOURCES})

add_dependencies(Java_C_expansion compile_java)

# target_link_options(Java_C_expansion PRIVATE -Rpass=.*  -Rpass-analysis=.* -Rpass-missed=.*)

target_include_directories(Java_C_expansion PUBLIC ${CMAKE_SOURCE_DIR}/src)

# Внешние библиотеки — SYSTEM, чтобы не ругался компилятор
# убожество!!!!!!!!!!!!! isystem НЕ появляется, если PUBLIC SYSTEM и оно...... МОЛЧИТ.
# target_include_directories(hero SYSTEM PUBLIC
#     ${GODOT_CPP_SELECTED_PATH}/gen/include
#     # ${CMAKE_SOURCE_DIR}/subprojects/godot-cpp
#     ${CMAKE_SOURCE_DIR}/subprojects/godot-cpp/include
#     ${CMAKE_SOURCE_DIR}/subprojects/godot-cpp/gdextension/
#     ${CMAKE_SOURCE_DIR}/subprojects/ennt/
#     ${CMAKE_SOURCE_DIR}/subprojects/nanoflann/include
# )



# add_subdirectory(subprojects/entt)

# Линкуем нужную библиотеку
# target_link_libraries(Java_C_expansion ${GODOT_CPP_LIB_NAME})
# target_link_libraries(hero godot-cpp.linux.debug.64)
# target_link_libraries(Java_C_expansion EnTT::EnTT)

find_package(JNI REQUIRED)

if(JNI_FOUND)
    message(STATUS "JNI найден! Пути к заголовочным файлам: ${JNI_INCLUDE_DIRS}")
    # Добавляем найденные пути к нашей цели
    target_include_directories(Java_C_expansion SYSTEM PUBLIC ${JNI_INCLUDE_DIRS})
else()
    message(FATAL_ERROR "Не удалось найти JNI. Убедитесь, что JDK установлен и JAVA_HOME настроен.")
endif()


# target_compile_options(hero PRIVATE -O2 -fopt-info-vec=vec_report.txt)

# clang
# target_compile_options(hero PRIVATE
#     -O2
#     -Rpass=loop-vectorize
#     -Rpass-missed=loop-vectorize
#     -Rpass-analysis=loop-vectorize
# )

# target_precompile_headers(hero PRIVATE
    # <iostream>
# )

# Указываем суффикс для Linux/Windows
if(WIN32)
    set_target_properties(Java_C_expansion PROPERTIES SUFFIX ".dll")
else()
    set_target_properties(Java_C_expansion PROPERTIES SUFFIX ".so")
endif()


# set(CMAKE_INTERPROCEDURAL_OPTIMIZATION TRUE)
# set(CMAKE_C_FLAGS_RELEASE "${CMAKE_C_FLAGS_RELEASE} -flto=thin")
# set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -flto=thin")

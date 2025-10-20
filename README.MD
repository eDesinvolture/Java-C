Порядок команд
```
Компиляция Java-кода
javac -encoding UTF-8 Main.java

Генерация C header
javac -h -encoding UTF-8 . Main.java

Компиляция C-кода в dll
gcc -shared -I"$env:JAVA_HOME\include" -I"$env:JAVA_HOME\include\win32" Main.c -o main.dll"
Запуск скомпилированный Java-класс, который загружает dll и вызывает C функцию
java Main


Можно билдить CMake и всё это пробилдится им и есть таргет ран
mkdir build
cd build
либо так
cmake -G "MinGW Makefiles" .. --preset GCC_analyze_vec #(вызывается пресет с вектрозиацией)
либо так
cmake -G "MinGW Makefiles" ..
cmake --build . --target run
cd ..

Код векторизуется с пресетом
--preset GCC_analyze_vec

Есть пресеты (пишет пресеты если пресет написан неправильно)
cmake --preset preset
```
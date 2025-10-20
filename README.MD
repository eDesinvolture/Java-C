Порядок команд

Компиляция Java-кода
javac -encoding UTF-8 Main.java

Генерация C header
javac -h -encoding UTF-8 . Main.java

Компиляция C-кода в dll
gcc -shared -I"$env:JAVA_HOME\include" -I"$env:JAVA_HOME\include\win32" Main.c -o main.dll

Запуск скомпилированный Java-класс, который загружает dll и вызывает C функцию
java Main


Можно билдить CMake и и всё это пробилдится им и есть таргет ран
mkdir build
cd build
cmake -G "MinGW Makefiles" ..
cmake --build . --target run
cd ..
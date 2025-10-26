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
cmake -G "MinGW Makefiles" .. --preset vectoriz #(вызывается пресет с вектрозиацией)
либо так
cmake -G "MinGW Makefiles" ..
cmake --build . --target run
cd ..

Код векторизуется с пресетом
--preset vectoriz

Есть пресеты (пишет пресеты если пресет написан неправильно)
cmake --preset preset
```

# выводы?

ну типа java extension на тривиальной операции сложения ≈ собранному C (по скорости роста вычислительной нагрузки от роста объёма вычислений ΔτC / ΔτJava ≈ 1.1 на достаточных значениях)

получается, java extension не бесплатный, хотя работает норм. Java в kubuntu походу собрана с векторными инструкциями. Запуск "C" в java добавил копирований и они обнулили все бонусы натива "C".
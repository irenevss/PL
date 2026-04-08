@echo off
echo === Generando archivos Java con JavaCC ===
java -cp javacc.jar javacc -OUTPUT_DIRECTORY=src\asint src\asint\spec.jj

echo.
echo === Compilando clases generadas ===
if not exist bin mkdir bin
javac -d bin src\asint\*.java

echo.
if "%~1"=="" (
    echo Por favor, proporciona un archivo de texto para analizar.
    echo Uso: compilar_y_ejecutar.bat ^<archivo.txt^>
    echo Ejemplo: compilar_y_ejecutar.bat test_sintactico.txt
) else (
    echo === Ejecutando el Analizador Sintactico Tiny ===
    java -cp bin asint.Main "%~1"
)

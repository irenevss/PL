@echo off
setlocal enabledelayedexpansion

:: ============================================================================
:: Script de Compilación y Prueba para DomJudge (Fase 3)
:: ============================================================================

:: 1. Compilación
echo [1/2] Compilando proyecto en la carpeta 'bin'...
if not exist bin mkdir bin

:: Compilamos desde DomJudge.java para que javac resuelva todas las dependencias
javac -d bin -cp "cup.jar;src" src\DomJudge.java

if !errorlevel! neq 0 (
    echo [ERROR] La compilacion ha fallado. Revisa los errores superiores.
    exit /b !errorlevel!
)
echo [OK] Compilacion completada con exito.
echo.

:: 2. Ejecución de casos de prueba
echo [2/2] Ejecutando casos de prueba de la carpeta 'casos\'...
set /a passed=0
set /a failed=0

:: Creamos una carpeta temporal para las salidas si no existe
if not exist temp_out mkdir temp_out

for %%f in (casos\*.in) do (
    set "filename=%%~nf"
    
    :: Ejecutamos DomJudge redirigiendo la entrada y salida
    java -cp "bin;cup.jar" DomJudge < "%%f" > "temp_out\!filename!.res"
    
    :: Comparamos la salida con el fichero .out esperado (ignorando espacios/saltos de linea superfluos)
    fc /W "temp_out\!filename!.res" "casos\!filename!.out" > nul
    
    if !errorlevel! equ 0 (
        echo [ OK ] !filename!
        set /a passed+=1
    ) else (
        echo [FALLO] !filename!
        set /a failed+=1
    )
)

:: 3. Resumen final
echo.
echo =======================================================
echo  RESUMEN: !passed! pasados, !failed! fallados.
echo =======================================================

if !failed! equ 0 (
    echo [EXITO] Todos los tests han pasado correctamente.
    :: Limpiamos la carpeta temporal si todo ha ido bien
    rmdir /s /q temp_out
) else (
    echo [ADVERTENCIA] Revisa las diferencias en la carpeta 'temp_out'.
    echo Puedes usar: fc /W temp_out\fichero.res casos\fichero.out
)

pause

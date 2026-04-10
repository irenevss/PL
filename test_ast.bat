@echo off
setlocal enabledelayedexpansion

:: Configuration
set BIN=bin
set SRC=src
set CP=bin;cup.jar;jflex.jar
set ACTUAL=actual_output.tmp

:: Create bin directory if it doesn't exist
if not exist "%BIN%" (
    mkdir "%BIN%"
)

echo --- Generating Parser (JavaCC) ---
java -cp javacc.jar javacc -OUTPUT_DIRECTORY=%SRC%/c_ast_descendente %SRC%/c_ast_descendente/spec.jj
if errorlevel 1 (
    echo JavaCC generation failed!
    exit /b 1
)

echo --- Generating Parser (CUP) ---
java -cp cup.jar java_cup.Main -parser AnalizadorSintacticoTiny -symbols ClaseLexica -nopositions -destdir %SRC%/c_ast_ascendente %SRC%/c_ast_ascendente/spec.cup
if errorlevel 1 (
    echo CUP generation failed!
    exit /b 1
)

echo --- Generating Lexer (JFlex) ---
java -jar jflex.jar -d %SRC%/alex %SRC%/alex/spec.jflex
if errorlevel 1 (
    echo JFlex generation failed!
    exit /b 1
)

echo --- Compiling Components ---
javac -g -d %BIN% -cp "%CP%" %SRC%/asint/*.java %SRC%/impresion/*.java %SRC%/alex/*.java %SRC%/c_ast_descendente/*.java %SRC%/c_ast_ascendente/*.java %SRC%/errors/*.java %SRC%/DomJudge.java
if errorlevel 1 (
    echo AST Compilation failed!
    exit /b 1
)

echo --- Running Tests ---
set passed=0
set total=0

for %%i in (casos\*.in) do (
    set /a total+=1
    set "file=%%~ni"
    set "inputFile=%%i"
    set "expectedFile=casos\!file!.out"
    set "diffFile=casos\!file!.diff"

    if not exist "!expectedFile!" (
        echo SKIP: !file! ^(Expected output missing^)
    ) else (
        REM Clean previous diff
        if exist "!diffFile!" del "!diffFile!"

        REM Run the test
        type "!inputFile!" | java -cp "%CP%" DomJudge > "%ACTUAL%" 2>&1

        if errorlevel 1 (
            echo FAIL: !file! ^(Crash or non-zero exit code^)
            move /y "%ACTUAL%" "!diffFile!" >nul
        ) else (
            REM Compare output
            fc "%ACTUAL%" "!expectedFile!" >nul
            if errorlevel 1 (
                echo FAIL: !file! ^(Mismatch^)
                fc "%ACTUAL%" "!expectedFile!" > "!diffFile!"
            ) else (
                echo PASS: !file!
                set /a passed+=1
            )
        )
    )
)

echo.
echo Summary: %passed% / %total% tests passed.

:: Clean up
REM if exist "%ACTUAL%" del "%ACTUAL%"

endlocal

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

echo --- Generating Parsers (JavaCC, CUP, JFlex) ---

:: JavaCC
java -cp javacc.jar javacc -OUTPUT_DIRECTORY=%SRC%/c_ast_descendente %SRC%/c_ast_descendente/spec.jj
if errorlevel 1 (
    echo JavaCC generation failed!
    exit /b 1
)

:: CUP
java -cp cup.jar java_cup.Main -parser AnalizadorSintacticoTiny -symbols ClaseLexica -nopositions -destdir %SRC%/c_ast_ascendente %SRC%/c_ast_ascendente/spec.cup
if errorlevel 1 (
    echo CUP generation failed!
    exit /b 1
)

:: JFlex
java -jar jflex.jar -d %SRC%/c_ast_ascendente %SRC%/c_ast_ascendente/spec.jflex
if errorlevel 1 (
    echo JFlex generation failed!
    exit /b 1
)

echo --- Compiling Full Processor (Phase 4) ---
:: Compile all packages including semantica, maquinap and codigo
javac -encoding UTF-8 -g -d %BIN% -cp "%CP%" %SRC%/asint/*.java %SRC%/semantica/*.java %SRC%/maquinap/*.java %SRC%/codigo/*.java %SRC%/errors/*.java %SRC%/c_ast_descendente/*.java %SRC%/c_ast_ascendente/*.java %SRC%/DomJudge.java %SRC%/BISReader.java
if errorlevel 1 (
    echo Compilation failed!
    exit /b 1
)

echo --- Running Full Integration Tests ---
set passed=0
set total=0

:: Iterate through all subdirectories in casos
for /d %%D in (casos\*) do (
    echo.
    echo Testing category: %%~nD
    for %%i in (%%D\*.in) do (
        set /a total+=1
        set "file=%%~ni"
        set "inputFile=%%i"
        set "baseName=!file:~0,-2!"
        set "expectedFile=%%D\!baseName!.out"
        set "diffFile=%%D\!file!.diff"

        if not exist "!expectedFile!" (
            echo   SKIP: !file! ^(Expected output missing^)
        ) else (
            REM Clean previous diff
            if exist "!diffFile!" del "!diffFile!"

            REM Run the processor via DomJudge
            java -Xss128m -Xmx1024m -Dfile.encoding=UTF-8 -cp "%CP%" DomJudge < "!inputFile!" > "%ACTUAL%" 2>&1

            if errorlevel 1 (
                echo   FAIL: !file! ^(Crash or non-zero exit code^)
                move /y "%ACTUAL%" "!diffFile!" >nul
            ) else (
                REM Compare output
                fc "%ACTUAL%" "!expectedFile!" >nul
                if errorlevel 1 (
                    echo   FAIL: !file! ^(Mismatch^)
                    git diff --no-index --color=never "!expectedFile!" "%ACTUAL%" > "!diffFile!"
                ) else (
                    echo   PASS: !file!
                    set /a passed+=1
                )
            )
        )
    )
)

echo.
echo ========================================
echo Final Summary: %passed% / %total% tests passed.
echo ========================================

:: Clean up
if exist "%ACTUAL%" del "%ACTUAL%"

endlocal

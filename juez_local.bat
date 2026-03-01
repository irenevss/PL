@echo off
setlocal enabledelayedexpansion

if not exist bin (
  echo Compilando primero...
  call compilar_y_ejecutar_asint.bat
)

echo === Ejecutando tests de DomJudge ===
set /a passed=0
set /a failed=0

for %%f in (tests\tiny\*.in) do (
    java -Dfile.encoding=UTF-8 -cp bin DomJudge < "%%f" > "temp_out.txt"
    fc /W "temp_out.txt" "tests\tiny\%%~nf.out" > nul
    
    if !errorlevel! equ 0 (
        echo [ OK ] %%~nxf
        set /a passed+=1
    ) else (
        echo [FALLO] %%~nxf
        set /a failed+=1
    )
)

if exist temp_out.txt del temp_out.txt

echo.
echo ===========================================
echo RESULTADO GLOBAL: !passed! OK, !failed! FALLOS
echo ===========================================

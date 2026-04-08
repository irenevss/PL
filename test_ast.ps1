# Script to compile only AST components and run tests
# Usage: powershell -File test_ast.ps1

$BIN = "bin"
$SRC = "src"
$CP = "bin;cup.jar;jflex.jar"
$ACTUAL = "actual_output.tmp"

if (!(Test-Path $BIN)) {
    New-Item -ItemType Directory -Path $BIN | Out-Null
}

Write-Host "--- Generating Parser (JavaCC) ---" -ForegroundColor Cyan
cmd /c "java -cp javacc.jar javacc -OUTPUT_DIRECTORY=$SRC/asint $SRC/asint/spec.jj"

Write-Host "--- Generating Parser (CUP) ---" -ForegroundColor Cyan
cmd /c "java -cp cup.jar java_cup.Main -parser AnalizadorSintacticoTiny -symbols ClaseLexica -destdir $SRC/asint_cup $SRC/asint_cup/spec.cup"

Write-Host "--- Generating Lexer (JFlex) ---" -ForegroundColor Cyan
cmd /c "java -jar jflex.jar -d $SRC/alex $SRC/alex/spec.jflex"

Write-Host "--- Compiling Components ---" -ForegroundColor Cyan
cmd /c "javac -d $BIN -cp `"$CP`" $SRC/ast/*.java $SRC/alex/*.java $SRC/asint/*.java $SRC/asint_cup/*.java $SRC/errors/*.java $SRC/DomJudge.java"

if ($LASTEXITCODE -ne 0) {
    Write-Host "AST Compilation failed!" -ForegroundColor Red
    exit $LASTEXITCODE
}

Write-Host "--- Running Tests ---" -ForegroundColor Cyan
$casos = Get-ChildItem "casos/*.in"
$passed = 0
$total = 0

foreach ($file in $casos) {
    $total++
    $expectedFile = $file.FullName.Replace(".in", ".out")
    
    if (!(Test-Path $expectedFile)) {
        Write-Host "SKIP: $($file.Name) (Expected output missing)" -ForegroundColor Yellow
        continue
    }

    $inputFile = $file.FullName
    cmd /c "type `"$inputFile`" | java -cp `"$CP`" DomJudge > `"$ACTUAL`" 2>&1"

    if ($LASTEXITCODE -ne 0) {
        Write-Host "FAIL: $($file.Name) (Crash or Exit Code $LASTEXITCODE)" -ForegroundColor Red
        continue
    }

    # Compare content
    $fc_result = cmd /c "fc /w `"$ACTUAL`" `"$expectedFile`""
    if ($LASTEXITCODE -eq 0) {
        Write-Host "PASS: $($file.Name)" -ForegroundColor Green
        $passed++
    } else {
        Write-Host "FAIL: $($file.Name) (Mismatch)" -ForegroundColor Red
    }
}

Write-Host ""
$finalColor = if ($passed -eq $total -and $total -gt 0) { "Green" } else { "Yellow" }
Write-Host "Summary: $passed / $total tests passed." -ForegroundColor $finalColor

# Clean up
if (Test-Path $ACTUAL) {
    Remove-Item $ACTUAL
}

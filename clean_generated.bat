@echo off
echo Cleaning generated files...

:: CUP
if exist "src\c_ast_ascendente\AnalizadorSintacticoTiny.java" del "src\c_ast_ascendente\AnalizadorSintacticoTiny.java"
if exist "src\c_ast_ascendente\ClaseLexica.java" del "src\c_ast_ascendente\ClaseLexica.java"

:: JFlex
if exist "src\c_ast_ascendente\AnalizadorLexicoTiny.java" del "src\c_ast_ascendente\AnalizadorLexicoTiny.java"
if exist "src\c_ast_ascendente\AnalizadorLexicoTiny.java~" del "src\c_ast_ascendente\AnalizadorLexicoTiny.java~"

:: JavaCC
if exist "src\c_ast_descendente\ConstructorASTsTiny.java" del "src\c_ast_descendente\ConstructorASTsTiny.java"
if exist "src\c_ast_descendente\ConstructorASTsTinyConstants.java" del "src\c_ast_descendente\ConstructorASTsTinyConstants.java"
if exist "src\c_ast_descendente\ConstructorASTsTinyTokenManager.java" del "src\c_ast_descendente\ConstructorASTsTinyTokenManager.java"
if exist "src\c_ast_descendente\ParseException.java" del "src\c_ast_descendente\ParseException.java"
if exist "src\c_ast_descendente\SimpleCharStream.java" del "src\c_ast_descendente\SimpleCharStream.java"
if exist "src\c_ast_descendente\Token.java" del "src\c_ast_descendente\Token.java"
if exist "src\c_ast_descendente\TokenMgrError.java" del "src\c_ast_descendente\TokenMgrError.java"

echo Subdirectorios limpios de archivos autogenerados.

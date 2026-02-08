package alex;

%%
%public
%class AnalizadorLexicoTiny
%type UnidadLexica
%unicode
%line
%column

%{
  private ALexOperations ops;
  public String lexema() {return yytext();}
  public int fila() {return yyline+1;}
  public int columna() {return yycolumn+1;}
%}

%init{
  ops = new ALexOperations(this);
%init}

letra  = [A-Za-z]
digito = [0-9]
digitoPositivo = [1-9]
subrayado = _
blanco = [ \b\r\t\n]
comentario = ##[^\n]*

ParteEntera = [+-]?(0|{digitoPositivo}{digito}*)
ParteDecimal = \.(0|{digito}*{digitoPositivo})
ParteExponencial = [eE]{ParteEntera}
LiteralReal = {ParteEntera}({ParteDecimal}|{ParteExponencial}|{ParteDecimal}{ParteExponencial})

Id = {letra}({letra}|{digito}|{subrayado})*
LiteralCadena = '([^'\\]|\\b|\\r|\\t|\\n)*'

%%
{blanco}               { /* ignore */ }
{comentario}           { /* ignore */ }

"program"              { return ops.unievaluada(ClaseLexica.PROGRAM); }
"end_program"          { return ops.unievaluada(ClaseLexica.END_PROGRAM); }
"int"                  { return ops.unievaluada(ClaseLexica.INT); }
"real"                 { return ops.unievaluada(ClaseLexica.REAL); }
"bool"                 { return ops.unievaluada(ClaseLexica.BOOL); }
"string"               { return ops.unievaluada(ClaseLexica.STRING); }
"null"                 { return ops.unievaluada(ClaseLexica.NULL); }
"true"                 { return ops.unievaluada(ClaseLexica.TRUE); }
"false"                { return ops.unievaluada(ClaseLexica.FALSE); }
"dectype"              { return ops.unievaluada(ClaseLexica.DECTYPE); }
"decvar"               { return ops.unievaluada(ClaseLexica.DECVAR); }
"decproc"              { return ops.unievaluada(ClaseLexica.DECPROC); }
"end_proc"             { return ops.unievaluada(ClaseLexica.END_PROC); }
"ref"                  { return ops.unievaluada(ClaseLexica.REF); }
"if"                   { return ops.unievaluada(ClaseLexica.IF); }
"end_if"               { return ops.unievaluada(ClaseLexica.END_IF); }
"else"                 { return ops.unievaluada(ClaseLexica.ELSE); }
"while"                { return ops.unievaluada(ClaseLexica.WHILE); }
"end_while"            { return ops.unievaluada(ClaseLexica.END_WHILE); }
"array"                { return ops.unievaluada(ClaseLexica.ARRAY); }
"of"                   { return ops.unievaluada(ClaseLexica.OF); }
"record"               { return ops.unievaluada(ClaseLexica.RECORD); }
"end_record"           { return ops.unievaluada(ClaseLexica.END_RECORD); }
"pointer"              { return ops.unievaluada(ClaseLexica.POINTER); }
"new"                  { return ops.unievaluada(ClaseLexica.NEW); }
"dispose"              { return ops.unievaluada(ClaseLexica.DISPOSE); }
"input"                { return ops.unievaluada(ClaseLexica.INPUT); }
"output"               { return ops.unievaluada(ClaseLexica.OUTPUT); }
"block"                { return ops.unievaluada(ClaseLexica.BLOCK); }
"end_block"            { return ops.unievaluada(ClaseLexica.END_BLOCK); }

"+"                    { return ops.unievaluada(ClaseLexica.MAS); }
"-"                    { return ops.unievaluada(ClaseLexica.MENOS); }
"*"                    { return ops.unievaluada(ClaseLexica.POR); }
"/"                    { return ops.unievaluada(ClaseLexica.DIV); }
"%"                    { return ops.unievaluada(ClaseLexica.MOD); }
"<"                    { return ops.unievaluada(ClaseLexica.MENOR); }
">"                    { return ops.unievaluada(ClaseLexica.MAYOR); }
"<="                   { return ops.unievaluada(ClaseLexica.MENOR_IGUAL); }
">="                   { return ops.unievaluada(ClaseLexica.MAYOR_IGUAL); }
"="                    { return ops.unievaluada(ClaseLexica.IGUAL); }
"<>"                   { return ops.unievaluada(ClaseLexica.DISTINTO); }
"("                    { return ops.unievaluada(ClaseLexica.PAP); }
")"                    { return ops.unievaluada(ClaseLexica.PCIERRE); }
";"                    { return ops.unievaluada(ClaseLexica.PUNTO_COMA); }
":"                    { return ops.unievaluada(ClaseLexica.DOS_PUNTOS); }
":="                   { return ops.unievaluada(ClaseLexica.ASIGNACION); }
"["                    { return ops.unievaluada(ClaseLexica.CAP); }
"]"                    { return ops.unievaluada(ClaseLexica.CCIERRE); }
"."                    { return ops.unievaluada(ClaseLexica.PUNTO); }
"->"                   { return ops.unievaluada(ClaseLexica.FLECHA); }
"&"                    { return ops.unievaluada(ClaseLexica.AND); }
"|"                    { return ops.unievaluada(ClaseLexica.OR); }
"!"                    { return ops.unievaluada(ClaseLexica.NOT); }
"@"                    { return ops.unievaluada(ClaseLexica.ARROBA); }
","                    { return ops.unievaluada(ClaseLexica.COMA); }
"--"                   { return ops.unievaluada(ClaseLexica.SEPARADOR); }

{LiteralReal}          { return ops.multivaluada(ClaseLexica.LITERAL_REAL, yytext()); }
{ParteEntera}          { return ops.multivaluada(ClaseLexica.LITERAL_ENTERO, yytext()); }
{Id}                   { return ops.multivaluada(ClaseLexica.ID, yytext()); }
{LiteralCadena}        { return ops.multivaluada(ClaseLexica.CADENA_LITERAL, yytext()); }

[^]                    { ops.error(); }

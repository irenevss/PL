package asint_cup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import alex.UnidadLexica;

public class ASTNode {
    private final String kind;
    private final String lexema;
    private final int fila;
    private final int columna;
    private final List<ASTNode> children;

    public ASTNode(String kind, String lexema, int fila, int columna, List<ASTNode> children) {
        this.kind = kind;
        this.lexema = lexema;
        this.fila = fila;
        this.columna = columna;
        this.children = children == null ? Collections.emptyList() : Collections.unmodifiableList(children);
    }

    public String kind() {
        return kind;
    }

    public String lexema() {
        return lexema;
    }

    public int fila() {
        return fila;
    }

    public int columna() {
        return columna;
    }

    public List<ASTNode> children() {
        return children;
    }

    public static ASTNode node(String kind, ASTNode... children) {
        return new ASTNode(kind, null, -1, -1, compact(children));
    }

    public static ASTNode withToken(String kind, UnidadLexica token, ASTNode... children) {
        return new ASTNode(kind, token.lexema(), token.fila(), token.columna(), compact(children));
    }

    public static ASTNode withLexemaAt(String kind, String lexema, UnidadLexica token, ASTNode... children) {
        return new ASTNode(kind, lexema, token.fila(), token.columna(), compact(children));
    }

    public static ASTNode option(String withValueKind, String emptyKind, ASTNode value) {
        if (value == null) {
            return node(emptyKind);
        }
        return node(withValueKind, value);
    }

    public static ASTNode appendLeft(String listKind, ASTNode left, ASTNode right) {
        return node(listKind, left, right);
    }

    public static ASTNode binaryFromToken(UnidadLexica op, ASTNode left, ASTNode right) {
        String lex = op.lexema();
        if ("+".equals(lex)) return withToken("exp_suma", op, left, right);
        if ("-".equals(lex)) return withToken("exp_resta", op, left, right);
        if ("*".equals(lex)) return withToken("exp_mul", op, left, right);
        if ("/".equals(lex)) return withToken("exp_div", op, left, right);
        if ("%".equals(lex)) return withToken("exp_mod", op, left, right);
        if ("&".equals(lex)) return withToken("exp_and", op, left, right);
        if ("|".equals(lex)) return withToken("exp_or", op, left, right);
        if ("<".equals(lex)) return withToken("exp_menor", op, left, right);
        if (">".equals(lex)) return withToken("exp_mayor", op, left, right);
        if ("<=".equals(lex)) return withToken("exp_menor_igual", op, left, right);
        if (">=".equals(lex)) return withToken("exp_mayor_igual", op, left, right);
        if ("=".equals(lex)) return withToken("exp_igual", op, left, right);
        if ("<>".equals(lex)) return withToken("exp_distinto", op, left, right);
        return withToken("exp_bin", op, left, right);
    }

    public static ASTNode unaryFromToken(UnidadLexica op, ASTNode operand) {
        String lex = op.lexema();
        if ("-".equals(lex)) return withToken("exp_menos_unario", op, operand);
        if ("!".equals(lex)) return withToken("exp_not", op, operand);
        if ("*".equals(lex)) return withToken("exp_indirec", op, operand);
        return withToken("exp_unaria", op, operand);
    }

    public static ASTNode postArray(ASTNode base, UnidadLexica cap, ASTNode index) {
        return withToken("exp_array", cap, base, index);
    }

    public static ASTNode postField(ASTNode base, UnidadLexica point, UnidadLexica id) {
        return new ASTNode("exp_campo", id.lexema(), id.fila(), id.columna(), compact(base));
    }

    public static ASTNode postArrow(ASTNode base, UnidadLexica arrow, UnidadLexica id) {
        return new ASTNode("exp_flecha", id.lexema(), id.fila(), id.columna(), compact(base));
    }

    private static List<ASTNode> compact(ASTNode... nodes) {
        List<ASTNode> values = new ArrayList<ASTNode>();
        if (nodes == null) {
            return values;
        }
        values.addAll(Arrays.asList(nodes));
        values.removeIf(n -> n == null);
        return values;
    }
}

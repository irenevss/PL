package codigo;

import java.io.Reader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;

public class MaquinaP {
    private final Reader inputReader;
    private final Scanner input;
    private final int tamdatos;
    private final int tamheap;
    private final int ndisplays;
    private final Object[] memoria;
    private final Deque<Object> pila = new ArrayDeque<>();
    private final Deque<Frame> frames = new ArrayDeque<>();
    private final Deque<Integer> retorno = new ArrayDeque<>();
    private final int[] display;
    private int stackTop;
    private int heapTop;
    private int heapBase;
    private List<Instr> programa;
    private int pc;

    private static class Frame {
        final int nivel;
        final int prevDisplay;
        final int base;
        final int tam;

        Frame(int nivel, int prevDisplay, int base, int tam) {
            this.nivel = nivel;
            this.prevDisplay = prevDisplay;
            this.base = base;
            this.tam = tam;
        }
    }

    public MaquinaP(Reader input, int tamdatos, int tampila, int tamheap, int ndisplays) {
        this(input, tamdatos, tampila, tamheap, ndisplays, 0);
    }

    public MaquinaP(Reader input, int tamdatos, int tampila, int tamheap, int ndisplays, int globalSize) {
        this.inputReader = input;
        this.input = new Scanner(input);
        this.tamdatos = tamdatos;
        this.tamheap = tamheap;
        this.ndisplays = ndisplays;
        this.memoria = new Object[tamdatos];
        this.display = new int[ndisplays];
        this.heapBase = tamdatos - tamheap;
        this.stackTop = Math.min(globalSize, heapBase);
        this.heapTop = tamdatos;
        for (int i = 0; i < ndisplays; i++) {
            display[i] = 0;
        }
    }

    public void cargaPrograma(List<Instr> programa) {
        this.programa = programa;
    }

    public int programaSize() {
        return programa.size();
    }

    public void ejecuta() {
        pc = 0;
        while (pc >= 0 && pc < programa.size()) {
            Instr instr = programa.get(pc);
            execute(instr);
            pc++;
        }
    }

    private void execute(Instr instr) {
        switch (instr.op) {
            case APILA_INT:
                pila.push(instr.args[0]);
                break;
            case APILA_REAL:
                pila.push(instr.args[0]);
                break;
            case APILA_BOOL:
                pila.push(instr.args[0]);
                break;
            case APILA_STRING:
                pila.push(instr.args[0]);
                break;
            case APILA_DIR: {
                int d = (Integer) instr.args[0];
                pila.push(memoria[d]);
                break;
            }
            case DESAPILA_DIR: {
                int d = (Integer) instr.args[0];
                Object v = pila.pop();
                memoria[d] = v;
                break;
            }
            case APILAD: {
                int nivel = (Integer) instr.args[0];
                int desp = (Integer) instr.args[1];
                pila.push(display[nivel] + desp);
                break;
            }
            case DESAPILAD: {
                int nivel = (Integer) instr.args[0];
                int desp = (Integer) instr.args[1];
                Object v = pila.pop();
                int addr = display[nivel] + desp;
                memoria[addr] = v;
                break;
            }
            case DUP: {
                Object top = pila.peek();
                pila.push(top);
                break;
            }
            case APILA_IND: {
                int addr = asAddress(pila.pop());
                pila.push(memoria[addr]);
                break;
            }
            case DESAPILA_IND: {
                Object value = pila.pop();
                int addr = asAddress(pila.pop());
                memoria[addr] = value;
                break;
            }
            case COPIA: {
                int tam = (Integer) instr.args[0];
                int src = asAddress(pila.pop());
                int dst = asAddress(pila.pop());
                for (int i = 0; i < tam; i++) {
                    memoria[dst + i] = memoria[src + i];
                }
                break;
            }
            case SUMA:
                pila.push(aritmetica((Number) pila.pop(), (Number) pila.pop(), Op.SUMA));
                break;
            case RESTA:
                pila.push(aritmetica((Number) pila.pop(), (Number) pila.pop(), Op.RESTA));
                break;
            case MUL:
                pila.push(aritmetica((Number) pila.pop(), (Number) pila.pop(), Op.MUL));
                break;
            case DIV:
                pila.push(aritmetica((Number) pila.pop(), (Number) pila.pop(), Op.DIV));
                break;
            case MOD: {
                int v1 = ((Number) pila.pop()).intValue();
                int v0 = ((Number) pila.pop()).intValue();
                pila.push(v0 % v1);
                break;
            }
            case AND:
                pila.push((Boolean) pila.pop() && (Boolean) pila.pop());
                break;
            case OR:
                pila.push((Boolean) pila.pop() || (Boolean) pila.pop());
                break;
            case NOT:
                pila.push(!((Boolean) pila.pop()));
                break;
            case MENOR:
                pila.push(compara((Comparable) pila.pop(), (Comparable) pila.pop(), Op.MENOR));
                break;
            case MAYOR:
                pila.push(compara((Comparable) pila.pop(), (Comparable) pila.pop(), Op.MAYOR));
                break;
            case MENOR_IGUAL:
                pila.push(compara((Comparable) pila.pop(), (Comparable) pila.pop(), Op.MENOR_IGUAL));
                break;
            case MAYOR_IGUAL:
                pila.push(compara((Comparable) pila.pop(), (Comparable) pila.pop(), Op.MAYOR_IGUAL));
                break;
            case IGUAL:
                pila.push(igual(pila.pop(), pila.pop()));
                break;
            case DISTINTO:
                pila.push(!igual(pila.pop(), pila.pop()));
                break;
            case IR_A: {
                Label etiqueta = (Label) instr.args[0];
                pc = etiqueta.address - 1;
                break;
            }
            case IR_F: {
                Label etiqueta = (Label) instr.args[0];
                Boolean condicion = (Boolean) pila.pop();
                if (!condicion) {
                    pc = etiqueta.address - 1;
                }
                break;
            }
            case IR_IND: {
                if (retorno.isEmpty()) {
                    pc = programa.size();
                } else {
                    pc = retorno.pop() - 1;
                }
                break;
            }
            case ACTIVA: {
                int nivel = (Integer) instr.args[0];
                int tam = (Integer) instr.args[1];
                int dirRet = (Integer) instr.args[2];
                if (nivel < 0 || nivel >= ndisplays) {
                    throw new RuntimeException("nivel incorrecto: " + nivel);
                }
                if (stackTop + tam > heapBase) {
                    throw new RuntimeException("error de memoria de pila");
                }
                frames.push(new Frame(nivel, display[nivel], stackTop, tam));
                display[nivel] = stackTop;
                stackTop += tam;
                retorno.push(dirRet);
                break;
            }
            case DESACTIVA: {
                int nivel = (Integer) instr.args[0];
                int tam = (Integer) instr.args[1];
                if (frames.isEmpty()) {
                    throw new RuntimeException("desactiva sin frame");
                }
                Frame frame = frames.pop();
                display[nivel] = frame.prevDisplay;
                stackTop = frame.base;
                break;
            }
            case ALLOC: {
                int tam = (Integer) instr.args[0];
                if (heapTop - tam < stackTop) {
                    throw new RuntimeException("error de memoria dinamica");
                }
                heapTop -= tam;
                pila.push(heapTop);
                break;
            }
            case DEALLOC: {
                int tam = (Integer) instr.args[0];
                int addr = asAddress(pila.pop());
                if (addr == -1) {
                    throw new RuntimeException("desreferenciacion de null en liberacion");
                }
                if (addr + tam == heapTop) {
                    heapTop += tam;
                }
                break;
            }
            case READ_INT: {
                int valor = input.nextInt();
                if (input.hasNextLine()) {
                    input.nextLine();
                }
                pila.push(valor);
                break;
            }
            case READ_REAL: {
                double valor = input.nextDouble();
                if (input.hasNextLine()) {
                    input.nextLine();
                }
                pila.push(valor);
                break;
            }
            case READ_STRING: {
                String valor = input.nextLine();
                pila.push(valor);
                break;
            }
            case PRINT: {
                Object valor = pila.pop();
                if (valor instanceof Boolean) {
                    System.out.print(((Boolean) valor) ? "true" : "false");
                } else {
                    System.out.print(valor);
                }
                break;
            }
            case STOP:
                pc = programa.size();
                break;
            default:
                throw new RuntimeException("instruccion desconocida: " + instr.op);
        }
    }

    private int asAddress(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        }
        throw new RuntimeException("valor no es direccion: " + value);
    }

    private Object aritmetica(Number v1, Number v0, Op operacion) {
        boolean real = v1 instanceof Double || v0 instanceof Double || v1 instanceof Float || v0 instanceof Float;
        double a = v0.doubleValue();
        double b = v1.doubleValue();
        switch (operacion) {
            case SUMA:
                return real ? a + b : (int) (a + b);
            case RESTA:
                return real ? a - b : (int) (a - b);
            case MUL:
                return real ? a * b : (int) (a * b);
            case DIV:
                return real ? a / b : (int) (a / b);
        }
        throw new RuntimeException("operacion aritmetica desconocida");
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private boolean compara(Comparable v1, Comparable v0, Op operacion) {
        if (v0 == null || v1 == null) {
            throw new RuntimeException("comparacion con null");
        }
        int cmp;
        if (v0 instanceof Number && v1 instanceof Number) {
            double a = ((Number) v0).doubleValue();
            double b = ((Number) v1).doubleValue();
            cmp = Double.compare(a, b);
        } else {
            cmp = v0.compareTo(v1);
        }
        switch (operacion) {
            case MENOR:
                return cmp < 0;
            case MAYOR:
                return cmp > 0;
            case MENOR_IGUAL:
                return cmp <= 0;
            case MAYOR_IGUAL:
                return cmp >= 0;
            default:
                return false;
        }
    }

    private boolean igual(Object v1, Object v0) {
        if (v0 == null && v1 == null) {
            return true;
        }
        if (v0 == null || v1 == null) {
            return false;
        }
        return v0.equals(v1);
    }

    public enum Op {
        APILA_INT,
        APILA_REAL,
        APILA_BOOL,
        APILA_STRING,
        APILA_DIR,
        DESAPILA_DIR,
        APILAD,
        DESAPILAD,
        DUP,
        APILA_IND,
        DESAPILA_IND,
        COPIA,
        SUMA,
        RESTA,
        MUL,
        DIV,
        MOD,
        AND,
        OR,
        NOT,
        MENOR,
        MAYOR,
        MENOR_IGUAL,
        MAYOR_IGUAL,
        IGUAL,
        DISTINTO,
        IR_A,
        IR_F,
        IR_IND,
        ACTIVA,
        DESACTIVA,
        ALLOC,
        DEALLOC,
        READ_INT,
        READ_REAL,
        READ_STRING,
        PRINT,
        STOP
    }

    public static class Instr {
        public final Op op;
        public final Object[] args;

        public Instr(Op op, Object... args) {
            this.op = op;
            this.args = args;
        }
    }

    public static class Label {
        private int address = -1;

        public int getAddress() {
            return address;
        }
    }

    public void setLabelAddress(Label label) {
        label.address = programa.size();
    }

    public void addInstr(Instr instr) {
        programa.add(instr);
    }

    public Instr instruccion(Op op, Object... args) {
        return new Instr(op, args);
    }
}

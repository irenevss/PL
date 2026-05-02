package maquinap;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.io.Reader;
import java.util.Scanner;

public class MaquinaP {
   public static class EAccesoIlegitimo extends RuntimeException {
      public EAccesoIlegitimo(Object v) {
         super(v == null ? "null" : v.getClass().getSimpleName() + ": " + v.toString());
      }
   }

   public static class EAccesoAMemoriaNoInicializada extends RuntimeException {
      public EAccesoAMemoriaNoInicializada(int pc, int dir) {
         super("pinst:" + pc + " dir:" + dir);
      }
   }

   public static class EAccesoFueraDeRango extends RuntimeException {
   }

   public enum Op {
      APILA_INT, APILA_REAL, APILA_BOOL, APILA_STRING, APILA_DIR, DESAPILA_DIR,
      APILAD, DESAPILAD, DUP, APILA_IND, DESAPILA_IND, COPIA, SUMA, RESTA,
      MUL, DIV, MOD, AND, OR, NOT, MENOR, MAYOR, MENOR_IGUAL, MAYOR_IGUAL,
      IGUAL, DISTINTO, IR_A, IR_F, IR_IND, ACTIVA, DESACTIVA, ALLOC, DEALLOC,
      READ_INT, READ_REAL, READ_STRING, PRINT, STOP, FIJAD, POP
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
      public int address = -1;

      @Override
      public String toString() {
         return String.valueOf(address);
      }
   }

   private GestorMemoriaDinamica gestorMemoriaDinamica;
   private GestorPilaActivaciones gestorPilaActivaciones;

   private class Valor {
      public int valorInt() {
         throw new EAccesoIlegitimo(this);
      }

      public boolean valorBool() {
         throw new EAccesoIlegitimo(this);
      }

      public double valorReal() {
         throw new EAccesoIlegitimo(this);
      }

      public String valorString() {
         throw new EAccesoIlegitimo(this);
      }
   }

   private class ValorInt extends Valor {
      private int valor;

      public ValorInt(int valor) {
         this.valor = valor;
      }

      public int valorInt() {
         return valor;
      }

      public double valorReal() {
         return (double) valor;
      }

      public String toString() {
         return String.valueOf(valor);
      }
   }

   private class ValorBool extends Valor {
      private boolean valor;

      public ValorBool(boolean valor) {
         this.valor = valor;
      }

      public boolean valorBool() {
         return valor;
      }

      public String toString() {
         return String.valueOf(valor);
      }
   }

   private class ValorReal extends Valor {
      private double valor;

      public ValorReal(double valor) {
         this.valor = valor;
      }

      public double valorReal() {
         return valor;
      }

      public String toString() {
         return String.valueOf(valor);
      }
   }

   private class ValorString extends Valor {
      private String valor;

      public ValorString(String valor) {
         if (valor.startsWith("'") && valor.endsWith("'")) {
            valor = valor.substring(1, valor.length() - 1);
         }
         this.valor = valor.replace("\\n", "\n").replace("\\t", "\t").replace("\\r", "\r");
      }

      public String valorString() {
         return valor;
      }

      public String toString() {
         return valor;
      }
   }

   private List<Instruccion> codigoP;
   private Stack<Valor> pilaEvaluacion;
   private Valor[] datos;
   private int pc;

   public interface Instruccion {
      void ejecuta();
   }

   private ISuma ISUMA;

   private class ISuma implements Instruccion {
      public void ejecuta() {
         Valor opnd2 = pilaEvaluacion.pop();
         Valor opnd1 = pilaEvaluacion.pop();
         if (opnd1 instanceof ValorReal || opnd2 instanceof ValorReal) {
            pilaEvaluacion.push(new ValorReal(opnd1.valorReal() + opnd2.valorReal()));
         } else {
            pilaEvaluacion.push(new ValorInt(opnd1.valorInt() + opnd2.valorInt()));
         }
         pc++;
      }

      public String toString() {
         return "suma";
      };
   }

   private IResta IRESTA;

   private class IResta implements Instruccion {
      public void ejecuta() {
         Valor opnd2 = pilaEvaluacion.pop();
         Valor opnd1 = pilaEvaluacion.pop();
         if (opnd1 instanceof ValorReal || opnd2 instanceof ValorReal) {
            pilaEvaluacion.push(new ValorReal(opnd1.valorReal() - opnd2.valorReal()));
         } else {
            pilaEvaluacion.push(new ValorInt(opnd1.valorInt() - opnd2.valorInt()));
         }
         pc++;
      }

      public String toString() {
         return "resta";
      };
   }

   private IMul IMUL;

   private class IMul implements Instruccion {
      public void ejecuta() {
         Valor opnd2 = pilaEvaluacion.pop();
         Valor opnd1 = pilaEvaluacion.pop();
         if (opnd1 instanceof ValorReal || opnd2 instanceof ValorReal) {
            pilaEvaluacion.push(new ValorReal(opnd1.valorReal() * opnd2.valorReal()));
         } else {
            pilaEvaluacion.push(new ValorInt(opnd1.valorInt() * opnd2.valorInt()));
         }
         pc++;
      }

      public String toString() {
         return "mul";
      };
   }

   private IDiv IDIV;

   private class IDiv implements Instruccion {
      public void ejecuta() {
         Valor opnd2 = pilaEvaluacion.pop();
         Valor opnd1 = pilaEvaluacion.pop();
         if (opnd1 instanceof ValorReal || opnd2 instanceof ValorReal) {
            pilaEvaluacion.push(new ValorReal(opnd1.valorReal() / opnd2.valorReal()));
         } else {
            pilaEvaluacion.push(new ValorInt(opnd1.valorInt() / opnd2.valorInt()));
         }
         pc++;
      }

      public String toString() {
         return "div";
      };
   }

   private IMod IMOD;

   private class IMod implements Instruccion {
      public void ejecuta() {
         Valor opnd2 = pilaEvaluacion.pop();
         Valor opnd1 = pilaEvaluacion.pop();
         pilaEvaluacion.push(new ValorInt(opnd1.valorInt() % opnd2.valorInt()));
         pc++;
      }

      public String toString() {
         return "mod";
      };
   }

   private IAnd IAND;

   private class IAnd implements Instruccion {
      public void ejecuta() {
         Valor opnd2 = pilaEvaluacion.pop();
         Valor opnd1 = pilaEvaluacion.pop();
         pilaEvaluacion.push(new ValorBool(opnd1.valorBool() && opnd2.valorBool()));
         pc++;
      }

      public String toString() {
         return "and";
      };
   }

   private IOr IOR;

   private class IOr implements Instruccion {
      public void ejecuta() {
         Valor opnd2 = pilaEvaluacion.pop();
         Valor opnd1 = pilaEvaluacion.pop();
         pilaEvaluacion.push(new ValorBool(opnd1.valorBool() || opnd2.valorBool()));
         pc++;
      }

      public String toString() {
         return "or";
      };
   }

   private INot INOT;

   private class INot implements Instruccion {
      public void ejecuta() {
         Valor opnd = pilaEvaluacion.pop();
         pilaEvaluacion.push(new ValorBool(!opnd.valorBool()));
         pc++;
      }

      public String toString() {
         return "not";
      };
   }

   private IMenor IMENOR;

   private class IMenor implements Instruccion {
      public void ejecuta() {
         Valor opnd2 = pilaEvaluacion.pop();
         Valor opnd1 = pilaEvaluacion.pop();
         if (opnd1 instanceof ValorReal || opnd2 instanceof ValorReal) {
            pilaEvaluacion.push(new ValorBool(opnd1.valorReal() < opnd2.valorReal()));
         } else if (opnd1 instanceof ValorInt && opnd2 instanceof ValorInt) {
            pilaEvaluacion.push(new ValorBool(opnd1.valorInt() < opnd2.valorInt()));
         } else if (opnd1 instanceof ValorString && opnd2 instanceof ValorString) {
            pilaEvaluacion.push(new ValorBool(opnd1.valorString().compareTo(opnd2.valorString()) < 0));
         } else {
            throw new EAccesoIlegitimo(this);
         }
         pc++;
      }

      public String toString() {
         return "menor";
      };
   }

   private IMayor IMAYOR;

   private class IMayor implements Instruccion {
      public void ejecuta() {
         Valor opnd2 = pilaEvaluacion.pop();
         Valor opnd1 = pilaEvaluacion.pop();
         if (opnd1 instanceof ValorReal || opnd2 instanceof ValorReal) {
            pilaEvaluacion.push(new ValorBool(opnd1.valorReal() > opnd2.valorReal()));
         } else if (opnd1 instanceof ValorInt && opnd2 instanceof ValorInt) {
            pilaEvaluacion.push(new ValorBool(opnd1.valorInt() > opnd2.valorInt()));
         } else if (opnd1 instanceof ValorString && opnd2 instanceof ValorString) {
            pilaEvaluacion.push(new ValorBool(opnd1.valorString().compareTo(opnd2.valorString()) > 0));
         } else {
            throw new EAccesoIlegitimo(this);
         }
         pc++;
      }

      public String toString() {
         return "mayor";
      };
   }

   private IMenorIgual IMENORIGUAL;

   private class IMenorIgual implements Instruccion {
      public void ejecuta() {
         Valor opnd2 = pilaEvaluacion.pop();
         Valor opnd1 = pilaEvaluacion.pop();
         if (opnd1 instanceof ValorReal || opnd2 instanceof ValorReal) {
            pilaEvaluacion.push(new ValorBool(opnd1.valorReal() <= opnd2.valorReal()));
         } else if (opnd1 instanceof ValorInt && opnd2 instanceof ValorInt) {
            pilaEvaluacion.push(new ValorBool(opnd1.valorInt() <= opnd2.valorInt()));
         } else if (opnd1 instanceof ValorString && opnd2 instanceof ValorString) {
            pilaEvaluacion.push(new ValorBool(opnd1.valorString().compareTo(opnd2.valorString()) <= 0));
         } else {
            throw new EAccesoIlegitimo(this);
         }
         pc++;
      }

      public String toString() {
         return "menor_igual";
      };
   }

   private IMayorIgual IMAYORIGUAL;

   private class IMayorIgual implements Instruccion {
      public void ejecuta() {
         Valor opnd2 = pilaEvaluacion.pop();
         Valor opnd1 = pilaEvaluacion.pop();
         if (opnd1 instanceof ValorReal || opnd2 instanceof ValorReal) {
            pilaEvaluacion.push(new ValorBool(opnd1.valorReal() >= opnd2.valorReal()));
         } else if (opnd1 instanceof ValorInt && opnd2 instanceof ValorInt) {
            pilaEvaluacion.push(new ValorBool(opnd1.valorInt() >= opnd2.valorInt()));
         } else if (opnd1 instanceof ValorString && opnd2 instanceof ValorString) {
            pilaEvaluacion.push(new ValorBool(opnd1.valorString().compareTo(opnd2.valorString()) >= 0));
         } else {
            throw new EAccesoIlegitimo(this);
         }
         pc++;
      }

      public String toString() {
         return "mayor_igual";
      };
   }

   private IIgual IIGUAL;

   private class IIgual implements Instruccion {
      public void ejecuta() {
         Valor opnd2 = pilaEvaluacion.pop();
         Valor opnd1 = pilaEvaluacion.pop();
         if (opnd1 instanceof ValorReal || opnd2 instanceof ValorReal) {
            pilaEvaluacion.push(new ValorBool(opnd1.valorReal() == opnd2.valorReal()));
         } else {
            pilaEvaluacion.push(new ValorBool(opnd1.toString().equals(opnd2.toString())));
         }
         pc++;
      }

      public String toString() {
         return "igual";
      };
   }

   private IDistinto IDISTINTO;

   private class IDistinto implements Instruccion {
      public void ejecuta() {
         Valor opnd2 = pilaEvaluacion.pop();
         Valor opnd1 = pilaEvaluacion.pop();
         if (opnd1 instanceof ValorReal || opnd2 instanceof ValorReal) {
            pilaEvaluacion.push(new ValorBool(opnd1.valorReal() != opnd2.valorReal()));
         } else {
            pilaEvaluacion.push(new ValorBool(!opnd1.toString().equals(opnd2.toString())));
         }
         pc++;
      }

      public String toString() {
         return "distinto";
      };
   }

   private class IApilaInt implements Instruccion {
      private int valor;

      public IApilaInt(int valor) {
         this.valor = valor;
      }

      public void ejecuta() {
         pilaEvaluacion.push(new ValorInt(valor));
         pc++;
      }

      public String toString() {
         return "apila-int(" + valor + ")";
      };
   }

   private class IApilaBool implements Instruccion {
      private boolean valor;

      public IApilaBool(boolean valor) {
         this.valor = valor;
      }

      public void ejecuta() {
         pilaEvaluacion.push(new ValorBool(valor));
         pc++;
      }

      public String toString() {
         return "apila-bool(" + valor + ")";
      };
   }

   private class IApilaReal implements Instruccion {
      private double valor;

      public IApilaReal(double valor) {
         this.valor = valor;
      }

      public void ejecuta() {
         pilaEvaluacion.push(new ValorReal(valor));
         pc++;
      }

      public String toString() {
         return "apila-real(" + valor + ")";
      };
   }

   private class IApilaString implements Instruccion {
      private String valor;

      public IApilaString(String valor) {
         this.valor = valor;
      }

      public void ejecuta() {
         pilaEvaluacion.push(new ValorString(valor));
         pc++;
      }

      public String toString() {
         return "apila-string(" + valor + ")";
      };
   }

   private class IApilaDir implements Instruccion {
      private int dir;

      public IApilaDir(int dir) {
         this.dir = dir;
      }

      public void ejecuta() {
         if (dir >= datos.length)
            throw new EAccesoFueraDeRango();
         if (datos[dir] == null)
            throw new EAccesoAMemoriaNoInicializada(pc, dir);
         pilaEvaluacion.push(datos[dir]);
         pc++;
      }

      public String toString() {
         return "apila-dir(" + dir + ")";
      };
   }

   private class IDesapilaDir implements Instruccion {
      private int dir;

      public IDesapilaDir(int dir) {
         this.dir = dir;
      }

      public void ejecuta() {
         Valor valor = pilaEvaluacion.pop();
         if (dir >= datos.length)
            throw new EAccesoFueraDeRango();
         datos[dir] = valor;
         pc++;
      }

      public String toString() {
         return "desapila-dir(" + dir + ")";
      };
   }

   private class IIrA implements Instruccion {
      private Label label;

      public IIrA(Label label) {
         this.label = label;
      }

      public void ejecuta() {
         pc = label.address;
      }

      public String toString() {
         return "ir-a(" + label.address + ")";
      };
   }

   private class IIrF implements Instruccion {
      private Label label;

      public IIrF(Label label) {
         this.label = label;
      }

      public void ejecuta() {
         if (!pilaEvaluacion.pop().valorBool()) {
            pc = label.address;
         } else {
            pc++;
         }
      }

      public String toString() {
         return "ir-f(" + label.address + ")";
      };
   }

   private class ICopia implements Instruccion {
      private int tam;

      public ICopia(int tam) {
         this.tam = tam;
      }

      public void ejecuta() {
         int dirOrigen = pilaEvaluacion.pop().valorInt();
         int dirDestino = pilaEvaluacion.pop().valorInt();
         if ((dirOrigen + (tam - 1)) >= datos.length)
            throw new EAccesoFueraDeRango();
         if ((dirDestino + (tam - 1)) >= datos.length)
            throw new EAccesoFueraDeRango();
         for (int i = 0; i < tam; i++)
            datos[dirDestino + i] = datos[dirOrigen + i];
         pc++;
      }

      public String toString() {
         return "copia(" + tam + ")";
      };
   }

   private IApilaind IAPILAIND;

   private class IApilaind implements Instruccion {
      public void ejecuta() {
         int dir = pilaEvaluacion.pop().valorInt();
         if (dir >= datos.length)
            throw new EAccesoFueraDeRango();
         if (datos[dir] == null)
            throw new EAccesoAMemoriaNoInicializada(pc, dir);
         pilaEvaluacion.push(datos[dir]);
         pc++;
      }

      public String toString() {
         return "apila-ind";
      };
   }

   private IDesapilaind IDESAPILAIND;

   private class IDesapilaind implements Instruccion {
      public void ejecuta() {
         Valor valor = pilaEvaluacion.pop();
         int dir = pilaEvaluacion.pop().valorInt();
         if (dir >= datos.length)
            throw new EAccesoFueraDeRango();
         datos[dir] = valor;
         pc++;
      }

      public String toString() {
         return "desapila-ind";
      };
   }

   private class IAlloc implements Instruccion {
      private int tam;

      public IAlloc(int tam) {
         this.tam = tam;
      }

      public void ejecuta() {
         int inicio = gestorMemoriaDinamica.alloc(tam);
         pilaEvaluacion.push(new ValorInt(inicio));
         pc++;
      }

      public String toString() {
         return "alloc(" + tam + ")";
      };
   }

   private class IDealloc implements Instruccion {
      private int tam;

      public IDealloc(int tam) {
         this.tam = tam;
      }

      public void ejecuta() {
         int inicio = pilaEvaluacion.pop().valorInt();
         gestorMemoriaDinamica.free(inicio, tam);
         pc++;
      }

      public String toString() {
         return "dealloc(" + tam + ")";
      };
   }

   private class IActiva implements Instruccion {
      private int nivel;
      private int tamdatos;
      private Label returnLabel;

      public IActiva(int nivel, int tamdatos, Label returnLabel) {
         this.nivel = nivel;
         this.tamdatos = tamdatos;
         this.returnLabel = returnLabel;
      }

      public void ejecuta() {
         int base = gestorPilaActivaciones.creaRegistroActivacion(tamdatos);
         datos[base] = new ValorInt(returnLabel.address);
         datos[base + 1] = new ValorInt(gestorPilaActivaciones.display(nivel));
         pilaEvaluacion.push(new ValorInt(base));
         pc++;
      }

      public String toString() {
         return "activa(" + nivel + "," + tamdatos + "," + returnLabel.address + ")";
      }
   }

   private class IDesactiva implements Instruccion {
      private int nivel;
      private int tamdatos;

      public IDesactiva(int nivel, int tamdatos) {
         this.nivel = nivel;
         this.tamdatos = tamdatos;
      }

      public void ejecuta() {
         int base = gestorPilaActivaciones.liberaRegistroActivacion(tamdatos);
         gestorPilaActivaciones.fijaDisplay(nivel, datos[base + 1].valorInt());
         pilaEvaluacion.push(datos[base]);
         pc++;
      }

      public String toString() {
         return "desactiva(" + nivel + "," + tamdatos + ")";
      }

   }

   private class IDesapilad implements Instruccion {
      private int nivel;
      private int despl;

      public IDesapilad(int nivel, int despl) {
         this.nivel = nivel;
         this.despl = despl;
      }

      public void ejecuta() {
         Valor valor = pilaEvaluacion.pop();
         int addr = gestorPilaActivaciones.display(nivel) + despl;
         if (addr >= datos.length)
            throw new EAccesoFueraDeRango();
         datos[addr] = valor;
         pc++;
      }

      public String toString() {
         return "desapilad(" + nivel + "," + despl + ")";
      }
   }

   private IDup IDUP;

   private class IDup implements Instruccion {
      public void ejecuta() {
         pilaEvaluacion.push(pilaEvaluacion.peek());
         pc++;
      }

      public String toString() {
         return "dup";
      }
   }

   private Instruccion ISTOP;

   private class IStop implements Instruccion {
      public void ejecuta() {
         pc = codigoP.size();
      }

      public String toString() {
         return "stop";
      }
   }

   private class IApilad implements Instruccion {
      private int nivel;
      private int despl;

      public IApilad(int nivel, int despl) {
         this.nivel = nivel;
         this.despl = despl;
      }

      public void ejecuta() {
         pilaEvaluacion.push(new ValorInt(gestorPilaActivaciones.display(nivel) + despl));
         pc++;
      }

      public String toString() {
         return "apilad(" + nivel + "," + despl + ")";
      }

   }

   private Scanner sc;

   private class IReadInt implements Instruccion {
      public void ejecuta() {
         pilaEvaluacion.push(new ValorInt(sc.nextInt()));
         if (sc.hasNextLine())
            sc.nextLine();
         pc++;
      }

      public String toString() {
         return "read-int";
      }
   }

   private class IReadReal implements Instruccion {
      public void ejecuta() {
         pilaEvaluacion.push(new ValorReal(sc.nextDouble()));
         if (sc.hasNextLine())
            sc.nextLine();
         pc++;
      }

      public String toString() {
         return "read-real";
      }
   }

   private class IReadString implements Instruccion {
      public void ejecuta() {
         pilaEvaluacion.push(new ValorString(sc.nextLine()));
         pc++;
      }

      public String toString() {
         return "read-string";
      }
   }

   private class IPrint implements Instruccion {
      public void ejecuta() {
         Valor v = pilaEvaluacion.pop();
         if (v instanceof ValorBool) {
            System.out.print(v.valorBool() ? "true" : "false");
         } else {
            System.out.print(v);
         }
         pc++;
      }

      public String toString() {
         return "print";
      }
   }

   private Instruccion IIRIND;

   private class IIrind implements Instruccion {
      public void ejecuta() {
         pc = pilaEvaluacion.pop().valorInt();
      }

      public String toString() {
         return "ir-ind";
      }
   }

   public Instruccion suma() {
      return ISUMA;
   }

   public Instruccion resta() {
      return IRESTA;
   }

   public Instruccion mul() {
      return IMUL;
   }

   public Instruccion div() {
      return IDIV;
   }

   public Instruccion mod() {
      return IMOD;
   }

   public Instruccion and() {
      return IAND;
   }

   public Instruccion or() {
      return IOR;
   }

   public int programaSize() {
      return codigoP.size();
   }

   public Instruccion not() {
      return INOT;
   }

   public Instruccion menor() {
      return IMENOR;
   }

   public Instruccion mayor() {
      return IMAYOR;
   }

   public Instruccion menor_igual() {
      return IMENORIGUAL;
   }

   public Instruccion mayor_igual() {
      return IMAYORIGUAL;
   }

   public Instruccion igual() {
      return IIGUAL;
   }

   public Instruccion distinto() {
      return IDISTINTO;
   }

   public Instruccion apila_int(int val) {
      return new IApilaInt(val);
   }

   public Instruccion apila_real(double val) {
      return new IApilaReal(val);
   }

   public Instruccion apila_bool(boolean val) {
      return new IApilaBool(val);
   }

   public Instruccion apila_string(String val) {
      return new IApilaString(val);
   }

   public Instruccion apila_dir(int dir) {
      return new IApilaDir(dir);
   }

   public Instruccion desapila_dir(int dir) {
      return new IDesapilaDir(dir);
   }

   public Instruccion apilad(int nivel, int despl) {
      return new IApilad(nivel, despl);
   }

   public Instruccion apila_ind() {
      return IAPILAIND;
   }

   public Instruccion desapila_ind() {
      return IDESAPILAIND;
   }

   public Instruccion copia(int tam) {
      return new ICopia(tam);
   }

   public Instruccion ir_a(Label label) {
      return new IIrA(label);
   }

   public Instruccion ir_f(Label label) {
      return new IIrF(label);
   }

   public Instruccion ir_ind() {
      return IIRIND;
   }

   public Instruccion alloc(int tam) {
      return new IAlloc(tam);
   }

   public Instruccion dealloc(int tam) {
      return new IDealloc(tam);
   }

   public Instruccion activa(int nivel, int tam, Label returnLabel) {
      return new IActiva(nivel, tam, returnLabel);
   }

   public Instruccion desactiva(int nivel, int tam) {
      return new IDesactiva(nivel, tam);
   }

   public Instruccion desapilad(int nivel, int despl) {
      return new IDesapilad(nivel, despl);
   }

   public Instruccion dup() {
      return IDUP;
   }

   public Instruccion stop() {
      return ISTOP;
   }

   public Instruccion read_int() {
      return new IReadInt();
   }

   public Instruccion read_real() {
      return new IReadReal();
   }

   public Instruccion read_string() {
      return new IReadString();
   }

   public Instruccion print() {
      return new IPrint();
   }

   public void emit(Instruccion i) {
      codigoP.add(i);
   }

   public void setLabelAddress(Label label) {
      label.address = codigoP.size();
   }

   public void addInstr(Instr instr) {
      switch (instr.op) {
         case SUMA:
            emit(suma());
            break;
         case RESTA:
            emit(resta());
            break;
         case MUL:
            emit(mul());
            break;
         case DIV:
            emit(div());
            break;
         case MOD:
            emit(mod());
            break;
         case AND:
            emit(and());
            break;
         case OR:
            emit(or());
            break;
         case NOT:
            emit(not());
            break;
         case MENOR:
            emit(menor());
            break;
         case MAYOR:
            emit(mayor());
            break;
         case MENOR_IGUAL:
            emit(menor_igual());
            break;
         case MAYOR_IGUAL:
            emit(mayor_igual());
            break;
         case IGUAL:
            emit(igual());
            break;
         case DISTINTO:
            emit(distinto());
            break;
         case APILA_INT:
            emit(apila_int((Integer) instr.args[0]));
            break;
         case APILA_REAL:
            emit(apila_real(((Number) instr.args[0]).doubleValue()));
            break;
         case APILA_BOOL:
            emit(apila_bool((Boolean) instr.args[0]));
            break;
         case APILA_STRING:
            emit(apila_string((String) instr.args[0]));
            break;
         case APILA_DIR:
            emit(apila_dir((Integer) instr.args[0]));
            break;
         case DESAPILA_DIR:
            emit(desapila_dir((Integer) instr.args[0]));
            break;
         case APILAD:
            emit(apilad((Integer) instr.args[0], (Integer) instr.args[1]));
            break;
         case DESAPILAD:
            emit(desapilad((Integer) instr.args[0], (Integer) instr.args[1]));
            break;
         case DUP:
            emit(dup());
            break;
         case APILA_IND:
            emit(apila_ind());
            break;
         case DESAPILA_IND:
            emit(desapila_ind());
            break;
         case COPIA:
            emit(copia((Integer) instr.args[0]));
            break;
         case IR_A:
            emit(ir_a((Label) instr.args[0]));
            break;
         case IR_F:
            emit(ir_f((Label) instr.args[0]));
            break;
         case IR_IND:
            emit(ir_ind());
            break;
         case ACTIVA:
            emit(activa((Integer) instr.args[0], (Integer) instr.args[1], (Label) instr.args[2]));
            break;
         case DESACTIVA:
            emit(desactiva((Integer) instr.args[0], (Integer) instr.args[1]));
            break;
         case ALLOC:
            emit(alloc((Integer) instr.args[0]));
            break;
         case DEALLOC:
            emit(dealloc((Integer) instr.args[0]));
            break;
         case READ_INT:
            emit(read_int());
            break;
         case READ_REAL:
            emit(read_real());
            break;
         case READ_STRING:
            emit(read_string());
            break;
         case PRINT:
            emit(print());
            break;
         case STOP:
            emit(stop());
            break;
         case FIJAD:
            emit(fijad((Integer) instr.args[0]));
            break;
         case POP:
            emit(pop());
            break;
      }
   }

   public Instr instruccion(Op op, Object... args) {
      return new Instr(op, args);
   }

   public void cargaPrograma(List<Instr> p) {
      // Compatibility with GeneracionCodigo
      for (Instr i : p)
         addInstr(i);
   }

   private int tamdatos;
   private int tamheap;
   private int ndisplays;

   public MaquinaP(Reader input, int tamdatos, int tampila, int tamheap, int ndisplays) {
      this.sc = new Scanner(input);
      this.tamdatos = tamdatos;
      this.tamheap = tamheap;
      this.ndisplays = ndisplays;
      this.codigoP = new ArrayList<>();
      pilaEvaluacion = new Stack<>();
      datos = new Valor[tamdatos + tampila + tamheap];
      this.pc = 0;
      ISUMA = new ISuma();
      IRESTA = new IResta();
      IAND = new IAnd();
      IOR = new IOr();
      INOT = new INot();
      IMUL = new IMul();
      IDIV = new IDiv();
      IMOD = new IMod();
      IMENOR = new IMenor();
      IMAYOR = new IMayor();
      IMENORIGUAL = new IMenorIgual();
      IMAYORIGUAL = new IMayorIgual();
      IIGUAL = new IIgual();
      IDISTINTO = new IDistinto();
      IAPILAIND = new IApilaind();
      IDESAPILAIND = new IDesapilaind();
      IIRIND = new IIrind();
      IDUP = new IDup();
      ISTOP = new IStop();
      IPOP = new IPop();
      gestorPilaActivaciones = new GestorPilaActivaciones(tamdatos, (tamdatos + tampila) - 1, ndisplays);
      gestorMemoriaDinamica = new GestorMemoriaDinamica(tamdatos + tampila, (tamdatos + tampila + tamheap) - 1);
   }

   public void ejecuta() {
      while (pc != codigoP.size()) {
         codigoP.get(pc).ejecuta();
      }
   }

   public void muestraCodigo() {
      System.out.println("CodigoP");
      for (int i = 0; i < codigoP.size(); i++) {
         System.out.println(" " + i + ":" + codigoP.get(i));
      }
   }

   public void muestraEstado() {
      System.out.println("Tam datos:" + tamdatos);
      System.out.println("Tam heap:" + tamheap);
      System.out.println("PP:" + gestorPilaActivaciones.pp());
      System.out.print("Displays:");
      for (int i = 1; i <= ndisplays; i++)
         System.out.print(i + ":" + gestorPilaActivaciones.display(i) + " ");
      System.out.println();
      System.out.println("Pila de evaluacion");
      for (int i = 0; i < pilaEvaluacion.size(); i++) {
         System.out.println(" " + i + ":" + pilaEvaluacion.get(i));
      }
      System.out.println("Datos");
      for (int i = 0; i < datos.length; i++) {
         System.out.println(" " + i + ":" + datos[i]);
      }
      System.out.println("PC:" + pc);
   }

   public Instruccion fijad(int nivel) {
      return new IFijad(nivel);
   }

   private class IFijad implements Instruccion {
      private int nivel;

      public IFijad(int nivel) {
         this.nivel = nivel;
      }

      public void ejecuta() {
         int base = pilaEvaluacion.pop().valorInt();
         gestorPilaActivaciones.fijaDisplay(nivel, base);
         pc++;
      }

      public String toString() {
         return "fijad(" + nivel + ")";
      }
   }

   public Instruccion pop() {
      return IPOP;
   }

   private IPop IPOP;

   private class IPop implements Instruccion {
      public void ejecuta() {
         pilaEvaluacion.pop();
         pc++;
      }

      public String toString() {
         return "pop";
      }
   }
}

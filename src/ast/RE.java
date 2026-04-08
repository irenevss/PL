package ast;

public class RE {
    public abstract static class RE0 {
        public abstract Exp apply(Exp left);
    }

    public static class RE0_op extends RE0 {
        public String op;
        public Exp right;

        public RE0_op(String op, Exp right) {
            this.op = op;
            this.right = right;
        }

        public Exp apply(Exp left) {
            if (op.equals("<")) return new Exp.Exp_menor(left, right, 0, 0);
            if (op.equals(">")) return new Exp.Exp_mayor(left, right, 0, 0);
            if (op.equals("<=")) return new Exp.Exp_menor_igual(left, right, 0, 0);
            if (op.equals(">=")) return new Exp.Exp_mayor_igual(left, right, 0, 0);
            if (op.equals("=")) return new Exp.Exp_igual(left, right, 0, 0);
            if (op.equals("<>")) return new Exp.Exp_distinto(left, right, 0, 0);
            return null; // error
        }
    }

    public static class RE0_eps extends RE0 {
        public Exp apply(Exp left) {
            return left;
        }
    }

    public abstract static class RE1 {
        public abstract Exp apply(Exp left);
    }

    public static class RE1_op extends RE1 {
        public String op;
        public Exp right;

        public RE1_op(String op, Exp right) {
            this.op = op;
            this.right = right;
        }

        public Exp apply(Exp left) {
            if (op.equals("+")) return new Exp.Exp_suma(left, right, 0, 0);
            if (op.equals("-")) return new Exp.Exp_resta(left, right, 0, 0);
            if (op.equals("|")) return new Exp.Exp_or(left, right, 0, 0);
            return null;
        }
    }

    public static class RE1_eps extends RE1 {
        public Exp apply(Exp left) {
            return left;
        }
    }

    public abstract static class RE2 {
        public abstract Exp apply(Exp left);
    }

    public static class RE2_op extends RE2 {
        public String op;
        public Exp right;

        public RE2_op(String op, Exp right) {
            this.op = op;
            this.right = right;
        }

        public Exp apply(Exp left) {
            if (op.equals("*")) return new Exp.Exp_mul(left, right, 0, 0);
            if (op.equals("/")) return new Exp.Exp_div(left, right, 0, 0);
            if (op.equals("%")) return new Exp.Exp_mod(left, right, 0, 0);
            if (op.equals("&")) return new Exp.Exp_and(left, right, 0, 0);
            return null;
        }
    }

    public static class RE2_eps extends RE2 {
        public Exp apply(Exp left) {
            return left;
        }
    }

    public abstract static class RE3 {
        public abstract Exp apply(Exp left);
    }

    public static class RE3_op extends RE3 {
        public Exp.PostfixOp op;

        public RE3_op(Exp.PostfixOp op) {
            this.op = op;
        }

        public Exp apply(Exp left) {
            return op.apply(left);
        }
    }

    public static class RE3_eps extends RE3 {
        public Exp apply(Exp left) {
            return left;
        }
    }
}
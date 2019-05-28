package manyif.pattern._enum;

public enum Handler {
    GROUP550 {
        @Override
        public int apply(int a, int b) {
            return a + b;
        }
    },
    TE50 {
        @Override
        public int apply(int a, int b) {
            return a - b;
        }
    };
    // other operators
    public abstract int apply(int a, int b);
}

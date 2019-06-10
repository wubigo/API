package manyif.pattern._enum;


public class CMD {
    public int transform(int a, int b, Handler operator) {
        return operator.apply(a, b);
    }

    public static void main(String args[]){

         int result = new CMD().transform(1, 2, Handler.valueOf("GROUP550"));
         System.out.println("reuslt="+result);

    }

}

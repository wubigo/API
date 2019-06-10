package manyif.pattern.mapfactory;

public class Group550 implements Operation {
    @Override
    public int apply(int a, int b) {
        int result = a + b ;
        System.out.println("result="+result);
        return result;
    }
}

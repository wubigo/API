package manyif.pattern.mapfactory;

import java.util.Optional;

public class CMD {
    public static void main(String args[]){
        int a = 1;
        int b = 2;
        System.out.println("a="+1);
        Optional<Operation> o = OperatorFactory.getOperation("Group550");
        if ( o.isPresent() ) {
            o.ifPresent(operation -> operation.apply(a, b));
        }

    }
}

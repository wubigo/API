package manyif.pattern.mapfactory;

import java.util.Optional;

public class CMD {
    public static void main(String args[]){
        int a = 1;
        int b = 2;
        Optional<Operation> o = OperatorFactory.getOperation("group550");

        o.ifPresent(operation -> operation.apply(a, b));
        

    }
}

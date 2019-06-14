package manyif.pattern._enum;


import com.timon.common.Device;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CMD {
    public int transform(int a, int b, Handler operator) {
        return operator.apply(a, b);
    }

    public static void main(String args[]){

         log.info("{} {}", Device.Group550.toString(), Device.Group550.name());

         int result = new CMD().transform(1, 2, Handler.valueOf("GROUP550"));
         System.out.println("reuslt="+result);

    }

}

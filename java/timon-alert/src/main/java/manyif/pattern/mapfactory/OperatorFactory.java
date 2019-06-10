package manyif.pattern.mapfactory;

import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OperatorFactory {

    @Value("${device.category}")
    private String deviceCategory;

    static Map<String, Operation> operationMap = new HashMap<>();
    static {
        try {
            //operationMap.put("group550", new Group550());
            operationMap.put("Group550", (Operation) Class.forName("manyif.pattern.mapfactory.Group550").newInstance());
            // more operators
        } catch (ClassNotFoundException cfe) {

        } catch (InstantiationException ie) {

        } catch (IllegalAccessException iae) {

        }
    }

    public static Optional<Operation> getOperation(String operator) {
        return Optional.ofNullable(operationMap.get(operator));
    }

}

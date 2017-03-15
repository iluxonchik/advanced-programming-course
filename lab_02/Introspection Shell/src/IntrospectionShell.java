import javax.sound.midi.SysexMessage;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/*
    Please don't jude the quality of this code :(
    This is more like a POC, than actual code that should be used
    in production.

    Teacher's Suggestion:
    Create command classes for each one, then get those classes via reflection (Class.forName()). Name each one of those
    classed like "ClassCommand", "SetCommand", ... . Then get the command name from user input, append "Command", get
    that class via reflection and call it to treat the call.
 */
public class IntrospectionShell {
        static Object unsaved;
        static Object selectedObj;
        static HashMap<String, Object> saved = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.print("Command:> ");
            String input = scanner.nextLine();

            String[] splitStr = input.split(" ");

            switch (splitStr[0].toLowerCase()){
                case("class"):
                    // TODO
                    // Class <name> : obtain instance of Class<name>
                    classCommand(splitStr);
                    break;
                case("set"):
                    // TODO
                    // Set <name>: save object from last result with name <name>
                    setCommand(splitStr);
                    break;
                case("get"):
                    // TODO
                    // Get <name> : select object previously saved with <name>
                    getCommand(splitStr);
                    break;
                case("index"):
                    // TODO
                    // Index <int> : select object within an array. The array should have been obtained as the result
                    //               of the previous command.
                    indexCommand(splitStr);
                    break;
                case("exit"):
                    System.out.println("Exiting...");
                    System.exit(1);
                    break;
                default:
                    // TODO:
                    // check if the attempted command names a method that can be invoked
                    // on the result of the previous command. If yes, the following params
                    // should be used as parameters to the method to invoke.
                    // An attempt is made to discover the best method via reflection and to convert the given
                    // parameters to the correct types.
                    tryInvoke(splitStr);
                    break;
            }
        }
    }


    private static void classCommand(String[] splitStr) {
        String className = splitStr[1];
        Class c = null;
        try {
            c = Class.forName(className);
            unsaved = c;
            selectedObj = unsaved;
            System.out.println(unsaved.getClass());
        } catch (ClassNotFoundException e) {
            System.err.println("Error: class \"" + splitStr[1] + "\" not found.");
        }
    }
    private static void setCommand(String[] splitStr) {
        if (unsaved != null) {
            saved.put(splitStr[1], unsaved);
            System.out.println("Saved name for object of type: " + unsaved.getClass());
            System.out.println(unsaved.getClass());
            selectedObj = unsaved;
            unsaved = null;
        } else {
            System.err.println("Could not save object, since it's null");
        }

    }

    private static void getCommand(String[] splitStr) {
        selectedObj = saved.get(splitStr[1]);
        if (selectedObj != null) {
            System.out.println(selectedObj.getClass());
        } else {
            System.err.println("Could not execute command since selected object is null");
        }
    }

    private static void indexCommand(String[] splitStr) {
        if (selectedObj != null) {
            if (selectedObj.getClass().isArray()) {
                int index = Integer.parseInt(splitStr[1]);
                selectedObj = ((Object[])selectedObj)[index];
                unsaved = selectedObj;
                System.out.println(selectedObj);
            } else {
                System.err.println("Could not execute command since selected object is not an array");
            }
        } else {
            System.err.println("Could not execute command since selected object is null");
        }
    }

    private static void tryInvoke(String[] splitStr) {
        System.out.println("Trying generic command: " + splitStr[0]);
        if (selectedObj != null) {
            invoke(selectedObj, splitStr[0], Arrays.copyOfRange(splitStr, 1, splitStr.length));
        } else {
            System.err.println("Could not execute " + String.join(" ", splitStr));
        }
    }

    private static void invoke(Object receiver, String name, String[] args) {
        try {
            Object res = invokeBestMethod(receiver, name, args);
            if (res != null) {
                selectedObj = res;
            } else {
                System.err.println("Result of invokeBestMethod is null");
            }
            if (res != null && res.getClass().isArray()) {
                for (Object o : (Object[]) res) {
                    System.out.println(o);
                }
            } else {
                System.out.println(res);
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private static Object invokeBestMethod(Object receiver, String name, String[] args) throws InvocationTargetException,
            IllegalAccessException {
        Class type = receiver.getClass();
        int paramCount = args.length;
        Method[] methods = type.getMethods();
        Method bestMethod = null;
        Object[] castedParams = null;

        for (Method method : methods) {
            if (name.equals(method.getName())) {
                // method name matches the one that was invoked from the shell
                if (method.getParameterCount() == paramCount) {
                    // parameter count matches, let's try to cast them
                    Class[] paramTypes = method.getParameterTypes();
                    // try and cast the provided params
                    castedParams = tryCastParameterTypes(args, paramTypes);
                    if (castedParams != null) {
                        bestMethod = method;
                        break;
                    }
                }
            }
        }
        if (bestMethod == null) {
            System.err.println("Could not find best method.");
            return null;
        }
        return bestMethod.invoke(receiver, castedParams);
    }

    private static Object[] tryCastParameterTypes(String[] args, Class[] paramTypes) {
        Object[] res = new Object[args.length];
        for (int i = 0; i < paramTypes.length; i++) {
            Class param = paramTypes[i];
            String currArg = args[i];

            if (param.equals(Integer.class)) {
                res[i] = Integer.parseInt(currArg);
            } else if (param.equals(Byte.class)) {
                res[i] = Byte.parseByte(currArg);
            } else if (param.equals(Short.class)) {
                res[i] = Short.parseShort(currArg);
            } else if (param.equals(Long.class)) {
                res[i] = Long.parseLong(currArg);
            } else if (param.equals(Float.class)) {
                res[i] = Float.parseFloat(currArg);
            } else if (param.equals(Double.class)) {
                res[i] = Double.parseDouble(currArg);
            } else if (param.equals(String.class)) {
                res[i] = new String(currArg);
            } else {
                System.err.println("Could not find an appropriate class to cast the parameter to");
                return null; // ughhh, great coding style (not)
            }
        }
        return res;
    }
}

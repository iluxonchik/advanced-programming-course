/*
    Please don't jude the quality of this code :(
    This is more like a POC, than actual code that should be used
    in production.
 */
public class IntrospectionShell {

    public static void main(String[] args) {
        while(true) {
            System.out.println("Command:> ");
            String input = System.console().readLine();

            String[] splitStr = input.split(" ");

            switch (splitStr[0].toLowerCase()){
                case("class"):
                    // TODO
                    // Class <name> : obtain instance of Class<name>
                    break;
                case("set"):
                    // TODO
                    // Set <name>: save object from last result with name <name>
                    break;
                case("get"):
                    // TODO
                    // Get <name> : select object previously saved with <name>
                    break;
                case("index"):
                    // TODO
                    // Index <int> : select object within an array. The array should have been obtained as the result
                    //               of the previous command.
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
            }
        }
    }
}

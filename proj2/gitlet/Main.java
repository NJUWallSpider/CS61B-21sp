package gitlet;

import java.io.IOException;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Bai Haozhen
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                validateNumArgs("init", args, 1);
                Repository.initiate();
                break;
            case "add":
                validateNumArgs("add", args, 2);
                Repository.add(args[1]);
                break;
            case "commit":
                validateNumArgs("commit", args, 2);
                Repository.commit(args[1]);
                break;
            case "status":
                validateNumArgs("status", args, 1);
                Repository.status();
                break;
            case "checkout":
                if (args.length == 3) {
                    Repository.checkoutFile(args[2]);
                } else if (args.length == 4) {
                    Repository.checkoutFile(args[3], args[1]);
                } else if (args.length == 2) {
                    Repository.checkoutBranch(args[1]);
                } else {
                    validateNumArgs("checkout", args, 2);
                }
                break;
            case "log":
                validateNumArgs("log", args, 1);
                Repository.log();
                break;
            case "rm":
                validateNumArgs("rm", args, 2);
                Repository.rm(args[1]);
            default:
                System.out.println("No command with that name exists.");
        }
    }

    /**
     * validate the arg numbers, if the number is wrong, throw the specific exception.
     * @param cmd the specific exception
     * @param args arg list input
     * @param n number of args you want
     */
    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            throw new RuntimeException(
                    String.format("Invalid number of arguments for: %s.", cmd));
        }
    }
}

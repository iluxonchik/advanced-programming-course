package pva.lab03;

import pva.lab03.task.TaskRunner;

public class Main {
    public static void main(String[] args) throws Exception {
        TaskRunner tr = new TaskRunner(args[0]);
        tr.run();
    }
}

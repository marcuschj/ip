package duke;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Storage {
    private String filePath;
    private Duke duke;

    public Storage(String filePath, Duke duke) {
        this.filePath = filePath;
        this.duke = duke;
    }

    protected void loadFileToList() throws FileNotFoundException {
        File f = new File(filePath); // create a File for the given file path
        Scanner s = new Scanner(f); // create a Scanner using the File as the source
        while (s.hasNext()) {
            String currentLine = s.nextLine();
            String[] taskData = currentLine.split("\\|");

            String category = taskData[0].trim();
            boolean isDone = taskData[1].trim().equals("1");
            String description = taskData[2].trim();

            if (category.equals("T")) {
                duke.getTasks().createTask(description, "", Task.Category.TODO, isDone, false);
                continue;
            }
            String time = taskData[3].trim();
            if (category.equals("D")) {
                duke.getTasks().createTask(description, time, Task.Category.DEADLINE, isDone, false);
            }
            if (category.equals("E")) {
                duke.getTasks().createTask(description, time, Task.Category.EVENT, isDone, false);
            }
        }
        duke.getUi().showListLoad();
    }

    protected void saveListToFile() throws IOException {
        duke.getUi().saveList();
        FileWriter fw = new FileWriter(filePath);
        String newInput = "";

        for (Task task : this.duke.getTasks().getList()) {
            switch (task.category) {
            case TODO:
                ToDo todo = (ToDo) task;
                int done = todo.isDone ? 1 : 0;
                newInput = newInput + ("T | " + done + " | " + todo.description + "\n");
                break;
            case DEADLINE:
                Deadline deadline = (Deadline) task;
                int done1 = deadline.isDone ? 1 : 0;
                newInput = newInput + ("D | " + done1 + " | " + deadline.description + " | " + deadline.by + "\n");
                break;
            case EVENT:
                Event event = (Event) task;
                int done2 = event.isDone ? 1 : 0;
                newInput = newInput + ("E | " + done2 + " | " + event.description + " | " + event.at + "\n");
                break;
            }
        }

        fw.write(newInput);
        fw.close();
    }
}
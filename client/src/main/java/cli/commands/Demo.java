package cli.commands;

import api.CastexSkiAPI;
import cli.framework.Command;
import cli.framework.Shell;

import java.io.InputStream;

public class Demo extends Command<CastexSkiAPI> {

    private static final String directory = "demo/";
    private static final String extension = ".ctxsk";

    private void demo() {
        runResource(directory + "initCatalog" + extension);
        runResource(directory + "initResort" + extension);
        runResource(directory + "initUsers" + extension);
        runResource(directory + "linkPassesAndCards" + extension);
        runResource(directory + "checkCards" + extension);
        runResource(directory + "showStats" + extension);
        shell.run(System.in, false, Shell.NB_INDENT);
    }

    private void resetDemo() {
        runResource(directory + "resetUsers" + extension);
        runResource(directory + "resetResort" + extension);
        runResource(directory + "resetCatalog" + extension);
    }

    private void runResource(String filename) {
        InputStream file = Demo.class.getClassLoader().getResourceAsStream(filename);
        shell.run(file,true,Shell.NB_INDENT);
    }

    @Override
    public String identifier() {
        return "runDemo";
    }

    @Override
    public void execute() throws Exception {
        Shell.NB_INDENT -= 4;
        demo();
        resetDemo();
        Shell.NB_INDENT += 4;
    }

    @Override
    public String describe() {
        return "# to launch our demo";
    }
}

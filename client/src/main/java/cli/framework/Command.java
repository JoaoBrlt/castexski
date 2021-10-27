package cli.framework;


import java.util.List;

public abstract class Command<T> {
    protected Shell<T> shell;

    abstract public String identifier();
    abstract public void execute() throws Exception;
    abstract public String describe();

    public boolean shouldContinue() { return true; }
    public void load(List<String> args) {  }
    public void withShell(Shell<T> shell) { this.shell = shell;   }
    public boolean process(List<String> args) throws Exception {
        if (args.size()>0 && args.get(0).startsWith("?")) {
            System.out.println(shell.getIndent()+"  - "+identifier()+": "+describe());
        } else {
            try {
                load(args);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
            Shell.NB_INDENT += 4;
            execute();
            Shell.NB_INDENT -= 4;
        }
        return shouldContinue();
    }
}

package cli.framework;


import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class Shell<T> {
    public static String INDENT = " ";
    public static int NB_INDENT = 0;
    public T system;
    protected String invite;
    private final Map<String, Class<? extends Command<T>>> commands = new HashMap<>();
    private static final String HELP_SYMBOL = "?";
    public Shell<T> internShell;
    public String userEmail;
    private Scanner scanner = null;
    private Scanner oldScanner = null;
    private InputStream stream = null;
    private boolean shouldEcho = false;

    public final void run() {
        run(true);
    }
    public final void run(boolean verbose) {
        if (verbose) {
            System.out.println("\nInteractive shell started.\n  " + HELP_SYMBOL + " for help.\n");
        }
        run(System.in, false, 0);
    }

    public void run(InputStream stream, boolean shouldEcho, int indent) {
        if (scanner==null&&stream==null) return;
        if (scanner==null || this.stream!=stream) {
            oldScanner = scanner;
            scanner = new Scanner(stream);
        }
        this.stream = stream;
        NB_INDENT = indent;
        this.shouldEcho = shouldEcho;
        boolean shouldContinue = true;
        while(shouldContinue) {
            System.out.flush();
            for(int i = 0; i < NB_INDENT; i++) { System.out.print(INDENT); }
            System.out.print(invite + " > ");
            if(!scanner.hasNext()) { System.out.println("\u001B[32mReaching end of file\u001B[0m\n"); break; }
            String keyword = scanner.next().trim();
            String rawArgs;
            List<String> args;
            if (scanner.hasNextLine()) {
                rawArgs = scanner.nextLine();
                args = Arrays.stream(rawArgs.split("\\\"?( |$)(?=(([^\\\"]*\\\"){2})*[^\\\"]*$)\\\"?")).filter(s -> !s.isEmpty()).collect(Collectors.toList());
            } else { rawArgs = ""; args = new LinkedList<>(); }
            if(shouldEcho) {
                System.out.print((keyword.startsWith("#"))?"\u001B[34m":"\u001B[36m");
                System.out.println(keyword + rawArgs + "\u001B[0m");
            }
            if (keyword.equals(HELP_SYMBOL)) { help(); }
            else {
                try {
                    if (!keyword.startsWith("#") && !keyword.equals(""))
                        shouldContinue = processCommand(keyword, args);
                }
                catch (IllegalArgumentException iae) {
                    System.err.println("Illegal arguments for command "+keyword+": " + args);
                } catch (Exception e) {
                    System.err.println("Exception caught while processing command:\n  " + e);
                    e.printStackTrace();
                }
            }
        }
        scanner = oldScanner;
    }

    public void runInternal(boolean isRoot) {
        if (!isRoot) {
            runInternal("customer");
        } else if (!invite.equalsIgnoreCase("root")) {
            runInternal("root");
        }
    }

    private void runInternal(String invite) {
        internShell.invite = invite;
        internShell.scanner = scanner;
        internShell.run(null, shouldEcho, NB_INDENT + 4);
        NB_INDENT -= 4;
    }

    private boolean processCommand(String keyword, List<String> args) throws Exception {
        if (!commands.containsKey(keyword)) {
            System.out.println(getIndent()+"  No such command: " + keyword);
            return true;
        }
        Class<? extends Command<T>> command = commands.get(keyword);
        try {
            Command<T> inst = command.newInstance();
            inst.withShell(this);
            return inst.process(args);
        } catch(InstantiationException|IllegalAccessException e) {
            System.err.println("Unable to instantiate command " + command);
            e.printStackTrace();
            return true;
        }
    }

    @SafeVarargs
    public final void register(Class<? extends Command<T>>... commands) {
        for(Class<? extends Command<T>> command: commands) {
            try {
                String identifier = command.newInstance().identifier();
                if (identifier.contains(" "))
                    throw new IllegalArgumentException("Identifier cannot contain whitespace");
                this.commands.put(identifier, command);
            } catch(InstantiationException|IllegalAccessException|IllegalArgumentException e) {
                System.err.println("Unable to register command " + command);
                e.printStackTrace();
            }
        }
    }

    private Collection<Class<? extends Command<T>>> instructions() {
        return commands.values();
    }

    private void help() {
        List<Class<? extends Command<T>>> avail = new ArrayList<>(instructions());
        avail.sort(Comparator.comparing(Class::getCanonicalName));
        for(Class<? extends Command<T>> c:  avail) {
            try {
                Command<T> instance = c.newInstance();
                System.out.println(getIndent()+"  - " + instance.identifier()+": " + instance.describe());
            }
            catch(InstantiationException|IllegalAccessException e) {
                System.err.println(getIndent()+"Unable to print help for registered command " + c);
                e.printStackTrace();
            }
        }
        if (avail.isEmpty()) System.out.println(getIndent()+"  No command found :'(");
    }

    public String getIndent() {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < Shell.NB_INDENT; i++) { indent.append(Shell.INDENT); }
        return indent.toString();
    }
}

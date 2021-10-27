using System;
using System.IO;
using System.Threading;
using System.ServiceModel;
using System.ServiceModel.Web;

using Partner.Service;

/**
 * Display service.
 */
public class Server {
    /**
     * The web server of the display service.
     */
    private WebServiceHost Host;

    /**
     * The port of the display service.
     */
    private string Port = "9191";

    /**
     * Whether the display server runs in a standalone mode.
     */
    private bool Standalone = false;

    /**
     * The path of the lock file.
     */
    private string Locker = "server.LOCK";

    /**
     * Configures the display server with command line options.
     */
    public void configure(string[] args) {
        // Standalone mode.
        int aloneIdx = Array.FindIndex(args, key => key == "/standalone");
        if (aloneIdx != -1) {
            this.Standalone = true;
        }

        // Server port.
        int portIndex = Array.FindLastIndex(args, key => key == "/port");
        if (portIndex != -1) {
            this.Port = args[portIndex + 1];
        }
    }

    /**
     * Starts the display server.
     */
    public void start() {
        // Initialize the URL.
        Console.WriteLine("Starting the display server... ");
        string url = "http://" + "localhost" + ":" + Port;

        // Configure the host.
        WebHttpBinding binding = new WebHttpBinding();

        // Add the display service.
        Host = new WebServiceHost(typeof(DisplayService), new Uri(url));
        Host.AddServiceEndpoint(typeof(IDisplayService), binding, "");

        // Start the host.
        Host.Open();
        Console.WriteLine("\nListening to " + "localhost" + ":" + Port + "\n");

        // Configure the execution mode.
        if (Standalone) {
            lockServer();
        } else {
            interactive();
        }
    }

    /**
     * Stops the display server.
     */
    private void stop() {
        Host.Close();
        Console.WriteLine("Stopped the display server.");
    }

    /**
     * Starts the display server in interactive mode.
     */
    private void interactive() {
        Console.WriteLine("Hit Return to shutdown the display server.");
        Console.ReadLine();
        stop();
    }

    /**
     * Locks the display server in standalone mode.
     */
    private void lockServer() {
        File.Create(Locker);
        Console.WriteLine("Delete the lock file (" + Locker + ") to stop the display server.");
        watchForLockRemoval();
    }

    /**
     * Polls the file system to check the existence of the lock file.
     */
    private void watchForLockRemoval() {
        var shouldStop = false;
        while (!shouldStop) {
            Thread.Sleep(1000);
            if (!File.Exists(Locker)){
                shouldStop = true;
            }
        }
        stop();
    }

    /**
     * Initializes the display server.
     */
    public static void Main(string[] args) {
        var server = new Server();
        server.configure(args);
        server.start();
    }
}

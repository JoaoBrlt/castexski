using System;
using System.IO;
using System.Threading;
using System.ServiceModel;
using System.ServiceModel.Web;

using Partner.Service;

/**
 * Phone service.
 */
public class Server {
    /**
     * The web service of the phone service.
     */
    private WebServiceHost Host;

    /**
     * The port of the phone service.
     */
    private string Port = "9393";

    /**
     * Whether the phone service runs in a standalone mode.
     */
    private bool Standalone = false;

    /**
     * The path of the lock file.
     */
    private string Locker = "server.LOCK";

    /**
     * Configures the phone service with command line options.
     */
    public void configure(string[] args) {
        // Standalone mode.
        int aloneIdx = Array.FindIndex(args, key => key == "/standalone");
        if (aloneIdx != -1) {
            this.Standalone = true;
        }

        // Service port.
        int portIndex = Array.FindLastIndex(args, key => key == "/port");
        if (portIndex != -1) {
            this.Port = args[portIndex + 1];
        }
    }

    /**
     * Starts the phone service.
     */
    public void start() {
        // Initialize the URL.
        Console.WriteLine("Starting the phone service... ");
        string url = "http://" + "localhost" + ":" + Port;

        // Configure the host.
        WebHttpBinding binding = new WebHttpBinding();

        // Add the phone service.
        Host = new WebServiceHost(typeof(PhoneService), new Uri(url));
        Host.AddServiceEndpoint(typeof(IPhoneService), binding, "");

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
     * Stops the phone service.
     */
    private void stop() {
        Host.Close();
        Console.WriteLine("Stopped the phone service.");
    }

    /**
     * Starts the phone service in interactive mode.
     */
    private void interactive() {
        Console.WriteLine("Hit Return to shutdown the phone service.");
        Console.ReadLine();
        stop();
    }

    /**
     * Locks the phone service in standalone mode.
     */
    private void lockServer() {
        File.Create(Locker);
        Console.WriteLine("Delete the lock file (" + Locker + ") to stop the phone service.");
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
     * Initializes the phone service.
     */
    public static void Main(string[] args) {
        var server = new Server();
        server.configure(args);
        server.start();
    }
}

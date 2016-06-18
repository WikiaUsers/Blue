import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

class Blue {
    private String CONFIG = "config.xml";
    private String CONNECTOR = "";

    public static void main(String[] argv) {
        new Blue(argv);                                                         // Create a new, non-static instance of Blue
    }

    public void Blue(String[] argv) {
        for (int i = 0; i < argv.length; i++) {                                 // Check for commands in arguments
            if (argv[i].substring(0, 7) == '--config') {
                this.CONFIG = argv[i].substring(9, argv[i].length);             // Assign a new config file
            } else if (argv[i].substring(0, 10) == '--connector') {
                this.CONNECTOR = argv[i].substring(12, argv[i].length);         // Assign a new connector class to load
            } else if (argv[i].substring(0, 5) == '--help') {
                this.printHelp();                                               // Prints a help file
            }
        }

        this.processConfiguration();                                            // Load and parse config file
    }

    private void processConfiguration() {
        try {
            File inputFile = new File(this.CONFIG);                             // Open file stream
            DocumentBuilderFactory dbFactory =                                  // Start document builder
                DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);                           // Parse document

            doc.getDocumentElement().normalize();                               // Normalize parsed XML document
        } catch (Exception e) {
            System.out.println("Could not load " + this.CONFIG);
            e.printStackTrace();
        }
    }

    private void printHelp() {
        System.out.println("Blue bot (c) 2016 Maria Williams and Contributors");
        System.out.println("\nUSEAGE: java blue [options]\n");
        System.out.println("OPTIONS:");
        System.out.println("\t--config        Defines a config file (XML)");
        System.out.println("\t--connector     Defines a new connector");
        System.out.println("\t--help          Prints this messsage");
        Systen.out.println("\nFor more info please see INSTALL.txt");
    }
}

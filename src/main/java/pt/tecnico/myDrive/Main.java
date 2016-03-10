package pt.tecnico.myDrive;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import java.io.IOException;

public class Main {
    static final Logger log = LogManager.getRootLogger();

    public static void main(String[] args) throws IOException {
        log.info("Hello World!");
        try {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FenixFramework.shutdown();
        }
    }

    @Atomic
    public static void setup() {

    }
}

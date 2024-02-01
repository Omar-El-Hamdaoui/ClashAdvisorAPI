package moviesapp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.File;
import java.io.IOException;



@Command(name = "appcli", mixinStandardHelpOptions = true, version = "1.0")
public class AppCLI implements Runnable {
    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine(new AppCLI());
        commandLine.addSubcommand("list", new ListMoviesCommand());
        commandLine.execute(args);
    }

    @Override
    public void run() {

        // La logique principale de votre application va ici
        System.out.println("Welcome to the movies app");
    }
}

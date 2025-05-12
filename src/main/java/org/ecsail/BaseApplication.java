package org.ecsail;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ecsail.mvci_main.MainController;
import org.ecsail.static_tools.VersionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import static org.ecsail.static_tools.HalyardPaths.LOGFILEDIR;


public class BaseApplication extends Application {

    public static Stage primaryStage;
    public static File outputFile;
    public static Logger logger = LoggerFactory.getLogger(BaseApplication.class);

    public static boolean testMode = false;

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("test")) {
            testMode = true;
        }
        launch(args);
    }

    private static void startFileLogger() {
        try {
            outputFile = File.createTempFile("debug", ".log", new File(LOGFILEDIR));
            PrintStream output = new PrintStream(new BufferedOutputStream(new FileOutputStream(outputFile)), true);
            System.setOut(output);
            System.setErr(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        if (!testMode)
            startFileLogger();
        else
            logger.info("Halyard: Running test mode");
        VersionUtil.logAppVersion();
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setScene(new Scene(new MainController().getView()));
        primaryStage.getScene().getStylesheets().addAll(
                "css/dark/dark.css",
                "css/dark/tabpane.css",
                "css/dark/tableview.css",
                "css/dark/chart.css",
                "css/dark/bod.css",
                "css/dark/table_changes.css",
                "css/dark/invoice.css");
        primaryStage.show();
    }
}

/*
I have a JavaFX application, that is a productivity app. I want the JavaFX app to connect to a spring boot app, which will
act as a REST server to provide the data.  I already have the spring boot server up and running and I will provide the security configuration
below.  At this point I would like to talk about what the options are before you start generating code. The app will have a login in dialogue
with a username, password and server.  I believe that I want the app to first send a message to the server to ask if it needs to log on before
displaying the dialoge.  If it doesn't need to log on then I expect it to just work with no indication to the user.  If it does need to log
on then I want the login dialogue to pop up so that the user can log on. below is my security configureation.  Again ask me questions that you need
to know so we can discuss it.  There is no need to generate code yet
 */

/*
Do you prefer adding a new /auth-check endpoint (Option 1), using an existing protected endpoint (Option 2), or leveraging the
/login endpoint (Option 3)? Or do you have another idea in mind?
I think I prefer option 1.  I don't mind an endpoint and it seems like the most simple and reliable choice
*/

/*
The login dialog includes a "server" field. Does this mean the user can specify any server URL (e.g., http://example.com),
or will there be a predefined list of servers? How should the JavaFX app store and validate this URL?
I will take care of this.  The app will store 1 or more logins so the user can connect to other servers if need be. In reality,
I will most likely have a second login for a test server
 */


/*
After a successful login, do you want the JavaFX app to persist the session (e.g., save the JSESSIONID or remember-me token) so
the user doesn’t need to log in again on the next app launch? If so, how long should this persist (e.g., until the app closes, or longer)?
Yes, I would like the app to persist the session, if the user closes the app then they would need to log in again.  If the user keeps the
app open then it persists as long as the app is open.
 */

/*
The server enforces a maximum of 3 sessions per user. If the user tries to log in but has reached this limit, they’ll be redirected to
/login?expired. How should the JavaFX app handle this? Should it notify the user and ask them to log out from other devices, or take
another action?
Yes the app should have a dialoug that pops up and informs the user, and gives them options
*/

/*
The app will likely make requests to endpoints like /rb_roster/**, which require ROLE_ADMIN or ROLE_MEMBERSHIP. If the user
logs in but lacks the required role (resulting in a 403 Forbidden), how should the JavaFX app handle this? Should it show an
error message, or redirect back to the login dialog?
It will pop up a dialoge to inform the user they don't have the correct access. Other than a couple of open enpoints for
logging in and determining status, all outer requests will go to a common branch of endponts


/*
The remember-me cookie is set to useSecureCookie(true), meaning it requires HTTPS. Is your Spring Boot server always
running over HTTPS, or might it run over HTTP during development? If HTTP is used, the cookie won’t be sent, which could
 affect the authentication flow.
 During developement and testing I will be running the test spring boot application on a computer in my home
 network so will be using HTTP for testing.  HTTPS will be what is used in production
 */

/*
Question: Would you like to add a /logout-others endpoint to allow the user to invalidate other sessions, or should
the dialog only inform the user without providing an actionable solution?
Allow the user to invalidate other sessions
 */

/*
The server supports remember-me, but we haven’t discussed including a “Remember Me” checkbox in the login dialog. Since
you want the session to persist only while the app is open, it seems like remember-me isn’t needed for this use case.
However, do you want to include a “Remember Me” option for future flexibility (e.g., to persist the session
across app restarts)? If not, we can ignore remember-me entirely.
There is no need to remember between app restarts, so let's ignore this
 */

/*
You mentioned that most requests will go through a “common branch of endpoints.” Can you confirm which endpoints the JavaFX
app will primarily interact with after login (e.g., /rb_roster/**)? This will help ensure we handle role-based access errors appropriately.

The branch name will be /halyard/**  which is the name of the app
 */

/*
There is one more matter to discuss.  I am heavily modifying the app. The initial design was using ssh to port forward
the database. This worked well, except inactivity would close the port and give non-desirable user experience.

Below is the list of my current dependencies and what I assume is the best way to use or get rid of them

    // we will keep all the logging libraries
   // SLF4J and Logback for logging
    implementation("org.slf4j:slf4j-api:2.0.5")
    implementation("ch.qos.logback:logback-classic:1.4.12")
    implementation("ch.qos.logback:logback-core:1.4.14")

    // Log4J bridge to SLF4J
    implementation("org.apache.logging.log4j:log4j-to-slf4j:2.20.0")
    implementation("org.slf4j:jcl-over-slf4j:2.0.5")

    // this should no longer be needed
    // MariaDB JDBC Driver
    implementation("org.mariadb.jdbc:mariadb-java-client:3.2.0")

    // this should no longer be needed
    // Spring JDBC
    implementation("org.springframework:spring-jdbc:6.1.7")

    // I definatly want to keep Jackson so this is not needed
    // JSON library
    implementation("org.json:json:20240303")

    // I am not sure I like this one, I think OKHttp may be better but I am willing to change my mind with a good argument
    // Apache HTTP Client
    implementation("org.apache.httpcomponents:httpcore:4.4.15")
    implementation("org.apache.httpcomponents:httpclient:4.5.14")

    // this will no longer be needed
    // JSch for SSH connections
    implementation("com.github.mwiede:jsch:0.2.7")

    // I think I will keep this
    // OkHttp for HTTP requests
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.14")

    // I certainly want to keep this because of its powerful ability to go from JSON to POJO and back again
    // Jackson for JSON processing
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.2")

    Let me know what you think of this and please off any suggestions.  Then we can begin to generate code
 */
package ezschedule;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Logger;

import ezschedule.commons.core.Config;
import ezschedule.commons.core.LogsCenter;
import ezschedule.commons.core.Version;
import ezschedule.commons.exceptions.DataConversionException;
import ezschedule.commons.util.ConfigUtil;
import ezschedule.commons.util.StringUtil;
import ezschedule.logic.Logic;
import ezschedule.logic.LogicManager;
import ezschedule.logic.commands.CommandResult;
import ezschedule.logic.parser.Parser;
import ezschedule.logic.parser.exceptions.ParseException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Runs the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(0, 2, 0, true);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    // TODO: also declare Ui, Logic, Storage, etc
    protected Config config;

    protected Logic logic;

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing ]===========================");
        super.init();

        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());

        // TODO: initialise Ui, Logic, Storage, etc
        logic = new LogicManager();

    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead
     * if {@code configFilePath} is null.
     */
    protected Config initConfig(Path configFilePath) {
        Config initializedConfig;
        Path configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. "
                + "Using default config properties");
            initializedConfig = new Config();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    @Override
    public void start(Stage primaryStage) throws ParseException {
        logger.info("Starting Ez-Schedule " + MainApp.VERSION);

        // TODO: Start to run UI of app
        // ui.start(primaryStage);

        // TODO: Temporary code to allow exiting
        String input;
        Scanner sc = new Scanner(System.in);
        do {
            input = sc.nextLine();

            // TODO: Found in MainWindow.java executeCommand method
            CommandResult cr = logic.execute(input);
            logger.info("Result: " + cr.getFeedbackToUser());
            // System.out.println("To be implemented. Type 'exit' to stop. You typed: " + input);
        } while (input.compareTo("exit") != 0);
        Platform.exit();
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping ] =============================");
        // TODO: do saving before closing
    }
}

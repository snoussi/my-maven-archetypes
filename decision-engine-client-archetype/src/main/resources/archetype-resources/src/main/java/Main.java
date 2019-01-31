#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.kie.api.KieBase;
import org.kie.api.KieServices;

import org.kie.api.command.KieCommands;
import org.kie.api.builder.ReleaseId;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.command.Command;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.command.CommandFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static final String GROUPID = "${groupId}";
    public static final String ARTIFACTID = "your-rules";
    public static final String VERSION = "1.0-SNAPSHOT"; // "LATEST" (non SNAPSHOT version) Can be used here
    // public static final String STATELESS_KIE_SESSION_ID = "default-stateless-ksession";

    public static void main(String[] args) {

        /*
         * Create a KieContainer of an existing project by ReleaseId
         */
        KieServices kieServices = KieServices.Factory.get();
        ReleaseId releaseId = kieServices.newReleaseId( GROUPID, ARTIFACTID, VERSION );
        KieContainer kieContainer = kieServices.newKieContainer( releaseId );

        /*
         * Retrive KieBases and KieSessions from the KieContainer
         * NB: Creating a KieBase is a relatively heavy operation (tightly related to the number of rules to load)
         */
        KieBase kieBase = kieContainer.getKieBase();
        KieSession kieSession = kieBase.newKieSession();

        /*
         * Insert Facts and Globals
         */
        // kieSession.setGlobal("ruleLoggerEnabled", true);
        // kieSession.insert(getTransaction());

        /*
         * Fire rules and retrive results
         */
        int nbFiredRules = kieSession.fireAllRules();
        LOGGER.info(nbFiredRules+" Rules Fired");

        // Collection<?> transactions = kieSession.getObjects(new ClassObjectFilter(Transaction.class));

        // for (Object transaction : transactions) {
        //     LOGGER.info(transaction.toString());
        // }

        // Collection<?> alerts = kieSession.getObjects(new ClassObjectFilter(Alert.class));

        // for (Object alert : alerts) {
        //     LOGGER.info(alert.toString());
        // }

        kieSession.dispose();

        /*
         * Other code snippets using commands 
         */

       // KieContainer kieContainer = kieServices.getKieClasspathContainer();
       // KieBase kieBase = kieContainer.getKieBase();
       // StatelessKieSession statelessKieSession = kieBase.newStatelessKieSession();

       // statelessKieSession.setGlobal("ruleLoggerEnabled",true);
       // statelessKieSession.execute(CommandFactory.newInsert(getTransaction1()));
       // statelessKieSession.execute(CommandFactory.newInsertElements(new ArrayList<>(Arrays.asList(getTransaction1(),getTransaction2()))));

       // List<Command<?>> commands = new ArrayList<>();
       // commands.add(CommandFactory.newInsert(getTransaction1()));
       // commands.add(CommandFactory.newInsert(getTransaction2()));
       // commands.add(CommandFactory.newSetGlobal("ruleLoggerEnabled", true));
       // commands.add(CommandFactory.newFireAllRules());
       // statelessKieSession.execute();
       // BatchExecutionCommand batchExecutionCommand = CommandFactory.newBatchExecution(commands,STATELESS_KIE_SESSION_ID);

       // KieCommands kieCommands = kieServices.getCommands();
       // List<Command> commands = new ArrayList<Command>();
       // commands.add( kieCommands.newInsert( getTransaction1() ) );
       // commands.add(CommandFactory.newInsert(getTransaction2()));
       // commands.add(CommandFactory.newSetGlobal("ruleLoggerEnabled", true));
       // statelessKieSession.execute( kieCommands.newBatchExecution( commands,STATELESS_KIE_SESSION_ID ) );

    }



    // private static Transaction getTransaction() {
    //     Transaction txn = new Transaction();
    //     txn.setAccountNumber("xxxxxxx");
    //     txn.setBeneficiaryBank("XXXX");
    //     txn.setOriginalAmount(new BigDecimal(531.97));
    //     try {
    //         SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");
    //         txn.setPartitionDate(dateFormat.parse("06/03/2018"));
    //     }catch (ParseException exc) {
    //         exc.printStackTrace();
    //     }
    //     txn.setReference("xxxxxxx");
    //     txn.setSender("XXXXXX");
    //     return txn;
    // }

}
#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.marshalling.Marshaller;
import org.kie.server.api.marshalling.MarshallerFactory;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.api.model.KieServiceResponse;
import org.kie.server.client.CredentialsProvider;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.RuleServicesClient;
import org.kie.server.client.credentials.EnteredCredentialsProvider;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static final String KIE_SERVER_URL = "http://localhost:8080/kie-server/services/rest/server";

    private static final String USERNAME = "dmAdmin";

    private static final String PASSWORD = "dmAdmin1!";

    private static final String STATELESS_KIE_SESSION_ID = "default-stateless-ksession";

    // We use the container 'alias' instead of container name to decouple the client from the KIE-Contianer deployments.
    private static final String CONTAINER_ID = "xxxxx-xxxxx";

    public static void main(String[] args) {



        KieServices kieServices = KieServices.Factory.get();

        CredentialsProvider credentialsProvider = new EnteredCredentialsProvider(USERNAME, PASSWORD);

        KieServicesConfiguration kieServicesConfig = KieServicesFactory.newRestConfiguration(KIE_SERVER_URL, credentialsProvider);

        // Set the Marshaling Format to JSON. Other options are JAXB and XSTREAM
        kieServicesConfig.setMarshallingFormat(MarshallingFormat.JSON);

        KieServicesClient kieServicesClient = KieServicesFactory.newKieServicesClient(kieServicesConfig);

        // Retrieve the RuleServices Client.
        RuleServicesClient rulesClient = kieServicesClient.getServicesClient(RuleServicesClient.class);

        /*
         * Create the list of commands that we want to fire against the rule engine.
         */
        List<Command<?>> commands = new ArrayList<>();

        KieCommands commandFactory = kieServices.getCommands();
        //The identifiers that we provide in the insert commands can later be used to retrieve the object from the response.
        // commands.add(commandFactory.newInsert(getTransaction(), "txn"));

        // commands.add(commandFactory.newSetGlobal("ruleLoggerEnabled", true));
        commands.add(commandFactory.newFireAllRules());

        /*
         * The BatchExecutionCommand contains all the commands we want to execute in the rules session, as well as the identifier of the
         * session we want to use.
         */
        BatchExecutionCommand batchExecutionCommand = commandFactory.newBatchExecution(commands, STATELESS_KIE_SESSION_ID);

        LOGGER.info(batchExecutionCommand.toString());

        ServiceResponse<ExecutionResults> response = rulesClient.executeCommandsWithResults(CONTAINER_ID, batchExecutionCommand);

        if (response.getType() == KieServiceResponse.ResponseType.SUCCESS) {
            LOGGER.info("Commands executed with success! Response: ");
            ExecutionResults results = response.getResult();

            Marshaller marshaller = MarshallerFactory.getMarshaller(kieServicesConfig.getExtraClasses(), kieServicesConfig.getMarshallingFormat(),
                    Thread.currentThread().getContextClassLoader());
            String json = marshaller.marshall(results);
            LOGGER.info("============================");
            LOGGER.info(json);
            LOGGER.info("============================");

            //We can retrieve the objects from the response using the identifiers we specified in the Insert commands.
         // Collection<String> identifiers = results.getIdentifiers();
         // for (String identifier : identifiers) {
         //     Object fact = results.getValue(identifier);
         //       LOGGER.info(identifier+" : "+fact);
         // }

         //   Object _all = results.getValue("all");
         //   LOGGER.info("all facts in WM : "+_all);

        } else {
            LOGGER.info("Error executing rules. Message: ");
            LOGGER.info(response.getMsg());
        }

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
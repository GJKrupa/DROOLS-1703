package uk.me.krupa.drools_1703;

import org.apache.commons.io.FileUtils;
import org.drools.core.ObjectFilter;
import org.drools.core.RuleBaseConfiguration;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.io.ResourceFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RuleFiredTest {

    public static final String RULES_FILE = "uk/me/krupa/drools_1703/GenericRule.drl";

    @Test
    public void ruleFired(){
        KieSession kieSession = sessionFromRulesFile(RULES_FILE);
        DummyCondition dummyCondition = new DummyCondition();
        kieSession.insert(dummyCondition);
        kieSession.fireAllRules();
        final Collection<Thingy> events = collectResults(kieSession);
        kieSession.dispose();
        assertTrue(events.size()==1);
        final List<Thingy> eventsList = new ArrayList<Thingy>(){{addAll(events);}};
        assertEquals("001", eventsList.get(0).getName());
    }

    private Collection<Thingy> collectResults(final KieSession kieSession) {
        return (Collection<Thingy>) kieSession.getObjects((ObjectFilter) object -> object instanceof Thingy);
    }

    private KieSession sessionFromRulesFile(String path) {
        KieServices kieServices = KieServices.Factory.get();

        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        URL resourceUrl = Thread.currentThread().getContextClassLoader().getResource(path);
        kieFileSystem.write(ResourceFactory.newFileResource(FileUtils.toFile(resourceUrl)));

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
            throw new IllegalStateException("Build Errors:\n" + kieBuilder.getResults().toString());
        }
        KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        KieBase kieBase = kieContainer.newKieBase(KnowledgeBaseFactory.newKnowledgeBaseConfiguration(null, RuleBaseConfiguration.class.getClassLoader()));

        return kieBase.newKieSession();
    }

}

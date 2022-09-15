package h.g.g.g.javaagent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

public class JavaAgentTest {
    static private Instrumentation _inst = null;
    public static void premain(String agentArgs, Instrumentation inst) {
//        Param.generatePARAMS(agentArgs);
        System.out.println("AopAgentTest.premain() was called.");

        /* Provides services that allow Java programming language agents to instrument programs running on the JVM.*/
        _inst = inst;

        /* ClassFileTransformer : An agent provides an implementation of this interface in order to transform class files.*/
        ClassFileTransformer trans = new AopAgentTransformer();
        /*Registers the supplied transformer.*/
        _inst.addTransformer(trans);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.eiafr.cojac.unit.replace;

import ch.eiafr.cojac.Agent;
import ch.eiafr.cojac.Arg;
import ch.eiafr.cojac.Args;
import ch.eiafr.cojac.CojacReferences;
import ch.eiafr.cojac.unit.AgentTest;
import static ch.eiafr.cojac.unit.AgentTest.instrumentation;
import java.lang.instrument.ClassFileTransformer;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import org.junit.Test;


/**
 *
 * @author romain
 */
public class FloatProxyLauncherTest {
	
	protected AgentTest dummyAgentTest=new AgentTest(); // just to ensure AgentTest is loaded

    Class<?> floatProxyTest;
    
	public FloatProxyLauncherTest() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        super();
        loadOperationsWithAgent(getClassFileTransformer());
    }

    protected ClassFileTransformer getClassFileTransformer() {
        Args args = new Args();
        args.specify(Arg.ALL);
        args.specify(Arg.EXCEPTION);
        args.specify(Arg.REPLACE_FLOATS);
        args.specify(Arg.INSTRUMENTATION_STATS);

        CojacReferences.CojacReferencesBuilder builder = new CojacReferences.CojacReferencesBuilder(args);
        builder.setSplitter(new CojacReferences.AgentSplitter());

        return new Agent(builder.build());
    }

    //TODO: review the test approach, so that it automatically instruments dependent classes
    public void loadOperationsWithAgent(ClassFileTransformer classFileTransformer) 
	         throws ClassNotFoundException {
	    instrumentation.addTransformer(classFileTransformer, true);
	    try {
	        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
	        classLoader.loadClass("ch.eiafr.cojac.unit.replace.FloatProxyNotInstrumented");
	        floatProxyTest = classLoader.loadClass("ch.eiafr.cojac.unit.replace.FloatProxy");
	    } finally {
	        instrumentation.removeTransformer(classFileTransformer);
	    }
	}
    
    
	
	@Test
    public void staticFieldDoubleAccess() throws Exception {
        invokeMethod("staticFieldDoubleAccess");
    }
	
	@Test
    public void staticFieldFloatAccess() throws Exception {
        invokeMethod("staticFieldFloatAccess");
    }
	
	
	private void invokeMethod(String methodName) throws Exception{
		if (floatProxyTest==null) return;
        Method m = floatProxyTest.getMethod(methodName);
        if (m==null) return;
        m.invoke(null);
	}
}

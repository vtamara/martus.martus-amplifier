package org.martus.amplifier.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.martus.amplifier.service.attachment.TestAllAttachment;
import org.martus.amplifier.service.search.TestAllSearch;
import org.martus.amplifier.service.datasynch.test.TestAllDataSynch;
import org.martus.amplifier.presentation.test.TestAllPresentation;

public class TestAllAmplifier extends TestSuite
{
    public TestAllAmplifier() 
    {
    }

	public static void main (String[] args) 
	{
		runTests();
	}

	public static void runTests () 
	{
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite ( ) 
	{
		TestSuite suite= new TestSuite("All Martus Amplifier Tests");
		
		// example of a test suite
		suite.addTest(TestAllAttachment.suite());
		suite.addTest(TestAllSearch.suite());
		suite.addTest(TestAllDataSynch.suite());
		suite.addTest(TestAllPresentation.suite());
		
		suite.addTest(new TestSuite(TestMartusAmplifier.class));
		suite.addTest(new TestSuite(TestAmplifierConfiguration.class));
		
	    return suite;
	}
}

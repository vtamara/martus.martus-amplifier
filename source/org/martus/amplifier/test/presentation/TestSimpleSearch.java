package org.martus.amplifier.test.presentation;

import java.util.Vector;

import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.context.Context;
import org.martus.amplifier.presentation.SimpleSearch;
import org.martus.common.test.TestCaseEnhanced;

public class TestSimpleSearch extends TestCaseEnhanced
{
	public TestSimpleSearch(String name)
	{
		super(name);
	}

	public void testBasics()
	{
		MockAmplifierRequest request = new MockAmplifierRequest();
		HttpServletResponse response = null;
		Context context = new MockContext();
		
		String sampleQuery = "this is what the user is searching for";
		request.putParameter("query", sampleQuery);
		assertEquals(sampleQuery, request.getParameter("query"));
		
		SimpleSearch ss = new SimpleSearch();
		String templateName = ss.selectTemplate(request, response, context);
		assertEquals("SimpleSearch.vm", templateName);
		
		assertEquals(sampleQuery, context.get("name"));
		
		Vector fields = (Vector)context.get("theFields");
		assertEquals(8, fields.size());
		assertContains("Title", fields);
	}
}

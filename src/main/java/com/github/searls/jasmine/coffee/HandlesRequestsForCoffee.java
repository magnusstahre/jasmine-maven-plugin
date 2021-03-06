package com.github.searls.jasmine.coffee;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.http.HttpHeaders;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.util.resource.Resource;

import com.github.searls.jasmine.format.BuildsJavaScriptToWriteFailureHtml;

public class HandlesRequestsForCoffee {

	private CoffeeScript coffeeScript = new CoffeeScript();
	private BuildsJavaScriptToWriteFailureHtml buildsJavaScriptToWriteFailureHtml = new BuildsJavaScriptToWriteFailureHtml(); 
	
	public void handle(Request baseRequest, HttpServletResponse response, Resource resource) throws IOException {
		baseRequest.setHandled(true);
		String javascript = compileCoffee(resource);
		setHeaders(response, resource, javascript);
		writeResponse(response, javascript);
	}

	private void writeResponse(HttpServletResponse response, String javascript) throws IOException {
		response.getWriter().write(javascript);
	}

	private void setHeaders(HttpServletResponse response, Resource resource, String javascript) {
		response.setContentType("text/javascript");
		response.setDateHeader(HttpHeaders.LAST_MODIFIED, resource.lastModified());
		response.setHeader(HttpHeaders.CONTENT_LENGTH,Integer.toString(javascript.length()));
	}

	private String compileCoffee(Resource resource) {
		try {
			return coffeeScript.compile(IOUtils.toString(resource.getInputStream()));
		} catch(Exception e) {
			return buildsJavaScriptToWriteFailureHtml.build("CoffeeScript Error: failed to compile <code>"+resource.getName()+"</code>. <br/>Error message:<br/><br/><code>"+e.getMessage()+"</code>");
		}
	}

}

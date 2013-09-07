package org.virtualrepository.service.rest.filters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.ext.Provider;

import com.sun.jersey.core.header.InBoundHeaders;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

@Provider
public class UriAdapter implements ContainerRequestFilter {

	private static List<String> headersAsParams = Arrays.asList(ContainerRequest.ACCEPT);
	
	@Override
	public ContainerRequest filter(ContainerRequest request) {
		
		Map<String,List<String>> params = request.getQueryParameters();
		Map<String,List<String>> matchingParams = new HashMap<String,List<String>>();
		
		for (String p : headersAsParams)
			if (params.containsKey(p))
				matchingParams.put(p,params.get(p));
		
		if (!matchingParams.isEmpty()) {
			
			//clone headers
			InBoundHeaders headers = new InBoundHeaders();
			headers.putAll(request.getRequestHeaders());
			
			//add params as headers
			for (Entry<String,List<String>> param :matchingParams.entrySet())
				for (String value : matchingParams.get(param.getKey()))
					headers.add(param.getKey(),value);
			
			request.setHeaders(headers);
			
			
		}
		
		return request;
	}
}

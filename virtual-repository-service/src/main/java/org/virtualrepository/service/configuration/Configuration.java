package org.virtualrepository.service.configuration;

import static org.virtualrepository.service.Constants.*;
import static org.virtualrepository.service.utils.Utils.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.AssetType;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.impl.Services;
import org.virtualrepository.service.Constants;

@Singleton
public class Configuration {

	private static final Logger log = LoggerFactory.getLogger(Configuration.class);
	
	private String name;
	private String version;
	private int ttl = default_ttl;
	
	private final VirtualRepository repository;
	
	@Inject
	public Configuration(VirtualRepository repository) {
		this.repository=repository;
	}
	
	@PostConstruct
	public void init() {
		
		try {
			
			Properties properties=loadProperties("/"+config_external_file);
			
			for (Map.Entry<Object,Object> p : loadProperties("/"+config_internal_file).entrySet())
				properties.put(p.getKey(),p.getValue());
			
			List<String> errors = errorsIn(properties); 
			
			if (!errors.isEmpty())
				throw new RuntimeException("invalid configuration: "+errors);
			
			//static properties
			name= properties.getProperty(config_endpoint_name);

			if (properties.containsKey(config_ttl_name))
				ttl = Integer.valueOf(properties.getProperty(config_ttl_name));
			
			version = properties.getProperty(config_virtual_repository);
			
			log.info("initialised configuration "+this);
			
			
		}
		catch(Exception e) {
			rethrow("cannot configure service (see details)",e);
		}
		
		
	}
	
	public String version() {
		return version;
	}
	
	public String name() {
		return name;
	}
	
	
	public int responseTTL() {
		
		return ttl;
	}
	
	public AssetType[] assetTypes() {
		
		//this has to be dynamic to reflect dynamic service additions (including from tests)
		Collection<AssetType> types = new HashSet<AssetType>();
		
		for(RepositoryService service : repository.services())
			for(AssetType type : service.returnedTypes())
				types.add(type);
		
		return types.toArray(new AssetType[0]);
	}
	
	public Services repositories() {
		
		return repository.services();
	}
	
	
	//helpers
	
	
	private Properties loadProperties(String location) throws Exception {
		
		InputStream configStream = getClass().getResourceAsStream(location);
		if (configStream==null)
			throw new RuntimeException("missing configuration file "+Constants.config_internal_file);
		
		Reader configReader = new InputStreamReader(configStream, Charset.forName("UTF-8"));
		
		Properties properties = new Properties();
		properties.load(configReader);
		return properties;
	}
	
	private List<String> errorsIn(Properties properties) {
		
		String missing = "missing required property ";
		String invalid = "invalid property ";
		
		List<String> errors = new ArrayList<String>();
		
		if (!properties.containsKey(config_endpoint_name) )
			errors.add(missing+config_endpoint_name);
		
		if (!properties.containsKey(config_virtual_repository) )
			errors.add(missing+config_virtual_repository);
		
		if (properties.containsKey(config_ttl_name) && !isNumber((String)properties.get(config_ttl_name)))
			errors.add(invalid+config_ttl_name);
		
		return errors;
	}
	
	private boolean isNumber(String s) {
		try {
			Integer.valueOf(s);
			return true;
		}
		catch(NumberFormatException e) {
			return false;
		}
	}

	@Override
	public String toString() {
		return "Configuration [name=" + name + ", version=" + version + ", ttl=" + ttl + "]";
	}
}

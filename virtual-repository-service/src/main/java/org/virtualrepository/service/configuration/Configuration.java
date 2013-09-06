package org.virtualrepository.service.configuration;

import static org.virtualrepository.service.Constants.*;
import static org.virtualrepository.service.utils.Utils.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashSet;
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
import org.virtualrepository.service.Constants;

@Singleton
public class Configuration {

	private static final Logger log = LoggerFactory.getLogger(Configuration.class);
	
	private final Properties properties;
	private final VirtualRepository repository;
	
	@Inject
	public Configuration(VirtualRepository repository) {
		this.properties = new Properties();
		this.repository=repository;
	}
	
	@PostConstruct
	public void init() {
		
		try {
			
			loadProperties();
			
			if (!areValidProperties())
				throw new RuntimeException("missing required properties or invalid properties in "+properties.keySet());
			
			addDerivedProperties();
			
			log.info("initialised configuration "+this);
			
			
		}
		catch(Exception e) {
			rethrow("cannot configure service (see details)",e);
		}
		
		
	}
	
	
	public int responseTTL() {
		
		return (Integer) properties.get(config_ttl_name);
	}
	
	public AssetType[] assetTypes() {
		
		//this has to be dynamic to reflect dynamic service additions (including from tests)
		Collection<AssetType> types = new HashSet<AssetType>();
		
		for(RepositoryService service : repository.services())
			for(AssetType type : service.returnedTypes())
				types.add(type);
		
		return types.toArray(new AssetType[0]);
	}
	
	
	public Properties properties() {
		return properties;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for (Map.Entry<Object,Object> e : properties.entrySet())
			builder.append(e.getKey()+"="+e.getValue()+" ");
		builder.append("]");
		return builder.toString();
	}
	
	
	//helpers
	
	
	private void loadProperties() throws Exception {
		
		InputStream configStream = getClass().getResourceAsStream("/"+configFile);
		if (configStream==null)
			throw new RuntimeException("missing configuration file "+Constants.configFile);
		
		Reader configReader = new InputStreamReader(configStream, Charset.forName("UTF-8"));
		
		this.properties.load(configReader);
	}
	
	private boolean areValidProperties() {
		
		return 
			properties.containsKey(config_endpoint_name) &&
			properties.containsKey(config_virtual_repository) && 
			(!properties.containsKey(config_ttl_name) ||
				isNumber((String)properties.get(config_ttl_name))
			);
	}
	
	private void addDerivedProperties() {
		
		Collection<String> names = new HashSet<String>();
		
		for(AssetType type : assetTypes())
			names.add(type.name());
		
		properties.put(configTypesProperty,names);
		
		if (!properties.containsKey(config_ttl_name))
			properties.put(config_ttl_name, default_ttl_);
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
}

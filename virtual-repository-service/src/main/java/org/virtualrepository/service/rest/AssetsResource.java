/**
 * (c) 2013 FAO / UN (project: virtual-repository-service)
 */
package org.virtualrepository.service.rest;

import static javax.ws.rs.core.MediaType.*;
import static javax.ws.rs.core.Response.*;
import static org.virtualrepository.service.Constants.*;
import static org.virtualrepository.service.rest.AssetsResource.*;
import static org.virtualrepository.service.utils.Utils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.Asset;
import org.virtualrepository.AssetType;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.service.configuration.Configuration;
import org.virtualrepository.service.io.Binder;
import org.virtualrepository.service.utils.Utils;

/**
 * Discovers available assets.
 * 
 * @author Fabio Fiorellato
 * @author Fabio Simeoni
 *
 */
@Path(path)
@Singleton
public class AssetsResource {
	
	private static Logger log = LoggerFactory.getLogger(AssetsResource.class);
	
	public static final String path = "/assets";
	public static final String typeParam = "type";
	
	private final VirtualRepository repository;
	private final Configuration configuration;
	private final Binder binder;
	
	private Map<AssetType, Collection<Asset>> index;

	@Inject
	public AssetsResource(VirtualRepository repository, Configuration configuration, Binder binder) {
		
		this.repository=repository;
		this.configuration=configuration;
		this.binder=binder;
		this.index = new HashMap<AssetType, Collection<Asset>>();
		
		
	}
	
	@PostConstruct
	public void init() {
		refresh();
	}
	
	
	@GET
	@Produces(APPLICATION_JSON)
	public Response getInJson(@Context UriInfo info) {
		
		Collection<AssetType> types = typesFrom(info);
		
		Collection<Asset> assets = assetsFor(types);
		
		String outcome = binder.jsonMoM(assets);
		
		log.info("returning metadata in Json about {} assets of types: {}", assets.size(),types);
		
		return ok(outcome).build();
		
	}
	
	@POST
	@Produces(APPLICATION_JSON)
	public Response refreshAndGetInJson(@Context UriInfo info) {
		
		refresh(typesFrom(info));
		
		return getInJson(info);
	}
	
	@GET
	@Produces(APPLICATION_XML)
	public Response getInXml(@Context UriInfo info) {
		
		Collection<AssetType> types = typesFrom(info);
		
		Collection<Asset> assets = assetsFor(types);
		
		String outcome = binder.xmlMoM(assets);
		
		log.info("returning metadata in XML about {} assets of types: {}", assets.size(),types);
		
		return ok(outcome).build();
	
	}
	
	@POST
	@Produces(APPLICATION_XML)
	public Response refreshAndGetInXml(@Context UriInfo info) {
		
		refresh(typesFrom(info));
		
		return getInXml(info);
	}
	
	@GET
	@Produces(APPLICATION_VXML)
	public Response getinVXml(@Context UriInfo info) {
		
		Collection<AssetType> types = typesFrom(info);
		
		Collection<Asset> assets = assetsFor(types);
		
		String outcome = binder.vxml(assets);
		
		log.info("returning metadata in vXML about {} assets of types: {}", assets.size(),types);
		
		return ok(outcome).build();
	}
	
	@POST
	@Produces(APPLICATION_VXML)
	public Response refreshAndGetInVXml(@Context UriInfo info) {
		
		refresh(typesFrom(info));
		
		return getinVXml(info);
	}
	
	
	//helpers
	Collection<AssetType> typesFrom(UriInfo info) {
	
		List<AssetType> types = new ArrayList<AssetType>();

		List<String> typeNames = info.getQueryParameters().get(typeParam);
		
		AssetType[] knownTypes = configuration.assetTypes();
		
		if (typeNames == null || typeNames.isEmpty())
			types = Arrays.asList(knownTypes);
		else
			for(String name : typeNames)
				types.add(typeWith(knownTypes, name));
		
		return types;
	}
	
	private Collection<Asset> assetsFor(Collection<AssetType> types) {
		
		Collection<Asset> assets = new ArrayList<Asset>();

		for(AssetType type : types) {
			Collection<Asset> requestedAssets = index.get(type);
			if(requestedAssets != null)
				assets.addAll(requestedAssets);
		}

		return assets;
	}
	
	
	
	private void refresh(Collection<AssetType> types) {
		refresh(types.toArray(new AssetType[0]));
	}
	
	private void refresh(AssetType ... types) {
		
		if (types.length==0)
			types = configuration.assetTypes();
		
		try {
			
			log.info("refreshing asset metadata for types {},types");
			
			repository.discover(types);
			
			buildIndex(types);
			
		}
		catch(Exception e) {
			Utils.rethrow("could not refresh assets (see cause)", e);
		}
	}

	private void clearIndex(AssetType ... types) {
		
		for(AssetType type : types)
			this.index.remove(type);
	}
	
	private void buildIndex(AssetType ... types) {
		
		clearIndex(types);
		
		Collection<Asset> assetsByType;
		
		for(Asset asset : repository) {
			
			assetsByType = this.index.get(asset.type());
			
			if(assetsByType == null) {
				assetsByType = new ArrayList<Asset>();
				this.index.put(asset.type(), assetsByType);
			}
			
			assetsByType.add(asset);
		}
	}
	
}
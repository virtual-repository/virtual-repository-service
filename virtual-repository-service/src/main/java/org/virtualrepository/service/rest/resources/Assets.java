/**
 * (c) 2013 FAO / UN (project: virtual-produced-service)
 */
package org.virtualrepository.service.rest.resources;

import static org.virtualrepository.service.Constants.*;
import static org.virtualrepository.service.rest.errors.Error.*;
import static org.virtualrepository.service.rest.resources.Assets.*;
import static org.virtualrepository.service.utils.Utils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.Asset;
import org.virtualrepository.AssetType;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.service.configuration.Configuration;
import org.virtualrepository.service.rest.VrsMediaType;
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
public class Assets implements Cacheable {

	private static Logger log = LoggerFactory.getLogger(Assets.class);
	
	public static final String path = "/assets";
	public static final String typeParam = "type";

	private final VirtualRepository repository;
	private final Configuration configuration;
	
	private Calendar lastRefresh;

	@Inject
	public Assets(VirtualRepository repository, Configuration configuration) {

		this.repository = repository;
		this.configuration = configuration;

	}

	@PostConstruct
	public void init() {
		refresh();
	}

	@GET
	@Produces({jmom,xmom,xobject})
	public Collection<Asset> get(@QueryParam(typeParam) List<String> typeNames) {

		
		Collection<AssetType> types = typesFrom(typeNames);

		Collection<Asset> assets = assetsFor(types);
			
		log.info("returning metadata about {} assets of types: {}", assets.size(), types);

		return assets;

	}
	
	@POST
	@Produces({jmom,xmom,xobject})
	public Collection<Asset> refreshAndGet(@QueryParam(typeParam) List<String> typeNames) {

		refresh(typesFrom(typeNames));

		return get(typeNames);
	}

	@GET
	@Path("{id}")
	@Produces({jmom,xmom,xobject})
	public Asset getOne(@PathParam("id") String id) {

		try {
			return repository.lookup(id);
		}
		catch(IllegalStateException e) {
			throw no_such_asset.toException("unknown asset "+id);
		}
		
	}
	
	@GET
	@Path("{id}")
	@Produces({sdmx_ml,jtable,xtable,vtable})
	public Object getOneContent(@PathParam("id") String id, @Context VrsMediaType type) {

		Asset asset = getOne(id);
		
		try {
			return repository.retrieve(asset, type.api());
			
		}
		catch(IllegalStateException e) {
			throw invalid_mediatype.toException(e);
		}
		
	}
	
	
	
	
	
	
	
	// helpers
	
	
	Collection<AssetType> typesFrom(List<String> typeNames) {

		List<AssetType> types = new ArrayList<AssetType>();

		AssetType[] knownTypes = configuration.assetTypes();

		if (typeNames == null || typeNames.isEmpty())
			types = Arrays.asList(knownTypes);
		else
			for (String name : typeNames)
				types.add(typeWith(knownTypes, name));

		return types;
	}

	private Collection<Asset> assetsFor(Collection<AssetType> types) {

		Collection<Asset> assets = new ArrayList<Asset>();
		Collection<List<Asset>> assetsByType = repository.lookup(types.toArray(new AssetType[0])).values();

		for (List<Asset> assetsForThisType : assetsByType)
			assets.addAll(assetsForThisType);

		return assets;
	}

	private void refresh(Collection<AssetType> types) {
		refresh(types.toArray(new AssetType[0]));
	}

	private void refresh(AssetType... types) {

		if (types.length == 0)
			types = configuration.assetTypes();

		try {

			log.info("refreshing asset metadata for types {}", Arrays.asList(types));

			int added = repository.discover(types);

			if (added > 0 || lastRefresh==null)
				lastRefresh = Calendar.getInstance();

		} catch (Exception e) {
			Utils.rethrow("could not refresh assets (see cause)", e);
		}
	}
	
	@Override
	public Calendar lastModified() {
		return lastRefresh;
	}
	
	public String etag() {
		return String.valueOf(lastRefresh.getTimeInMillis());
	}
}
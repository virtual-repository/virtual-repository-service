/**
 * (c) 2013 FAO / UN (project: virtual-repository-service)
 */
package org.virtualrepository.service.rest;

import static org.virtualrepository.service.Constants.*;
import static org.virtualrepository.service.rest.AssetsResource.*;
import static org.virtualrepository.service.rest.errors.Error.*;
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
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.Asset;
import org.virtualrepository.AssetType;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.service.configuration.Configuration;
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
@Produces({jmom,xmom,xobject})
public class AssetsResource {

	private static Logger log = LoggerFactory.getLogger(AssetsResource.class);
	
	public static final String path = "/assets";
	public static final String typeParam = "type";

	private final VirtualRepository repository;
	private final Configuration configuration;
	
	private Calendar lastRefresh;

	@Inject
	public AssetsResource(VirtualRepository repository, Configuration configuration) {

		this.repository = repository;
		this.configuration = configuration;

	}

	@PostConstruct
	public void init() {
		refresh();
	}

	@GET
	@Path("{id}")
	public Response getOne(@PathParam("id") String id, @Context Request request) {

		Response unchanged = evaluateChange(request);

		if (unchanged != null)
			return unchanged;

		try {
			Asset asset = repository.lookup(id);
			return ok(asset);
		}
		catch(IllegalStateException e) {
			throw no_such_asset.toException("unknown asset "+id);
		}
		
	}
	
	@GET
	public Response get(@QueryParam(typeParam) List<String> typeNames, @Context Request request) {

		Response unchanged = evaluateChange(request);

		if (unchanged != null)
			return unchanged;

		
		Collection<AssetType> types = typesFrom(typeNames);

		Collection<Asset> assets = assetsFor(types);
			
		log.info("returning metadata about {} assets of types: {}", assets.size(), types);

		return ok(assets);

	}
	
	


	@POST
	public Response refreshAndGet(@QueryParam(typeParam) List<String> typeNames, @Context Request request) {

		refresh(typesFrom(typeNames));

		return get(typeNames, request);
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

			if (added > 0)
				lastRefresh = Calendar.getInstance();

			System.err.println(lastRefresh.getTime());

		} catch (Exception e) {
			Utils.rethrow("could not refresh assets (see cause)", e);
		}
	}

	// helpers

	private Response ok(Object outcome) {

		return Response.ok(outcome).lastModified(lastRefresh.getTime()).tag(etag()).build();
	}
	
	private EntityTag etag() {
		return new EntityTag(String.valueOf(lastRefresh.getTimeInMillis()));
	}

	private Response evaluateChange(Request request) {

		//if not null, adds etag header
		ResponseBuilder builder = request.evaluatePreconditions(lastRefresh.getTime(),etag());
		
		return builder == null ? null : builder.build();
	}
}
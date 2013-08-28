/**
 * (c) 2013 FAO / UN (project: virtual-repository-service)
 */
package org.virtualrepository.service.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.virtualrepository.Asset;
import org.virtualrepository.AssetType;
import org.virtualrepository.service.utils.TypeUtilities;

import com.sun.jersey.spi.resource.Singleton;

/**
 * Place your class / interface description here.
 *
 * History:
 *
 * ------------- --------------- -----------------------
 * Date			 Author			 Comment
 * ------------- --------------- -----------------------
 * 27 Aug 2013   Fiorellato     Creation.
 *
 * @version 1.0
 * @since 27 Aug 2013
 */
@Path("/assets")
@Singleton
public class AssetsServices extends AbstractVirtualRepositoryServices {
	private Map<AssetType, Collection<Asset>> _assets;

	public AssetsServices() {
		this._assets = new HashMap<AssetType, Collection<Asset>>();
		
		this.updateAssets();
	}

	protected AssetsServices updateAssets() {
		this._assets.clear();
		
		return this.updateAssets(this.availableTypes());
	}
	
	protected AssetsServices updateAssets(AssetType[] types) {
		for(AssetType type : types)
			this._assets.remove(type);
		
		repository().discover(types);
		
		Collection<Asset> assetsByType;
		for(Asset asset : repository()) {
			assetsByType = this._assets.get(asset.type());
			
			if(assetsByType == null) {
				assetsByType = new ArrayList<Asset>();

				this._assets.put(asset.type(), assetsByType);
			}
			
			assetsByType.add(asset);
		}
		
		return this;
	}
	
	private Collection<Asset> doGetAssets(@Context UriInfo info) {
		Collection<AssetType> requestedAssetTypes = new ArrayList<AssetType>();

		AssetType[] availableAssetTypes = availableTypes();
		
		List<String> requestedAssetTypesParameterValues = info.getQueryParameters().get("assetType");
		
		if(requestedAssetTypesParameterValues != null)
			for(String requestedAssetType : requestedAssetTypesParameterValues) {
				requestedAssetTypes.add(TypeUtilities.forName(availableAssetTypes, requestedAssetType));
			}
		
		Collection<Asset> assets = new ArrayList<Asset>();

		if(requestedAssetTypes.isEmpty())
			requestedAssetTypes = Arrays.asList(availableAssetTypes);
		
		Collection<Asset> byType;
		for(AssetType type : requestedAssetTypes) {
			byType = this._assets.get(type);
			
			if(byType != null)
				for(Asset asset : byType)
					assets.add(asset);
		}

		return assets;
	}
	
	private Collection<Asset> doGetUpdatedAssets(UriInfo info) {
		return this.updateAssets().doGetAssets(info);
	}
	
	@GET
	@Path("/meta")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getJSONAssets(@Context UriInfo info) {
		try {
			return this.jsonResponse(this.doGetAssets(info));
		} catch (Throwable t) {
			return this.handleError(t);
		}
	}
	
	@GET
	@Path("/meta")
	@Produces(MediaType.APPLICATION_XML)
	public Response getXMLAssets(@Context UriInfo info) {
		try {
			return this.xmlResponse(this.doGetAssets(info));
		} catch (Throwable t) {
			return this.handleError(t);
		}
	}

	@POST
	@Path("/meta")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getJSONUpdatedAssets(@Context UriInfo info) {
		try {
			return this.jsonResponse(this.doGetUpdatedAssets(info));
		} catch (Throwable t) {
			return this.handleError(t);
		}
	}
	
	@POST
	@Path("/meta")
	@Produces(MediaType.APPLICATION_XML)
	public Response getXMLUpdatedAssets(@Context UriInfo info) {
		try {
			return this.xmlResponse(this.doGetUpdatedAssets(info));
		} catch (Throwable t) {
			return this.handleError(t);
		}
	}
}
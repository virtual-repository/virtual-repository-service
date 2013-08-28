/**
 * (c) 2013 FAO / UN (project: virtual-repository-service)
 */
package org.virtualrepository.service.model;

import java.io.Serializable;

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
public class ConfigurationData implements Serializable {
	/** Field serialVersionUID */
	private static final long serialVersionUID = -4506944056776921217L;
	
	private String endpointName;
	private String virtualRepositoryVersion;
	private String[] supportedAssetTypes;
	
	/**
	 * Class constructor
	 *
	 */
	public ConfigurationData() {
		super();
	}

	/**
	 * Class constructor
	 *
	 * @param endpointName
	 * @param virtualRepositoryVersion
	 * @param supportedAssetTypes
	 */
	public ConfigurationData(String endpointName, String virtualRepositoryVersion, String[] supportedAssetTypes) {
		super();
		this.endpointName = endpointName;
		this.virtualRepositoryVersion = virtualRepositoryVersion;
		this.supportedAssetTypes = supportedAssetTypes;
	}

	/**
	 * @return the 'endpointName' value
	 */
	public String getEndpointName() {
		return this.endpointName;
	}
	
	/**
	 * @param endpointName the 'endpointName' value to set
	 */
	public void setEndpointName(String endpointName) {
		this.endpointName = endpointName;
	}
	
		
	/**
	 * @return the 'virtualRepositoryVersion' value
	 */
	public String getVirtualRepositoryVersion() {
		return this.virtualRepositoryVersion;
	}

	/**
	 * @param virtualRepositoryVersion the 'virtualRepositoryVersion' value to set
	 */
	public void setVirtualRepositoryVersion(String virtualRepositoryVersion) {
		this.virtualRepositoryVersion = virtualRepositoryVersion;
	}
	
	/**
	 * @return the 'supportedAssetTypes' value
	 */
	public String[] getSupportedAssetTypes() {
		return this.supportedAssetTypes;
	}

	/**
	 * @param supportedAssetTypes the 'supportedAssetTypes' value to set
	 */
	public void setSupportedAssetTypes(String[] supportedAssetTypes) {
		this.supportedAssetTypes = supportedAssetTypes;
	}
}
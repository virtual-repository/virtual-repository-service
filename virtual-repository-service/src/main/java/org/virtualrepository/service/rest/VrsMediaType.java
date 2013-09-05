package org.virtualrepository.service.rest;

import static javax.ws.rs.core.MediaType.*;
import static org.virtualrepository.service.Constants.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Variant;
import javax.ws.rs.core.Variant.VariantListBuilder;

import org.virtualrepository.service.io.Binder;

/**
 * An enumeration of the media types supported by the service.
 * 
 * @author Fabio Simeoni
 * 
 */
public enum VrsMediaType {

	JSON(APPLICATION_JSON) {

		@Override
		public String bind(Binder binder, Object object) {
			return binder.jsonMoM(object);
		}
	},
	
	
	XML(APPLICATION_XML) {

		@Override
		public String bind(Binder binder, Object object) {
			return binder.xmlMoM(object);
		}
	},
	
	VXML(APPLICATION_VXML) {

		@Override
		public String bind(Binder binder, Object object) {
			return binder.vxml(object);
		}
	};
	

	/**
	 * Helper to allow fluent dispatching of data bindings requests for specific media types
	 * 
	 */
	public class BindClause {

		final Object object;

		/**
		 * Creates an instance to bind a given object for this media type;
		 * 
		 * @param object the object
		 */
		public BindClause(Object object) {
			this.object = object;
		}

		/**
		 * Returns a binding of the object for this media type
		 * 
		 * @param binder the {@link Binder} to use for the binding
		 * @return the binding
		 */
		public String with(Binder binder) {
			return bind(binder, object);
		}
	}

	private final static Map<String, String> names = new HashMap<String, String>();

	String standardName;

	// prepares a mapping form enumeration values to standardName media type names
	static {
		for (VrsMediaType type : values())
			names.put(type.standardName, type.name());
	}

	/**
	 * Crates a value of this enumeration corresponding to a given media type
	 * 
	 * @param name the standardName name of the media type
	 */
	VrsMediaType(String name) {
		this.standardName = name;
	}
	
	@Override
	public String toString() {
		return standardName;
	}

	/**
	 * Overridden internally by enumeration values to bind a given object to a corresponding format.
	 * 
	 * @param binder the {@link Binder} to use for the binding
	 * @param object the object to be bound
	 * @return
	 */
	abstract String bind(Binder binder, Object object);

	/**
	 * Binds a given object to the format associated with this media type.
	 * 
	 * @param object the object
	 * @return a {@link BindClause} that allows client to provide a {@link Binder} to use for the binding
	 */
	public BindClause bind(Object object) {
		return new BindClause(object);
	}

	/**
	 * Return the {@link VrsMediaType} with a given standard name.
	 * 
	 * @param name the standard name 
	 * @return the media type
	 *         
	 * @throws IllegalArgumentException if the request requires a media type which is not supported by the service
	 */
	public static VrsMediaType fromString(String name) {

			String match = names.get(name);
			
			if (match==null)
				throw new IllegalArgumentException("unexpected media type " + name);
			
			return valueOf(match);
	}
	
	public static List<Variant> supported() {
		
		VariantListBuilder builder = VariantListBuilder.newInstance();
		
		for (VrsMediaType type : values())
			builder.mediaTypes(MediaType.valueOf(type.toString()));
		
		return builder.add().build();
	}

}
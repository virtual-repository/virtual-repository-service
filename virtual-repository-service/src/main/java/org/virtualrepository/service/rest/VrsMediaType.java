package org.virtualrepository.service.rest;

import static org.virtualrepository.service.Constants.*;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.virtualrepository.service.io.Binder;

/**
 * An enumeration of the media types supported by the service.
 * 
 * @author Fabio Simeoni
 * 
 */
public enum VrsMediaType {

	JMOM(jmom) {

		@Override
		public String bind(Binder binder, Object object) {
			return binder.jsonMoM(object);
		}
	},
	
	
	XMOM(xmom) {

		@Override
		public String bind(Binder binder, Object object) {
			return binder.xmlMoM(object);
		}
	},
	
	XOBJECT(xobject) {

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

	private final static Map<String, VrsMediaType> index = new HashMap<String, VrsMediaType>();

	private MediaType type;

	// prepares a mapping form enumeration values to standardName media type index
	static {
		for (VrsMediaType type : values())
			index.put(type.toString(), type);
	}

	/**
	 * Crates a value of this enumeration corresponding to a given media type
	 * 
	 * @param name the standardName name of the media type
	 */
	VrsMediaType(String name) {
		this.type= MediaType.valueOf(name);
	}
	
	/**
	 * Returns a {@link MediaType} that corresponds to this type.
	 * @return the {@link MediaType}
	 */
	public MediaType type() {
		return type;
	}
	
	@Override
	public String toString() {
		return type.toString();
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
	 * Return the {@link VrsMediaType} from its string representation.
	 * 
	 * @param representation the string
	 * @return the media type
	 *         
	 * @throws IllegalArgumentException if string does not represent a known type
	 */
	public static VrsMediaType fromString(String representation) {

			VrsMediaType match = index.get(representation);
			
			if (match==null)
				throw new IllegalArgumentException("unexpected media type " + representation);
			
			return match;
	}

	public static void main(String[] args) {
		System.out.println(fromString(jmom.toString()));
	}
}
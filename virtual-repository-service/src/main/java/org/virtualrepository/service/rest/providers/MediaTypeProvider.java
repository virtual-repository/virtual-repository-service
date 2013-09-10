package org.virtualrepository.service.rest.providers;

import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Variant;
import javax.ws.rs.core.Variant.VariantListBuilder;
import javax.ws.rs.ext.Provider;

import org.virtualrepository.service.rest.VrsMediaType;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

@Provider
public class MediaTypeProvider extends AbstractHttpContextInjectable<VrsMediaType> implements
		InjectableProvider<Context, Type> {

	private static List<Variant> supportedTypes;
	
	static {

		VariantListBuilder builder = VariantListBuilder.newInstance();
		
		for (VrsMediaType type : VrsMediaType.values())
			builder.mediaTypes(type.type());
		
		supportedTypes = builder.add().build();
	}

	@Override
	public ComponentScope getScope() {
		return ComponentScope.PerRequest;
	}

	@Override
	public Injectable<VrsMediaType> getInjectable(ComponentContext ic, Context a, Type c) {

		return c.equals(VrsMediaType.class) ? this : null;

	}

	@Override
	public VrsMediaType getValue(HttpContext context) {
		
		// media type selection
		Variant preferred = context.getRequest().selectVariant(supportedTypes);
		
		return preferred==null?null:VrsMediaType.fromMediaType(preferred.getMediaType());
	}
	
	

}

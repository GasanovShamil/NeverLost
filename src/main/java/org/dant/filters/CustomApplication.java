package org.dant.filters;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
 
import org.dant.filters.AuthFilter;

 
public class CustomApplication extends ResourceConfig 
{
    public CustomApplication() 
    {
        packages("org.dant.filters");
        register(LoggingFilter.class);
        register(AuthFilter.class);
    }
}

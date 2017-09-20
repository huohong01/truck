package com.hsdi.NetMe.util;

import com.getsentry.raven.DefaultRavenFactory;
import com.getsentry.raven.Raven;
import com.getsentry.raven.dsn.Dsn;
import com.getsentry.raven.event.helper.ForwardedAddressResolver;
import com.getsentry.raven.event.helper.HttpEventBuilderHelper;

public class NetMeRavenFactory extends DefaultRavenFactory {

    @Override
    public Raven createRavenInstance(Dsn dsn) {
        Raven raven = new Raven();
        raven.setConnection(createConnection(dsn));

        /*
        Create and use the ForwardedAddressResolver, which will use the
        X-FORWARDED-FOR header for the remote address if it exists.
         */
        ForwardedAddressResolver forwardedAddressResolver = new ForwardedAddressResolver();
        raven.addBuilderHelper(new HttpEventBuilderHelper(forwardedAddressResolver));

        return raven;
    }
}

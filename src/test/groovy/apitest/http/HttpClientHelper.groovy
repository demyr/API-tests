package apitest.http

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder

import javax.ws.rs.client.Client
import java.util.concurrent.TimeUnit

/**
 * Created by demyr on 10-Nov-19.
 */
final class HttpClientHelper {

    static Client newClient(int baseTimeoutSeconds) {
        new ResteasyClientBuilder()
                .connectTimeout(baseTimeoutSeconds, TimeUnit.SECONDS)
                .readTimeout(baseTimeoutSeconds, TimeUnit.SECONDS)
                .disableTrustManager()
                .build()
    }

}

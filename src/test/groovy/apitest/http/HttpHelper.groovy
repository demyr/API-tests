package apitest.http

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

import javax.ws.rs.client.Client
import javax.ws.rs.client.Entity
import javax.ws.rs.client.Invocation
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MultivaluedHashMap
import javax.ws.rs.core.Response

/**
 * Created by demyr on 10-Nov-19.
 */
class HttpHelper {

    private final Client client
    private final String serverUrl;

    HttpHelper(Client client, String serverUrl) {
        this.client = client
        this.serverUrl = serverUrl
    }

    def get(final URI uri) {
        get(uri.toString())
    }

    def get(final String url) {
        def response = get(url, MediaType.APPLICATION_JSON_TYPE)
        response
    }

    private static String stringEntity(final Response response) {
        bufferEntity(response)
        response.readEntity(String)
    }

    private static boolean bufferEntity(Response response) {
        response.bufferEntity()
    }

    def get(final String url, final MediaType accept) {
        request('GET', url, accept)
    }

    def request(final String method, final String url) {
        request(method, url, MediaType.APPLICATION_JSON_TYPE)
    }

    ResponseHelper post(String url, Map body) {
        request('POST',
                url,
                MediaType.APPLICATION_JSON_TYPE,
                [:], body)
    }

    ResponseHelper patch(String url, Map body) {
        request('PATCH',
                url,
                MediaType.APPLICATION_JSON_TYPE,
                [:], body)
    }

    ResponseHelper delete(String url) {
        request('DELETE',
                url,
                MediaType.APPLICATION_JSON_TYPE,
                [:])
    }


    ResponseHelper request(final String method,
                           final String url,
                           final MediaType mediaType,
                           final Map<String, String> headersMap = [:],
                           final def body = null) {
        MultivaluedHashMap<String, Object> requestHeaderMap = new MultivaluedHashMap<>()
        headersMap.each {
            requestHeaderMap.put(it.key, Collections.singletonList(it.value))
        }
        Invocation.Builder builder = client.target(serverUrl + url)
                .request()
                .headers(requestHeaderMap)
                .accept(mediaType)

        if (body) {
            return new ResponseHelper(builder.method(method, Entity.json(
                    JsonOutput.toJson(body)
            )))
        } else {
            return new ResponseHelper(builder.method(method))
        }

    }

    public static class ResponseHelper {

        private final Response origin

        ResponseHelper(Response origin) {
            this.origin = origin
        }

        Response origin() {
            return origin
        }

        ResponseHelper assertStatus(int status){
            assert origin.status == status
            return this
        }

        ResponseHelper withBody(Closure withBodyAction){
            withBodyAction.call(this.getBodyAsJson())
            return this
        }

        def getBodyAsJson() {
            new JsonSlurper().parseText(
                    stringEntity(origin)
            )
        }
    }

}

package http

/**
 * Created by demyr on 10-Nov-19.
 */
class UrlConfig {
    static Map urlConfigMap = [
            proto    : 'https',
            host     : 'www.thecocktaildb.com',
            restBase : "api"
    ]

    static String restApiUrl() {
        "${urlConfigMap.proto}://${urlConfigMap.host}/${urlConfigMap.restBase}"
    }
}

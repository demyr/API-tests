package apitest

import http.UrlConfig
import http.HttpClientHelper
import http.HttpHelper
import spock.lang.Specification

/**
 * Created by demyr on 10-Nov-19.
 */
class BaseSpecification extends Specification{

    private synchronized HttpHelper httpHelper

    synchronized HttpHelper httpHelper() {
        if (httpHelper == null) {
            httpHelper = new HttpHelper(
                    HttpClientHelper.newClient(60),
                    UrlConfig.restApiUrl() + '/json/v1/1'
            )
        }
        return httpHelper
    }
}


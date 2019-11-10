package apitest.common

import apitest.http.HttpClientHelper
import apitest.http.HttpHelper
import spock.lang.Specification

/**
 * Created by demyr on 10-Nov-19.
 */
class BaseHttp extends Specification{

    private synchronized HttpHelper httpHelper

    synchronized HttpHelper httpHelper() {
        if (httpHelper == null) {
            httpHelper = new HttpHelper(
                    HttpClientHelper.newClient(60),
                    Config.restApiUrl() + '/json/v1/1'
            )
        }
        return httpHelper
    }
}


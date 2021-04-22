package util

import groovy.util.logging.Slf4j
import groovyx.net.http.RESTClient
import org.apache.http.client.RedirectStrategy

import static groovyx.net.http.ContentType.JSON

@Slf4j
class RequestHelper {

    def get(Map params) {
        return createClient(params.port).get(
            path: params.path,
            query: params.query,
            headers: createHeaderMap(params.uuid, params.headers),
        )
    }

    def post(Map params) {
        log.info("===============>$params")
        return createClient(params.port).post(
            path: params.path,
            query: params.query,
            requestContentType: JSON,
            body: params.body ?: "",
            headers: createHeaderMap(params.token, params.headers),
        )
    }

    def put(Map params) {
        return createClient(params.port).put(
            path: params.path,
            query: params.query,
            requestContentType: JSON,
            body: params.body ?: "",
            headers: createHeaderMap(params.token, params.headers),
        )
    }

    def delete(Map params) {
        return createClient(params.port).delete(
            path: params.path,
            query: params.query,
            headers: createHeaderMap(params.token, params.headers),
        )
    }

    def patch(Map params) {
        return createClient(params.port).patch(
            path: params.path,
            query: params.query,
            requestContentType: JSON,
            headers: createHeaderMap(params.token, params.headers),
            body: params.body ?: ""
        )
    }

    private def createHeaderMap(String token, Map other) {
        def headers = (token != null) ?
            [
                    "Accept"       : "application/json; text/plain",
                    "Content-Type" : "application/json;charset=utf-8",
                    "Authorization": "Bearer ${token}",
            ] :
            [
                    "Accept"      : "application/json; text/plain",
                    "Content-Type": "application/json;charset=utf-8"
            ]

        return headers + (other ?: [:])
    }

    private def createClient(int port) {
        log.info("=====>port: $port")

        def client = new RESTClient("http://localhost:$port")
        def redirectStrategy = [
            getRedirect : { request, response, context -> null},
            isRedirected : { request, response, context -> false}
        ]
        client.client.setRedirectStrategy(redirectStrategy as RedirectStrategy)
        client.parser.'text/html' = client.parser.'text/plain'
        client.parser.'application/problem+json' = client.parser.'application/json'
        client.handler.failure = { resp, data ->
            resp.setData(data)
            return resp
        }

        return client
    }

}

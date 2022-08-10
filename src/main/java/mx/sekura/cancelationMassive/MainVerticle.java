package mx.sekura.cancelationMassive;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import mx.sekura.cancelationMassive.Connection.WebClientConnection;
import mx.sekura.cancelationMassive.Entity.Environment;
import mx.sekura.cancelationMassive.Reactor.ReactorUnit;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: Jorge Martinez Mohedano
 * @since: 2022-08-08
 * <p>
 */
public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        JsonObject configuration = config();
        Environment.createInstance(
                configuration.getBoolean("production"),
                configuration.getString("kernoApiHost"),
                configuration.getInteger("port"),
                vertx);

        WebClientOptions options = new WebClientOptions()
                .setUserAgent("CancellationMassive/1.0.0");
        options.setDefaultPort(443);
        options.setSsl(true);
        options.setVerifyHost(false);
        options.setTrustAll(true);
        WebClient webClientKrn = WebClient.create(vertx, options);
        WebClientConnection.createInstance(webClientKrn);

        Set<String> allowedHeaders = new HashSet<>();
        allowedHeaders.add(HttpHeaderNames.X_REQUESTED_WITH.toString());
        allowedHeaders.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN.toString());
        allowedHeaders.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS.toString());
        allowedHeaders.add(HttpHeaderNames.ORIGIN.toString());
        allowedHeaders.add(HttpHeaderNames.CONTENT_TYPE.toString());
        allowedHeaders.add(HttpHeaderNames.ACCEPT.toString());
        Set<HttpMethod> allowedMethods = new HashSet<>();
        allowedMethods.add(HttpMethod.GET);
        allowedMethods.add(HttpMethod.POST);

        Router router = Router.router(vertx);

        router.route().handler(CorsHandler.create("*").allowedHeaders(allowedHeaders).allowedMethods(allowedMethods));
        router.route().handler(BodyHandler.create());

        router.post("/cancellation/massive").handler(ReactorUnit::cancelationMassive);

        // Creamos nuestro servidor HTTP
        vertx.createHttpServer(
                        // para fines de depuración, habilitamos el registro de la actividad de la red.
                        new HttpServerOptions().setLogActivity(true))
                // Handle every request using the router
                .requestHandler(router)
                // Start listening
                .listen(Environment.getInstance().getPort())
                // Print the port
                .onSuccess(server ->
                        System.out.println(
                                "HTTP server started on port " + server.actualPort()
                        )
                );

    }
}

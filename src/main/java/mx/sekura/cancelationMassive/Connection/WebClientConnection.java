package mx.sekura.cancelationMassive.Connection;

import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

/**
 * @author: Jorge Martinez Mohedano
 * @since: 2022-08-08
 * <p>
 */
public class WebClientConnection {
    private WebClient client;
    private static WebClientConnection instance;

    private WebClientConnection(WebClient webClient){
        this.client = webClient;
    }
    public static void createInstance(WebClient webClient){
        if(instance == null){
            instance = new WebClientConnection(webClient);
        }
    }

    public WebClient getClient() {
        return client;
    }

    public void setClient(WebClient client) {
        this.client = client;
    }

    public static WebClientConnection getInstance() {
        return instance;
    }

    public static void setInstance(WebClientConnection instance) {
        WebClientConnection.instance = instance;
    }
}

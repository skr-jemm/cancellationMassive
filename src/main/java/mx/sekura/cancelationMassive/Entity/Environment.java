package mx.sekura.cancelationMassive.Entity;

import io.vertx.core.Vertx;

/**
 * @author: Jorge Martinez Mohedano
 * @since: 2022-08-08
 * <p>
 */
public class Environment {
    private boolean production;
    private String kernoApiHost;
    private Integer port;
    private Vertx vertx;
    //---> Environment
    private static Environment instance;

    private Environment (boolean production, String kernoApiHost, Integer port, Vertx vertx) {
        this.production = production;
        this.kernoApiHost = kernoApiHost;
        this.port = port;
        this.vertx = vertx;
    }
    public static void createInstance (boolean production, String kernoApiHost, Integer port, Vertx vertx) {
        if(instance == null){
            instance = new Environment(production,kernoApiHost,port,vertx);
        }
    }

    public boolean isProduction() {
        return production;
    }

    public void setProduction(boolean production) {
        this.production = production;
    }

    public String getKernoApiHost() {
        return kernoApiHost;
    }

    public void setKernoApiHost(String kernoApiHost) {
        this.kernoApiHost = kernoApiHost;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Vertx getVertx() {
        return vertx;
    }

    public void setVertx(Vertx vertx) {
        this.vertx = vertx;
    }

    public static Environment getInstance() {
        return instance;
    }

    public static void setInstance(Environment instance) {
        Environment.instance = instance;
    }
}

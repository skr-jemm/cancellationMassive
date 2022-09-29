package mx.sekura.cancelationMassive.Business;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.multipart.MultipartForm;
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import mx.sekura.cancelationMassive.Connection.WebClientConnection;
import mx.sekura.cancelationMassive.Entity.Environment;
import mx.sekura.cancelationMassive.Entity.WorkOrderResult;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.binary.BinaryCodec;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.util.Base64;

/**
 * @author: Jorge Martinez Mohedano
 * @since: 2022-08-08
 * <p>
 */
public class BusinessUnit {
    private static final Logger logger = LoggerFactory.getLogger(BusinessUnit.class);

    public static void getWorkOrder(JsonObject policyInformation, Handler<AsyncResult<WorkOrderResult>> objectResponse) {
        WebClientConnection.getInstance().getClient() //Obtenemos el cliente
                .post(Environment.getInstance().getKernoApiHost(),"/workorders?pSize=10&pStart=0&pOrder=")
                .basicAuthentication("admin@system.xyz","Qwerty999.")
                .sendJson(policyInformation)
                .onSuccess(result -> {
                    if(result.statusCode() == 200){
                        WorkOrderResult workOrderResult = result.bodyAsJson(WorkOrderResult.class);
                        objectResponse.handle(Future.succeededFuture(workOrderResult));
                    } else {
                        logger.debug(result.bodyAsString());
                        objectResponse.handle(Future.failedFuture(result.statusMessage()));
                    }
                })
                .onFailure(ern -> {
                    objectResponse.handle(Future.failedFuture(ern.getMessage()));
                });
    }
    public static void cancellationPolicy (JsonObject kernoCancellation, String workId, Handler<AsyncResult<Integer>> resultHandler){
        logger.debug(Environment.getInstance().getKernoApiHost()+"/workorders/"+workId+"/cancellations");
        logger.debug(kernoCancellation.toString());
        WebClientConnection.getInstance().getClient()
                .post(Environment.getInstance().getKernoApiHost(),"/workorders/"+workId+"/cancellations")
                .basicAuthentication("admin@system.xyz","Qwerty999.")
                .putHeader("AuditId","8")
                .sendJson(kernoCancellation)
                .onSuccess(result -> {
                    if(result.statusCode() == 200){
                        resultHandler.handle(Future.succeededFuture(1));
                    } else {
                        logger.debug(result.bodyAsString());
                        resultHandler.handle(Future.failedFuture(result.statusMessage()));
                    }
                })
                .onFailure(ern -> {
                    resultHandler.handle(Future.failedFuture(ern.getMessage()));
                });
    }
    public static void getWorkOrderByWorkOrderId (String WorkOrderId, Handler<AsyncResult<JsonObject>> workOrderCompleteInformation) {
        WebClientConnection.getInstance().getClient()
                .get(Environment.getInstance().getKernoApiHost(),"/workorders/"+WorkOrderId)
                .basicAuthentication("admin@system.xyz","Qwerty999.")
                .send()
                .onSuccess(workOrderResult->{
                    if(workOrderResult.statusCode() == 200){
                        workOrderCompleteInformation.handle(Future.succeededFuture(workOrderResult.bodyAsJsonObject()));
                    } else {
                        logger.debug("Error al obtener, Código_:"+ workOrderResult.statusCode());
                        workOrderCompleteInformation.handle(Future.failedFuture(workOrderResult.bodyAsString()));
                    }
                })
                .onFailure(ern->{
                    logger.debug(ern.getMessage());
                    workOrderCompleteInformation.handle(Future.failedFuture(ern.getMessage()));
        });
    }
    public static void getCustomerByIdCustomer (String customerId, Handler<AsyncResult<JsonObject>> customerInformation) {
        WebClientConnection.getInstance().getClient()
                .get(Environment.getInstance().getKernoApiHost(),"/customers/"+customerId)
                .basicAuthentication("admin@system.xyz","Qwerty999.")
                .send()
                .onSuccess(workOrderResult->{
                    if(workOrderResult.statusCode() == 200){
                        customerInformation.handle(Future.succeededFuture(workOrderResult.bodyAsJsonObject()));
                    } else {
                        logger.debug("Error al obtener, Código_:"+ workOrderResult.statusCode());
                        customerInformation.handle(Future.failedFuture(workOrderResult.bodyAsString()));
                    }
                })
                .onFailure(ern->{
                    logger.debug(ern.getMessage());
                    customerInformation.handle(Future.failedFuture(ern.getMessage()));
        });
    }
    public static void injectionPDF(byte[] upload, String key, String name, String description, String size, String filename, Handler<AsyncResult<Integer>> asyncResultHandler){
        Integer i = 1;
        logger.info(Environment.getInstance().getKernoApiHost() + "/cloud/bucket/folder/upload2");

        Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
        WebTarget webTarget = client.target(Environment.getInstance().getKernoApiHost() + "/cloud/bucket/folder/upload2");



        Invocation.Builder builder = webTarget.request();
        //Response response = builder.post(Entity.entity(multiPart,MediaType.MULTIPART_FORM_DATA_TYPE));

        //logger.info(String.valueOf(response.getStatus()));
        //logger.info(response.readEntity(String.class));
        //asyncResultHandler.handle(Future.succeededFuture(response.getStatus()));
    }
    public static void injectPdfToDigitalCenter (Buffer buffer,
            String key, String name, String description, String size, String filename
            ,Handler<AsyncResult<Integer>> statusInserted) throws IOException {

        /*File tempFile = File.createTempFile(filename,"_tem",null);
        FileOutputStream fos = new FileOutputStream(tempFile);
        fos.write(upload);

        logger.debug(tempFile.getAbsolutePath());


        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        client.register(HttpAuthenticationFeature.basic("admin@system.xyz","Qwerty999."));
        WebTarget webTarget = client.target(URI.create("https://" +Environment.getInstance().getKernoApiHost() + "/cloud/bucket/folder/upload2"));

        FormDataMultiPart multiPart = new FormDataMultiPart();
        //multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
        multiPart.bodyPart(new FileDataBodyPart("file",tempFile,MediaType.APPLICATION_OCTET_STREAM_TYPE));
        multiPart.field("key",key);
        multiPart.field("name",name);
        multiPart.field("description",description);
        multiPart.field("size",size);

        Invocation.Builder builder = webTarget.request(MediaType.MULTIPART_FORM_DATA_TYPE)
                .header("AuditId","8");

        Response clientResponse = builder.post(Entity.entity(multiPart,MediaType.MULTIPART_FORM_DATA_TYPE));
        logger.debug(String.valueOf(clientResponse.getStatus()));
        logger.debug(clientResponse.readEntity(String.class));
        statusInserted.handle(Future.succeededFuture(clientResponse.getStatus())); */

        MultipartForm form = MultipartForm.create();
        form.binaryFileUpload("file",filename,buffer,"application/octet-stream");
        form.attribute("key",key);
        form.attribute("name",name);
        form.attribute("description",description);
        form.attribute("size",size);




        WebClientConnection.getInstance().getClient()
                .post(Environment.getInstance().getKernoApiHost(),"/cloud/bucket/folder/upload2")
                .basicAuthentication("admin@system.xyz","Qwerty999.")
                .putHeader("AuditId","8")
                .putHeader("content-type", "multipart/form-data")
                .sendMultipartForm(form)
                .onSuccess(result -> {
                    if(result.statusCode() == 200){
                        logger.debug(String.valueOf(result.statusCode()));
                        statusInserted.handle(Future.succeededFuture(1));
                    } else {
                        logger.debug(String.valueOf(result.statusCode()));
                        statusInserted.handle(Future.failedFuture(result.statusMessage()));
                    }
                })
                .onFailure(ern -> {
                   logger.debug(ern.getMessage());
                   statusInserted.handle(Future.failedFuture(ern.getMessage()));
                });
    }
}

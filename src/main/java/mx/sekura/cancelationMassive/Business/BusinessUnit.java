package mx.sekura.cancelationMassive.Business;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.multipart.MultipartForm;
import mx.sekura.cancelationMassive.Connection.WebClientConnection;
import mx.sekura.cancelationMassive.Entity.Environment;
import mx.sekura.cancelationMassive.Entity.WorkOrderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public static void injectPdfToDigitalCenter (byte[] upload,
            String key, String name, String description, String size, String filename
            ,Handler<AsyncResult<Integer>> statusInserted){

        MultipartForm form = MultipartForm.create();
        form.binaryFileUpload("file",filename, Buffer.buffer(upload),"application/octet-stream");
        form.attribute("key",key);
        form.attribute("name",name);
        form.attribute("description",description);
        form.attribute("size",size);


        WebClientConnection.getInstance().getClient()
                .post(Environment.getInstance().getKernoApiHost(),"/cloud/bucket/folder/upload2")
                .basicAuthentication("admin@system.xyz","Qwerty999.")
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

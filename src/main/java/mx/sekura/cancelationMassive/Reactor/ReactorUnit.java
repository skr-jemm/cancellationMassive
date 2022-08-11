package mx.sekura.cancelationMassive.Reactor;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import mx.sekura.cancelationMassive.Business.BusinessUnit;
import mx.sekura.cancelationMassive.Entity.*;
import mx.sekura.cancelationMassive.Helper.CancellationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @author: Jorge Martinez Mohedano
 * @since: 2022-08-08
 * <p>
 */
public class ReactorUnit {
    private static final Logger logger = LoggerFactory.getLogger(ReactorUnit.class);

    public static void cancelationMassive(RoutingContext routingContext) {
        long startTime = System.nanoTime();
        //Verificamos que contenga los siguientes archivos.
        //1.-fileCancellation: relación de los documentos a eliminar
        //2.-fileUpload: achivo a subir
        FileUpload cancellationFile = null;
        List<FileUpload> uploadFile = new ArrayList<>();
        //Recorremos el arreglo para acomodar en dos variables,
        // 1.- La primera el el archivo de cancelación
        // 2.- Los archivos a subir
        for (FileUpload upload : routingContext.fileUploads()) {
            //trabajar con el objeto de cancelación
            if (upload.name().equals("fileCancellation")) {
                cancellationFile = upload;
            } else {
                logger.debug(upload.fileName());
                uploadFile.add(upload);
            }
        }
        final Object[] asyncResponseData = {new WorkOrderResult()};
        CancellationHelper.builderCancellationDetailObject(cancellationFile, listResult -> {
            if (listResult.succeeded()) {
                //Preparamos las acciones necesarias para ingresar documentos al Centro Digital
                //1.-Obtenemos la información de la WorkOrden (Orden de trabajo) en ella, se encuenta la información de la carpeta.
                //2.-Obtenemos la informació del cliente, para obtener el bucket
                int i;
                int cancellation;
                int noCancellation;
                //Interamos para ir obteniendo el detalle en la posición y cancelarlo
                for(i =0; i<= listResult.result().size()-1; i++){
                    CancellationDetail detail = listResult.result().get(i);
                    //Paso 1.- Consultamos la información de la póliza por el número externo
                    Future<WorkOrderResult> workOrderResultFuture = BusinessUnit.getWorkOrder(detail.getPolicy());
                    workOrderResultFuture.onComplete(resultAsyncResult -> {
                        if(resultAsyncResult.succeeded()){
                            //Posición 0, Almacena la respuesta de la consulta del WorkOrder
                            asyncResponseData[0] = resultAsyncResult.result();
                        } else {
                            routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end(resultAsyncResult.cause().getMessage());
                        }
                    });
                    List<Future> futures = new ArrayList<>();
                    futures.add(workOrderResultFuture);

                    CompositeFuture.all(futures)
                    .onSuccess( success ->{
                        if(success.succeeded(0)){
                            WorkOrderResult resultSearch = (WorkOrderResult) asyncResponseData[0];
                        } else {
                        }
                    });
                }

                //for (CancellationDetail detail : listResult.result()) {
                    //////////////////////////////////////////////////////////////////////////////////////////////
                    /*BusinessUnit.getWorkOrder(requestSearchByExternalNumber[0], workOrderResultSearch -> {
                        KernoCancelation cancelation = null;
                        try {
                            cancelation = CancellationHelper.buildCancellation(detail);
                            if (workOrderResultSearch.succeeded()) {
                                //Buscamos la orden de trabajo para obtener la información

                                for (WorkOrderResult.WorkOrderResponse workOrderResponse : workOrderResultSearch.result().getWorkOrderList()) {
                                    if (Objects.equals(workOrderResponse.getExternalNumber(), detail.getPolicy())) {
                                        logger.debug("Encontro la coincidencia");
                                        //Encontramos la orden de trabjo si coincide el resultado en el campo externalNumber con la
                                        //póliza que llega por parámetros
                                        //Procedemos a obtener la información de la orden de trabajo completa
                                        KernoCancelation finalCancelation = cancelation;
                                        BusinessUnit.getWorkOrderByWorkOrderId(workOrderResponse.getWorkOrderId(), workOrderCompleteInformation -> {
                                            if (workOrderCompleteInformation.succeeded()) {
                                                //Procedemos a obtener la información de un cliente
                                                //De este JSON sólo trabajaremos con los siguientes campos.
                                                //#"folderS3Key" (si es nulo, se deberá crear y se validará en ocaciones especiales)
                                                //#"customerId" para obtener el Identificador del cliente
                                                //Procedemos a buscar la información del cliente
                                                String folderS3Key = workOrderCompleteInformation.result().getString("folderS3Key");
                                                BusinessUnit.getCustomerByIdCustomer(workOrderCompleteInformation.result().getString("customerId"), customerInformation -> {
                                                    if (customerInformation.succeeded()) {
                                                        //Aquí usaremos el atributo bucket": "skr-cloud-test:::::23011" sin los 5 :
                                                        //Iteramos para obtener la posición necesaria
                                                        String bucket = customerInformation.result().getString("bucket");
                                                        //El método split separa la cadena en por medio de la expresión regular para hacer n cadenas.
                                                        String[] folderResource = bucket.split(":::::");
                                                        //#folderResource[0]; //----> Bucket de configuración
                                                        //#folderResource[1]; //----> Información de la carpeta del cliente
                                                        //Procedemos a validar si cuenta con la con carpeta ya hecha, usaremos la variable folderS3Key, si no, se debe de hacer una
                                                        String keySaveDocument = "";
                                                        keySaveDocument = keySaveDocument + folderResource[1] + "/";
                                                        if (folderS3Key == null) {
                                                            // ¡¡¡ SE PUEDE CAMBIAR, EN ESTE MOMENTO ES UNA FIANZA !!
                                                            keySaveDocument = keySaveDocument + "FIANZA_";
                                                            String externalNumber = workOrderResponse.getExternalNumber().replace("/", ":_:_:");
                                                            keySaveDocument = keySaveDocument + externalNumber + "/";
                                                            //keySaveDocument = keySaveDocument + "ID_WO:"+workOrderResponse.getWorkOrderId();
                                                        } else {
                                                            keySaveDocument = keySaveDocument + folderS3Key;
                                                        }
                                                        logger.debug(keySaveDocument);
                                                        //Procedemos primero con la inserción del documento del PDF.
                                                        //Recoremos primero los documentos de cancelacion
                                                        for (FileUpload upload : uploadFile) {
                                                            if (Objects.equals(upload.fileName(), detail.getDocumentToDigitalCenter())) {
                                                                String finalKeySaveDocument = keySaveDocument;
                                                                BusinessUnit.cancellationPolicy(new JsonObject(Json.encode(finalCancelation)), workOrderResponse.getWorkOrderId(), resultCancellation -> {
                                                                    if (!resultCancellation.succeeded()) {
                                                                        logger.info("Error al cancelar la póliza:" + workOrderResponse.getExternalNumber());
                                                                    } else {
                                                                        if (resultCancellation.result() == 1) {
                                                                            logger.info("Se canceló la póliza:" + workOrderResponse.getExternalNumber());
                                                                            Environment.getInstance().getVertx().fileSystem().readFile(upload.uploadedFileName(), result -> {
                                                                                BusinessUnit.injectPdfToDigitalCenter(result.result().getBytes(), finalKeySaveDocument, folderResource[0],
                                                                                        "OTRO", String.valueOf(upload.size()), upload.fileName(), insertDocumentResponse -> {
                                                                                            if (insertDocumentResponse.succeeded()) {
                                                                                                logger.debug("Lo insertó");
                                                                                            } else {
                                                                                                routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end(insertDocumentResponse.cause().getMessage());
                                                                                            }
                                                                                        });
                                                                            });
                                                                        } else {
                                                                            logger.info("Error al cancelar la póliza:" + workOrderResponse.getExternalNumber());

                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    } else {
                                                        routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end(customerInformation.cause().getMessage());
                                                    }
                                                });
                                            } else {
                                                routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end(workOrderCompleteInformation.cause().getMessage());
                                            }
                                        });
                                    }
                                }
                            } else {
                                routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end(workOrderResultSearch.cause().getMessage());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            routingContext.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end(e.getMessage());
                        }
                    }); */
                    //////////////////////////////////////////////////////////////////////////////////////////////

                    /*if(i == listResult.result().size()-1){
                        routingContext.response().setStatusCode(HttpResponseStatus.OK.code()).putHeader("Content-Type", "json/application").end("Se concluye con la cancelación masiva");
                    }*/
                } else {
                routingContext.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end(listResult.cause().getMessage());
            }
        });
    }

}


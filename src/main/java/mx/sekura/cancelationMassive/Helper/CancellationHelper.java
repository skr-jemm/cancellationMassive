package mx.sekura.cancelationMassive.Helper;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import mx.sekura.cancelationMassive.Business.BusinessUnit;
import mx.sekura.cancelationMassive.Entity.CancellationDetail;
import mx.sekura.cancelationMassive.Entity.Environment;
import mx.sekura.cancelationMassive.Entity.KernoCancelation;
import mx.sekura.cancelationMassive.Entity.WorkOrderResult;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: Jorge Martinez Mohedano
 * @since: 2022-08-08
 * <p>
 */
public class CancellationHelper {
    private static final Logger logger = LoggerFactory.getLogger(CancellationHelper.class);

    public static void builderCancellationDetailObject(FileUpload cancellationFile, Handler<AsyncResult<List<CancellationDetail>>> asyncResult){
        //Construimos el archivo subiendo al caché de vertx el archivo para poder manipularlo
        Environment.getInstance().getVertx().fileSystem().readFile(cancellationFile.uploadedFileName(), result -> {
            byte[] excelDocument = result.result().getBytes();
            //Construimos el documento
            InputStream input = new ByteArrayInputStream(excelDocument);
            //Procedemos a manipularlo el documento de Excel
            try {

                //Creamos el XLS
                XSSFWorkbook file = new XSSFWorkbook(input);
                //Obtenemos la primera pestaña
                XSSFSheet mySheet = file.getSheetAt(0);
                //Guardamos los objetos con la infomarción la pre información, nos servivira para guardar los id de las pólizas, la fecha, motivo
                List<CancellationDetail> cancellationDetails = new ArrayList<>();
                DataFormatter formatter = new DataFormatter();
                //Usamos el método getPhysicalNumberOfRows para obtener el número de celdas obtenidas
                int row; //--> variable de fila
                int cell; //---> variable de celda
                logger.debug(String.valueOf(mySheet.getPhysicalNumberOfRows()));
                //Iniciamos la interacción con las filas, para posteriormente recorrer cada celda de la fila seleccionada
                try{
                    for(row = 0; row <= mySheet.getPhysicalNumberOfRows()-1; row++){
                        //Declaramos el objeto como nulo, para que nos permita crearlo una vez que pasó los encabezados del documento
                        CancellationDetail objetDetail = null;
                        //Iniciaremos la creación del objeto siempre y cuando la fila en la que se encuentra apuntando sea cabecera
                        if(row != 0){
                            //Recorreremos cuatro posición por cada fila ya que el documento cuenta actualmente con 4 celdas por fila
                            for(cell = 0; cell <= 5; cell ++){
                                //Validamos, si la posición de la celda es 0, creamos el objeto, para que no se instancie cada vez que entre al ciclo
                                if(cell == 0) objetDetail = new CancellationDetail();
                                //Seguido a ello, procedemos a entrar en un switch, con el fin de identificar en que atributo del objeto se asignará la información
                                switch (cell){
                                    case 0: //Número de póliza
                                        objetDetail.setPolicy(formatter.formatCellValue(mySheet.getRow(row).getCell(cell)));
                                        break;
                                    case 1: //Fecha de cancelación
                                        objetDetail.setCancellationDate(String.valueOf(mySheet.getRow(row).getCell(cell).getDateCellValue()));
                                        break;
                                    case 2: // Id de cancelación
                                        objetDetail.setCancellationReasonId(formatter.formatCellValue(mySheet.getRow(row).getCell(cell)));
                                        break;
                                    case 3://Nombre de la cancelación
                                        objetDetail.setCancellationDescription(mySheet.getRow(row).getCell(cell).getStringCellValue());
                                        break;
                                    case 4://Nombre del documento para el centro digital
                                        objetDetail.setDocumentToDigitalCenter(mySheet.getRow(row).getCell(cell).getStringCellValue());
                                        break;
                                    case 5://Observaciones
                                        objetDetail.setObservations(mySheet.getRow(row).getCell(cell).getStringCellValue());
                                        break;
                                }
                            }
                        }
                        //Procedemos a la inserción, está se realizará una vez recorrida todas las celdas de la fila correspondiente
                        //Validamos que no sea nula para ser insertada
                        if(objetDetail != null){
                            cancellationDetails.add(objetDetail);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    asyncResult.handle(Future.failedFuture(e.getMessage()));
                }
                //Hasta este punto, ya separamos los objetos que séran cancelados y los archivos que seŕan enviados
                asyncResult.handle(Future.succeededFuture(cancellationDetails));
            } catch (Exception e) {
                e.printStackTrace();
                asyncResult.handle(Future.failedFuture(e.getMessage()));
            }
        });
    }
    public static JsonObject builderSearchByExternalNumber(String policyId){
        JsonObject request = new JsonObject();
        request.put("bail", true);
        request.put("endorsement", true);
        request.put("insurance", true);
        request.put("key", "externalNumber");
        request.put("ot",true);
        request.put("otEndorsement",true);
        request.put("policy",true);
        request.put("value",policyId);
        return request;
    }
    public static KernoCancelation buildCancellation(CancellationDetail cancellationDetail) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",
                Locale.ENGLISH);
        Date parsedDate = sdf.parse(cancellationDetail.getCancellationDate());

        KernoCancelation cancelation = new KernoCancelation();
        cancelation.setCancellationReasonId(cancellationDetail.getCancellationReasonId());
        cancelation.setCancellationDescription(cancellationDetail.getCancellationDescription());
        cancelation.setObservations(cancellationDetail.getObservations());
        cancelation.setCancellationDate(new SimpleDateFormat("dd/MM/yyyy").format(parsedDate));

        return cancelation;
    }
    /*public static Future<List<Integer>> cancellationPolices (List<FileUpload> pdfsToInsert, KernoCancelation kernoCancelation, String KeySaveDocument, String folderResource,CancellationDetail detail, WorkOrderResult.WorkOrderResponse workOrderResponse){
        Promise<List<Integer>> promise = Promise.promise();
        List<Integer> operationRelation = new ArrayList<>()

        for(FileUpload upload: pdfsToInsert){
            if(Objects.equals(upload.fileName(), detail.getDocumentToDigitalCenter())){
                //
                logger.debug("Encontró coincidencia");
                BusinessUnit.cancellationPolicy(new JsonObject(Json.encode(kernoCancelation)),workOrderResponse.getWorkOrderId(), resultCancellation -> {
                    if(!resultCancellation.succeeded()){
                        //posicion, value
                        operationRelation.add(0, 1);
                        logger.info("Error al cancelar la póliza:"+workOrderResponse.getExternalNumber());
                    } else {
                        if(resultCancellation.result() == 1){
                            logger.info("Se canceló la póliza:"+workOrderResponse.getExternalNumber());
                            Environment.getInstance().getVertx().fileSystem().readFile(upload.uploadedFileName(), result -> {
                                BusinessUnit.injectPdfToDigitalCenter(result.result().getBytes(), KeySaveDocument, folderResource, "OTRO",String.valueOf(upload.size()), upload.fileName(),insertDocumentResponse -> {
                                            if(insertDocumentResponse.succeeded()){
                                                logger.debug("Lo insertó");
                                            } else {
                                                logger.debug("No insertó el pdf");
                                            }
                                        });
                            });
                            operationRelation.add(1, 1);
                        } else {
                            logger.info("Error al cancelar la póliza:"+workOrderResponse.getExternalNumber());
                            operationRelation.add(0,  1);
                        }
                    }
                });

            }
        }
        promise.complete(operationRelation);
        return promise.future();
    }*/
}

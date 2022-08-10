package mx.sekura.cancelationMassive.Entity;

/**
 * @author: Jorge Martinez Mohedano
 * @since: 2022-08-08
 * <p>
 */
public class CancellationDetail {
    private String policy;
    private String cancellationDate;
    private String cancellationDescription;
    private String cancellationReasonId;
    private String observations;

    private String documentToDigitalCenter;

    public CancellationDetail() {
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(String cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public String getCancellationDescription() {
        return cancellationDescription;
    }

    public void setCancellationDescription(String cancellationDescription) {
        this.cancellationDescription = cancellationDescription;
    }

    public String getCancellationReasonId() {
        return cancellationReasonId;
    }

    public void setCancellationReasonId(String cancellationReasonId) {
        this.cancellationReasonId = cancellationReasonId;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getDocumentToDigitalCenter() {
        return documentToDigitalCenter;
    }

    public void setDocumentToDigitalCenter(String documentToDigitalCenter) {
        this.documentToDigitalCenter = documentToDigitalCenter;
    }
}

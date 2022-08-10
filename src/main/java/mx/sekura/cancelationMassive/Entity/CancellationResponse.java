package mx.sekura.cancelationMassive.Entity;

/**
 * @author: Jorge Martinez Mohedano
 * @since: 2022-08-09
 * <p>
 */
public class CancellationResponse {
    private String message;
    private Integer cancellationCount;
    private Integer noCancellationCount;
    private Long timer;

    public CancellationResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCancellationCount() {
        return cancellationCount;
    }

    public void setCancellationCount(Integer cancellationCount) {
        this.cancellationCount = cancellationCount;
    }

    public Integer getNoCancellationCount() {
        return noCancellationCount;
    }

    public void setNoCancellationCount(Integer noCancellationCount) {
        this.noCancellationCount = noCancellationCount;
    }

    public Long getTimer() {
        return timer;
    }

    public void setTimer(Long timer) {
        this.timer = timer;
    }
}

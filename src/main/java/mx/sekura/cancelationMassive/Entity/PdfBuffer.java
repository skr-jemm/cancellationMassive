package mx.sekura.cancelationMassive.Entity;

import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.FileUpload;

/**
 * @author: Jorge Martinez Mohedano
 * @since: 2022-09-29
 * <p>
 */
public class PdfBuffer {
    private String policyName;
    private Buffer buffer;
    private String size;

    public PdfBuffer() {
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public Buffer getBuffer() {
        return buffer;
    }

    public void setBuffer(Buffer buffer) {
        this.buffer = buffer;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "PdfBuffer{" +
                "policyName='" + policyName + '\'' +
                ", buffer=" + buffer +
                ", size='" + size + '\'' +
                '}';
    }
}

package org.lorislab.quarkus.data.test.source;

import java.time.LocalTime;
import java.util.Set;

import org.lorislab.quarkus.data.*;

import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.buffer.Buffer;

@Entity
@Table(name = "MODEL")
public class Model {

    @Id
    public String id;

    public Integer version;

    public String messageId;

    public String parent;

    @Column(name = "process")
    public String processId;

    public String processVersion;

    @Column(enumType = EnumType.INTEGER)
    private ModelStatus statusIntegerPrivate;

    @Column(enumType = EnumType.STRING)
    private ModelStatus statusPrivate;

    public ModelStatus status;

    @Column(enumType = EnumType.INTEGER)
    public ModelStatus statusInteger;

    public JsonObject data;

    public Set<Long> createdFrom;

    public LocalTime time;

    @Column(ignore = true)
    public String ignore;

    private String privateTest1;

    @Column(name = "p_test_2")
    private String privateTest2;

    @Column(name = "p_test_bool")
    private Boolean privateTest3;

    private int primitiveTest1;

    private boolean primitiveTest2;

    @Column(name = "buffer")
    private Buffer buffer;

    @Column(name = "dataByte")
    private byte[] dataByte;

    public ModelStatus getStatusIntegerPrivate() {
        return statusIntegerPrivate;
    }

    public void setStatusIntegerPrivate(ModelStatus statusIntegerPrivate) {
        this.statusIntegerPrivate = statusIntegerPrivate;
    }

    public ModelStatus getStatusPrivate() {
        return statusPrivate;
    }

    public void setStatusPrivate(ModelStatus statusPrivate) {
        this.statusPrivate = statusPrivate;
    }

    public byte[] getDataByte() {
        return dataByte;
    }

    public void setDataByte(byte[] dataByte) {
        this.dataByte = dataByte;
    }

    public Buffer getBuffer() {
        return buffer;
    }

    public void setBuffer(Buffer buffer) {
        this.buffer = buffer;
    }

    public String getPrivateTest1() {
        return privateTest1;
    }

    public void setPrivateTest1(String privateTest1) {
        this.privateTest1 = privateTest1;
    }

    public String getPrivateTest2() {
        return privateTest2;
    }

    public void setPrivateTest2(String privateTest2) {
        this.privateTest2 = privateTest2;
    }

    public Boolean getPrivateTest3() {
        return privateTest3;
    }

    public void setPrivateTest3(Boolean privateTest3) {
        this.privateTest3 = privateTest3;
    }

    public int getPrimitiveTest1() {
        return primitiveTest1;
    }

    public void setPrimitiveTest1(int primitiveTest1) {
        this.primitiveTest1 = primitiveTest1;
    }

    public boolean isPrimitiveTest2() {
        return primitiveTest2;
    }

    public void setPrimitiveTest2(boolean primitiveTest2) {
        this.primitiveTest2 = primitiveTest2;
    }
}

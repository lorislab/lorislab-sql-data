package org.lorislab.quarkus.data.test.source;

public class SimpleClassMapperImpl extends SimpleClassMapper {

    public static final SimpleClassMapperImpl INSTANCE = new SimpleClassMapperImpl();

    @Override
    public org.lorislab.quarkus.data.test.source.Model map(io.vertx.mutiny.sqlclient.Row row) {
        if (row == null) {
            return null;
        }

        org.lorislab.quarkus.data.test.source.Model result = new org.lorislab.quarkus.data.test.source.Model();

        int idx;

        java.lang.String id = null;
        if ((idx = row.getColumnIndex(org.lorislab.quarkus.data.test.source.Model_.ID)) != -1
                && (id = row.getString(idx)) != null) {
            result.id = id;
        }
        if (id == null) {
            return null;
        }

        java.lang.Integer version;
        if ((idx = row.getColumnIndex(org.lorislab.quarkus.data.test.source.Model_.VERSION)) != -1
                && (version = row.getInteger(idx)) != null) {
            result.version = version;
        }

        java.lang.String messageId;
        if ((idx = row.getColumnIndex(org.lorislab.quarkus.data.test.source.Model_.MESSAGE_ID)) != -1
                && (messageId = row.getString(idx)) != null) {
            result.messageId = messageId;
        }

        java.lang.String parent;
        if ((idx = row.getColumnIndex(org.lorislab.quarkus.data.test.source.Model_.PARENT)) != -1
                && (parent = row.getString(idx)) != null) {
            result.parent = parent;
        }

        java.lang.String processId;
        if ((idx = row.getColumnIndex(org.lorislab.quarkus.data.test.source.Model_.PROCESS_ID)) != -1
                && (processId = row.getString(idx)) != null) {
            result.processId = processId;
        }

        java.lang.String processVersion;
        if ((idx = row.getColumnIndex(org.lorislab.quarkus.data.test.source.Model_.PROCESS_VERSION)) != -1
                && (processVersion = row.getString(idx)) != null) {
            result.processVersion = processVersion;
        }

        java.lang.Integer statusIntegerPrivate;
        if ((idx = row.getColumnIndex(org.lorislab.quarkus.data.test.source.Model_.STATUS_INTEGER_PRIVATE)) != -1
                && (statusIntegerPrivate = row.getInteger(idx)) != null) {
            if (0 <= statusIntegerPrivate
                    && statusIntegerPrivate < org.lorislab.quarkus.data.test.source.ModelStatus.values().length) {
                result.setStatusIntegerPrivate(
                        org.lorislab.quarkus.data.test.source.ModelStatus.values()[statusIntegerPrivate]);
            }
        }

        java.lang.String statusPrivate;
        if ((idx = row.getColumnIndex(org.lorislab.quarkus.data.test.source.Model_.STATUS_PRIVATE)) != -1
                && (statusPrivate = row.getString(idx)) != null) {
            result.setStatusPrivate(org.lorislab.quarkus.data.test.source.ModelStatus.valueOf(statusPrivate));
        }

        java.lang.Integer statusInteger;
        if ((idx = row.getColumnIndex(org.lorislab.quarkus.data.test.source.Model_.STATUS_INTEGER)) != -1
                && (statusInteger = row.getInteger(idx)) != null) {
            if (0 <= statusInteger && statusInteger < org.lorislab.quarkus.data.test.source.ModelStatus.values().length) {
                result.statusInteger = org.lorislab.quarkus.data.test.source.ModelStatus.values()[statusInteger];
            }
        }

        io.vertx.core.json.JsonObject data;
        if ((idx = row.getColumnIndex(org.lorislab.quarkus.data.test.source.Model_.DATA)) != -1
                && (data = row.getJsonObject(idx)) != null) {
            result.data = data;
        }

        java.lang.Long[] createdFrom;
        if ((idx = row.getColumnIndex(org.lorislab.quarkus.data.test.source.Model_.CREATED_FROM)) != -1
                && (createdFrom = row.getArrayOfLongs(idx)) != null) {
            result.createdFrom = new java.util.HashSet<>(java.util.Arrays.asList(createdFrom));
        }

        java.time.LocalTime time;
        if ((idx = row.getColumnIndex(org.lorislab.quarkus.data.test.source.Model_.TIME)) != -1
                && (time = row.getLocalTime(idx)) != null) {
            result.time = time;
        }

        java.lang.String privateTest1;
        if ((idx = row.getColumnIndex(org.lorislab.quarkus.data.test.source.Model_.PRIVATE_TEST1)) != -1
                && (privateTest1 = row.getString(idx)) != null) {
            result.setPrivateTest1(privateTest1);
        }

        java.lang.String privateTest2;
        if ((idx = row.getColumnIndex(org.lorislab.quarkus.data.test.source.Model_.PRIVATE_TEST2)) != -1
                && (privateTest2 = row.getString(idx)) != null) {
            result.setPrivateTest2(privateTest2);
        }

        java.lang.Boolean privateTest3;
        if ((idx = row.getColumnIndex(org.lorislab.quarkus.data.test.source.Model_.PRIVATE_TEST3)) != -1
                && (privateTest3 = row.getBoolean(idx)) != null) {
            result.setPrivateTest3(privateTest3);
        }

        java.lang.Integer primitiveTest1;
        if ((idx = row.getColumnIndex(org.lorislab.quarkus.data.test.source.Model_.PRIMITIVE_TEST1)) != -1
                && (primitiveTest1 = row.getInteger(idx)) != null) {
            result.setPrimitiveTest1(java.util.Optional.of(primitiveTest1).orElse(0));
        }

        java.lang.Boolean primitiveTest2;
        if ((idx = row.getColumnIndex(org.lorislab.quarkus.data.test.source.Model_.PRIMITIVE_TEST2)) != -1
                && (primitiveTest2 = row.getBoolean(idx)) != null) {
            result.setPrimitiveTest2(java.util.Optional.of(primitiveTest2).orElse(false));
        }

        io.vertx.mutiny.core.buffer.Buffer buffer;
        if ((idx = row.getColumnIndex(org.lorislab.quarkus.data.test.source.Model_.BUFFER)) != -1
                && (buffer = row.getBuffer(idx)) != null) {
            result.setBuffer(buffer);
        }

        io.vertx.mutiny.core.buffer.Buffer dataByte;
        if ((idx = row.getColumnIndex(org.lorislab.quarkus.data.test.source.Model_.DATA_BYTE)) != -1
                && (dataByte = row.getBuffer(idx)) != null) {
            result.setDataByte(dataByte.getBytes());
        }

        return result;
    }

    public static org.lorislab.quarkus.data.test.source.Model mapS(io.vertx.mutiny.sqlclient.Row row) {
        return INSTANCE.map(row);
    }

}

package com.github.eybv.messenger.application.service.uuid;

import java.nio.ByteBuffer;
import java.util.UUID;

public class DefaultUUIDService implements UUIDService {

    @Override
    public UUID combine(UUID a, UUID b) {
        return toUUID(xor(toBytes(a), toBytes(b)));
    }

    private UUID toUUID(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        long mostSigBits = buffer.getLong();
        long leastSigBits = buffer.getLong();
        return new UUID(mostSigBits, leastSigBits);
    }

    private byte[] toBytes(UUID uuid) {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return buffer.array();
    }

    private byte[] xor(byte[] a, byte[] b) {
        byte[] bytes = new byte[16];
        for (int i = 0; i < 16; i++) {
            bytes[i] = (byte) (a[i] ^ b[i]);
        }
        return bytes;
    }

}

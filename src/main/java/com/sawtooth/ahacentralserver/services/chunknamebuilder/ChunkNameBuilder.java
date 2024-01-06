package com.sawtooth.ahacentralserver.services.chunknamebuilder;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class ChunkNameBuilder implements IChunkNameBuilder {
    @Override
    public String GetChunkName(String path, String name, int chunkPointer) throws NoSuchAlgorithmException {
        StringBuilder stringBuilder = new StringBuilder();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(String.join("/", path, name, Integer.toString(chunkPointer)).getBytes(StandardCharsets.UTF_8));
        for (byte value : hash)
            stringBuilder.append(value);
        return stringBuilder.toString();
    }
}

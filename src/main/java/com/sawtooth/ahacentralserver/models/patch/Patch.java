package com.sawtooth.ahacentralserver.models.patch;

public record Patch(String op, String path, Object value) {
}

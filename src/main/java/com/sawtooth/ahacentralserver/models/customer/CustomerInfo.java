package com.sawtooth.ahacentralserver.models.customer;

public record CustomerInfo(String name, long filesCount, long chunksCount, long groupsCount) {
}

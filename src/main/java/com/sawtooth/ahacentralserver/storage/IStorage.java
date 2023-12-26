package com.sawtooth.ahacentralserver.storage;

import com.sawtooth.ahacentralserver.storage.repositories.IRepository;

public interface IStorage {
    public <T extends IRepository> T GetRepository(Class<T> interfaceObject) throws InstantiationException;
}

package com.sawtooth.ahacentralserver.services.filerightresolver;

import com.sawtooth.ahacentralserver.models.customer.Customer;
import com.sawtooth.ahacentralserver.models.file.File;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.customer.ICustomerRepository;
import com.sawtooth.ahacentralserver.storage.repositories.groupfileright.IGroupFileRightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileRightResolver implements IFileRightResolver {
    private final IStorage storage;

    @Autowired
    public FileRightResolver(IStorage storage) {
        this.storage = storage;
    }

    @Override
    public boolean Resolve(String right, String customerName, File file) throws InstantiationException {
        Customer customer = storage.GetRepository(ICustomerRepository.class).Get(customerName);

        if (customer.customerID() == file.ownerID() || !storage.GetRepository(IGroupFileRightRepository.class).IsFileHaveGroupRights(file))
            return true;
        return storage.GetRepository(ICustomerRepository.class).IsCustomerHaveFileRight(customer, file, right);
    }
}

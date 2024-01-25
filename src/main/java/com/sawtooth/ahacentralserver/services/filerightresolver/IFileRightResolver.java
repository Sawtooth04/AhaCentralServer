package com.sawtooth.ahacentralserver.services.filerightresolver;

import com.sawtooth.ahacentralserver.models.customer.Customer;
import com.sawtooth.ahacentralserver.models.file.File;

public interface IFileRightResolver {
    public boolean Resolve(String right, String customerName, File file) throws InstantiationException;
}

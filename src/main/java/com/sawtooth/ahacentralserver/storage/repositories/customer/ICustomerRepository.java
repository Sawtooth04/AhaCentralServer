package com.sawtooth.ahacentralserver.storage.repositories.customer;

import com.sawtooth.ahacentralserver.models.customer.Customer;
import com.sawtooth.ahacentralserver.models.file.File;
import com.sawtooth.ahacentralserver.storage.repositories.IRepository;

public interface ICustomerRepository extends IRepository {
    public boolean IsCustomerNameFree(String name);

    public void Add(Customer customer);

    public Customer Get(String name);

    public int Count();

    public boolean IsCustomerHaveFileRight(Customer customer, File file, String fileRight);

    public boolean IsCustomerHaveRole(Customer customer, String role);
}

package com.sawtooth.ahacentralserver.storage.repositories.customer;

import com.sawtooth.ahacentralserver.models.customer.Customer;
import com.sawtooth.ahacentralserver.models.customer.CustomerInfo;
import com.sawtooth.ahacentralserver.models.file.File;
import com.sawtooth.ahacentralserver.storage.repositories.IRepository;

import java.util.List;

public interface ICustomerRepository extends IRepository {
    public boolean IsCustomerNameFree(String name);

    public void Add(Customer customer);

    public Customer Get(String name);

    public List<CustomerInfo> GetInfos();

    public int Count();

    public boolean IsCustomerHaveFileRight(Customer customer, File file, String fileRight);

    public boolean IsCustomerHaveRole(Customer customer, String role);

    public void Update(Customer customer);

    public void Delete(String name);
}

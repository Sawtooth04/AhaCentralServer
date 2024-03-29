package com.sawtooth.ahacentralserver.storage.repositories.groupfileright;

import com.sawtooth.ahacentralserver.models.customer.Customer;
import com.sawtooth.ahacentralserver.models.file.File;
import com.sawtooth.ahacentralserver.models.groupfileright.GroupFileRight;
import com.sawtooth.ahacentralserver.storage.repositories.IRepository;

import java.util.List;

public interface IGroupFileRightRepository extends IRepository {
    public List<GroupFileRight> Get(File file);

    public List<GroupFileRight> GetOfOwner(Customer customer, File file);

    public void Add(GroupFileRight groupFileRight);

    public void Delete(GroupFileRight groupFileRight);

    public boolean IsFileHaveGroupRights(File file);
}

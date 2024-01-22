package com.sawtooth.ahacentralserver.storage.repositories.group;

import com.sawtooth.ahacentralserver.models.customer.Customer;
import com.sawtooth.ahacentralserver.models.group.Group;
import com.sawtooth.ahacentralserver.storage.repositories.IRepository;

import java.util.List;

public interface IGroupRepository extends IRepository {
    public Group Get(int groupID);

    public List<Group> Get(Customer customer);

    public List<Group> GetOwn(Customer customer);

    public void Add(Customer customer, Group group);

    public void Update(Group group);

    public void Delete(Group group);
}

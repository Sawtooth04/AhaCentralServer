import React, {useState} from 'react';
import styles from './style.module.css'
import CustomersHeading from "../CustomersHeading/CustomersHeading";
import CustomersList from "../CustomersList/CustomersList";

const Customers = () => {
    const [selectedCustomersBuffer, setSelectedCustomersBuffer] = useState([]);
    const [customers, setCustomers] = useState([{}]);

    return (
        <div className={styles.customers}>
            <CustomersHeading changePasswordCustomer={null} renameCustomer={null} deleteCustomer={null}
                selectedCustomers={selectedCustomersBuffer}/>
            <CustomersList items={customers} selectedCustomersBuffer={selectedCustomersBuffer} setSelectedCustomersBuffer={setSelectedCustomersBuffer}/>
        </div>
    );
};

export default Customers;
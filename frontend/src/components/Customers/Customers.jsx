import React, {useEffect, useState} from 'react';
import styles from './style.module.css'
import CustomersHeading from "../CustomersHeading/CustomersHeading";
import CustomersList from "../CustomersList/CustomersList";
import CsrfFetch from "../../utils/CsrfFetch";
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";
import RenameCustomerForm from "../RenameCustomerForm/RenameCustomerForm";
import ChangeCustomerPasswordForm from "../ChangeCustomerPasswordForm/ChangeCustomerPasswordForm";
import DeleteCustomerForm from "../DeleteCustomerForm/DeleteCustomerForm";
import AdminComponent from "../AdminComponent/AdminComponent";

const Customers = () => {
    const [selectedCustomersBuffer, setSelectedCustomersBuffer] = useState([]);
    const [customers, setCustomers] = useState([]);
    const [isRenameCustomerFormHidden, setIsRenameCustomerFormHidden] = useState(true);
    const [isChangeCustomerPasswordFormHidden, setIsChangeCustomerPasswordFormHidden] = useState(true);
    const [isDeleteCustomerFormHidden, setIsDeleteCustomerFormHidden] = useState(true);

    useEffect(() => {
        void getCustomers();
    }, []);

    async function getCustomers() {
        let response = await CsrfFetch(await CentralServerLinksProvider.getLink('customer-info-get'), {
            method: 'get'
        });
        setCustomers((await response.json()).customerInfos);
        setSelectedCustomersBuffer([]);
    }

    function renameCustomer() {
        setIsRenameCustomerFormHidden(false);
    }

    function changeCustomerPassword() {
        setIsChangeCustomerPasswordFormHidden(false);
    }

    function deleteCustomers() {
        setIsDeleteCustomerFormHidden(false);
    }

    return (
        <AdminComponent>
            <div className={styles.customers}>
                <RenameCustomerForm isHidden={isRenameCustomerFormHidden} setIsHidden={setIsRenameCustomerFormHidden}
                    customer={selectedCustomersBuffer[0]} onRename={getCustomers}/>
                <ChangeCustomerPasswordForm isHidden={isChangeCustomerPasswordFormHidden} setIsHidden={setIsChangeCustomerPasswordFormHidden}
                    customer={selectedCustomersBuffer[0]} onChange={getCustomers}/>
                <DeleteCustomerForm isHidden={isDeleteCustomerFormHidden} setIsHidden={setIsDeleteCustomerFormHidden}
                    selectedCustomersBuffer={selectedCustomersBuffer} onDelete={getCustomers}/>
                <CustomersHeading changeCustomerPassword={changeCustomerPassword} renameCustomer={renameCustomer} deleteCustomer={deleteCustomers}
                    selectedCustomers={selectedCustomersBuffer}/>
                <CustomersList items={customers} selectedCustomersBuffer={selectedCustomersBuffer} setSelectedCustomersBuffer={setSelectedCustomersBuffer}/>
            </div>
        </AdminComponent>
    );
};

export default Customers;
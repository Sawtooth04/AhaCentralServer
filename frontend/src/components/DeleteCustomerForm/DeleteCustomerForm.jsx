import React from 'react';
import CsrfFetch from "../../utils/CsrfFetch";
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";
import PopUpForm from "../UI/PopUpForm/PopUpForm";
import Button from "../UI/Button/Button";

const DeleteCustomerForm = ({ isHidden, setIsHidden, selectedCustomersBuffer, onDelete }) => {
    async function deleteCustomer(customer) {
        await CsrfFetch((await CentralServerLinksProvider.getLink("customer-delete")).replace('{name}', customer.name), {
            method: 'delete'
        });
    }

    async function deleteCustomers() {
        for (let customer of selectedCustomersBuffer)
            await deleteCustomer(customer);
        await onDelete();
        setIsHidden(true);
    }

    return (
        <PopUpForm header={`Вы действительно хотите удалить выбранные аккаунты пользователей?`} isHidden={isHidden}>
            <Button text={'Удалить'} onClick={deleteCustomers}/>
            <Button text={'Отменить'} onClick={() => setIsHidden(true)}/>
        </PopUpForm>
    );
};

export default DeleteCustomerForm;
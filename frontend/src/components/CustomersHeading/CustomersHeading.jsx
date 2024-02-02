import React from 'react';
import styles from "./style.module.css";
import HintButton from "../UI/HintButton/HintButton";

const CustomersHeading = ({ renameCustomer, changeCustomerPassword, deleteCustomer, selectedCustomers }) => {
    return (
        <h1 className={styles.customersHeading}>
            Пользователи
            <HintButton src={'assets/icons/change.png'} hint={"Переименовать"} onClick={renameCustomer} isActive={selectedCustomers.length === 1}/>
            <HintButton src={'assets/icons/password.png'} hint={"Сменить пароль"} onClick={changeCustomerPassword} isActive={selectedCustomers.length === 1}/>
            <HintButton src={'assets/icons/delete.png'} hint={"Удалить"} onClick={deleteCustomer} isActive={selectedCustomers.length > 0}/>
        </h1>
    );
};

export default CustomersHeading;
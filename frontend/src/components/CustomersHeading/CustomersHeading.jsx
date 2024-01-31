import React from 'react';
import styles from "./style.module.css";
import FilesButton from "../UI/FilesButton/FilesButton";

const CustomersHeading = ({ renameCustomer, changeCustomerPassword, deleteCustomer, selectedCustomers }) => {
    return (
        <h1 className={styles.customersHeading}>
            Пользователи
            <FilesButton src={'assets/icons/change.png'} hint={"Переименовать"} onClick={renameCustomer} isActive={selectedCustomers.length === 1}/>
            <FilesButton src={'assets/icons/password.png'} hint={"Сменить пароль"} onClick={changeCustomerPassword} isActive={selectedCustomers.length === 1}/>
            <FilesButton src={'assets/icons/delete.png'} hint={"Удалить"} onClick={deleteCustomer} isActive={selectedCustomers.length > 0}/>
        </h1>
    );
};

export default CustomersHeading;
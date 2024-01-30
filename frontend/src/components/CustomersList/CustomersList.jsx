import React from 'react';
import styles from "./style.module.css";
import CustomersListItem from "../CustomersListItem/CustomersListItem";

const CustomersList = ({ items, selectedCustomersBuffer, setSelectedCustomersBuffer }) => {
    function onCustomerClick(file) {
        let index = selectedCustomersBuffer.findIndex(f => f.name === file.name);
        if (index === -1)
            setSelectedCustomersBuffer([...selectedCustomersBuffer, file]);
        else {
            selectedCustomersBuffer.splice(index, 1);
            setSelectedCustomersBuffer([...selectedCustomersBuffer]);
        }
    }

    return (
        <div className={styles.customersList}>
            <div className={styles.customersListHeader}>
                <p className={styles.customersListHeaderText}> Имя </p>
                <p className={styles.customersListHeaderText}> Файлов </p>
                <p className={styles.customersListHeaderText}> Чанков </p>
                <p className={styles.customersListHeaderText}> Групп </p>
            </div>
            <div className={styles.customers}>
                {items.map((item, index) => {
                    return <CustomersListItem key={`${index}${item.name}`} item={item} onClick={onCustomerClick}
                        isSelected={typeof(selectedCustomersBuffer.find(i => i.name === item.name)) !== 'undefined'}/>
                })}
            </div>
        </div>
    );
};

export default CustomersList;
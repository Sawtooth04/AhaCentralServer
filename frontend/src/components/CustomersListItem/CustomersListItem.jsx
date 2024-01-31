import React from 'react';
import styles from "./style.module.css";

const CustomersListItem = ({ item, onClick, isSelected }) => {
    return (
        <div className={isSelected ? styles.selectedCustomersListItem : styles.customersListItem} onClick={() => onClick(item)}>
            <p className={styles.customersListItemText}> {item.name} </p>
            <p className={styles.customersListItemText}> {item.filesCount} </p>
            <p className={styles.customersListItemText}> {item.chunksCount} </p>
            <p className={styles.customersListItemText}> {item.groupsCount} </p>
        </div>
    );
};

export default CustomersListItem;
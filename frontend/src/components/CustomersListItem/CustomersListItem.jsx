import React from 'react';
import styles from "./style.module.css";

const CustomersListItem = ({ item, onClick, isSelected }) => {
    return (
        <div className={isSelected ? styles.selectedCustomersListItem : styles.customersListItem} onClick={() => onClick(item)}>
            <p className={styles.customersListItemText}> Писюн </p>
            <p className={styles.customersListItemText}> 45 </p>
            <p className={styles.customersListItemText}> 2 </p>
            <p className={styles.customersListItemText}> 4 </p>
        </div>
    );
};

export default CustomersListItem;
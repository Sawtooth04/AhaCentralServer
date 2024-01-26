import React from 'react';
import styles from "./style.module.css";

const StorageServersItem = ({ item, onClick, isSelected }) => {
    return (
        <div className={isSelected ? styles.selectedStorageServersListItem : styles.storageServersListItem} onClick={() => onClick(item)}>
            <p className={styles.storageServersListItemText}> {item.name} </p>
            <p className={styles.storageServersListItemText}> {item.address} </p>
            <p className={styles.storageServersListItemText}> Storage </p>
        </div>
    );
};

export default StorageServersItem;
import React from 'react';
import styles from './style.module.css';
import GetStorageServerStatusName from "../../utils/StorageServerStatusNameProvider";

const StorageServerStatusListItem = ({ item, onClick, isSelected }) => {
    return (
        <p className={isSelected ? styles.selectedStorageServerStatusListItem : styles.storageServerStatusListItem} onClick={() => onClick(item)}>
            {GetStorageServerStatusName(item.name)}
        </p>
    );
};

export default StorageServerStatusListItem;
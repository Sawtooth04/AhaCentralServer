import React from 'react';
import styles from "./style.module.css";

const BackupServersItem = ({ item, onClick, isSelected }) => {
    return (
        <div className={isSelected ? styles.selectedBackupServersListItem : styles.backupServersListItem} onClick={() => onClick(item)}>
            <p className={styles.backupServersListItemText}> {item.name} </p>
            <p className={styles.backupServersListItemText}> {item.address} </p>
            <p className={styles.backupServersListItemText}> Backup </p>
        </div>
    );
};

export default BackupServersItem;
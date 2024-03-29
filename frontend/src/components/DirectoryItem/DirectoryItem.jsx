import React from 'react';
import { formatBytes } from "../../utils/SpaceFormatter";
import styles from './style.module.css'

const DirectoryItem = ({ item, onClick, isSelected }) => {
    function formatDate(date) {
        return `${date.getHours()}:${date.getMinutes()} ${date.getDate()}.${date.getMonth() + 1}.${date.getFullYear()}`;
    }

    return (
        <div className={isSelected ? styles.selectedDirectoryItem : styles.directoryItem} onClick={() => onClick(item)}>
            <p className={styles.directoryItemText}> {item.name} </p>
            <p className={styles.directoryItemCenteredText}> { item.isFile ? formatBytes(item.size) : ""} </p>
            <p className={styles.directoryItemText}> { item.isFile ? item.extension : ""} </p>
            <p className={styles.directoryItemCenteredText}> { item.isFile ? formatDate(new Date(item.uploadDate)) : ""} </p>
            <p className={styles.directoryItemCenteredText}> { item.isFile ? formatDate(new Date(item.updateDate)) : ""} </p>
        </div>
    );
};

export default DirectoryItem;
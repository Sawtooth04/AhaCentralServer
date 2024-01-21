import React from 'react';
import styles from "./style.module.css";

const GroupsListItem = ({ item, onClick, isSelected }) => {
    return (
        <div className={isSelected ? styles.selectedGroupListItem : styles.groupListItem} onClick={() => onClick(item)}>
            <p className={styles.groupListItemText}> {item.name} </p>
            <p className={styles.groupListItemText}> {item.customersCount} </p>
            <p className={styles.groupListItemText}> {item.ownerName} </p>
        </div>
    );
};

export default GroupsListItem;
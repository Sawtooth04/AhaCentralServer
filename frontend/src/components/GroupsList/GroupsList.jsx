import React from 'react';
import styles from "./style.module.css";
import GroupsListItem from "../GroupsListItem/GroupsListItem";

const GroupsList = ({ items, selectedGroupsBuffer, setSelectedGroupsBuffer }) => {
    function onGroupClick(group) {
        let index = selectedGroupsBuffer.findIndex(g => g.name === group.name && g.ownerName === group.ownerName);
        if (index === -1)
            setSelectedGroupsBuffer([...selectedGroupsBuffer, group]);
        else {
            selectedGroupsBuffer.splice(index, 1);
            setSelectedGroupsBuffer([...selectedGroupsBuffer]);
        }
    }

    function isSelected(group) {
        return selectedGroupsBuffer.findIndex(g => g.name === group.name && g.ownerName === group.ownerName) !== -1;
    }

    return (
        <div className={styles.groupsList}>
            <div className={styles.groupsHeader}>
                <p className={styles.groupsHeaderText}> Название </p>
                <p className={styles.groupsHeaderText}> Пользователей </p>
                <p className={styles.groupsHeaderText}> Владелец </p>
            </div>
            <div className={styles.groups}>
                {items.map((item, index) => {
                    return <GroupsListItem key={`${index}${item.name}`} item={item} onClick={onGroupClick} isSelected={isSelected(item)}/>
                })}
            </div>
        </div>
    );
};

export default GroupsList;
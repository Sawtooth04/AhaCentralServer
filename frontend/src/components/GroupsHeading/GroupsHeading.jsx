import React from 'react';
import styles from "./style.module.css";
import HintButton from "../UI/HintButton/HintButton";

const GroupsHeading = ({ selectedGroups, addGroup, renameGroup, deleteGroups }) => {
    return (
        <h1 className={styles.groupsHeading}>
            Группы
            <HintButton src={'assets/icons/add.png'} hint={"Создать группу"} onClick={addGroup} isActive={true}/>
            <HintButton src={'assets/icons/delete.png'} hint={"Удалить группу"} onClick={deleteGroups} isActive={selectedGroups.length > 0}/>
            <HintButton src={'assets/icons/change.png'} hint={"Переименовать группу"} onClick={renameGroup} isActive={selectedGroups.length === 1}/>
        </h1>
    );
};

export default GroupsHeading;
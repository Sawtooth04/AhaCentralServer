import React from 'react';
import styles from "./style.module.css";
import HintButton from "../UI/HintButton/HintButton";

const StorageServersHeading = ({ addServer, deleteServer, selectedServers }) => {
    return (
        <h1 className={styles.storageServersHeading}>
            Серверы хранения
            <HintButton src={'assets/icons/add.png'} hint={"Добавить сервер"} onClick={addServer} isActive={true}/>
            <HintButton src={'assets/icons/delete.png'} hint={"Удалить серверы"} onClick={deleteServer} isActive={selectedServers.length > 0}/>
        </h1>
    );
};

export default StorageServersHeading;
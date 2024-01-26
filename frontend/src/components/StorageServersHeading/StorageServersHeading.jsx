import React from 'react';
import styles from "./style.module.css";
import FilesButton from "../UI/FilesButton/FilesButton";

const StorageServersHeading = ({ addServer, deleteServer, selectedServers }) => {
    return (
        <h1 className={styles.storageServersHeading}>
            Серверы хранения
            <FilesButton src={'assets/icons/add.png'} hint={"Добавить сервер"} onClick={addServer} isActive={true}/>
            <FilesButton src={'assets/icons/delete.png'} hint={"Удалить серверы"} onClick={deleteServer} isActive={selectedServers.length > 0}/>
        </h1>
    );
};

export default StorageServersHeading;
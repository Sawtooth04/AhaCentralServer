import React from 'react';
import styles from "./style.module.css";
import StorageServersItem from "../StorageServersItem/StorageServersItem";
import BackupServersItem from "../BackupServersItem/BackupServersItem";

const StorageServersList = ({ storage, backup, serversBuffer, setServersBuffer }) => {
    function onServerClick(server) {
        let index = serversBuffer.findIndex(s => s.name === server.name);
        if (index === -1)
            setServersBuffer([...serversBuffer, server]);
        else {
            serversBuffer.splice(index, 1);
            setServersBuffer([...serversBuffer]);
        }
    }

    return (
        <div className={styles.storageServersList}>
            <div className={styles.directoryItemsHeader}>
                <p className={styles.directoryItemsHeaderText}> Название </p>
                <p className={styles.directoryItemsHeaderText}> Адрес </p>
                <p className={styles.directoryItemsHeaderText}> Тип сервера </p>
            </div>
            <div className={styles.storageServers}>
                {storage.map((item, index) => {
                    return <StorageServersItem key={`${index}${item.name}`} item={item} onClick={onServerClick}
                        isSelected={typeof(serversBuffer.find(i => i.name === item.name)) !== 'undefined'}/>
                })}
                {backup.map((item, index) => {
                    return <BackupServersItem key={`${index}${item.name}`} item={item} onClick={onServerClick}
                        isSelected={typeof(serversBuffer.find(i => i.name === item.name)) !== 'undefined'}/>
                })}
            </div>
        </div>
    );
};

export default StorageServersList;
import React, {useEffect, useState} from 'react';
import styles from "./style.module.css";

import StorageServersHeading from "../StorageServersHeading/StorageServersHeading";
import StorageServersList from "../StorageServersList/StorageServersList";
import csrfFetch from "../../utils/CsrfFetch";
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";

const StorageServers = () => {
    const [storageServers, setStorageServers] = useState([]);
    const [backupServers, setBackupServers] = useState([]);
    const [selectedServersBuffer, setSelectedServersBuffer] = useState([]);

    useEffect(() => {
        void getStorageServers();
        void getBackupServers();
    }, []);

    async function getStorageServers() {
        let response = await csrfFetch(await CentralServerLinksProvider.getLink('storage-server-get'));
        setStorageServers((await response.json()).servers);
    }

    async function getBackupServers() {
        let response = await csrfFetch(await CentralServerLinksProvider.getLink('storage-server-backup-get'));
        setBackupServers((await response.json()).servers);
    }

    return (
        <div className={styles.storageServers}>
            <StorageServersHeading selectedServers={selectedServersBuffer}/>
            <StorageServersList storage={storageServers} backup={backupServers} serversBuffer={selectedServersBuffer}
                setServersBuffer={setSelectedServersBuffer}/>
        </div>
    );
};

export default StorageServers;
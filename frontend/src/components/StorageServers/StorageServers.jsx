import React, {useEffect, useState} from 'react';
import styles from "./style.module.css";
import StorageServersHeading from "../StorageServersHeading/StorageServersHeading";
import StorageServersList from "../StorageServersList/StorageServersList";
import csrfFetch from "../../utils/CsrfFetch";
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";
import AddStorageServerForm from "../AddStorageServerForm/AddStorageServerForm";
import DeleteStorageServerForm from "../DeleteStorageServerForm/DeleteStorageServerForm";
import AdminComponent from "../AdminComponent/AdminComponent";

const StorageServers = () => {
    const [storageServers, setStorageServers] = useState([]);
    const [backupServers, setBackupServers] = useState([]);
    const [selectedServersBuffer, setSelectedServersBuffer] = useState([]);
    const [isAddStorageServerFormHidden, setIsAddStorageServerFormHidden] = useState(true);
    const [isDeleteStorageServerFormHidden, setIsDeleteStorageServerFormHidden] = useState(true);

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

    async function refresh() {
        await getStorageServers();
        await getBackupServers();
    }

    function addStorageServer() {
        setIsAddStorageServerFormHidden(false);
    }

    function deleteStorageServer() {
        setIsDeleteStorageServerFormHidden(false);
    }

    return (
        <AdminComponent>
            <div className={styles.storageServers}>
                <AddStorageServerForm isHidden={isAddStorageServerFormHidden} setIsHidden={setIsAddStorageServerFormHidden}
                    onAdd={refresh}/>
                <DeleteStorageServerForm isHidden={isDeleteStorageServerFormHidden} setIsHidden={setIsDeleteStorageServerFormHidden}
                    selectedStorageServersBuffer={selectedServersBuffer} onDelete={refresh}/>
                <StorageServersHeading selectedServers={selectedServersBuffer} addServer={addStorageServer} deleteServer={deleteStorageServer}/>
                <StorageServersList storage={storageServers} backup={backupServers} serversBuffer={selectedServersBuffer}
                    setServersBuffer={setSelectedServersBuffer}/>
            </div>
        </AdminComponent>
    );
};

export default StorageServers;
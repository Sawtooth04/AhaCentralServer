import React, {useEffect, useState} from 'react';
import styles from './style.module.css'
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";
import csrfFetch from "../../utils/CsrfFetch";
import formatSpace from "../../utils/SpaceFormatter";
import RefreshButton from "../UI/RefreshButton/RefreshButton";

const Main = () => {
    const [general, setGeneral] = useState({});
    const [storageServers, setStorageServers] = useState([]);

    async function getGeneral() {
        let response = await (await csrfFetch(await CentralServerLinksProvider.getLink('central-server-general'))).json();
        setGeneral(response);
    }

    async function getStorageServers() {
        let response = await (await csrfFetch(await CentralServerLinksProvider.getLink('central-server-available-servers'))).json();
        setStorageServers(response.servers);
    }

    useEffect(() => {
        async function getCentralServerData() {
            await getGeneral();
            await getStorageServers();
        }

        void getCentralServerData();
    }, []);

    return (
        <div className={styles.main}>
            <h1 className={styles.mainHeading}>
                Общее
                <RefreshButton onClick={() => {console.log("click")}}/>
            </h1>
            <p className={styles.mainText}> Всего серверов хранения: {general.storageServersCount} </p>
            <p className={styles.mainText}> Всего бэкап-серверов: {general.backupServersCount} </p>
            <p className={styles.mainText}> Всего пользователей: {general.customersCount} </p>
            <p className={styles.mainText}> Всего чанков: {general.chunksCount} </p>
            <p className={styles.mainText}> Объём доступных данных: {formatSpace(general.free)} </p>
            <p className={styles.mainText}> Занято: {general.occupied ? general.occupied * 100 : 0}% </p>
            <h1 className={styles.mainHeading}> Доступные серверы хранения </h1>
            {storageServers.map(server => {
                return <p className={styles.mainText} key={server.storageServerID}> {server.name}: {server.address} </p>
            })}
        </div>
    );
};

export default Main;
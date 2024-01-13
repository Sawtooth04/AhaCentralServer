import React, {useEffect, useState} from 'react';
import styles from './style.module.css'
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";

const Main = () => {
    const [general, setGeneral] = useState({});

    useEffect(() => {
        async function getGeneral() {
            let response = await (await fetch(await CentralServerLinksProvider.getLink('central-server-general'))).json();
            setGeneral(response);
        }

        void getGeneral();
    }, []);

    return (
        <div className={styles.main}>
            <h1 className={styles.mainHeading}> Общее </h1>
            <p className={styles.mainText}> Всего серверов хранения: {general.storageServersCount} </p>
            <p className={styles.mainText}> Всего бэкап-серверов: {general.backupServersCount} </p>
            <p className={styles.mainText}> Всего пользователей: {general.customersCount} </p>
            <p className={styles.mainText}> Всего чанков: {general.chunksCount} </p>
            <p className={styles.mainText}> Объём доступных данных: 2345346 MB </p>
            <p className={styles.mainText}> Занято: 15% (Доступно 2412342352 MB) </p>
            <h1 className={styles.mainHeading}> Конфигурация центрального сервера </h1>
            <p className={styles.mainText}> Какая-то инфа </p>
            <p className={styles.mainText}> Какая-то инфа </p>
            <p className={styles.mainText}> Какая-то инфа </p>
            <p className={styles.mainText}> Какая-то инфа </p>
            <p className={styles.mainText}> Какая-то инфа </p>
            <h1 className={styles.mainHeading}> Доступные серверы хранения </h1>
            <p className={styles.mainText}> Test-server: 192.168.1.104:8090 </p>
            <p className={styles.mainText}> Test-server: 192.168.1.104:8090 </p>
            <p className={styles.mainText}> Test-server: 192.168.1.104:8090 </p>
            <p className={styles.mainText}> Test-server: 192.168.1.104:8090 </p>
            <p className={styles.mainText}> Test-server: 192.168.1.104:8090 </p>
        </div>
    );
};

export default Main;
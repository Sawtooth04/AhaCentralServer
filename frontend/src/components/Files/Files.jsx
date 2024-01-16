import React, {useCallback, useEffect, useState} from 'react';
import styles from './style.module.css'
import FilesPathPart from "../FilesPathPart/FilesPathPart";
import csrfFetch from "../../utils/CsrfFetch";
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";
import DirectoryItem from "../DirectoryItem/DirectoryItem";

const Files = () => {
    const [pathParts, setPathParts] = useState(['root']);
    const [directoryItems, setDirectoryItems] = useState([]);
    const buildPathCallback = useCallback(buildPath, [pathParts]);
    const getDirectoryItemsCallback = useCallback(getDirectoryItems, [buildPathCallback]);

    useEffect(() => {
        void getDirectoryItemsCallback(0);
    }, [getDirectoryItemsCallback]);

    function buildPath(index) {
        return pathParts.slice(0, index + 1).join('/');
    }

    async function getDirectoryItems(index) {
        let response = await csrfFetch(`${await CentralServerLinksProvider.getLink('files-get')}/${buildPathCallback(index)}`);
        setDirectoryItems((await response.json()).items);
    }

    return (
        <div className={styles.files}>
            <h1 className={styles.filesHeading}> Файлы </h1>
            <div className={styles.filesPathPartsWrapper}>
                {pathParts.map((part, index) =>
                    <FilesPathPart key={`${index}${part}`} label={part} index={index} onClick={getDirectoryItemsCallback}/>)}
            </div>
            <div className={styles.directoryItemsHeader}>
                <p className={styles.directoryItemsHeaderText}> Название </p>
                <p className={styles.directoryItemsHeaderText}> Размер </p>
                <p className={styles.directoryItemsHeaderText}> Формат </p>
                <p className={styles.directoryItemsHeaderText}> Загружен </p>
                <p className={styles.directoryItemsHeaderText}> Обновлен </p>
            </div>
            {directoryItems.map((item, index) => {
                return <DirectoryItem key={`${index}${item.name}`} item={item}/>
            })}
        </div>
    );
};

export default Files;
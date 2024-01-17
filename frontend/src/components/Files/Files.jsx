import React, {useCallback, useEffect, useRef, useState} from 'react';
import styles from './style.module.css'
import FilesPathPart from "../FilesPathPart/FilesPathPart";
import csrfFetch from "../../utils/CsrfFetch";
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";
import DirectoryItem from "../DirectoryItem/DirectoryItem";
import FilesButton from "../UI/FilesButton/FilesButton";

const Files = () => {
    const [pathParts, setPathParts] = useState(['root']);
    const [directoryItems, setDirectoryItems] = useState([]);
    const buildPathCallback = useCallback(buildPath, [pathParts]);
    const getDirectoryItemsCallback = useCallback(getDirectoryItems, [buildPathCallback]);
    const fileInputRef = useRef(null);

    useEffect(() => {
        void getDirectoryItemsCallback();
    }, [getDirectoryItemsCallback]);

    function buildPath() {
        return pathParts.join('/');
    }

    async function getDirectoryItems() {
        let response = await csrfFetch(`${await CentralServerLinksProvider.getLink('files-get')}/${buildPathCallback()}`);
        setDirectoryItems((await response.json()).items);
    }

    function onDirectoryClick(directory) {
        setPathParts([...pathParts, directory.name]);
    }

    function onPathPartClick(index) {
        setPathParts(pathParts.slice(0, index + 1));
    }

    function onPutFileClick() {
        fileInputRef.current.click();
    }

    async function putFile() {
        let formData = new FormData()
        formData.append('file', fileInputRef.current.files[0])
        formData.append('path', `/${pathParts.slice(1).join('/')}`)
        await csrfFetch(`${await CentralServerLinksProvider.getLink('file-put')}`, {
            method: 'put',
            headers: {'Content-Type': 'multipart/form-data'},
            body: formData
        });
    }

    return (
        <div className={styles.files}>
            <input type={"file"} hidden={true} ref={fileInputRef} onChange={putFile}/>
            <h1 className={styles.filesHeading}>
                Файлы
                <FilesButton src={'assets/icons/add-file.png'} hint={"Загрузить файл"} onClick={onPutFileClick} isActive={true}/>
                <FilesButton src={'assets/icons/add-folder.png'} hint={"Добавить папку"} onClick={null} isActive={true}/>
                <FilesButton src={'assets/icons/delete.png'} hint={"Удалить"} onClick={null} isActive={false}/>
                <FilesButton src={'assets/icons/change.png'} hint={"Переименовать"} onClick={null} isActive={false}/>
            </h1>
            <div className={styles.filesPathPartsWrapper}>
                {pathParts.map((part, index) =>
                    <FilesPathPart key={`${index}${part}`} label={part} index={index} onClick={onPathPartClick}/>)}
            </div>
            <div className={styles.directoryItemsHeader}>
                <p className={styles.directoryItemsHeaderText}> Название </p>
                <p className={styles.directoryItemsHeaderText}> Размер </p>
                <p className={styles.directoryItemsHeaderText}> Формат </p>
                <p className={styles.directoryItemsHeaderText}> Загружен </p>
                <p className={styles.directoryItemsHeaderText}> Обновлен </p>
            </div>
            {directoryItems.map((item, index) => {
                return <DirectoryItem key={`${index}${item.name}`} item={item} onClick={item.isFile ? null : onDirectoryClick}/>
            })}
        </div>
    );
};

export default Files;
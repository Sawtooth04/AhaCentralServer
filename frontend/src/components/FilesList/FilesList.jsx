import React from 'react';
import styles from "./style.module.css";
import DirectoryItem from "../DirectoryItem/DirectoryItem";

const FilesList = ({ items, setPathParts, filesBuffer, setFilesBuffer, pathParts }) => {

    function onDirectoryClick(directory) {
        setPathParts([...pathParts, directory.name]);
    }

    function onFileClick(file) {
        let index = filesBuffer.findIndex(f => f.name === file.name);
        if (index === -1)
            setFilesBuffer([...filesBuffer, file]);
        else {
            filesBuffer.splice(index, 1);
            setFilesBuffer([...filesBuffer]);
        }

    }

    return (
        <div className={styles.filesList}>
            <div className={styles.directoryItemsHeader}>
                <p className={styles.directoryItemsHeaderText}> Название </p>
                <p className={styles.directoryItemsHeaderText}> Размер </p>
                <p className={styles.directoryItemsHeaderText}> Формат </p>
                <p className={styles.directoryItemsHeaderText}> Загружен </p>
                <p className={styles.directoryItemsHeaderText}> Обновлен </p>
            </div>
            <div className={styles.files}>
                {items.map((item, index) => {
                    return <DirectoryItem key={`${index}${item.name}`} item={item} onClick={item.isFile ? onFileClick : onDirectoryClick}
                        isSelected={typeof(filesBuffer.find(i => i.name === item.name)) !== 'undefined'}/>
                })}
            </div>
        </div>
    );
};

export default FilesList;
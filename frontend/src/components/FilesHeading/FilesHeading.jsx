import React from 'react';
import styles from "./style.module.css";
import HintButton from "../UI/HintButton/HintButton";

const FilesHeading = ({ putFile, addDirectory, deleteFiles, renameFile, replaceFiles, downloadFiles, changePermissions, selectedFilesBuffer, items }) => {
    return (
        <h1 className={styles.filesHeading}>
            Файлы
            <HintButton src={'assets/icons/add-file.png'} hint={"Загрузить файл"} onClick={putFile} isActive={true}/>
            <HintButton src={'assets/icons/add-folder.png'} hint={"Добавить папку"} onClick={addDirectory} isActive={items.length > 0}/>
            <HintButton src={'assets/icons/delete.png'} hint={"Удалить"} onClick={deleteFiles} isActive={selectedFilesBuffer.length > 0}/>
            <HintButton src={'assets/icons/change.png'} hint={"Переименовать"} onClick={renameFile} isActive={selectedFilesBuffer.length === 1}/>
            <HintButton src={'assets/icons/replace-file.png'} hint={"Переместить"} onClick={replaceFiles} isActive={selectedFilesBuffer.length > 0}/>
            <HintButton src={'assets/icons/download-file.png'} hint={"Скачать"} onClick={downloadFiles} isActive={selectedFilesBuffer.length > 0}/>
            <HintButton src={'assets/icons/permissions.png'} hint={"Права"} onClick={changePermissions} isActive={selectedFilesBuffer.length === 1}/>
        </h1>
    );
};

export default FilesHeading;
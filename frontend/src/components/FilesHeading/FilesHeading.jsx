import React from 'react';
import styles from "./style.module.css";
import FilesButton from "../UI/FilesButton/FilesButton";

const FilesHeading = ({ putFile, addDirectory, deleteFiles, renameFile, replaceFiles, selectedFilesBuffer, items }) => {
    return (
        <h1 className={styles.filesHeading}>
            Файлы
            <FilesButton src={'assets/icons/add-file.png'} hint={"Загрузить файл"} onClick={putFile} isActive={true}/>
            <FilesButton src={'assets/icons/add-folder.png'} hint={"Добавить папку"} onClick={addDirectory} isActive={items.length > 0}/>
            <FilesButton src={'assets/icons/delete.png'} hint={"Удалить"} onClick={deleteFiles} isActive={selectedFilesBuffer.length > 0}/>
            <FilesButton src={'assets/icons/change.png'} hint={"Переименовать"} onClick={renameFile} isActive={selectedFilesBuffer.length === 1}/>
            <FilesButton src={'assets/icons/replace-file.png'} hint={"Переместить"} onClick={replaceFiles} isActive={selectedFilesBuffer.length > 0}/>
        </h1>
    );
};

export default FilesHeading;
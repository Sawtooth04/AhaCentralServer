import React, {useEffect, useState} from 'react';
import styles from './style.module.css'
import DirectoryItem from "../DirectoryItem/DirectoryItem";
import csrfFetch from "../../utils/CsrfFetch";
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";
import PopUpForm from "../UI/PopUpForm/PopUpForm";
import FilesPathPart from "../FilesPathPart/FilesPathPart";
import Button from "../UI/Button/Button";
import CsrfFetch from "../../utils/CsrfFetch";

const ReplaceFileForm = ({ isHidden, setIsHidden, files, currentPath, onReplace }) => {
    const [pathParts, setPathParts] = useState(['root']);
    const [directoryItems, setDirectoryItems] = useState([]);

    useEffect(() => {
        function buildPath() {
            return pathParts.join('/');
        }

        async function getDirectoryItems() {
            let response = await csrfFetch(`${await CentralServerLinksProvider.getLink('file-directories-get')}/${buildPath()}`);
            setDirectoryItems((await response.json()).items);
        }

        if (!isHidden)
            void getDirectoryItems();
    }, [pathParts, isHidden]);

    function onPathPartClick(index) {
        setPathParts(pathParts.slice(0, index + 1));
    }

    function onDirectoryClick(directory) {
        setPathParts([...pathParts, directory.name]);
    }

    async function replaceFile(file, path, filePath) {
        console.log(file);
        await CsrfFetch(`${await CentralServerLinksProvider.getLink("file-patch")}/${filePath}/${file.name}`, {
            method: 'PATCH',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify([{
                'op': 'replace',
                'path': '/path',
                'value': path
            }])
        });
    }

    async function replaceFiles() {
        let path = pathParts.join('/');

        for (let file of files)
            await replaceFile(file, path, currentPath);
        await onReplace();
        setIsHidden(true);
    }

    return (
        <div className={styles.replaceFileForm}>
            <PopUpForm header={'Перемещение файлов'} isHidden={isHidden}>
                <div className={styles.pathPartsWrapper}>
                    {pathParts.map((part, index) =>
                        <FilesPathPart key={`${index}${part}`} label={part} index={index} onClick={onPathPartClick}/>)}
                </div>
                <p className={styles.directoryItemsHeaderText}> Название </p>
                <div className={styles.directoriesList}>
                    {directoryItems.map((item, index) => {
                        return <DirectoryItem key={`${index}${item.name}`} item={item} onClick={onDirectoryClick} isSelected={false}/>
                    })}
                </div>
                <Button text={'Переместить'} onClick={replaceFiles}/>
                <Button text={'Отменить'} onClick={() => setIsHidden(true)}/>
            </PopUpForm>
        </div>
    );
};

export default ReplaceFileForm;
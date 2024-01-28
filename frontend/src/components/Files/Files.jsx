import React, {useCallback, useEffect, useRef, useState} from 'react';
import styles from './style.module.css'
import FilesPathPart from "../FilesPathPart/FilesPathPart";
import csrfFetch from "../../utils/CsrfFetch";
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";
import CreateDirectoryForm from "../CreateDirectoryForm/CreateDirectoryForm";
import FilesList from "../FilesList/FilesList";
import FilesHeading from "../FilesHeading/FilesHeading";
import DeleteFileForm from "../DeleteFileForm/DeleteFileForm";
import RenameFileForm from "../RenameFileForm/RenameFileForm";
import ReplaceFileForm from "../ReplaceFileForm/ReplaceFileForm";
import DownloadFileForm from "../DownloadFileForm/DownloadFileForm";
import PutFileForm from "../PutFileForm/PutFileForm";
import ChangeFilePermissionsForm from "../ChangeFilePermissionsForm/ChangeFilePermissionsForm";
import FileRightForbiddenForm from "../FileRightForbiddenForm/FileRightForbiddenForm";

const Files = ({ uploads, setUploads }) => {
    const [pathParts, setPathParts] = useState(['root']);
    const [directoryItems, setDirectoryItems] = useState([]);
    const [selectedFilesBuffer, setSelectedFilesBuffer] = useState([]);
    const [isCreateDirectoryFormHidden, setIsCreateDirectoryFormHidden] = useState(true);
    const [isDeleteFormHidden, setIsDeleteFormHidden] = useState(true);
    const [isRenameFileFormHidden, setIsRenameFileFormHidden] = useState(true);
    const [isReplaceFileFormHidden, setIsReplaceFileFormHidden] = useState(true);
    const [isDownloadFileFormHidden, setIsDownloadFileFormHidden] = useState(true);
    const [isPutFileFormHidden, setIsPutFileFormHidden] = useState(true);
    const [isChangeFilePermissionsFormHidden, setIsChangeFilePermissionsFormHidden] = useState(true);
    const [isFileOperationForbiddenFormHidden, setIsFileOperationForbiddenFormHidden] = useState(true);

    const fileInputRef = useRef(null);

    const buildPathCallback = useCallback(buildPath, [pathParts]);
    const getDirectoryItemsCallback = useCallback(getDirectoryItems, [buildPathCallback]);

    useEffect(() => {
        void getDirectoryItemsCallback();
        setSelectedFilesBuffer([]);
    }, [getDirectoryItemsCallback]);

    function buildPath() {
        return pathParts.join('/');
    }

    async function getDirectoryItems() {
        let response = await csrfFetch(`${await CentralServerLinksProvider.getLink('file-all-get')}/${buildPathCallback()}/`);
        setDirectoryItems((await response.json()).items);
    }

    function onPathPartClick(index) {
        setPathParts(pathParts.slice(0, index + 1));
    }

    function onPutFileClick() {
        fileInputRef.current.click();
    }

    async function putFile() {
        setIsPutFileFormHidden(false);
    }

    function onAddDirectoryClick() {
        setIsCreateDirectoryFormHidden(false);
    }

    function deleteSelectedFiles() {
        setIsDeleteFormHidden(false);
    }

    function renameSelectedFile() {
        setIsRenameFileFormHidden(false);
    }

    function replaceSelectedFiles() {
        setIsReplaceFileFormHidden(false);
    }

    function downloadSelectedFiles() {
        setIsDownloadFileFormHidden(false);
    }

    function changePermissionsOfSelectedFile() {
        setIsChangeFilePermissionsFormHidden(false);
    }

    function forbidden() {
        setIsFileOperationForbiddenFormHidden(false);
    }

    async function refresh() {
        await getDirectoryItemsCallback();
        setSelectedFilesBuffer([]);
    }

    return (
        <div className={styles.files}>
            <DeleteFileForm isHidden={isDeleteFormHidden} setIsHidden={setIsDeleteFormHidden} selectedFilesBuffer={selectedFilesBuffer}
                buildPath={buildPathCallback} onDelete={refresh} forbidden={forbidden}/>
            <CreateDirectoryForm setPathParts={setPathParts} pathParts={pathParts} isHidden={isCreateDirectoryFormHidden}
                setIsHidden={setIsCreateDirectoryFormHidden}/>
            <RenameFileForm isHidden={isRenameFileFormHidden} buildPath={buildPathCallback} file={selectedFilesBuffer[0]}
                setIsHidden = {setIsRenameFileFormHidden} onRename={refresh} forbidden={forbidden}/>
            <ReplaceFileForm isHidden={isReplaceFileFormHidden} setIsHidden={setIsReplaceFileFormHidden} files={selectedFilesBuffer}
                onReplace={refresh} currentPath={buildPathCallback()} forbidden={forbidden}/>
            <DownloadFileForm isHidden={isDownloadFileFormHidden} setIsHidden={setIsDownloadFileFormHidden} buildPath={buildPathCallback}
                selectedFilesBuffer={selectedFilesBuffer}/>
            <PutFileForm isHidden={isPutFileFormHidden} setIsHidden={setIsPutFileFormHidden} buildPath={buildPathCallback}
                file={fileInputRef.current?.files[0]} uploads={uploads} setUploads={setUploads}/>
            <ChangeFilePermissionsForm isHidden={isChangeFilePermissionsFormHidden} setIsHidden={setIsChangeFilePermissionsFormHidden}
                buildPath={buildPathCallback} file={selectedFilesBuffer[0]} forbidden={forbidden}/>
            <FileRightForbiddenForm isHidden={isFileOperationForbiddenFormHidden} setIsHidden={setIsFileOperationForbiddenFormHidden}/>
            <input type={"file"} hidden={true} ref={fileInputRef} onChange={putFile}/>
            <FilesHeading putFile={onPutFileClick} addDirectory={onAddDirectoryClick} deleteFiles={deleteSelectedFiles} items={directoryItems}
                renameFile={renameSelectedFile} replaceFiles={replaceSelectedFiles} selectedFilesBuffer={selectedFilesBuffer}
                downloadFiles={downloadSelectedFiles} changePermissions={changePermissionsOfSelectedFile}/>
            <div className={styles.filesPathPartsWrapper}>
                {pathParts.map((part, index) =>
                    <FilesPathPart key={`${index}${part}`} label={part} index={index} onClick={onPathPartClick}/>)}
            </div>
            <FilesList items={directoryItems} pathParts={pathParts} setPathParts={setPathParts} filesBuffer={selectedFilesBuffer}
                setFilesBuffer={setSelectedFilesBuffer}/>
        </div>
    );
};

export default Files;
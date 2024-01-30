import React, {useCallback, useEffect, useState} from 'react';
import styles from "./style.module.css";
import buildCsrfXhr from "../../utils/CsrfXhr";
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";
import FilesButton from "../UI/FilesButton/FilesButton";

const FilesUploadsListItem = ({ item, onClick, deleteUpload }) => {
    const [isResultError, setIsResultError] = useState(false);
    const [progress, setProgress] = useState(0);
    const [request, setRequest] = useState(null);
    const onLoadCallback = useCallback(onLoad, [deleteUpload, item, request]);

    useEffect(() => {
        async function buildRequest() {
            let xhr = buildCsrfXhr('put', await CentralServerLinksProvider.getLink('file-put'))
            xhr.timeout = 0;
            setRequest(xhr);
        }
        
        void buildRequest();
    }, []);

    useEffect(() => {
        function onFirstLoad() {
            if (request.status === 403) {
                request.onload = onLoadCallback;
                request.send(item.formData);
            }
            else
                onLoadCallback();
        }

        if (request != null && !isResultError) {
            request.upload.onprogress = onProgress;
            request.onload = onFirstLoad;
            request.send(item.formData);
        }
    }, [item.formData, onLoadCallback, request]);

    useEffect(() => {
        if (request != null && progress >= 0) {
            request.onload = onLoadCallback;
            request.upload.onprogress = onProgress;
        }
    }, [onLoadCallback, progress, request]);

    function onProgress(event) {
        setProgress(event.loaded / event.total * 100);
    }

    async function onLoad() {
        if (request.status === 200 || request.status === 201) {
            await item.onSuccess();
            deleteUpload();
        }
        else
            setIsResultError(true);
    }

    function onDelete() {
        request.abort();
        deleteUpload();
    }

    return (
        <div className={styles.filesUploadsListItem}>
            <p className={styles.filesUploadsListItemText}> {item.name} </p>

            {
                isResultError ?
                    <p className={styles.filesUploadsListItemText}>
                        Произошла ошибка. Проверьте наличие активных серверов и объём доступной памяти.
                    </p> :
                progress !== 100 ?
                    <p className={styles.filesUploadsListItemText}> {`Загружено на ${Math.round(progress)}%`} </p> :
                    <p className={styles.filesUploadsListItemText}> {'Файл обрабатывается. Пожалуйста, подождите.'} </p>
            }
            <img className={styles.filesUploadsListItemImg} src={'assets/icons/remove-1.png'} alt={'Delete'} onClick={onDelete}/>
        </div>
    );
};

export default FilesUploadsListItem;
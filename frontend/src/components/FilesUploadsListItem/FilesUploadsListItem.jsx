import React, {useCallback, useEffect, useState} from 'react';
import styles from "./style.module.css";
import buildCsrfXhr from "../../utils/CsrfXhr";
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";

const FilesUploadsListItem = ({ item, onClick }) => {
    const [progress, setProgress] = useState(-1);
    const [request, setRequest] = useState(null);
    const onLoadCallback = useCallback(onLoad, [request]);

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

        if (request != null) {
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

    function onLoad() {
        if (request.status === 200 || request.status === 201)
            console.log('Namana');
        else
            console.log('ploxo');
    }

    return (
        <div className={styles.filesUploadsListItem} onClick={() => onClick(item)}>
            <p className={styles.filesUploadsListItemText}> {Math.round(progress)} </p>
            <p className={styles.filesUploadsListItemText}> {item.name} </p>
        </div>
    );
};

export default FilesUploadsListItem;
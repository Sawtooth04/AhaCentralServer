import React, {useEffect, useState} from 'react';
import styles from "./style.module.css";

const FilesUploadsListItem = ({ item, onClick }) => {
    const [progress, setProgress] = useState(0);
    const [request, setRequest] = useState(null);

    useEffect(() => {

    }, []);

    return (
        <div className={styles.filesUploadsListItem} onClick={() => onClick(item)}>
            <p className={styles.filesUploadsListItemText}> {item.name} </p>
        </div>
    );
};

export default FilesUploadsListItem;
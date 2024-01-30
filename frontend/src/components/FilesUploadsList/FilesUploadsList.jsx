import React from 'react';
import styles from "./style.module.css";
import FilesUploadsListItem from "../FilesUploadsListItem/FilesUploadsListItem";

const FilesUploadsList = ({ items, deleteUpload }) => {
    return (
        <div className={styles.filesUploadsList}>
            <div className={styles.uploadsHeading}>
                <p className={styles.uploadsHeadingText}> Название </p>
                <p className={styles.uploadsHeadingText}> Статус </p>
            </div>
            <div className={styles.uploads}>
                {items.map((item, index) => {
                    let deleteUploadsListItem = () => deleteUpload(index);
                    return <FilesUploadsListItem key={`${index}${item.name}`} item={item} deleteUpload={deleteUploadsListItem}
                        onClick={deleteUploadsListItem}/>
                })}
            </div>
        </div>
    );
};

export default FilesUploadsList;
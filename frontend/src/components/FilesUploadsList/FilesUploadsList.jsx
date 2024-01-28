import React from 'react';
import styles from "./style.module.css";
import FilesUploadsListItem from "../FilesUploadsListItem/FilesUploadsListItem";

const FilesUploadsList = ({ items }) => {
    return (
        <div className={styles.filesUploadsList}>
            <div className={styles.uploads}>
                {items.map((item, index) => {
                    return <FilesUploadsListItem key={`${index}${item.name}`} item={item} onClick={null}/>
                })}
            </div>
        </div>
    );
};

export default FilesUploadsList;
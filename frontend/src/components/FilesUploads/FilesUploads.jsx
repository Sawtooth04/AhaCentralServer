import React, {useContext} from 'react';
import styles from './style.module.css';
import UploadsContext from '../../contexts/UploadsContext';
import FilesUploadsList from "../FilesUploadsList/FilesUploadsList";

const FilesUploads = ({ setUploads }) => {
    const uploads = useContext(UploadsContext);

    function deleteUpload(index) {
        uploads.splice(index, 1);
        setUploads([...uploads]);
    }

    return (
        <div className={styles.filesUploads}>
            <h1 className={styles.filesUploadsHeading}> Загрузки </h1>
            <FilesUploadsList items={uploads} deleteUpload={deleteUpload}/>
        </div>
    );
};

export default FilesUploads;
import React, {useContext} from 'react';
import styles from './style.module.css';
import UploadsContext from '../../contexts/UploadsContext';
import FilesUploadsList from "../FilesUploadsList/FilesUploadsList";

const FilesUploads = ({ }) => {
    const uploads = useContext(UploadsContext);

    return (
        <div className={styles.filesUploads}>
            <h1 className={styles.filesUploadsHeading}> Загрузки </h1>
            <FilesUploadsList items={uploads}/>
        </div>
    );
};

export default FilesUploads;
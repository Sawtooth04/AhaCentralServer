import React from 'react';
import styles from './style.module.css'

const FilesPathPart = ({ label, index, onClick }) => {
    return (
        <p className={styles.filePartPath} onClick={() => {onClick(index)}}>
            {index !== 0 ? `/${label}` : label}
        </p>
    );
};

export default FilesPathPart;
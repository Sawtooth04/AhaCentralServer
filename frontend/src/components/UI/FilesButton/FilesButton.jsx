import React from 'react';
import styles from './style.module.css';

const FilesButton = ({ src, hint, onClick, isActive }) => {
    return (
        <div className={isActive ? styles.filesButton : styles.filesButtonDisable} onClick={onClick}>
            <img className={styles.filesButtonImage} src={src} alt={"Icon"}/>
            <p className={styles.filesButtonHint}> {hint} </p>
        </div>
    );
};

export default FilesButton;
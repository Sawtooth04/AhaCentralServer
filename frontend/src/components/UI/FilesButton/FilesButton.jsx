import React from 'react';
import styles from './style.module.css';

const FilesButton = ({ src, hint, onClick, isActive }) => {
    function onClickCallback() {
        if (isActive)
            onClick();
    }

    return (
        <div className={isActive ? styles.filesButton : styles.filesButtonDisable} onClick={onClickCallback}>
            <img className={styles.filesButtonImage} src={src} alt={"Icon"}/>
            <p className={styles.filesButtonHint}> {hint} </p>
        </div>
    );
};

export default FilesButton;